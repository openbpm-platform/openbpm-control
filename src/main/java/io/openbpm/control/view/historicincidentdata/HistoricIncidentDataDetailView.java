/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.historicincidentdata;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.LoadContext;
import io.jmix.core.Messages;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.*;
import io.openbpm.control.entity.incident.HistoricIncidentData;
import io.openbpm.control.service.incident.IncidentService;
import io.openbpm.control.view.externaltask.ExternalTaskErrorDetailsView;
import io.openbpm.control.view.job.JobErrorDetailsView;
import io.openbpm.control.view.main.MainView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@Route(value = "historic-incidents/:id", layout = MainView.class)
@ViewController("HistoricIncidentData.detail")
@ViewDescriptor("historic-incident-data-detail-view.xml")
@EditedEntityContainer("historicIncidentDataDc")
public class HistoricIncidentDataDetailView extends StandardDetailView<HistoricIncidentData> {

    @Autowired
    protected IncidentService incidentService;
    @ViewComponent
    protected JmixButton viewStacktraceBtn;

    @ViewComponent
    protected TypedTextField<Object> configurationField;
    @Autowired
    protected Messages messages;
    @ViewComponent
    protected TypedTextField<Object> causeIncidentIdField;
    @ViewComponent
    protected TypedTextField<Object> rootCauseIncidentIdField;
    @Autowired
    protected DialogWindows dialogWindows;

    @Subscribe
    public void onInit(final InitEvent event) {
        addClassNames(LumoUtility.Padding.Top.XSMALL);
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        initIncidentTypeRelatedFields();
        initCauseIncidentFields();
        initRootCauseIncidentFields();
    }

    @Install(to = "historicIncidentDataDl", target = Target.DATA_LOADER)
    protected HistoricIncidentData historicIncidentDataDlLoadDelegate(final LoadContext<HistoricIncidentData> loadContext) {
        Object id = loadContext.getId();
        if (id != null) {
            return incidentService.findHistoricIncidentById(id.toString());
        }
        return null;
    }

    protected void initIncidentTypeRelatedFields() {
        boolean notEmptyPayload = getEditedEntity().getConfiguration() != null;

        if (getEditedEntity().isExternalTaskFailed()) {
            viewStacktraceBtn.setVisible(notEmptyPayload);
            configurationField.setLabel(messages.getMessage("io.openbpm.control.view.incidentdata/externalTaskIdLabel"));
        } else if (getEditedEntity().isJobFailed()) {
            configurationField.setLabel(messages.getMessage("io.openbpm.control.view.incidentdata/jobIdLabel"));
            viewStacktraceBtn.setVisible(notEmptyPayload);
        } else {
            viewStacktraceBtn.setVisible(false);
        }
    }

    @Subscribe(id = "viewStacktraceBtn", subject = "clickListener")
    public void onViewStacktraceBtnClick(final ClickEvent<JmixButton> event) {
        if (getEditedEntity().isJobFailed()) {
            dialogWindows.view(this, JobErrorDetailsView.class)
                    .withViewConfigurer(view -> {
                        view.setJobId(getEditedEntity().getConfiguration());
                        view.fromHistory();
                    })
                    .build()
                    .open();
        } else if (getEditedEntity().isExternalTaskFailed()) {
            dialogWindows.view(this, ExternalTaskErrorDetailsView.class)
                    .withViewConfigurer(view -> {
                        view.setExternalTaskId(getEditedEntity().getConfiguration());
                        view.fromHistory();
                    })
                    .build()
                    .open();
        }
    }

    protected void initCauseIncidentFields() {
        String causeIncidentLabel;
        String causeIncidentId = getEditedEntity().getCauseIncidentId();
        if (StringUtils.equals(getEditedEntity().getIncidentId(), causeIncidentId)) {
            causeIncidentLabel = messages.formatMessage("io.openbpm.control.view.incidentdata", "incidentWithProcess", causeIncidentId, getEditedEntity().getProcessDefinitionKey());
        } else {
            causeIncidentLabel = getRelatedIncidentFieldLabel(causeIncidentId);
        }
        causeIncidentIdField.setValue(causeIncidentLabel);
    }

    protected String getRelatedIncidentFieldLabel(String relatedIncidentId) {
        String relatedIncidentLabel;
        HistoricIncidentData relatedIncident = incidentService.findHistoricIncidentById(relatedIncidentId);
        if (relatedIncident != null) {
            relatedIncidentLabel = messages.formatMessage("io.openbpm.control.view.incidentdata", "incidentWithProcess", relatedIncidentId, relatedIncident.getProcessDefinitionKey());
        } else {
            relatedIncidentLabel = relatedIncidentId;
        }
        return relatedIncidentLabel;
    }

    protected void initRootCauseIncidentFields() {
        String rootCauseIncidentLabel;
        String rootCauseIncidentId = getEditedEntity().getRootCauseIncidentId();
        boolean sameIncident = StringUtils.equals(getEditedEntity().getIncidentId(), getEditedEntity().getRootCauseIncidentId());
        if (sameIncident) {
            rootCauseIncidentLabel = messages.formatMessage("io.openbpm.control.view.incidentdata", "incidentWithProcess", rootCauseIncidentId, getEditedEntity().getProcessDefinitionKey());
        } else {
            rootCauseIncidentLabel = getRelatedIncidentFieldLabel(rootCauseIncidentId);
        }
        rootCauseIncidentIdField.setValue(rootCauseIncidentLabel);
    }
}
