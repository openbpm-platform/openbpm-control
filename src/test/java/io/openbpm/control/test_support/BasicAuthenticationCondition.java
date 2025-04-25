/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.test_support;

import io.openbpm.control.entity.engine.AuthType;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.extension.ConditionEvaluationResult.disabled;
import static org.junit.jupiter.api.extension.ConditionEvaluationResult.enabled;
import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;

/**
 * Checks whether to run the test for the engine authentication type set in {@link ControlEngineTestingProperties#getAuthType()}.
 */
public class BasicAuthenticationCondition implements ExecutionCondition {

    /**
     * Evaluates whether to run the test based on the engine authentication type.
     * The test is run if:
     * <ol>
     *     <li>The test is not annotated with {@link EnabledOnBasicAuthentication}</li>
     *     <li>Spring profile <code>test-engine</code> is not used when running tests (not used by default)</li>
     *     <li>The test is not annotated with {@link EnabledOnBasicAuthentication} and {@link ControlEngineTestingProperties#getAuthType()} is Basic</li>
     * </ol>
     *
     * @param context the current extension context; never {@code null}
     * @return is testing allowed for a BPM engine with basic authenticated
     */
    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        Optional<EnabledOnBasicAuthentication> enabledOnBasicAuthentication = findAnnotation(context.getElement(), EnabledOnBasicAuthentication.class);
        if (enabledOnBasicAuthentication.isPresent()) {
            return evaluateEnabledOnBasicAuthentication(context);
        }

        return enabledByDefault();
    }

    protected ConditionEvaluationResult evaluateEnabledOnBasicAuthentication(ExtensionContext context) {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);
        Environment environment = applicationContext.getEnvironment();

        boolean testEngineProfileUsed = environment.matchesProfiles(Constants.TEST_ENGINE_PROFILE);
        if (!testEngineProfileUsed) {
            return enabled("Test engine profile is not used");
        }

        ControlEngineTestingProperties testingProperties = applicationContext.getBean(ControlEngineTestingProperties.class);
        String authTypeProperty = testingProperties.getAuthType();
        if (StringUtils.isEmpty(authTypeProperty)) {
            return disabled("Authentication type is not set");
        }

        AuthType authType = AuthType.fromId(authTypeProperty);

        return authType == AuthType.BASIC ? enabled("Basic authentication is used") : disabled("Basic authentication is not set as Basic");
    }

    private ConditionEvaluationResult enabledByDefault() {
        return enabled("Basic authenticated rule is not present");
    }
}
