package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

public class UserDoc {
    @SerializedName("id")
    private int id;
    @SerializedName("document_name")
    private String documentName;
    @SerializedName("scanned")
    private int scanned;
    @SerializedName("original")
    private int original;
    @SerializedName("file_path")
    private String filePath;

    public UserDoc(int id, String documentName, int scanned, int original, String filePath) {
        this.id = id;
        this.documentName = documentName;
        this.scanned = scanned;
        this.original = original;
        this.filePath = filePath;
    }

    public int getId() {
        return id;
    }

    public String getDocumentName() {
        return documentName;
    }

    public int getScanned() {
        return scanned;
    }

    public int getOriginal() {
        return original;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setScanned(int scanned) {
        this.scanned = scanned;
    }

    public void setOriginal(int original) {
        this.original = original;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
