package in.technitab.ess.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import in.technitab.ess.util.ConstantVariable;

public class NewTecEntry implements Parcelable {

    @SerializedName(ConstantVariable.Tec.ID)
    private int id;
    @SerializedName(ConstantVariable.Tec.TEC_ID)
    private int tecId;
    @SerializedName(ConstantVariable.Tec.BOOKING_ID)
    private int bookingId;
    @SerializedName(ConstantVariable.Tec.PAYMENT_ID)
    private int paymentId;
    @SerializedName(ConstantVariable.Tec.ENTRY_CATEGORY)
    private String entryCategory;
    @SerializedName(ConstantVariable.Tec.TRAVEL_MODE)
    private String travelMode;
    @SerializedName(ConstantVariable.Tec.DEPARTURE_DATE)
    private String departureDate;
    @SerializedName(ConstantVariable.Tec.DEPARTURE_TIME)
    private String departureTime;
    @SerializedName(ConstantVariable.Tec.ARRIVAL_DATE)
    private String arrivalDate;
    @SerializedName(ConstantVariable.Tec.ARRIVAL_TIME)
    private String arrivalTime;
    @SerializedName(ConstantVariable.Tec.LOCATION)
    private String location;
    @SerializedName(ConstantVariable.Tec.FROM_LOCATION)
    private String fromLocation;
    @SerializedName(ConstantVariable.Tec.TO_LOCATION)
    private String toLocation;
    @SerializedName(ConstantVariable.Tec.KILO_METER)
    private double kiloMeter;
    @SerializedName(ConstantVariable.Tec.MILEAGE)
    private double mileage;
    @SerializedName(ConstantVariable.Tec.UNIT_PRICE)
    private double unitPrice;
    @SerializedName(ConstantVariable.Tec.VENDOR)
    private String vendor;
    @SerializedName(ConstantVariable.Tec.TOTAL_QUANTITTY)
    private double totalQuantitty;
    @SerializedName(ConstantVariable.Tec.NON_WORKING_DAYS)
    private int nonWorkingDays;
    @SerializedName(ConstantVariable.Tec.DESCRIPTION)
    private String description;
    @SerializedName(ConstantVariable.Tec.DATE)
    private String date;
    @SerializedName(ConstantVariable.Tec.PAID_TO)
    private String paidTo;
    @SerializedName(ConstantVariable.Tec.PAID_BY)
    private String paidBy;
    @SerializedName(ConstantVariable.Tec.GSTIN)
    private String gstin;
    @SerializedName(ConstantVariable.Tec.BILL_AMOUNT)
    private double billAmount;
    @SerializedName(ConstantVariable.Tec.BILL_NUM)
    private String billNum;
    @SerializedName(ConstantVariable.Tec.ATTACHMENT_PATH)
    private String attachmentPath;
    @SerializedName(ConstantVariable.Tec.CREATED_BY_ID)
    private int createdById;
    @SerializedName(ConstantVariable.Tec.CREATED_DATE)
    private String createdDate;
    @SerializedName(ConstantVariable.Tec.MODIFIED_BY_ID)
    private int modifiedById;
    @SerializedName(ConstantVariable.Tec.MODIFIED_DATE)
    private String modifiedDate;
    @SerializedName(ConstantVariable.Tec.CITY_VALUE)
    private String cityMetroValue;
    @SerializedName(ConstantVariable.Tec.IS_BILLABLE)
    private String isBillable;
    @SerializedName(ConstantVariable.Tec.PAID_TO_ID)
    private int paidToId;


    public NewTecEntry() {
        this.id = 0;
        this.tecId = 0;
        this.bookingId = 0;
        this.paymentId = 0;
        this.entryCategory = "";
        this.travelMode = "";
        this.departureDate = "";
        this.departureTime = "";
        this.arrivalDate = "";
        this.arrivalTime = "";
        this.location = "";
        this.fromLocation = "";
        this.toLocation = "";
        this.kiloMeter = 0;
        this.mileage = 0;
        this.unitPrice = 0;
        this.vendor = "";
        this.totalQuantitty = 0;
        this.nonWorkingDays = 0;
        this.description = "";
        this.date = "";
        this.paidTo = "";
        this.paidBy = "";
        this.gstin = "";
        this.billAmount = 0;
        this.billNum = "";
        this.attachmentPath = "";
        this.createdById = 0;
        this.createdDate = "";
        this.modifiedById = 0;
        this.modifiedDate = "";
        this.cityMetroValue = "";
        this.isBillable = "";
        this.paidToId = 0;
    }

