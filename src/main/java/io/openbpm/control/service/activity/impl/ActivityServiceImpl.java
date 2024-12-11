/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.service.activity.impl;

import io.jmix.core.Sort;
import io.openbpm.control.entity.activity.ActivityInstanceTreeItem;
import io.openbpm.control.entity.activity.ActivityShortData;
import io.openbpm.control.entity.activity.HistoricActivityInstanceData;
import io.openbpm.control.entity.filter.ActivityFilter;
import io.openbpm.control.mapper.ActivityMapper;
import io.openbpm.control.mapper.HistoryActivityMapper;
import io.openbpm.control.service.activity.ActivityLoadContext;
import io.openbpm.control.service.activity.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.camunda.community.rest.client.api.HistoryApiClient;
import org.camunda.community.rest.client.api.ProcessInstanceApiClient;
import org.camunda.community.rest.client.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("control_ActivityService")
@Slf4j
public class ActivityServiceImpl implements ActivityService {
    protected final HistoryActivityMapper historyActivityMapper;
    protected final HistoryApiClient historyApiClient;
    protected final ActivityMapper activityMapper;
    protected final ProcessInstanceApiClient processInstanceApi;

    public ActivityServiceImpl(HistoryActivityMapper historyActivityMapper,
                               HistoryApiClient historyApiClient,
                               ActivityMapper activityMapper,
                               ProcessInstanceApiClient processInstanceApi) {
        this.historyActivityMapper = historyActivityMapper;
        this.historyApiClient = historyApiClient;
        this.activityMapper = activityMapper;
        this.processInstanceApi = processInstanceApi;
    }

    @Override
    public List<ActivityShortData> findRunningActivities(String processInstanceId) {
        HistoricActivityInstanceQueryDto queryDto = new HistoricActivityInstanceQueryDto()
                .processInstanceId(processInstanceId)
                .unfinished(true);

        ResponseEntity<List<HistoricActivityInstanceDto>> response = historyApiClient.queryHistoricActivityInstances(null, null,
                queryDto);
        if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
            return response.getBody()
                    .stream()
                    .map(activityMapper::fromActivityDto)
                    .toList();
        }

