/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.decisiondeployment;

import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.openbpm.control.entity.decisiondefinition.DecisionDefinitionData;
import io.openbpm.control.uicomponent.dmnviewer.DmnDecisionDefinition;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@FragmentDescriptor("decision-deployment-confirm-content-fragment.xml")
public class DecisionDeploymentConfirmContentFragment extends Fragment<VerticalLayout> {
    @ViewComponent
    protected VerticalLayout existingDecisionsVBox;

    @ViewComponent
    protected Icon warningIcon;

    @ViewComponent
    protected H5 warningHeader;

    @ViewComponent
    protected UnorderedList existingDecisionsList;

    @ViewComponent
    protected UnorderedList deployingDecisionsList;

    @Subscribe
    public void onReady(ReadyEvent event) {
        initExistingDecisionsWarningStyles();
        deployingDecisionsList.addClassName(LumoUtility.Margin.NONE);
    }

    public void setExistingDecisions(List<DecisionDefinitionData> existingDecisions) {
        if (CollectionUtils.isNotEmpty(existingDecisions)) {
            existingDecisionsVBox.setVisible(true);
            existingDecisions.forEach(decisionDefinitionData -> {
                ListItem listItem = uiComponents.create(ListItem.class);
                listItem.setText(decisionDefinitionData.getKey());
                existingDecisionsList.add(listItem);
            });
        }
    }

    public void setDeployingDecisions(List<DmnDecisionDefinition> deployingDecisions) {
        deployingDecisions.forEach(deployingDecision -> {
            ListItem listItem = uiComponents.create(ListItem.class);
            listItem.setText(deployingDecision.getKey());
            deployingDecisionsList.add(listItem);
        });
    }

    protected void initExistingDecisionsWarningStyles() {
        warningIcon.addClassNames(LumoUtility.TextColor.WARNING);
        warningHeader.addClassNames(LumoUtility.TextColor.WARNING_CONTRAST);
        existingDecisionsVBox.addClassNames(LumoUtility.BorderColor.WARNING,
                LumoUtility.Border.ALL, LumoUtility.BorderRadius.LARGE,
                LumoUtility.Background.WARNING_10);
        existingDecisionsList.addClassName(LumoUtility.Margin.NONE);
    }
}
