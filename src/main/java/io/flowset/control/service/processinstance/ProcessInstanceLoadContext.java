/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.service.processinstance;

import io.jmix.core.Sort;
import io.flowset.control.entity.filter.ProcessInstanceFilter;
import io.flowset.control.service.ItemListLoadContext;
import lombok.Getter;

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
@Getter
public class ProcessInstanceLoadContext extends ItemListLoadContext<ProcessInstanceFilter> {

    protected boolean loadIncidents;

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

    /**
     * Sets a flag that allows to load incidents for the state.
     *
     * @param loadIncidents needs to load incidents to mark the instance state.
     * @return current context
     */
    public ProcessInstanceLoadContext setLoadIncidents(boolean loadIncidents) {
        this.loadIncidents = loadIncidents;
        return this;
    }
}
