package io.openbpm.control.view.dashboard;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.openbpm.control.view.processdefinition.ProcessDefinitionListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.jmix.flowui.component.UiComponentUtils.getCurrentView;

@FragmentDescriptor("deployed-processes-statistics-card-fragment.xml")
public class DeployedProcessesStatisticsCardFragment extends DashboardCard<Div> {
    protected static final Logger log = LoggerFactory.getLogger(DeployedProcessesStatisticsCardFragment.class);

    @ViewComponent
    protected H3 deployProcessesCount;


    public void refresh(long deployedProcessCount) {
        updateComponents(false);
        deployProcessesCount.setText(formattedCount(deployedProcessCount));
    }

    protected void updateComponents(boolean loading) {
        if (loading) {
            deployProcessesCount.addClassNames(SKELETON_BACKGROUND);
        } else {
            deployProcessesCount.removeClassName(SKELETON_BACKGROUND);
        }
    }

    @Subscribe(id = "viewDefinitionsBtn", subject = "clickListener")
    public void onViewDefinitionsBtnClick(final ClickEvent<JmixButton> event) {
        viewNavigators.view(getCurrentView(), ProcessDefinitionListView.class).navigate();
    }

}