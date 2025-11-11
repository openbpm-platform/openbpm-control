/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.service.externaltask;

import io.jmix.core.Sort;
import io.flowset.control.entity.filter.ExternalTaskFilter;
import io.flowset.control.service.ItemListLoadContext;

/**
 * A context that contains the following options to load external task instances:
 * <ul>
 *     <li>Pagination options: first and max results</li>
 *     <li>Sort options: property and direction to sort. Supported properties: "priority", "createTime", "id".</li>
 *     <li>Filtering options</li>
 * </ul>
 * <p>
 * Example: load first 50 external task instances for the specified process instance and sort them by priority
 * <pre>
 *
 *     ExternalTaskFilter filter = metadata.create(ExternalTaskFilter.class);
 *     filter.setProcessInstanceId(processInstanceId);
 *
 *     ExternalTaskLoadContext context = new ExternalTaskLoadContext()
 *      .setFirstResult(0)
 *      .setMaxResults(50)
 *      .setFilter(filter)
 *      .setSort(Sort.by(Sort.Direction.DESC, "priority"));
 * </pre>
 *
 * @see ExternalTaskFilter
 * @see io.jmix.core.Metadata
 * @see Sort
 */
public class ExternalTaskLoadContext extends ItemListLoadContext<ExternalTaskFilter> {
    /**
     * Sets an external task filter.
     *
     * @param filter an instance of {@link ExternalTaskFilter}
     * @return current context
     */
    public ExternalTaskLoadContext setFilter(ExternalTaskFilter filter) {
        this.filter = filter;
        return this;
    }

    /**
     * Sets a first result number.
     *
     * @param firstResult first result
     * @return current context
     */
    public ExternalTaskLoadContext setFirstResult(Integer firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    /**
     * Sets a max number of results.
     *
     * @param maxResults max results
     * @return current context
     */
    public ExternalTaskLoadContext setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    /**
     * Sets sort options.
     *
     * @param sort sort options
     * @return current context
     */
    public ExternalTaskLoadContext setSort(Sort sort) {
        this.sort = sort;
        return this;
    }
}
