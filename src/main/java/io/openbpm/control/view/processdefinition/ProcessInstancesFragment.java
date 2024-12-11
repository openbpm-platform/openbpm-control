/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processdefinition;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.DataLoadContext;
import io.jmix.core.Messages;
import io.jmix.flowui.Fragments;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.pagination.SimplePagination;
import io.jmix.flowui.data.pagination.PaginationDataLoader;
import io.jmix.flowui.data.pagination.PaginationDataLoaderImpl;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.BaseCollectionLoader;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.HasLoader;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import io.openbpm.control.entity.processdefinition.ProcessDefinitionData;
import io.openbpm.control.entity.processinstance.ProcessInstanceData;
import io.openbpm.control.service.processinstance.ProcessInstanceService;
import io.openbpm.control.view.processinstance.ProcessInstanceStateColumnFragment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static io.jmix.flowui.component.UiComponentUtils.getCurrentView;
import static io.openbpm.control.view.util.JsUtils.COPY_SCRIPT_TEXT;

@FragmentDescriptor("process-instances-fragment.xml")
public class ProcessInstancesFragment extends Fragment<VerticalLayout> {

    @Autowired
    protected ApplicationContext applicationContext;
    @Autowired
    protected Notifications notifications;
    @ViewComponent
    protected MessageBundle messageBundle;
    @Autowired
    protected Messages messages;
    @Autowired
    protected ViewNavigators viewNavigators;

    @ViewComponent
    protected InstanceContainer<ProcessDefinitionData> processDefinitionDataDc;
    @ViewComponent
    protected CollectionContainer<ProcessInstanceData> processInstanceDataDc;

    @Autowired
    protected ProcessInstanceService processInstanceService;

    @ViewComponent
    protected VerticalLayout processInstanceVBox;
    @ViewComponent
    protected Span currentVersionsInstancesCountSpan;
    @ViewComponent
    protected Span allVersionsInstancesCountSpan;
    @ViewComponent
    protected DataGrid<ProcessInstanceData> processInstancesGrid;

    @ViewComponent
    protected SimplePagination processInstancesPagination;
    @Autowired
    protected Fragments fragments;

    @Subscribe
    public void onReady(ReadyEvent event) {
        initProcessInstanceGroupStyles();
        if (processInstanceDataDc instanceof HasLoader container && container.getLoader() instanceof BaseCollectionLoader) {
            PaginationDataLoader paginationLoader =
                    applicationContext.getBean(PaginationDataLoaderImpl.class, container.getLoader());
            processInstancesPagination.setPaginationLoader(paginationLoader);
        }
    }

    @Subscribe(target = Target.HOST_CONTROLLER)
    public void onHostBeforeShow(View.BeforeShowEvent event) {
        initInstancesCountLabels();
    }

    @Install(to = "processInstancesPagination", subject = "totalCountDelegate")
    protected Integer processInstancesPaginationTotalCountDelegate(final DataLoadContext dataLoadContext) {
        ProcessDefinitionData processDefinition = processDefinitionDataDc.getItem();
        return (int) processInstanceService.getCountByProcessDefinitionId(processDefinition.getProcessDefinitionId());
    }

    @Subscribe("processInstancesGrid.edit")
    public void onProcessDefinitionsGridViewDetails(ActionPerformedEvent event) {
        if (processInstancesGrid.getSingleSelectedItem() == null) {
            return;
        }
        viewNavigators.detailView(getCurrentView(), ProcessInstanceData.class)
                .withRouteParameters(new RouteParameters("id", processInstancesGrid.getSingleSelectedItem().getId()))
                .withBackwardNavigation(true)
                .navigate();
    }

    @Supply(to = "processInstancesGrid.id", subject = "renderer")
    protected Renderer<ProcessInstanceData> processInstancesGridIdRenderer() {
        return new ComponentRenderer<>(this::createProcessInstanceIdComponent);
    }

    @Install(to = "processInstancesGrid.id", subject = "tooltipGenerator")
    protected String processInstancesGridIdTooltipGenerator(final ProcessInstanceData processInstanceData) {
        return processInstanceData.getInstanceId();
    }

    @Supply(to = "processInstancesGrid.state", subject = "renderer")
    protected Renderer<ProcessInstanceData> processInstancesGridStateRenderer() {
        return new ComponentRenderer<>(processInstanceData -> {
            ProcessInstanceStateColumnFragment processInstancesFragment = fragments.create(this, ProcessInstanceStateColumnFragment.class);
            processInstancesFragment.setItem(processInstanceData);
            return processInstancesFragment;
        });
    }

    public void initInstancesCountLabels() {
        ProcessDefinitionData item = processDefinitionDataDc.getItem();
        long currentVersionInstancesCount = processInstanceService.getCountByProcessDefinitionId(item.getProcessDefinitionId());

        long allVersionsInstancesCount = processInstanceService.getCountByProcessDefinitionKey(item.getKey());
        currentVersionsInstancesCountSpan.setText(": " + currentVersionInstancesCount);
        allVersionsInstancesCountSpan.setText(": " + allVersionsInstancesCount);
    }

    protected void initProcessInstanceGroupStyles() {
        processInstanceVBox.addClassNames(LumoUtility.Padding.Top.SMALL, LumoUtility.Padding.Left.XSMALL);
        allVersionsInstancesCountSpan.addClassNames(LumoUtility.FontWeight.BOLD);
        currentVersionsInstancesCountSpan.addClassNames(LumoUtility.FontWeight.BOLD);
    }

    protected HorizontalLayout createProcessInstanceIdComponent(ProcessInstanceData processInstanceData) {
        HorizontalLayout layout = uiComponents.create(HorizontalLayout.class);
        layout.setSpacing(false);
        Span span = uiComponents.create(Span.class);
        span.addClassNames(LumoUtility.TextOverflow.ELLIPSIS, LumoUtility.Overflow.HIDDEN, LumoUtility.Width.FULL);
        span.setText(processInstanceData.getInstanceId());
        layout.addClassNames(LumoUtility.Overflow.HIDDEN);

        Button button = createCopyProcessInstanceIdButton(processInstanceData);


        layout.addAndExpand(span);
        layout.add(button);
        return layout;
    }

    protected Button createCopyProcessInstanceIdButton(ProcessInstanceData processInstanceData) {
        Button button = uiComponents.create(Button.class);
        button.setIcon(VaadinIcon.COPY_O.create());
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        button.addClassNames(LumoUtility.TextColor.SECONDARY);
        button.addClickListener(event -> {
            Element buttonElement = event.getSource().getElement();
            String valueToCopy = processInstanceData.getInstanceId();
            buttonElement.executeJs(COPY_SCRIPT_TEXT, valueToCopy)
                    .then(successResult -> notifications.create(messageBundle.getMessage("processInstanceIdCopied"))
                                    .withPosition(Notification.Position.TOP_END)
                                    .withThemeVariant(NotificationVariant.LUMO_SUCCESS)
                                    .show(),
                            errorResult -> notifications.create(messageBundle.getMessage("processInstanceIdCopyFailed"))
                                    .withPosition(Notification.Position.TOP_END)
                                    .withThemeVariant(NotificationVariant.LUMO_ERROR)
                                    .show());
        });
        return button;
    }

}
