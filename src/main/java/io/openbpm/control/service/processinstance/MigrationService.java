/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.service.processinstance;

import java.util.List;

/**
 * Provides methods related to the process instances migration.
 */
public interface MigrationService {
    /**
     * Validates that process instance with the specified identifier can be migrated to the specified process definition version.
     * Migration plan generated by the BPM engine is used for checking.
     *
     * @param processInstanceId         a process instance identifier
     * @param targetProcessDefinitionId a target process definition identifier
     * @return a list of migration failures
     */
    List<String> validateMigrationOfSingleProcessInstance(String processInstanceId, String targetProcessDefinitionId);

    /**
     * Migrates process instance with the specified identifier to the specified process definition.
     * Migration plan generated by the BPM engine is used.
     *
     * @param processInstanceId         a process instance identifier
     * @param targetProcessDefinitionId a target process definition identifier
     */
    void migrateSingleProcessInstance(String processInstanceId, String targetProcessDefinitionId);

    /**
     * Validates that all process instances related to the source process definition can be migrated to the specified process definition.
     * Migration plan generated by the BPM engine is used for checking.
     *
     * @param srcProcessDefinitionId    a source process definition identifier
     * @param targetProcessDefinitionId a target process definition identifier
     * @return a list of migration failures
     */
    List<String> validateMigrationOfProcessInstances(String srcProcessDefinitionId, String targetProcessDefinitionId);

    /**
     * Migrates all process instances related to the source process definition to the specified process definition.
     * Migration plan generated by the BPM engine is used.
     *
     * @param srcProcessDefinitionId    a source process definition identifier
     * @param targetProcessDefinitionId a target process definition identifier
     */
    void migrateAllProcessInstances(String srcProcessDefinitionId, String targetProcessDefinitionId);
}