package in.technitab.ess.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

import in.technitab.ess.util.ConstantVariable;

public class Trip implements Parcelable {
    @SerializedName(ConstantVariable.Trip.TRIP_ID)
    private int id;
    @SerializedName(ConstantVariable.Trip.PROJECT_ID)
    private int projectId;
    @SerializedName(ConstantVariable.Trip.PROJECT_NAME)
    private String projectName;
    @SerializedName(ConstantVariable.Trip.SOURCE)
    private String source;
    @SerializedName(ConstantVariable.Trip.DESTINATION)
    private String destination;
    @SerializedName(ConstantVariable.Trip.START_DATE)
    private String startDate;
    @SerializedName(ConstantVariable.Trip.END_DATE)
    private String endDate;
    @SerializedName(ConstantVariable.Trip.MEMBER_JSON)
    private ArrayList<TripMember> memberJson;
    @SerializedName(ConstantVariable.Trip.STATUS)
    private String status;
    @SerializedName(ConstantVariable.Trip.REMARK)
    private String remark;
    @SerializedName(ConstantVariable.Trip.COMMENT)
    private String comment;
    @SerializedName(ConstantVariable.Trip.CREATED_DATE)
    private String createdDate;
    @SerializedName(ConstantVariable.Trip.CREATED_BY_ID)
    private int createdById;
    @SerializedName(ConstantVariable.Trip.CREATED_BY)
    private String createdBy;

    public Trip() {
    }

    public Trip(int id, int projectId, String projectName, String source, String destination, String startDate,String endDate, ArrayList<TripMember> memberJson, String status, String remark, String comment, String createdDate, int createdById, String createdBy) {
        this.id = id;
        this.projectId = projectId;
        this.projectName = projectName;
        this.source = source;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.memberJson = memberJson;
        this.status = status;
        this.remark = remark;
        this.comment = comment;
        this.createdDate = createdDate;
        this.createdById = createdById;
        this.createdBy = createdBy;
    }

    protected Trip(Parcel in) {
        id = in.readInt();
        projectId = in.readInt();
        projectName = in.readString();
        source = in.readString();
        destination = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        status = in.readString();
        remark = in.readString();
        comment = in.readString();
        createdDate = in.readString();
        createdById = in.readInt();
        createdBy = in.readString();
    }

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public ArrayList<TripMember> getMemberJson(){
        return memberJson;
    }

    public void setMemberList(ArrayList<TripMember> memberJson) {
        this.memberJson = memberJson;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getCreatedById() {
        return createdById;
    }

    public void setCreatedById(int createdById) {
        this.createdById = createdById;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeInt(id);
        parcel.writeInt(projectId);
        parcel.writeString(projectName);
        parcel.writeString(source);
        parcel.writeString(destination);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
        parcel.writeString(status);
        parcel.writeString(remark);
        parcel.writeString(comment);
        parcel.writeString(createdDate);
        parcel.writeInt(createdById);
        parcel.writeString(createdBy);
    }


}
