package in.technitab.ess.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Vendor implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("vendor_type_id")
    private int vendorTypeId;
    @SerializedName("vendor_type")
    private String vendorType;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("contact_name")
    private String contactName;
    @SerializedName("display_name")
    private String displayName;
    @SerializedName("company_name")
    private String companyName;

    @SerializedName("email")
    private String email;
    @SerializedName("contact")
    private String contact;
    @SerializedName("gst_treatment")
    private String gst_treatment;
    @SerializedName("gst_num")
    private String gstNumber;
    @SerializedName("district")
    private String district;
    @SerializedName("place_of_supply")
    private String placeOfSupply;
    @SerializedName("payment_term")
    private String paymentTerm;
    @SerializedName("billing_address")
    private String billingAddress;
    @SerializedName("billing_city")
    private String billingCity;
    @SerializedName("billing_state")
    private String billingState;
    @SerializedName("billing_zipcode")
    private String billingZipCode;
    @SerializedName("billing_country")
    private String billingCountry;
    @SerializedName("billing_phone")
    private String billingPhone;
    @SerializedName("shipping_address")
    private String shippingAddress;
    @SerializedName("shipping_city")
    private String shippingCity;
    @SerializedName("shipping_state")
    private String shippingState;
    @SerializedName("shipping_zipcode")
    private String shippingZipCode;
    @SerializedName("shipping_country")
    private String shippingCountry;
    @SerializedName("shipping_phone")
    private String shippingPhone;
    @SerializedName("pan_number")
    private String panNumber;
    @SerializedName("service_tax_number")
    private String serviceTaxNumber;
    @SerializedName("tax_number")
    private String taxNumber;
    @SerializedName("adhaar_number")
    private String adhaarNumber;
    @SerializedName("bank_name")
    private String bankName;
    @SerializedName("bank_holder_name")
    private String bankHolderName;
    @SerializedName("bank_address")
    private String bankAddress;
    @SerializedName("ifsc")
    private String ifsc;
    @SerializedName("account_number")
    private String account_number;
    @SerializedName("bank_file")
    private String bankFile;
    @SerializedName("id_proof_file")
    private String idProof;
    @SerializedName("created_by_id")
    private int createById;
    @SerializedName("modified_by_id")
    private int modifiedById;
    @SerializedName("voter_id")
    private String voterId;
    @SerializedName("rate")
    private double rate;
    @SerializedName("payment_mode")
    private String paymentMode;
    @SerializedName("bill_file")
    private String billFilePath;
    @SerializedName("bill_number")
    private String billNumber;


    public Vendor() {
        this.firstName = "";
        this.lastName = "";
        this.contactName = "";
        this.displayName = "";
        this.companyName = "";
        this.email = "";
        this.contact = "";
        this.gst_treatment = "";
        this.gstNumber = "";
        this.placeOfSupply = "";
        this.paymentTerm = "";
        this.billingAddress = "";
        this.district = "";
        this.billingCity = "";
        this.billingState = "";
        this.billingZipCode = "";
        this.billingCountry = "";
        this.billingPhone = "";
        this.shippingAddress = "";
        this.shippingCity = "";
        this.shippingState = "";
        this.shippingZipCode = "";
        this.shippingCountry = "";
        this.shippingPhone = "";
        this.bankHolderName = "";
        this.panNumber = "";
        this.serviceTaxNumber = "";
        this.taxNumber = "";
        this.adhaarNumber = "";
        this.bankName = "";
        this.bankAddress = "";
        this.ifsc = "";
        this.account_number = "";
        this.bankFile = "";
        this.idProof = "";
        this.billFilePath = "";
        this.createById = 0;
        modifiedById = 0;
        this.vendorTypeId = 1;
        this.vendorType = "";
        this.voterId = "";
        this.rate = 0;
        this.paymentMode = "Cash";
        this.billNumber = "";
    }

    public Vendor(int id, int vendorTypeId, String vendorType, String firstName, String lastName, String contactName, String displayName, String companyName, String email, String contact, String gst_treatment, String gstNumber, String district, String placeOfSupply, String paymentTerm, String billingAddress, String billingCity, String billingState, String billingZipCode, String billingCountry, String billingPhone, String shippingAddress, String shippingCity, String shippingState, String shippingZipCode, String shippingCountry, String shippingPhone, String panNumber, String serviceTaxNumber, String taxNumber, String adhaarNumber, String bankName, String bankHolderName, String bankAddress, String ifsc, String account_number, String bankFile, String idProof, int createById, String voterId, double rate, String paymentMode, String billFilePath, String billNumber) {
        this.id = id;
        this.vendorTypeId = vendorTypeId;
        this.vendorType = vendorType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactName = contactName;
        this.displayName = displayName;
        this.companyName = companyName;
        this.email = email;
        this.contact = contact;
        this.gst_treatment = gst_treatment;
        this.gstNumber = gstNumber;
        this.district = district;
        this.placeOfSupply = placeOfSupply;
        this.paymentTerm = paymentTerm;
        this.billingAddress = billingAddress;
        this.billingCity = billingCity;
        this.billingState = billingState;
        this.billingZipCode = billingZipCode;
        this.billingCountry = billingCountry;
        this.billingPhone = billingPhone;
        this.shippingAddress = shippingAddress;
        this.shippingCity = shippingCity;
        this.shippingState = shippingState;
        this.shippingZipCode = shippingZipCode;
        this.shippingCountry = shippingCountry;
        this.shippingPhone = shippingPhone;
        this.panNumber = panNumber;
        this.serviceTaxNumber = serviceTaxNumber;
        this.taxNumber = taxNumber;
        this.adhaarNumber = adhaarNumber;
        this.bankName = bankName;
        this.bankHolderName = bankHolderName;
        this.bankAddress = bankAddress;
        this.ifsc = ifsc;
        this.account_number = account_number;
        this.bankFile = bankFile;
        this.idProof = idProof;
        this.createById = createById;
        this.voterId = voterId;
        this.rate = rate;
        this.paymentMode = paymentMode;
        this.billFilePath = billFilePath;
        this.billNumber = billNumber;
    }

    protected Vendor(Parcel in) {
        id = in.readInt();
        vendorTypeId = in.readInt();
        vendorType = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        contactName = in.readString();
        displayName = in.readString();
        companyName = in.readString();
        email = in.readString();
        contact = in.readString();
        gst_treatment = in.readString();
        gstNumber = in.readString();
        district = in.readString();
        placeOfSupply = in.readString();
        paymentTerm = in.readString();
        billingAddress = in.readString();
        billingCity = in.readString();
        billingState = in.readString();
        billingZipCode = in.readString();
        billingCountry = in.readString();
        billingPhone = in.readString();
        shippingAddress = in.readString();
        shippingCity = in.readString();
        shippingState = in.readString();
        shippingZipCode = in.readString();
        shippingCountry = in.readString();
        shippingPhone = in.readString();
        panNumber = in.readString();
        serviceTaxNumber = in.readString();
        taxNumber = in.readString();
        adhaarNumber = in.readString();
        bankName = in.readString();
        bankHolderName = in.readString();
        bankAddress = in.readString();
        ifsc = in.readString();
        account_number = in.readString();
        bankFile = in.readString();
        idProof = in.readString();
        createById = in.readInt();
        modifiedById = in.readInt();
        voterId = in.readString();
        rate = in.readDouble();
        paymentMode = in.readString();
        billFilePath = in.readString();
        billNumber = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(vendorTypeId);
        dest.writeString(vendorType);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(contactName);
        dest.writeString(displayName);
        dest.writeString(companyName);
        dest.writeString(email);
        dest.writeString(contact);
        dest.writeString(gst_treatment);
        dest.writeString(gstNumber);
        dest.writeString(district);
        dest.writeString(placeOfSupply);
        dest.writeString(paymentTerm);
        dest.writeString(billingAddress);
        dest.writeString(billingCity);
        dest.writeString(billingState);
        dest.writeString(billingZipCode);
        dest.writeString(billingCountry);
        dest.writeString(billingPhone);
        dest.writeString(shippingAddress);
        dest.writeString(shippingCity);
        dest.writeString(shippingState);
        dest.writeString(shippingZipCode);
        dest.writeString(shippingCountry);
        dest.writeString(shippingPhone);
        dest.writeString(panNumber);
        dest.writeString(serviceTaxNumber);
        dest.writeString(taxNumber);
        dest.writeString(adhaarNumber);
        dest.writeString(bankName);
        dest.writeString(bankHolderName);
        dest.writeString(bankAddress);
        dest.writeString(ifsc);
        dest.writeString(account_number);
        dest.writeString(bankFile);
        dest.writeString(idProof);
        dest.writeInt(createById);
        dest.writeInt(modifiedById);
        dest.writeString(voterId);
        dest.writeDouble(rate);
        dest.writeString(paymentMode);
        dest.writeString(billFilePath);
        dest.writeString(billNumber);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Vendor> CREATOR = new Creator<Vendor>() {
        @Override
        public Vendor createFromParcel(Parcel in) {
            return new Vendor(in);
        }

        @Override
        public Vendor[] newArray(int size) {
            return new Vendor[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVendorTypeId() {
        return vendorTypeId;
    }

    public void setVendorTypeId(int vendorTypeId) {
        this.vendorTypeId = vendorTypeId;
    }

    public String getVendorType() {
        return vendorType;
    }

    public void setVendorType(String vendorType) {
        this.vendorType = vendorType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setModifiedById(int modifiedById) {
        this.modifiedById = modifiedById;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getGst_treatment() {
        return gst_treatment;
    }

    public void setGst_treatment(String gst_treatment) {
        this.gst_treatment = gst_treatment;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPlaceOfSupply() {
        return placeOfSupply;
    }

    public void setPlaceOfSupply(String placeOfSupply) {
        this.placeOfSupply = placeOfSupply;
    }

    public String getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(String paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getBillingCity() {
        return billingCity;
    }

    public void setBillingCity(String billingCity) {
        this.billingCity = billingCity;
    }

    public String getBillingState() {
        return billingState;
    }

    public void setBillingState(String billingState) {
        this.billingState = billingState;
    }

    public String getBillingZipCode() {
        return billingZipCode;
    }

    public void setBillingZipCode(String billingZipCode) {
        this.billingZipCode = billingZipCode;
    }

    public String getBillingCountry() {
        return billingCountry;
    }

    public void setBillingCountry(String billingCountry) {
        this.billingCountry = billingCountry;
    }

    public String getBillingPhone() {
        return billingPhone;
    }

    public void setBillingPhone(String billingPhone) {
        this.billingPhone = billingPhone;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingCity() {
        return shippingCity;
    }

    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }

    public String getShippingState() {
        return shippingState;
    }

    public void setShippingState(String shippingState) {
        this.shippingState = shippingState;
    }

    public String getShippingZipCode() {
        return shippingZipCode;
    }

    public void setShippingZipCode(String shippingZipCode) {
        this.shippingZipCode = shippingZipCode;
    }

    public String getShippingCountry() {
        return shippingCountry;
    }

    public void setShippingCountry(String shippingCountry) {
        this.shippingCountry = shippingCountry;
    }

    public String getShippingPhone() {
        return shippingPhone;
    }

    public void setShippingPhone(String shippingPhone) {
        this.shippingPhone = shippingPhone;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getServiceTaxNumber() {
        return serviceTaxNumber;
    }

    public void setServiceTaxNumber(String serviceTaxNumber) {
        this.serviceTaxNumber = serviceTaxNumber;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getAdhaarNumber() {
        return adhaarNumber;
    }

    public void setAdhaarNumber(String adhaarNumber) {
        this.adhaarNumber = adhaarNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankHolderName() {
        return bankHolderName;
    }

    public void setBankHolderName(String bankHolderName) {
        this.bankHolderName = bankHolderName;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getBankFile() {
        return bankFile;
    }

    public void setBankFile(String bankFile) {
        this.bankFile = bankFile;
    }

    public String getIdProof() {
        return idProof;
    }

    public void setIdProof(String idProof) {
        this.idProof = idProof;
    }

    public int getCreateById() {
        return createById;
    }

    public void setCreateById(int createById) {
        this.createById = createById;
    }

    public String getVoterId() {
        return voterId;
    }

    public void setVoterId(String voterId) {
        this.voterId = voterId;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getBillFilePath() {
        return billFilePath;
    }

    public void setBillFilePath(String billFilePath) {
        this.billFilePath = billFilePath;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }
}
