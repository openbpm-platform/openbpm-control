package io.openbpm.control.uicomponent.bpmnviewer.command;

public class ShowDecisionInstanceLinkOverlay {

    protected String activityId;
    protected String decisionInstanceId;
    protected String tooltipMessage;

    public ShowDecisionInstanceLinkOverlay(String activityId, String decisionInstanceId, String tooltipMessage) {
        this.activityId = activityId;
        this.decisionInstanceId = decisionInstanceId;
        this.tooltipMessage = tooltipMessage;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getDecisionInstanceId() {
        return decisionInstanceId;
    }

    public void setDecisionInstanceId(String decisionInstanceId) {
        this.decisionInstanceId = decisionInstanceId;
    }

    public String getTooltipMessage() {
        return tooltipMessage;
    }

    public void setTooltipMessage(String tooltipMessage) {
        this.tooltipMessage = tooltipMessage;
    }
}
