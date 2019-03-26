package in.technitab.ess.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PayVendor implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("user_name")
    private String userName;
    @SerializedName("vendor_id")
    private int vendorId;
    @SerializedName("vendor_name")
    private String vendorName;
    @SerializedName("bank_name")
    private String bankName;
    @SerializedName("bank_address")
    private String bankAddress;
    @SerializedName("ifsc")
    private String ifsc;
    @SerializedName("account_number")
    private String accountNumber;
    @SerializedName("start_stay_date")
    private String startStayDate;
    @SerializedName("end_stay_date")
    private String endStayDate;
    @SerializedName("nights")
    private double nights;
    @SerializedName("rate")
    private double rate;
    @SerializedName("total_amount")
    private double totalAmount;
    @SerializedName("status")
    private String status;
    @SerializedName("created_by_id")
    private int createdById;
    @SerializedName("created_date")
    private String createdDate;

    public PayVendor(int id, String userName, int vendorId, String vendorName, String bankName, String bankAddress, String ifsc, String accountNumber, String startStayDate, String endStayDate, double nights, double rate, double totalAmount, String status, int createdById,String createdDate) {
        this.id = id;
        this.userName = userName;
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.bankName = bankName;
        this.bankAddress = bankAddress;
        this.ifsc = ifsc;
        this.accountNumber = accountNumber;
        this.startStayDate = startStayDate;
        this.endStayDate = endStayDate;
        this.nights = nights;
        this.rate = rate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createdById = createdById;
        this.createdDate = createdDate;
    }

    protected PayVendor(Parcel in) {
        id = in.readInt();
        userName = in.readString();
        vendorId = in.readInt();
        vendorName = in.readString();
        bankName = in.readString();
        bankAddress = in.readString();
        ifsc = in.readString();
        accountNumber = in.readString();
        startStayDate = in.readString();
        endStayDate = in.readString();
        nights = in.readDouble();
        rate = in.readDouble();
        totalAmount = in.readDouble();
        status = in.readString();
        createdById = in.readInt();
        createdDate = in.readString();
    }

    public static final Creator<PayVendor> CREATOR = new Creator<PayVendor>() {
        @Override
        public PayVendor createFromParcel(Parcel in) {
            return new PayVendor(in);
        }

        @Override
        public PayVendor[] newArray(int size) {
            return new PayVendor[size];
        }
    };

    public PayVendor() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(userName);
        parcel.writeInt(vendorId);
        parcel.writeString(vendorName);
        parcel.writeString(bankName);
        parcel.writeString(bankAddress);
        parcel.writeString(ifsc);
        parcel.writeString(accountNumber);
        parcel.writeString(startStayDate);
        parcel.writeString(endStayDate);
        parcel.writeDouble(nights);
        parcel.writeDouble(rate);
        parcel.writeDouble(totalAmount);
        parcel.writeString(status);
        parcel.writeInt(createdById);
        parcel.writeString(createdDate);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setCreatedById(int createdById) {
        this.createdById = createdById;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getId() {
        return id;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getUserName() {
        return userName;
    }

    public int getVendorId() {
        return vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public String getIfsc() {
        return ifsc;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getStartStayDate() {
        return startStayDate;
    }

    public String getEndStayDate() {
        return endStayDate;
    }

    public double getNights() {
        return nights;
    }

    public double getRate() {
        return rate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public int getCreatedById() {
        return createdById;
    }

    public void setStartStayDate(String startStayDate) {
        this.startStayDate = startStayDate;
    }

    public void setEndStayDate(String endStayDate) {
        this.endStayDate = endStayDate;
    }

    public void setNights(double nights) {
        this.nights = nights;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
