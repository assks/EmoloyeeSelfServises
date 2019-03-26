package in.technitab.ess.model;


import com.google.gson.annotations.SerializedName;

import in.technitab.ess.util.ConstantVariable;


public class TecTripBooking {

    @SerializedName(ConstantVariable.TripBooking.ID)
    private int id;
    @SerializedName(ConstantVariable.TripBooking.TRIP_ID)
    private int trip_id;
    @SerializedName(ConstantVariable.TripBooking.TRAVEL_TYPE)
    private String travel_type;
    @SerializedName(ConstantVariable.TripBooking.ADMIN_BOOKING_MODE)
    private String adminBookingMode;
    @SerializedName(ConstantVariable.TripBooking.ADMIN_VENDOR)
    private String adminVendor;
    @SerializedName(ConstantVariable.TripBooking.ADMIN_VENDOR_ID)
    private int adminVendorId;
    @SerializedName(ConstantVariable.TripBooking.ADMIN_CITY_AREA)
    private String adminCityArea;
    @SerializedName(ConstantVariable.TripBooking.ADMIN_SOURCE)
    private String adminSource;
    @SerializedName(ConstantVariable.TripBooking.ADMIN_DESTINATION)
    private String adminDestination;
    @SerializedName(ConstantVariable.TripBooking.ADMIN_INSTRUCTION)
    private String adminInstruction;
    @SerializedName(ConstantVariable.TripBooking.ADMIN_CHECK_IN)
    private String adminCheckIn;
    @SerializedName(ConstantVariable.TripBooking.ADMIN_CHECK_OUT)
    private String adminCheckOut;
    @SerializedName(ConstantVariable.TripBooking.ADMIN_ROOM)
    private int adminRoom;
    @SerializedName(ConstantVariable.TripBooking.ADMIN_TOTAL_AMOUNT)
    private double adminTotalAmount;
    @SerializedName(ConstantVariable.TripBooking.ADMIN_ARRIVAL_DATE_TIME)
    private String adminArrival;
    @SerializedName(ConstantVariable.TripBooking.ADMIN_DEPARTURE_DATE_TIME)
    private String adminDeparture;
    @SerializedName(ConstantVariable.TripBooking.TOTAL_AMOUNT)
    private double totalAmount;
    @SerializedName(ConstantVariable.TripBooking.ADMIN_BOOKING_ATTACHMENT)
    private String bookingAttachment;
    @SerializedName(ConstantVariable.TripBooking.USER_TOTAL_AMOUNT)
    private double user_total_amount;
    @SerializedName(ConstantVariable.TripBooking.TRIP_STATUS)
    private String trip_status;
    @SerializedName(ConstantVariable.TripBooking.PAYMENT_ID)
    private int paymentId;
    @SerializedName(ConstantVariable.TripBooking.PAID_BY)
    private String paidBy;
    @SerializedName(ConstantVariable.TripBooking.VENDOR_NAME)
    private String vendor_name;
    @SerializedName(ConstantVariable.TripBooking.TOTAL)
    private double total;
    private boolean isSelected;


    public TecTripBooking() {
        this.id = 0;
        this.trip_id = 0;
        this.travel_type = "";
        this.user_total_amount = 0;
        this.trip_status = "";
        this.vendor_name = "";
        this.adminBookingMode = "";
        adminVendor = "";
        adminVendorId = 0;
        adminCityArea = "";
        adminSource = "";
        adminDestination = "";
        adminInstruction = "";
        adminCheckIn = "";
        adminCheckOut = "";
        adminRoom = 0;
        adminTotalAmount = 0;
        adminArrival = "";
        adminDeparture = "";
        totalAmount = 0;
        bookingAttachment = "";
        paymentId = 0;
        paidBy = "";
        this.total = 0;
        isSelected = false;

    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTripId() {
        return trip_id;
    }

    public String getTravelType() {
        return travel_type;
    }


    public void setAdminBookingMode(String adminBookingMode) {
        this.adminBookingMode = adminBookingMode;
    }

    public void setAdminCityArea(String adminCityArea) {
        this.adminCityArea = adminCityArea;
    }

    public String getAdminVendor() {
        return adminVendor;
    }


    public int getAdminVendorId() {
        return adminVendorId;
    }


    public String getAdminSource() {
        return adminSource;
    }

    public String getAdminBookingMode() {
        return adminBookingMode;
    }

    public String getAdminCityArea() {
        return adminCityArea;
    }


    public String getAdminDestination() {
        return adminDestination;
    }


    public String getAdminInstruction() {
        return adminInstruction;
    }


    public String getAdminCheckIn() {
        return adminCheckIn;
    }


    public String getAdminCheckOut() {
        return adminCheckOut;
    }

    public int getAdminRoom() {
        return adminRoom;
    }


    public double getAdminTotalAmount() {
        return adminTotalAmount;
    }


    public String getAdminArrival() {
        return adminArrival;
    }


    public String getAdminDeparture() {
        return adminDeparture;
    }


    public double getTotalAmount() {
        return totalAmount;
    }


    public String getBookingAttachment() {
        return bookingAttachment;
    }


    public double getUserTotalAmount() {
        return user_total_amount;
    }

    public String getTripStatus() {
        return trip_status;
    }

    public String getVendorName() {
        return vendor_name;
    }

    public double getTotal() {
        return total;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
