package in.technitab.ess.model;



import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class GuesthouseBooking {
    @SerializedName("check_in")
    private String checkIn;
    @SerializedName("check_out")
    private String checkOut;
    @SerializedName("paid_via")
    private String paidVia;
    @SerializedName("reference_number")
    private String referenceNumber;
    @SerializedName("created_date")
    private String createdDate;
    @SerializedName("created_by_id")
    private int createdById;
    @SerializedName("status")
    private String status;
    @SerializedName("rent")
    private double rent;
    private boolean roomAvailabilityCheck;
    private boolean ruleCheck;

    public GuesthouseBooking(String checkIn, String checkOut,String paidVia, String referenceNumber,boolean roomAvailabilityCheck,boolean ruleCheck) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.paidVia = paidVia;
        this.referenceNumber = referenceNumber;
        this.roomAvailabilityCheck = roomAvailabilityCheck;
        this.ruleCheck = ruleCheck;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public boolean isRoomAvailabilityCheck() {
        return roomAvailabilityCheck;
    }

    public boolean isRuleCheck() {
        return ruleCheck;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getPaidVia() {
        return paidVia;
    }

    public void setPaidVia(String paidVia) {
        this.paidVia = paidVia;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getCreatedById() {
        return createdById;
    }

    public void setCreatedById(int createdById) {
        this.createdById = createdById;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public StringResponse validate(GuesthouseBooking booking){
        boolean isError = false;
        String message = "";
        if (TextUtils.isEmpty(booking.getCheckIn())){
            isError = true;
            message = "Check in date is required";
        }else if (TextUtils.isEmpty(booking.getCheckOut())){
            isError = true;
            message = "Check out date is required";
        }else if (TextUtils.isEmpty(booking.getReferenceNumber()) && !booking.getPaidVia().equalsIgnoreCase("Cash")){
            isError = true;
            message = "Reference number is required";
        }else if (!booking.isRoomAvailabilityCheck()){
            isError = true;
            message = "Room availabilty check required";
        }else if (!booking.ruleCheck){
            isError = true;
            message = "Guesthouse rule check is required";
        }

        return new StringResponse(isError,message);
    }
}
