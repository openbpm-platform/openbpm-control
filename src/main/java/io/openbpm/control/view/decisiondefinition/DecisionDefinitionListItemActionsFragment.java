package io.openbpm.control.view.decisiondefinition;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouteParameters;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.Subscribe;
import io.openbpm.control.entity.decisiondefinition.DecisionDefinitionData;
import io.openbpm.control.entity.processinstance.ProcessInstanceData;
import io.openbpm.control.view.processinstance.ProcessInstanceDetailView;
import org.springframework.beans.factory.annotation.Autowired;

import static io.jmix.flowui.component.UiComponentUtils.getCurrentView;

@FragmentDescriptor("decision-definition-list-item-actions-fragment.xml")
public class DecisionDefinitionListItemActionsFragment extends Fragment<HorizontalLayout> {

    @Autowired
    private ViewNavigators viewNavigators;

    protected DecisionDefinitionData decisionDefinitionData;

    public void setDecisionDefinition(DecisionDefinitionData decisionDefinitionData) {
        this.decisionDefinitionData = decisionDefinitionData;
    }

    @Subscribe(id = "viewDetailsBtn", subject = "clickListener")
    public void onViewDetailsBtnClick(final ClickEvent<JmixButton> event) {
        viewNavigators.detailView(getCurrentView(), DecisionDefinitionData.class)
                .withViewClass(DecisionDefinitionDetailView.class)
                .withRouteParameters(new RouteParameters("id", decisionDefinitionData.getId()))
                .withBackwardNavigation(true)
                .navigate();
    }
}