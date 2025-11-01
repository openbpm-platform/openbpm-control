/*
 * Copyright (c) Haulmont 2025. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.uicomponent.menu;

import io.jmix.flowui.component.main.JmixListMenu;
import io.jmix.flowui.xml.layout.loader.component.ListMenuLoader;

/**
 * Loader of the {@link ControlListMenu} component.
 */
public class ControlListMenuLoader extends ListMenuLoader {

    @Override
    protected JmixListMenu createComponent() {
        return factory.create(ControlListMenu.class);
    }
}
