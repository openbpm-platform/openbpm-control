/*
 * Copyright (c) Haulmont 2024. All Rights Reserved.
 * Use is subject to license terms.
 */

package io.openbpm.control.uicomponent.bpmnviewer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.shared.Registration;
import elemental.json.JsonValue;
import io.openbpm.control.uicomponent.bpmnviewer.command.AddMarkerCmd;
import io.openbpm.control.uicomponent.bpmnviewer.command.RemoveMarkerCmd;
import io.openbpm.control.uicomponent.bpmnviewer.command.SetElementColorCmd;
import io.openbpm.control.uicomponent.bpmnviewer.command.SetIncidentCountCmd;
import io.openbpm.control.uicomponent.bpmnviewer.event.ImportCompleteEvent;
import io.openbpm.control.uicomponent.bpmnviewer.event.SelectionChangedEvent;

import java.util.concurrent.CompletableFuture;

@Tag("openbpm-control-bpmn-viewer")
@NpmPackage(value = "bpmn-js", version = "17.11.1")
@CssImport("bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css")
@CssImport("bpmn-js/dist/assets/bpmn-js.css")
@CssImport("bpmn-js/dist/assets/diagram-js.css")
@JsModule("./src/bpmn-modeler/bpmn-viewer.ts")
public class BpmnViewerImpl extends Component implements BpmnViewer {
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected String bpmnXml;

    public void setBpmnXml(String bpmnXml) {
        this.bpmnXml = bpmnXml;
        getElement().callJsFunction("reloadSchema", bpmnXml);
    }

    public String getBpmnXmlFromState() {
        return bpmnXml;
    }

    public CompletableFuture<String> requestForBpmnXml() {
        return getElement()
                .callJsFunction("getXmlSchema")
                .toCompletableFuture()
                .thenApply(xmlSchema -> {
                    this.bpmnXml = xmlSchema.asString();
                    return xmlSchema.asString();
                });
    }

    @Override
    public void setBpmnXml(String bpmnXml, SerializableConsumer<JsonValue> callback) {
        this.bpmnXml = bpmnXml;
        getElement().callJsFunction("reloadSchema", bpmnXml).then(callback);
    }

    @Override
    public void setElementColor(SetElementColorCmd cmd) {
        callJsEncodedArgumentFunction("setElementColor", cmd);
    }

    @Override
    public void addMarker(AddMarkerCmd cmd) {
        callJsEncodedArgumentFunction("addMarker", cmd);
    }

    @Override
    public void removeMarker(RemoveMarkerCmd cmd) {
        callJsEncodedArgumentFunction("removeMarker", cmd);
    }

    @Override
    public void addSelectedElementChangedListener(ComponentEventListener<SelectionChangedEvent> listener) {
        addListener(SelectionChangedEvent.class, listener);
    }

    @Override
    public void resetZoom() {
        callJsFunction("resetZoom");
    }

    @Override
    public void setIncidentCount(SetIncidentCountCmd cmd) {
        callJsEncodedArgumentFunction("setIncidentCount", cmd);
    }

    public Registration addImportCompleteListener(ComponentEventListener<ImportCompleteEvent> listener) {
        return addListener(ImportCompleteEvent.class, listener);
    }

    private void callJsEncodedArgumentFunction(String cmdName, Object cmd) {
        String encodedCmd;
        try {
            encodedCmd = objectMapper.writeValueAsString(cmd);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
        getElement().callJsFunction(cmdName, encodedCmd);
    }

    private void callJsFunction(String cmdName) {
        getElement().callJsFunction(cmdName);
    }
}