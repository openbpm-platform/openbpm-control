/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.mapper;

import io.openbpm.control.entity.job.JobData;
import io.openbpm.control.entity.job.JobDefinitionData;
import io.jmix.core.Metadata;
import org.camunda.community.rest.client.model.JobDefinitionDto;
import org.camunda.community.rest.client.model.JobDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Date;

@Mapper(componentModel = "spring")
public abstract class JobMapper {
    @Autowired
    Metadata metadata;

    @Mapping(target = "jobId", source = "id")
    @Mapping(target = "id", ignore = true)
    public abstract JobData fromJobDto(JobDto source);

    JobData targetClassFactory() {
        return metadata.create(JobData.class);
    }

    @Mapping(target = "jobDefinitionId", source = "id")
    @Mapping(target = "id", ignore = true)
    public abstract JobDefinitionData fromJobDefinitionDto(JobDefinitionDto source);

    JobDefinitionData jobDefinitionTargetClassFactory() {
        return metadata.create(JobDefinitionData.class);
    }

    Date map(OffsetDateTime value) {
        if (value == null) {
            return null;
        }
        Instant instant = value.toInstant();
        return Date.from(instant);
    }
}
