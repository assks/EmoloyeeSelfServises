package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import in.technitab.ess.util.ConstantVariable;

public class ApproveLeave {
    @SerializedName(ConstantVariable.UserPrefVar.USER_ID)
    private String userId;
    @SerializedName(ConstantVariable.UserPrefVar.NAME)
    private String name;
    @SerializedName("leave_response")
    private ArrayList<MyLeaves> myLeavesArrayList;


    public ApproveLeave(String userId, String name, ArrayList<MyLeaves> myLeavesArrayList) {
        this.userId = userId;
        this.name = name;
        this.myLeavesArrayList = myLeavesArrayList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<MyLeaves> getMyLeavesArrayList() {
        return myLeavesArrayList;
    }

    public void setMyLeavesArrayList(ArrayList<MyLeaves> myLeavesArrayList) {
        this.myLeavesArrayList = myLeavesArrayList;
    }
}
