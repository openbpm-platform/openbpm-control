/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.deploymenterror;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.fragmentrenderer.FragmentRenderer;
import io.jmix.flowui.fragmentrenderer.RendererItemContainer;
import io.jmix.flowui.view.ViewComponent;
import io.openbpm.control.entity.deployment.ResourceValidationError;
import io.openbpm.control.entity.deployment.ValidationErrorType;

@RendererItemContainer("validationErrorDc")
@FragmentDescriptor("validation-error-type-column-fragment.xml")
public class ValidationErrorTypeColumnFragment extends FragmentRenderer<HorizontalLayout, ResourceValidationError> {

    @ViewComponent
    protected Span errorType;

    @Override
    public void setItem(ResourceValidationError item) {
        super.setItem(item);

        ValidationErrorType type = item.getType();
        switch (type) {
            case ERROR -> errorType.getElement().getThemeList().add("error");
            case WARNING -> errorType.getElement().getThemeList().add("warning");
            case null -> errorType.setVisible(false);
            default -> errorType.getElement().getThemeList().add("contrast");
        }
    }
}