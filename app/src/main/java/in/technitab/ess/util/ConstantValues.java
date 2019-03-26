package in.technitab.ess.util;

import java.util.ArrayList;

import in.technitab.ess.model.BookingMode;

public class ConstantValues {

    public static ArrayList<BookingMode> getPerDiemCity(){
        ArrayList<BookingMode> cities = new ArrayList<>();
        cities.add(new BookingMode("Other","Non metro"));
        cities.add(new BookingMode("Ghaziabad","NCR"));
        cities.add(new BookingMode("Faridabad","NCR"));
        cities.add(new BookingMode("Noida","NCR"));
        cities.add(new BookingMode("Gurgaon","NCR"));
        cities.add(new BookingMode("Delhi","Metro"));
        cities.add(new BookingMode("Mumbai","Metro"));
        cities.add(new BookingMode("Bangalore","Metro"));
        cities.add(new BookingMode("Hyderabad","Metro"));
        cities.add(new BookingMode("Chennai","Metro"));
        cities.add(new BookingMode("Kolkata","Metro"));
        return cities;
    }
}
