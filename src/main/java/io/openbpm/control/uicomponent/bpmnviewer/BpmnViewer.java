/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.uicomponent.bpmnviewer;


import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.function.SerializableConsumer;
import elemental.json.JsonValue;
import io.openbpm.control.uicomponent.bpmnviewer.command.AddMarkerCmd;
import io.openbpm.control.uicomponent.bpmnviewer.command.RemoveMarkerCmd;
import io.openbpm.control.uicomponent.bpmnviewer.command.SetElementColorCmd;
import io.openbpm.control.uicomponent.bpmnviewer.command.SetIncidentCountCmd;
import io.openbpm.control.uicomponent.bpmnviewer.event.SelectionChangedEvent;

/**
 * Interface for a BPMN viewer component that allows visualization and interaction with BPMN diagrams.
 * This interface extends {@link HasElement} to integrate with Vaadin Flow.
 */
public interface BpmnViewer extends HasElement {

    /**
     * The name of the BPMN viewer component.
     */
    String NAME = "bpmnViewer";

    /**
     * Sets the BPMN XML for the viewer component and invokes the callback after the BPMN XML is set.
     *
     * @param bpmnXml  The BPMN XML to be set.
     * @param callback A callback to be invoked after the BPMN XML is set.
     */
    void setBpmnXml(String bpmnXml, SerializableConsumer<JsonValue> callback);

    /**
     * Sets the color of a BPMN element using the specified command.
     * <p>
     * Usage example:
     *
     * <pre>
     * bpmnViewer.setElementColor(new SetElementColorCmd(elementId,
     *    "#000", //stroke color
     *    "#c2d5ed" //fill color
     * ));
     * </pre>
     * @param cmd The command to set the element color.
     */
    void setElementColor(SetElementColorCmd cmd);

    /**
     * Adds a marker to a BPMN element using the specified command.
     * <p>
     * A style for the "highlighted" marker is already defined in the viewer component css file.
     * @param addMarkerCmd The command to add a marker to an element.
     */
    void addMarker(AddMarkerCmd addMarkerCmd);

    /**
     * Removes a marker from a BPMN element using the specified command.
     *
     * @param removeMarkerCmd The command to remove a marker from an element.
     */
    void removeMarker(RemoveMarkerCmd removeMarkerCmd);

    /**
     * Adds a listener to handle BPMN diagram element selection changes.
     *
     * @param listener The listener to handle selection changes.
     */
    void addSelectedElementChangedListener(ComponentEventListener<SelectionChangedEvent> listener);

    /**
     * Resets zoom and centralize a diagram position.
     */
    void resetZoom();

    /**
     * Sets an incident count for provided elements
     * @param cmd The command to set an incident count for elements.
     */
    void setIncidentCount(SetIncidentCountCmd cmd);
}