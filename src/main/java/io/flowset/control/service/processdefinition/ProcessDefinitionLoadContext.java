/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.service.processdefinition;

import io.jmix.core.Sort;
import io.flowset.control.entity.filter.ProcessDefinitionFilter;
import io.flowset.control.service.ItemListLoadContext;

/**
 * A context that contains the following options to load incidents:
 * <ul>
 *     <li>Pagination options: first and max results</li>
 *     <li>Sort options: property and direction to sort. Supported properties:  "name", "key", "version".</li>
 *     <li>Filtering options</li>
 * </ul>
 * <p>
 * Example: load first 50 process definitions considering latest versions only and sort them by key
 * <pre>
 *     ProcessDefinitionFilter filter = metadata.create(ProcessDefinitionFilter.class);
 *     filter.setLatestVersionOnly(true);
 *
 *     ProcessDefinitionLoadContext context = new ProcessDefinitionLoadContext()
 *      .setFirstResult(0)
 *      .setMaxResults(50)
 *      .setFilter(filter)
 *      .setSort(Sort.by(Sort.Direction.ASC, "key"));
 * </pre>
 *
 * @see ProcessDefinitionFilter
 * @see io.jmix.core.Metadata
 * @see Sort
 */
public class ProcessDefinitionLoadContext extends ItemListLoadContext<ProcessDefinitionFilter> {

    /**
     * Sets a process definition filter.
     *
     * @param filter an instance of {@link ProcessDefinitionFilter}
     * @return current context
     */
    public ProcessDefinitionLoadContext setFilter(ProcessDefinitionFilter filter) {
        this.filter = filter;
        return this;
    }

    /**
     * Sets a first result number.
     *
     * @param firstResult first result
     * @return current context
     */
    public ProcessDefinitionLoadContext setFirstResult(Integer firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    /**
     * Sets a max number of results.
     *
     * @param maxResults max results
     * @return current context
     */
    public ProcessDefinitionLoadContext setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    /**
     * Sets sort options.
     *
     * @param sort sort options
     * @return current context
     */
    public ProcessDefinitionLoadContext setSort(Sort sort) {
        this.sort = sort;
        return this;
    }
}
