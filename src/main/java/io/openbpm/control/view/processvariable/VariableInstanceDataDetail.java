/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.view.processvariable;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import io.openbpm.control.entity.variable.CamundaVariableType;
import io.openbpm.control.entity.variable.VariableInstanceData;
import io.openbpm.control.service.variable.VariableService;
import io.openbpm.control.service.variable.VariableUtils;
import io.jmix.core.Metadata;
import io.jmix.core.metamodel.datatype.DatatypeRegistry;
import io.jmix.flowui.Actions;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.UiComponents;
import io.jmix.flowui.component.combobox.JmixComboBox;
import io.jmix.flowui.component.datetimepicker.TypedDateTimePicker;
import io.jmix.flowui.component.formlayout.JmixFormLayout;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Date;

@Route(value = "bpm/variableinstancedata", layout = DefaultMainViewParent.class)
@ViewController("bpm_VariableInstanceData.detail")
@ViewDescriptor("variable-instance-data-detail.xml")
@EditedEntityContainer("variableInstanceDc")
@PrimaryDetailView(VariableInstanceData.class)
@DialogMode(width = "38em", resizable = true)
public class VariableInstanceDataDetail extends StandardDetailView<VariableInstanceData> {
    protected static final Logger log = LoggerFactory.getLogger(VariableInstanceDataDetail.class);

    @Autowired
    protected DatatypeRegistry datatypeRegistry;
    @ViewComponent
    protected MessageBundle messageBundle;
    @Autowired
    protected Metadata metadata;
    @Autowired
    protected UiComponents uiComponents;
    @Autowired
    protected Actions actions;

    @ViewComponent
    protected JmixComboBox<CamundaVariableType> typeComboBox;
    @ViewComponent
    protected TypedTextField<String> nameField;
    @ViewComponent
    protected InstanceContainer<VariableInstanceData> variableInstanceDc;

    @ViewComponent
    protected JmixFormLayout form;
    @ViewComponent
    protected TypedTextField<String> objectTypeInfoField;
    @ViewComponent
    protected JmixFormLayout valueForm;
    @ViewComponent
    protected TypedTextField<String> fileNameField;
    @ViewComponent
    protected TypedTextField<String> mimeTypeField;
    @ViewComponent
    protected TypedTextField<String> encodingField;
    @ViewComponent
    protected TypedTextField<String> serializationDataFormatField;
    @Autowired
    protected VariableService variableService;
    @Autowired
    protected Notifications notifications;

    @ViewComponent
    protected JmixButton saveBtn;
    @ViewComponent
    protected JmixButton okBtn;

    protected boolean newVariable = false;
    @ViewComponent
    protected TypedTextField<String> activityInstanceIdField;

    @SuppressWarnings("LombokSetterMayBeUsed")
    public void setNewVariable(boolean editable) {
        this.newVariable = editable;
    }

    @Subscribe
    public void onInit(InitEvent event) {
        addClassNames(LumoUtility.Padding.Top.XSMALL);
        valueForm.getOwnComponents().forEach(component -> component.addClassNames(LumoUtility.Padding.Top.SMALL));
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        initTypeLookup();
        if(!newVariable) {
            form.getOwnComponents().forEach(component -> {
                if (component instanceof HasValue<?, ?> hasValue) {
                    hasValue.setReadOnly(true);
                }
            });
            activityInstanceIdField.setVisible(true);
        }

        initActions();
        initValueComponent();
    }


    @Subscribe(id = "variableInstanceDc", target = Target.DATA_CONTAINER)
    public void onVariableInstanceDcItemPropertyChange(InstanceContainer.ItemPropertyChangeEvent<VariableInstanceData> event) {
        if ("type".equals(event.getProperty())) {
            event.getItem().setValue(null);
            initValueComponent();
        }
    }

    @Subscribe("saveAction")
    public void onSaveAction(final ActionPerformedEvent event) {
        VariableInstanceData variableInstanceData = variableInstanceDc.getItem();
        variableService.updateVariableLocal(variableInstanceData);

        notifications.create(messageBundle.getMessage("variableValueUpdated"))
                .withType(Notifications.Type.SUCCESS);
        close(StandardOutcome.SAVE);
    }

    protected void initTypeLookup() {
        if (newVariable) {
            typeComboBox.setItems(Arrays.stream(CamundaVariableType.values())
                    .filter(CamundaVariableType::isPrimitive)
                    .toList());
        } else {
            typeComboBox.setItems(CamundaVariableType.class);
        }

        typeComboBox.setItemLabelGenerator(CamundaVariableType::getId);
        typeComboBox.setRequired(true);
        typeComboBox.addValueChangeListener(event -> {
            getEditedEntity().setType(event.getValue().getId());
            updateComponentsVisibility(event.getValue());
        });

        String type = getEditedEntity().getType();
        if (type != null) {
            CamundaVariableType camundaVariableType = CamundaVariableType.fromId(type);
            typeComboBox.setValue(camundaVariableType);
            updateComponentsVisibility(camundaVariableType);
        }
    }

