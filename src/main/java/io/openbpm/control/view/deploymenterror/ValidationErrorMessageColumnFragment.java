/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.deploymenterror;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import io.jmix.flowui.Fragments;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.fragmentrenderer.FragmentRenderer;
import io.jmix.flowui.fragmentrenderer.RendererItemContainer;
import io.jmix.flowui.view.ViewComponent;
import io.openbpm.control.entity.deployment.ResourceValidationError;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

@RendererItemContainer("validationErrorDc")
@FragmentDescriptor("validation-error-message-column-fragment.xml")
public class ValidationErrorMessageColumnFragment extends FragmentRenderer<HorizontalLayout, ResourceValidationError> {
    @Autowired
    protected Fragments fragments;

    @ViewComponent
    protected Span errorMessageText;

    @Override
    public void setItem(ResourceValidationError item) {
        super.setItem(item);

        String message = item.getMessage();
        if (StringUtils.isNotEmpty(message)) {
            ValidationMessageTooltipFragment tooltipFragment = fragments.create(this, ValidationMessageTooltipFragment.class);
            tooltipFragment.setText(message);

            Popover popover = new Popover(tooltipFragment);
            popover.setPosition(PopoverPosition.BOTTOM_START);
            popover.setTarget(errorMessageText);
        }
    }

}