        log.error("Error while getting running history activities by process instance id {},  response code {}", processInstanceId, response.getStatusCode());
        return List.of();
    }

    @Override
    public List<ActivityInstanceTreeItem> getActivityInstancesTree(String processInstanceId) {
        List<ActivityInstanceTreeItem> items = new ArrayList<>();
        ResponseEntity<ActivityInstanceDto> response = processInstanceApi.getActivityInstanceTree(processInstanceId);
        if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
            ActivityInstanceDto rootActivityInstance = response.getBody();
            ActivityInstanceTreeItem rootTreeItem = cresteActivityInstanceTreeItem(null, rootActivityInstance);
            items.add(rootTreeItem);

            addChildItems(rootActivityInstance, rootTreeItem, items);
            addTransitionItems(rootActivityInstance, rootTreeItem, items);
        } else {
            log.error("Error while getting activity instances tree by process instance id {},  response code {}", processInstanceId, response.getStatusCode());
        }

        return items;
    }


    @Override
    public List<ActivityShortData> findFinishedActivities(String processInstanceId) {
        HistoricActivityInstanceQueryDto queryDto = new HistoricActivityInstanceQueryDto()
                .processInstanceId(processInstanceId)
                .finished(true);

        ResponseEntity<List<HistoricActivityInstanceDto>> response = historyApiClient.queryHistoricActivityInstances(null, null,
                queryDto);
        if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
            return response.getBody()
                    .stream()
                    .map(activityMapper::fromActivityDto)
                    .toList();
        }
        log.error("Error while getting finished history activities by process instance id {}, response code {}", processInstanceId, response.getStatusCode());
        return List.of();
    }

    @Override
    public List<HistoricActivityInstanceData> findAllHistoryActivities(ActivityLoadContext loadContext) {
        HistoricActivityInstanceQueryDto queryDto = createActivityQueryDto(loadContext.getFilter());
        addSortOptions(loadContext.getSort(), queryDto);

        ResponseEntity<List<HistoricActivityInstanceDto>> response = historyApiClient.queryHistoricActivityInstances(loadContext.getFirstResult(), loadContext.getMaxResults(),
                queryDto);
        if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
            return response.getBody()
                    .stream()
                    .map(historyActivityMapper::fromHistoryActivityDto)
                    .toList();
        }

        log.error("Error while getting history activities, {}", response.getStatusCode());
        return List.of();
    }

    @Override
    public long getHistoryActivitiesCount(@Nullable ActivityFilter filter) {
        HistoricActivityInstanceQueryDto queryDto = createActivityQueryDto(filter);
        ResponseEntity<CountResultDto> response = historyApiClient.queryHistoricActivityInstancesCount(queryDto);
        if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
            Long count = response.getBody().getCount();
            return count != null ? count : 0;
        }

        log.error("Error while getting history activities count, {}", response.getStatusCode());
        return 0;
    }

    @Override
    @Nullable
    public HistoricActivityInstanceData findById(String activityInstanceId) {
        ResponseEntity<HistoricActivityInstanceDto> response = historyApiClient.getHistoricActivityInstance(activityInstanceId);
        if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
            return historyActivityMapper.fromHistoryActivityDto(response.getBody());
        }
        log.error("Error while getting history activity by id {}, status code: {}", activityInstanceId, response.getStatusCode());
        return null;
    }

    protected HistoricActivityInstanceQueryDto createActivityQueryDto(@Nullable ActivityFilter filter) {
        HistoricActivityInstanceQueryDto queryDto = new HistoricActivityInstanceQueryDto();

        if (filter != null) {
            queryDto.setProcessInstanceId(filter.getProcessInstanceId());
        }

        return queryDto;
    }

    protected void addSortOptions(Sort sort, HistoricActivityInstanceQueryDto queryDto) {
        if (sort != null) {
            List<HistoricActivityInstanceQueryDtoSortingInner> sortOptions = new ArrayList<>();
            for (Sort.Order order : sort.getOrders()) {
                HistoricActivityInstanceQueryDtoSortingInner sortOption = new HistoricActivityInstanceQueryDtoSortingInner();
                switch (order.getProperty()) {
                    case "activityId" ->
                            sortOption.setSortBy(HistoricActivityInstanceQueryDtoSortingInner.SortByEnum.ACTIVITYID);
                    case "activityType" ->
                            sortOption.setSortBy(HistoricActivityInstanceQueryDtoSortingInner.SortByEnum.ACTIVITYTYPE);
                    case "activityName" ->
                            sortOption.setSortBy(HistoricActivityInstanceQueryDtoSortingInner.SortByEnum.ACTIVITYNAME);
                    case "startTime" ->
                            sortOption.setSortBy(HistoricActivityInstanceQueryDtoSortingInner.SortByEnum.STARTTIME);
                    case "endTime" ->
                            sortOption.setSortBy(HistoricActivityInstanceQueryDtoSortingInner.SortByEnum.ENDTIME);
                }

                if (order.getDirection() == Sort.Direction.ASC) {
                    sortOption.setSortOrder(HistoricActivityInstanceQueryDtoSortingInner.SortOrderEnum.ASC);
                } else if (order.getDirection() == Sort.Direction.DESC) {
                    sortOption.setSortOrder(HistoricActivityInstanceQueryDtoSortingInner.SortOrderEnum.DESC);
                }

                if (sortOption.getSortBy() != null && sortOption.getSortOrder() != null) {
                    sortOptions.add(sortOption);
                }
            }

            queryDto.setSorting(sortOptions);
        }
    }

    protected void addChildItems(ActivityInstanceDto rootItem, ActivityInstanceTreeItem parentItem, List<ActivityInstanceTreeItem> resultItems) {
        if (rootItem == null || CollectionUtils.isEmpty(rootItem.getChildActivityInstances())) {
            return;
        }

        rootItem.getChildActivityInstances()
                .forEach(activityInstanceDto -> {
                    ActivityInstanceTreeItem activityInstanceTreeItem = cresteActivityInstanceTreeItem(parentItem, activityInstanceDto);
                    resultItems.add(activityInstanceTreeItem);

                    addChildItems(activityInstanceDto, activityInstanceTreeItem, resultItems);
                    addTransitionItems(activityInstanceDto, activityInstanceTreeItem, resultItems);
                });
    }

    protected ActivityInstanceTreeItem cresteActivityInstanceTreeItem(@Nullable ActivityInstanceTreeItem treeItem, ActivityInstanceDto activityInstanceDto) {
        ActivityInstanceTreeItem activityInstanceTreeItem = activityMapper.fromRuntimeActivityDto(activityInstanceDto);
        activityInstanceTreeItem.setParentActivityInstance(treeItem);
        return activityInstanceTreeItem;
    }

    protected void addTransitionItems(ActivityInstanceDto rootItem, ActivityInstanceTreeItem treeItem, List<ActivityInstanceTreeItem> resultItems) {
        if (rootItem == null || CollectionUtils.isEmpty(rootItem.getChildTransitionInstances())) {
            return;
        }

        rootItem.getChildTransitionInstances()
                .forEach(transitionInstanceDto -> {
                    ActivityInstanceTreeItem activityInstanceTreeItem = activityMapper.fromRuntimeTransitionDto(transitionInstanceDto);
                    activityInstanceTreeItem.setParentActivityInstance(treeItem);
                    resultItems.add(activityInstanceTreeItem);
                });
    }
}
