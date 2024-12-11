/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.mapper;

import io.jmix.core.Metadata;
import io.openbpm.control.entity.DeploymentData;
import org.camunda.community.rest.client.model.DeploymentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class DeploymentMapper {
    @Autowired
    Metadata metadata;

    @Mapping(target = "deploymentId", source = "id")
    @Mapping(target = "id", ignore = true)
    public abstract DeploymentData fromDto(DeploymentDto source);

    DeploymentData targetClassFactory() {
        return metadata.create(DeploymentData.class);
    }
}
