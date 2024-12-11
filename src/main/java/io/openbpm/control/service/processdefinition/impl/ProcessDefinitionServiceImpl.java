/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.service.processdefinition.impl;

import feign.FeignException;
import feign.utils.ExceptionUtils;
import io.jmix.core.Sort;
import io.openbpm.control.entity.filter.ProcessDefinitionFilter;
import io.openbpm.control.entity.processdefinition.ProcessDefinitionData;
import io.openbpm.control.exception.EngineNotSelectedException;
import io.openbpm.control.mapper.ProcessDefinitionMapper;
import io.openbpm.control.service.processdefinition.ProcessDefinitionLoadContext;
import io.openbpm.control.service.processdefinition.ProcessDefinitionService;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.camunda.community.rest.client.api.ProcessDefinitionApiClient;
import org.camunda.community.rest.client.model.ProcessDefinitionDiagramDto;
import org.camunda.community.rest.client.model.ProcessDefinitionSuspensionStateDto;
import org.camunda.community.rest.impl.RemoteRepositoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.List;

import static io.openbpm.control.util.QueryUtils.addDefinitionFilters;
import static io.openbpm.control.util.QueryUtils.addDefinitionSort;

@Service("control_ProcessDefinitionService")
@Slf4j
public class ProcessDefinitionServiceImpl implements ProcessDefinitionService {
    protected final RemoteRepositoryService remoteRepositoryService;
    protected final ProcessDefinitionMapper processDefinitionMapper;
    protected final ProcessDefinitionApiClient processDefinitionApiClient;

    public ProcessDefinitionServiceImpl(RemoteRepositoryService remoteRepositoryService,
                                        ProcessDefinitionMapper processDefinitionMapper,
                                        ProcessDefinitionApiClient processDefinitionApiClient) {
        this.remoteRepositoryService = remoteRepositoryService;
        this.processDefinitionMapper = processDefinitionMapper;
        this.processDefinitionApiClient = processDefinitionApiClient;
    }

    @Override
    public List<ProcessDefinitionData> findLatestVersions() {
        try {
            return remoteRepositoryService.createProcessDefinitionQuery()
                    .orderByProcessDefinitionVersion()
                    .asc()
                    .latestVersion()
                    .list()
                    .stream()
                    .map(processDefinitionMapper::fromProcessDefinitionModel)
                    .toList();
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            if (rootCause instanceof EngineNotSelectedException) {
                log.warn("Unable to load latest versions of process definitions because BPM engine not selected");
                return List.of();
            }
            throw e;
        }
    }


    @Override
    public List<ProcessDefinitionData> findAllByKey(String processDefinitionKey) {
        return remoteRepositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionVersion()
                .asc()
                .processDefinitionKey(processDefinitionKey)
                .list()
                .stream()
                .map(processDefinitionMapper::fromProcessDefinitionModel)
                .toList();
    }

    @Override
    public List<ProcessDefinitionData> findAll(ProcessDefinitionLoadContext context) {
        try {
            ProcessDefinitionQuery processDefinitionQuery = createProcessDefinitionQuery(context.getFilter(), context.getSort());

            List<ProcessDefinition> processDefinitions;
            if (context.getFirstResult() != null && context.getMaxResults() != null) {
                processDefinitions = processDefinitionQuery.listPage(context.getFirstResult(), context.getMaxResults());
            } else {
                processDefinitions = processDefinitionQuery.list();
            }

            return processDefinitions
                    .stream()
                    .map(processDefinitionMapper::fromProcessDefinitionModel)
                    .toList();
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            if (rootCause instanceof EngineNotSelectedException) {
                log.warn("Unable to load process definitions because BPM engine not selected");
                return List.of();
            }
            if (rootCause instanceof ConnectException) {
                log.error("Unable to load process definitions because of connection error: ", e);
                return List.of();
            }
            throw e;
        }
    }

    @Override
    public long getCount(@Nullable ProcessDefinitionFilter filter) {
        ProcessDefinitionQuery processDefinitionQuery = createProcessDefinitionQuery(filter, null);
        return processDefinitionQuery.count();
    }

    @Override
    public ProcessDefinitionData getById(String processDefinitionId) {
        try {
            ProcessDefinition processDefinition = remoteRepositoryService.createProcessDefinitionQuery()
                    .processDefinitionId(processDefinitionId)
                    .singleResult();
            return processDefinitionMapper.fromProcessDefinitionModel(processDefinition);
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            if (rootCause instanceof EngineNotSelectedException) {
                log.warn("Unable to load process definition by id '{}' because BPM engine not selected", processDefinitionId);
                return null;
            }
            if (rootCause instanceof ConnectException) {
                log.error("Unable load process definition by id '{}' because of connection error: ", processDefinitionId, e);
                return null;
            }
            throw e;
        }
    }

