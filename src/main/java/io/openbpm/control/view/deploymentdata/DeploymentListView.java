package io.openbpm.control.view.deploymentdata;

import com.vaadin.flow.router.Route;
import io.jmix.core.LoadContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.DialogMode;
import io.jmix.flowui.view.Install;
import io.jmix.flowui.view.LookupComponent;
import io.jmix.flowui.view.StandardListView;
import io.jmix.flowui.view.Target;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.openbpm.control.entity.DeploymentData;
import io.openbpm.control.entity.filter.DeploymentFilter;
import io.openbpm.control.entity.filter.ProcessDefinitionFilter;
import io.openbpm.control.service.deployment.DeploymentLoadContext;
import io.openbpm.control.service.deployment.DeploymentService;
import io.openbpm.control.service.processdefinition.ProcessDefinitionLoadContext;
import io.openbpm.control.view.main.MainView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

@Route(value = "bpmn/deployments", layout = MainView.class)
@ViewController(id = "bpm_Deployment.list")
@ViewDescriptor(path = "deployment-list-view.xml")
public class DeploymentListView extends StandardListView<DeploymentData> {

    @ViewComponent
    private InstanceContainer<DeploymentFilter> deploymentFilterDc;
    @Autowired
    private DeploymentService deploymentService;

    @Install(to = "deploymentDatasDl", target = Target.DATA_LOADER)
    protected List<DeploymentData> deploymentDatasDlLoadDelegate(LoadContext<DeploymentData> loadContext) {
        LoadContext.Query query = loadContext.getQuery();
        DeploymentFilter filter = deploymentFilterDc.getItemOrNull();

        DeploymentLoadContext context = new DeploymentLoadContext().setFilter(filter);
        if (query != null) {
            context = context.setFirstResult(query.getFirstResult())
                    .setMaxResults(query.getMaxResults())
                    .setSort(query.getSort());
        }

        return deploymentService.findAll(context);
    }

    @Install(to = "deploymentDatasDataGrid.remove", subject = "delegate")
    private void deploymentDatasDataGridRemoveDelegate(final Collection<DeploymentData> collection) {
        for (DeploymentData entity : collection) {
            // Here you can remove entities from an external storage
        }
    }
}
