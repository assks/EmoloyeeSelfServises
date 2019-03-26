package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TimeSheetCalender {

    @SerializedName("date_response")
    private ArrayList<TimesheetDataResponse> dataResponses;
    @SerializedName("project_response")
    private ArrayList<TimesheetProject> projectResponses;


    public TimeSheetCalender(ArrayList<TimesheetDataResponse> dataResponses, ArrayList<TimesheetProject> projectResponses) {
        this.dataResponses = dataResponses;
        this.projectResponses = projectResponses;
    }

    public ArrayList<TimesheetDataResponse> getDataResponses() {
        return dataResponses;
    }

    public ArrayList<TimesheetProject> getProjectResponses() {
        return projectResponses;
    }
}
