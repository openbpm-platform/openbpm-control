/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.test_support.camunda7;

import io.flowset.control.test_support.testcontainers.EngineContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * A base class for Camunda 7 and compatible with Camunda 7 engines.
 *
 * @param <SELF>
 */
public abstract class Camunda7Container<SELF extends Camunda7Container<SELF>> extends EngineContainer<SELF> {
    public Camunda7Container(DockerImageName dockerImageName) {
        super(dockerImageName);
    }

    @Override
    public String getRestBaseUrl() {
        return "http://" + getHost() + ":" + getFirstMappedPort() + "/engine-rest";
    }
}
