/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.service.usertask;

import io.flowset.control.entity.UserTaskData;
import io.flowset.control.entity.filter.UserTaskFilter;
import io.flowset.control.entity.variable.VariableInstanceData;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * Provides methods to load and update user tasks in the BPM engine.
 */
public interface UserTaskService {

    /**
     * Loads active user tasks from the engine using the specified context.
     *
     * @param loadContext a context to load user tasks
     * @return a list of active user tasks
     */
    List<UserTaskData> findRuntimeTasks(UserTaskLoadContext loadContext);

    /**
     * Loads a total count of active user tasks from the engine that match the specified filter.
     *
     * @param filter user tasks filter
     * @return count of user tasks
     */
    long getRuntimeTasksCount(@Nullable UserTaskFilter filter);

    /**
     * Loads user tasks from the engine history using the specified context.
     *
     * @param loadContext a context to load user tasks
     * @return a list of user tasks
     */
    List<UserTaskData> findHistoricTasks(UserTaskLoadContext loadContext);

    /**
     * Loads from the engine history the total count of user tasks that match the specified filter.
     *
     * @param filter user tasks filter
     * @return count of user tasks
     */
    long getHistoryTasksCount(@Nullable UserTaskFilter filter);

    /**
     * Updates an assignee to the specified value for the user task with the specified identifier.
     *
     * @param taskId      a user task identifier
     * @param newAssignee new assignee username
     */
    void setAssignee(String taskId, String newAssignee);

    /**
     * Completes a user task with the specified identifier using the specified variables values.
     *
     * @param taskId            a user task identifier
     * @param variableInstances process variable values
     */
    void completeTaskById(String taskId, Collection<VariableInstanceData> variableInstances);

    /**
     * Loads a user task with the specified identifier.
     *
     * @param taskId a user task identifier
     * @return found user task or null if not found
     */
    @Nullable
    UserTaskData findTaskById(String taskId);
}
