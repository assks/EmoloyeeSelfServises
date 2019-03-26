package in.technitab.ess.model;

public class BookingMode {
    private String title;
    private String value;
    private String type;
    private String itcEligibility;

    public BookingMode(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public BookingMode(String title,String value,String type,String itcEligibility){
        this.title = title;
        this.value = value;
        this.type = type;
        this.itcEligibility = itcEligibility;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public String getItcEligibility() {
        return itcEligibility;
    }
}
