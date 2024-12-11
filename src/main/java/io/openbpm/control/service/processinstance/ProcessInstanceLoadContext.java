/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.service.processinstance;

import io.jmix.core.Sort;
import io.openbpm.control.entity.filter.ProcessInstanceFilter;
import io.openbpm.control.service.ItemListLoadContext;

/**
 * A context that contains the following options to load incidents:
 * <ul>
 *     <li>Pagination options: first and max results</li>
 *     <li>Sort options: property and direction to sort. Supported properties: "id", "processDefinitionId", "processDefinitionKey", "businessKey". <br/>
 *     Additionally, for instances from the engine history: "startTime", "endTime". </li>
 *     <li>Filtering options</li>
 * </ul>
 * <p>
 * Example: load first 50 process instances for the process definition with key and sort them by business key
 * <pre>
 *     ProcessInstanceFilter filter = metadata.create(ProcessInstanceFilter.class);
 *     filter.setProcessDefinitionKey("approve-invoice");
 *
 *     ProcessInstanceLoadContext context = new ProcessInstanceLoadContext()
 *      .setFirstResult(0)
 *      .setMaxResults(50)
 *      .setFilter(filter)
 *      .setSort(Sort.by(Sort.Direction.ASC, "businessKey"));
 * </pre>
 *
 * @see ProcessInstanceFilter
 * @see io.jmix.core.Metadata
 * @see Sort
 */
public class ProcessInstanceLoadContext extends ItemListLoadContext<ProcessInstanceFilter> {

    /**
     * Sets a process instance filter.
     *
     * @param filter an instance of {@link ProcessInstanceFilter}
     * @return current context
     */
    public ProcessInstanceLoadContext setFilter(ProcessInstanceFilter filter) {
        this.filter = filter;
        return this;
    }

    /**
     * Sets a first result number.
     *
     * @param firstResult first result
     * @return current context
     */
    public ProcessInstanceLoadContext setFirstResult(Integer firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    /**
     * Sets a max number of results.
     *
     * @param maxResults max results
     * @return current context
     */
    public ProcessInstanceLoadContext setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    /**
     * Sets sort options.
     *
     * @param sort sort options
     * @return current context
     */
    public ProcessInstanceLoadContext setSort(Sort sort) {
        this.sort = sort;
        return this;
    }
}
