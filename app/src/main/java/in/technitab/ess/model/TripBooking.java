package in.technitab.ess.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.technitab.ess.util.ConstantVariable;

public class TripBooking implements Parcelable {

    @SerializedName(ConstantVariable.TripBooking.ID)
    private int id;
    @SerializedName(ConstantVariable.TripBooking.TRIP_ID)
    private int trip_id;
    @SerializedName(ConstantVariable.TripBooking.TRAVEL_TYPE)
    private String travel_type;
    @SerializedName(ConstantVariable.TripBooking.CREATE_BY)
    private String createdBy;
    @SerializedName(ConstantVariable.TripBooking.USER_BOOKING_MODE)
    private String user_booking_mode;
    @SerializedName(ConstantVariable.TripBooking.USER_RATE)
    private double userRate;
    @SerializedName(ConstantVariable.TripBooking.USER_CITY_AREA)
    private String user_city_area;
    @SerializedName(ConstantVariable.TripBooking.USER_VENDOR)
    private String user_vendor;
    @SerializedName(ConstantVariable.TripBooking.USER_VENDOR_ID)
    private int userVendorId;
    @SerializedName(ConstantVariable.TripBooking.USER_SOURCE)
    private String user_source;
    @SerializedName(ConstantVariable.TripBooking.USER_DESTINATION)
    private String user_destination;
    @SerializedName(ConstantVariable.TripBooking.USER_TRAVEL_DATE)
    private String user_travel_date;
    @SerializedName(ConstantVariable.TripBooking.USER_INSTRUCTION)
    private String user_instruction;
    @SerializedName(ConstantVariable.TripBooking.USER_CHECK_IN)
    private String user_check_in;
    @SerializedName(ConstantVariable.TripBooking.USER_CHECK_OUT)
    private String user_check_out;
    @SerializedName(ConstantVariable.TripBooking.USER_ROOM)
    private int user_room;
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
    @SerializedName(ConstantVariable.TripBooking.SERVICE_TAX_NAME)
    private String serviceTaxName;
    @SerializedName(ConstantVariable.TripBooking.SERVICE_TAX_PERCENT)
    private String serviceTaxPercent;
    @SerializedName(ConstantVariable.TripBooking.SERVICE_TAX_AMOUNT)
    private double serviceTaxAmount;
    @SerializedName(ConstantVariable.TripBooking.SERVICE_TAX_TYPE)
    private String serviceTaxType;
    @SerializedName(ConstantVariable.TripBooking.TOTAL_AMOUNT)
    private double totalAmount;
    @SerializedName(ConstantVariable.TripBooking.USER_TOTAL_AMOUNT)
    private double user_total_amount;
    @SerializedName(ConstantVariable.TripBooking.TRIP_STATUS)
    private String trip_status;
    @SerializedName(ConstantVariable.TripBooking.CREATED_BY_ID)
    private int created_by_id;
    @SerializedName(ConstantVariable.TripBooking.CREATED_DATE)
    private String created_date;
    @SerializedName(ConstantVariable.TripBooking.MODIFIED_BY_ID)
    private int modified_by_id;
    @SerializedName(ConstantVariable.TripBooking.MODIFIED_DATE)
    private String modified_date;
    @SerializedName(ConstantVariable.TripBooking.BILL_DATE)
    private String bill_date;
    @SerializedName(ConstantVariable.TripBooking.BILL_ID)
    private int bill_id;
    @SerializedName(ConstantVariable.TripBooking.BILL_NUMBER)
    private String bill_number;
    @SerializedName(ConstantVariable.TripBooking.PURCHASE_ORDER)
    private String purchase_order;
    @SerializedName(ConstantVariable.TripBooking.BILL_STATUS)
    private String bill_status;
    @SerializedName(ConstantVariable.TripBooking.DESTINATION_OF_SUPPLY)
    private String destination_of_supply;
    @SerializedName(ConstantVariable.TripBooking.GST_TEATMENT)
    private String GST_treatment;
    @SerializedName(ConstantVariable.TripBooking.GST_NUM)
    private String gst_num;
    @SerializedName(ConstantVariable.TripBooking.VENDOR_NAME)
    private String vendor_name;
    @SerializedName(ConstantVariable.TripBooking.DUE_DATE)
    private String due_date;
    @SerializedName(ConstantVariable.TripBooking.CURRENCY_CODE)
    private String currency_code;
    @SerializedName(ConstantVariable.TripBooking.ITEM_NAME)
    private String item_name;
    @SerializedName(ConstantVariable.TripBooking.ACCOUNT)
    private String account;
    @SerializedName(ConstantVariable.TripBooking.ITEM_DESCRIPTION)
    private String item_description;
    @SerializedName(ConstantVariable.TripBooking.QUANTITY)
    private int quantity;
    @SerializedName(ConstantVariable.TripBooking.USAGE_UNIT)
    private String usage_unit;
    @SerializedName(ConstantVariable.TripBooking.RATE)
    private double rate;
    @SerializedName(ConstantVariable.TripBooking.TAX_NAME)
    private String tax_name;
    @SerializedName(ConstantVariable.TripBooking.TAX_PERCENTAGE)
    private String tax_percentage;
    @SerializedName(ConstantVariable.TripBooking.TAX_AMOUNT)
    private double tax_amount;
    @SerializedName(ConstantVariable.TripBooking.TAX_TYPE)
    private String tax_type;
    @SerializedName(ConstantVariable.TripBooking.ITEM_TOTAL)
    private double item_total;
    @SerializedName(ConstantVariable.TripBooking.SUB_TOTAL)
    private double sub_total;
    @SerializedName(ConstantVariable.TripBooking.TOTAL)
    private double total;
    @SerializedName(ConstantVariable.TripBooking.PAYMENT_ID)
    private int paymentId;
    @SerializedName(ConstantVariable.TripBooking.ADMIN_BOOKING_ATTACHMENT)
    private String bookingAttachment;
    @SerializedName(ConstantVariable.TripBooking.TRIP_BOOKING_MEMBER)
    private ArrayList<TripMember> bookingMembers;


