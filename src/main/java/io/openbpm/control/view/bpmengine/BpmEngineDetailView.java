/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.bpmengine;

import com.vaadin.flow.component.AbstractField;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.jmix.core.EntityStates;
import io.jmix.core.SaveContext;
import io.jmix.flowui.Fragments;
import io.jmix.flowui.component.SupportsTypedValue;
import io.jmix.flowui.component.checkbox.JmixCheckbox;
import io.jmix.flowui.component.radiobuttongroup.JmixRadioButtonGroup;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.view.*;
import io.openbpm.control.action.TestEngineConnectionAction;
import io.openbpm.control.entity.engine.AuthType;
import io.openbpm.control.entity.engine.BpmEngine;
import io.openbpm.control.entity.engine.EngineType;
import io.openbpm.control.service.engine.EngineService;
import io.openbpm.control.view.main.MainView;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@Route(value = "bpm/engines/:id", layout = MainView.class)
@ViewController(id = "BpmEngine.detail")
@ViewDescriptor(path = "bpm-engine-detail-view.xml")
@EditedEntityContainer("bpmEngineDc")
public class BpmEngineDetailView extends StandardDetailView<BpmEngine> {
    protected static final Logger log = LoggerFactory.getLogger(BpmEngineDetailView.class);
    @ViewComponent
    protected Div authBox;
    @ViewComponent
    protected JmixRadioButtonGroup<AuthType> authTypeGroup;
    @Autowired
    protected Fragments fragments;
    @Autowired
    protected EngineService engineService;

    @ViewComponent
    protected MessageBundle messageBundle;
    @Autowired
    protected EntityStates entityStates;
    @ViewComponent
    protected JmixCheckbox defaultField;
    @ViewComponent
    protected TestEngineConnectionAction testConnectionAction;

    @Subscribe
    public void onInitEntity(final InitEntityEvent<BpmEngine> event) {
        BpmEngine entity = event.getEntity();
        entity.setType(EngineType.CAMUNDA_7);

        boolean engineExists = engineService.engineExists();
        if (!engineExists) {
            entity.setIsDefault(true);
        }
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        BpmEngine engine = getEditedEntity();
        testConnectionAction.setEngine(engine);
        initAuthBox(engine.getAuthEnabled());

        if (BooleanUtils.isTrue(engine.getIsDefault()) && !entityStates.isNew(engine)) {
            defaultField.setEnabled(false);
        }
    }

    @Override
    public String getPageTitle() {
        BpmEngine engine = getEditedEntityOrNull();
        if (engine == null) {
            return messageBundle.getMessage("bpmEngineDetailView.title");
        }
        return entityStates.isNew(engine) ? messageBundle.getMessage("newBpmEngineDetailView.title") :
                messageBundle.formatMessage("existingBpmEngineDetailView.title", engine.getName());
    }

    @Install(target = Target.DATA_CONTEXT)
    protected Set<Object> saveDelegate(final SaveContext saveContext) {
        BpmEngine engineToSave = saveContext.getEntitiesToSave().get(BpmEngine.class, getEditedEntity().getId());
        Set<Object> entities = engineService.saveEngine(engineToSave);

        return entities;
    }


    @Subscribe("authEnabledField")
    public void onAuthEnabledFieldComponentValueChange(final AbstractField.ComponentValueChangeEvent<JmixCheckbox, Boolean> event) {
        Boolean isEnabled = event.getValue();
        initAuthBox(isEnabled);
    }

    protected void initAuthBox(Boolean isEnabled) {
        authBox.setEnabled(isEnabled);
        authTypeGroup.setEnabled(isEnabled);
        if (BooleanUtils.isNotTrue(isEnabled)) {
            getEditedEntity().setAuthType(null);

            getEditedEntity().setBasicAuthUsername(null);
            getEditedEntity().setBasicAuthPassword(null);

            getEditedEntity().setHttpHeaderName(null);
            getEditedEntity().setHttpHeaderValue(null);
        }
    }

    @Subscribe("authTypeGroup")
    public void onAuthTypeGroupComponentValueChange(final AbstractField.ComponentValueChangeEvent<JmixRadioButtonGroup<AuthType>, AuthType> event) {
        AuthType type = event.getValue();

        Fragment<VerticalLayout> authFragment = switch (type) {
            case BASIC -> fragments.create(this, BasicAuthFragment.class);
            case HTTP_HEADER -> fragments.create(this, HttpHeaderAuthFragment.class);
            case null, default -> null;
        };

        authBox.removeAll();
        if (authFragment != null) {
            authBox.add(authFragment);
        }
    }

    @Subscribe("baseUrlField")
    public void onBaseUrlFieldTypedValueChange(final SupportsTypedValue.TypedValueChangeEvent<TypedTextField<String>, String> event) {
        if (event.isFromClient()) {
            String trimmedValue = event.getValue() != null ? event.getValue().trim() : null;
            getEditedEntity().setBaseUrl(trimmedValue);
        }
    }
}