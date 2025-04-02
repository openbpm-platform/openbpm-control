package io.openbpm.control.uicomponent.formviewer;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.function.SerializableConsumer;
import elemental.json.JsonValue;

@Tag("openbpm-control-form-viewer")
@NpmPackage(value = "@bpmn-io/form-js", version = "1.15.0")
@NpmPackage(value = "@bpmn-io/form-js-viewer", version = "1.15.0")
@JsModule("./src/form-viewer/form-viewer.ts")
public class FormViewer extends Component {

    public void setFormJson(String formJson) {
        getElement().callJsFunction("reloadSchema", formJson);
    }

    public void setFormJson(String formJson, SerializableConsumer<JsonValue> callback) {
        getElement().callJsFunction("reloadSchema", formJson).then(callback);
    }
}
