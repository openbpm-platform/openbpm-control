package io.flowset.control.view.dashboard;

import com.vaadin.flow.component.Component;
import io.jmix.core.Messages;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.ViewNavigators;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.MessageBundle;
import io.jmix.flowui.view.ViewComponent;
import io.flowset.control.entity.engine.BpmEngine;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public abstract class DashboardCard<V extends Component> extends Fragment<V> {

    public static final String SKELETON_BACKGROUND = "skeleton-background";
    @ViewComponent
    protected MessageBundle messageBundle;
    @Autowired
    protected CurrentAuthentication currentAuthentication;
    @Autowired
    protected Messages messages;

    @Autowired
    protected ViewNavigators viewNavigators;
    @ViewComponent
    protected InstanceContainer<BpmEngine> selectedEngineDc;

    public void setLoading() {
        updateComponents(true);
    }

    protected abstract void updateComponents(boolean loading);


    protected String formattedCount(long totalCount) {
        if (totalCount < 1000) {
            return String.valueOf(totalCount);
        }
        double thousands = totalCount / 1000.0;
        DecimalFormat decimalFormat = new DecimalFormat(messages.getMessage("thousandsFormat"));
        decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(currentAuthentication.getLocale()));
        return messageBundle.formatMessage("formattedTotalCount", decimalFormat.format(thousands));
    }
}
