package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TripResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("response")
    private ArrayList<Trip> tripArrayList;


    public TripResponse(String status, ArrayList<Trip> tripArrayList) {
        this.status = status;
        this.tripArrayList = tripArrayList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Trip> getTripArrayList() {
        return tripArrayList;
    }

    public void setTripArrayList(ArrayList<Trip> tripArrayList) {
        this.tripArrayList = tripArrayList;
    }
}
