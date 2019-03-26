package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

public class AssignProject {
    @SerializedName("project_id")
    private int projectId;
    @SerializedName("project_name")
    private String projectName;
    @SerializedName("customer_name")
    private String customerName;
    @SerializedName("total_timesheet_duration")
    private String totalTimesheetDuration;
    @SerializedName("total_billable_duration")
    private String totalBillableDuration;
    @SerializedName("tec_claim_expense")
    private double tecClaimExpense;
    @SerializedName("booking_expense")
    private double bookingExpense;
    @SerializedName("project_profile")
    private String projectProfit;


    public AssignProject(int projectId, String projectName, String customerName, String totalTimesheetDuration, String totalBillableDuration, double tecClaimExpense, double bookingExpense, String projectProfit) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.customerName = customerName;
        this.totalTimesheetDuration = totalTimesheetDuration;
        this.totalBillableDuration = totalBillableDuration;
        this.tecClaimExpense = tecClaimExpense;
        this.bookingExpense = bookingExpense;
        this.projectProfit = projectProfit;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getTotalTimesheetDuration() {
        return totalTimesheetDuration;
    }

    public String getTotalBillableDuration() {
        return totalBillableDuration;
    }

    public double getTecClaimExpense() {
        return tecClaimExpense;
    }

    public double getBookingExpense() {
        return bookingExpense;
    }

    public String getProjectProfit() {
        return projectProfit;
    }

}