    public TripBooking() {
        this.id = 0;
        this.trip_id = 0;
        this.travel_type = "";
        this.createdBy = "";
        this.user_booking_mode = "";
        this.userRate = 0;
        this.user_city_area = "";
        this.user_vendor = "";
        this.user_source = "";
        this.user_destination = "";
        this.user_travel_date = "";
        this.user_instruction = "";
        this.user_check_in = "";
        this.user_check_out = "";
        this.user_room = 1;
        this.user_total_amount = 0;
        this.trip_status = "";
        this.created_by_id = 0;
        this.created_date = "";
        this.modified_by_id = 0;
        this.modified_date = "";
        this.bill_date = "";
        this.userVendorId = 0;
        this.bill_id = 0;
        this.bill_number = "";
        this.purchase_order = "";
        this.bill_status = "";
        this.destination_of_supply = "";
        this.GST_treatment = "";
        this.gst_num = "";
        this.vendor_name = "";
        this.due_date = "";
        this.currency_code = "";
        this.item_name = "";
        this.account = "";
        this.item_description = "";
        this.quantity = 1;
        this.usage_unit = "";
        this.rate = 0;
        this.tax_name = "";
        this.tax_percentage = "";
        this.tax_amount = 0;
        this.tax_type = "";
        this.adminBookingMode = "";
        adminVendor = "";
        adminVendorId = 0;
        adminSource = "";
        adminDestination = "";
        adminInstruction = "";
        adminCheckIn = "";
        adminCheckOut = "";
        adminRoom = 0;
        adminTotalAmount = 0;
        adminArrival = "";
        adminDeparture = "";
        serviceTaxName = "";
        serviceTaxPercent = "";
        serviceTaxAmount = 0;
        serviceTaxType = "";
        totalAmount = 0;
        bookingAttachment = "";
        this.item_total = 0;
        this.sub_total = 0;
        this.total = 0;
        this.bookingMembers = new ArrayList<>();
    }


