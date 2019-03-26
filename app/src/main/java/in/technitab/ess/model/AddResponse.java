package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

public class AddResponse {

    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;
    @SerializedName("id")
    private int id;
    @SerializedName(value = "tec_id", alternate = {"activity_user_id"})
    private int tecId;

    public AddResponse(boolean error, String message, int id,int tecId) {
        this.error = error;
        this.message = message;
        this.id  = id;
        this.tecId = tecId;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public int getTecId() {
        return tecId;
    }
}
