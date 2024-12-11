/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.uicomponent.bpmnviewer.command;

public class RemoveMarkerCmd {

    protected String elementId;
    protected String marker;

    public RemoveMarkerCmd() {
    }

    public RemoveMarkerCmd(String elementId, String marker) {
        this.elementId = elementId;
        this.marker = marker;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getMarker() {
        return marker;
    }

    public void setMarker(String marker) {
        this.marker = marker;
    }
}
