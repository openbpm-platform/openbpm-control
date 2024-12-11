package io.openbpm.control.view.processinstance;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.fragmentrenderer.FragmentRenderer;
import io.jmix.flowui.fragmentrenderer.RendererItemContainer;
import io.jmix.flowui.view.ViewComponent;
import io.openbpm.control.entity.processinstance.ProcessInstanceData;
import io.openbpm.control.view.util.ComponentHelper;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;

@FragmentDescriptor("process-instance-state-column-fragment.xml")
@RendererItemContainer("processInstanceDataDc")
public class ProcessInstanceStateColumnFragment extends FragmentRenderer<HorizontalLayout, ProcessInstanceData> {

    @Autowired
    protected ComponentHelper componentHelper;
    @ViewComponent
    protected Icon incidentIcon;

    @Override
    public void setItem(ProcessInstanceData item) {
        super.setItem(item);

        Span processInstanceStateBadge = componentHelper.createProcessInstanceStateBadge(item.getState());
        getContent().addComponentAsFirst(processInstanceStateBadge);

        incidentIcon.setVisible(BooleanUtils.isTrue(item.getHasIncidents()));
    }
}