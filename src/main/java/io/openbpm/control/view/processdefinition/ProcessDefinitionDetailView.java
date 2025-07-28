/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processdefinition;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.LoadContext;
import io.jmix.core.Messages;
import io.jmix.core.Metadata;
import io.jmix.core.Sort;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.UiEventPublisher;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.codeeditor.CodeEditor;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.model.DataLoader;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import io.openbpm.control.entity.filter.ProcessDefinitionFilter;
import io.openbpm.control.entity.filter.ProcessInstanceFilter;
import io.openbpm.control.entity.processdefinition.ProcessDefinitionData;
import io.openbpm.control.entity.processinstance.ProcessInstanceData;
import io.openbpm.control.service.processdefinition.ProcessDefinitionLoadContext;
import io.openbpm.control.service.processdefinition.ProcessDefinitionService;
import io.openbpm.control.service.processinstance.ProcessInstanceLoadContext;
import io.openbpm.control.service.processinstance.ProcessInstanceService;
import io.openbpm.control.view.event.TitleUpdateEvent;
import io.openbpm.uikit.fragment.bpmnviewer.BpmnViewerFragment;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

@Route(value = "bpm/process-definitions/:id", layout = DefaultMainViewParent.class)
@ViewController("bpm_ProcessDefinition.detail")
@ViewDescriptor("process-definition-detail-view.xml")
@EditedEntityContainer("processDefinitionDataDc")
@DialogMode(width = "50em", height = "37.5em")
@PrimaryDetailView(ProcessDefinitionData.class)
public class ProcessDefinitionDetailView extends StandardDetailView<ProcessDefinitionData> {
    public static final CloseAction REMOVE_PROCESS_DEFINITION_CLOSE_ACTION = new StandardCloseAction("removeProcessDefinition");

    protected String title = "";

    @Autowired
    protected Dialogs dialogs;
    @Autowired
    protected DialogWindows dialogWindows;
    @ViewComponent
    protected MessageBundle messageBundle;
    @Autowired
    protected ViewNavigators viewNavigators;
    @Autowired
    protected Notifications notifications;
    @Autowired
    protected Messages messages;
    @Autowired
    protected UiComponents uiComponents;
    @Autowired
    protected UiEventPublisher uiEventPublisher;
    @Autowired
    protected Metadata metadata;

    @ViewComponent
    protected InstanceContainer<ProcessDefinitionData> processDefinitionDataDc;
    @ViewComponent
    protected DataLoader processDefinitionDataDl;

    @ViewComponent
    protected CollectionContainer<ProcessInstanceData> processInstanceDataDc;
    @ViewComponent
    protected CollectionLoader<ProcessInstanceData> processInstanceDataDl;


    @ViewComponent
    protected ComboBox<ProcessDefinitionData> versionComboBox;

    @Autowired
    protected ProcessInstanceService processInstanceService;
    @Autowired
    protected ProcessDefinitionService processDefinitionService;

    @ViewComponent
    protected ProcessInstancesFragment processInstancesFragment;

    @ViewComponent
    protected BpmnViewerFragment viewerFragment;

    @ViewComponent
    protected CodeEditor bpmnXmlEditor;

    @ViewComponent
    protected Span allVersionsInstancesCountSpan;

    @ViewComponent
    protected GeneralPanelFragment generalPanel;

    @ViewComponent("tabsheet.processInstancesTab")
    protected Tab tabsheetProcessInstancesTab;

    @ViewComponent("tabsheet.bpmnXmlTab")
    protected Tab tabsheetBpmnXmlTab;

    @Subscribe
    public void onInit(final InitEvent event) {
        addClassNames(LumoUtility.Padding.Top.NONE);
        initTabIcons();
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        initVersionLookup(getEditedEntity());

        updateInstancesCount();
        String processDefinitionBpmnXml = processDefinitionService.getBpmnXml(getEditedEntity().getProcessDefinitionId());

        viewerFragment.initViewer(processDefinitionBpmnXml);
        generalPanel.refresh();
    }

    @Subscribe
    public void onReady(final ReadyEvent event) {
        sendUpdateViewTitleEvent();
    }

    @Subscribe(id = "processInstanceDataDc", target = Target.DATA_CONTAINER)
    public void onProcessInstanceDataDcCollectionChange(final CollectionContainer.CollectionChangeEvent<ProcessInstanceData> event) {
        updateInstancesCount();
    }

    @Override
    public String getPageTitle() {
        return title;
    }

    protected void sendUpdateViewTitleEvent() {
        this.title = messageBundle.formatMessage("processDefinitionDetail.processName", getEditedEntity().getKey());

        FlexLayout flexLayout = uiComponents.create(FlexLayout.class);
        flexLayout.addClassNames(LumoUtility.Margin.Left.XSMALL, LumoUtility.Gap.SMALL);

        Span version = uiComponents.create(Span.class);
        version.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.FontWeight.BOLD);
        version.setText(messageBundle.formatMessage("processDefinitionDetail.processVersion", getEditedEntity().getVersion()));

