/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.service.engine;

import io.jmix.core.DataManager;
import io.flowset.control.entity.EngineConnectionCheckResult;
import io.flowset.control.entity.engine.AuthType;
import io.flowset.control.entity.engine.BpmEngine;
import io.flowset.control.exception.EngineConnectionFailedException;
import io.flowset.control.test_support.AbstractIntegrationTest;
import io.flowset.control.test_support.AuthenticatedAsAdmin;
import io.flowset.control.test_support.EnabledOnBasicAuthentication;
import io.flowset.control.test_support.RunningEngine;
import io.flowset.control.test_support.WithRunningEngine;
import io.flowset.control.test_support.testcontainers.EngineContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ExtendWith(AuthenticatedAsAdmin.class)
@WithRunningEngine(selected = false, shared = false)
@EnabledOnBasicAuthentication
public class EngineUiServiceBasicAuthTest extends AbstractIntegrationTest {
    @RunningEngine
    EngineContainer<?> engineContainer;

    @Autowired
    EngineUiService engineUiService;

    @Autowired
    DataManager dataManager;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("DELETE FROM control_bpm_engine");
    }

    @ParameterizedTest
    @MethodSource("provideInvalidEngineCredentials")
    @DisplayName("No successful connection to BPM engine if credentials are invalid")
    void givenExistingEngineWithInvalidCredentials_whenTestConnection_thenResultIsNotSuccessful(String username, String password) {
        //given
        BpmEngine bpmEngine = dataManager.create(BpmEngine.class);
        bpmEngine.setName("Test Engine");
        bpmEngine.setType(engineContainer.getEngineType());
        bpmEngine.setBaseUrl(engineContainer.getRestBaseUrl());
        bpmEngine.setAuthEnabled(true);
        bpmEngine.setAuthType(AuthType.BASIC);
        bpmEngine.setBasicAuthUsername(username);
        bpmEngine.setBasicAuthUsername(password);
        bpmEngine = dataManager.save(bpmEngine);

        //when
        EngineConnectionCheckResult result = engineUiService.checkConnection(bpmEngine);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getSuccess()).isFalse();
        assertThat(result.getVersion()).isNull();
    }

    @ParameterizedTest
    @MethodSource("provideInvalidEngineCredentials")
    @DisplayName("EngineConnectionFailedException thrown if get version of BPM engine with invalid credentials")
    void givenExistingEngineWithInvalidCredentials_whenGetVersion_thenEngineConnectionFailedExceptionThrown(String username, String password) {
        //given
        BpmEngine bpmEngine = dataManager.create(BpmEngine.class);
        bpmEngine.setName("Test Engine");
        bpmEngine.setType(engineContainer.getEngineType());
        bpmEngine.setBaseUrl(engineContainer.getRestBaseUrl());
        bpmEngine.setAuthEnabled(true);
        bpmEngine.setAuthType(AuthType.BASIC);
        bpmEngine.setBasicAuthUsername(username);
        bpmEngine.setBasicAuthUsername(password);
        bpmEngine = dataManager.save(bpmEngine);

        //when and then
        BpmEngine finalBpmEngine = bpmEngine;
        assertThatThrownBy(() -> engineUiService.getVersion(finalBpmEngine))
                .isInstanceOf(EngineConnectionFailedException.class)
                .satisfies(e -> {
                    assertThat(((EngineConnectionFailedException) e).getStatusCode()).isEqualTo(401);
                });
    }

    /**
     * Provides a stream of pairs with engine username and password that are required for basic authentication.
     *
     * @return stream of "username-password" pairs
     */
    static Stream<Arguments> provideInvalidEngineCredentials() {
        return Stream.of(Arguments.of("admin1", "admin"),
                Arguments.of("admin", "admin1"),
                Arguments.of("", ""),
                Arguments.of(null, null));
    }
}
