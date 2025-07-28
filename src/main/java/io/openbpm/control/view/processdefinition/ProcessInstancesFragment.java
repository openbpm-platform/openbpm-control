/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processdefinition;

import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.router.RouteParameters;
import io.jmix.core.AccessManager;
import io.jmix.core.DataLoadContext;
import io.jmix.core.Messages;
import io.jmix.core.Metadata;
import io.jmix.flowui.Fragments;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.accesscontext.UiEntityContext;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.pagination.SimplePagination;
import io.jmix.flowui.data.pagination.PaginationDataLoader;
import io.jmix.flowui.data.pagination.PaginationDataLoaderImpl;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.BaseCollectionLoader;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.HasLoader;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.Install;
import io.jmix.flowui.view.MessageBundle;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.Supply;
import io.jmix.flowui.view.ViewComponent;
import io.openbpm.control.entity.processdefinition.ProcessDefinitionData;
import io.openbpm.control.entity.processinstance.ProcessInstanceData;
import io.openbpm.control.service.processinstance.ProcessInstanceService;
import io.openbpm.control.view.processinstance.ProcessInstanceStateColumnFragment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static io.jmix.flowui.component.UiComponentUtils.getCurrentView;

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
    protected DataGrid<ProcessInstanceData> processInstancesGrid;

    @ViewComponent
    protected SimplePagination processInstancesPagination;
    @Autowired
    protected Fragments fragments;
    @Autowired
    protected AccessManager accessManager;
    @Autowired
    protected Metadata metadata;

    @Autowired
    protected UiComponents uiComponents;

    @Subscribe
    public void onReady(ReadyEvent event) {
        if (processInstanceDataDc instanceof HasLoader container && container.getLoader() instanceof BaseCollectionLoader) {
            PaginationDataLoader paginationLoader =
                    applicationContext.getBean(PaginationDataLoaderImpl.class, container.getLoader());
            processInstancesPagination.setPaginationLoader(paginationLoader);
        }
    }

    @Install(to = "processInstancesPagination", subject = "totalCountDelegate")
    protected Integer processInstancesPaginationTotalCountDelegate(final DataLoadContext dataLoadContext) {
        ProcessDefinitionData processDefinition = processDefinitionDataDc.getItem();
        return (int) processInstanceService.getCountByProcessDefinitionId(processDefinition.getProcessDefinitionId());
    }

    @Subscribe("processInstancesGrid.edit")
    public void onProcessDefinitionsGridViewDetails(ActionPerformedEvent event) {
        ProcessInstanceData selectedInstance = processInstancesGrid.getSingleSelectedItem();
        if (selectedInstance == null) {
            return;
        }
        openProcessInstanceDetailView(selectedInstance);
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

    @Supply(to = "processInstancesGrid.actions", subject = "renderer")
    protected Renderer<ProcessInstanceData> processInstancesGridActionsRenderer() {
        return new ComponentRenderer<>(processInstance -> {
            UiEntityContext context = new UiEntityContext(metadata.getClass(processInstance));
            accessManager.applyRegisteredConstraints(context);

            if (!context.isViewPermitted()) {
                return null;
            }

            JmixButton viewButton = uiComponents.create(JmixButton.class);
            viewButton.setIcon(VaadinIcon.EYE.create());
            viewButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
            viewButton.setText(messages.getMessage("actions.View"));
            viewButton.addClickListener(event -> openProcessInstanceDetailView(processInstance));
            return viewButton;
        });
    }

    protected void openProcessInstanceDetailView(ProcessInstanceData selectedInstance) {
        viewNavigators.detailView(getCurrentView(), ProcessInstanceData.class)
                .withRouteParameters(new RouteParameters("id", selectedInstance.getId()))
                .withBackwardNavigation(true)
                .navigate();
    }

}
