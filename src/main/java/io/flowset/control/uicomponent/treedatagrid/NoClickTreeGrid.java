/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.uicomponent.treedatagrid;

import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.function.SerializableComparator;
import com.vaadin.flow.function.ValueProvider;
import io.jmix.flowui.component.grid.DataGridColumn;
import io.jmix.flowui.component.grid.TreeDataGrid;

import java.util.List;

public class NoClickTreeGrid<T> extends TreeDataGrid<T> {
    public NoClickTreeGrid() {
        super();
        addClassName("no-click-tree-grid");
    }

    @Override
    public DataGridColumn<T> addHierarchyColumn(ValueProvider<T, ?> valueProvider) {
        DataGridColumn<T> column = addColumn(LitRenderer.<T> of(
                        "<vaadin-grid-tree-toggle @click=${onClick} .leaf=${!item.children} .expanded=${model.expanded} .level=${model.level}>"
                                + "</vaadin-grid-tree-toggle><span class=\"no-click-tree-item-name\">${item.name}</span>")
                .withProperty("children",
                        item -> getDataCommunicator().hasChildren(item))
                .withProperty("name", value -> {
                    Object name = valueProvider.apply(value);
                    return name == null ? "" : String.valueOf(name);
                }).withFunction("onClick", item -> {
                    if (getDataCommunicator().hasChildren(item)) {
                        if (isExpanded(item)) {
                            collapse(List.of(item), true);
                        } else {
                            expand(List.of(item), true);
                        }
                    }
                }));

        final SerializableComparator<T> comparator =
                (a, b) -> compareMaybeComparables(valueProvider.apply(a),
                        valueProvider.apply(b));
        column.setComparator(comparator);

        return column;
    }
}
