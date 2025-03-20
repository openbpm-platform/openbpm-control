package io.openbpm.control.view.deploymentdata;

import com.vaadin.flow.router.Route;
import io.jmix.core.LoadContext;
import io.jmix.core.SaveContext;
import io.jmix.flowui.view.DialogMode;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.Install;
import io.jmix.flowui.view.PrimaryDetailView;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.Target;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.openbpm.control.entity.DeploymentData;
import io.openbpm.control.entity.processdefinition.ProcessDefinitionData;
import io.openbpm.control.view.main.MainView;

import java.util.Set;

@Route(value = "bpm/deployment/:id", layout = MainView.class)
@ViewController(id = "bpm_Deployment.detail")
@ViewDescriptor(path = "deployment-detail-view.xml")
@EditedEntityContainer("deploymentDataDc")
@DialogMode(width = "50em", height = "37.5em")
@PrimaryDetailView(DeploymentData.class)
public class DeploymentDetailView extends StandardDetailView<DeploymentData> {

    @Install(to = "deploymentDataDl", target = Target.DATA_LOADER)
    private DeploymentData customerDlLoadDelegate(final LoadContext<DeploymentData> loadContext) {
        Object id = loadContext.getId();
        // Here you can load the entity by id from an external storage.
        // Set the loaded entity to the not-new state using EntityStates.setNew(entity, false).
        return null;
    }

    @Install(target = Target.DATA_CONTEXT)
    private Set<Object> saveDelegate(final SaveContext saveContext) {
        DeploymentData entity = getEditedEntity();
        // Here you can save the entity to an external storage and return the saved instance.
        // Set the returned entity to the not-new state using EntityStates.setNew(entity, false).
        // If the new entity ID is assigned by the storage, set the ID to the original instance too 
        // to let the framework match the saved instance with the original one.
        DeploymentData saved = entity;
        return Set.of(saved);
    }
}
