/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.uicomponent.bpmnviewer.command;

import java.io.Serializable;

public class SetElementColorCmd implements Serializable {

    protected String elementId;
    protected String stroke;
    protected String fill;

    public SetElementColorCmd() {
    }

    public SetElementColorCmd(String elementId, String stroke, String fill) {
        this.elementId = elementId;
        this.stroke = stroke;
        this.fill = fill;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getStroke() {
        return stroke;
    }

    public void setStroke(String stroke) {
        this.stroke = stroke;
    }

    public String getFill() {
        return fill;
    }

    public void setFill(String fill) {
        this.fill = fill;
    }
}
