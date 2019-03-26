package in.technitab.ess.model;

public class SpinnerModel {
    private String frontValue;
    private int backEndValue;

    public SpinnerModel(String frontValue, int backEndValue) {
        this.frontValue = frontValue;
        this.backEndValue = backEndValue;
    }

    public String getFrontValue() {
        return frontValue;
    }

    public int getBackEndValue() {
        return backEndValue;
    }

}
