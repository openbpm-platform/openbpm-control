package io.openbpm.control.view.dmnviewer;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.function.SerializableConsumer;
import elemental.json.JsonValue;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.view.ViewComponent;
import io.openbpm.control.uicomponent.dmnviewer.DmnViewer1;
import io.openbpm.control.uicomponent.dmnviewer.command.ShowDecisionInstanceCmd;
import io.openbpm.control.uicomponent.dmnviewer.event.ImportCompleteEvent;

@FragmentDescriptor("dmn-viewer-fragment.xml")
public class DmnViewerFragmentNew extends Fragment<Div> {

    @ViewComponent
    protected Div viewerContainer;

    protected DmnViewer1 dmnViewer1;

    public void initViewer() {
        this.dmnViewer1 = uiComponents.create(DmnViewer1.class);
        viewerContainer.removeAll();
        viewerContainer.add(dmnViewer1);
    }

    public void setDmnXml(String dmnXml) {
        if (dmnViewer1 != null) {
            dmnViewer1.setDmnXml(dmnXml);
        }
    }

    public void setDmnXml(String dmnXml, SerializableConsumer<JsonValue> callback) {
        if (dmnViewer1 != null) {
            dmnViewer1.setDmnXml(dmnXml, callback);
        }
    }

    public void showDecisionDefinition(String decisionDefinitionKey, SerializableConsumer<JsonValue> callback) {
        if (dmnViewer1 != null) {
            dmnViewer1.showDecisionDefinition(decisionDefinitionKey, callback);
        }
    }

    public void showDecisionInstance(ShowDecisionInstanceCmd cmd) {
        if (dmnViewer1 != null) {
            dmnViewer1.showDecisionInstance(cmd);
        }
    }

    public void addImportCompleteListener(ComponentEventListener<ImportCompleteEvent> listener) {
        if (dmnViewer1 != null) {
            dmnViewer1.addImportCompleteListener(listener);
        }
    }
}

