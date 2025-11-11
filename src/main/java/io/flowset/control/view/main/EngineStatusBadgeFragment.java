package io.flowset.control.view.main;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.jmix.core.Messages;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import io.flowset.control.entity.EngineConnectionCheckResult;
import io.flowset.control.entity.engine.BpmEngine;
import io.flowset.control.entity.engine.EngineType;
import io.flowset.control.view.engineconnectionsettings.EngineConnectionSettingsView;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.vaadin.addons.componentfactory.spinner.Spinner;

import static io.jmix.flowui.component.UiComponentUtils.getCurrentView;

@FragmentDescriptor("engine-status-badge-fragment.xml")
public class EngineStatusBadgeFragment extends Fragment<FlexLayout> {
    @ViewComponent
    protected Spinner statusSpinner;

    @ViewComponent
    protected InstanceContainer<EngineConnectionCheckResult> engineConnectionStatusDc;
    @ViewComponent

    protected InstanceContainer<BpmEngine> selectedEngineDc;

    @ViewComponent
    protected Span engineStateBadge;

    @ViewComponent
    protected Div connectionStatusText;

    @ViewComponent
    protected MessageBundle messageBundle;

    @ViewComponent
    protected Icon successStatusIcon;

    @ViewComponent
    protected Icon warningStatusIcon;

    @Autowired
    protected Messages messages;

    @Autowired
    protected DialogWindows dialogWindows;

    @Subscribe
    protected void onAttachEvent(final AttachEvent event) {
        initConnectionStatusComponents();
    }

    public void initConnectionStatusComponents() {
        statusSpinner.setVisible(true);
        statusSpinner.setLoading(true);

        successStatusIcon.setVisible(false);
        warningStatusIcon.setVisible(false);

        if (selectedEngineDc.getItemOrNull() != null) {
            engineStateBadge.setTitle(selectedEngineDc.getItem().getBaseUrl());
            addSelectedEngineData(null);
        } else {
            setNoSelectedEngineStatus();
        }
    }

    public void updateConnectionStatusComponents() {
        statusSpinner.setLoading(false);

        EngineConnectionCheckResult item = engineConnectionStatusDc.getItem();
        if (BooleanUtils.isTrue(item.getSuccess())) {
            setSuccessfulConnectionStatus(item.getVersion());
        } else {
            setFailedConnectionStatus();
        }
    }

    @Subscribe(id = "viewEngineConfigBtn", subject = "clickListener")
    public void onViewEngineConfigBtnClick(final ClickEvent<JmixButton> event) {
        dialogWindows.view(getCurrentView(), EngineConnectionSettingsView.class)
                .build()
                .open();
    }

    protected void setSuccessfulConnectionStatus(String version) {
        addSelectedEngineData(version);

        engineStateBadge.getElement().getThemeList().clear();
        engineStateBadge.getElement().getThemeList().add("badge pill success");
        successStatusIcon.setVisible(true);
        warningStatusIcon.setVisible(false);
    }

    protected void addSelectedEngineData(@Nullable String version) {
        connectionStatusText.removeAll();

        BpmEngine engine = selectedEngineDc.getItem();

        Span engineName = new Span(engine.getName());
        engineName.setMaxWidth("10em");
        engineName.addClassNames(LumoUtility.TextOverflow.ELLIPSIS,
                LumoUtility.Overflow.HIDDEN, LumoUtility.Whitespace.NOWRAP);

        Span engineTypeAndVersion;
        if (version != null) {
            String generalTypeName = messages.getMessage(EngineType.class, "EngineType.%s.general".formatted(engine.getType().name()));
            engineTypeAndVersion = new Span("(%s %s)".formatted(generalTypeName, version));
        } else {
            engineTypeAndVersion = new Span("(%s)".formatted(messages.getMessage(engine.getType())));
        }

        connectionStatusText.add(engineName, engineTypeAndVersion);
    }

    public void setFailedConnectionStatus() {
        statusSpinner.setLoading(false);
        addSelectedEngineData(null);
        engineStateBadge.getElement().getThemeList().clear();
        engineStateBadge.getElement().getThemeList().add("badge pill error");
        successStatusIcon.setVisible(false);
        warningStatusIcon.setVisible(true);
    }

    public void setNoSelectedEngineStatus() {
        connectionStatusText.removeAll();
        connectionStatusText.setText(messageBundle.getMessage("noSelectedEngine"));
        engineStateBadge.getElement().getThemeList().clear();
        engineStateBadge.getElement().getThemeList().add("badge pill warning");
        successStatusIcon.setVisible(false);
        warningStatusIcon.setVisible(true);
        statusSpinner.setLoading(false);
    }
}