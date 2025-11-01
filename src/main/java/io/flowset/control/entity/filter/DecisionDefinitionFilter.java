/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.entity.filter;

import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.entity.annotation.JmixId;
import io.jmix.core.metamodel.annotation.InstanceName;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.flowset.control.entity.processdefinition.ProcessDefinitionState;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@JmixEntity
@Getter
@Setter
public class DecisionDefinitionFilter {

    @JmixGeneratedValue
    @JmixId
    protected UUID id;

    protected String keyLike;

    protected String key;

    protected List<String> idIn;

    @InstanceName
    protected String nameLike;

    protected Boolean latestVersionOnly = true;
}