package in.technitab.ess.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.adapter.SpinAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.model.AddResponse;
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
import in.technitab.ess.util.SetTime;
import in.technitab.ess.util.UserPref;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.technitab.ess.util.ConstantVariable.Value.getGSTTaxPercent;
import static in.technitab.ess.util.ConstantVariable.Value.getIGSTTaxPercent;
import static in.technitab.ess.util.ConstantVariable.Value.getTripBookingMode;

public class TravelBookingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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
    @BindView(R.id.hotelTaxPercent)
    Spinner hotelTaxPercent;
    @BindView(R.id.hotel_tax_amount)
    CustomEditText hotelTaxAmount;
    @BindView(R.id.service_charge)
    TextView serviceCharge;
    @BindView(R.id.serviceTaxPercent)
    Spinner serviceTaxPercent;
    @BindView(R.id.serviceChargeSubAmount)
    CustomEditText serviceChargeSubAmount;
    @BindView(R.id.serviceChargeLayout)
    LinearLayout serviceChargeLayout;
    @BindView(R.id.hotelBillTotalAmount)
    TextView hotelBillTotalAmount;
    @BindView(R.id.hotelTotalItemAmountLayout)
    RelativeLayout hotelTotalItemAmountLayout;
    @BindView(R.id.hotelLayout)
    LinearLayout hotelLayout;
    @BindView(R.id.from)
    EditText from;
    @BindView(R.id.to)
    EditText to;
    @BindView(R.id.departureDate)
    EditText departureDate;
    @BindView(R.id.departureTime)
    EditText departureTime;
    @BindView(R.id.arrivalDate)
    EditText arrivalDate;
    @BindView(R.id.arrivalTime)
    EditText arrivalTime;
    @BindView(R.id.busVendor)
    CustomEditText busVendor;
    @BindView(R.id.busAmount)
    CustomEditText busAmount;
    @BindView(R.id.bookingTaxPercent)
    Spinner bookingTaxPercent;
    @BindView(R.id.bookingTaxAmount)
    CustomEditText bookingTaxAmount;
    @BindView(R.id.busServiceCharge)
    TextView busServiceCharge;
    @BindView(R.id.busServiceTaxPercent)
    Spinner busServiceTaxPercent;
    @BindView(R.id.busServiceChargeSubAmount)
    CustomEditText busServiceChargeSubAmount;
    @BindView(R.id.busServiceChargeLayout)
    LinearLayout busServiceChargeLayout;
    @BindView(R.id.attachment)
    TextView attachment;
    @BindView(R.id.billTotalAmount)
    TextView billTotalAmount;
    @BindView(R.id.busTotalItemAmountLayout)
    RelativeLayout busTotalItemAmountLayout;
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
    private int RC_VENDOR = 1;
    private int RC_HOTEL = 2;
    private int RC_PAYMENT = 6;
    private String vendorDistrict = "";
    private String actionBy = "";
    private int vendorId = 0;
    private double vendorHotelNightRate = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_booking);
        ButterKnife.bind(this);

        init();
        setToolbar();
        setSpinner();
        new CustomDate(checkIn, activity, trip.getStartDate(), null);
        new CustomDate(checkIn, activity, trip.getStartDate(), null);
        new CustomDate(departureDate, activity, trip.getStartDate(), null);
        new CustomDate(arrivalDate, activity, trip.getStartDate(), null);
        new SetTime(departureTime, activity);
        new SetTime(arrivalTime, activity);

        room.addTextChangedListener(new MyWatcher(room));
        rate.addTextChangedListener(new MyWatcher(rate));
        checkIn.addTextChangedListener(new MyWatcher(checkIn));
        checkOut.addTextChangedListener(new MyWatcher(checkOut));
        busAmount.addTextChangedListener(new MyWatcher(busAmount));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            trip = bundle.getParcelable(resources.getString(R.string.trip));
            actionBy = bundle.getString(ConstantVariable.MIX_ID.ACTION);
            memberArrayList = bundle.getParcelableArrayList(ConstantVariable.LIST);
            showMemberList(memberArrayList);
        }
    }

    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(resources.getString(R.string.travel_booking));
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
        trip = new Trip();
        tripBooking = new TripBooking();
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


    @OnClick({R.id.hotel_vendor, R.id.busVendor})
    protected void onPickVendor(View view) {
        String strBookingMode = "", title = "";
        int RC;

        switch (view.getId()) {
            case R.id.busVendor:
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
        String vendorName;
        if (requestCode == RC_HOTEL && resultCode == Activity.RESULT_OK && data != null) {

            vendorName = data.getStringExtra(ConstantVariable.Vendor.DISPLAY_NAME);
            vendorDistrict = data.getStringExtra(ConstantVariable.Vendor.DISTRICT);
            vendorHotelNightRate = data.getDoubleExtra(ConstantVariable.Vendor.RATE, 0);
            vendorId = data.getIntExtra(ConstantVariable.MIX_ID.ID, 0);
            hotelVendor.setText(vendorName);
            rate.setText(String.valueOf(vendorHotelNightRate));
            setTaxSpinner();

        } else if (requestCode == RC_VENDOR && resultCode == Activity.RESULT_OK && data != null) {
            vendorName = data.getStringExtra(ConstantVariable.Vendor.DISPLAY_NAME);
            vendorDistrict = data.getStringExtra(ConstantVariable.Vendor.DISTRICT);
            vendorId = data.getIntExtra(ConstantVariable.MIX_ID.ID, 0);
            busVendor.setText(vendorName);

            setTaxSpinner();
        } else if (requestCode == RC_PAYMENT){
            startHomeActivity();
        }
    }



    private void setTaxSpinner() {
        ArrayList<Object> mTaxPercentArrays = new ArrayList<>();
        if (TextUtils.equals(vendorDistrict, "DL")) {
            mTaxPercentArrays.addAll(getGSTTaxPercent());
        } else {
            mTaxPercentArrays.addAll(getIGSTTaxPercent());
        }

        SpinAdapter suggestionAdapter = new SpinAdapter(this, android.R.layout.simple_list_item_1, mTaxPercentArrays);
        hotelTaxPercent.setAdapter(suggestionAdapter);
        serviceTaxPercent.setAdapter(suggestionAdapter);
        bookingTaxPercent.setAdapter(suggestionAdapter);
        busServiceTaxPercent.setAdapter(suggestionAdapter);


        hotelTaxPercent.setOnItemSelectedListener(this);
        serviceTaxPercent.setOnItemSelectedListener(this);
        bookingTaxPercent.setOnItemSelectedListener(this);
        busServiceTaxPercent.setOnItemSelectedListener(this);

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

                case R.id.hotelTaxPercent:
                    calculateHotelTaxPercent();
                    break;

                case R.id.serviceTaxPercent:
                    calculateHotelServiceChargePercent();
                    break;

                case R.id.bookingTaxPercent:
                    calculateBusTaxPercent();
                    break;

                case R.id.busServiceTaxPercent:
                    calculateBusServiceChargePercent();
                    break;
            }
        }

    }


    @OnClick(R.id.submit)
    protected void onSubmit() {
        String strTravelType = travelTypeSpinner.getSelectedItem().toString();
        tripBooking.setTravel_type(strTravelType);
        tripBooking.setTrip_id(trip.getId());
        tripBooking.setAdminBookingMode(strSelectedTravel);
        if (strSelectedTravel.equalsIgnoreCase(resources.getString(R.string.hotel_booking_mode))) {
            tripBooking.setAdminCityArea(hotelCityArea.getText().toString().trim());
            tripBooking.setAdminCheckIn(checkIn.getText().toString().trim());
            tripBooking.setAdminCheckOut(checkOut.getText().toString().trim());
            String strRoom = room.getText().toString().trim();
            tripBooking.setAdminRoom(TextUtils.isEmpty(strRoom) ? 0 : Integer.parseInt(strRoom));
            String vendor = hotelVendor.getText().toString().trim();
            tripBooking.setAdminVendor(vendor);
            tripBooking.setAdminVendorId(vendorId);
            String strNightRate = rate.getText().toString().trim();
            tripBooking.setRate(TextUtils.isEmpty(strNightRate) ? 0 : Double.parseDouble(strNightRate));
            String strAmount = hotelAmount.getText().toString().trim();
            tripBooking.setAdminTotalAmount(TextUtils.isEmpty(strAmount) ? 0 : Double.parseDouble(strAmount));
            if (!TextUtils.isEmpty(vendor)) {
                BookingMode taxBookingMode = (BookingMode) hotelTaxPercent.getSelectedItem();
                tripBooking.setTax_name(taxBookingMode.getTitle());
                String strTaxAmount = hotelTaxAmount.getText().toString().trim();
                double taxAmount = TextUtils.isEmpty(strTaxAmount) ? 0 : Double.parseDouble(strTaxAmount);
                tripBooking.setTax_amount(taxAmount);
                tripBooking.setTax_type(taxBookingMode.getType());
                BookingMode serviceChargeBookingMode = (BookingMode) serviceTaxPercent.getSelectedItem();
                tripBooking.setServiceTaxName(serviceChargeBookingMode.getTitle());
                String strServiceAmount = serviceChargeSubAmount.getText().toString().trim();
                double serviceAmount = TextUtils.isEmpty(strServiceAmount) ? 0 : Double.parseDouble(strServiceAmount);
                tripBooking.setServiceTaxAmount(serviceAmount);
                tripBooking.setServiceTaxType(serviceChargeBookingMode.getType());
            }
            String strTotal = hotelAmount.getText().toString().trim();
            Double totalAmount = TextUtils.isEmpty(strTotal) ? 0 : Double.parseDouble(strTotal);
            tripBooking.setTotalAmount(totalAmount);
            String strTotalNights = (hotelNights.getText().toString()).replaceAll("[^\\d]", "");
            int days = TextUtils.isDigitsOnly(strTotalNights) ? Integer.parseInt(strTotalNights) : 1;
            tripBooking.setQuantity(days);


        } else if (strSelectedTravel.equalsIgnoreCase(resources.getString(R.string.booking_train)) || strSelectedTravel.equalsIgnoreCase(resources.getString(R.string.booking_bus)) || strSelectedTravel.equalsIgnoreCase(resources.getString(R.string.booking_flight))) {
            tripBooking.setAdminSource(from.getText().toString().trim());
            tripBooking.setAdminDestination(to.getText().toString().trim());
            String strDepartureDT = resources.getString(R.string.date_time, departureDate.getText().toString().trim(), departureTime.getText().toString().trim());
            tripBooking.setAdminDeparture(strDepartureDT);
            String strArrivalDT = resources.getString(R.string.date_time, arrivalDate.getText().toString().trim(), arrivalTime.getText().toString().trim());
            tripBooking.setAdminArrival(strArrivalDT);
            String vendor = busVendor.getText().toString().trim();
            tripBooking.setAdminVendor(vendor);
            tripBooking.setAdminVendorId(vendorId);
            String strAmount = busAmount.getText().toString().trim();
            double amountValue = TextUtils.isEmpty(strAmount) ? 0 : Double.parseDouble(strAmount);
            tripBooking.setAdminTotalAmount(amountValue);
            tripBooking.setRate(amountValue);
            tripBooking.setQuantity(1);
            if (!TextUtils.isEmpty(vendor)) {
                BookingMode taxBookingMode = (BookingMode) bookingTaxPercent.getSelectedItem();
                tripBooking.setTax_name(taxBookingMode.getTitle());
                String strTaxAmount = bookingTaxAmount.getText().toString().trim();
                double taxAmount = TextUtils.isEmpty(strTaxAmount) ? 0 : Double.parseDouble(strTaxAmount);
                tripBooking.setTax_amount(taxAmount);
                tripBooking.setTax_type(taxBookingMode.getType());
                BookingMode serviceChargeBookingMode = (BookingMode) busServiceTaxPercent.getSelectedItem();
                tripBooking.setServiceTaxName(serviceChargeBookingMode.getTitle());
                String strServiceAmount = busServiceChargeSubAmount.getText().toString().trim();
                double serviceAmount = TextUtils.isEmpty(strServiceAmount) ? 0 : Double.parseDouble(strServiceAmount);
                tripBooking.setServiceTaxAmount(serviceAmount);
                tripBooking.setServiceTaxType(serviceChargeBookingMode.getType());
            }
            String strTotal = billTotalAmount.getText().toString().trim();
            Double totalAmount = TextUtils.isEmpty(strTotal) ? 0 : Double.parseDouble(strTotal);
            tripBooking.setTotalAmount(totalAmount);
        }
        tripBooking.setBookingMembers(memberArrayList);
        tripBooking.setCreatedBy(actionBy);


        if (invalidate()) {
            if (connection.isNetworkAvailable(this)) {
                dialog.showDialog();
                tripBooking.setCreated_by_id(Integer.parseInt(userPref.getUserId()));
                Gson gson = new Gson();
                String json = gson.toJson(tripBooking);
                Map<String, RequestBody> myMap = new HashMap<>();

                RequestBody rbAction = RequestBody.create(MediaType.parse("text/plain"), "self booking");
                myMap.put(ConstantVariable.MIX_ID.ACTION, rbAction);

                RequestBody rbProjectId = RequestBody.create(MediaType.parse("text/plain"), json);
                myMap.put(ConstantVariable.BOOKING_JSON, rbProjectId);

                Call<AddResponse> bookingRequestApi = api.tripSelfBooking(myMap);
                bookingRequestApi.enqueue(new Callback<AddResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<AddResponse> call, @NonNull Response<AddResponse> response) {
                        dialog.dismissDialog();
                        if (response.isSuccessful()) {
                            AddResponse stringResponse = response.body();
                            if (stringResponse != null) {
                                tripBooking.setId(stringResponse.getId());
                                showActionDialog(stringResponse.getId());
                            }
                        } else {
                            showMessage(resources.getString(R.string.problem_to_connect));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AddResponse> call, @NonNull Throwable t) {
                        dialog.dismissDialog();
                        if (t instanceof SocketTimeoutException) {
                            showMessage(resources.getString(R.string.slow_internet_connection));
                        }
                    }
                });
            }
        }
    }

    private void showActionDialog(int bookingId) {
        if (actionBy.equalsIgnoreCase(resources.getString(R.string.user)))
            showUserPaymentDialog(bookingId);
        else
            showAdminPaymentDialog();
    }

    private void showAdminPaymentDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        builder.setMessage(R.string.payment_done_message);
        builder.setCancelable(true);
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                startHomeActivity();
            }
        });

        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                proceedPayment();

            }
        });

        builder.show();
    }

    private void startHomeActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setResult(Activity.RESULT_OK);
                finish();
            }
        },500);

    }

    private void proceedPayment(){
        Intent intent = new Intent(this,TripBookingPaymentActivity.class);
        intent.putExtra(ConstantVariable.MIX_ID.ACTION,actionBy);
        intent.putExtra(ConstantVariable.TRIP_BOOKINGS,tripBooking);
        startActivityForResult(intent,RC_PAYMENT);
    }
    private void showUserPaymentDialog(final int bookingId) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        builder.setMessage(R.string.payment_request_message);
        builder.setCancelable(true);
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                startHomeActivity();
            }
        });

        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                proceedPaymentRequest(bookingId);

            }
        });

        builder.show();
    }

    private void proceedPaymentRequest(int bookingId) {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<StringResponse> bookingRequestApi = api.paymentRequest("payment request", bookingId, userPref.getUserId());
            bookingRequestApi.enqueue(new Callback<StringResponse>() {
                @Override
                public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        StringResponse stringResponse = response.body();
                        if (stringResponse != null) {
                            if (!stringResponse.isError()){
                                startHomeActivity();
                            }
                            showMessage(stringResponse.getMessage());

                        }
                    } else {
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
        } else {
            showMessage(resources.getString(R.string.internet_not_available));
        }
    }

    private boolean invalidate() {
        boolean valid = true;
        if (strSelectedTravel.equalsIgnoreCase(resources.getString(R.string.hotel_booking_mode))) {
            if (TextUtils.isEmpty(tripBooking.getAdminCityArea())) {
                showMessage("City/Area is required");
                valid = false;
            } else if (TextUtils.isEmpty(tripBooking.getAdminCheckIn())) {
                showMessage("Check-in is required");
                valid = false;
            } else if (TextUtils.isEmpty(tripBooking.getAdminCheckOut())) {
                showMessage("Check-out is required");
                valid = false;
            } else if (tripBooking.getAdminRoom() <= 0) {
                showMessage("Room can not be zero");
                valid = false;
            } else if (TextUtils.isEmpty(tripBooking.getAdminVendor())) {
                showMessage("Vendor is required");
                valid = false;
            } else if (tripBooking.getTotalAmount() == 0) {
                showMessage("Rent is required");
                valid = false;
            }
        } else if (strSelectedTravel.equalsIgnoreCase(resources.getString(R.string.booking_train)) || strSelectedTravel.equalsIgnoreCase(resources.getString(R.string.booking_flight)) || strSelectedTravel.equalsIgnoreCase(resources.getString(R.string.booking_bus))) {
            if (TextUtils.isEmpty(tripBooking.getAdminSource())) {
                showMessage("From location is required");
                valid = false;
            } else if (TextUtils.isEmpty(tripBooking.getAdminDestination())) {
                showMessage("To location is required");
                valid = false;
            } else if (TextUtils.isEmpty(tripBooking.getAdminDeparture())) {
                showMessage("departure date time is required");
                valid = false;
            } else if (TextUtils.isEmpty(tripBooking.getAdminArrival())) {
                showMessage("Arrival date time is required");
                valid = false;
            } else if (TextUtils.isEmpty(tripBooking.getAdminVendor())) {
                showMessage("Vendor is required");
                valid = false;
            } else if (tripBooking.getAdminTotalAmount() == 0) {
                showMessage("Total fare is required");
                valid = false;
            }
        }
        return valid;
    }

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }


    private void calculateBusServiceChargePercent() {
        BookingMode bookingMode = (BookingMode) busServiceTaxPercent.getSelectedItem();
        String strBusAmount = busAmount.getText().toString();
        if ((bookingMode.getTitle().startsWith("G") || bookingMode.getTitle().startsWith("I")) && !TextUtils.isEmpty(strBusAmount)) {
            double percentValue = Double.parseDouble(bookingMode.getValue());
            double amount = (percentValue * (Double.parseDouble(strBusAmount))) / 100;
            busServiceChargeSubAmount.setText(String.valueOf(amount));
        } else {
            busServiceChargeSubAmount.setText("");
        }

        calculateTotalBusAmount();
    }

    private void calculateBusTaxPercent() {
        BookingMode bookingMode = (BookingMode) bookingTaxPercent.getSelectedItem();
        String strBusAmount = busAmount.getText().toString();
        if ((bookingMode.getTitle().startsWith("G") || bookingMode.getTitle().startsWith("I")) && !TextUtils.isEmpty(strBusAmount)) {
            double percentValue = Double.parseDouble(bookingMode.getValue());
            double amount = (percentValue * (Double.parseDouble(strBusAmount))) / 100;
            bookingTaxAmount.setText(String.valueOf(amount));
        } else {
            bookingTaxAmount.setText("");
        }

        calculateTotalBusAmount();
    }

    private void calculateHotelServiceChargePercent() {
        BookingMode bookingMode = (BookingMode) serviceTaxPercent.getSelectedItem();
        String strHotelAmount = hotelAmount.getText().toString();
        if ((bookingMode.getTitle().startsWith("G") || bookingMode.getTitle().startsWith("I")) && !TextUtils.isEmpty(strHotelAmount)) {
            double percentValue = Double.parseDouble(bookingMode.getValue());
            double amount = (percentValue * (Double.parseDouble(strHotelAmount))) / 100;

            serviceChargeSubAmount.setText(resources.getString(R.string.double_value, amount));
        } else {
            serviceChargeSubAmount.setText("");
        }

        calculateTotalHotelAmount();
    }

    private void calculateHotelTaxPercent() {
        BookingMode bookingMode = (BookingMode) hotelTaxPercent.getSelectedItem();
        String strHotelAmount = hotelAmount.getText().toString();
        if ((bookingMode.getTitle().startsWith("G") || bookingMode.getTitle().startsWith("I")) && !TextUtils.isEmpty(strHotelAmount)) {
            double percentValue = Double.parseDouble(bookingMode.getValue());
            double amount = (percentValue * (Double.parseDouble(strHotelAmount))) / 100;
            hotelTaxAmount.setText(resources.getString(R.string.double_value, amount));
        } else {
            hotelTaxAmount.setText("");
        }
        calculateTotalHotelAmount();
    }


    private void calculateTotalHotelAmount() {
        String strHotelTaxAmount = hotelTaxAmount.getText().toString().trim();
        String strHotelServiceChargeAmount = serviceChargeSubAmount.getText().toString().trim();
        String strHotelAmount = hotelAmount.getText().toString().trim();

        double hotelTaxValue = TextUtils.isEmpty(strHotelTaxAmount) ? 0 : Double.parseDouble(strHotelTaxAmount);
        double hotelServiceValue = TextUtils.isEmpty(strHotelServiceChargeAmount) ? 0 : Double.parseDouble(strHotelServiceChargeAmount);
        double hotelValue = TextUtils.isEmpty(strHotelAmount) ? 0 : Double.parseDouble(strHotelAmount);
        double finalAmount = hotelValue + hotelTaxValue + hotelServiceValue;
        hotelBillTotalAmount.setText(resources.getString(R.string.double_value, finalAmount));
    }


    private void calculateTotalBusAmount() {
        String strBusTaxAmount = bookingTaxAmount.getText().toString().trim();
        String strBusServiceChargeAmount = busServiceChargeSubAmount.getText().toString().trim();
        String strBusAmount = busAmount.getText().toString().trim();

        double hotelTaxValue = TextUtils.isEmpty(strBusTaxAmount) ? 0 : Double.parseDouble(strBusTaxAmount);
        double hotelServiceValue = TextUtils.isEmpty(strBusServiceChargeAmount) ? 0 : Double.parseDouble(strBusServiceChargeAmount);
        double busAmountValue = TextUtils.isEmpty(strBusAmount) ? 0 : Double.parseDouble(strBusAmount);
        double finalAmount = busAmountValue + hotelTaxValue + hotelServiceValue;
        billTotalAmount.setText(resources.getString(R.string.double_value, finalAmount));
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
                    calculateTotalAmount();
                    break;

                case R.id.checkOut:
                    calculateTotalAmount();
                    break;

                case R.id.room:
                    calculateNights();
                    calculateTotalAmount();
                    break;

                case R.id.rate:
                    calculateTotalAmount();
                    break;

                case R.id.busAmount:
                    calculateTotalBusAmount();
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


    private void calculateTotalAmount() {
        String strHotelFirst = checkIn.getText().toString().trim();
        String strHotelLast = checkOut.getText().toString().trim();
        String strRate = rate.getText().toString().trim();
        String strRoom = room.getText().toString().trim();
        double vendorHotelNightRate = TextUtils.isEmpty(strRate) ? 0 : Double.parseDouble(strRate);
        if (!TextUtils.isEmpty(strHotelFirst) && !TextUtils.isEmpty(strHotelLast) && !TextUtils.isEmpty(strRoom) && TextUtils.isDigitsOnly(strRoom)) {
            int days = DateCal.getDays(strHotelFirst, strHotelLast) * (Integer.parseInt(strRoom));
            hotelNights.setVisibility(View.VISIBLE);
            hotelNights.setText(resources.getString(R.string.total_night, days));
            double totalNightsRate = vendorHotelNightRate * days;
            hotelAmount.setText(resources.getString(R.string.double_value, totalNightsRate));
            calculateTotalHotelAmount();
        }
    }


    private void calculateNights() {
        String strRoom = room.getText().toString().trim();
        if (!TextUtils.isEmpty(strRoom) && TextUtils.isDigitsOnly(strRoom)) {
            double totalNightsRate = vendorHotelNightRate * (Integer.parseInt(strRoom));
            rate.setText(resources.getString(R.string.double_value, totalNightsRate));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
