/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.uicomponent.spinner;

import io.jmix.flowui.xml.layout.loader.AbstractComponentLoader;
import org.vaadin.addons.componentfactory.spinner.Spinner;

public class SpinnerLoader extends AbstractComponentLoader<Spinner> {
    @Override
    protected Spinner createComponent() {
        return factory.create(Spinner.class);
    }

    @Override
    public void loadComponent() {
        loadBoolean(element, "loading", resultComponent::setLoading);
        loadString(element, "size", resultComponent::setSize);
        loadString(element, "color", resultComponent::setColor);

        componentLoader().loadClassNames(resultComponent, element);
    }
}
