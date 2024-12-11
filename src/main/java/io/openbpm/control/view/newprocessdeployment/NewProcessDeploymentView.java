/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.newprocessdeployment;


import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.shared.Tooltip;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.Metadata;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.Fragments;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.action.DialogAction;
import io.jmix.flowui.component.upload.FileUploadField;
import io.jmix.flowui.kit.action.ActionVariant;
import io.jmix.flowui.kit.component.ComponentUtils;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.kit.component.upload.event.FileUploadSucceededEvent;
import io.jmix.flowui.view.*;
import io.openbpm.control.entity.filter.ProcessDefinitionFilter;
import io.openbpm.control.entity.processdefinition.ProcessDefinitionData;
import io.openbpm.control.service.deployment.DeploymentContext;
import io.openbpm.control.service.deployment.DeploymentService;
import io.openbpm.control.service.processdefinition.ProcessDefinitionLoadContext;
import io.openbpm.control.service.processdefinition.ProcessDefinitionService;
import io.openbpm.control.uicomponent.bpmnviewer.BpmProcessDefinition;
import io.openbpm.control.uicomponent.bpmnviewer.event.ImportCompleteEvent;
import io.openbpm.control.view.bpmnviewer.BpmnViewerFragment;
import io.openbpm.control.view.main.MainView;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.repository.DeploymentWithDefinitions;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.community.rest.exception.RemoteProcessEngineException;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.openbpm.control.util.BpmParseUtil.parseProcessDefinitionsJson;

@Route(value = "bpm/new-process-deployment", layout = MainView.class)
@ViewController("bpm_NewProcessDeploymentView")
@ViewDescriptor("new-process-deployment-view.xml")
@Slf4j
public class NewProcessDeploymentView extends StandardView {

    @ViewComponent
    protected FileUploadField bpmnXmlUploadField;

    @Autowired
    protected DeploymentService deploymentService;
    @Autowired
    protected ProcessDefinitionService processDefinitionService;

    @Autowired
    protected Metadata metadata;
    @Autowired
    protected MessageBundle messageBundle;
    @Autowired
    protected Notifications notifications;
    @Autowired
    protected UiComponents uiComponents;
    @Autowired
    protected Dialogs dialogs;
    @Autowired
    protected Fragments fragments;

    @ViewComponent
    protected VerticalLayout previewVBox;
    @ViewComponent
    protected Span emptyPreviewText;
    @ViewComponent
    protected HorizontalLayout emptyPreviewHBox;

    @ViewComponent
    protected JmixButton okBtn;

    @ViewComponent
    protected HorizontalLayout processInfoHBox;
    @ViewComponent
    protected Icon processCountInfoIcon;
    @ViewComponent
    protected H4 collaborationLabel;
    @ViewComponent
    protected Span processCountLabel;
    @ViewComponent
    protected H4 processLabel;
    @ViewComponent
    protected Span processIdLabel;

    @ViewComponent
    protected BpmnViewerFragment viewerFragment;

    protected List<BpmProcessDefinition> processDefinitions = new ArrayList<>();

    @Subscribe
    public void onInit(final InitEvent event) {
        bpmnXmlUploadField.addClassNames(LumoUtility.Padding.Top.NONE);
        initEmptyPreviewStyles();
        initProcessInfoHBoxStyles();
        viewerFragment.removeBorders();
    }

