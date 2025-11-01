/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.view.processinstance.column.state;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.fragmentrenderer.FragmentRenderer;
import io.jmix.flowui.fragmentrenderer.RendererItemContainer;
import io.jmix.flowui.view.ViewComponent;
import io.flowset.control.entity.processinstance.RuntimeProcessInstanceData;
import io.flowset.control.view.util.ComponentHelper;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;

@FragmentDescriptor("process-instance-state-column-fragment.xml")
@RendererItemContainer("processInstanceDataDc")
public class ProcessInstanceStateColumnFragment extends FragmentRenderer<HorizontalLayout, RuntimeProcessInstanceData> {

    @Autowired
    protected ComponentHelper componentHelper;
    @ViewComponent
    protected Icon incidentIcon;

    @Override
    public void setItem(RuntimeProcessInstanceData item) {
        super.setItem(item);

        Span processInstanceStateBadge = componentHelper.createProcessInstanceStateBadge(item.getState());
        getContent().addComponentAsFirst(processInstanceStateBadge);

        incidentIcon.setVisible(BooleanUtils.isTrue(item.getHasIncidents()));
    }
}