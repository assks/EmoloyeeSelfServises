package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

public class Customer {
    @SerializedName("id")
    private int id;
    @SerializedName("customer_name")
    private String customerName;

    public Customer(int id, String customerName) {
        this.id = id;
        this.customerName = customerName;

    }

    public int getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

}