        boolean active = BooleanUtils.isNotTrue(getEditedEntity().getSuspended());
        String text = active ? messageBundle.getMessage("active") : messageBundle.getMessage("suspended");
        Span badge = createStateBadge(active, text);

        flexLayout.add(version, badge);

        uiEventPublisher.publishEventForCurrentUI(new TitleUpdateEvent(this, title, flexLayout));
    }

    protected void initTabIcons() {
        tabsheetProcessInstancesTab.addComponentAsFirst(VaadinIcon.TASKS.create());
        tabsheetBpmnXmlTab.addComponentAsFirst(VaadinIcon.FILE_CODE.create());
    }


    @Subscribe("versionComboBox")
    protected void onVersionLookupValueChange(AbstractField.ComponentValueChangeEvent<ComboBox<ProcessDefinitionData>, ProcessDefinitionData> event) {
        ProcessDefinitionData selectedProcessDefinition = event.getValue();
        processDefinitionDataDc.setItem(selectedProcessDefinition);

        updateInstancesCount();
        generalPanel.refresh();

        sendUpdateViewTitleEvent();
    }

    @Subscribe(id = "processDefinitionDataDc", target = Target.DATA_CONTAINER)
    protected void onProcessDefinitionDcItemChange(InstanceContainer.ItemChangeEvent<ProcessDefinitionData> event) {
        ProcessDefinitionData processDefinition = event.getItem();


        String bpmnXml = "";
        if (processDefinition != null) {
            bpmnXml = processDefinitionService.getBpmnXml(processDefinition.getProcessDefinitionId());
        }

        viewerFragment.initViewer(bpmnXml);
        bpmnXmlEditor.setValue(bpmnXml);

        processInstanceDataDl.load();

        sendUpdateViewTitleEvent();
    }

    @Install(to = "processInstanceDataDl", target = Target.DATA_LOADER)
    protected List<ProcessInstanceData> processInstanceDataDlLoadDelegate(final LoadContext<ProcessInstanceData> loadContext) {
        ProcessInstanceFilter filter = metadata.create(ProcessInstanceFilter.class);
        filter.setProcessDefinitionId(processDefinitionDataDc.getItem().getId());
        filter.setUnfinished(true);

        ProcessInstanceLoadContext context = new ProcessInstanceLoadContext().setFilter(filter);

        LoadContext.Query query = loadContext.getQuery();
        if (query != null) {
            context = context.setFirstResult(query.getFirstResult())
                    .setMaxResults(query.getMaxResults())
                    .setSort(query.getSort());
        }

        return processInstanceService.findAllHistoricInstances(context);
    }

    @Install(to = "processDefinitionDataDl", target = Target.DATA_LOADER)
    protected ProcessDefinitionData loadProcessDefinition(LoadContext<ProcessDefinitionData> loadContext) {
        ProcessDefinitionData item = processDefinitionDataDc.getItemOrNull();
        String id = item == null ? Objects.requireNonNull(loadContext.getId()).toString() : item.getId();
        return processDefinitionService.getById(id);
    }

    protected void updateInstancesCount() {
        ProcessDefinitionData item = processDefinitionDataDc.getItem();

        long currentVersionInstancesCount = processInstanceService.getCountByProcessDefinitionId(item.getProcessDefinitionId());
        updateTabCaption(currentVersionInstancesCount);

        long allVersionsInstancesCount = processInstanceService.getCountByProcessDefinitionKey(item.getKey());
        allVersionsInstancesCountSpan.setText(String.valueOf(allVersionsInstancesCount));
    }

    protected void updateTabCaption(long count) {
        tabsheetProcessInstancesTab.setLabel(messageBundle.formatMessage("processInstancesTab.label", count));
        tabsheetProcessInstancesTab.addComponentAsFirst(VaadinIcon.TASKS.create());
    }

    protected void initVersionLookup(ProcessDefinitionData processDefinition) {
        ProcessDefinitionFilter filter = metadata.create(ProcessDefinitionFilter.class);
        filter.setLatestVersionOnly(false);
        filter.setKey(processDefinition.getKey());

        List<ProcessDefinitionData> optionsList = processDefinitionService.findAll(new ProcessDefinitionLoadContext()
                        .setFilter(filter)
                        .setSort(Sort.by(Sort.Direction.DESC, "version")));
        versionComboBox.setItems(optionsList);
        versionComboBox.setItemLabelGenerator(item -> item.getVersion() != null ? String.valueOf(item.getVersion()) : null);
        versionComboBox.setValue(processDefinition);
    }

    protected Span createStateBadge(boolean success, String text) {
        Span badge = uiComponents.create(Span.class);
        String themeNames = success ? "badge success pill" : "badge warning pill";
        badge.getElement().getThemeList().add(themeNames);

        badge.setText(text);
        return badge;
    }
}
