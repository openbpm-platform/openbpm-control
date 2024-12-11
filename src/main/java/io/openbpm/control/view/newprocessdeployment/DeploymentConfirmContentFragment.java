/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.newprocessdeployment;

import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.openbpm.control.entity.processdefinition.ProcessDefinitionData;
import io.openbpm.control.uicomponent.bpmnviewer.BpmProcessDefinition;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@FragmentDescriptor("deployment-confirm-content-fragment.xml")
public class DeploymentConfirmContentFragment extends Fragment<VerticalLayout> {
    @ViewComponent
    protected VerticalLayout existingProcessVBox;

    @ViewComponent
    protected Icon warningIcon;

    @ViewComponent
    protected H5 warningHeader;

    @ViewComponent
    protected UnorderedList existingProcessesList;

    @ViewComponent
    protected UnorderedList deployingProcessesList;

    @Subscribe
    public void onReady(ReadyEvent event) {
        initExistingProcessesWarningStyles();
        deployingProcessesList.addClassName(LumoUtility.Margin.NONE);
    }

    public void setExistingProcesses(List<ProcessDefinitionData> existingProcesses) {
        if (CollectionUtils.isNotEmpty(existingProcesses)) {
            existingProcessVBox.setVisible(true);
            existingProcesses.forEach(processDefinitionData -> {
                ListItem listItem = uiComponents.create(ListItem.class);
                listItem.setText(processDefinitionData.getKey());
                existingProcessesList.add(listItem);
            });
        }
    }

    public void setDeployingProcesses(List<BpmProcessDefinition> deployingProcesses) {
        deployingProcesses.forEach(processDefinitionData -> {
            ListItem listItem = uiComponents.create(ListItem.class);
            listItem.setText(processDefinitionData.getKey());
            deployingProcessesList.add(listItem);
        });
    }

    protected void initExistingProcessesWarningStyles() {
        warningIcon.addClassNames(LumoUtility.TextColor.WARNING);
        warningHeader.addClassNames(LumoUtility.TextColor.WARNING_CONTRAST);
        existingProcessVBox.addClassNames(LumoUtility.BorderColor.WARNING,
                LumoUtility.Border.ALL, LumoUtility.BorderRadius.LARGE,
                LumoUtility.Background.WARNING_10);
        existingProcessesList.addClassName(LumoUtility.Margin.NONE);
    }

}
