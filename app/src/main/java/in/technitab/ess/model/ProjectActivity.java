package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

public class ProjectActivity {
    @SerializedName("id")
    private int id;
    @SerializedName("activity_type")
    private String activityType;
    @SerializedName("activity_type_id")
    private int activityTypeId;
    @SerializedName("is_selected")
    private boolean isSelected;

    public ProjectActivity(int id, String activityType, int activityTypeId, boolean isSelected) {
        this.id = id;
        this.activityType = activityType;
        this.activityTypeId = activityTypeId;
        this.isSelected = isSelected;
    }

    public int getId() {
        return id;
    }

    public String getActivityType() {
        return activityType;
    }

    public int getActivityTypeId() {
        return activityTypeId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public void setActivityTypeId(int activityTypeId) {
        this.activityTypeId = activityTypeId;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
