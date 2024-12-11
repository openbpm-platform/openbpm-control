package io.openbpm.control.view.bpmengine;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.fragmentrenderer.FragmentRenderer;
import io.jmix.flowui.fragmentrenderer.RendererItemContainer;
import io.jmix.flowui.view.ViewComponent;
import io.openbpm.control.entity.engine.BpmEngine;
import org.apache.commons.lang3.BooleanUtils;

@FragmentDescriptor("bpm-engine-name-column-fragment.xml")
@RendererItemContainer("bpmEngineDc")
public class BpmEngineNameColumnFragment extends FragmentRenderer<HorizontalLayout, BpmEngine> {

    @ViewComponent
    protected Span defaultEngineBadge;

    @Override
    public void setItem(BpmEngine item) {
        super.setItem(item);

        if (BooleanUtils.isTrue(item.getIsDefault())) {
            defaultEngineBadge.setVisible(true);
        }
    }

}
