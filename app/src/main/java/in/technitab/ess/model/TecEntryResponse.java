package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TecEntryResponse {
    @SerializedName("category")
    private String category;
    @SerializedName("total_amount")
    private Double totalAmount;
    @SerializedName("emp_amount")
    private double empPaid;
    @SerializedName("ac_amount")
    private double accountPaid;
    @SerializedName("response")
    private ArrayList<NewTecEntry> tecArrayList;

    public TecEntryResponse(String category, Double totalAmount, double empPaid, double accountPaid, ArrayList<NewTecEntry> tecArrayList) {
        this.category = category;
        this.totalAmount = totalAmount;
        this.empPaid = empPaid;
        this.accountPaid = accountPaid;
        this.tecArrayList = tecArrayList;
    }

    public String getCategory() {
        return category;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ArrayList<NewTecEntry> getTecArrayList() {
        return tecArrayList;
    }

    public void setTecArrayList(ArrayList<NewTecEntry> tecArrayList) {
        this.tecArrayList = tecArrayList;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getEmpPaid() {
        return empPaid;
    }

    public void setEmpPaid(double empPaid) {
        this.empPaid = empPaid;
    }

    public double getAccountPaid() {
        return accountPaid;
    }

    public void setAccountPaid(double accountPaid) {
        this.accountPaid = accountPaid;
    }
}
