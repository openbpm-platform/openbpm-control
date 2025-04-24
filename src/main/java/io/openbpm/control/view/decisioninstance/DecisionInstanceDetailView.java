package io.openbpm.control.view.decisioninstance;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.LoadContext;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.DefaultMainViewParent;
import io.jmix.flowui.view.DialogMode;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.Install;
import io.jmix.flowui.view.PrimaryDetailView;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.Supply;
import io.jmix.flowui.view.Target;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.openbpm.control.action.CopyComponentValueToClipboardAction;
import io.openbpm.control.entity.activity.HistoricActivityInstanceData;
import io.openbpm.control.entity.decisioninstance.HistoricDecisionInputInstanceShortData;
import io.openbpm.control.entity.decisioninstance.HistoricDecisionInstanceShortData;
import io.openbpm.control.entity.decisioninstance.HistoricDecisionOutputInstanceShortData;
import io.openbpm.control.entity.processdefinition.ProcessDefinitionData;
import io.openbpm.control.entity.processinstance.ProcessInstanceData;
import io.openbpm.control.service.activity.ActivityService;
import io.openbpm.control.service.decisiondefinition.DecisionDefinitionService;
import io.openbpm.control.service.decisioninstance.DecisionInstanceService;
import io.openbpm.control.service.processinstance.ProcessInstanceService;
import io.openbpm.control.view.processdefinition.ProcessDefinitionDetailView;
import io.openbpm.control.view.processinstance.ProcessInstanceDetailView;
import io.openbpm.uikit.component.dmnviewer.command.ShowDecisionInstanceCmd;
import io.openbpm.uikit.component.dmnviewer.model.DecisionInstanceOutputData;
import io.openbpm.uikit.fragment.dmnviewer.DmnViewerFragment;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@Route(value = "bpm/decision-instances/:id", layout = DefaultMainViewParent.class)
@ViewController(id = "bpm_DecisionInstance.detail")
@ViewDescriptor("decision-instance-detail-view.xml")
@EditedEntityContainer("decisionInstanceDc")
@DialogMode(width = "50em", height = "37.5em")
@PrimaryDetailView(HistoricDecisionInstanceShortData.class)
public class DecisionInstanceDetailView extends StandardDetailView<HistoricDecisionInstanceShortData> {

    @Autowired
    private DecisionDefinitionService decisionDefinitionService;
    @Autowired
    private DecisionInstanceService decisionInstanceService;

    @ViewComponent
    private InstanceContainer<HistoricDecisionInstanceShortData> decisionInstanceDc;
    @ViewComponent
    private DmnViewerFragment dmnViewerFragment;
    @ViewComponent
    private CopyComponentValueToClipboardAction copyToClipboardAction;
    @ViewComponent
    private TypedTextField<String> decisionInstanceIdTextField;
    @Autowired
    private ViewNavigators viewNavigators;
    @ViewComponent
    private HorizontalLayout detailActions;
    @Autowired
    private ProcessInstanceService processInstanceService;
    @Autowired
    private ActivityService activityService;
    @ViewComponent
    private TypedTextField<Object> activityNameTextField;
    @ViewComponent
    private TypedTextField<Object> processBusinessKeyTextField;

    @Subscribe
    public void onInit(final InitEvent event) {
        detailActions.addClassNames(LumoUtility.Padding.Top.SMALL);
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        dmnViewerFragment.initViewer();

        String dmnXml = decisionDefinitionService.getDmnXml(decisionInstanceDc.getItem().getDecisionDefinitionId());
        dmnViewerFragment.setDmnXml(dmnXml, setDmnXmlJson ->
            dmnViewerFragment.showDecisionDefinition(decisionInstanceDc.getItem().getDecisionDefinitionKey(),
                    showDecisionDefinitionJson -> dmnViewerFragment.showDecisionInstance(createDecisionInstanceClientData(decisionInstanceDc.getItem())))
        );

        initAdditionalFields();
    }

    @Subscribe(id = "copyDecisionInstanceId", subject = "clickListener")
    public void onCopyDecisionInstanceIdClick(final ClickEvent<JmixButton> event) {
        copyToClipboardAction.setTarget(decisionInstanceIdTextField);
        copyToClipboardAction.actionPerform(event.getSource());
    }

    @Subscribe(id = "viewProcessDefinition", subject = "clickListener")
    public void onViewProcessDefinitionClick(final ClickEvent<JmixButton> event) {
        viewNavigators.detailView(this, ProcessDefinitionData.class)
                .withViewClass(ProcessDefinitionDetailView.class)
                .withRouteParameters(new RouteParameters("id", getEditedEntity().getProcessDefinitionId()))
                .withBackwardNavigation(true)
                .navigate();
    }

    @Subscribe(id = "viewProcessInstance", subject = "clickListener")
    public void onViewProcessInstanceClick(final ClickEvent<JmixButton> event) {
        viewNavigators.detailView(this, ProcessInstanceData.class)
                .withViewClass(ProcessInstanceDetailView.class)
                .withRouteParameters(new RouteParameters("id", getEditedEntity().getProcessInstanceId()))
                .withBackwardNavigation(true)
                .navigate();
    }

    @Supply(to = "inputsDataGrid.value", subject = "renderer")
    protected Renderer<HistoricDecisionInputInstanceShortData> inputValueRenderer() {
        return new TextRenderer<>(e -> e.getValue() != null ? e.getValue().toString() : null);
    }

    @Supply(to = "outputsDataGrid.value", subject = "renderer")
    protected Renderer<HistoricDecisionOutputInstanceShortData> outputValueRenderer() {
        return new TextRenderer<>(e -> e.getValue() != null ? e.getValue().toString() : null);
    }

    private ShowDecisionInstanceCmd createDecisionInstanceClientData(
            HistoricDecisionInstanceShortData decisionInstance) {
        ShowDecisionInstanceCmd decisionInstanceClientData = new ShowDecisionInstanceCmd();
        decisionInstanceClientData.setOutputDataList(decisionInstance.getOutputs().stream().map(output -> {
            DecisionInstanceOutputData result = new DecisionInstanceOutputData();
            result.setValue(output.getValue() != null ? output.getValue().toString() : "");
            result.setDataRowId(output.getRuleId());
            result.setDataColId(output.getClauseId());
            return result;
        }).toList());
        return decisionInstanceClientData;
    }

    @Install(to = "decisionInstanceDl", target = Target.DATA_LOADER)
    private HistoricDecisionInstanceShortData decisionDefinitionDlDelegate(
            final LoadContext<HistoricDecisionInstanceShortData> loadContext) {
        HistoricDecisionInstanceShortData item = decisionInstanceDc.getItemOrNull();
        String id = item == null ? Objects.requireNonNull(loadContext.getId()).toString() : item.getId();
        return decisionInstanceService.getById(id);
    }

    private void initAdditionalFields() {
        HistoricActivityInstanceData activityInstanceData = activityService.findById(
                decisionInstanceDc.getItem().getActivityInstanceId());
        if (activityInstanceData != null) {
            activityNameTextField.setTypedValue(activityInstanceData.getActivityName());
        }
        ProcessInstanceData processInstanceData = processInstanceService.getProcessInstanceById(
                decisionInstanceDc.getItem().getProcessInstanceId());
        if (processInstanceData != null) {
            processBusinessKeyTextField.setTypedValue(processInstanceData.getBusinessKey());
        }
    }
}
