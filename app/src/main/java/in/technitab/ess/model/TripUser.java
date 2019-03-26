package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TripUser {
    @SerializedName("name")
    private String name;
    @SerializedName("user_tecs")
    private ArrayList<NewTec> tecUserArrayList;

    public TripUser(String name, ArrayList<NewTec> tecUserArrayList) {
        this.name = name;
        this.tecUserArrayList = tecUserArrayList;
    }

    public String getName() {
        return name;
    }

    public ArrayList<NewTec> getTecUserArrayList() {
        return tecUserArrayList;
    }
}
