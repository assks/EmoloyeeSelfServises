package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

import in.technitab.ess.util.ConstantVariable;

public class RequestedProject {
    @SerializedName(ConstantVariable.MIX_ID.ID)
    private int id;
    @SerializedName(ConstantVariable.RequestedProject.PROJECT_ID)
    private int project_id;
    @SerializedName(ConstantVariable.RequestedProject.PROJECT_NAME)
    private String projectName;
    @SerializedName(ConstantVariable.RequestedProject.PROJECT_TYPE_ID)
    private int projectTypeId;
    @SerializedName(ConstantVariable.RequestedProject.CREATED_BY_ID)
    private int createdById;
    @SerializedName(ConstantVariable.RequestedProject.CREATED_BY)
    private String createdBy;


    public RequestedProject(int id, int project_id, String projectName,int projectTypeId, int createdById, String createdBy) {
        this.id = id;
        this.project_id = project_id;
        this.projectName = projectName;
        this.projectTypeId = projectTypeId;
        this.createdById = createdById;
        this.createdBy = createdBy;
    }

    public int getId() {
        return id;
    }

    public int getProjectId() {
        return project_id;
    }

    public String getProjectName() {
        return projectName;
    }

    public int getProjectTypeId() {
        return projectTypeId;
    }

    public int getCreatedById() {
        return createdById;
    }

    public String getCreatedBy() {
        return createdBy;
    }
}
