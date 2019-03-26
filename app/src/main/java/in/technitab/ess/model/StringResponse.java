package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

public class StringResponse {
    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;

    public StringResponse(boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