    @Subscribe(id = "okBtn", subject = "clickListener")
    public void onOkBtnClick(final ClickEvent<JmixButton> event) {
        byte[] uploadedXml = bpmnXmlUploadField.getValue();
        if (uploadedXml == null) {
            notifications.create(messageBundle.getMessage("bpmnFileNotSelected"))
                    .withType(Notifications.Type.ERROR)
                    .show();
            return;
        }
        List<ProcessDefinitionData> existingProcesses = findExistingProcessesByKeys();

        dialogs.createOptionDialog()
                .withHeader(messageBundle.getMessage("createDeploymentConfirmDialog.header"))
                .withContent(createConfirmDialogContent(existingProcesses))
                .withWidth("35em")
                .withActions(
                        new DialogAction(DialogAction.Type.YES)
                                .withHandler(e -> deployBpmnXml(uploadedXml))
                                .withText(messageBundle.getMessage("deploy"))
                                .withIcon(VaadinIcon.ROCKET)
                                .withVariant(ActionVariant.PRIMARY)
                        ,
                        new DialogAction(DialogAction.Type.CANCEL)
                                .withIcon(ComponentUtils.convertToIcon(VaadinIcon.BAN))
                )
                .open();

    }

    @Subscribe("bpmnXmlUploadField")
    public void onBpmnXmlUploadFieldComponentValueChange(final AbstractField.ComponentValueChangeEvent<FileUploadField, ?> event) {
        boolean emptyValue = event.getValue() == null;
        viewerFragment.setVisible(!emptyValue);

        emptyPreviewHBox.setVisible(emptyValue);

        okBtn.setEnabled(!emptyValue);
        if (emptyValue) {
            this.processDefinitions = new ArrayList<>();
            processInfoHBox.setVisible(false);
        }
    }

    @Subscribe("bpmnXmlUploadField")
    public void onBpmnXmlUploadFieldFileUploadSucceeded(final FileUploadSucceededEvent<FileUploadField> event) {
        if (bpmnXmlUploadField.getValue() != null) {
            String processDefinitionBpmnXml = new String(bpmnXmlUploadField.getValue(), StandardCharsets.UTF_8);

            viewerFragment.initViewer(processDefinitionBpmnXml);
            viewerFragment.addImportCompleteListener(this::updateImportedProcesses);
            viewerFragment.setVisible(true);

            emptyPreviewHBox.setVisible(false);
        }
    }

    @Subscribe(id = "cancelBtn", subject = "clickListener")
    public void onCancelBtnClick(final ClickEvent<JmixButton> event) {
        close(StandardOutcome.CLOSE);
    }

