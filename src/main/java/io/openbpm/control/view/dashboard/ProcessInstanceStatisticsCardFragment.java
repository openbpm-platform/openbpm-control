package io.openbpm.control.view.dashboard;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.openbpm.control.view.processinstance.ProcessInstanceListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.jmix.flowui.component.UiComponentUtils.getCurrentView;

@FragmentDescriptor("process-instance-statistics-card-fragment.xml")
public class ProcessInstanceStatisticsCardFragment extends DashboardCard<Div> {
    protected static final Logger log = LoggerFactory.getLogger(ProcessInstanceStatisticsCardFragment.class);

    @ViewComponent
    protected H3 runningProcessInstances;
    @ViewComponent
    protected H3 suspendedProcessInstances;


    @Override
    protected void updateComponents(boolean loading) {
        if (loading) {
            runningProcessInstances.addClassNames(SKELETON_BACKGROUND);
            suspendedProcessInstances.addClassNames(SKELETON_BACKGROUND);
        } else {
            runningProcessInstances.removeClassName(SKELETON_BACKGROUND);
            suspendedProcessInstances.removeClassName(SKELETON_BACKGROUND);
        }
    }

    public void refresh(long runningCount, long suspendedCount) {
        updateComponents(false);

        runningProcessInstances.setText(formattedCount(runningCount));
        suspendedProcessInstances.setText(formattedCount(suspendedCount));
    }


    @Subscribe(id = "viewInstancesBtn", subject = "clickListener")
    public void onViewInstancesBtnClick(final ClickEvent<JmixButton> event) {
        viewNavigators.view(getCurrentView(), ProcessInstanceListView.class).navigate();
    }

}