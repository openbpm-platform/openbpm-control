/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.view.alltasks;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.fragmentrenderer.FragmentRenderer;
import io.jmix.flowui.fragmentrenderer.RendererItemContainer;
import io.jmix.flowui.view.ViewComponent;
import io.flowset.control.entity.UserTaskData;
import org.apache.commons.lang3.BooleanUtils;

@FragmentDescriptor("task-key-column-fragment.xml")
@RendererItemContainer("userTaskDataDc")
public class TaskKeyColumnFragment extends FragmentRenderer<HorizontalLayout, UserTaskData> {

    @ViewComponent
    protected Span suspendedBadge;

    @Override
    public void setItem(UserTaskData item) {
        super.setItem(item);

        suspendedBadge.setVisible(BooleanUtils.isTrue(item.getSuspended()));
    }
}