/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.test_support;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Runs a test Docker container for the BPM engine using {@link RunningEngineExtension}.
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({RunningEngineExtension.class})
@Inherited
public @interface WithRunningEngine {

    /**
     * Sets the running engine as the selected engine
     *
     * @return whether the running BPM engine should be set as selected
     */
    boolean selected() default true;

    /**
     * Specifies that the engine should be run once for all test methods in a single test class.
     * If the engine is shared, the data in the running engine is clean between test methods.
     *
     * @return whether to run the engine for all test methods in a test class.
     */
    boolean shared() default true;

    /**
     * Default docker image used to run an engine container.
     *
     * @return engine docker image name with tag
     */
    String dockerImageName() default "camunda/camunda-bpm-platform:run-7.22.0";
}
