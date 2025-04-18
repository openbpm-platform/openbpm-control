package io.openbpm.control.view.dmnviewer;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.function.SerializableConsumer;
import elemental.json.JsonValue;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.view.ViewComponent;
import io.openbpm.control.entity.decisioninstance.HistoricDecisionInstanceShortData;
import io.openbpm.control.uicomponent.dmnviewer.DmnViewer1;
import io.openbpm.control.uicomponent.dmnviewer.command.OutputData;
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

    public void showDecisionInstance(HistoricDecisionInstanceShortData decisionInstance) {
        if (dmnViewer1 != null) {
            dmnViewer1.showDecisionInstance(createDecisionInstanceClientData(decisionInstance));
        }
    }

    public void addImportCompleteListener(ComponentEventListener<ImportCompleteEvent> listener) {
        if (dmnViewer1 != null) {
            dmnViewer1.addImportCompleteListener(listener);
        }
    }

    private ShowDecisionInstanceCmd createDecisionInstanceClientData(
            HistoricDecisionInstanceShortData decisionInstance) {
        ShowDecisionInstanceCmd decisionInstanceClientData = new ShowDecisionInstanceCmd();
        decisionInstanceClientData.setOutputDataList(decisionInstance.getOutputs().stream().map(output -> {
            OutputData result = new OutputData();
            result.setValue(output.getValue() != null ? output.getValue().toString() : "");
            result.setDataRowId(output.getRuleId());
            result.setDataColId(output.getClauseId());
            return result;
        }).toList());
        return decisionInstanceClientData;
    }
}

