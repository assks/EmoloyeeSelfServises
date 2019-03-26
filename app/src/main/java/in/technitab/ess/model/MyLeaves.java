package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MyLeaves {


    @SerializedName("id")
    private int id;
    @SerializedName("leave_type")
    private String leaveType;
    @SerializedName("total_days")
    private double totalDays;
    @SerializedName("total_leaves")
    private double totalLeaves;
    @SerializedName("leave_location")
    private String leaveLocation;
    @SerializedName("reason")
    private String reason;
    @SerializedName("description")
    private String description;
    @SerializedName("status")
    private String status;
    @SerializedName("leave_array")
    private ArrayList<ParticularLeave> leaveArrayList;

    public MyLeaves(int id, String leaveType, double totalDays, double totalLeaves, String leaveLocation, String reason, String description, String status, ArrayList<ParticularLeave> leaveArrayList) {
        this.id = id;
        this.leaveType = leaveType;
        this.totalDays = totalDays;
        this.totalLeaves = totalLeaves;
        this.leaveLocation = leaveLocation;
        this.reason = reason;
        this.description = description;
        this.status = status;
        this.leaveArrayList = leaveArrayList;
    }


    public int getId() {
        return id;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public double getTotalDays() {
        return totalDays;
    }

    public double getTotalLeaves() {
        return totalLeaves;
    }

    public String getLeaveLocation() {
        return leaveLocation;
    }

    public String getReason() {
        return reason;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<ParticularLeave> getLeaveArrayList() {
        return leaveArrayList;
    }
}
