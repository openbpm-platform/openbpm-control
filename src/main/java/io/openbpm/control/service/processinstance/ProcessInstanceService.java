/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.service.processinstance;

import io.openbpm.control.entity.filter.ProcessInstanceFilter;
import io.openbpm.control.entity.processinstance.ProcessInstanceData;
import io.openbpm.control.entity.variable.VariableInstanceData;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.List;

/**
 * Provides methods for managing process instances in the BPM engine.
 */
public interface ProcessInstanceService {

    /**
     * Loads process instances from the engine history using the specified context.
     *
     * @param loadContext a context to load process instances
     * @return a list of process instances
     */
    List<ProcessInstanceData> findAllHistoricInstances(ProcessInstanceLoadContext loadContext);

    /**
     * Loads from the engine history the total count of process instances that match the specified filter.
     *
     * @param filter a process instance filter
     * @return count of process instances
     */
    long getHistoricInstancesCount(ProcessInstanceFilter filter);

    /**
     * Loads a process instance with the specified identifier.
     *
     * @param processInstanceId a process instance identifier
     * @return found instance
     */
    ProcessInstanceData getProcessInstanceById(String processInstanceId);

    /**
     * Start a new instance of the process definition with the specified identifier and using the specified process variable values.
     *
     * @param processDefinitionId a process definition identifier
     * @param variableInstances   process variables with values
     * @return started process instance
     */
    ProcessInstanceData startProcessByDefinitionId(String processDefinitionId, Collection<VariableInstanceData> variableInstances);

    /**
     * Suspends the process instance with the specified identifier.
     *
     * @param processInstanceId a process instance identifier
     */
    void suspendById(String processInstanceId);

    /**
     * Activates the process instance with the specified identifier.
     *
     * @param processInstanceId a process instance identifier
     */
    void activateById(String processInstanceId);

    /**
     * Terminates the process instance with the specified identifier.
     *
     * @param processInstanceId a process instance identifier
     */
    void terminateById(String processInstanceId);

    /**
     * Asynchronously terminates process instances with the specified identifiers.
     *
     * @param processInstanceIds a list of process instance identifiers
     * @param reason             a reason to terminate
     */
    void terminateByIdsAsync(List<String> processInstanceIds, @Nullable String reason);

    /**
     * Activates asynchronously the process instances with the specified identifiers.
     *
     * @param processInstancesIds a list of process instance identifiers
     */
    void activateByIdsAsync(List<String> processInstancesIds);

    /**
     * Suspends asynchronously the process instances with the specified identifiers.
     *
     * @param processInstancesIds a list of process instance identifiers
     */
    void suspendByIdsAsync(List<String> processInstancesIds);

    /**
     * Loads the total count of running instances of the process definition version with the specified identifier.
     *
     * @param processDefinitionId a process definition identifier
     * @return count of instances
     */
    long getCountByProcessDefinitionId(String processDefinitionId);

    /**
     * Loads the count of running instances of the process definition with the specified key.
     *
     * @param processDefinitionKey a process key
     * @return count of instances
     */
    long getCountByProcessDefinitionKey(String processDefinitionKey);
}
