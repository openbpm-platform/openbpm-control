/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.service.deployment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.InputStream;

/**
 * A context that contains the following options to deploy processes:
 * <ul>
 *     <li>Resource name: a filename containing a business process, e.g. approve-invoice-process.bpmn.</li>
 *     <li>Resource content: an input stream containing a business process described in BPMN 2.0 XML format.</li>
 * </ul>
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeploymentContext {
    private String resourceName;
    private InputStream resourceContent;

    /**
     * Sets name and content of the resource that should be deployed to the BPM engine.
     * @param resourceName a resource name, e.g. approve-invoice.bpmn
     * @param resourceContent an input stream containing resource content
     * @return current context
     */
    public DeploymentContext withResource(String resourceName, InputStream resourceContent) {
        this.resourceName = resourceName;
        this.resourceContent = resourceContent;

        return this;
    }

}
