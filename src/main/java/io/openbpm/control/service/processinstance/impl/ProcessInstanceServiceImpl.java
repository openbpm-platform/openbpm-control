/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.service.processinstance.impl;

import feign.FeignException;
import feign.utils.ExceptionUtils;
import io.jmix.core.Sort;
import io.openbpm.control.entity.filter.ProcessInstanceFilter;
import io.openbpm.control.entity.processinstance.ProcessInstanceData;
import io.openbpm.control.entity.processinstance.RuntimeProcessInstanceData;
import io.openbpm.control.entity.variable.VariableInstanceData;
import io.openbpm.control.exception.EngineNotSelectedException;
import io.openbpm.control.mapper.ProcessInstanceMapper;
import io.openbpm.control.service.processinstance.ProcessInstanceLoadContext;
import io.openbpm.control.service.processinstance.ProcessInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.community.rest.client.api.HistoryApiClient;
import org.camunda.community.rest.client.api.ProcessInstanceApiClient;
import org.camunda.community.rest.client.model.CountResultDto;
import org.camunda.community.rest.client.model.HistoricProcessInstanceDto;
import org.camunda.community.rest.client.model.HistoricProcessInstanceQueryDto;
import org.camunda.community.rest.client.model.ProcessInstanceSuspensionStateAsyncDto;
import org.camunda.community.rest.impl.RemoteHistoryService;
import org.camunda.community.rest.impl.RemoteRuntimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static io.openbpm.control.service.variable.VariableUtils.createVariableMap;
import static io.openbpm.control.util.EngineRestUtils.getCountResult;
import static io.openbpm.control.util.QueryUtils.*;

@Slf4j
@Service("control_ProcessInstanceService")
public class ProcessInstanceServiceImpl implements ProcessInstanceService {
    protected final RemoteRuntimeService remoteRuntimeService;
    protected final RemoteHistoryService historyService;
    protected final ProcessInstanceApiClient processInstanceApiClient;
    protected final HistoryApiClient historyApiClient;
    protected final ProcessInstanceMapper processInstanceMapper;

    public ProcessInstanceServiceImpl(RemoteRuntimeService remoteRuntimeService,
                                      RemoteHistoryService historyService,
                                      ProcessInstanceApiClient processInstanceApiClient,
                                      HistoryApiClient historyApiClient,
                                      ProcessInstanceMapper processInstanceMapper) {
        this.remoteRuntimeService = remoteRuntimeService;
        this.historyService = historyService;
        this.processInstanceApiClient = processInstanceApiClient;
        this.historyApiClient = historyApiClient;
        this.processInstanceMapper = processInstanceMapper;
    }

