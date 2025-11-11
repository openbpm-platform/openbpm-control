/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.mapper;

import io.flowset.control.entity.dashboard.IncidentStatistics;
import io.flowset.control.entity.dashboard.ProcessDefinitionStatistics;
import io.flowset.control.entity.processdefinition.ProcessDefinitionData;
import io.jmix.core.Metadata;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.community.rest.client.model.IncidentStatisticsResultDto;
import org.camunda.community.rest.client.model.ProcessDefinitionDto;
import org.camunda.community.rest.client.model.ProcessDefinitionStatisticsResultDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ProcessDefinitionMapper {

    @Autowired
    Metadata metadata;


    @Mapping(target = "processDefinitionId", source = "id")
    @Mapping(target = "suspended", expression = "java(isSuspended(source))")
    @Mapping(target = "startableInTaskList", source = "startableInTasklist")
    public abstract ProcessDefinitionData fromProcessDefinitionModel(ProcessDefinition source);

    ProcessDefinitionData targetClassFactory() {
        return metadata.create(ProcessDefinitionData.class);
    }

    @Mapping(target = "processDefinition", source = "definition")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "processDefinitionId", source = "id")
    @Mapping(target = "failedJobCount", source = "failedJobs")
    @Mapping(target = "instanceCount", source = "instances")
    public abstract ProcessDefinitionStatistics fromStatisticsResultDto(ProcessDefinitionStatisticsResultDto resultDto);


    @Mapping(target = "resourceName", source = "resource")
    @Mapping(target = "diagramResourceName", source = "resource")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "processDefinitionId", source = "id")
    @Mapping(target = "suspended", ignore = true)
    @Mapping(target = "startableInTaskList", source = "startableInTasklist")
    public abstract ProcessDefinitionData fromProcessDefinitionDto(ProcessDefinitionDto source);

    ProcessDefinitionStatistics processDefinitionStatisticsClassFactory() {
        return metadata.create(ProcessDefinitionStatistics.class);
    }

    @Mapping(target = "id", ignore = true)
    public abstract IncidentStatistics fromStatisticsResultDto(IncidentStatisticsResultDto resultDto);

    IncidentStatistics incidentStatisticsClassFactory() {
        return metadata.create(IncidentStatistics.class);
    }

    public boolean isSuspended(ProcessDefinition processDefinition) {
        return processDefinition.isSuspended();
    }
}
