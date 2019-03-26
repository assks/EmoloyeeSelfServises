package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TimeSheetDate {
    @SerializedName(value = "date", alternate = {"project"})
    private String date;
    @SerializedName(value = "date_response", alternate = {"project_response"})
    private ArrayList<TimesheetDataResponse> responses;

    public TimeSheetDate(String date, ArrayList<TimesheetDataResponse> responses) {
        this.date = date;
        this.responses = responses;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<TimesheetDataResponse> getResponses() {
        return responses;
    }
}
