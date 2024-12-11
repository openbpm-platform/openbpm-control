/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.service.deployment.impl;

import feign.FeignException;
import io.openbpm.control.entity.DeploymentData;
import io.openbpm.control.mapper.DeploymentMapper;
import io.openbpm.control.service.deployment.DeploymentContext;
import io.openbpm.control.service.deployment.DeploymentService;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.repository.DeploymentWithDefinitions;
import org.camunda.community.rest.client.api.DeploymentApiClient;
import org.camunda.community.rest.client.model.DeploymentDto;
import org.camunda.community.rest.impl.RemoteRepositoryService;
import org.camunda.community.rest.impl.builder.DelegatingDeploymentBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service("control_DeploymentService")
@Slf4j
public class DeploymentServiceImpl implements DeploymentService {
    public static final String OPENBPM_CONTROL_SOURCE = "OpenBPM Control";

    protected final RemoteRepositoryService remoteRepositoryService;
    protected final DeploymentMapper deploymentMapper;
    protected final DeploymentApiClient deploymentApiClient;

    public DeploymentServiceImpl(RemoteRepositoryService remoteRepositoryService,
                                 DeploymentMapper deploymentMapper,
                                 DeploymentApiClient deploymentApiClient) {
        this.remoteRepositoryService = remoteRepositoryService;
        this.deploymentMapper = deploymentMapper;
        this.deploymentApiClient = deploymentApiClient;
    }

    @Override
    public DeploymentWithDefinitions createDeployment(DeploymentContext context) {
        DelegatingDeploymentBuilder deployment = remoteRepositoryService.createDeployment();

        return deployment
                .source(OPENBPM_CONTROL_SOURCE)
                .addInputStream(context.getResourceName(), context.getResourceContent())
                .deployWithResult();
    }

    @Override
    @Nullable
    public DeploymentData findById(String deploymentId) {
        try {
            ResponseEntity<DeploymentDto> response = deploymentApiClient.getDeployment(deploymentId);
            if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
                return deploymentMapper.fromDto(response.getBody());
            }
        } catch (Exception e) {
            if (e instanceof FeignException feignException && feignException.status() == 404) {
                log.error("Unable to find deployment by id {}", deploymentId, e);
                return null;
            }
            throw e;
        }

        return null;
    }
}
