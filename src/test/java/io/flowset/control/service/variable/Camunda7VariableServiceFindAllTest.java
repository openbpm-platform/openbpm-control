/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.service.variable;

import io.jmix.core.DataManager;
import io.flowset.control.entity.variable.VariableInstanceData;
import io.flowset.control.test_support.AuthenticatedAsAdmin;
import io.flowset.control.test_support.RunningEngine;
import io.flowset.control.test_support.WithRunningEngine;
import io.flowset.control.test_support.camunda7.AbstractCamunda7IntegrationTest;
import io.flowset.control.test_support.camunda7.Camunda7Container;
import io.flowset.control.test_support.camunda7.CamundaRestTestHelper;
import io.flowset.control.test_support.camunda7.CamundaSampleDataManager;
import io.flowset.control.test_support.camunda7.dto.request.StartProcessDto;
import io.flowset.control.test_support.camunda7.dto.request.VariableValueDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@SpringBootTest
@ExtendWith(AuthenticatedAsAdmin.class)
@WithRunningEngine
public class Camunda7VariableServiceFindAllTest extends AbstractCamunda7IntegrationTest {
    @RunningEngine
    static Camunda7Container<?> camunda7;

    @Autowired
    VariableService variableService;

    @Autowired
    CamundaRestTestHelper camundaRestTestHelper;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    DataManager dataManager;

    @Test
    @DisplayName("Return all runtime variables if load context is empty")
    void givenExistingVariablesAndEmptyContext_whenFindRuntimeVariables_thenAllVariablesReturned() {
        //given

       applicationContext.getBean(CamundaSampleDataManager.class, camunda7)
                .deploy("test_support/testUpdateVariable.bpmn")
                .startByKey("testUpdateVariable", StartProcessDto.builder()
                        .variable("firstVariable", new VariableValueDto("String", "Some value"))
                        .build())
                .startByKey("testUpdateVariable", StartProcessDto.builder()
                        .variable("secondVariable", new VariableValueDto("String", "Some another value"))
                        .build());

       VariableLoadContext loadContext = new VariableLoadContext();

        //when
       List<VariableInstanceData> runtimeVariables = variableService.findRuntimeVariables(loadContext);

       //then
       assertThat(runtimeVariables).hasSize(2)
               .extracting(VariableInstanceData::getName, VariableInstanceData::getValue, VariableInstanceData::getType)
               .containsExactlyInAnyOrder(
                       tuple("firstVariable", "Some value", "String"),
                       tuple("secondVariable", "Some another value", "String")
               );
    }

    @ParameterizedTest
    @MethodSource("provideValidPaginationData")
    @DisplayName("Load a page with runtime variables")
    void givenContextWithPagination_whenFindRuntimeVariables_thenPageWithVariablesReturned(int firstResult, int maxResults,
                                                                                           int expectedCount) {
        //given
        applicationContext.getBean(CamundaSampleDataManager.class, camunda7)
                .deploy("test_support/testUpdateVariable.bpmn")
                .startByKey("testUpdateVariable", StartProcessDto.builder()
                        .variable("firstVariable", new VariableValueDto("String", "Some value"))
                        .build(), 3);

        VariableLoadContext loadContext = new VariableLoadContext()
                .setFirstResult(firstResult)
                .setMaxResults(maxResults);

        //when
        List<VariableInstanceData> runtimeVariables = variableService.findRuntimeVariables(loadContext);

        //then
        assertThat(runtimeVariables).hasSize(expectedCount);
    }

    static Stream<Arguments> provideValidPaginationData() {
        return Stream.of(
                Arguments.of(0, 2, 2),
                Arguments.of(2, 4, 1)
        );
    }
}
