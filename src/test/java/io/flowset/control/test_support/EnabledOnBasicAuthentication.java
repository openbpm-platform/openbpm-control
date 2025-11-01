/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.test_support;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables to run the test if BPM engine running in the test container has configured basic authentication.
 * The test is skipped if the Spring profile <code>test-engine</code> is used and {@link ControlEngineTestingProperties#getAuthType()} is not Basic.
 * If the Spring profile <code>test-engine</code> is not used, the test is executed on engine container with configured basic authentication.
 *
 * @see BasicAuthenticationCondition
 * @see RunningEngineExtension
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Order(1)
@ExtendWith(BasicAuthenticationCondition.class)
@Inherited
public @interface EnabledOnBasicAuthentication {
}
