/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.mapper;

import io.openbpm.control.entity.activity.ActivityShortData;
import io.openbpm.control.entity.activity.ActivityInstanceTreeItem;
import io.jmix.core.Metadata;
import org.camunda.community.rest.client.model.ActivityInstanceDto;
import org.camunda.community.rest.client.model.HistoricActivityInstanceDto;
import org.camunda.community.rest.client.model.TransitionInstanceDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Date;

@Mapper(componentModel = "spring")
public abstract class ActivityMapper {

    @Autowired
    Metadata metadata;

    @Mapping(target = "internalId", source = "id")
    @Mapping(target = "id", ignore = true)
    public abstract ActivityShortData fromActivityDto(HistoricActivityInstanceDto source);

    @Mapping(target = "activityInstanceId", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parentActivityInstance", ignore = true)
    @Mapping(target = "transition", constant = "false")
    public abstract ActivityInstanceTreeItem fromRuntimeActivityDto(ActivityInstanceDto source);

    ActivityInstanceTreeItem targetTreeItemClassFactory() {
        return metadata.create(ActivityInstanceTreeItem.class);
    }

    @Mapping(target = "activityInstanceId", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parentActivityInstance", ignore = true)
    @Mapping(target = "transition", constant = "true")
    public abstract ActivityInstanceTreeItem fromRuntimeTransitionDto(TransitionInstanceDto source);

    ActivityShortData targetClassFactory() {
        return metadata.create(ActivityShortData.class);
    }

    Date map(OffsetDateTime value) {
        if (value == null) {
            return null;
        }
        Instant instant = value.toInstant();
        return Date.from(instant);
    }
}
