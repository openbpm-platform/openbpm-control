/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.uicomponent.bpmnviewer.command;

import java.util.List;

public class SetIncidentCountCmd {

    protected List<ElementIncidentData> elements;

    public SetIncidentCountCmd() {
    }

    public SetIncidentCountCmd(List<ElementIncidentData> elements) {
        this.elements = elements;
    }

    public List<ElementIncidentData> getElements() {
        return elements;
    }

    public void setElements(List<ElementIncidentData> elements) {
        this.elements = elements;
    }
}
