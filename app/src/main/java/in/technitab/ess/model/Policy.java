package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

public class Policy {
    @SerializedName("error")
    private boolean error;
    @SerializedName("message")
    private String message;
    @SerializedName("base_url")
    private String baseUrl;
    @SerializedName("file_url")
    private String fileUrl;
    @SerializedName("download_file_url")
    private String downloadFileUrl;

    public Policy(boolean error, String message, String baseUrl, String fileUrl, String downloadFileUrl) {
        this.error = error;
        this.message = message;
        this.baseUrl = baseUrl;
        this.fileUrl = fileUrl;
        this.downloadFileUrl = downloadFileUrl;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getDownloadFileUrl() {
        return downloadFileUrl;
    }
}
