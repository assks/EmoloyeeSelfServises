package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName(value = "id", alternate = "user_id")
    private int id;
    @SerializedName(value = "name", alternate = "user_name")
    private String name;
    @SerializedName("is_selected")
    private boolean isSelected;

    public User(int id, String name, boolean is_selected) {
        this.id = id;
        this.name = name;
        this.isSelected = is_selected;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
