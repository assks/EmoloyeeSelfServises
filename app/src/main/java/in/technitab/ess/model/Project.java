package in.technitab.ess.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Project implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("project_name")
    private String projectName;
    @SerializedName("project_type_id")
    private int projectTypeId;
    @SerializedName("project_type")
    private String projectType;
    @SerializedName("description")
    private String description;
    @SerializedName("billing_type")
    private String billingType;
    @SerializedName("project_cost")
    private double projectCost;
    @SerializedName("customer_name")
    private String customerName;
    @SerializedName("cust_id")
    private int customerId;
    @SerializedName("client_name")
    private String clientName;
    @SerializedName("currency_code")
    private String currencyCode;
    @SerializedName("budget_type")
    private String budgetType;
    @SerializedName("budget_amount")
    private double budgetAmount;
    @SerializedName("project_budget_hours")
    private String projectBudgetHours;
    @SerializedName("estimated_days")
    private int estimatedDays;
    @SerializedName("tl_ts_approver_id")
    private int tlId;
    @SerializedName("location")
    private String location;
    @SerializedName("address")
    private String address;
    @SerializedName("district")
    private String district;
    @SerializedName("state")
    private String state;
    @SerializedName("country")
    private String country;
    @SerializedName("is_international")
    private int isInternational;
    @SerializedName("is_job_allocation_sheet")
    private int isJobActionSheet;
    @SerializedName("is_system_backup")
    private int isSystemBackup;
    @SerializedName("planned_start_date")
    private String plannedStartDate;
    @SerializedName("planned_end_date")
    private String plannedEndDate;
    @SerializedName("created_by_id")
    private int createdById;
    @SerializedName("name")
    private String name;
    @SerializedName("modified_by_id")
    private int modifiedById;


    public Project() {
        this.id = 0;
        this.projectName = "";
        this.projectTypeId = 1;
        this.projectType = "";
        this.description = "";
        this.billingType = "";
        this.projectCost = 0;
        this.customerName = "";
        this.customerId = 0;
        this.clientName = "";
        this.currencyCode = "INR";
        this.budgetType = "";
        this.budgetAmount = 0;
        this.projectBudgetHours = "0";
        this.isInternational = 0;
        this.estimatedDays = 0;
        this.tlId = 0;
        this.location = "";
        this.address = "";
        this.district = "";
        this.state = "";
        this.country = "";
        this.isJobActionSheet = 0;
        this.isSystemBackup = 0;
        this.plannedStartDate = "";
        this.plannedEndDate = "";
        this.createdById = 0;
        this.name = "";
        this.modifiedById = 0;
    }

    public Project(int id, String projectName, int projectTypeId) {
        this.id = id;
        this.projectName = projectName;
        this.projectTypeId = projectTypeId;
    }

    public Project(int id, String projectName, int projectTypeId, String projectType, String description, String billingType, double projectCost, String customerName, int customerId, String clientName, String currencyCode, String budgetType, double budgetAmount, String projectBudgetHours, int estimatedDays, int tlId, String location, String address, String district,String state,String country,int isInternational, int isJobActionSheet, int isSystemBackup, String plannedStartDate, String plannedEndDate, int createdById, String name, int modifiedById) {
        this.id = id;
        this.projectName = projectName;
        this.projectTypeId = projectTypeId;
        this.projectType = projectType;
        this.description = description;
        this.billingType = billingType;
        this.projectCost = projectCost;
        this.customerName = customerName;
        this.customerId = customerId;
        this.clientName = clientName;
        this.currencyCode = currencyCode;
        this.budgetType = budgetType;
        this.budgetAmount = budgetAmount;
        this.projectBudgetHours = projectBudgetHours;
        this.estimatedDays = estimatedDays;
        this.tlId = tlId;
        this.location = location;
        this.address = address;
        this.district = district;
        this.state = state;
        this.country = country;
        this.isInternational = isInternational;
        this.isJobActionSheet = isJobActionSheet;
        this.isSystemBackup = isSystemBackup;
        this.plannedStartDate = plannedStartDate;
        this.plannedEndDate = plannedEndDate;
        this.createdById = createdById;
        this.name = name;
        this.modifiedById = modifiedById;
    }

    protected Project(Parcel in) {
        id = in.readInt();
        projectName = in.readString();
        projectTypeId = in.readInt();
        projectType = in.readString();
        description = in.readString();
        billingType = in.readString();
        projectCost = in.readDouble();
        customerName = in.readString();
        customerId = in.readInt();
        clientName = in.readString();
        currencyCode = in.readString();
        budgetType = in.readString();
        budgetAmount = in.readDouble();
        projectBudgetHours = in.readString();
        estimatedDays = in.readInt();
        tlId = in.readInt();
        location = in.readString();
        address = in.readString();
        district = in.readString();
        state = in.readString();
        country = in.readString();
        isInternational = in.readInt();
        isJobActionSheet = in.readInt();
        isSystemBackup = in.readInt();
        plannedStartDate = in.readString();
        plannedEndDate = in.readString();
        createdById = in.readInt();
        name = in.readString();
        modifiedById = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(projectName);
        dest.writeInt(projectTypeId);
        dest.writeString(projectType);
        dest.writeString(description);
        dest.writeString(billingType);
        dest.writeDouble(projectCost);
        dest.writeString(customerName);
        dest.writeInt(customerId);
        dest.writeString(clientName);
        dest.writeString(currencyCode);
        dest.writeString(budgetType);
        dest.writeDouble(budgetAmount);
        dest.writeString(projectBudgetHours);
        dest.writeInt(estimatedDays);
        dest.writeInt(tlId);
        dest.writeString(location);
        dest.writeString(address);
        dest.writeString(district);
        dest.writeString(state);
        dest.writeString(country);
        dest.writeInt(isInternational);
        dest.writeInt(isJobActionSheet);
        dest.writeInt(isSystemBackup);
        dest.writeString(plannedStartDate);
        dest.writeString(plannedEndDate);
        dest.writeInt(createdById);
        dest.writeString(name);
        dest.writeInt(modifiedById);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getProjectTypeId() {
        return projectTypeId;
    }

    public void setProjectTypeId(int projectTypeId) {
        this.projectTypeId = projectTypeId;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getDescription() {
        return description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getIsInternational() {
        return isInternational;
    }

    public void setIsInternational(int isInternational) {
        this.isInternational = isInternational;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBillingType() {
        return billingType;
    }

    public void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    public double getProjectCost() {
        return projectCost;
    }

    public void setProjectCost(double projectCost) {
        this.projectCost = projectCost;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(String budgetType) {
        this.budgetType = budgetType;
    }

    public double getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(double budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public String getProjectBudgetHours() {
        return projectBudgetHours;
    }

    public void setProjectBudgetHours(String projectBudgetHours) {
        this.projectBudgetHours = projectBudgetHours;
    }

    public int getEstimatedDays() {
        return estimatedDays;
    }

    public void setEstimatedDays(int estimatedDays) {
        this.estimatedDays = estimatedDays;
    }

    public int getTlId() {
        return tlId;
    }

    public void setTlId(int tlId) {
        this.tlId = tlId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getIsJobActionSheet() {
        return isJobActionSheet;
    }

    public void setIsJobActionSheet(int isJobActionSheet) {
        this.isJobActionSheet = isJobActionSheet;
    }

    public int getIsSystemBackup() {
        return isSystemBackup;
    }

    public void setIsSystemBackup(int isSystemBackup) {
        this.isSystemBackup = isSystemBackup;
    }

    public String getPlannedStartDate() {
        return plannedStartDate;
    }

    public void setPlannedStartDate(String plannedStartDate) {
        this.plannedStartDate = plannedStartDate;
    }

    public String getPlannedEndDate() {
        return plannedEndDate;
    }

    public void setPlannedEndDate(String plannedEndDate) {
        this.plannedEndDate = plannedEndDate;
    }

    public int getCreatedById() {
        return createdById;
    }

    public void setCreatedById(int createdById) {
        this.createdById = createdById;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getModifiedById() {
        return modifiedById;
    }

    public void setModifiedById(int modifiedById) {
        this.modifiedById = modifiedById;
    }
}
