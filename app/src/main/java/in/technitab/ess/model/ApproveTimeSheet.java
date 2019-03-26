package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

import in.technitab.ess.util.ConstantVariable;

public class ApproveTimeSheet {
    @SerializedName("id")
    private int timesheetId;
    @SerializedName(ConstantVariable.UserPrefVar.NAME)
    private String name;
    @SerializedName(ConstantVariable.Tbl_TimeSheet.DATE)
    private String date;
    @SerializedName(ConstantVariable.Tbl_TimeSheet.ACTIVITY)
    private String activity;
    @SerializedName(ConstantVariable.Tbl_TimeSheet.TIME_SPENT)
    private String spentHours;
    @SerializedName(ConstantVariable.Tbl_TimeSheet.DESCRIPTION)
    private String description;
    @SerializedName(ConstantVariable.Tbl_TimeSheet.IS_BILLABLE)
    private int isBillable;

    public ApproveTimeSheet() {
        this.timesheetId = 0;
        this.name = "";
        this.date = "";
        this.activity = "";
        this.spentHours = "";
        this.description = "";
    }

    public ApproveTimeSheet(int timesheetId, String name, String date, String activity, String spentHours, String description, int isBillable) {
        this.timesheetId = timesheetId;
        this.name = name;
        this.date = date;
        this.activity = activity;
        this.spentHours = spentHours;
        this.description = description;
        this.isBillable = isBillable;
    }

    public int getTimesheetId() {
        return timesheetId;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
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

