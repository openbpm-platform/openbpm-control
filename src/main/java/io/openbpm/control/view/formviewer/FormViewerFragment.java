/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.formviewer;

import com.vaadin.flow.component.html.Div;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.view.ViewComponent;
import io.openbpm.control.uicomponent.formviewer.FormViewer;

@FragmentDescriptor("form-viewer-fragment.xml")
public class FormViewerFragment extends Fragment<Div> {

    @ViewComponent
    protected Div viewerContainer;

    protected FormViewer formViewer;

    public void initViewer(String bpmnXml) {
        this.formViewer = uiComponents.create(FormViewer.class);
        this.formViewer.setFormJson(bpmnXml);
        viewerContainer.removeAll();
        viewerContainer.add(formViewer);
    }
}
