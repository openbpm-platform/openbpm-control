/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.service.activity;

import io.jmix.core.Sort;
import io.flowset.control.entity.filter.ActivityFilter;
import io.flowset.control.service.ItemListLoadContext;

/**
 * A context that contains the following options to load process activity instances:
 * <ul>
 *     <li>Pagination options: first and max results</li>
 *     <li>Sort options: property and direction to sort. Supported properties: "activityId", "activityType", "activityName", "startTime", "endTime".</li>
 *     <li>Filtering options</li>
 * </ul>
 * <p>
 * Example: load first 50 activity instances for the specified process instance and sort them by name
 * <pre>
 *     ActivityFilter filter = metadata.create(ActivityFilter.class);
 *     filter.setProcessInstanceId(processInstanceId);
 *
 *     ActivityLoadContext context = new ActivityLoadContext()
 *      .setFirstResult(0)
 *      .setMaxResults(50)
 *      .setFilter(filter)
 *      .setSort(Sort.by(Sort.Direction.ASC, "activityName"));
 * </pre>
 *
 * @see ActivityFilter
 * @see io.jmix.core.Metadata
 * @see Sort
 */
public class ActivityLoadContext extends ItemListLoadContext<ActivityFilter> {

    /**
     * Sets an activity filter.
     *
     * @param filter an instance of {@link ActivityFilter}
     * @return current context
     */
    public ActivityLoadContext setFilter(ActivityFilter filter) {
        this.filter = filter;
        return this;
    }

    /**
     * Sets a first result number.
     *
     * @param firstResult first result
     * @return current context
     */
    public ActivityLoadContext setFirstResult(Integer firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    /**
     * Sets a max number of results.
     *
     * @param maxResults max results
     * @return current context
     */
    public ActivityLoadContext setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    /**
     * Sets sort options.
     *
     * @param sort sort options
     * @return current context
     */
    public ActivityLoadContext setSort(Sort sort) {
        this.sort = sort;
        return this;
    }
}
