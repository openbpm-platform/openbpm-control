/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.service.variable;

import io.openbpm.control.entity.filter.VariableFilter;
import io.openbpm.control.entity.variable.HistoricVariableInstanceData;
import io.openbpm.control.entity.variable.VariableInstanceData;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * Provides methods for managing process variable instances in the BPM engine.
 */
public interface VariableService {

    /**
     * Loads variable instances from the engine runtime data using the specified context.
     *
     * @param loadContext a context to load process variable instances
     * @return a list of variable instances
     */
    List<VariableInstanceData> findRuntimeVariables(VariableLoadContext loadContext);

    /**
     * Loads process variable instance with the specified identifier
     * from the engine runtime data.
     *
     * @param variableInstanceId identifier of the process variable instance
     * @return found process variable instance
     */
    VariableInstanceData findRuntimeVariableById(String variableInstanceId);

    /**
     * Loads the binary value of the process variable instance
     * with the specified identifier.
     *
     * @param variableInstanceId identifier of the process variable instance
     * @return resource containing binary data of the variable instance
     */
    Resource getVariableInstanceBinary(String variableInstanceId);

    /**
     * Loads variable instances from the engine history using the specified context.
     *
     * @param loadContext a context to load variable instances
     * @return a list of process variable instances
     */
    List<HistoricVariableInstanceData> findHistoricVariables(VariableLoadContext loadContext);

    /**
     * Loads a total count of process variable instances from the engine runtime data that match the specified filter.
     *
     * @param filter variable instance filter
     * @return a count of variable instances
     */
    long getRuntimeVariablesCount(@Nullable VariableFilter filter);

    /**
     * Loads a total count of process variable instances from the engine history that match the specified filter.
     *
     * @param filter variable instance filter
     * @return a count of process variable instances
     */
    long getHistoricVariablesCount(@Nullable VariableFilter filter);

    /**
     * Updates the value of the specified process variable to the specified value.
     *
     * @param variableInstanceData variable instance data containing new value
     */
    void updateVariableLocal(VariableInstanceData variableInstanceData);

    /**
     * Loads process variable instance with the specified identifier from the engine history.
     *
     * @param variableInstanceId variable instance identifier
     * @return found process variable instance
     */
    HistoricVariableInstanceData findHistoricVariableById(String variableInstanceId);

    /**
     * Removes the specified process variable instance from the engine runtime data.
     *
     * @param variableInstanceData process variable instance to be removed
     */
    void removeVariableLocal(VariableInstanceData variableInstanceData);

    /**
     * Removes the specified set of process variable instances
     * from the engine runtime data within the given execution context.
     *
     * @param executionId   identifier of the process execution context
     * @param variableItems set of process variable instances to be removed
     */
    void removeVariablesLocal(String executionId, Set<VariableInstanceData> variableItems);
    /**
     * Updates the binary value of the specified process variable instance
     * with the provided file data.
     *
     * @param variableInstanceData process variable instance to update
     * @param data                 file containing new binary value
     */
    void updateVariableBinary(VariableInstanceData variableInstanceData, File data);
}
