/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.service.deployment;

import io.openbpm.control.entity.DeploymentData;
import org.camunda.bpm.engine.repository.DeploymentWithDefinitions;
import org.springframework.lang.Nullable;

/**
 * Provides methods to deploy processes to the BPM engine and get data about process deployment.
 */
public interface DeploymentService {

    /**
     * Deploys the business process to BPM engine using the specified context.
     *
     * @param context a context containing business process filename and content that should be deployed.
     * @return deployment result including information about deployed processes
     */
    DeploymentWithDefinitions createDeployment(DeploymentContext context);

    /**
     * Loads from the engine a process deployment data using the specified identifier.
     *
     * @param deploymentId a deployment identifier
     * @return found deployment information or null if not found
     */
    @Nullable
    DeploymentData findById(String deploymentId);
}
