package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AddTimesheet {

    @SerializedName("punch_in")
    private String punchIn;
    @SerializedName("punch_out")
    private String punchOut;
    @SerializedName("manual_punch_in")
    private String manualPunchIn;
    @SerializedName("manual_punch_out")
    private String manualPunchOut;

    @SerializedName("activity_user_array")
    private ArrayList<ProjectTask> mProjectTaskArrayList;

    public AddTimesheet(String punchIn, String punchOut, String manualPunchIn, String manualPunchOut, ArrayList<ProjectTask> mProjectTaskArrayList) {
        this.punchIn = punchIn;
        this.punchOut = punchOut;
        this.manualPunchIn = manualPunchIn;
        this.manualPunchOut = manualPunchOut;
        this.mProjectTaskArrayList = mProjectTaskArrayList;
    }

    public String getPunchIn() {
        return punchIn;
    }

    public String getManualPunchIn() {
        return manualPunchIn;
    }

    public String getManualPunchOut() {
        return manualPunchOut;
    }

    public String getPunchOut() {
        return punchOut;
    }

    public ArrayList<ProjectTask> getmProjectTaskArrayList() {
        return mProjectTaskArrayList;
    }
}