    @Override
    public String getBpmnXml(String processDefinitionId) {
        try {
            ResponseEntity<ProcessDefinitionDiagramDto> processDefinitionBpmn20Xml = processDefinitionApiClient.getProcessDefinitionBpmn20Xml(processDefinitionId);
            if (processDefinitionBpmn20Xml.getStatusCode().is2xxSuccessful() && processDefinitionBpmn20Xml.getBody() != null) {
                return processDefinitionBpmn20Xml.getBody().getBpmn20Xml();
            }
            return null;
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            if (rootCause instanceof EngineNotSelectedException) {
                log.warn("Unable to load process definition XML by id '{}' because BPM engine not selected", processDefinitionId);
                return null;
            }

            if (rootCause instanceof ConnectException) {
                log.error("Unable load process definition XML by id '{}' because of connection error: ", processDefinitionId, e);
                return null;
            }

            if (rootCause instanceof FeignException feignException && feignException.status() == 404) {
                log.warn("Unable to load process definition XML by id '{}' because process does not exist", processDefinitionId);
                return null;
            }

            throw e;
        }
    }

    @Override
    public void activateById(String processDefinitionId, boolean activateBelongsInstances) {
        suspendOrActivateProcessDefinitionById(processDefinitionId, activateBelongsInstances, false);
    }

    @Override
    public void activateAllVersionsByKey(String processDefinitionKey, boolean activateBelongsInstances) {
        suspendOrActivateProcessDefinitionsByKey(processDefinitionKey, activateBelongsInstances, false);
    }

    @Override
    public void suspendById(String processDefinitionId, boolean suspendBelongsInstances) {
        suspendOrActivateProcessDefinitionById(processDefinitionId, suspendBelongsInstances, true);
    }

    @Override
    public void suspendAllVersionsByKey(String processDefinitionKey, boolean suspendBelongsInstances) {
        suspendOrActivateProcessDefinitionsByKey(processDefinitionKey, suspendBelongsInstances, true);
    }

    @Override
    public void deleteAllVersionsByKey(String processDefinitionKey, boolean deleteAllRelatedInstances) {
        boolean hasNextVersion = deleteProcessVersionByKey(processDefinitionKey, deleteAllRelatedInstances);
        if (hasNextVersion) {
            do {
                hasNextVersion = deleteProcessVersionByKey(processDefinitionKey, deleteAllRelatedInstances);
            } while (hasNextVersion);
        }
    }

    /**
     * Delete last process definition version by the provided key.
     *
     * @param processDefinitionKey      process definition key
     * @param deleteAllRelatedInstances where remove or not related process instances
     * @return true if version for the provided key does not exist (response = 404), false - otherwise.
     */
    protected boolean deleteProcessVersionByKey(String processDefinitionKey, boolean deleteAllRelatedInstances) {
        try {
            processDefinitionApiClient.deleteProcessDefinitionsByKey(processDefinitionKey, deleteAllRelatedInstances, null, null);
            return true;
        } catch (Exception e) {
            if (e instanceof FeignException feignException && feignException.status() == 404) {
                return false;
            }
            throw e;
        }
    }

    @Override
    public void deleteById(String processDefinitionId, boolean deleteAllRelatedInstances) {
        remoteRepositoryService.deleteProcessDefinition(processDefinitionId, deleteAllRelatedInstances);
    }

    @SuppressWarnings("ConstantValue")
    protected void suspendOrActivateProcessDefinitionById(String processDefinitionId,
                                                          boolean cascadeOperationToBelongsInstances,
                                                          boolean suspended) {
        ProcessDefinitionSuspensionStateDto suspendedCommand = new ProcessDefinitionSuspensionStateDto()
                .suspended(suspended);
        if (cascadeOperationToBelongsInstances) {
            suspendedCommand.includeProcessInstances(cascadeOperationToBelongsInstances);
        }
        processDefinitionApiClient.updateProcessDefinitionSuspensionStateById(processDefinitionId, suspendedCommand);
    }

    @SuppressWarnings("ConstantValue")
    protected void suspendOrActivateProcessDefinitionsByKey(String processDefinitionKey,
                                                            boolean cascadeOperationToBelongsInstances,
                                                            boolean suspended) {
        ProcessDefinitionSuspensionStateDto suspendedCommand = new ProcessDefinitionSuspensionStateDto()
                .processDefinitionKey(processDefinitionKey)
                .suspended(suspended);
        if (cascadeOperationToBelongsInstances) {
            suspendedCommand.includeProcessInstances(cascadeOperationToBelongsInstances);
        }
        processDefinitionApiClient.updateProcessDefinitionSuspensionState(suspendedCommand);
    }

    protected ProcessDefinitionQuery createProcessDefinitionQuery(@Nullable ProcessDefinitionFilter filter, @Nullable Sort sort) {
        ProcessDefinitionQuery processDefinitionQuery = remoteRepositoryService.createProcessDefinitionQuery();

        addDefinitionFilters(processDefinitionQuery, filter);
        addDefinitionSort(processDefinitionQuery, sort);
        return processDefinitionQuery;
    }
}
