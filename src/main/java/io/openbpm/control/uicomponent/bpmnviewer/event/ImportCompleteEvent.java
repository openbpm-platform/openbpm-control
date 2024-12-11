/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.uicomponent.bpmnviewer.event;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import io.openbpm.control.uicomponent.bpmnviewer.BpmnViewerImpl;

@DomEvent("xml-import-completed")
public class ImportCompleteEvent extends ComponentEvent<BpmnViewerImpl> {

    private final String processDefinitionsJson;
    /**
     * Creates a new event using the given source and indicator whether the
     * event originated from the client side or the server side.
     *
     * @param source     the source component
     * @param fromClient <code>true</code> if the event originated from the client
     *                   side, <code>false</code> otherwise
     */
    public ImportCompleteEvent(BpmnViewerImpl source, boolean fromClient,
                               @EventData("event.processDefinitionsJson") String processDefinitionsJson) {
        super(source, fromClient);
        this.processDefinitionsJson = processDefinitionsJson;
    }

    public String getProcessDefinitionsJson() {
        return processDefinitionsJson;
    }
}
