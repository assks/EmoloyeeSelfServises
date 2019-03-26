package in.technitab.ess.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ParticularLeave implements Parcelable {

    @SerializedName("date")
    private String date;
    @SerializedName("length_hours")
    private String lengthHours;
    @SerializedName("length_days")
    private double lengthDays;
    @SerializedName("duration_type")
    private  int durationType;
    @SerializedName("comments")
    private String comments;


    public ParticularLeave(String date, String lengthHours, double lengthDays, int durationType, String comments) {
        this.date = date;
        this.lengthHours = lengthHours;
        this.lengthDays = lengthDays;
        this.durationType = durationType;
        this.comments = comments;
    }


    public String getDate() {
        return date;
    }

    public String getLengthHours() {
        return lengthHours;
    }

    public double getLengthDays() {
        return lengthDays;
    }

    public int getDurationType() {
        return durationType;
    }

    public String getComments() {
        return comments;
    }

    protected ParticularLeave(Parcel in) {
        date = in.readString();
        lengthHours = in.readString();
        lengthDays = in.readDouble();
        durationType = in.readInt();
        comments = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(lengthHours);
        dest.writeDouble(lengthDays);
        dest.writeInt(durationType);
        dest.writeString(comments);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParticularLeave> CREATOR = new Creator<ParticularLeave>() {
        @Override
        public ParticularLeave createFromParcel(Parcel in) {
            return new ParticularLeave(in);
        }

        @Override
        public ParticularLeave[] newArray(int size) {
            return new ParticularLeave[size];
        }
    };
}