    @Override
    public List<ProcessInstanceData> findAllHistoricInstances(ProcessInstanceLoadContext context) {
        try {
            HistoricProcessInstanceQueryDto queryDto = createHistoryProcessInstanceQueryDto(context.getFilter(), context.getSort());
            ResponseEntity<List<HistoricProcessInstanceDto>> responseEntity = historyApiClient.queryHistoricProcessInstances(context.getFirstResult(), context.getMaxResults(),
                    queryDto);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                List<HistoricProcessInstanceDto> processInstances = Optional.ofNullable(responseEntity.getBody()).orElse(List.of());

                List<String> instancesWithIncidents = context.isLoadIncidents() ? findRunningInstancesWithIncidents(processInstances) : List.of();

                return processInstances
                        .stream()
                        .map(source -> {
                            ProcessInstanceData processInstanceData = processInstanceMapper.fromHistoryProcessInstanceDto(source);
                            processInstanceData.setHasIncidents(instancesWithIncidents.contains(source.getId()));
                            return processInstanceData;
                        })
                        .toList();
            } else {
                log.error("Unable to load historic process instances, status code: {}", responseEntity.getStatusCode());
                return List.of();
            }
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            if (rootCause instanceof EngineNotSelectedException) {
                log.warn("Unable to load historic process instances because BPM engine not selected");
                return List.of();
            }
            if (rootCause instanceof ConnectException) {
                log.error("Unable to load historic process instances because of connection error: ", e);
                return List.of();
            }
            throw e;
        }

    }

    @Override
    public List<RuntimeProcessInstanceData> findAllRuntimeInstances(ProcessInstanceLoadContext loadContext) {
        try {
            ProcessInstanceQuery processInstanceQuery = remoteRuntimeService.createProcessInstanceQuery();

            addRuntimeFilters(processInstanceQuery, loadContext.getFilter());
            addRuntimeSort(processInstanceQuery, loadContext.getSort());

            List<ProcessInstance> processInstances;
            if (loadContext.getFirstResult() != null && loadContext.getMaxResults() != null) {
                processInstances = processInstanceQuery.listPage(loadContext.getFirstResult(), loadContext.getMaxResults());
            } else {
                processInstances = processInstanceQuery.list();
            }

            List<String> instancesWithIncidents = loadContext.isLoadIncidents() ? findRuntimeInstancesWithIncidents(processInstances) : List.of();

            return processInstances
                    .stream()
                    .map(source -> {
                        RuntimeProcessInstanceData processInstanceData = processInstanceMapper.toRuntimeProcessInstanceData(source);
                        processInstanceData.setHasIncidents(instancesWithIncidents.contains(source.getId()));
                        return processInstanceData;
                    })
                    .toList();

        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            if (rootCause instanceof EngineNotSelectedException) {
                log.warn("Unable to load runtime process instances because BPM engine not selected");
                return List.of();
            }
            if (rootCause instanceof ConnectException) {
                log.error("Unable to load runtime process instances because of connection error: ", e);
                return List.of();
            }
            throw e;
        }
    }

    @Override
    public long getHistoricInstancesCount(ProcessInstanceFilter filter) {
        HistoricProcessInstanceQueryDto queryDto = createHistoryProcessInstanceQueryDto(filter, null);
        try {
            ResponseEntity<CountResultDto> responseEntity = historyApiClient.queryHistoricProcessInstancesCount(queryDto);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return getCountResult(responseEntity.getBody());
            }
            log.warn("Unable to load historic process instances count, status code: '{}'", responseEntity.getStatusCode().value());
            return 0;

        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            if (rootCause instanceof EngineNotSelectedException) {
                log.warn("Unable to load historic process instances count because BPM engine not selected");
                return 0;
            }
            if (rootCause instanceof ConnectException) {
                log.error("Unable to load historic process instances count because of connection error: ", e);
                return 0;
            }
            throw e;
        }
    }

    @Override
    public long getRuntimeInstancesCount(ProcessInstanceFilter filter) {
        try {
            ProcessInstanceQuery processInstanceQuery = remoteRuntimeService.createProcessInstanceQuery();

            addRuntimeFilters(processInstanceQuery, filter);

            return processInstanceQuery.count();
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            if (rootCause instanceof EngineNotSelectedException) {
                log.warn("Unable to load runtime process instances count because BPM engine not selected");
                return 0;
            }
            if (rootCause instanceof ConnectException) {
                log.error("Unable to load runtime process instances count because of connection error: ", e);
                return 0;
            }
            throw e;
        }
    }

    @Override
    public ProcessInstanceData getProcessInstanceById(String processInstanceId) {
        try {
            ResponseEntity<HistoricProcessInstanceDto> responseEntity = historyApiClient.getHistoricProcessInstance(processInstanceId);

            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.hasBody()) {
                return processInstanceMapper.fromHistoryProcessInstanceDto(responseEntity.getBody());

            } else {
                log.warn("Unable to load historic process instance by id '{}', status code: '{}'", processInstanceId, responseEntity.getStatusCode().value());
                return null;
            }
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            if (rootCause instanceof EngineNotSelectedException) {
                log.warn("Unable to load process instance by id '{}' because BPM engine not selected", processInstanceId);
                return null;
            }
            if (rootCause instanceof ConnectException) {
                log.error("Unable to load process instance by id '{}' because of connection error: ", processInstanceId, e);
                return null;
            }
            if (rootCause instanceof FeignException feignException && feignException.status() == 404) {
                log.warn("Process instance by id '{}' not found: ", processInstanceId, e);
                return null;
            }
            throw e;
        }
    }

    @Override
    public ProcessInstanceData startProcessByDefinitionId(String processDefinitionId, Collection<VariableInstanceData> variableInstances) {
        try {
            VariableMap variables = createVariableMap(variableInstances);
            ProcessInstance processInstance = remoteRuntimeService.startProcessInstanceById(processDefinitionId, variables);

            return mapFromModelWithDetails(processInstance);
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            if (rootCause instanceof EngineNotSelectedException) {
                log.warn("Unable to load start process by definitionId by id '{}' because BPM engine not selected", processDefinitionId);
                return null;
            }
            throw e;
        }
    }

    @Override
    public void suspendById(String processInstanceId) {
        try {
            remoteRuntimeService.suspendProcessInstanceById(processInstanceId);
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            if (rootCause instanceof EngineNotSelectedException) {
                log.warn("Unable to load suspend instance by id '{}' because BPM engine not selected", processInstanceId);
            }
            throw e;
        }
    }

    @Override
    public void activateById(String processInstanceId) {
        try {
            remoteRuntimeService.activateProcessInstanceById(processInstanceId);
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            if (rootCause instanceof EngineNotSelectedException) {
                log.warn("Unable to load activate instance by id '{}' because BPM engine not selected", processInstanceId);
            }
            throw e;
        }
    }

    @Override
    public void terminateById(String processInstanceId) {
        try {
            remoteRuntimeService.deleteProcessInstance(processInstanceId, null, false, true);
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            if (rootCause instanceof EngineNotSelectedException) {
                log.warn("Unable to load terminate instance by id '{}' because BPM engine not selected", processInstanceId);
            }
            throw e;
        }
    }

    @Override
    public void terminateByIdsAsync(List<String> processInstanceIds, @Nullable String reason) {
        try {
            remoteRuntimeService.deleteProcessInstancesAsync(processInstanceIds, reason);
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            if (rootCause instanceof EngineNotSelectedException) {
                log.warn("Unable to load terminate instances by ids '{}' because BPM engine not selected", processInstanceIds);
            }
            throw e;
        }
    }

    @Override
    public void activateByIdsAsync(List<String> processInstancesIds) {
        try {
            processInstanceApiClient.updateSuspensionStateAsyncOperation(
                    new ProcessInstanceSuspensionStateAsyncDto()
                            .processInstanceIds(processInstancesIds));
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            if (rootCause instanceof EngineNotSelectedException) {
                log.warn("Unable to load activate instances by ids '{}' because BPM engine not selected", processInstancesIds);
            }
            throw e;
        }
    }

    @Override
    public void suspendByIdsAsync(List<String> processInstancesIds) {
        try {
            processInstanceApiClient.updateSuspensionStateAsyncOperation(
                    new ProcessInstanceSuspensionStateAsyncDto()
                            .processInstanceIds(processInstancesIds)
                            .suspended(true));
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            if (rootCause instanceof EngineNotSelectedException) {
                log.warn("Unable to load suspend instances by ids '{}' because BPM engine not selected", processInstancesIds);
            }
            throw e;
        }
    }

    @Override
    public long getCountByProcessDefinitionId(String processDefinitionId) {
        ResponseEntity<CountResultDto> processInstancesCount = processInstanceApiClient.getProcessInstancesCount(
                null, null, null,
                null, processDefinitionId, null,
                null, null, null,
                null, null, null,
                null, null, null,
                null, null, null,
                null, null, null,
                null, null, null,
                null, null, null,
                null, null
        );
        if (processInstancesCount.getStatusCode().is2xxSuccessful()) {
            return getCountResult(processInstancesCount.getBody());
        }
        return -1;
    }

    @Override
    public long getCountByDeploymentId(String deploymentId) {
        ResponseEntity<CountResultDto> processInstancesCount = processInstanceApiClient.getProcessInstancesCount(
                null, null, null,
                null, null, null,
                null, null, deploymentId,
                null, null, null,
                null, null, null,
                null, null, null,
                null, null, null,
                null, null, null,
                null, null, null,
                null, null
        );
        CountResultDto countResultDto = processInstancesCount.getBody();
        if (processInstancesCount.getStatusCode().is2xxSuccessful() && countResultDto != null) {
            return countResultDto.getCount();
        }
        return -1;
    }

    @Override
    public long getCountByProcessDefinitionKey(String processDefinitionKey) {
        ResponseEntity<CountResultDto> processInstancesCount = processInstanceApiClient.getProcessInstancesCount(
                null, null, null,
                null, null, processDefinitionKey,
                null, null, null,
                null, null, null,
                null, null, null,
                null, null, null,
                null, null, null,
                null, null, null,
                null, null, null,
                null, null
        );
        if (processInstancesCount.getStatusCode().is2xxSuccessful()) {
            return getCountResult(processInstancesCount.getBody());
        }
        return -1;
    }

    protected HistoricProcessInstanceQueryDto createHistoryProcessInstanceQueryDto(@Nullable ProcessInstanceFilter filter, @Nullable Sort sort) {
        HistoricProcessInstanceQueryDto processInstanceQuery = new HistoricProcessInstanceQueryDto();

        addHistoryFilters(processInstanceQuery, filter);
        addHistorySort(processInstanceQuery, sort);

        return processInstanceQuery;
    }

    protected ProcessInstanceData mapFromModelWithDetails(ProcessInstance responseModel) {
        ProcessInstanceData processInstanceData = processInstanceMapper.fromProcessInstanceModel(responseModel);
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(responseModel.getProcessInstanceId())
                .singleResult();
        if (historicProcessInstance != null) {
            processInstanceData.setEndTime(historicProcessInstance.getEndTime());
            processInstanceData.setStartTime(historicProcessInstance.getStartTime());
            processInstanceData.setProcessDefinitionName(historicProcessInstance.getProcessDefinitionName());
            processInstanceData.setBusinessKey(historicProcessInstance.getBusinessKey());
            processInstanceData.setDeleteReason(historicProcessInstance.getDeleteReason());
            processInstanceData.setProcessDefinitionKey(historicProcessInstance.getProcessDefinitionKey());
            processInstanceData.setProcessDefinitionVersion(historicProcessInstance.getProcessDefinitionVersion());
        }
        return processInstanceData;
    }

    protected List<String> findRunningInstancesWithIncidents(List<HistoricProcessInstanceDto> processInstances) {
        Set<String> activeInstanceIds = processInstances
                .stream()
                .filter(historicProcessInstance -> historicProcessInstance.getEndTime() == null)
                .map(HistoricProcessInstanceDto::getId)
                .collect(Collectors.toSet());

        return loadInstancesWithIncidents(activeInstanceIds);
    }


    protected List<String> findRuntimeInstancesWithIncidents(List<ProcessInstance> processInstances) {
        Set<String> activeInstanceIds = processInstances.stream()
                .map(Execution::getId)
                .collect(Collectors.toSet());
        return loadInstancesWithIncidents(activeInstanceIds);
    }

    protected List<String> loadInstancesWithIncidents(Set<String> activeInstanceIds) {
        List<ProcessInstance> instancesWithIncidents = remoteRuntimeService.createProcessInstanceQuery()
                .withIncident()
                .processInstanceIds(activeInstanceIds)
                .listPage(0, activeInstanceIds.size());

        return instancesWithIncidents.stream().map(ProcessInstance::getId).toList();
    }
}
