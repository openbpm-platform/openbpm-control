/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.view.deploymenterror;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.popover.Popover;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;

import java.util.Optional;

@FragmentDescriptor("validation-message-tooltip-fragment.xml")
public class ValidationMessageTooltipFragment extends Fragment<HorizontalLayout> {

    @ViewComponent
    private Span errorMessage;

    public void setText(String text) {
        errorMessage.setText(text);
    }

    @Subscribe(id = "closeBtn", subject = "clickListener")
    protected void onCloseBtnClick(final ClickEvent<JmixButton> event) {
        Popover popover = findPopover(this);
        if (popover != null) {
            popover.close();
        }
    }

    protected Popover findPopover(Component component) {
        if (component instanceof Popover) {
            return (Popover) component;
        } else {
            Optional<Component> parent = component.getParent();
            return parent.map(this::findPopover).orElse(null);
        }
    }
}