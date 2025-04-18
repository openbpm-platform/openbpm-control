package io.openbpm.control.view.decisiondefinition;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.LoadContext;
import io.jmix.core.Metadata;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.UiEventPublisher;
import io.jmix.flowui.component.codeeditor.CodeEditor;
import io.jmix.flowui.component.combobox.JmixComboBox;
import io.jmix.flowui.component.datetimepicker.TypedDateTimePicker;
import io.jmix.flowui.component.formlayout.JmixFormLayout;
import io.jmix.flowui.component.tabsheet.JmixTabSheet;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.DefaultMainViewParent;
import io.jmix.flowui.view.DialogMode;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.Install;
import io.jmix.flowui.view.MessageBundle;
import io.jmix.flowui.view.PrimaryDetailView;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.Target;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.openbpm.control.entity.DeploymentData;
import io.openbpm.control.entity.decisiondefinition.DecisionDefinitionData;
import io.openbpm.control.service.decisiondefinition.DecisionDefinitionService;
import io.openbpm.control.service.deployment.DeploymentService;
import io.openbpm.control.view.decisioninstance.DecisionInstancesFragment;
import io.openbpm.control.view.dmnviewer.DmnViewerFragmentNew;
import io.openbpm.control.view.event.TitleUpdateEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

@Route(value = "bpm/decision-definitions/:id", layout = DefaultMainViewParent.class)
@ViewController(id = "bpm_DecisionDefinition.detail")
@ViewDescriptor("decision-definition-detail-view.xml")
@EditedEntityContainer("decisionDefinitionDc")
@DialogMode(width = "50em", height = "37.5em")
@PrimaryDetailView(DecisionDefinitionData.class)
public class DecisionDefinitionDetailView extends StandardDetailView<DecisionDefinitionData> {

    @ViewComponent
    private InstanceContainer<DecisionDefinitionData> decisionDefinitionDc;
    @Autowired
    private DecisionDefinitionService decisionDefinitionService;
    @ViewComponent
    private JmixComboBox<DecisionDefinitionData> versionComboBox;
    @Autowired
    private DeploymentService deploymentService;
    @ViewComponent
    private TypedDateTimePicker<Comparable> deploymentTimeField;
    @ViewComponent
    private TypedTextField<Object> deploymentSourceField;
    @Autowired
    private Notifications notifications;
    @Autowired
    private Metadata metadata;
    @ViewComponent
    private DecisionInstancesFragment decisionInstancesFragment;
    @ViewComponent
    private MessageBundle messageBundle;
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private UiEventPublisher uiEventPublisher;

    private String title = "";
    @ViewComponent
    private CodeEditor dmnXmlEditor;
    @ViewComponent
    private DmnViewerFragmentNew viewerFragment;
    @ViewComponent
    private JmixFormLayout decisionDefinitionForm;
    @ViewComponent
    private JmixTabSheet tabSheet;

    @Subscribe
    public void onInit(final InitEvent event) {
        addClassNames(LumoUtility.Padding.NONE);
        initTabIcons();
        decisionDefinitionForm.addClassName(LumoUtility.Padding.Right.XSMALL);
    }

    protected void initTabIcons() {
        tabSheet.getTabAt(0).addComponentAsFirst(VaadinIcon.INFO_CIRCLE_O.create());
        tabSheet.getTabAt(1).addComponentAsFirst(VaadinIcon.SITEMAP.create());
        tabSheet.getTabAt(2).addComponentAsFirst(VaadinIcon.FILE_CODE.create());
    }

    @Subscribe(id = "viewDeployment", subject = "clickListener")
    public void onViewDeploymentClick(final ClickEvent<JmixButton> event) {
        notifications.show("View deployment");
        /*   viewNavigators.detailView(this, DeploymentData.class)
                .withViewClass(DeploymentDetailView.class)
                .withRouteParameters(new RouteParameters("id", getEditedEntity().getDeploymentId()))
                .withBackwardNavigation(true)
                .navigate();*/
    }

    @Subscribe
    public void onReady(final ReadyEvent event) {
        sendUpdateViewTitleEvent();
    }

    @Override
    public String getPageTitle() {
        return title;
    }

    @Subscribe
    protected void onBeforeShow(BeforeShowEvent event) {
        initVersionLookup(getEditedEntity());
//        actionsFragment.updateButtonsVisibility();
//        processInstancesFragment.initInstancesCountLabels();
//        String processDefinitionBpmnXml = processDefinitionService.getBpmnXml(
//                getEditedEntity().getProcessDefinitionId());
//        viewerFragment.initViewer(processDefinitionBpmnXml);

        initDeploymentData();
    }

    @Install(to = "decisionDefinitionDl", target = Target.DATA_LOADER)
    private DecisionDefinitionData decisionDefinitionDlDelegate(final LoadContext<DecisionDefinitionData> loadContext) {
        DecisionDefinitionData item = decisionDefinitionDc.getItemOrNull();
        String id = item == null ? Objects.requireNonNull(loadContext.getId()).toString() : item.getId();
        return decisionDefinitionService.getById(id);
    }

    @Subscribe("versionComboBox")
    protected void onVersionLookupValueChange(
            AbstractField.ComponentValueChangeEvent<ComboBox<DecisionDefinitionData>, DecisionDefinitionData> event) {
        DecisionDefinitionData selectedProcessDefinition = event.getValue();
        decisionDefinitionDc.setItem(selectedProcessDefinition);
        decisionInstancesFragment.initInstancesCountLabels();
        sendUpdateViewTitleEvent();
        initDeploymentData();
    }

    @Subscribe(id = "decisionDefinitionDc", target = Target.DATA_CONTAINER)
    protected void onDecisionDefinitionDcItemChange(InstanceContainer.ItemChangeEvent<DecisionDefinitionData> event) {
        DecisionDefinitionData decisionDefinition = event.getItem();

        String dmnXml = decisionDefinitionService.getDmnXml(decisionDefinition.getDecisionDefinitionId());
        viewerFragment.initViewer();
        viewerFragment.setDmnXml(dmnXml);

        dmnXmlEditor.setValue(dmnXml);

        sendUpdateViewTitleEvent();
    }

    protected void sendUpdateViewTitleEvent() {
        this.title = messageBundle.formatMessage("decisionDefinition.title.name", getEditedEntity().getKey());

        FlexLayout flexLayout = uiComponents.create(FlexLayout.class);
        flexLayout.addClassNames(LumoUtility.Margin.Left.XSMALL, LumoUtility.Gap.SMALL);

        Span version = uiComponents.create(Span.class);
        version.addClassNames(LumoUtility.TextColor.SECONDARY, LumoUtility.FontWeight.BOLD);
        version.setText(messageBundle.formatMessage("decisionDefinition.title.version", getEditedEntity().getVersion()));

        flexLayout.add(version);

        uiEventPublisher.publishEventForCurrentUI(new TitleUpdateEvent(this, title, flexLayout));
    }

    protected void initVersionLookup(DecisionDefinitionData decisionDefinition) {
        List<DecisionDefinitionData> optionsList = decisionDefinitionService.findAllByKey(decisionDefinition.getKey());
        versionComboBox.setItems(optionsList);
        versionComboBox.setItemLabelGenerator(DecisionDefinitionData::getVersion);
        versionComboBox.setValue(decisionDefinition);
    }

    protected void initDeploymentData() {
        DeploymentData deployment = deploymentService.findById(getEditedEntity().getDeploymentId());
        if (deployment != null) {
            String source = deployment.getSource();
            deploymentSourceField.setTypedValue(source);
            deploymentTimeField.setTypedValue(deployment.getDeploymentTime());
        }
    }
}
