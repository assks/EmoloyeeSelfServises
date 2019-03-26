package in.technitab.ess.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.adapter.SpinAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.model.BookingMode;
import in.technitab.ess.model.StringResponse;
import in.technitab.ess.model.Trip;
import in.technitab.ess.model.TripBooking;
import in.technitab.ess.model.TripMember;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.CustomDate;
import in.technitab.ess.util.CustomEditText;
import in.technitab.ess.util.DateCal;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.technitab.ess.util.ConstantVariable.Value.getTripBookingMode;

public class TripRequestActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.hotel_users_layout)
    LinearLayout hotelUsersLayout;
    @BindView(R.id.travelTypeSpinner)
    Spinner travelTypeSpinner;
    @BindView(R.id.travelModeSpinner)
    Spinner travelModeSpinner;
    @BindView(R.id.hotel_city_area)
    EditText hotelCityArea;
    @BindView(R.id.checkIn)
    CustomEditText checkIn;
    @BindView(R.id.checkOut)
    CustomEditText checkOut;
    @BindView(R.id.room)
    CustomEditText room;
    @BindView(R.id.hotelNights)
    TextView hotelNights;
    @BindView(R.id.hotel_vendor)
    CustomEditText hotelVendor;
    @BindView(R.id.rate)
    CustomEditText rate;
    @BindView(R.id.hotel_amount)
    CustomEditText hotelAmount;
    @BindView(R.id.hotel_instruction)
    EditText hotelInstruction;
    @BindView(R.id.hotelLayout)
    LinearLayout hotelLayout;
    @BindView(R.id.from)
    EditText from;
    @BindView(R.id.to)
    EditText to;
    @BindView(R.id.bus_date)
    CustomEditText busDate;
    @BindView(R.id.vendor)
    EditText vendor;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.busTrainFlightLayout)
    LinearLayout busTrainFlightLayout;

    private ArrayList<Object> mTravelModeList;
    private String strSelectedTravel = "";
    private UserPref userPref;
    private Dialog dialog;
    private NetConnection connection;
    private RestApi api;
    private Resources resources;
    private Activity activity;
    private Trip trip;
    private TripBooking tripBooking;
    private ArrayList<TripMember> memberArrayList;
    private int RC_VENDOR = 1, RC_HOTEL = 2;
    private int vendorId = 0;
    private double vendorHotelNightRate = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_request);
        ButterKnife.bind(this);

        init();
        setToolbar();
        setSpinner();

        init();
        setSpinner();
        new CustomDate(checkIn, activity, trip.getStartDate(), null);
        new CustomDate(busDate,activity,trip.getStartDate(),null);
        checkIn.addTextChangedListener(new MyWatcher(checkIn));
        checkOut.addTextChangedListener(new MyWatcher(checkOut));
        room.addTextChangedListener(new MyWatcher(room));
        rate.addTextChangedListener(new MyWatcher(rate));


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            trip = bundle.getParcelable(resources.getString(R.string.trip));
            memberArrayList = bundle.getParcelableArrayList(ConstantVariable.LIST);
            showMemberList(memberArrayList);
        }
    }

    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(resources.getString(R.string.booking_request));
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void init() {
        activity = this;
        mTravelModeList = new ArrayList<>();
        userPref = new UserPref(activity);
        connection = new NetConnection();
        dialog = new Dialog(activity);
        api = APIClient.getClient().create(RestApi.class);
        resources = getResources();
        memberArrayList = new ArrayList<>();
        tripBooking = new TripBooking();
        trip = new Trip();
    }

    private void setSpinner() {
        List<String> mTravelTypeList = Arrays.asList(getResources().getStringArray(R.array.travelTypeArray));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, android.R.layout.simple_list_item_1, mTravelTypeList);
        travelTypeSpinner.setAdapter(adapter);
        travelTypeSpinner.setOnItemSelectedListener(this);

        mTravelModeList.addAll(getTripBookingMode());
        SpinAdapter suggestionAdapter = new SpinAdapter(this, android.R.layout.simple_list_item_1, mTravelModeList);
        travelModeSpinner.setAdapter(suggestionAdapter);
        travelModeSpinner.setOnItemSelectedListener(this);
    }


    @OnClick({R.id.hotel_vendor, R.id.vendor})
    protected void onPickVendor(View view) {
        String strBookingMode = "", title = "";
        int RC;

        switch (view.getId()) {
            case R.id.vendor:
                RC = RC_VENDOR;
                break;

            default:
                RC = RC_HOTEL;
        }

        if (travelModeSpinner.getSelectedItem() instanceof BookingMode) {
            BookingMode bookingMode = (BookingMode) travelModeSpinner.getSelectedItem();
            strBookingMode = bookingMode.getValue();
            title = bookingMode.getTitle();
        }

        Intent intent = new Intent(this, BookingVendorActivity.class);
        intent.putExtra(ConstantVariable.Project.ID, trip.getProjectId());
        intent.putExtra(ConstantVariable.Booking.BOOKING_MODE, strBookingMode);
        intent.putExtra(ConstantVariable.Booking.TRAVEL_TYPE, travelTypeSpinner.getSelectedItem().toString());
        intent.putExtra(ConstantVariable.MIX_ID.TITLE, title);
        startActivityForResult(intent, RC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String vendorName ;
        if (requestCode == RC_HOTEL && resultCode == Activity.RESULT_OK && data != null) {

            vendorName = data.getStringExtra(ConstantVariable.Vendor.DISPLAY_NAME);

            vendorHotelNightRate = data.getDoubleExtra(ConstantVariable.Vendor.RATE, 0);
            vendorId = data.getIntExtra(ConstantVariable.MIX_ID.ID,0);
            hotelVendor.setText(vendorName);
            tripBooking.setUserVendorId(data.getIntExtra(ConstantVariable.MIX_ID.ID,0));
            rate.setText(String.valueOf(vendorHotelNightRate));
            calculateNights();

        } else if (requestCode == RC_VENDOR && resultCode == Activity.RESULT_OK && data != null) {
            vendorName = data.getStringExtra(ConstantVariable.Vendor.DISPLAY_NAME);
            vendorId = data.getIntExtra(ConstantVariable.MIX_ID.ID,0);
            vendor.setText(vendorName);
        }
    }

    private void showMemberList(ArrayList<TripMember> list) {
        if (hotelUsersLayout.getChildCount() > 0) {
            hotelUsersLayout.removeAllViews();
        }

        for (TripMember user : list) {
            hotelUsersLayout.setVisibility(View.VISIBLE);
            View view = LayoutInflater.from(activity).inflate(R.layout.layout_trip_member, null);
            TextView checkedTextView = view.findViewById(R.id.name);
            if (checkedTextView.getParent() != null) {
                ((ViewGroup) checkedTextView.getParent()).removeView(checkedTextView);
            }
            checkedTextView.setText(user.getMemberId() == Integer.parseInt(userPref.getUserId()) ? "Self" : user.getName());
            hotelUsersLayout.addView(checkedTextView);
        }
    }

    @OnClick(R.id.submit)
    protected void onSubmit() {
        String strTravelType = travelTypeSpinner.getSelectedItem().toString();
        tripBooking.setTravel_type(strTravelType);
        tripBooking.setTrip_id(trip.getId());
        tripBooking.setUser_booking_mode(strSelectedTravel);
        if (strSelectedTravel.equalsIgnoreCase(resources.getString(R.string.hotel_booking_mode)) || strSelectedTravel.equalsIgnoreCase(resources.getString(R.string.booking_guesthouse))) {
            tripBooking.setUser_city_area(hotelCityArea.getText().toString().trim());
            tripBooking.setUser_check_in(checkIn.getText().toString().trim());
            tripBooking.setUser_check_out(checkOut.getText().toString().trim());
            String strRoom = room.getText().toString().trim();
            tripBooking.setUser_room(TextUtils.isEmpty(strRoom) ? 0 : Integer.parseInt(strRoom));
            tripBooking.setUser_vendor(hotelVendor.getText().toString().trim());
            tripBooking.setUserVendorId(vendorId);
            String strNightRate = rate.getText().toString().trim();
            tripBooking.setRate(TextUtils.isEmpty(strNightRate) ? 0 : Double.parseDouble(strNightRate));
            String strAmount = hotelAmount.getText().toString().trim();
            tripBooking.setUser_total_amount(TextUtils.isEmpty(strAmount)?0:Double.parseDouble(strAmount));
            tripBooking.setUser_instruction(hotelInstruction.getText().toString().trim());
        }else if(strSelectedTravel.equalsIgnoreCase(resources.getString(R.string.booking_train)) || strSelectedTravel.equalsIgnoreCase(resources.getString(R.string.booking_bus)) || strSelectedTravel.equalsIgnoreCase(resources.getString(R.string.booking_flight))){
            tripBooking.setUser_source(from.getText().toString().trim());
            tripBooking.setUser_destination(to.getText().toString().trim());
            tripBooking.setUser_travel_date(busDate.getText().toString().trim());
            tripBooking.setUser_instruction(description.getText().toString().trim());
            tripBooking.setUserVendorId(vendorId);
            tripBooking.setUser_vendor(vendor.getText().toString().trim());
        }

        tripBooking.setBookingMembers(memberArrayList);


        if (invalidate()){
            if (connection.isNetworkAvailable(this)){
                dialog.showDialog();
                tripBooking.setCreated_by_id(Integer.parseInt(userPref.getUserId()));
                Gson gson = new Gson();
                String json = gson.toJson(tripBooking);
                Call<StringResponse> bookingRequestApi = api.tripRequestBooking(json,"booking_request");
                bookingRequestApi.enqueue(new Callback<StringResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                        dialog.dismissDialog();
                        if (response.isSuccessful()){
                            StringResponse stringResponse = response.body();
                            if (stringResponse != null){
                                showMessage(stringResponse.getMessage());
                                startTripActivity();
                            }
                        }else{
                            showMessage(resources.getString(R.string.problem_to_connect));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<StringResponse> call, @NonNull Throwable t) {
                        dialog.dismissDialog();
                        if (t instanceof SocketTimeoutException) {
                            showMessage(resources.getString(R.string.slow_internet_connection));
                        }
                    }
                });
            }
        }
    }

    private void startTripActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setResult(Activity.RESULT_OK);
                finish();
            }
        },500);
    }

    private boolean invalidate() {
        boolean valid = true;
        if (strSelectedTravel.equalsIgnoreCase(resources.getString(R.string.hotel_booking_mode))){
            if (TextUtils.isEmpty(tripBooking.getUser_city_area())){
                showMessage("City/Area is required");
                valid = false;
            }else if (TextUtils.isEmpty(tripBooking.getUser_check_in())){
                showMessage("Check-in is required");
                valid = false;
            }else if (TextUtils.isEmpty(tripBooking.getUser_check_out())){
                showMessage("Check-out is required");
                valid = false;
            }else if (tripBooking.getUser_room() <=0){
                showMessage("Room can not be zero");
                valid = false;
            }
        }else if (strSelectedTravel.equalsIgnoreCase(resources.getString(R.string.booking_train)) || strSelectedTravel.equalsIgnoreCase(resources.getString(R.string.booking_flight)) || strSelectedTravel.equalsIgnoreCase(resources.getString(R.string.booking_bus))){
            if (TextUtils.isEmpty(tripBooking.getUser_source())){
                showMessage("From location is required");
                valid = false;
            }else if (TextUtils.isEmpty(tripBooking.getUser_destination())){
                showMessage("To location is required");
                valid = false;
            }else if (TextUtils.isEmpty(tripBooking.getUser_travel_date())){
                showMessage("Reporting date is required");
                valid = false;
            }
        }
        return valid;
    }

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (view != null) {
            view.setPadding(2, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
            ((TextView) view).setTextColor(Color.BLACK);
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.mediumTextSize));

            switch (adapterView.getId()) {

                case R.id.travelModeSpinner:

                    if (mTravelModeList.get(position) instanceof BookingMode) {
                        strSelectedTravel = ((BookingMode) mTravelModeList.get(position)).getTitle();

                        switch (strSelectedTravel) {
                            case "Hotel/PG/Lodge":
                                hotelLayout.setVisibility(View.VISIBLE);
                                busTrainFlightLayout.setVisibility(View.GONE);
                                break;

                            case "Guesthouse":
                                hotelLayout.setVisibility(View.VISIBLE);
                                busTrainFlightLayout.setVisibility(View.GONE);
                                room.setText(resources.getString(R.string.single_room));
                                room.setFocusable(false);
                                hotelVendor.setText(resources.getString(R.string.guesthouse_vendor));
                                rate.setText(resources.getString(R.string.guesthouse_per_night_rate));
                                rate.setFocusable(false);
                                break;

                            case "Bus":
                                hotelLayout.setVisibility(View.GONE);
                                busTrainFlightLayout.setVisibility(View.VISIBLE);
                                break;

                            case "Train":
                                hotelLayout.setVisibility(View.GONE);
                                busTrainFlightLayout.setVisibility(View.VISIBLE);
                                break;

                            case "Flight":
                                hotelLayout.setVisibility(View.GONE);
                                busTrainFlightLayout.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                    break;

            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private class MyWatcher implements TextWatcher {

        private View view;

        private MyWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            switch (view.getId()) {
                case R.id.checkIn:
                    String date = checkIn.getText().toString().trim();
                    if (!date.isEmpty())
                        new CustomDate(checkOut, activity, date, null);
                    break;

                case R.id.checkOut:
                    String strCheckOut = checkOut.getText().toString().trim();
                    if (!strCheckOut.isEmpty())
                        break;

                case R.id.room:
                    calculateNights();
                    break;

                case R.id.rate:
            }
            calculateTotalAmount();

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private void calculateTotalAmount(){
        String strHotelFirst = checkIn.getText().toString().trim();
        String strHotelLast = checkOut.getText().toString().trim();
        String strRate = rate.getText().toString().trim();
        String strRoom = room.getText().toString().trim();
        double vendorHotelNightRate = TextUtils.isEmpty(strRate)?0:Double.parseDouble(strRate);
        if (!TextUtils.isEmpty(strHotelFirst) && !TextUtils.isEmpty(strHotelLast) && !TextUtils.isEmpty(strRoom) && TextUtils.isDigitsOnly(strRoom)) {
            int days = DateCal.getDays(strHotelFirst, strHotelLast);
            double totalNightsRate = vendorHotelNightRate * days;
            Double calRate = totalNightsRate * (Integer.parseInt(strRoom));
            hotelAmount.setText(String.valueOf(calRate));
        }
    }
    private void calculateNights() {
        String strRoom = room.getText().toString().trim();
        if (!TextUtils.isEmpty(strRoom) && TextUtils.isDigitsOnly(strRoom)) {
            double totalNightsRate = vendorHotelNightRate* (Integer.parseInt(strRoom));
            rate.setText(String.valueOf(totalNightsRate));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
