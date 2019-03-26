package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TecTripResponse {
    @SerializedName("trip_bookings")
    private ArrayList<TecTripBooking> mTripBookings;
    @SerializedName("tec_entries")
    private ArrayList<TecEntryResponse> mTecEntrys;

    public TecTripResponse(ArrayList<TecTripBooking> mTripBookings, ArrayList<TecEntryResponse> mTecEntrys) {
        this.mTripBookings = mTripBookings;
        this.mTecEntrys = mTecEntrys;
    }

    public ArrayList<TecTripBooking> getTripBookings() {
        return mTripBookings;
    }

    public ArrayList<TecEntryResponse> getTecEntrys() {
        return mTecEntrys;
    }


}
