/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.bpmnviewer;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.openbpm.control.uicomponent.bpmnviewer.BpmnViewerImpl;
import io.openbpm.control.uicomponent.bpmnviewer.command.AddMarkerCmd;
import io.openbpm.control.uicomponent.bpmnviewer.command.SetElementColorCmd;
import io.openbpm.control.uicomponent.bpmnviewer.command.SetIncidentCountCmd;
import io.openbpm.control.uicomponent.bpmnviewer.event.ImportCompleteEvent;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;

@FragmentDescriptor("bpmn-viewer-fragment.xml")
public class BpmnViewerFragment extends Fragment<Div> {
    protected final static String BORDER_STYLES = String.join(" ", LumoUtility.Border.ALL, LumoUtility.BorderRadius.LARGE,
            LumoUtility.BorderColor.CONTRAST_30);

    @ViewComponent
    protected Div viewerVBox;
    @ViewComponent
    protected Div viewerContainer;

    protected BpmnViewerImpl bpmnViewer;

    @ViewComponent
    protected JmixButton zoomResetBtn;

    @Subscribe
    public void onReady(ReadyEvent event) {
        viewerVBox.addClassNames(BORDER_STYLES);
    }

    public void showInDialog() {
        zoomResetBtn.getStyle().setPosition(Style.Position.ABSOLUTE);
    }

    public void initViewer(String bpmnXml) {
        this.bpmnViewer = uiComponents.create(BpmnViewerImpl.class);
        this.bpmnViewer.setBpmnXml(bpmnXml);
        viewerContainer.removeAll();
        viewerContainer.add(bpmnViewer);
    }

    public void addMarker(AddMarkerCmd cmd) {
        if (this.bpmnViewer != null) {
            this.bpmnViewer.addMarker(cmd);
        }
    }

    public void setElementColor(SetElementColorCmd cmd) {
        if (this.bpmnViewer != null) {
            this.bpmnViewer.setElementColor(cmd);
        }
    }

    public void setIncidentCount(SetIncidentCountCmd cmd) {
        if (this.bpmnViewer != null) {
            this.bpmnViewer.setIncidentCount(cmd);
        }
    }

    public void removeBorders() {
        viewerVBox.removeClassNames(BORDER_STYLES);
    }

    public void addImportCompleteListener(ComponentEventListener<ImportCompleteEvent> listener) {
        if (bpmnViewer != null) {
            bpmnViewer.addImportCompleteListener(listener);
        }
    }

    @Subscribe(id = "zoomResetBtn", subject = "clickListener")
    public void onZoomResetBtnClick(final ClickEvent<JmixButton> event) {
        if (bpmnViewer != null) {
            bpmnViewer.resetZoom();
        }
    }
}