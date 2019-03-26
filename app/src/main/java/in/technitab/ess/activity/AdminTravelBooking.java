package in.technitab.ess.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.model.Trip;
import in.technitab.ess.model.TripMember;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.CustomDate;
import in.technitab.ess.util.CustomEditText;
import in.technitab.ess.util.DateCal;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;

public class AdminTravelBooking extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = AdminTravelBooking.class.getSimpleName();
    private static final int RC_TRAVEL_VENDOR = 1, RC_HOTEL_VENDOR = 2;
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
    @BindView(R.id.hotel_users_layout)
    LinearLayout hotelUsersLayout;
    @BindView(R.id.hotel_vendor)
    CustomEditText hotelVendor;
    @BindView(R.id.room)
    CustomEditText room;
    @BindView(R.id.hotel_amount)
    CustomEditText hotelAmount;
    @BindView(R.id.hotelLayout)
    LinearLayout hotelLayout;
    @BindView(R.id.vendor)
    EditText vendor;
    @BindView(R.id.from)
    EditText from;
    @BindView(R.id.to)
    EditText to;
    @BindView(R.id.bus_date)
    CustomEditText busDate;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.busTrainFlightLayout)
    LinearLayout busTrainFlightLayout;
    @BindView(R.id.bank)
    RadioButton bank;
    @BindView(R.id.cash)
    RadioButton cash;
    @BindView(R.id.credit_card)
    RadioButton creditCard;
    @BindView(R.id.paymentMode)
    RadioGroup paymentMode;
    @BindView(R.id.paymentDate)
    CustomEditText paymentDate;
    @BindView(R.id.amount)
    EditText amount;
    @BindView(R.id.reference_number)
    CustomEditText referenceNumber;
    @BindView(R.id.notes)
    EditText notes;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.rate)
    CustomEditText rate;
    private Trip trip;
    private ArrayList<TripMember> memberArrayList;
    private List<String> mTravelModeList;
    private String strSelectedTravel = "";
    private Resources resources;
    private NetConnection connection;
    private Dialog dialog;
    private UserPref userPref;
    private RestApi api;
    private int vendorId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_travel_booking);
        ButterKnife.bind(this);

        init();
        setToolbar();
        setSpinner();
        new CustomDate(checkIn, this, trip.getStartDate(), null);
        checkIn.addTextChangedListener(new MyWatcher(checkIn));
    }


    private void init() {
        mTravelModeList = new ArrayList<>();
        userPref = new UserPref(this);
        connection = new NetConnection();
        dialog = new Dialog(this);
        api = APIClient.getClient().create(RestApi.class);
        resources = getResources();
        memberArrayList = new ArrayList<>();
        trip = new Trip();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            trip = bundle.getParcelable(resources.getString(R.string.trip));
            memberArrayList = bundle.getParcelableArrayList(ConstantVariable.LIST);
            showMemberList(hotelUsersLayout, memberArrayList);
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


    @OnClick({R.id.hotel_vendor, R.id.vendor})
    protected void onPickVendor(View view) {
        vendorId = 0;
        Intent intent = new Intent(this, VendorActivity.class);
        int RC = 3;
        switch (view.getId()) {

            case R.id.vendor:
                RC = RC_TRAVEL_VENDOR;
                break;

            case R.id.hotel_vendor:
                RC = RC_HOTEL_VENDOR;
                break;
        }

        intent.putExtra(ConstantVariable.MIX_ID.ACTION, resources.getString(R.string.pick_vendor));
        intent.putExtra(ConstantVariable.Tec.ID, "Hotel");
        intent.putExtra(ConstantVariable.MIX_ID.TITLE, resources.getString(R.string.lodging));
        startActivityForResult(intent, RC);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_HOTEL_VENDOR && resultCode == Activity.RESULT_OK && data != null) {
            String vendor = data.getStringExtra(ConstantVariable.MIX_ID.VENDOR);
            vendorId = data.getIntExtra(ConstantVariable.MIX_ID.ID, -1);
            rate.setText(String.valueOf(data.getDoubleExtra(ConstantVariable.MIX_ID.VALUE,0)));
            hotelVendor.setText(vendor);
        } else if (requestCode == RC_TRAVEL_VENDOR && resultCode == Activity.RESULT_OK && data != null) {
            String strVendor = data.getStringExtra(ConstantVariable.MIX_ID.VENDOR);
            vendorId = data.getIntExtra(ConstantVariable.MIX_ID.ID, -1);
            vendor.setText(strVendor);
        }
    }

    private void showMemberList(final LinearLayout memberLayout, ArrayList<TripMember> list) {
        if (hotelUsersLayout.getChildCount() > 0) {
            memberLayout.removeAllViews();
        }

        for (TripMember user : list) {
            memberLayout.setVisibility(View.VISIBLE);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_member, null);
            final CheckBox checkedTextView = view.findViewById(R.id.name);
            if (checkedTextView.getParent() != null) {
                ((ViewGroup) checkedTextView.getParent()).removeView(checkedTextView);
            }

            checkedTextView.setText(user.getMemberId() == Integer.parseInt(userPref.getUserId()) ? "Self" : user.getName());
            checkedTextView.setChecked(user.getMemberId() == Integer.parseInt(userPref.getUserId()) || user.isSelected());
            user.setSelected(user.getMemberId() == Integer.parseInt(userPref.getUserId()) || user.isSelected());

            memberLayout.addView(checkedTextView);
            checkedTextView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int position = memberLayout.indexOfChild(checkedTextView);
                    memberArrayList.get(position).setSelected(!memberArrayList.get(position).isSelected());

                }
            });
        }
    }

    private void setSpinner() {
        List<String> mTravelTypeList = Arrays.asList(getResources().getStringArray(R.array.travelTypeArray));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mTravelTypeList);
        travelTypeSpinner.setAdapter(adapter);
        travelTypeSpinner.setOnItemSelectedListener(this);

        mTravelModeList = Arrays.asList(getResources().getStringArray(R.array.bookingTravelModeArray));
        ArrayAdapter<String> travelModeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mTravelModeList);
        travelModeSpinner.setAdapter(travelModeAdapter);
        travelModeSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (view != null) {
            view.setPadding(2, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
            ((TextView) view).setTextColor(Color.BLACK);
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.mediumTextSize));

            switch (adapterView.getId()) {

                case R.id.travelModeSpinner:
                    strSelectedTravel = travelModeSpinner.getSelectedItem().toString();
                    switch (strSelectedTravel) {
                        case "Hotel/PG/Lodge":
                            hotelLayout.setVisibility(View.VISIBLE);
                            busTrainFlightLayout.setVisibility(View.GONE);
                            showMemberList(hotelUsersLayout, memberArrayList);
                            break;

                        case "Bus":
                            hotelLayout.setVisibility(View.GONE);
                            //showMemberList(travelUsersLayout, memberArrayList);
                            busTrainFlightLayout.setVisibility(View.VISIBLE);
                            break;

                        case "Train":
                            hotelLayout.setVisibility(View.GONE);
                            //showMemberList(travelUsersLayout, memberArrayList);
                            busTrainFlightLayout.setVisibility(View.VISIBLE);
                            break;

                        case "Flight":
                            hotelLayout.setVisibility(View.GONE);
                            //showMemberList(travelUsersLayout, memberArrayList);
                            busTrainFlightLayout.setVisibility(View.VISIBLE);
                            break;
                    }
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
                        new CustomDate(checkOut, AdminTravelBooking.this, date, null);
                    break;

                case R.id.checkOut:
                    String strCheckOut = checkOut.getText().toString().trim();
                    if (!strCheckOut.isEmpty())
                        break;
            }
            calculateNights();

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    }


    private void calculateNights() {
        String strHotelFirst = checkIn.getText().toString().trim();
        String strHotelLast = checkOut.getText().toString().trim();
        if (strHotelFirst.isEmpty() || strHotelLast.isEmpty()) {
            rate.setText(String.valueOf(0));
        } else {
            rate.setText(String.valueOf(DateCal.getDays(strHotelFirst, strHotelLast)));
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
