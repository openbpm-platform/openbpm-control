/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.service.incident;

import io.jmix.core.Sort;
import io.openbpm.control.entity.filter.IncidentFilter;
import io.openbpm.control.service.ItemListLoadContext;

/**
 * A context that contains the following options to load incidents:
 * <ul>
 *     <li>Pagination options: first and max results</li>
 *     <li>Sort options: property and direction to sort. Supported properties: "id", "type, "message", "activityId". <br/>
 *         Additionally, for open incidents: "timestamp", "processInstanceId", "processDefinitionId". <br/>
 *         For historic incidents: "createTime", "endTime", "resolved".</li>
 *     <li>Filtering options</li>
 * </ul>
 * <p>
 * Example: load first 50 open incidents for the specified process instance and sort them by incident date
 * <pre>
 *     IncidentFilter filter = metadata.create(IncidentFilter.class);
 *     filter.setProcessInstanceId(processInstanceId);
 *
 *     IncidentLoadContext context = new IncidentLoadContext()
 *      .setFirstResult(0)
 *      .setMaxResults(50)
 *      .setFilter(filter)
 *      .setSort(Sort.by(Sort.Direction.DESC, "timestamp"));
 * </pre>
 *
 * @see IncidentFilter
 * @see io.jmix.core.Metadata
 * @see Sort
 */
public class IncidentLoadContext extends ItemListLoadContext<IncidentFilter> {
    /**
     * Sets an incident filter.
     *
     * @param filter an instance of {@link IncidentFilter}
     * @return current context
     */
    public IncidentLoadContext setFilter(IncidentFilter filter) {
        this.filter = filter;
        return this;
    }

    /**
     * Sets a first result number.
     *
     * @param firstResult first result
     * @return current context
     */
    public IncidentLoadContext setFirstResult(Integer firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    /**
     * Sets a max number of results.
     *
     * @param maxResults max results
     * @return current context
     */
    public IncidentLoadContext setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    /**
     * Sets sort options.
     *
     * @param sort sort options
     * @return current context
     */
    public IncidentLoadContext setSort(Sort sort) {
        this.sort = sort;
        return this;
    }
}