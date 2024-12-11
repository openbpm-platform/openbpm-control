/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.service;

import io.jmix.core.Sort;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Base class of context used to load a list of items from the BPM engine.
 * Context contains includes the information such as pagination, sorting and filtering options.
 *
 * @param <V> a class containing filtering options
 */
@Getter
@Setter
@Accessors(chain = true)
public abstract class ItemListLoadContext<V> {
    protected V filter;
    protected Integer firstResult;
    protected Integer maxResults;
    protected Sort sort;
}
