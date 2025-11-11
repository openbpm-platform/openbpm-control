/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.service.job;

import io.jmix.core.Sort;
import io.flowset.control.entity.filter.JobFilter;
import io.flowset.control.service.ItemListLoadContext;

/**
 * A context that contains the following options to load jobs:
 * <ul>
 *     <li>Pagination options: first and max results</li>
 *     <li>Sort options: property and direction to sort. Supported properties: "id", "retries", "dueDate", "priority".</li>
 *     <li>Filtering options</li>
 * </ul>
 * <p>
 * Example: load first 50 jobs for the specified process instance and sort them by due date
 * <pre>
 *
 *     JobFilter filter = metadata.create(JobFilter.class);
 *     filter.setProcessInstanceId(processInstanceId);
 *
 *     JobLoadContext context = new JobLoadContext()
 *      .setFirstResult(0)
 *      .setMaxResults(50)
 *      .setFilter(filter)
 *      .setSort(Sort.by(Sort.Direction.DESC, "dueDate"));
 * </pre>
 *
 * @see JobFilter
 * @see io.jmix.core.Metadata
 * @see Sort
 */
public class JobLoadContext extends ItemListLoadContext<JobFilter> {

    /**
     * Sets a job filter.
     *
     * @param filter an instance of {@link JobFilter}
     * @return current context
     */
    public JobLoadContext setFilter(JobFilter filter) {
        this.filter = filter;
        return this;
    }

    /**
     * Sets a first result number.
     *
     * @param firstResult first result
     * @return current context
     */
    public JobLoadContext setFirstResult(Integer firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    /**
     * Sets a max number of results.
     *
     * @param maxResults max results
     * @return current context
     */
    public JobLoadContext setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    /**
     * Sets sort options.
     *
     * @param sort sort options
     * @return current context
     */
    public JobLoadContext setSort(Sort sort) {
        this.sort = sort;
        return this;
    }
}
