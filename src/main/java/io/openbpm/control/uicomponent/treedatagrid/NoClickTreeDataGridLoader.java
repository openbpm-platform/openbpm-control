/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.uicomponent.treedatagrid;

import io.jmix.flowui.component.grid.TreeDataGrid;
import io.jmix.flowui.xml.layout.loader.component.TreeDataGridLoader;

public class NoClickTreeDataGridLoader extends TreeDataGridLoader {
    @Override
    protected TreeDataGrid<?> createComponent() {
        return factory.create(NoClickTreeGrid.class);
    }
}
