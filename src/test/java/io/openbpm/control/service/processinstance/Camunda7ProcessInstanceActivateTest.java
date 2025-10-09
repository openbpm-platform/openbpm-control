/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.service.processinstance;

import io.openbpm.control.test_support.AuthenticatedAsAdmin;
import io.openbpm.control.test_support.RunningEngine;
import io.openbpm.control.test_support.WithRunningEngine;
import io.openbpm.control.test_support.camunda7.AbstractCamunda7IntegrationTest;
import io.openbpm.control.test_support.camunda7.Camunda7Container;
import io.openbpm.control.test_support.camunda7.CamundaRestTestHelper;
import io.openbpm.control.test_support.camunda7.CamundaSampleDataManager;
import io.openbpm.control.test_support.camunda7.dto.response.RuntimeProcessInstanceDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(AuthenticatedAsAdmin.class)
@WithRunningEngine
public class Camunda7ProcessInstanceActivateTest extends AbstractCamunda7IntegrationTest {
    @RunningEngine
    static Camunda7Container<?> camunda7;

    @Autowired
    ProcessInstanceService processInstanceService;

    @Autowired
    CamundaRestTestHelper camundaRestTestHelper;

    @Autowired
    ApplicationContext applicationContext;


    @Test
    @DisplayName("Activate existing suspended instance by id")
    void givenExistingSuspendedInstance_whenActivateById_thenInstanceActivated() {
        //given
        CamundaSampleDataManager sampleDataManager = applicationContext.getBean(CamundaSampleDataManager.class, camunda7)
                .deploy("test_support/testVisitPlanningV1.bpmn")
                .startByKey("visitPlanning");

        String processId = sampleDataManager.getDeployedProcessVersions("visitPlanning").get(0);
        String instanceId = camundaRestTestHelper.getRuntimeInstancesById(camunda7, processId).get(0).getId();

        camundaRestTestHelper.suspendInstanceById(camunda7, instanceId);

        RuntimeProcessInstanceDto suspendedInstance = camundaRestTestHelper.getRuntimeInstanceById(camunda7, instanceId);

        //when
        processInstanceService.activateById(instanceId);

        //then
        RuntimeProcessInstanceDto foundInstance = camundaRestTestHelper.getRuntimeInstanceById(camunda7, instanceId);
        assertThat(foundInstance).isNotNull();
        assertThat(suspendedInstance).isNotNull();
        assertThat(foundInstance.getSuspended()).isNotEqualTo(suspendedInstance.getSuspended());
        assertThat(foundInstance.getSuspended()).isFalse();
    }

    @Test
    @DisplayName("Activate asynchronously existing suspended instances by ids")
    void givenExistingSuspendedInstances_whenActivateByIdsAsync_thenInstancesActivated() {
        //given
        CamundaSampleDataManager sampleDataManager = applicationContext.getBean(CamundaSampleDataManager.class, camunda7)
                .deploy("test_support/testVisitPlanningV1.bpmn")
                .startByKey("visitPlanning", 5);

        String processId = sampleDataManager.getDeployedProcessVersions("visitPlanning").get(0);
        camundaRestTestHelper.suspendInstanceByProcessId(camunda7, processId);

        List<String> suspendedInstanceIds = camundaRestTestHelper.getSuspendedInstancesByProcessId(camunda7, processId);

        //when
        processInstanceService.activateByIdsAsync(suspendedInstanceIds);
        waitForBatchExecution();

        //then
        List<String> activeInstanceIds = camundaRestTestHelper.getActiveInstancesByProcessId(camunda7, processId);
        assertThat(activeInstanceIds)
                .hasSize(5)
                .containsExactlyInAnyOrderElementsOf(suspendedInstanceIds);

    }

    private void waitForBatchExecution() {
        boolean batchExists;
        int attempts = 0;
        do {
            batchExists = camundaRestTestHelper.activeBatchExits(camunda7);
            attempts++;

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                //do nothing
            }
            if (attempts > 100) { //prevent infinite loop
                break;
            }
        } while (batchExists);
    }
}
