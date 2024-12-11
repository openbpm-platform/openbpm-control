/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.service.usertask;

import io.jmix.core.Sort;
import io.openbpm.control.entity.filter.UserTaskFilter;
import io.openbpm.control.service.ItemListLoadContext;

/**
 * A context that contains the following options to load user tasks:
 * <ul>
 *     <li>Pagination options: first and max results</li>
 *     <li>Sort options: property and direction to sort. Supported properties: "id", "name", "assignee". <br/>
 *      Additionally, for user tasks from the engine history: "startTime", "endTime", "dueDate", "taskDefinitionKey".
 *     </li>
 *     <li>Filtering options</li>
 * </ul>
 * <p>
 * Example: load first 50 user tasks for the specified process instance and sort them by name
 * <pre>
 *     UserTaskFilter filter = metadata.create(UserTaskFilter.class);
 *     filter.setProcessInstanceId(processInstanceId);
 *
 *     UserTaskLoadContext context = new UserTaskLoadContext()
 *      .setFirstResult(0)
 *      .setMaxResults(50)
 *      .setFilter(filter)
 *      .setSort(Sort.by(Sort.Direction.ASC, "name"));
 * </pre>
 *
 * @see UserTaskFilter
 * @see io.jmix.core.Metadata
 * @see Sort
 */
public class UserTaskLoadContext extends ItemListLoadContext<UserTaskFilter> {

    /**
     * Sets a user task filter.
     *
     * @param filter an instance of {@link UserTaskFilter}
     * @return current context
     */
    public UserTaskLoadContext setFilter(UserTaskFilter filter) {
        this.filter = filter;
        return this;
    }

    /**
     * Sets a first result number.
     *
     * @param firstResult first result
     * @return current context
     */
    public UserTaskLoadContext setFirstResult(Integer firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    /**
     * Sets a max number of results.
     *
     * @param maxResults max results
     * @return current context
     */
    public UserTaskLoadContext setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    /**
     * Sets sort options.
     *
     * @param sort sort options
     * @return current context
     */
    public UserTaskLoadContext setSort(Sort sort) {
        this.sort = sort;
        return this;
    }
}
