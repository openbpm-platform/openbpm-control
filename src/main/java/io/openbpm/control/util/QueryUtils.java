/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.util;

import io.jmix.core.Sort;
import io.openbpm.control.entity.filter.ProcessDefinitionFilter;
import io.openbpm.control.entity.filter.ProcessInstanceFilter;
import io.openbpm.control.entity.processdefinition.ProcessDefinitionState;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.camunda.bpm.engine.query.Query;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.camunda.community.rest.client.model.HistoricProcessInstanceQueryDto;
import org.camunda.community.rest.client.model.HistoricProcessInstanceQueryDtoSortingInner;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class QueryUtils {

    public static final String INSTANCE_ID = "id";
    public static final String PROCESS_DEFINITION_ID = "processDefinitionId";
    public static final String PROCESS_DEFINITION_KEY = "processDefinitionKey";
    public static final String BUSINESS_KEY = "businessKey";
    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";


    public static void addRuntimeFilters(ProcessInstanceQuery processInstanceQuery, @Nullable ProcessInstanceFilter filter) {
        if (filter == null) {
            return;
        }

        addIfStringNotEmpty(filter.getProcessDefinitionId(), processInstanceQuery::processDefinitionId);
        addIfStringNotEmpty(filter.getProcessDefinitionKey(), processInstanceQuery::processDefinitionKey);
        wrapAndAddStringIfNotEmpty(filter.getBusinessKeyLike(), processInstanceQuery::processInstanceBusinessKeyLike);

        addIfTrue(filter.getActive(), processInstanceQuery::active);
        addIfTrue(filter.getSuspended(), processInstanceQuery::suspended);
        addIfTrue(filter.getWithIncidents(), processInstanceQuery::withIncident);

    }

    public static void addRuntimeSort(ProcessInstanceQuery processInstanceQuery, @Nullable Sort sort) {
        if (sort != null) {
            for (Sort.Order order : sort.getOrders()) {
                boolean unknownValueUsed = false;
                switch (order.getProperty()) {
                    case INSTANCE_ID -> processInstanceQuery.orderByProcessInstanceId();
                    case PROCESS_DEFINITION_ID -> processInstanceQuery.orderByProcessDefinitionId();
                    case PROCESS_DEFINITION_KEY -> processInstanceQuery.orderByProcessDefinitionKey();
                    case BUSINESS_KEY -> processInstanceQuery.orderByBusinessKey();
                    default -> unknownValueUsed = true;
                }

                addSortDirection(processInstanceQuery, !unknownValueUsed, order);
            }
        }
    }

    public static void addHistoryFilters(HistoricProcessInstanceQueryDto queryDto, @Nullable ProcessInstanceFilter filter) {
        if (filter == null) {
            return;
        }

        addIfStringNotEmpty(filter.getProcessDefinitionId(), queryDto::processDefinitionId);
        addIfStringNotEmpty(filter.getProcessDefinitionKey(), queryDto::processDefinitionKey);
        addIfStringNotEmpty(filter.getProcessInstanceId(), queryDto::processInstanceId);
        wrapAndAddStringIfNotEmpty(filter.getBusinessKeyLike(), queryDto::processInstanceBusinessKeyLike);


        addIfTrue(filter.getFinished(), () -> queryDto.setFinished(true));
        addIfTrue(filter.getActive(), () -> queryDto.setActive(true));
        addIfTrue(filter.getSuspended(), () -> queryDto.setSuspended(true));
        addIfTrue(filter.getUnfinished(), () -> queryDto.setUnfinished(true));
        addIfTrue(filter.getWithIncidents(), () -> {
            queryDto.withIncidents(true);
            queryDto.incidentStatus(HistoricProcessInstanceQueryDto.IncidentStatusEnum.OPEN);
        });

        addIfNotNull(filter.getStartTimeAfter(), queryDto::startedAfter);
        addIfNotNull(filter.getStartTimeBefore(), queryDto::startedBefore);
        addIfNotNull(filter.getEndTimeAfter(), queryDto::finishedAfter);
        addIfNotNull(filter.getEndTimeBefore(), queryDto::finishedBefore);
    }

    public static void addHistorySort(HistoricProcessInstanceQueryDto queryDto, @Nullable Sort sort) {
        if (sort != null) {
            List<HistoricProcessInstanceQueryDtoSortingInner> sortOptions = new ArrayList<>();
            for (Sort.Order order : sort.getOrders()) {
                HistoricProcessInstanceQueryDtoSortingInner.SortByEnum sortBy = switch (order.getProperty()) {
                    case INSTANCE_ID -> HistoricProcessInstanceQueryDtoSortingInner.SortByEnum.INSTANCEID;
                    case PROCESS_DEFINITION_ID -> HistoricProcessInstanceQueryDtoSortingInner.SortByEnum.DEFINITIONID;
                    case PROCESS_DEFINITION_KEY -> HistoricProcessInstanceQueryDtoSortingInner.SortByEnum.DEFINITIONKEY;
                    case BUSINESS_KEY -> HistoricProcessInstanceQueryDtoSortingInner.SortByEnum.BUSINESSKEY;
                    case START_TIME -> HistoricProcessInstanceQueryDtoSortingInner.SortByEnum.STARTTIME;
                    case END_TIME -> HistoricProcessInstanceQueryDtoSortingInner.SortByEnum.ENDTIME;
                    default -> null;
                };

                HistoricProcessInstanceQueryDtoSortingInner.SortOrderEnum sortOrder = null;
                Sort.Direction direction = order.getDirection();
                if (direction == Sort.Direction.ASC) {
                    sortOrder = HistoricProcessInstanceQueryDtoSortingInner.SortOrderEnum.ASC;
                } else if (direction == Sort.Direction.DESC) {
                    sortOrder = HistoricProcessInstanceQueryDtoSortingInner.SortOrderEnum.DESC;
                }

                if (sortBy != null && sortOrder != null) {
                    sortOptions.add(new HistoricProcessInstanceQueryDtoSortingInner()
                            .sortBy(sortBy)
                            .sortOrder(sortOrder));
                }
            }
            queryDto.setSorting(sortOptions);
        }
    }

    public static void addDefinitionFilters(ProcessDefinitionQuery processDefinitionQuery, @Nullable ProcessDefinitionFilter filter) {
        if (filter == null) {
            return;
        }

        wrapAndAddStringIfNotEmpty(filter.getKeyLike(), processDefinitionQuery::processDefinitionKeyLike);
        wrapAndAddStringIfNotEmpty(filter.getNameLike(), processDefinitionQuery::processDefinitionNameLike);

        addIfStringNotEmpty(filter.getKey(), processDefinitionQuery::processDefinitionKey);

        addCollectionIfNotEmpty(filter.getKeyIn(), processDefinitionQuery::processDefinitionKeyIn);
        addCollectionIfNotEmpty(filter.getIdIn(), processDefinitionQuery::processDefinitionIdIn);

        addIfTrue(filter.getLatestVersionOnly(), processDefinitionQuery::latestVersion);

        addIfTrue(filter.getState() == ProcessDefinitionState.ACTIVE, processDefinitionQuery::active);
        addIfTrue(filter.getState() == ProcessDefinitionState.SUSPENDED, processDefinitionQuery::suspended);
    }

    public static void addDefinitionSort(ProcessDefinitionQuery processDefinitionQuery, @Nullable Sort sort) {
        if (sort != null) {
            for (Sort.Order order : sort.getOrders()) {
                boolean unknownValueUsed = false;
                switch (order.getProperty()) {
                    case "name" -> processDefinitionQuery.orderByProcessDefinitionName();
                    case "key" -> processDefinitionQuery.orderByProcessDefinitionKey();
                    case "version" -> processDefinitionQuery.orderByProcessDefinitionVersion();
                    default -> unknownValueUsed = true;
                }

                addSortDirection(processDefinitionQuery, !unknownValueUsed, order);
            }
        }
    }


    public static void addIfStringNotEmpty(String filterValue, Consumer<String> filterValueConsumer) {
        if (StringUtils.hasText(filterValue)) {
            filterValueConsumer.accept(filterValue);
        }
    }

    public static void wrapAndAddStringIfNotEmpty(String filterValue, Consumer<String> filterValueConsumer) {
        if (StringUtils.hasText(filterValue)) {
            filterValueConsumer.accept("%" + filterValue + "%");
        }
    }

    public static void addIfTrue(Boolean filterValue, Runnable filterValueConsumer) {
        if (BooleanUtils.isTrue(filterValue)) {
            filterValueConsumer.run();
        }
    }

    public static <V> void addIfNotNull(V filterValue, Consumer<V> filterValueConsumer) {
        if (filterValue != null) {
            filterValueConsumer.accept(filterValue);
        }
    }

    public static void addCollectionIfNotEmpty(Collection<String> filterValues, Consumer<String> filterValueConsumer) {
        if (CollectionUtils.isNotEmpty(filterValues)) {
            filterValueConsumer.accept(String.join(",", filterValues));
        }
    }

    public static void addSortDirection(Query<?, ?> query, boolean propertyIsSet, Sort.Order order) {
        if (!propertyIsSet) {
            return;
        }
        Sort.Direction direction = order.getDirection();
        if (direction == Sort.Direction.ASC) {
            query.asc();
        } else if (direction == Sort.Direction.DESC) {
            query.desc();
        }
    }
}
