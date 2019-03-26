package in.technitab.ess.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TripMember implements Parcelable {
    @SerializedName("id")
    private int tripMemberTblId;
    @SerializedName(value = "member_id", alternate = "user_id")
    private int memberId;
    @SerializedName(value = "memberName", alternate = {"user_name", "name"})
    private String name;
    @SerializedName("is_selected")
    private boolean isSelected;

    public TripMember(int tripMemberTblId, int memberId, String name, boolean isSelected) {
        this.tripMemberTblId = tripMemberTblId;
        this.memberId = memberId;
        this.name = name;
        this.isSelected = isSelected;
    }

    protected TripMember(Parcel in) {
        tripMemberTblId = in.readInt();
        memberId = in.readInt();
        name = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<TripMember> CREATOR = new Creator<TripMember>() {
        @Override
        public TripMember createFromParcel(Parcel in) {
            return new TripMember(in);
        }

        @Override
        public TripMember[] newArray(int size) {
            return new TripMember[size];
        }
    };

    public int getTripMemberTblId() {
        return tripMemberTblId;
    }

    public void setTripMemberTblId(int tripMemberTblId) {
        this.tripMemberTblId = tripMemberTblId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(tripMemberTblId);
        parcel.writeInt(memberId);
        parcel.writeString(name);
        parcel.writeByte((byte) (isSelected ? 1 : 0));
    }
}
