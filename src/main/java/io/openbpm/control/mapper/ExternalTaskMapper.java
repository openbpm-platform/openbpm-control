/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.mapper;

import io.jmix.core.Metadata;
import io.openbpm.control.entity.ExternalTaskData;
import org.camunda.bpm.engine.externaltask.ExternalTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ExternalTaskMapper {
    @Autowired
    Metadata metadata;

    @Mapping(target = "externalTaskId", source = "id")
    @Mapping(target = "id", ignore = true)
    public abstract ExternalTaskData fromExternalTask(ExternalTask source);

    ExternalTaskData targetClassFactory() {
        return metadata.create(ExternalTaskData.class);
    }
}
