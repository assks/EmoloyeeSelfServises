package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

public class TimesheetDataResponse {

    @SerializedName(value = "project", alternate = {"date"})
    private String name;
    @SerializedName("spent_time")
    private String spentTime;

    public TimesheetDataResponse(String name, String spentTime) {
        this.name = name;
        this.spentTime = spentTime;
    }

    public String getName() {
        return name;
    }

    public String getSpentTime() {
        return spentTime;
    }
}
