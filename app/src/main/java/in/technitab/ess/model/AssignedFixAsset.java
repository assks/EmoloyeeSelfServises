package in.technitab.ess.model;


import com.google.gson.annotations.SerializedName;

public class AssignedFixAsset {
    @SerializedName("id")
    private int id;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("name")
    private String name;
    @SerializedName("assign_date")
    private String assignDate;
    @SerializedName("type_id")
    private int typeId;
    @SerializedName("asset_type")
    private String assetType;
    @SerializedName("description")
    private String description;
    @SerializedName("model")
    private String model;
    @SerializedName("attachment_path")
    private String assetUrl;

    public AssignedFixAsset(int id, int userId, String name, String assignDate, int typeId, String assetType, String description, String model, String assetUrl) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.assignDate = assignDate;
        this.typeId = typeId;
        this.assetType = assetType;
        this.description = description;
        this.model = model;
        this.assetUrl = assetUrl;
    }

    public int getId() {
        return id;
    }

    public String getAssetType() {
        return assetType;
    }

    public String getDescription() {
        return description;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getAssignDate() {
        return assignDate;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getModel() {
        return model;
    }

    public String getAssetUrl() {
        return assetUrl;
    }

}
