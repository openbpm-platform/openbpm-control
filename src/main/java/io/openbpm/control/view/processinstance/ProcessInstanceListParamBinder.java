/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processinstance;

import com.google.common.collect.ImmutableMap;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.QueryParameters;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.facet.UrlQueryParametersFacet;
import io.jmix.flowui.facet.urlqueryparameters.AbstractUrlQueryParametersBinder;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.model.InstanceContainer;
import io.openbpm.control.entity.filter.ProcessInstanceFilter;
import io.openbpm.control.entity.processinstance.ProcessInstanceData;
import io.openbpm.control.entity.processinstance.ProcessInstanceState;
import io.openbpm.control.view.processinstance.filter.ProcessInstanceStateHeaderFilter;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class ProcessInstanceListParamBinder extends AbstractUrlQueryParametersBinder {

    private static final String MODE_URL_PARAM = "mode";

    private final InstanceContainer<ProcessInstanceFilter> filterDc;
    private final CollectionLoader<ProcessInstanceData> processInstanceDl;
    private final ProcessInstanceStateHeaderFilter stateHeaderFilter;
    private final List<JmixButton> modeButtons;

    public ProcessInstanceListParamBinder(HorizontalLayout buttonsPanel,
                                          InstanceContainer<ProcessInstanceFilter> filterDc,
                                          CollectionLoader<ProcessInstanceData> processInstanceDl,
                                          DataGrid<ProcessInstanceData> dataGrid) {

        this.filterDc = filterDc;
        this.processInstanceDl = processInstanceDl;
        //noinspection DataFlowIssue
        this.stateHeaderFilter = (ProcessInstanceStateHeaderFilter) dataGrid.getColumnByKey("state").getHeaderComponent();

        this.modeButtons = IntStream.range(0, buttonsPanel.getComponentCount())
                .mapToObj(buttonIdx -> {
                    JmixButton modeBtn = (JmixButton) buttonsPanel.getComponentAt(buttonIdx);
                    modeBtn.addClickListener(clickEvent -> {
                        boolean active = modeBtn.hasThemeName("primary");
                        if (!active) {
                            activateModeButton(buttonIdx);
                        }
                    });
                    return modeBtn;
                }).toList();

    }


    @Override
    public Component getComponent() {
        return null;
    }

    @Override
    public void updateState(QueryParameters queryParameters) {
        List<String> modeStrings = queryParameters.getParameters().get(MODE_URL_PARAM);
        if (modeStrings != null) {
            ProcessInstanceViewMode mode = ProcessInstanceViewMode.fromId(modeStrings.getFirst());
            loadInstances(mode);
        }
    }

    private void activateModeButton(int activeButtonIdx) {
        updateButtons(activeButtonIdx);

        ProcessInstanceViewMode mode = ProcessInstanceViewMode.values()[activeButtonIdx];

        loadInstances(mode);

        QueryParameters qp = new QueryParameters(ImmutableMap.of(MODE_URL_PARAM, Collections.singletonList(mode.getId())));
        fireQueryParametersChanged(new UrlQueryParametersFacet.UrlQueryParametersChangeEvent(this, qp));

        stateHeaderFilter.update(mode);
    }

    private void updateButtons(int activeIdx) {
        IntStream.range(0, modeButtons.size())
                .forEach(idx -> {
                    JmixButton modeBtn = modeButtons.get(idx);
                    if (activeIdx == idx) {
                        modeBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                    } else {
                        modeBtn.removeThemeVariants(ButtonVariant.LUMO_PRIMARY);
                    }
                });
    }

    private void loadInstances(ProcessInstanceViewMode mode) {
        switch (mode) {
            case ALL -> {
                this.filterDc.getItem().setState(null);
                this.filterDc.getItem().setUnfinished(null);
                this.processInstanceDl.load();
            }
            case COMPLETED -> {
                this.filterDc.getItem().setState(ProcessInstanceState.COMPLETED);
                this.filterDc.getItem().setUnfinished(null);
                this.processInstanceDl.load();
            }
            case null, default -> {
                this.filterDc.getItem().setState(null);
                this.filterDc.getItem().setUnfinished(true);
                this.processInstanceDl.load();
            }
        }
    }
}
