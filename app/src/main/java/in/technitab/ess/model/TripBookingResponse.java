package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TripBookingResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("response")
    private ArrayList<TripBooking> tripBookings;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTripBookings(ArrayList<TripBooking> tripBookings) {
        this.tripBookings = tripBookings;
    }

    public ArrayList<TripBooking> getTripBookings() {
        return tripBookings;
    }
}
