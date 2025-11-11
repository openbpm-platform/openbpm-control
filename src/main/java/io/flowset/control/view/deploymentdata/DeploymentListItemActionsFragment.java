/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.flowset.control.view.deploymentdata;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.Messages;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.view.MessageBundle;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.View;
import io.jmix.flowui.view.ViewComponent;
import io.flowset.control.entity.deployment.DeploymentData;
import org.springframework.beans.factory.annotation.Autowired;

@FragmentDescriptor("deployment-list-item-actions-fragment.xml")
public class DeploymentListItemActionsFragment extends Fragment<HorizontalLayout> {

    @Autowired
    protected ViewNavigators viewNavigators;
    @Autowired
    protected DialogWindows dialogWindows;
    @Autowired
    protected Notifications notifications;
    @Autowired
    protected Messages messages;

    @ViewComponent
    protected MessageBundle messageBundle;
    @ViewComponent
    protected CollectionContainer<DeploymentData> deploymentDatasDc;
    @ViewComponent
    protected JmixButton viewDetailsBtn;

    protected DeploymentData deploymentData;

    public void setDeploymentData(DeploymentData deploymentData) {
        this.deploymentData = deploymentData;
    }

    @Subscribe
    public void onReady(ReadyEvent event) {
        viewDetailsBtn.addClassNames(LumoUtility.Height.MEDIUM);
    }

    @Subscribe("viewDetailsBtn")
    public void onViewDetailsBtnClick(final ClickEvent<Button> event) {
        viewNavigators.detailView(getView(), DeploymentData.class)
                .withViewClass(DeploymentDetailView.class)
                .withRouteParameters(new RouteParameters("id", deploymentData.getId()))
                .withBackwardNavigation(true)
                .navigate();
    }

    protected View<?> getView() {
        return (View<?>) getParentController();
    }
}
