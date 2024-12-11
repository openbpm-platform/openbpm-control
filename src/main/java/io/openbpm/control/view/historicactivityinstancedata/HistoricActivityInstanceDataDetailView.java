/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.historicactivityinstancedata;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.openbpm.control.entity.activity.HistoricActivityInstanceData;
import io.openbpm.control.service.activity.ActivityService;
import io.openbpm.control.view.main.MainView;
import io.jmix.core.LoadContext;
import io.jmix.flowui.component.formlayout.JmixFormLayout;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;

@Route(value = "historicActivityInstanceDatas/:id", layout = MainView.class)
@ViewController("HistoricActivityInstanceData.detail")
@ViewDescriptor("historic-activity-instance-data-detail-view.xml")
@EditedEntityContainer("historicActivityInstanceDataDc")
@DialogMode(minWidth = "30em", maxWidth = "60em", width = "auto")
public class HistoricActivityInstanceDataDetailView extends StandardDetailView<HistoricActivityInstanceData> {
    @ViewComponent
    protected MessageBundle messageBundle;

    @Autowired
    protected ActivityService activityService;
    @ViewComponent
    protected JmixFormLayout form;
    @ViewComponent
    protected TypedTextField<Object> durationField;

    @Subscribe
    public void onInit(final InitEvent event) {
        addClassNames(LumoUtility.Padding.Top.XSMALL);
        form.getOwnComponents().forEach(component -> component.addClassNames(LumoUtility.Padding.Top.SMALL));
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        Long durationInMillis = getEditedEntity().getDurationInMillis();
        if (durationInMillis != null) {
            Duration duration = Duration.ofMillis(durationInMillis);

            durationField.setValue(messageBundle.formatMessage("formattedDuration", duration.toDaysPart(),
                    duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart()));
        }
    }

    @Install(to = "historicActivityInstanceDataDl", target = Target.DATA_LOADER)
    protected HistoricActivityInstanceData customerDlLoadDelegate(final LoadContext<HistoricActivityInstanceData> loadContext) {
        Object id = loadContext.getId();

        return activityService.findById(id.toString());
    }
}
