/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.view.processdefinition;

import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.flowset.control.entity.processdefinition.ProcessDefinitionData;
import io.jmix.flowui.view.StandardView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ProcessDefinitionBulkOperationView extends StandardView {
    @ViewComponent
    protected Icon allVersionsContextHelp;
    @ViewComponent
    protected Icon allInstancesContextHelp;

    protected Collection<ProcessDefinitionData> processDefinitions;


    @Subscribe
    public void onInit(final InitEvent event) {
        onInit();
    }

    protected void onInit() {
        addClassNames(LumoUtility.Padding.Top.NONE, LumoUtility.Padding.Left.LARGE);
        allVersionsContextHelp.addClassNames(LumoUtility.TextColor.SECONDARY);
        allInstancesContextHelp.addClassNames(LumoUtility.TextColor.SECONDARY);
    }

    @SuppressWarnings("LombokSetterMayBeUsed")
    public void setProcessDefinitions(Collection<ProcessDefinitionData> processDefinitions) {
        this.processDefinitions = processDefinitions;
    }

    public Set<String> collectProcessDefinitionKeys() {
        return processDefinitions.stream()
                .map(ProcessDefinitionData::getKey)
                .collect(Collectors.toSet());
    }
}
