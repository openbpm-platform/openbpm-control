/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.service.processdefinition;

import io.openbpm.control.exception.RemoteProcessEngineException;
import io.openbpm.control.test_support.AuthenticatedAsAdmin;
import io.openbpm.control.test_support.RunningEngine;
import io.openbpm.control.test_support.WithRunningEngine;
import io.openbpm.control.test_support.camunda7.AbstractCamunda7IntegrationTest;
import io.openbpm.control.test_support.camunda7.Camunda7Container;
import io.openbpm.control.test_support.camunda7.CamundaRestTestHelper;
import io.openbpm.control.test_support.camunda7.CamundaSampleDataManager;
import io.openbpm.control.test_support.camunda7.dto.response.HistoricProcessInstanceDto;
import org.camunda.community.rest.client.model.ProcessInstanceDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith(AuthenticatedAsAdmin.class)
@WithRunningEngine
public class Camunda7ProcessDefinitionDeleteTest extends AbstractCamunda7IntegrationTest {
    @RunningEngine
    static Camunda7Container<?> camunda7;

    @Autowired
    CamundaRestTestHelper camundaRestTestHelper;

    @Autowired
    ProcessDefinitionService processDefinitionService;

    @Autowired
    ApplicationContext applicationContext;

    @Test
    @DisplayName("Delete active process version but not related active instances by id")
    void givenActiveProcessVersionWithActiveInstances_whenDeleteByIdWithoutInstances_thenExceptionThrownAndProcessNotDeleted() {
        //given
        CamundaSampleDataManager sampleDataManager = applicationContext.getBean(CamundaSampleDataManager.class, camunda7)
                .deploy("test_support/vacationApproval.bpmn")
                .startByKey("vacation_approval");

        String processVersionId = sampleDataManager.getDeployedProcessVersions("vacation_approval").get(0);

        //when and then
        assertThatThrownBy(() -> {
            processDefinitionService.deleteById(processVersionId, false);
        })
                .isInstanceOf(RemoteProcessEngineException.class)
                .hasMessageContaining("Process definition with id: %s can't be deleted", processVersionId);


        Boolean existsProcess = camundaRestTestHelper.existsProcessById(camunda7, processVersionId);
        assertThat(existsProcess).isTrue();

    }

    @Test
    @DisplayName("Delete active process version and related active instances by id")
    void givenActiveProcessVersionWithActiveInstances_whenDeleteByIdWithInstances_thenExceptionThrownAndProcessNotDeleted() {
        //given
        CamundaSampleDataManager sampleDataManager = applicationContext.getBean(CamundaSampleDataManager.class, camunda7)
                .deploy("test_support/vacationApproval.bpmn")
                .startByKey("vacation_approval");

        String processVersionId = sampleDataManager.getDeployedProcessVersions("vacation_approval").get(0);

        //when and then
        processDefinitionService.deleteById(processVersionId, true);

        Boolean existsProcess = camundaRestTestHelper.existsProcessById(camunda7, processVersionId);
        assertThat(existsProcess).isFalse();

        List<ProcessInstanceDto> runtimeInstances = camundaRestTestHelper.findRuntimeProcessInstancesById(camunda7, processVersionId);
        assertThat(runtimeInstances).isEmpty();

        List<HistoricProcessInstanceDto> historyInstances = camundaRestTestHelper.findHistoryProcessInstancesById(camunda7, processVersionId);
        assertThat(historyInstances).isEmpty();

    }
}
