/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.mapper;

import io.jmix.core.Metadata;
import io.flowset.control.entity.deployment.DeploymentResource;
import org.camunda.community.rest.client.model.DeploymentResourceDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class DeploymentResourceMapper {
    @Autowired
    Metadata metadata;

    @Mapping(target = "resourceId", source = "id")
    @Mapping(target = "id", ignore = true)
    public abstract DeploymentResource fromDto(DeploymentResourceDto source);

    DeploymentResource targetClassFactory() {
        return metadata.create(DeploymentResource.class);
    }
}
