/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processdefinition;


import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.openbpm.control.entity.processdefinition.ProcessDefinitionData;
import io.openbpm.control.service.processdefinition.ProcessDefinitionService;
import io.openbpm.control.view.bpmnviewer.BpmnViewerFragment;
import io.openbpm.control.view.main.MainView;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "process-definition-diagram", layout = MainView.class)
@ViewController("ProcessDefinitionDiagramView")
@ViewDescriptor("process-definition-diagram-view.xml")
@DialogMode(width = "90%", height = "90%", minWidth = "40em", minHeight = "25em")
public class ProcessDefinitionDiagramView extends StandardView {

    protected ProcessDefinitionData processDefinition;
    @Autowired
    protected ProcessDefinitionService processDefinitionService;
    @ViewComponent
    protected BpmnViewerFragment viewerFragment;
    @ViewComponent
    protected InstanceContainer<ProcessDefinitionData> processDefinitionDc;

    @SuppressWarnings("LombokSetterMayBeUsed")
    public void setProcessDefinition(ProcessDefinitionData processDefinitionId) {
        this.processDefinition = processDefinitionId;
    }

    @Subscribe
    public void onInit(final InitEvent event) {
        addClassNames(LumoUtility.Padding.Top.XSMALL, LumoUtility.Padding.Left.LARGE,
                LumoUtility.Padding.Right.LARGE);
        viewerFragment.showInDialog();
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        processDefinitionDc.setItem(processDefinition);

        String bpmnXml = processDefinitionService.getBpmnXml(processDefinition.getProcessDefinitionId());
        viewerFragment.initViewer(bpmnXml);
    }

}