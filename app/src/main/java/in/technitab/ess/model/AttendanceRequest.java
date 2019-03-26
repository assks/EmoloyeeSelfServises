package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

public class AttendanceRequest {
    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private int id;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("date")
    private String date;
    @SerializedName("update_date")
    private String updateDate;
    @SerializedName("attendacne")
    private String attendance;
    @SerializedName("attendance_duration")
    private String attendanceDuration;
    @SerializedName("timesheet_duration")
    private String timesheetDuration;
    @SerializedName("punch_in")
    private String punchIn;
    @SerializedName("punch_out")
    private String punchOut;

}
