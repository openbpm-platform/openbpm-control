/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.service.variable.impl;

import io.jmix.core.Sort;
import io.openbpm.control.entity.filter.VariableFilter;
import io.openbpm.control.entity.variable.HistoricVariableInstanceData;
import io.openbpm.control.entity.variable.VariableInstanceData;
import io.openbpm.control.mapper.VariableMapper;
import io.openbpm.control.service.variable.VariableLoadContext;
import io.openbpm.control.service.variable.VariableService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.camunda.community.rest.client.api.HistoryApiClient;
import org.camunda.community.rest.client.api.VariableInstanceApiClient;
import org.camunda.community.rest.client.model.*;
import org.camunda.community.rest.impl.RemoteRuntimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static io.openbpm.control.util.EngineRestUtils.getCountResult;

@Service("control_VariableService")
@Slf4j
public class VariableServiceImpl implements VariableService {
    protected final HistoryApiClient historyApiClient;
    protected final VariableMapper variableMapper;
    protected final RemoteRuntimeService remoteRuntimeService;
    protected final VariableInstanceApiClient variableInstanceApiClient;

    public VariableServiceImpl(HistoryApiClient historyApiClient,
                               VariableMapper variableMapper,
                               RemoteRuntimeService remoteRuntimeService,
                               VariableInstanceApiClient variableInstanceApiClient) {
        this.historyApiClient = historyApiClient;
        this.variableMapper = variableMapper;
        this.remoteRuntimeService = remoteRuntimeService;
        this.variableInstanceApiClient = variableInstanceApiClient;
    }

    @Override
    public List<VariableInstanceData> findRuntimeVariables(VariableLoadContext loadContext) {
        VariableInstanceQueryDto queryDto = createVariableInstanceQuery(loadContext.getFilter());

        Sort sort = loadContext.getSort();
        if (sort != null) {
            List<VariableInstanceQueryDtoSortingInner> sortDtoList = createSortDtoList(sort);
            queryDto.setSorting(sortDtoList);
        }

        ResponseEntity<List<VariableInstanceDto>> response = variableInstanceApiClient.queryVariableInstances(loadContext.getFirstResult(), loadContext.getMaxResults(),
                true, queryDto
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return CollectionUtils.emptyIfNull(response.getBody())
                    .stream()
                    .map(variableMapper::fromVariableDto)
                    .toList();
        }

        log.error("Error on loading runtime variables, query {}, status code {}", queryDto, response.getStatusCode());
        return List.of();
    }

    @Override
    public List<HistoricVariableInstanceData> findHistoricVariables(VariableLoadContext loadContext) {
        HistoricVariableInstanceQueryDto queryDto = createHistoricVariableQuery(loadContext.getFilter());

        Sort sort = loadContext.getSort();
        if (sort != null) {
            List<HistoricVariableInstanceQueryDtoSortingInner> sortDtoList = createHistoricVariableSortOptions(sort);
            queryDto.setSorting(sortDtoList);
        }

        ResponseEntity<List<HistoricVariableInstanceDto>> response = historyApiClient.queryHistoricVariableInstances(loadContext.getFirstResult(), loadContext.getMaxResults(), true, queryDto);

        if (response.getStatusCode().is2xxSuccessful()) {
            return CollectionUtils.emptyIfNull(response.getBody())
                    .stream()
                    .map(variableMapper::fromHistoricVariableInstanceDto)
                    .toList();
        }
        log.error("Error on loading historic variables, query {}, status code {}", queryDto, response.getStatusCode());
        return List.of();
    }


    @Override
    public long getRuntimeVariablesCount(@Nullable VariableFilter filter) {
        VariableInstanceQueryDto queryDto = createVariableInstanceQuery(filter);
        ResponseEntity<CountResultDto> response = variableInstanceApiClient.queryVariableInstancesCount(queryDto);
        if (response.getStatusCode().is2xxSuccessful()) {
            return getCountResult(response.getBody());
        }

        log.error("Error on loading runtime variables count, query {}, status code {}", queryDto, response.getStatusCode());
        return 0;
    }

    @Override
    public long getHistoricVariablesCount(@Nullable VariableFilter filter) {
        HistoricVariableInstanceQueryDto queryDto = createHistoricVariableQuery(filter);
        ResponseEntity<CountResultDto> response = historyApiClient.queryHistoricVariableInstancesCount(queryDto);
        if (response.getStatusCode().is2xxSuccessful()) {
            return getCountResult(response.getBody());
        }
        log.error("Error on loading historic variables count, query {}, status code {}", queryDto, response.getStatusCode());
        return 0;
    }

