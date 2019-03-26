package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import in.technitab.ess.util.ConstantVariable;

public class AttendanceCalendar {

    @SerializedName("date")
    private Date date;
    @SerializedName("attendanceType")
    private String attendanceType;
    @SerializedName("hrs")
    private String spentTime;
    @SerializedName("punch_in")
    private String punchIn;
    @SerializedName("punch_out")
    private String punchOut;
    @SerializedName("manual_punch_in")
    private String manualPunchIn;
    @SerializedName("manual_punchout")
    private String manualPunchOut;
    @SerializedName("summery")
    private String summery;
    @SerializedName(ConstantVariable.Tbl_Attendance.TIMESHEET_HOURS)
    private String timesheetHours;


    public AttendanceCalendar(Date date, String attendanceType, String spentTime, String punchIn, String punchOut, String manualPunchIn, String manualPunchOut, String summery, String timesheetHours) {
        this.date = date;
        this.attendanceType = attendanceType;
        this.spentTime = spentTime;
        this.punchIn = punchIn;
        this.punchOut = punchOut;
        this.manualPunchIn = manualPunchIn;
        this.manualPunchOut = manualPunchOut;
        this.summery = summery;
        this.timesheetHours = timesheetHours;
    }

    public Date getDate() {
        return date;
    }

    public String getAttendanceType() {
        return attendanceType;
    }

    public String getSpentTime() {
        return spentTime;
    }

    public String getPunchIn() {
        return punchIn;
    }

    public String getPunchOut() {
        return punchOut;
    }

    public String getManualPunchIn() {
        return manualPunchIn;
    }

    public String getManualPunchOut() {
        return manualPunchOut;
    }

    public String getSummery() {
        return summery;
    }

    public String getTimesheetHours() {
        return timesheetHours;
    }
}
