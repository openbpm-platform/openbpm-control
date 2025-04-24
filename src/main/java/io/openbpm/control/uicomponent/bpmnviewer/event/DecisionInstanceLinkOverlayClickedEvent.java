/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.uicomponent.bpmnviewer.event;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import io.openbpm.uikit.component.bpmnviewer.BpmnViewer;

@DomEvent("decision-instance-link-overlay-clicked")
public class DecisionInstanceLinkOverlayClickedEvent extends ComponentEvent<BpmnViewer> {

    private final String decisionInstanceId;
    /**
     * Creates a new event using the given source and indicator whether the
     * event originated from the client side or the server side.
     *
     * @param source     the source component
     * @param fromClient <code>true</code> if the event originated from the client
     *                   side, <code>false</code> otherwise
     */
    public DecisionInstanceLinkOverlayClickedEvent(BpmnViewer source, boolean fromClient,
                                                   @EventData("event.decisionInstanceId") String decisionInstanceId) {
        super(source, fromClient);
        this.decisionInstanceId = decisionInstanceId;
    }

    public String getDecisionInstanceId() {
        return decisionInstanceId;
    }
}
