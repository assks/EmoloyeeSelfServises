package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

public class BookingPayment {
    @SerializedName("id")
    private int id;
    @SerializedName("booking_id")
    private int bookingId;
    @SerializedName("payment_mode")
    private String paymentMode;
    @SerializedName("paid_by")
    private String paidBy;
    @SerializedName("bill_date")
    private String billDate;
    @SerializedName("payment_term")
    private String paymentTerm;
    @SerializedName("payment_term_label")
    private String paymentTermLabel;
    @SerializedName("payment_date")
    private String paymentDate;
    @SerializedName("due_date")
    private String dueDate;
    @SerializedName("paid_amount")
    private double paidAmount;
    @SerializedName("reference_number")
    private String referenceNumber;
    @SerializedName("notes")
    private String notes;
    @SerializedName("created_by_id")
    private int createById;
    @SerializedName("created_date")
    private String createdDate;

    public BookingPayment() {
        this.id = 0;
        this.bookingId = 0;
        this.paymentMode = "";
        this.paidBy = "";
        this.billDate = "";
        this.paymentTerm = "";
        this.paymentTermLabel = "";
        this.paymentDate = "";
        this.dueDate = "";
        this.paidAmount = 0;
        this.referenceNumber = "";
        this.notes = "";
        this.createById = 0;
        this.createdDate = "";
    }

    public BookingPayment(int id, int bookingId, String paymentMode, String paidBy, String billDate, String paymentTerm, String paymentTermLabel, String paymentDate,String dueDate, double paidAmount, String referenceNumber, String notes, int createById, String createdDate) {
        this.id = id;
        this.bookingId = bookingId;
        this.paymentMode = paymentMode;
        this.paidBy = paidBy;
        this.billDate = billDate;
        this.paymentTerm = paymentTerm;
        this.paymentTermLabel = paymentTermLabel;
        this.paymentDate = paymentDate;
        this.dueDate = dueDate;
        this.paidAmount = paidAmount;
        this.referenceNumber = referenceNumber;
        this.notes = notes;
        this.createById = createById;
        this.createdDate = createdDate;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public int getId() {
        return id;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(String paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    public String getPaymentTermLabel() {
        return paymentTermLabel;
    }

    public void setPaymentTermLabel(String paymentTermLabel) {
        this.paymentTermLabel = paymentTermLabel;
    }


    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getCreateById() {
        return createById;
    }

    public void setCreateById(int createById) {
        this.createById = createById;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
