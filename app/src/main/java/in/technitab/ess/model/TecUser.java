package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

public class TecUser {
    @SerializedName("tec_id")
    private int tecId;
    @SerializedName("claim_start_date")
    private String claimStartDate;
    @SerializedName("claim_end_date")
    private String claimEndDate;

    public TecUser(int tecId, String claimStartDate, String claimEndDate) {
        this.tecId = tecId;
        this.claimStartDate = claimStartDate;
        this.claimEndDate = claimEndDate;
    }

    public int getTecId() {
        return tecId;
    }

    public String getClaimStartDate() {
        return claimStartDate;
    }

    public String getClaimEndDate() {
        return claimEndDate;
    }
}
