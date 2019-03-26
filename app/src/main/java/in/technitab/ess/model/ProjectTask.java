package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

import in.technitab.ess.util.ConstantVariable;

public class ProjectTask {

    @SerializedName(ConstantVariable.Tbl_TimeSheet.PROJECT_ID)
    private int projectId;
    @SerializedName(ConstantVariable.Tbl_TimeSheet.PROJECT)
    private String project;
    @SerializedName(ConstantVariable.Tbl_TimeSheet.PROJECT_TYPE_ID)
    private String projectTypeId;
    @SerializedName(ConstantVariable.Tbl_TimeSheet.ACTIVITY_ID)
    private String activityId;
    @SerializedName(ConstantVariable.Tbl_TimeSheet.ACTIVITY_TYPE_ID)
    private int activityTypeId;
    @SerializedName(ConstantVariable.Tbl_TimeSheet.ACTIVITY)
    private String activity;
    @SerializedName(ConstantVariable.Tbl_TimeSheet.TIME_SPENT)
    private String spentHours;
    @SerializedName(ConstantVariable.Tbl_TimeSheet.DESCRIPTION)
    private String description;
    @SerializedName(ConstantVariable.Tbl_TimeSheet.IS_BILLABLE)
    private int isBillable;


    public ProjectTask(int projectId, String project, String projectTypeId, String activityId, int activityTypeId, String activity, String spentHours, String description, int isBillable) {
        this.projectId = projectId;
        this.project = project;
        this.projectTypeId = projectTypeId;
        this.activityId = activityId;
        this.activityTypeId = activityTypeId;
        this.activity = activity;
        this.spentHours = spentHours;
        this.description = description;
        this.isBillable = isBillable;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getProject() {
        return project;
    }

    public String getProjectTypeId() {
        return projectTypeId;
    }

    public String getActivityId() {
        return activityId;
    }

    public int getActivityTypeId() {
        return activityTypeId;
    }

    public String getActivity() {
        return activity;
    }

    public String getSpentHours() {
        return spentHours;
    }

    public String getDescription() {
        return description;
    }

    public int getIsBillable() {
        return isBillable;
    }

    public void setSpentHours(String spentHours) {
        this.spentHours = spentHours;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsBillable(int isBillable) {
        this.isBillable = isBillable;
    }
}
