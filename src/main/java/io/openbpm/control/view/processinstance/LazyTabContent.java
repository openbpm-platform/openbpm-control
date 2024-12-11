/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processinstance;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class LazyTabContent extends Div {
    protected SerializableSupplier<? extends Component> supplier;

    public LazyTabContent(SerializableSupplier<? extends Component> supplier) {
        addClassNames(LumoUtility.Width.FULL, LumoUtility.Height.FULL);
        initComponent(supplier);
        this.supplier = supplier;
    }

    protected void initComponent(SerializableSupplier<? extends Component> supplier) {
        addAttachListener(event -> {
            if (getElement().getChildCount() == 0) {
                add(supplier.get());
            }
        });
    }

    public void init() {
        if (getElement().getChildCount() == 0) {
            add(this.supplier.get());
        }
    }
}
