package io.flowset.control.view.dashboard;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.flowset.control.view.alltasks.AllTasksView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.jmix.flowui.component.UiComponentUtils.getCurrentView;

@FragmentDescriptor("user-task-statistics-card-fragment.xml")
public class UserTaskStatisticsCardFragment extends DashboardCard<Div> {
    protected static final Logger log = LoggerFactory.getLogger(UserTaskStatisticsCardFragment.class);

    @ViewComponent
    protected H3 userTasksCount;

    @Override
    protected void updateComponents(boolean loading) {
        if (loading) {
            userTasksCount.addClassNames(SKELETON_BACKGROUND);
        } else {
            userTasksCount.removeClassName(SKELETON_BACKGROUND);
        }
    }

    public void refresh(long taskCount) {
        updateComponents(false);
        userTasksCount.setText(formattedCount(taskCount));
    }

    @Subscribe(id = "viewUserTaskBtn", subject = "clickListener")
    public void onViewUserTaskBtnClick(final ClickEvent<JmixButton> event) {
        viewNavigators.view(getCurrentView(), AllTasksView.class).navigate();
    }
}