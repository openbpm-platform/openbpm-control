/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.mapper;

import io.jmix.core.Metadata;
import io.openbpm.control.entity.decisiondefinition.DecisionDefinitionData;
import org.camunda.bpm.engine.repository.DecisionDefinition;
import org.camunda.community.rest.client.model.DecisionDefinitionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class DecisionDefinitionMapper {

    @Autowired
    Metadata metadata;

    @Mapping(target = "decisionDefinitionId", source = "id")
    public abstract DecisionDefinitionData fromDecisionDefinitionModel(DecisionDefinition source);

    @Mapping(target = "resourceName", source = "resource")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "decisionDefinitionId", source = "id")
    public abstract DecisionDefinitionData fromDecisionDefinitionDto(DecisionDefinitionDto source);

    DecisionDefinitionData targetClassFactory() {
        return metadata.create(DecisionDefinitionData.class);
    }
}
