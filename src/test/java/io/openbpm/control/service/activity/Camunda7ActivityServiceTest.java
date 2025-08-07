/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.service.activity;

import io.openbpm.control.entity.activity.ProcessActivityStatistics;
import io.openbpm.control.test_support.AuthenticatedAsAdmin;
import io.openbpm.control.test_support.RunningEngine;
import io.openbpm.control.test_support.WithRunningEngine;
import io.openbpm.control.test_support.camunda7.AbstractCamunda7IntegrationTest;
import io.openbpm.control.test_support.camunda7.Camunda7Container;
import io.openbpm.control.test_support.camunda7.CamundaSampleDataManager;
import io.openbpm.control.test_support.camunda7.dto.request.StartProcessDto;
import io.openbpm.control.test_support.camunda7.dto.request.VariableValueDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@SpringBootTest
@ExtendWith(AuthenticatedAsAdmin.class)
@WithRunningEngine
public class Camunda7ActivityServiceTest extends AbstractCamunda7IntegrationTest {
    @RunningEngine
    static Camunda7Container<?> camunda7;

    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    ActivityService activityService;

    @Test
    @DisplayName("Load activity statistics by process definition id")
    void givenProcessWithRunningInstances_whenGetStatisticsByProcessId_thenStatisticsReturned() {
        // given
        StartProcessDto startProcessDto = new StartProcessDto();
        startProcessDto.setVariables(Map.of("fail",
                new VariableValueDto("boolean", true)));

        CamundaSampleDataManager sampleDataManager = applicationContext.getBean(CamundaSampleDataManager.class, camunda7)
                .deploy("test_support/testActivityStatistics.bpmn")
                .startByKey("testActivityStatistics", 3)
                .startByKey("testActivityStatistics", startProcessDto, 2)
                .waitJobsExecution();

        String processDefId = sampleDataManager.getDeployedProcessVersions("testActivityStatistics").getFirst();

        // when
        List<ProcessActivityStatistics> stats = activityService.getStatisticsByProcessId(processDefId);

        // then
        assertThat(stats).isNotEmpty()
                .hasSize(2);

        assertThat(stats)
                .filteredOn("activityId", "testScriptTask")
                .singleElement()
                .satisfies(stat -> {
                    assertThat(stat.getInstanceCount()).isEqualTo(2);
                    assertThat(stat.getFailedJobCount()).isEqualTo(2);
                    assertThat(stat.getIncidents())
                            .hasSize(1)
                            .extracting("incidentType", "incidentCount")
                            .contains(tuple("failedJob", 2));
                });

        assertThat(stats)
                .filteredOn("activityId", "testUserTask")
                .singleElement()
                .satisfies(stat -> {
                    assertThat(stat.getInstanceCount()).isEqualTo(3);
                    assertThat(stat.getFailedJobCount()).isZero();
                    assertThat(stat.getIncidents()).isNullOrEmpty();
                });
    }
}
