/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.service.variable;

import io.jmix.core.Sort;
import io.openbpm.control.entity.filter.VariableFilter;
import io.openbpm.control.service.ItemListLoadContext;

/**
 * A context that contains the following options to load process variable instances:
 * <ul>
 *     <li>Pagination options: first and max results</li>
 *     <li>Sort options: property and direction to sort. Supported properties: "name".</li>
 *     <li>Filtering options</li>
 * </ul>
 * <p>
 * Example: load first 50 process variable instances for the specified process instance and sort them by name
 * <pre>
 *     VariableFilter filter = metadata.create(VariableFilter.class);
 *     filter.setProcessInstanceId(processInstanceId);
 *
 *     VariableLoadContext context = new VariableLoadContext()
 *      .setFirstResult(0)
 *      .setMaxResults(50)
 *      .setFilter(filter)
 *      .setSort(Sort.by(Sort.Direction.ASC, "name"));
 * </pre>
 *
 * @see VariableFilter
 * @see io.jmix.core.Metadata
 * @see Sort
 */
public class VariableLoadContext extends ItemListLoadContext<VariableFilter> {

    /**
     * Sets a process variable instance filter.
     *
     * @param filter an instance of {@link VariableFilter}
     * @return current context
     */
    public VariableLoadContext setFilter(VariableFilter filter) {
        this.filter = filter;
        return this;
    }

    /**
     * Sets a first result number.
     *
     * @param firstResult first result
     * @return current context
     */
    public VariableLoadContext setFirstResult(Integer firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    /**
     * Sets a max number of results.
     *
     * @param maxResults max results
     * @return current context
     */
    public VariableLoadContext setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    /**
     * Sets sort options.
     *
     * @param sort sort options
     * @return current context
     */
    public VariableLoadContext setSort(Sort sort) {
        this.sort = sort;
        return this;
    }
}