    @Override
    public void updateVariableLocal(VariableInstanceData variableInstanceData) {
        Objects.requireNonNull(variableInstanceData.getExecutionId(), "executionId can not be null");
        remoteRuntimeService.setVariableLocal(variableInstanceData.getExecutionId(), variableInstanceData.getName(), variableInstanceData.getValue());
    }

    @Override
    public HistoricVariableInstanceData findHistoricVariableById(String variableInstanceId) {
        ResponseEntity<HistoricVariableInstanceDto> response = historyApiClient.getHistoricVariableInstance(variableInstanceId, true);
        if (response.getStatusCode().is2xxSuccessful()) {
            HistoricVariableInstanceDto variableInstanceDto = response.getBody();
            return variableInstanceDto != null ? variableMapper.fromHistoricVariableInstanceDto(variableInstanceDto) : null;
        }
        log.error("Error on find historic variable, variable id {}, status code {}", variableInstanceId, response.getStatusCode());
        return null;
    }

    protected List<VariableInstanceQueryDtoSortingInner> createSortDtoList(Sort sort) {
        List<VariableInstanceQueryDtoSortingInner> sortDtoList = new ArrayList<>();
        for (Sort.Order order : sort.getOrders()) {
            String property = order.getProperty();

            VariableInstanceQueryDtoSortingInner sortDto = new VariableInstanceQueryDtoSortingInner();
            switch (property) {
                case "name" -> sortDto.setSortBy(VariableInstanceQueryDtoSortingInner.SortByEnum.VARIABLE_NAME);
                case "activityInstanceId" ->
                        sortDto.setSortBy(VariableInstanceQueryDtoSortingInner.SortByEnum.ACTIVITY_INSTANCE_ID);
                case "type" -> sortDto.setSortBy(VariableInstanceQueryDtoSortingInner.SortByEnum.VARIABLE_TYPE);
                default -> {
                }
            }

            if (order.getDirection() == Sort.Direction.ASC) {
                sortDto.setSortOrder(VariableInstanceQueryDtoSortingInner.SortOrderEnum.ASC);
            } else if (order.getDirection() == Sort.Direction.DESC) {
                sortDto.setSortOrder(VariableInstanceQueryDtoSortingInner.SortOrderEnum.DESC);
            }

            if (sortDto.getSortBy() != null && sortDto.getSortOrder() != null) {
                sortDtoList.add(sortDto);
            }
        }
        return sortDtoList;
    }

    protected HistoricVariableInstanceQueryDto createHistoricVariableQuery(@Nullable VariableFilter filter) {
        HistoricVariableInstanceQueryDto queryDto = new HistoricVariableInstanceQueryDto();

        if (filter != null) {
            if (StringUtils.isNotBlank(filter.getActivityInstanceId())) {
                queryDto.activityInstanceIdIn(List.of(filter.getActivityInstanceId()));
            }
            if (StringUtils.isNotBlank(filter.getProcessInstanceId())) {
                queryDto.processInstanceId(filter.getProcessInstanceId());
            }
        }

        return queryDto;
    }


    protected List<HistoricVariableInstanceQueryDtoSortingInner> createHistoricVariableSortOptions(Sort sort) {
        List<HistoricVariableInstanceQueryDtoSortingInner> sortDtoList = new ArrayList<>();
        for (Sort.Order order : sort.getOrders()) {
            String property = order.getProperty();

            HistoricVariableInstanceQueryDtoSortingInner sortDto = new HistoricVariableInstanceQueryDtoSortingInner();
            if (property.equals("name")) {
                sortDto.setSortBy(HistoricVariableInstanceQueryDtoSortingInner.SortByEnum.VARIABLE_NAME);
            }

            if (order.getDirection() == Sort.Direction.ASC) {
                sortDto.setSortOrder(HistoricVariableInstanceQueryDtoSortingInner.SortOrderEnum.ASC);
            } else if (order.getDirection() == Sort.Direction.DESC) {
                sortDto.setSortOrder(HistoricVariableInstanceQueryDtoSortingInner.SortOrderEnum.DESC);
            }

            if (sortDto.getSortBy() != null && sortDto.getSortOrder() != null) {
                sortDtoList.add(sortDto);
            }
        }
        return sortDtoList;
    }

    protected VariableInstanceQueryDto createVariableInstanceQuery(@Nullable VariableFilter filter) {
        VariableInstanceQueryDto variableInstanceQueryDto = new VariableInstanceQueryDto();

        if (filter != null) {
            if (StringUtils.isNotBlank(filter.getActivityInstanceId())) {
                variableInstanceQueryDto.activityInstanceIdIn(List.of(filter.getActivityInstanceId()));
            }
            if (StringUtils.isNotBlank(filter.getProcessInstanceId())) {
                variableInstanceQueryDto.addProcessInstanceIdInItem(filter.getProcessInstanceId());
            }
        }
        return variableInstanceQueryDto;
    }
}
