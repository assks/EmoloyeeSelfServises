package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

public class Leave {
    @SerializedName("id")
    private int id;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("leave_type")
    private String leaveType;
    @SerializedName("start_date")
    private String startDate;
    @SerializedName("end_date")
    private String endDate;
    @SerializedName("leave_duration")
    private String leaveDuration;
    @SerializedName("shift")
    private String shift;
    @SerializedName("status")
    private String status;
    @SerializedName("description")
    private String description;
    @SerializedName("reason")
    private String reason;
    @SerializedName("leave_location")
    private String leaveLocation;
    @SerializedName("created_by_id")
    private int createById;


    public Leave() {
        id = userId = createById = 0;
        leaveType = startDate = endDate = leaveDuration = shift = status = reason = leaveLocation = "";
    }


    public Leave(int id, int userId, String leaveType, String startDate, String endDate, String leaveDuration, String shift, String status, String description, String reason, String leaveLocation, int createById) {
        this.id = id;
        this.userId = userId;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.leaveDuration = leaveDuration;
        this.shift = shift;
        this.status = status;
        this.description = description;
        this.reason = reason;
        this.leaveLocation = leaveLocation;
        this.createById = createById;

    }


    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getLeaveDuration() {
        return leaveDuration;
    }

    public String getShift() {
        return shift;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getReason() {
        return reason;
    }

    public String getLeaveLocation() {
        return leaveLocation;
    }

    public int getCreateById() {
        return createById;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setLeaveDuration(String leaveDuration) {
        this.leaveDuration = leaveDuration;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setLeaveLocation(String leaveLocation) {
        this.leaveLocation = leaveLocation;
    }

    public void setCreateById(int createById) {
        this.createById = createById;
    }
}
