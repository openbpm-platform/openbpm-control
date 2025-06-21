package io.openbpm.control.security;

import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.role.annotation.EntityAttributePolicy;
import io.jmix.security.role.annotation.EntityPolicy;
import io.jmix.security.role.annotation.ResourceRole;
import io.jmix.security.role.annotation.SpecificPolicy;
import io.jmix.securityflowui.role.annotation.MenuPolicy;
import io.jmix.securityflowui.role.annotation.ViewPolicy;
import io.openbpm.control.entity.EngineConnectionCheckResult;
import io.openbpm.control.entity.ExternalTaskData;
import io.openbpm.control.entity.ProcessExecutionGraphEntry;
import io.openbpm.control.entity.UserTaskData;
import io.openbpm.control.entity.activity.ActivityInstanceTreeItem;
import io.openbpm.control.entity.activity.ActivityShortData;
import io.openbpm.control.entity.activity.HistoricActivityInstanceData;
import io.openbpm.control.entity.dashboard.IncidentStatistics;
import io.openbpm.control.entity.dashboard.ProcessDefinitionStatistics;
import io.openbpm.control.entity.decisiondefinition.DecisionDefinitionData;
import io.openbpm.control.entity.decisioninstance.HistoricDecisionInputInstanceShortData;
import io.openbpm.control.entity.decisioninstance.HistoricDecisionInstanceShortData;
import io.openbpm.control.entity.decisioninstance.HistoricDecisionOutputInstanceShortData;
import io.openbpm.control.entity.deployment.ResourceDeploymentReport;
import io.openbpm.control.entity.deployment.ResourceValidationError;
import io.openbpm.control.entity.engine.BpmEngine;
import io.openbpm.control.entity.filter.*;
import io.openbpm.control.entity.incident.HistoricIncidentData;
import io.openbpm.control.entity.incident.IncidentData;
import io.openbpm.control.entity.job.JobData;
import io.openbpm.control.entity.job.JobDefinitionData;
import io.openbpm.control.entity.processdefinition.ProcessDefinitionData;
import io.openbpm.control.entity.processinstance.ProcessInstanceData;
import io.openbpm.control.entity.variable.HistoricVariableInstanceData;
import io.openbpm.control.entity.variable.ObjectTypeInfo;
import io.openbpm.control.entity.variable.VariableInstanceData;
import io.openbpm.control.entity.variable.VariableValueInfo;

@ResourceRole(name = "ViewerAccessRole", code = ViewerAccessRole.CODE, scope = "UI")
public interface ViewerAccessRole {
    String CODE = "viewer-access-role";

    @MenuPolicy(menuIds = {"AboutProductView", "bpm_ProcessDefinition.list", "bpm_ProcessInstance.list", "bpm_AllTasksView", "bpm_DecisionDefinition.list", "IncidentData.list"})
    @ViewPolicy(viewIds = {"AboutProductView", "bpm_ProcessDefinition.list", "bpm_ProcessInstance.list", "bpm_AllTasksView", "bpm_DecisionDefinition.list", "IncidentData.list", "MainView", "EngineConnectionSettingsView", "bpm_ProcessDefinition.detail", "ProcessDefinitionDiagramView", "bpm_DecisionInstance.detail", "bpm_VariableInstanceData.detail", "JobData.detail", "bpm_DecisionDefinition.detail", "HistoricActivityInstanceData.detail", "HistoricIncidentData.detail", "HistoricVariableInstanceData.detail", "IncidentData.detail", "JobErrorDetailsView", "LoginView", "bpm_ProcessInstanceData.detail"})
    void screens();

    @SpecificPolicy(resources = {"ui.loginToUi", "ui.genericfilter.modifyConfiguration", "ui.genericfilter.modifyGlobalConfiguration", "ui.genericfilter.modifyJpqlCondition", "ui.showExceptionDetails"})
    void specific();

    @EntityAttributePolicy(entityClass = BpmEngine.class, attributes = {"id", "authEnabled", "baseUrl", "type", "name", "isDefault", "version", "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate", "deletedBy", "deletedDate", "authType"}, action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = BpmEngine.class, actions = EntityPolicyAction.READ)
    void bpmEngine();

    @EntityPolicy(entityClass = ActivityShortData.class, actions = EntityPolicyAction.READ)
    void activityShortData();

    @EntityAttributePolicy(entityClass = ActivityFilter.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = ActivityFilter.class, actions = EntityPolicyAction.READ)
    void activityFilter();

