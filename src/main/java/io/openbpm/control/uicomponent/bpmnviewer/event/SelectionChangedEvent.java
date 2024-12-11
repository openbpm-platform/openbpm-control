/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.uicomponent.bpmnviewer.event;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.DomEvent;
import com.vaadin.flow.component.EventData;
import io.openbpm.control.uicomponent.bpmnviewer.BpmnViewerImpl;

/**
 * Represents an event that is triggered when the selection of an element changes in the BPMN viewer.
 * This event is associated with the "ElementSelectionBpmEvent" DOM event and carries information about the selected BPMN element.
 *
 * @see DomEvent
 */
@DomEvent("ElementSelectionBpmEvent")
public class SelectionChangedEvent extends ComponentEvent<BpmnViewerImpl> {

    /**
     * JSON representation of the business object associated with the selected BPMN element.
     */
    private final String businessObjectJson;

    /**
     * The type of the selected BPMN element. This could be a task, gateway, event, or any other BPMN element type.
     */
    private final String elementType;

    /**
     * Creates a new selection changed event using the given source, indicator of whether the event originated from the client side or the server side,
     * BPMN element type, and JSON representation of the associated business object.
     *
     * @param source            The source component triggering the event.
     * @param fromClient        <code>true</code> if the event originated from the client side, <code>false</code> otherwise.
     * @param type              The type of the selected BPMN element.
     * @param businessObjectJson JSON representation of the business object associated with the selected element.
     */
    public SelectionChangedEvent(BpmnViewerImpl source,
                                 boolean fromClient,
                                 @EventData("event.$type") String type,
                                 @EventData("event.businessObject") String businessObjectJson) {
        super(source, fromClient);
        this.elementType = type;
        this.businessObjectJson = businessObjectJson;
    }

    /**
     * Gets the JSON representation of the business object associated with the selected element.
     *
     * @return The business object JSON.
     */
    public String getBusinessObjectJson() {
        return businessObjectJson;
    }

    /**
     * Gets the type of the selected BPMN element.
     *
     * @return The element type.
     */
    public String getElementType() {
        return elementType;
    }
}

