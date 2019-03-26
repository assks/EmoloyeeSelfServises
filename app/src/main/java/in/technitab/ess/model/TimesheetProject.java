package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

public class TimesheetProject {
    @SerializedName("project")
    private String project;
    @SerializedName("activity_name")
    private String activityName;
    @SerializedName("spent_time")
    private String spentTime;

    public TimesheetProject(String project, String activityName, String spentTime) {
        this.project = project;
        this.activityName = activityName;
        this.spentTime = spentTime;
    }

    public String getProject() {
        return project;
    }

    public String getActivityName() {
        return activityName;
    }

    public String getSpentTime() {
        return spentTime;
    }
}