    @EntityPolicy(entityClass = ActivityInstanceTreeItem.class, actions = EntityPolicyAction.READ)
    @EntityAttributePolicy(entityClass = ActivityInstanceTreeItem.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    void activityInstanceTreeItem();

    @EntityAttributePolicy(entityClass = DecisionDefinitionData.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = DecisionDefinitionData.class, actions = EntityPolicyAction.READ)
    void decisionDefinitionData();

    @EntityAttributePolicy(entityClass = DecisionDefinitionFilter.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = DecisionDefinitionFilter.class, actions = EntityPolicyAction.READ)
    void decisionDefinitionFilter();

    @EntityAttributePolicy(entityClass = DecisionInstanceFilter.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = DecisionInstanceFilter.class, actions = EntityPolicyAction.READ)
    void decisionInstanceFilter();

    @EntityAttributePolicy(entityClass = EngineConnectionCheckResult.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = EngineConnectionCheckResult.class, actions = EntityPolicyAction.READ)
    void engineConnectionCheckResult();

    @EntityAttributePolicy(entityClass = ExternalTaskData.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = ExternalTaskData.class, actions = EntityPolicyAction.READ)
    void externalTaskData();

    @EntityAttributePolicy(entityClass = ExternalTaskFilter.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = ExternalTaskFilter.class, actions = EntityPolicyAction.READ)
    void externalTaskFilter();

    @EntityAttributePolicy(entityClass = HistoricActivityInstanceData.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = HistoricActivityInstanceData.class, actions = EntityPolicyAction.READ)
    void historicActivityInstanceData();

    @EntityAttributePolicy(entityClass = HistoricDecisionInputInstanceShortData.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = HistoricDecisionInputInstanceShortData.class, actions = EntityPolicyAction.READ)
    void historicDecisionInputInstanceShortData();

    @EntityAttributePolicy(entityClass = HistoricDecisionInstanceShortData.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = HistoricDecisionInstanceShortData.class, actions = EntityPolicyAction.READ)
    void historicDecisionInstanceShortData();

    @EntityAttributePolicy(entityClass = HistoricDecisionOutputInstanceShortData.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = HistoricDecisionOutputInstanceShortData.class, actions = EntityPolicyAction.READ)
    void historicDecisionOutputInstanceShortData();

    @EntityAttributePolicy(entityClass = HistoricIncidentData.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = HistoricIncidentData.class, actions = EntityPolicyAction.READ)
    void historicIncidentData();

    @EntityAttributePolicy(entityClass = HistoricVariableInstanceData.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = HistoricVariableInstanceData.class, actions = EntityPolicyAction.READ)
    void historicVariableInstanceData();

    @EntityAttributePolicy(entityClass = IncidentData.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = IncidentData.class, actions = EntityPolicyAction.READ)
    void incidentData();

    @EntityAttributePolicy(entityClass = IncidentFilter.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = IncidentFilter.class, actions = EntityPolicyAction.READ)
    void incidentFilter();

    @EntityAttributePolicy(entityClass = IncidentStatistics.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = IncidentStatistics.class, actions = EntityPolicyAction.READ)
    void incidentStatistics();

    @EntityAttributePolicy(entityClass = JobData.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = JobData.class, actions = EntityPolicyAction.READ)
    void jobData();

    @EntityAttributePolicy(entityClass = JobDefinitionData.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = JobDefinitionData.class, actions = EntityPolicyAction.READ)
    void jobDefinitionData();

    @EntityAttributePolicy(entityClass = JobFilter.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = JobFilter.class, actions = EntityPolicyAction.READ)
    void jobFilter();

    @EntityAttributePolicy(entityClass = ObjectTypeInfo.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = ObjectTypeInfo.class, actions = EntityPolicyAction.READ)
    void objectTypeInfo();

    @EntityAttributePolicy(entityClass = ProcessDefinitionData.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = ProcessDefinitionData.class, actions = EntityPolicyAction.READ)
    void processDefinitionData();

    @EntityAttributePolicy(entityClass = UserTaskFilter.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = UserTaskFilter.class, actions = EntityPolicyAction.READ)
    void userTaskFilter();

    @EntityAttributePolicy(entityClass = VariableFilter.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = VariableFilter.class, actions = EntityPolicyAction.READ)
    void variableFilter();

    @EntityAttributePolicy(entityClass = UserTaskData.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = UserTaskData.class, actions = EntityPolicyAction.READ)
    void userTaskData();

    @EntityAttributePolicy(entityClass = ResourceValidationError.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = ResourceValidationError.class, actions = EntityPolicyAction.READ)
    void resourceValidationError();

    @EntityAttributePolicy(entityClass = ResourceDeploymentReport.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = ResourceDeploymentReport.class, actions = EntityPolicyAction.READ)
    void resourceDeploymentReport();

    @EntityAttributePolicy(entityClass = ProcessInstanceData.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = ProcessInstanceData.class, actions = EntityPolicyAction.READ)
    void processInstanceData();

    @EntityAttributePolicy(entityClass = ProcessInstanceFilter.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = ProcessInstanceFilter.class, actions = EntityPolicyAction.READ)
    void processInstanceFilter();

    @EntityAttributePolicy(entityClass = ProcessDefinitionStatistics.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = ProcessDefinitionStatistics.class, actions = EntityPolicyAction.READ)
    void processDefinitionStatistics();

    @EntityAttributePolicy(entityClass = ProcessExecutionGraphEntry.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = ProcessExecutionGraphEntry.class, actions = EntityPolicyAction.READ)
    void processExecutionGraphEntry();

    @EntityAttributePolicy(entityClass = ProcessDefinitionFilter.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityPolicy(entityClass = ProcessDefinitionFilter.class, actions = EntityPolicyAction.READ)
    void processDefinitionFilter();

    @EntityPolicy(entityClass = VariableValueInfo.class, actions = EntityPolicyAction.READ)
    @EntityAttributePolicy(entityClass = VariableValueInfo.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    void variableValueInfo();

    @EntityPolicy(entityClass = VariableInstanceData.class, actions = EntityPolicyAction.READ)
    @EntityAttributePolicy(entityClass = VariableInstanceData.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    void variableInstanceData();
}