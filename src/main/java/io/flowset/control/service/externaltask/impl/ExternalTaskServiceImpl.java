/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.service.externaltask.impl;

import com.google.common.base.Strings;
import io.jmix.core.Sort;
import io.flowset.control.entity.ExternalTaskData;
import io.flowset.control.entity.filter.ExternalTaskFilter;
import io.flowset.control.mapper.ExternalTaskMapper;
import io.flowset.control.service.externaltask.ExternalTaskLoadContext;
import io.flowset.control.service.externaltask.ExternalTaskService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.externaltask.ExternalTask;
import org.camunda.bpm.engine.externaltask.ExternalTaskQuery;
import org.camunda.community.rest.client.api.ExternalTaskApiClient;
import org.camunda.community.rest.client.api.HistoryApiClient;
import org.camunda.community.rest.impl.RemoteExternalTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

import static io.flowset.control.util.QueryUtils.addIfStringNotEmpty;
import static io.flowset.control.util.QueryUtils.addSortDirection;

@Service("control_ExternalTaskService")
@Slf4j
public class ExternalTaskServiceImpl implements ExternalTaskService {
    protected final RemoteExternalTaskService remoteExternalTaskService;
    protected final HistoryApiClient historyApiClient;
    protected final ExternalTaskApiClient externalTaskApiClient;
    protected final ExternalTaskMapper externalTaskMapper;

    public ExternalTaskServiceImpl(RemoteExternalTaskService remoteExternalTaskService,
                                   HistoryApiClient historyApiClient,
                                   ExternalTaskApiClient externalTaskApiClient,
                                   ExternalTaskMapper externalTaskMapper) {
        this.remoteExternalTaskService = remoteExternalTaskService;
        this.historyApiClient = historyApiClient;
        this.externalTaskApiClient = externalTaskApiClient;
        this.externalTaskMapper = externalTaskMapper;
    }

    @Override
    public List<ExternalTaskData> findRunningTasks(ExternalTaskLoadContext loadContext) {
        ExternalTaskQuery externalTaskQuery = remoteExternalTaskService.createExternalTaskQuery();
        addSort(loadContext.getSort(), externalTaskQuery);
        addFilters(loadContext.getFilter(), externalTaskQuery);

        List<ExternalTask> externalTasks;
        if (loadContext.getFirstResult() != null && loadContext.getMaxResults() != null) {
            externalTasks = externalTaskQuery.listPage(loadContext.getFirstResult(), loadContext.getMaxResults());
        } else {
            externalTasks = externalTaskQuery.list();
        }
        return externalTasks
                .stream()
                .map(externalTaskMapper::fromExternalTask)
                .toList();
    }

    @Override
    public long getRunningTasksCount(@Nullable ExternalTaskFilter filter) {
        ExternalTaskQuery externalTaskQuery = remoteExternalTaskService.createExternalTaskQuery();
        addFilters(filter, externalTaskQuery);
        return externalTaskQuery.count();
    }

    @Override
    public void setRetries(String externalTaskId, int retries) {
        remoteExternalTaskService.setRetries(externalTaskId, retries);
        log.debug("Update retries count for external task {}. New value: {}", externalTaskId, retries);
    }

    @Override
    public void setRetriesAsync(List<String> externalTaskIds, int retries) {
        remoteExternalTaskService.setRetriesAsync(externalTaskIds, null, retries);
        log.debug("Async update retries count for external tasks {}. New value: {}", externalTaskIds, retries);
    }

    @Override
    public String getErrorDetails(String externalTaskId) {
        ResponseEntity<String> response = externalTaskApiClient.getExternalTaskErrorDetails(externalTaskId);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        return "";
    }

    @Override
    public String getHistoryErrorDetails(String externalTaskId) {
        ResponseEntity<String> response = historyApiClient.getErrorDetailsHistoricExternalTaskLog(externalTaskId);
        if (response.getStatusCode().is2xxSuccessful()) {
            return Strings.nullToEmpty(response.getBody());
        }
        return "";
    }

    protected void addFilters(@Nullable ExternalTaskFilter filter, ExternalTaskQuery externalTaskQuery) {
        if (filter != null) {
            addIfStringNotEmpty(filter.getProcessInstanceId(), externalTaskQuery::processInstanceId);
            addIfStringNotEmpty(filter.getActivityId(), externalTaskQuery::activityId);
        }
    }

    protected void addSort(Sort sort, ExternalTaskQuery externalTaskQuery) {
        if (sort != null) {
            for (Sort.Order order : sort.getOrders()) {
                String property = order.getProperty();
                boolean unknownValueUsed = false;
                switch (property) {
                    case "priority" -> externalTaskQuery.orderByPriority();
                    case "createTime" -> externalTaskQuery.orderByCreateTime();
                    case "id" -> externalTaskQuery.orderById();
                    default -> unknownValueUsed = true;
                }
                addSortDirection(externalTaskQuery, !unknownValueUsed, order);
            }

        }
    }
}