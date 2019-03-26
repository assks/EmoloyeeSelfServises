package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

public class ProjectUser {

    @SerializedName("project_name")
    private String projectName;
    @SerializedName("project_type")
    private String projectType;
    @SerializedName("start_date")
    private String startDate;
    @SerializedName("end_date")
    private String endDate;

    public ProjectUser(String projectName, String projectType, String startDate, String endDate) {
        this.projectName = projectName;
        this.projectType = projectType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectType() {
        return projectType;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