    public NewTecEntry(int id, int tecId, int bookingId, int paymentId, String entryCategory, String travelMode, String departureDate, String departureTime, String arrivalDate, String arrivalTime, String location, String fromLocation, String toLocation, double kiloMeter, double mileage, double unitPrice, String vendor, double totalQuantitty, int nonWorkingDays, String description, String date, String paidTo, String paidBy, String gstin, double billAmount, String billNum, String attachmentPath, int createdById, String createdDate, int modifiedById, String modifiedDate, String cityMetroValue, String isBillable, int paidToId) {
        this.id = id;
        this.tecId = tecId;
        this.bookingId = bookingId;
        this.paymentId = paymentId;
        this.entryCategory = entryCategory;
        this.travelMode = travelMode;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.arrivalDate = arrivalDate;
        this.arrivalTime = arrivalTime;
        this.location = location;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.kiloMeter = kiloMeter;
        this.mileage = mileage;
        this.unitPrice = unitPrice;
        this.vendor = vendor;
        this.totalQuantitty = totalQuantitty;
        this.nonWorkingDays = nonWorkingDays;
        this.description = description;
        this.date = date;
        this.paidTo = paidTo;
        this.paidBy = paidBy;
        this.gstin = gstin;
        this.billAmount = billAmount;
        this.billNum = billNum;
        this.attachmentPath = attachmentPath;
        this.createdById = createdById;
        this.createdDate = createdDate;
        this.modifiedById = modifiedById;
        this.modifiedDate = modifiedDate;
        this.cityMetroValue = cityMetroValue;
        this.isBillable = isBillable;
        this.paidToId = paidToId;
    }

    protected NewTecEntry(Parcel in) {
        id = in.readInt();
        tecId = in.readInt();
        bookingId = in.readInt();
        paymentId = in.readInt();
        entryCategory = in.readString();
        travelMode = in.readString();
        departureDate = in.readString();
        departureTime = in.readString();
        arrivalDate = in.readString();
        arrivalTime = in.readString();
        location = in.readString();
        fromLocation = in.readString();
        toLocation = in.readString();
        kiloMeter = in.readDouble();
        mileage = in.readDouble();
        unitPrice = in.readDouble();
        vendor = in.readString();
        totalQuantitty = in.readDouble();
        nonWorkingDays = in.readInt();
        description = in.readString();
        date = in.readString();
        paidTo = in.readString();
        paidBy = in.readString();
        gstin = in.readString();
        billAmount = in.readDouble();
        billNum = in.readString();
        attachmentPath = in.readString();
        createdById = in.readInt();
        createdDate = in.readString();
        modifiedById = in.readInt();
        modifiedDate = in.readString();
        cityMetroValue = in.readString();
        isBillable = in.readString();
        paidToId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(tecId);
        dest.writeInt(bookingId);
        dest.writeInt(paymentId);
        dest.writeString(entryCategory);
        dest.writeString(travelMode);
        dest.writeString(departureDate);
        dest.writeString(departureTime);
        dest.writeString(arrivalDate);
        dest.writeString(arrivalTime);
        dest.writeString(location);
        dest.writeString(fromLocation);
        dest.writeString(toLocation);
        dest.writeDouble(kiloMeter);
        dest.writeDouble(mileage);
        dest.writeDouble(unitPrice);
        dest.writeString(vendor);
        dest.writeDouble(totalQuantitty);
        dest.writeInt(nonWorkingDays);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(paidTo);
        dest.writeString(paidBy);
        dest.writeString(gstin);
        dest.writeDouble(billAmount);
        dest.writeString(billNum);
        dest.writeString(attachmentPath);
        dest.writeInt(createdById);
        dest.writeString(createdDate);
        dest.writeInt(modifiedById);
        dest.writeString(modifiedDate);
        dest.writeString(cityMetroValue);
        dest.writeString(isBillable);
        dest.writeInt(paidToId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NewTecEntry> CREATOR = new Creator<NewTecEntry>() {
        @Override
        public NewTecEntry createFromParcel(Parcel in) {
            return new NewTecEntry(in);
        }

        @Override
        public NewTecEntry[] newArray(int size) {
            return new NewTecEntry[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTecId() {
        return tecId;
    }

    public void setTecId(int tecId) {
        this.tecId = tecId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public String getEntryCategory() {
        return entryCategory;
    }

    public void setEntryCategory(String entryCategory) {
        this.entryCategory = entryCategory;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public double getKiloMeter() {
        return kiloMeter;
    }

    public void setKiloMeter(double kiloMeter) {
        this.kiloMeter = kiloMeter;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getTotalQuantitty() {
        return totalQuantitty;
    }

    public void setTotalQuantitty(double totalQuantitty) {
        this.totalQuantitty = totalQuantitty;
    }

    public int getNonWorkingDays() {
        return nonWorkingDays;
    }

    public void setNonWorkingDays(int nonWorkingDays) {
        this.nonWorkingDays = nonWorkingDays;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPaidTo() {
        return paidTo;
    }

    public void setPaidTo(String paidTo) {
        this.paidTo = paidTo;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(double billAmount) {
        this.billAmount = billAmount;
    }

    public String getBillNum() {
        return billNum;
    }

    public void setBillNum(String billNum) {
        this.billNum = billNum;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public int getCreatedById() {
        return createdById;
    }

    public void setCreatedById(int createdById) {
        this.createdById = createdById;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getModifiedById() {
        return modifiedById;
    }

    public void setModifiedById(int modifiedById) {
        this.modifiedById = modifiedById;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getCityMetroValue() {
        return cityMetroValue;
    }

    public void setCityMetroValue(String cityMetroValue) {
        this.cityMetroValue = cityMetroValue;
    }

    public String getIsBillable() {
        return isBillable;
    }

    public void setIsBillable(String isBillable) {
        this.isBillable = isBillable;
    }

    public int getPaidToId() {
        return paidToId;
    }

    public void setPaidToId(int paidToId) {
        this.paidToId = paidToId;
    }
}
