/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.deploymenterror;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.fragmentrenderer.FragmentRenderer;
import io.jmix.flowui.fragmentrenderer.RendererItemContainer;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.ViewComponent;
import io.openbpm.control.entity.deployment.ResourceValidationError;
import org.apache.commons.lang3.StringUtils;

@RendererItemContainer("validationErrorDc")
@FragmentDescriptor("main-element-id-column-fragment.xml")
public class MainElementIdColumnFragment extends FragmentRenderer<HorizontalLayout, ResourceValidationError> {

    @ViewComponent
    protected JmixButton copyValueBtn;

    @Override
    public void setItem(ResourceValidationError item) {
        super.setItem(item);

        copyValueBtn.setVisible(StringUtils.isNotBlank(item.getMainElementId()));
    }
}