    protected void initEmptyPreviewStyles() {
        previewVBox.addClassNames(LumoUtility.Border.ALL,
                LumoUtility.BorderRadius.LARGE,
                LumoUtility.BorderColor.CONTRAST_20);
        emptyPreviewText.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.Width.LARGE, LumoUtility.Height.LARGE);
    }

    protected void initProcessInfoHBoxStyles() {
        processInfoHBox.addClassNames(LumoUtility.Margin.Left.AUTO);
        processCountLabel.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.TextColor.SECONDARY, LumoUtility.FontWeight.MEDIUM);
        processIdLabel.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.TextColor.SECONDARY, LumoUtility.FontWeight.MEDIUM);
        processCountInfoIcon.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.PRIMARY);
        setProcessCountTooltipClassName();
    }

    protected void deployBpmnXml(byte[] uploadedXml) {
        String uploadedFileName = bpmnXmlUploadField.getUploadedFileName();

        try (InputStream inputStream = new ByteArrayInputStream(uploadedXml)) {
            DeploymentWithDefinitions result = deploymentService.createDeployment(new DeploymentContext()
                    .withResource(uploadedFileName, inputStream));
            List<ProcessDefinition> deployedProcessDefinitions = result.getDeployedProcessDefinitions();
            int size = CollectionUtils.size(deployedProcessDefinitions);

            notifications.create(messageBundle.formatMessage("processesDeployed", size))
                    .withType(Notifications.Type.SUCCESS)
                    .withDuration(2000)
                    .show();

            close(StandardOutcome.SAVE);
        } catch (IOException ex) {
            log.error("Error on uploaded file reading ", ex);
            notifications.create(messageBundle.getMessage("errorOnUploadedFileReading"))
                    .withType(Notifications.Type.ERROR)
                    .withDuration(2000)
                    .show();
        } catch (RemoteProcessEngineException ex) {
            log.error("Error on process deployment", ex);
            notifications.create(messageBundle.getMessage("processesNotDeployed"))
                    .withType(Notifications.Type.ERROR)
                    .withDuration(2000)
                    .show();
        }
    }

    @Nullable
    protected List<ProcessDefinitionData> findExistingProcessesByKeys() {
        List<ProcessDefinitionData> existingProcesses = null;
        if (CollectionUtils.isNotEmpty(processDefinitions)) {
            ProcessDefinitionFilter filter = metadata.create(ProcessDefinitionFilter.class);
            filter.setKeyIn(processDefinitions.stream().map(BpmProcessDefinition::getKey).toList());
            existingProcesses = processDefinitionService.findAll(new ProcessDefinitionLoadContext().setFilter(filter));
        }
        return existingProcesses;
    }

    protected void updateImportedProcesses(ImportCompleteEvent importCompleteEvent) {
        this.processDefinitions = parseProcessDefinitionsJson(importCompleteEvent.getProcessDefinitionsJson());
        updateProcessCountComponents();
    }

    protected void updateProcessCountComponents() {
        boolean hasProcessDefinitions = !processDefinitions.isEmpty();
        if (!hasProcessDefinitions) {
            processInfoHBox.setVisible(false);
            return;
        }
        boolean multipleProcessDefinitions = processDefinitions.size() > 1;

        if (multipleProcessDefinitions) {
            Tooltip tooltip = processCountInfoIcon.getTooltip();
            String processDefinitionsString = getProcessDefinitionsString();
            tooltip.setText(processDefinitionsString);
            processCountLabel.setText(messageBundle.formatMessage("collaborationProcessesLabel", String.valueOf(processDefinitions.size())));
        } else {
            processIdLabel.setText(processDefinitions.getFirst().getKey());
        }

        processInfoHBox.setVisible(true);

        //Processes collaboration fields
        processCountInfoIcon.setVisible(multipleProcessDefinitions);
        processCountLabel.setVisible(multipleProcessDefinitions);
        collaborationLabel.setVisible(multipleProcessDefinitions);

        //Single process fields
        processIdLabel.setVisible(!multipleProcessDefinitions);
        processLabel.setVisible(!multipleProcessDefinitions);
    }

    protected String getProcessDefinitionsString() {
        return processDefinitions.stream().map(bpmProcessDefinition -> {
            int idx = processDefinitions.indexOf(bpmProcessDefinition) + 1;
            return messageBundle.formatMessage("importedProcessKeyAndName", idx, StringUtils.defaultIfEmpty(bpmProcessDefinition.getKey(), "-"),
                    StringUtils.defaultIfEmpty(bpmProcessDefinition.getName(), "-"));
        }).collect(Collectors.joining("\n"));
    }

    protected void setProcessCountTooltipClassName() {
        //workaround to update a CSS class name for tooltip
        processCountInfoIcon.getElement().executeJs(
                """
                           if ($0.getElementsByTagName('vaadin-tooltip').length == 1) {
                               $0.getElementsByTagName('vaadin-tooltip')[0]._overlayElement.setAttribute('class','process-tooltip');
                           } else {
                               const tooltips = document.getElementsByTagName('vaadin-tooltip');
                               for (let i=0; i<tooltips.length; i++ ) {
                                   const tooltip = tooltips[i];
                                   if (tooltip._overlayElement.id === $0.getAttribute('aria-describedBy')) {
                                       tooltip._overlayElement.setAttribute('class','process-tooltip')
                                   }
                               }
                           }
                        """, processCountInfoIcon);
    }

    protected VerticalLayout createConfirmDialogContent(List<ProcessDefinitionData> existingProcesses) {
        DeploymentConfirmContentFragment fragment = fragments.create(this, DeploymentConfirmContentFragment.class);
        fragment.setExistingProcesses(existingProcesses);
        fragment.setDeployingProcesses(processDefinitions);
        return fragment.getContent();
    }
}