    @Override
    public boolean isShowSaveNotification() {
        return !newVariable;
    }

    protected void initValueComponent() {
        valueForm.removeAll();
        VariableInstanceData variableInstance = variableInstanceDc.getItem();
        if (variableInstance.getType() != null) {
            Component valueComponent = createComponent(variableInstance);
            if (valueComponent != null) {
                setupValueComponent(valueComponent);
            } else {
                createAndSetupInfoComponent();
            }
        }
    }

    protected void createAndSetupInfoComponent() {
        Div infoDiv = uiComponents.create(Div.class);
        infoDiv.setWidth("100%");
        infoDiv.setText(messageBundle.getMessage("variableInstanceDataEdit.cannotCreateEditComponent"));
        valueForm.addFormItem(infoDiv, messageBundle.getMessage("variableInstanceDataEdit.value"));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void setupValueComponent(Component valueComponent) {
        VariableInstanceData variableInstance = variableInstanceDc.getItem();
        ((HasSize) valueComponent).setWidthFull();

        if (valueComponent instanceof HasValue<?, ?>) {
            Object value = variableInstance.getValue();
            if (value != null) {
                if (valueComponent instanceof TypedTextField typedTextField) {
                    typedTextField.setTypedValue(value);
                } else if (valueComponent instanceof TypedDateTimePicker typedDateTimePicker) {
                    typedDateTimePicker.setTypedValue((Comparable) VariableUtils.parseDateValue(value));
                } else {
                    ((HasValue) valueComponent).setValue(value);
                }
            }
        }
        valueForm.addFormItem(valueComponent, messageBundle.getMessage("variableInstanceDataEdit.value"));
    }
    protected void initActions() {
        saveBtn.setVisible(!newVariable);
        okBtn.setVisible(newVariable);
    }

    protected Component createComponent(VariableInstanceData variableInstance) {
        Component component;
        String typeName = variableInstance.getType();
        CamundaVariableType type = CamundaVariableType.fromId(typeName);

        if (type == null) {
            return createTextField(String.class);
        }
        component = switch (type) {
            case LONG -> createTextField(Long.class);
            case SHORT -> createTextField(Short.class);
            case INTEGER -> createIntegerField();
            case DOUBLE -> createNumberField();
            case DATE -> createDateField(Date.class);
            case BOOLEAN -> createCheckbox();
            default -> createTextField(String.class);
        };

        return component;
    }

    protected Checkbox createCheckbox() {
        Checkbox checkbox = uiComponents.create(Checkbox.class);
        checkbox.addValueChangeListener(event -> {
            getEditedEntity().setValue(event.getValue());
        });
        return checkbox;
    }


    protected NumberField createNumberField() {
        NumberField numberField = uiComponents.create(NumberField.class);
        numberField.setValueChangeMode(ValueChangeMode.ON_BLUR);
        numberField.addValueChangeListener(event -> {
            getEditedEntity().setValue(event.getValue());
        });
        return numberField;
    }


    protected IntegerField createIntegerField() {
        IntegerField integerField = uiComponents.create(IntegerField.class);
        integerField.setValueChangeMode(ValueChangeMode.ON_BLUR);
        integerField.addValueChangeListener(event -> {
            getEditedEntity().setValue(event.getValue());
        });
        return integerField;
    }


    @SuppressWarnings("unchecked")
    protected <V extends Comparable<?>> TypedDateTimePicker<V> createDateField(Class<V> clazz) {
        TypedDateTimePicker<V> component = uiComponents.create(TypedDateTimePicker.class);
        component.setDatatype(datatypeRegistry.get(clazz));
        component.addTypedValueChangeListener(event -> {
            V value = event.getValue();
            getEditedEntity().setValue(value);
        });
        return component;
    }

    @SuppressWarnings("unchecked")
    protected <V> TypedTextField<V> createTextField(Class<V> clazz) {
        TypedTextField<V> component = uiComponents.create(TypedTextField.class);
        component.setDatatype(datatypeRegistry.get(clazz));
        component.setValueChangeMode(ValueChangeMode.ON_BLUR);
        component.addTypedValueChangeListener(event -> {
            V value = event.getValue();
            getEditedEntity().setValue(value);
        });

        return component;
    }


    protected void updateComponentsVisibility(CamundaVariableType camundaVariableType) {
        boolean isFileType = camundaVariableType == CamundaVariableType.FILE;
        fileNameField.setVisible(isFileType);
        mimeTypeField.setVisible(isFileType);
        encodingField.setVisible(isFileType);

        boolean isObjectType = camundaVariableType == CamundaVariableType.OBJECT;
        objectTypeInfoField.setVisible(isObjectType);
        serializationDataFormatField.setVisible(isObjectType);

        boolean nullType = camundaVariableType == CamundaVariableType.NULL;
        valueForm.setVisible(!nullType);
    }
}