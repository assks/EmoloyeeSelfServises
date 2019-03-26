package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

public class Education {
    @SerializedName("college_type")
    private String collegeType;
    @SerializedName("college")
    private String college;
    @SerializedName("year")
    private String year;
    @SerializedName("board")
    private String board;
    @SerializedName("percentage")
    private String percentage;

    public Education(String collegeType,String college, String year, String board, String percentage) {
        this.college = college;
        this.collegeType = collegeType;
        this.year = year;
        this.board = board;
        this.percentage = percentage;
    }

    public String getCollegeType() {
        return collegeType;
    }

    public String getCollege() {
        return college;
    }

    public String getYear() {
        return year;
    }

    public String getBoard() {
        return board;
    }

    public String getPercentage() {
        return percentage;
    }
}
