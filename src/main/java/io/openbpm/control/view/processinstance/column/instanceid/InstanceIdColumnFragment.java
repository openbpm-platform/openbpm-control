/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processinstance.column.instanceid;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.fragmentrenderer.FragmentRenderer;
import io.jmix.flowui.fragmentrenderer.RendererItemContainer;
import io.openbpm.control.entity.processinstance.RuntimeProcessInstanceData;

@FragmentDescriptor("instance-id-column-fragment.xml")
@RendererItemContainer("processInstanceDc")
public class InstanceIdColumnFragment extends FragmentRenderer<HorizontalLayout, RuntimeProcessInstanceData> {

}