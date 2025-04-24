/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.test_support;

import io.openbpm.control.entity.engine.EngineType;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Optional;

import static io.openbpm.control.test_support.Constants.TEST_ENGINE_PROFILE;
import static org.junit.jupiter.api.extension.ConditionEvaluationResult.disabled;
import static org.junit.jupiter.api.extension.ConditionEvaluationResult.enabled;
import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;

/**
 * Checks whether a test should be executed for the engine type set in {@link ControlEngineTestingProperties#getType()}.
 */
public class EngineTypeCondition implements ExecutionCondition {

    /**
     * Evaluates whether to run the test based on the engine type.
     * The test is enabled if:
     * <ol>
     *     <li>The test is not annotated with {@link EnabledOnEngine}</li>
     *     <li>Spring profile <code>test-engine</code> is not used when running tests (not used by default)</li>
     *     <li>Spring profile <code>test-engine</code> is used and {@link ControlEngineTestingProperties#getType()} is empty or
     *     is one of the allowed engine types that are set in {@link EnabledOnEngine#value()}</li>
     * </ol>
     *
     * @param context the current extension context; never {@code null}
     * @return is testing allowed for a specific type of BPM engine
     */
    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        Optional<EnabledOnEngine> enabledOnEngine = findAnnotation(context.getElement(), EnabledOnEngine.class);
        return enabledOnEngine
                .map(annotation -> evaluateCondition(annotation, context))
                .orElseGet(this::enabledByDefault);
    }


    protected ConditionEvaluationResult evaluateCondition(EnabledOnEngine annotation, ExtensionContext context) {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);
        Environment environment = applicationContext.getEnvironment();

        if (!environment.matchesProfiles(TEST_ENGINE_PROFILE)) {
            return enabled("The 'test-engine' profile is not used");
        }

        ControlEngineTestingProperties engineTestingProperties = applicationContext.getBean(ControlEngineTestingProperties.class);

        String engineTypeProperty = engineTestingProperties.getType();
        if (StringUtils.isBlank(engineTypeProperty)) {
            return enabled("Configured test engine type is not set");
        }

        EngineType[] allowedTypes = annotation.value();

        boolean enabled = Arrays.stream(allowedTypes)
                .anyMatch(engineType -> StringUtils.equalsIgnoreCase(engineTypeProperty, engineType.name()));

        if (!enabled) {
            return disabled("Configured test engine type is " + engineTypeProperty);
        }

        return enabled("Configured test engine type is " + engineTypeProperty);

    }

    protected ConditionEvaluationResult enabledByDefault() {
        return enabled("@EnabledOnEngine is not present");
    }
}
