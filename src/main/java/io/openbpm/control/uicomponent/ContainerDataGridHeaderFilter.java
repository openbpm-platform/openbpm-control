/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.uicomponent;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.flowui.app.datagrid.HeaderPropertyFilterLayout;
import io.jmix.flowui.component.grid.DataGridColumn;
import io.jmix.flowui.component.grid.headerfilter.DataGridHeaderFilter;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.InstanceContainer;

public abstract class ContainerDataGridHeaderFilter<T, V> extends DataGridHeaderFilter {
    protected InstanceContainer<T> filterDc;

    public ContainerDataGridHeaderFilter(Grid<V> dataGrid,
                                         DataGridColumn<V> column,
                                         InstanceContainer<T> filterDc) {
        super(new HeaderFilterContext(dataGrid, column));
        this.filterDc = filterDc;
    }

    @Override
    protected void initOverlay() {
        Component component = createFilterComponent();

        JmixButton resetButton = createResetButton();
        JmixButton applyButton = createApplyButton();
        JmixButton cancelButton = createCancelButton();

        initFilterDialog(component, resetButton, applyButton, cancelButton);
    }

    protected abstract Component createFilterComponent();

    protected void initFilterDialog(Component filterDialogContent, Component... actionComponents) {
        HeaderPropertyFilterLayout headerPropertyFilterLayout = uiComponents.create(HeaderPropertyFilterLayout.class);
        headerPropertyFilterLayout.addClassNames(LumoUtility.AlignItems.BASELINE, LumoUtility.Width.FULL);
        headerPropertyFilterLayout.getContent().add(filterDialogContent);

        overlay = new Dialog(headerPropertyFilterLayout);
        overlay.addClassName(COLUMN_FILTER_DIALOG_CLASSNAME);

        if (!isSmallDevice()) {
            overlay.addClassName(COLUMN_FILTER_POPUP_CLASSNAME);
        } else {
            overlay.addClassName(COLUMN_FILTER_FOOTER_SMALL_CLASSNAME);
        }

        overlay.getFooter().add(actionComponents);
        overlay.addOpenedChangeListener(this::onOpenOverlay);
        overlay.addDialogCloseActionListener(this::onOverlayClose);
    }


    protected JmixButton createResetButton() {
        JmixButton resetButton = uiComponents.create(JmixButton.class);
        resetButton.setIcon(VaadinIcon.ERASER.create());
        resetButton.setText(messages.getMessage("actions.Clear"));
        resetButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        resetButton.addClassNames(LumoUtility.Margin.Right.AUTO);
        resetButton.addClickListener(this::onResetButtonClick);

        return resetButton;
    }

    protected void onResetButtonClick(ClickEvent<Button> buttonClickEvent) {
        resetFilterValues();
    }

    protected void resetFilterValues() {

    }
}