    protected TripBooking(Parcel in) {
        id = in.readInt();
        trip_id = in.readInt();
        travel_type = in.readString();
        createdBy = in.readString();
        user_booking_mode = in.readString();
        userRate = in.readDouble();
        user_city_area = in.readString();
        user_vendor = in.readString();
        userVendorId = in.readInt();
        user_source = in.readString();
        user_destination = in.readString();
        user_travel_date = in.readString();
        user_instruction = in.readString();
        user_check_in = in.readString();
        user_check_out = in.readString();
        user_room = in.readInt();
        adminBookingMode = in.readString();
        adminVendor = in.readString();
        adminVendorId = in.readInt();
        adminCityArea = in.readString();
        adminSource = in.readString();
        adminDestination = in.readString();
        adminInstruction = in.readString();
        adminCheckIn = in.readString();
        adminCheckOut = in.readString();
        adminRoom = in.readInt();
        adminTotalAmount = in.readDouble();
        adminArrival = in.readString();
        adminDeparture = in.readString();
        serviceTaxName = in.readString();
        serviceTaxPercent = in.readString();
        serviceTaxAmount = in.readDouble();
        serviceTaxType = in.readString();
        totalAmount = in.readDouble();
        user_total_amount = in.readDouble();
        trip_status = in.readString();
        created_by_id = in.readInt();
        created_date = in.readString();
        modified_by_id = in.readInt();
        modified_date = in.readString();
        bill_date = in.readString();
        bill_id = in.readInt();
        bill_number = in.readString();
        purchase_order = in.readString();
        bill_status = in.readString();
        destination_of_supply = in.readString();
        GST_treatment = in.readString();
        gst_num = in.readString();
        vendor_name = in.readString();
        due_date = in.readString();
        currency_code = in.readString();
        item_name = in.readString();
        account = in.readString();
        item_description = in.readString();
        quantity = in.readInt();
        usage_unit = in.readString();
        rate = in.readDouble();
        tax_name = in.readString();
        tax_percentage = in.readString();
        tax_amount = in.readDouble();
        tax_type = in.readString();
        item_total = in.readDouble();
        sub_total = in.readDouble();
        total = in.readDouble();
        paymentId = in.readInt();
        bookingAttachment = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(trip_id);
        dest.writeString(travel_type);
        dest.writeString(createdBy);
        dest.writeString(user_booking_mode);
        dest.writeDouble(userRate);
        dest.writeString(user_city_area);
        dest.writeString(user_vendor);
        dest.writeInt(userVendorId);
        dest.writeString(user_source);
        dest.writeString(user_destination);
        dest.writeString(user_travel_date);
        dest.writeString(user_instruction);
        dest.writeString(user_check_in);
        dest.writeString(user_check_out);
        dest.writeInt(user_room);
        dest.writeString(adminBookingMode);
        dest.writeString(adminVendor);
        dest.writeInt(adminVendorId);
        dest.writeString(adminCityArea);
        dest.writeString(adminSource);
        dest.writeString(adminDestination);
        dest.writeString(adminInstruction);
        dest.writeString(adminCheckIn);
        dest.writeString(adminCheckOut);
        dest.writeInt(adminRoom);
        dest.writeDouble(adminTotalAmount);
        dest.writeString(adminArrival);
        dest.writeString(adminDeparture);
        dest.writeString(serviceTaxName);
        dest.writeString(serviceTaxPercent);
        dest.writeDouble(serviceTaxAmount);
        dest.writeString(serviceTaxType);
        dest.writeDouble(totalAmount);
        dest.writeDouble(user_total_amount);
        dest.writeString(trip_status);
        dest.writeInt(created_by_id);
        dest.writeString(created_date);
        dest.writeInt(modified_by_id);
        dest.writeString(modified_date);
        dest.writeString(bill_date);
        dest.writeInt(bill_id);
        dest.writeString(bill_number);
        dest.writeString(purchase_order);
        dest.writeString(bill_status);
        dest.writeString(destination_of_supply);
        dest.writeString(GST_treatment);
        dest.writeString(gst_num);
        dest.writeString(vendor_name);
        dest.writeString(due_date);
        dest.writeString(currency_code);
        dest.writeString(item_name);
        dest.writeString(account);
        dest.writeString(item_description);
        dest.writeInt(quantity);
        dest.writeString(usage_unit);
        dest.writeDouble(rate);
        dest.writeString(tax_name);
        dest.writeString(tax_percentage);
        dest.writeDouble(tax_amount);
        dest.writeString(tax_type);
        dest.writeDouble(item_total);
        dest.writeDouble(sub_total);
        dest.writeDouble(total);
        dest.writeInt(paymentId);
        dest.writeString(bookingAttachment);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TripBooking> CREATOR = new Creator<TripBooking>() {
        @Override
        public TripBooking createFromParcel(Parcel in) {
            return new TripBooking(in);
        }

        @Override
        public TripBooking[] newArray(int size) {
            return new TripBooking[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrip_id() {
        return trip_id;
    }

    public void setTrip_id(int trip_id) {
        this.trip_id = trip_id;
    }

    public String getTravel_type() {
        return travel_type;
    }

    public void setTravel_type(String travel_type) {
        this.travel_type = travel_type;
    }

    public String getUser_booking_mode() {
        return user_booking_mode;
    }

    public void setUser_booking_mode(String user_booking_mode) {
        this.user_booking_mode = user_booking_mode;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public double getUserRate() {
        return userRate;
    }

    public void setUserRate(double userRate) {
        this.userRate = userRate;
    }

    public String getUser_city_area() {
        return user_city_area;
    }

    public void setUser_city_area(String user_city_area) {
        this.user_city_area = user_city_area;
    }

    public String getUser_vendor() {
        return user_vendor;
    }

    public void setUser_vendor(String user_vendor) {
        this.user_vendor = user_vendor;
    }

    public int getUserVendorId() {
        return userVendorId;
    }

    public void setUserVendorId(int userVendorId) {
        this.userVendorId = userVendorId;
    }

    public String getUser_source() {
        return user_source;
    }

    public void setUser_source(String user_source) {
        this.user_source = user_source;
    }

    public String getUser_destination() {
        return user_destination;
    }

    public void setUser_destination(String user_destination) {
        this.user_destination = user_destination;
    }

    public String getUser_travel_date() {
        return user_travel_date;
    }

    public void setUser_travel_date(String user_travel_date) {
        this.user_travel_date = user_travel_date;
    }

    public String getUser_instruction() {
        return user_instruction;
    }

    public void setUser_instruction(String user_instruction) {
        this.user_instruction = user_instruction;
    }

    public String getUser_check_in() {
        return user_check_in;
    }

    public void setUser_check_in(String user_check_in) {
        this.user_check_in = user_check_in;
    }

    public String getUser_check_out() {
        return user_check_out;
    }

    public void setUser_check_out(String user_check_out) {
        this.user_check_out = user_check_out;
    }

    public int getUser_room() {
        return user_room;
    }

    public void setUser_room(int user_room) {
        this.user_room = user_room;
    }

    public void setAdminBookingMode(String adminBookingMode) {
        this.adminBookingMode = adminBookingMode;
    }

    public String getAdminVendor() {
        return adminVendor;
    }

    public void setAdminVendor(String adminVendor) {
        this.adminVendor = adminVendor;
    }

    public int getAdminVendorId() {
        return adminVendorId;
    }

    public void setAdminVendorId(int adminVendorId) {
        this.adminVendorId = adminVendorId;
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

    public void setAdminCityArea(String adminCityArea) {
        this.adminCityArea = adminCityArea;
    }

    public void setAdminSource(String adminSource) {
        this.adminSource = adminSource;
    }

    public String getAdminDestination() {
        return adminDestination;
    }

    public void setAdminDestination(String adminDestination) {
        this.adminDestination = adminDestination;
    }

    public String getAdminInstruction() {
        return adminInstruction;
    }

    public void setAdminInstruction(String adminInstruction) {
        this.adminInstruction = adminInstruction;
    }

    public String getAdminCheckIn() {
        return adminCheckIn;
    }

    public void setAdminCheckIn(String adminCheckIn) {
        this.adminCheckIn = adminCheckIn;
    }

    public String getAdminCheckOut() {
        return adminCheckOut;
    }

    public void setAdminCheckOut(String adminCheckOut) {
        this.adminCheckOut = adminCheckOut;
    }

    public int getAdminRoom() {
        return adminRoom;
    }

    public void setAdminRoom(int adminRoom) {
        this.adminRoom = adminRoom;
    }

    public double getAdminTotalAmount() {
        return adminTotalAmount;
    }

    public void setAdminTotalAmount(double adminTotalAmount) {
        this.adminTotalAmount = adminTotalAmount;
    }

    public String getAdminArrival() {
        return adminArrival;
    }

    public void setAdminArrival(String adminArrival) {
        this.adminArrival = adminArrival;
    }

    public String getAdminDeparture() {
        return adminDeparture;
    }

    public void setAdminDeparture(String adminDeparture) {
        this.adminDeparture = adminDeparture;
    }

    public String getServiceTaxName() {
        return serviceTaxName;
    }

    public void setServiceTaxName(String serviceTaxName) {
        this.serviceTaxName = serviceTaxName;
    }

    public String getServiceTaxPercent() {
        return serviceTaxPercent;
    }

    public void setServiceTaxPercent(String serviceTaxPercent) {
        this.serviceTaxPercent = serviceTaxPercent;
    }

    public double getServiceTaxAmount() {
        return serviceTaxAmount;
    }

    public void setServiceTaxAmount(double serviceTaxAmount) {
        this.serviceTaxAmount = serviceTaxAmount;
    }

    public String getServiceTaxType() {
        return serviceTaxType;
    }

    public void setServiceTaxType(String serviceTaxType) {
        this.serviceTaxType = serviceTaxType;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBookingAttachment() {
        return bookingAttachment;
    }

    public void setBookingAttachment(String bookingAttachment) {
        this.bookingAttachment = bookingAttachment;
    }

    public double getUser_total_amount() {
        return user_total_amount;
    }

    public void setUser_total_amount(double user_total_amount) {
        this.user_total_amount = user_total_amount;
    }

    public String getTrip_status() {
        return trip_status;
    }

    public void setTrip_status(String trip_status) {
        this.trip_status = trip_status;
    }

    public int getCreated_by_id() {
        return created_by_id;
    }

    public void setCreated_by_id(int created_by_id) {
        this.created_by_id = created_by_id;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public int getModified_by_id() {
        return modified_by_id;
    }

    public void setModified_by_id(int modified_by_id) {
        this.modified_by_id = modified_by_id;
    }

    public String getModified_date() {
        return modified_date;
    }

    public void setModified_date(String modified_date) {
        this.modified_date = modified_date;
    }

    public String getBill_date() {
        return bill_date;
    }

    public void setBill_date(String bill_date) {
        this.bill_date = bill_date;
    }

    public int getBill_id() {
        return bill_id;
    }

    public void setBill_id(int bill_id) {
        this.bill_id = bill_id;
    }

    public String getBill_number() {
        return bill_number;
    }

    public void setBill_number(String bill_number) {
        this.bill_number = bill_number;
    }

    public String getPurchase_order() {
        return purchase_order;
    }

    public void setPurchase_order(String purchase_order) {
        this.purchase_order = purchase_order;
    }

    public String getBill_status() {
        return bill_status;
    }

    public void setBill_status(String bill_status) {
        this.bill_status = bill_status;
    }

    public String getDestination_of_supply() {
        return destination_of_supply;
    }

    public void setDestination_of_supply(String destination_of_supply) {
        this.destination_of_supply = destination_of_supply;
    }

    public String getGST_treatment() {
        return GST_treatment;
    }

    public void setGST_treatment(String GST_treatment) {
        this.GST_treatment = GST_treatment;
    }

    public String getGst_num() {
        return gst_num;
    }

    public void setGst_num(String gst_num) {
        this.gst_num = gst_num;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUsage_unit() {
        return usage_unit;
    }

    public void setUsage_unit(String usage_unit) {
        this.usage_unit = usage_unit;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getTax_name() {
        return tax_name;
    }

    public void setTax_name(String tax_name) {
        this.tax_name = tax_name;
    }

    public String getTax_percentage() {
        return tax_percentage;
    }

    public void setTax_percentage(String tax_percentage) {
        this.tax_percentage = tax_percentage;
    }

    public double getTax_amount() {
        return tax_amount;
    }

    public void setTax_amount(double tax_amount) {
        this.tax_amount = tax_amount;
    }

    public String getTax_type() {
        return tax_type;
    }

    public void setTax_type(String tax_type) {
        this.tax_type = tax_type;
    }

    public double getItem_total() {
        return item_total;
    }

    public void setItem_total(double item_total) {
        this.item_total = item_total;
    }

    public double getSub_total() {
        return sub_total;
    }

    public void setSub_total(double sub_total) {
        this.sub_total = sub_total;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public ArrayList<TripMember> getBookingMembers() {
        return bookingMembers;
    }

    public void setBookingMembers(ArrayList<TripMember> bookingMembers) {
        this.bookingMembers = bookingMembers;
    }
}
