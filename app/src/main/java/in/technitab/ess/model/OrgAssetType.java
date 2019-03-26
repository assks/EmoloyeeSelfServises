package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

public class OrgAssetType {
    @SerializedName("id")
    private int id;
    @SerializedName("fix_asset")
    private String fixAsset;
    @SerializedName("description")
    private String description;
    @SerializedName("image_path")
    private String imagePath;

    public OrgAssetType(int id, String fixAsset, String description, String imagePath) {
        this.id = id;
        this.fixAsset = fixAsset;
        this.description = description;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public String getFixAsset() {
        return fixAsset;
    }

    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }
}
