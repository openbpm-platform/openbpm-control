/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.test_support;

import io.flowset.control.entity.engine.EngineType;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables executing a test on specific engine types.
 * <p>
 * Works when the Spring `test-engine` profile is used as an additional profile.
 *
 * @see ControlEngineTestingProperties#getType()
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ExtendWith(EngineTypeCondition.class)
@Inherited
public @interface EnabledOnEngine {

    /**
     * A list of engine types for which the test should be run.
     *
     * @return engine types to which the test applies
     */
    EngineType[] value();
}
