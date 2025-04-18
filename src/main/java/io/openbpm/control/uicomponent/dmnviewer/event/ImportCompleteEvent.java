/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.uicomponent.dmnviewer.event;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import io.openbpm.control.uicomponent.dmnviewer.DmnViewer1;

@DomEvent("xml-import-completed")
public class ImportCompleteEvent extends ComponentEvent<DmnViewer1> {

    private final String decisionDefinitionsJson;
    /**
     * Creates a new event using the given source and indicator whether the
     * event originated from the client side or the server side.
     *
     * @param source     the source component
     * @param fromClient <code>true</code> if the event originated from the client
     *                   side, <code>false</code> otherwise
     */
    public ImportCompleteEvent(DmnViewer1 source, boolean fromClient,
                               @EventData("event.decisionDefinitionsJson") String decisionDefinitionsJson) {
        super(source, fromClient);
        this.decisionDefinitionsJson = decisionDefinitionsJson;
    }

    public String getDecisionDefinitionsJson() {
        return decisionDefinitionsJson;
    }
}
