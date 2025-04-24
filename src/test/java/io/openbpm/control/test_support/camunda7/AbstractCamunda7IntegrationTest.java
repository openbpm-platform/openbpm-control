/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.test_support.camunda7;

import io.openbpm.control.entity.engine.EngineType;
import io.openbpm.control.test_support.AbstractIntegrationTest;
import io.openbpm.control.test_support.EnabledOnEngine;

/**
 * A base class for integration test related to Camunda 7 and compatible with Camunda 7 engines.
 */
@EnabledOnEngine(EngineType.CAMUNDA_7)
public class AbstractCamunda7IntegrationTest extends AbstractIntegrationTest {
}
