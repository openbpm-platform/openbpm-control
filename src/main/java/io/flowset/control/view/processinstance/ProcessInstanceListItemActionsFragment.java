/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.view.processinstance;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.flowset.control.entity.processinstance.ProcessInstanceData;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import org.springframework.beans.factory.annotation.Autowired;

import static io.jmix.flowui.component.UiComponentUtils.getCurrentView;

@FragmentDescriptor("process-instance-list-item-actions-fragment.xml")
public class ProcessInstanceListItemActionsFragment extends Fragment<HorizontalLayout> {

    @Autowired
    protected ViewNavigators viewNavigators;

    @ViewComponent
    protected JmixButton viewDetailsBtn;

    protected ProcessInstanceData processInstance;

    @SuppressWarnings("LombokSetterMayBeUsed")
    public void setProcessInstance(ProcessInstanceData processInstance) {
        this.processInstance = processInstance;
    }

    @Subscribe
    public void onReady(ReadyEvent event) {
        getContent().addClassNames(LumoUtility.Width.AUTO);
        viewDetailsBtn.addClassNames(LumoUtility.Height.MEDIUM);
    }


    @Subscribe("viewDetailsBtn")
    public void onViewDetailsBtnClick(ClickEvent<Button> event) {
        viewNavigators.detailView(getCurrentView(), ProcessInstanceData.class)
                .withViewClass(ProcessInstanceDetailView.class)
                .withRouteParameters(new RouteParameters("id", processInstance.getId()))
                .withBackwardNavigation(true)
                .navigate();
    }

}
