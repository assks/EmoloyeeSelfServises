package in.technitab.ess.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import in.technitab.ess.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.adapter.SpinAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.model.BookingMode;
import in.technitab.ess.model.NewTec;
import in.technitab.ess.model.NewTecEntry;
import in.technitab.ess.model.StringResponse;
import in.technitab.ess.util.ConstantValues;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.CustomDate;
import in.technitab.ess.util.CustomEditText;
import in.technitab.ess.util.DateCal;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.FileNamePath;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.Permissions;
import in.technitab.ess.util.SetTime;
import in.technitab.ess.util.UserPref;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditTecClaimActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.fuelDate)
    EditText fuelDate;
    @BindView(R.id.typeVehicleSpinner)
    Spinner typeVehicleSpinner;
    @BindView(R.id.fromFuelSpinner)
    Spinner fromFuelSpinner;
    @BindView(R.id.otherFormFuel)
    EditText otherFormFuel;
    @BindView(R.id.otherFormFuelLayout)
    TextInputLayout otherFormFuelLayout;
    @BindView(R.id.otherToFuel)
    EditText otherToFuel;
    @BindView(R.id.otherToFuelLayout)
    TextInputLayout otherToFuelLayout;
    @BindView(R.id.toFuelSpinner)
    Spinner toFuelSpinner;
    @BindView(R.id.travel_distance)
    EditText travelDistance;
    @BindView(R.id.mileage)
    EditText mileage;
    @BindView(R.id.fuel_bill_amount)
    EditText fuelBillAmount;
    @BindView(R.id.fuelLayout)
    LinearLayout fuelLayout;
    @BindView(R.id.intercity_vendor)
    EditText intercityVendor;
    @BindView(R.id.userLocation)
    RelativeLayout userLocation;
    @BindView(R.id.adminFrom)
    EditText adminFrom;
    @BindView(R.id.adminTo)
    EditText adminTo;
    @BindView(R.id.adminLocation)
    RelativeLayout adminLocation;
    @BindView(R.id.userFuelLocation)
    RelativeLayout userFuelLocation;
    @BindView(R.id.adminFuelFrom)
    EditText adminFuelFrom;
    @BindView(R.id.adminFuelTo)
    EditText adminFuelTo;
    @BindView(R.id.adminFuelLocation)
    RelativeLayout adminFuelLocation;
    @BindView(R.id.hotelBillDate)
    EditText hotelBillDate;
    @BindView(R.id.hotelStayFirst)
    EditText hotelStayFirst;
    @BindView(R.id.hotelStayLast)
    EditText hotelStayLast;
    @BindView(R.id.foodFromDate)
    EditText foodFromDate;

    @BindView(R.id.foodToDate)
    EditText foodToDate;
    @BindView(R.id.non_working_days)
    EditText nonWorkingDays;
    @BindView(R.id.siteFromDate)
    EditText siteFromDate;
    @BindView(R.id.siteToDate)
    EditText siteToDate;
    @BindView(R.id.claimSpinner)
    TextView claimSpinner;
    @BindView(R.id.travelModeSpinner)
    Spinner travelModeSpinner;
    @BindView(R.id.from)
    TextView from;
    @BindView(R.id.fromSpinner)
    Spinner fromSpinner;
    @BindView(R.id.toSpinner)
    Spinner toSpinner;
    @BindView(R.id.otherForm)
    EditText otherForm;
    @BindView(R.id.otherFormLayout)
    TextInputLayout otherFormLayout;
    @BindView(R.id.otherTo)
    EditText otherTo;
    @BindView(R.id.intercityBillingDate)
    CustomEditText intercityBillingDate;
    @BindView(R.id.otherToLayout)
    TextInputLayout otherToLayout;
    @BindView(R.id.DepartureDate)
    EditText DepartureDate;
    @BindView(R.id.DepartureTime)
    EditText DepartureTime;
    @BindView(R.id.arrivalDate)
    EditText arrivalDate;
    @BindView(R.id.arrivalTime)
    EditText arrivalTime;
    @BindView(R.id.bill_amount)
    EditText billAmount;
    @BindView(R.id.intercityEditAttachment)
    TextView intercityEditAttachment;
    @BindView(R.id.admin_view_attachment)
    RelativeLayout adminViewAttachment;
    @BindView(R.id.intercityEmployee)
    RadioButton intercityEmployee;
    @BindView(R.id.intercityAccount)
    RadioButton intercityAccount;
    @BindView(R.id.intercityPaidRadioGroup)
    RadioGroup intercityPaidRadioGroup;
    @BindView(R.id.intercity_travel_cost)
    LinearLayout intercityTravelCost;
    @BindView(R.id.cityHotel)
    EditText cityHotel;
    @BindView(R.id.hotel_gstin)
    EditText hotelGstin;
    @BindView(R.id.hotelBillNum)
    EditText hotelBillNum;
    @BindView(R.id.hotelNights)
    EditText hotelNights;
    @BindView(R.id.hotel_rate)
    EditText hotelRate;
    @BindView(R.id.hotel_bill_amount)
    EditText hotelBillAmount;
    @BindView(R.id.hotelEditAttachment)
    TextView hotelEditAttachment;
    @BindView(R.id.admin_view_hotel_attachment)
    RelativeLayout adminViewHotelAttachment;
    @BindView(R.id.hotelEmployee)
    RadioButton hotelEmployee;
    @BindView(R.id.hotelAccount)
    RadioButton hotelAccount;
    @BindView(R.id.hotelPaidRadioGroup)
    RadioGroup hotelPaidRadioGroup;
    @BindView(R.id.layoutHotel)
    LinearLayout layoutHotel;
    @BindView(R.id.city)
    Spinner city;
    @BindView(R.id.otherCityLayout)
    TextInputLayout otherCityLayout;
    @BindView(R.id.otherCity)
    EditText otherCity;
    @BindView(R.id.foodStayModeGroup)
    RadioGroup foodStayModeGroup;
    @BindView(R.id.foodBillable)
    CheckBox foodBillable;
    @BindView(R.id.foodHotel)
    RadioButton foodHotel;
    @BindView(R.id.foodGuesthouse)
    RadioButton foodGuesthouse;
    @BindView(R.id.foodSelf)
    RadioButton foodSelf;
    @BindView(R.id.foodAttachFile)
    ImageButton foodAttachFile;
    @BindView(R.id.foodEditAttachment)
    TextView foodEditAttachment;
    @BindView(R.id.admin_view_food_attachment)
    RelativeLayout adminViewFoodAttachment;
    @BindView(R.id.days)
    EditText days;
    @BindView(R.id.rate)
    EditText rate;
    @BindView(R.id.signPerDiemDate)
    CustomEditText signPerDiemDate;
    @BindView(R.id.food_bill_amount)
    EditText foodBillAmount;
    @BindView(R.id.layoutFoodBoardingIem)
    LinearLayout layoutFoodBoardingIem;
    @BindView(R.id.conveyanceDays)
    EditText conveyanceDays;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.actual)
    EditText actual;
    @BindView(R.id.site_bill_amount)
    EditText siteBillAmount;
    @BindView(R.id.layoutSiteConveyance)
    LinearLayout layoutSiteConveyance;
    @BindView(R.id.bill_date)
    EditText billDate;
    @BindView(R.id.mislenious_description)
    EditText misleniousDescription;
    @BindView(R.id.mis_vendor)
    EditText misVendor;
    @BindView(R.id.mis_gstin)
    EditText misGstin;
    @BindView(R.id.misBillNum)
    EditText misBillNum;
    @BindView(R.id.miscQuantity)
    EditText miscQuantity;
    @BindView(R.id.miscRate)
    EditText miscRate;
    @BindView(R.id.misc_bill_amount)
    EditText miscBillAmount;
    @BindView(R.id.miscEditAttachment)
    TextView miscEditAttachment;
    @BindView(R.id.admin_view_misc_attachment)
    RelativeLayout adminViewMiscAttachment;
    @BindView(R.id.mislleniousLaoyut)
    LinearLayout mislleniousLaoyut;
    @BindView(R.id.onlyEditAttachment)
    TextView onlyEditAttachment;
    @BindView(R.id.admin_view_only_attachment)
    RelativeLayout adminViewOnlyAttachment;
    @BindView(R.id.attachmentDate)
    EditText attachmentDate;
    @BindView(R.id.attachmentDescription)
    EditText attachmentDescription;
    @BindView(R.id.attachmentLayout)
    LinearLayout attachmentLayout;
    @BindView(R.id.submit)
    Button submit;

    String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int RC_PERMISSIONS = 1;
    private static final int RC_CAPTURE = 2, RC_PICK = 3, RC_CONVERT = 4, VENDOR = 6, CITY_HOTEL = 7, INTERCITY_VENDOR = 8;

    private NetConnection connection;
    private Dialog dialog;
    private RestApi api;
    private UserPref userPref;
    private Resources resources;
    private String strClaimCategory = "";
    private List<String> claimCategoryList;
    private int tecId = 0, position, headerPosition = 0, createById = 0;
    private String projectName = "", strBaseLocation = "", strSiteLocation = "", strClaimDate = "", action = "";
    private File mFile = null;
    private List<String> travelCategoryList;
    private NewTecEntry tecEntry;
    private NewTec newTec;
    private ArrayList<BookingMode> FoodStayCities;
    private Uri mFileUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tec_claim);
        ButterKnife.bind(this);

        init();
        setSpinner();
        setDataTimePicker();
        setListener();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            newTec = bundle.getParcelable(resources.getString(R.string.tec));
            headerPosition = bundle.getInt(ConstantVariable.MIX_ID.HEADER_POSITION);
            action = bundle.getString(ConstantVariable.MIX_ID.ACTION);
            position = bundle.getInt(ConstantVariable.MIX_ID.ID);
            tecId = bundle.getInt(ConstantVariable.Tec.TEC_ID);
            projectName = bundle.getString(ConstantVariable.Tec.PROJECT_NAME);
            strSiteLocation = bundle.getString(ConstantVariable.Tec.SITE_LOCATION);
            strBaseLocation = bundle.getString(ConstantVariable.Tec.BASE_LOCATION);
            tecEntry = bundle.getParcelable(resources.getString(R.string.tec_entry));
            createById = bundle.getInt(ConstantVariable.Tec.CREATED_BY_ID);
            setFromToSpinner(strSiteLocation, strBaseLocation);

            setToolbar();
            showHide(tecEntry);
        }

    }

    private void init() {
        connection = new NetConnection();
        resources = getResources();
        dialog = new Dialog(this);
        userPref = new UserPref(this);
        claimCategoryList = new ArrayList<>();
        travelCategoryList = new ArrayList<>();
        api = APIClient.getClient().create(RestApi.class);
        FoodStayCities = new ArrayList<>();
        claimCategoryList = Arrays.asList(resources.getStringArray(R.array.tecCategoryArray));
    }

    private void setListener() {
        intercityBillingDate.addTextChangedListener(new MyWatcher(intercityBillingDate));
        DepartureDate.addTextChangedListener(new MyWatcher(DepartureDate));
        rate.addTextChangedListener(new MyWatcher(rate));
        hotelNights.addTextChangedListener(new MyWatcher(hotelNights));
        hotelRate.addTextChangedListener(new MyWatcher(hotelRate));
        days.addTextChangedListener(new MyWatcher(days));
        conveyanceDays.addTextChangedListener(new MyWatcher(conveyanceDays));
        actual.addTextChangedListener(new MyWatcher(actual));
        travelDistance.addTextChangedListener(new MyWatcher(travelDistance));
        hotelStayFirst.addTextChangedListener(new MyWatcher(hotelStayFirst));
        hotelStayLast.addTextChangedListener(new MyWatcher(hotelStayLast));
        signPerDiemDate.addTextChangedListener(new MyWatcher(signPerDiemDate));
        foodFromDate.addTextChangedListener(new MyWatcher(foodFromDate));
        foodToDate.addTextChangedListener(new MyWatcher(foodToDate));
        siteFromDate.addTextChangedListener(new MyWatcher(siteFromDate));
        siteToDate.addTextChangedListener(new MyWatcher(siteToDate));
        nonWorkingDays.addTextChangedListener(new MyWatcher(nonWorkingDays));
        foodStayModeGroup.setOnCheckedChangeListener(this);
        miscQuantity.addTextChangedListener(new MyWatcher(miscQuantity));
        miscRate.addTextChangedListener(new MyWatcher(miscRate));
    }

    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(projectName);
            actionBar.setBackgroundDrawable(new ColorDrawable());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void setDataTimePicker() {
        new SetTime(DepartureTime, this);
        new SetTime(arrivalTime, this);
        new CustomDate(intercityBillingDate, this, strClaimDate, null);
        new CustomDate(fuelDate, this, strClaimDate, null);
        new CustomDate(billDate, this, null, null);
        new CustomDate(fuelDate, this, strClaimDate, null);
        new CustomDate(hotelBillDate, this, strClaimDate, null);
        new CustomDate(hotelStayFirst, this, strClaimDate, null);
        new CustomDate(signPerDiemDate, this, strClaimDate, null);
        new CustomDate(siteFromDate, this, strClaimDate, null);
        new CustomDate(siteToDate, this, strClaimDate, null);
    }

    private void setSpinner() {
        travelCategoryList = Arrays.asList(resources.getStringArray(R.array.travelModeArray));
        ArrayAdapter<String> travelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, travelCategoryList);
        travelModeSpinner.setAdapter(travelAdapter);
        travelModeSpinner.setOnItemSelectedListener(this);

        List<String> vehicleList = Arrays.asList(resources.getStringArray(R.array.typeVehicleArray));
        ArrayAdapter<String> vehicleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, vehicleList);
        typeVehicleSpinner.setAdapter(vehicleAdapter);
        typeVehicleSpinner.setOnItemSelectedListener(this);

        List<Object> foodMetroCities = new ArrayList<>();
        FoodStayCities.addAll(ConstantValues.getPerDiemCity());
        foodMetroCities.addAll(FoodStayCities);
        SpinAdapter spinAdapter = new SpinAdapter(this, android.R.layout.simple_list_item_1, foodMetroCities);
        city.setAdapter(spinAdapter);
        city.setOnItemSelectedListener(this);
    }

    private void setFromToSpinner(String strSiteLocation, String strBaseLocation) {
        List<String> fromList = new ArrayList<>();
        fromList.add(strBaseLocation);
        fromList.add(resources.getString(R.string.other));

        List<String> toList = new ArrayList<>();
        toList.add(strSiteLocation);
        toList.add(resources.getString(R.string.other));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, fromList);
        fromSpinner.setAdapter(adapter);
        fromSpinner.setOnItemSelectedListener(this);

        fromFuelSpinner.setAdapter(adapter);
        fromFuelSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<String> toAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, toList);
        toSpinner.setAdapter(toAdapter);
        toSpinner.setOnItemSelectedListener(this);

        toFuelSpinner.setAdapter(toAdapter);
        toFuelSpinner.setOnItemSelectedListener(this);
    }

    private void showHide(NewTecEntry tecEntry) {
        if (tecEntry != null) {
            strClaimCategory = tecEntry.getEntryCategory();
            claimSpinner.setText(strClaimCategory);
            if (strClaimCategory.equalsIgnoreCase(claimCategoryList.get(0))) {
                intercityTravelCost.setVisibility(View.VISIBLE);

                for (int i = 0; i < travelCategoryList.size(); i++) {
                    if (tecEntry.getTravelMode().equalsIgnoreCase(travelCategoryList.get(i)))
                        travelModeSpinner.setSelection(i);
                }

                intercityBillingDate.setText(tecEntry.getDate());
                DepartureDate.setText(tecEntry.getDepartureDate());
                DepartureTime.setText(tecEntry.getDepartureTime());
                arrivalDate.setText(tecEntry.getArrivalDate());
                arrivalTime.setText(tecEntry.getArrivalTime());
                adminViewAttachment.setVisibility(View.VISIBLE);

                if (createById == Integer.parseInt(userPref.getUserId())) {
                    userLocation.setVisibility(View.VISIBLE);
                    if (!tecEntry.getFromLocation().equalsIgnoreCase(strBaseLocation)) {
                        otherFormLayout.setVisibility(View.VISIBLE);
                        fromSpinner.setSelection(1);
                        otherForm.setText(tecEntry.getFromLocation());
                    }

                    if (!tecEntry.getToLocation().equalsIgnoreCase(strSiteLocation)) {
                        otherToLayout.setVisibility(View.VISIBLE);
                        toSpinner.setSelection(1);
                        otherTo.setText(tecEntry.getToLocation());
                    }

                } else {
                    adminLocation.setVisibility(View.VISIBLE);
                    adminFrom.setText(tecEntry.getFromLocation());
                    adminTo.setText(tecEntry.getToLocation());

                }
                intercityVendor.setText(tecEntry.getPaidTo());
                String fileName = tecEntry.getAttachmentPath();
                if (!fileName.isEmpty())
                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1);

                intercityEditAttachment.setText(fileName);
                intercityEditAttachment.setTextColor(resources.getColor(R.color.colorPrimary));
                if (tecEntry.getPaidBy().equalsIgnoreCase(resources.getString(R.string.employee)))
                    intercityEmployee.setChecked(true);
                else
                    intercityAccount.setChecked(true);

                billAmount.setText(String.valueOf(tecEntry.getBillAmount()));


            } else if (strClaimCategory.equalsIgnoreCase(claimCategoryList.get(1))) {

                layoutFoodBoardingIem.setVisibility(View.VISIBLE);

                if (newTec.getIsInternational() == 1) {
                    city.setEnabled(false);
                    rate.setFocusable(true);
                } else {
                    city.setEnabled(true);
                    rate.setFocusable(false);
                }
                int selectedCityIndex = 0;
                for (int i = 0; i < FoodStayCities.size(); i++) {
                    if (FoodStayCities.get(i).getTitle().equalsIgnoreCase(tecEntry.getLocation())) {
                        selectedCityIndex = i;
                        break;
                    }
                }
                signPerDiemDate.setText(tecEntry.getDate());
                foodFromDate.setText(tecEntry.getDepartureDate());
                long workingDays = DateCal.getSiteDays(tecEntry.getDepartureDate(), tecEntry.getArrivalDate());
                int day = (int)(workingDays-tecEntry.getTotalQuantitty());
                nonWorkingDays.setText(String.valueOf(day));
                foodToDate.setText(tecEntry.getArrivalDate());
                city.setSelection(selectedCityIndex);
                otherCityLayout.setVisibility(View.VISIBLE);
                otherCity.setText(tecEntry.getLocation());
                days.setText(String.valueOf(tecEntry.getTotalQuantitty()));
                rate.setText(String.valueOf(tecEntry.getUnitPrice()));
                foodBillAmount.setText(String.valueOf(tecEntry.getBillAmount()));
                adminViewFoodAttachment.setVisibility(View.VISIBLE);

                String fileName = tecEntry.getAttachmentPath();
                if (!fileName.isEmpty())
                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1);

                foodEditAttachment.setText(fileName);
                foodEditAttachment.setTextColor(resources.getColor(R.color.colorPrimary));
                foodBillable.setChecked(tecEntry.getIsBillable().equalsIgnoreCase("Y"));

                if (newTec.getIsInternational() == 0) {
                    if (tecEntry.getTravelMode().equalsIgnoreCase(resources.getString(R.string.hotel)))
                        foodHotel.setChecked(true);
                    else if (tecEntry.getTravelMode().equalsIgnoreCase(resources.getString(R.string.guesthouse)))
                        foodGuesthouse.setChecked(true);
                    else if (tecEntry.getTravelMode().equalsIgnoreCase(resources.getString(R.string.self)))
                        foodSelf.setChecked(true);
                } else {
                    foodGuesthouse.setChecked(true);
                    foodHotel.setEnabled(false);
                    foodSelf.setEnabled(false);
                }

            } else if (strClaimCategory.equalsIgnoreCase(claimCategoryList.get(2))) {
                layoutSiteConveyance.setVisibility(View.VISIBLE);
                actual.setText(String.valueOf(tecEntry.getUnitPrice()));
                siteFromDate.setText(tecEntry.getDepartureDate());
                siteToDate.setText(tecEntry.getArrivalDate());
                conveyanceDays.setText(String.valueOf(tecEntry.getTotalQuantitty()));
                description.setText(tecEntry.getDescription());
                siteBillAmount.setText(String.valueOf(tecEntry.getBillAmount()));

            } else if (strClaimCategory.equalsIgnoreCase(claimCategoryList.get(3)) || strClaimCategory.equalsIgnoreCase(claimCategoryList.get(4)) || strClaimCategory.equalsIgnoreCase(claimCategoryList.get(8)) || strClaimCategory.equalsIgnoreCase(claimCategoryList.get(5))) {
                mislleniousLaoyut.setVisibility(View.VISIBLE);
                billDate.setText(tecEntry.getDate());
                miscQuantity.setText(String.valueOf(tecEntry.getTotalQuantitty()));
                miscRate.setText(String.valueOf(tecEntry.getUnitPrice()));
                miscBillAmount.setText(String.valueOf(tecEntry.getBillAmount()));
                misleniousDescription.setText(tecEntry.getDescription());
                misVendor.setText(tecEntry.getPaidTo());
                misBillNum.setText(tecEntry.getBillNum());
                misGstin.setText(tecEntry.getGstin());

                adminViewMiscAttachment.setVisibility(View.VISIBLE);
                String fileName = tecEntry.getAttachmentPath();
                if (!fileName.isEmpty())
                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                miscEditAttachment.setText(fileName);
                miscEditAttachment.setTextColor(resources.getColor(R.color.colorPrimary));


            } else if (strClaimCategory.equalsIgnoreCase(claimCategoryList.get(7))) {
                layoutHotel.setVisibility(View.VISIBLE);
                hotelBillDate.setText(tecEntry.getDate());

                cityHotel.setText(tecEntry.getPaidTo());
                hotelGstin.setText(tecEntry.getGstin());
                hotelBillNum.setText(tecEntry.getBillNum());
                hotelBillNum.setText(tecEntry.getBillNum());
                hotelNights.setText(String.valueOf(tecEntry.getTotalQuantitty()));
                hotelRate.setText(String.valueOf(tecEntry.getUnitPrice()));
                hotelBillAmount.setText(String.valueOf(tecEntry.getBillAmount()));
                hotelStayFirst.setText(tecEntry.getDepartureDate());
                hotelStayLast.setText(tecEntry.getArrivalDate());
                adminViewHotelAttachment.setVisibility(View.VISIBLE);
                String fileName = tecEntry.getAttachmentPath();
                if (!fileName.isEmpty())
                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                hotelEditAttachment.setText(fileName);
                hotelEditAttachment.setTextColor(resources.getColor(R.color.colorPrimary));
                if (tecEntry.getPaidBy().equalsIgnoreCase(resources.getString(R.string.employee)))
                    hotelEmployee.setChecked(true);
                else
                    hotelAccount.setChecked(true);

            } else if (strClaimCategory.equalsIgnoreCase(claimCategoryList.get(6))) {
                fuelLayout.setVisibility(View.VISIBLE);
                fuelDate.setText(tecEntry.getDate());

                if (tecEntry.getTravelMode().equalsIgnoreCase(resources.getString(R.string.two_wheeler)))
                    typeVehicleSpinner.setSelection(0);
                else
                    typeVehicleSpinner.setSelection(1);

                mileage.setText(String.valueOf(tecEntry.getMileage()));
                fuelBillAmount.setText(String.valueOf(tecEntry.getBillAmount()));
                travelDistance.setText(String.valueOf(tecEntry.getKiloMeter()));


                if (createById == Integer.parseInt(userPref.getUserId())) {
                    userFuelLocation.setVisibility(View.VISIBLE);
                    if (!tecEntry.getFromLocation().equalsIgnoreCase(strBaseLocation)) {
                        otherFormFuelLayout.setVisibility(View.VISIBLE);
                        fromFuelSpinner.setSelection(1);
                        otherFormFuel.setText(tecEntry.getFromLocation());
                    }

                    if (!tecEntry.getToLocation().equalsIgnoreCase(strSiteLocation)) {
                        otherToFuelLayout.setVisibility(View.VISIBLE);
                        toFuelSpinner.setSelection(1);
                        otherToFuel.setText(tecEntry.getToLocation());
                    }
                } else {
                    adminFuelLocation.setVisibility(View.VISIBLE);
                    adminFuelFrom.setText(tecEntry.getFromLocation());
                    adminFuelTo.setText(tecEntry.getToLocation());
                }

            } else {
                attachmentLayout.setVisibility(View.VISIBLE);
                String fileName = tecEntry.getAttachmentPath();
                if (!fileName.isEmpty())
                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1);

                onlyEditAttachment.setText(fileName);
                onlyEditAttachment.setTextColor(resources.getColor(R.color.colorPrimary));
                adminViewOnlyAttachment.setVisibility(View.VISIBLE);
                attachmentDate.setText(tecEntry.getDate());
                attachmentDescription.setText(tecEntry.getDescription());
            }
        }
    }

    @OnClick({R.id.attachOnlyFile, R.id.foodAttachFile, R.id.intercityAttachFile, R.id.miscAttachFile, R.id.hotelAttachFile})
    protected void onViewFile() {
        Intent intent = new Intent(this, ProjectListActivity.class);
        intent.putExtra(ConstantVariable.MIX_ID.ACTION, resources.getString(R.string.tec));
        intent.putExtra(getResources().getString(R.string.project), projectName);
        intent.putExtra("file_url", tecEntry.getAttachmentPath());
        startActivity(intent);
    }

    @OnClick(R.id.mis_vendor)
    protected void onVendor() {
        String vendor_type = "Travel";

        if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.misc))) {
            vendor_type = "Miscellaneous";

        } else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.repair_maintenance))) {
            vendor_type = "Miscellaneous";
        } else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.intl_travel))) {
            vendor_type = "Admin";
        } else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.fix_asset))) {
            vendor_type = "Fixed Assets";
        }

        if (!strClaimCategory.equalsIgnoreCase(resources.getString(R.string.misc))) {
            Intent intent = new Intent(this, VendorActivity.class);
            intent.putExtra(ConstantVariable.MIX_ID.ACTION, resources.getString(R.string.vendor));
            intent.putExtra(ConstantVariable.Tec.ID, vendor_type);
            intent.putExtra(ConstantVariable.MIX_ID.TITLE, strClaimCategory);
            startActivityForResult(intent, VENDOR);
        } else {
            Intent intent = new Intent(this, MiscVendorActivity.class);
            intent.putExtra(ConstantVariable.MIX_ID.ACTION, resources.getString(R.string.vendor));
            intent.putExtra(ConstantVariable.Tec.ID, vendor_type);
            intent.putExtra(ConstantVariable.MIX_ID.TITLE, strClaimCategory);
            startActivityForResult(intent, VENDOR);
        }
    }


    @OnClick(R.id.cityHotel)
    protected void onCityHotel() {
        Intent intent = new Intent(this, VendorActivity.class);
        intent.putExtra(ConstantVariable.MIX_ID.ACTION, resources.getString(R.string.vendor));
        intent.putExtra(ConstantVariable.Tec.ID, "Hotel");
        intent.putExtra(ConstantVariable.MIX_ID.TITLE, strClaimCategory);
        startActivityForResult(intent, CITY_HOTEL);
    }


    @OnClick(R.id.intercity_vendor)
    protected void onCityVendor() {
        Intent intent = new Intent(this, VendorActivity.class);
        intent.putExtra(ConstantVariable.MIX_ID.ACTION, resources.getString(R.string.vendor));
        intent.putExtra(ConstantVariable.Tec.ID, "Travel");
        intent.putExtra(ConstantVariable.MIX_ID.TITLE, strClaimCategory);
        startActivityForResult(intent, INTERCITY_VENDOR);
    }


    @OnClick(R.id.submit)
    protected void onSubmit() {
        if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.intercity_travel))) {
            tecEntry.setTravelMode(travelModeSpinner.getSelectedItem().toString());
            String fromLocation = fromSpinner.getSelectedItem().toString();
            String strUserFrom = fromLocation.equalsIgnoreCase(resources.getString(R.string.other)) ? otherForm.getText().toString() : fromLocation;
            tecEntry.setFromLocation((tecEntry.getCreatedById() == Integer.parseInt(userPref.getUserId())) ? strUserFrom : tecEntry.getFromLocation());
            String toLocation = toSpinner.getSelectedItem().toString();
            String strUserToLocation = toLocation.equalsIgnoreCase(resources.getString(R.string.other)) ? otherTo.getText().toString() : toLocation;
            tecEntry.setToLocation((tecEntry.getCreatedById() == Integer.parseInt(userPref.getUserId())) ? strUserToLocation : tecEntry.getToLocation());
            tecEntry.setDepartureDate(DepartureDate.getText().toString().trim());
            tecEntry.setDepartureTime(DepartureTime.getText().toString().trim());
            tecEntry.setArrivalDate(arrivalDate.getText().toString().trim());
            tecEntry.setTotalQuantitty(1);
            tecEntry.setPaidTo(intercityVendor.getText().toString().trim());
            tecEntry.setArrivalTime(arrivalTime.getText().toString().trim());
            int selectedRadio = intercityPaidRadioGroup.getCheckedRadioButtonId();
            tecEntry.setDate(intercityBillingDate.getText().toString().trim());
            if (selectedRadio != -1) {
                tecEntry.setPaidBy(((RadioButton) findViewById(selectedRadio)).getText().toString());
            }

            String amount = billAmount.getText().toString().trim();
            if (!amount.isEmpty()) {
                tecEntry.setBillAmount(Double.parseDouble(amount));
            }
            tecEntry.setAttachmentPath(mFile != null ? mFile.getName() : tecEntry.getAttachmentPath());
            int id = isCreatedUser()?Integer.parseInt(userPref.getUserId()):tecEntry.getCreatedById();
            tecEntry.setPaidToId(id);

        } else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.per_diem))) {
            tecEntry.setDate(signPerDiemDate.getText().toString().trim());
            BookingMode bookingMode = (BookingMode) city.getSelectedItem();
            tecEntry.setLocation(bookingMode.getTitle().equalsIgnoreCase(resources.getString(R.string.other)) ? otherCity.getText().toString().trim() : bookingMode.getTitle());
            String strDays = days.getText().toString().trim();
            tecEntry.setTotalQuantitty(TextUtils.isEmpty(strDays) ? 0 : Double.parseDouble(strDays));
            String strNonWorkingDays = nonWorkingDays.getText().toString().trim();
            tecEntry.setNonWorkingDays(TextUtils.isEmpty(strNonWorkingDays) ? 0 : Integer.parseInt(strNonWorkingDays));
            String strRate = rate.getText().toString();
            tecEntry.setUnitPrice(TextUtils.isEmpty(strRate) ? 0 : Double.parseDouble(strRate));
            String strAmount = foodBillAmount.getText().toString();
            tecEntry.setBillAmount(TextUtils.isEmpty(strAmount) ? 0 : Double.parseDouble(strAmount));
            tecEntry.setDepartureDate(foodFromDate.getText().toString().trim());
            tecEntry.setArrivalDate(foodToDate.getText().toString().trim());
            tecEntry.setPaidBy(resources.getString(R.string.employee));
            tecEntry.setPaidTo(tecEntry.getPaidTo());
            tecEntry.setAttachmentPath(mFile != null ? mFile.getName() :tecEntry.getAttachmentPath());
            tecEntry.setIsBillable(foodBillable.isChecked() ? "Y" : "N");
            int id = isCreatedUser()?Integer.parseInt(userPref.getUserId()):tecEntry.getCreatedById();
            tecEntry.setPaidToId(id);

        } else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.local_travel))) {
            String strActuals = actual.getText().toString().trim();
            if (!strActuals.isEmpty())
                tecEntry.setUnitPrice(Double.parseDouble(strActuals));
            String strTotal = conveyanceDays.getText().toString().trim();
            if (!strTotal.isEmpty())
                tecEntry.setTotalQuantitty(Double.parseDouble(strTotal));
            tecEntry.setDescription(description.getText().toString().trim());
            String strAmount = siteBillAmount.getText().toString();
            if (!strAmount.isEmpty()) {
                tecEntry.setBillAmount(Double.parseDouble(strAmount));
            }
            tecEntry.setDepartureDate(siteFromDate.getText().toString().trim());
            tecEntry.setArrivalDate(siteToDate.getText().toString().trim());
            tecEntry.setPaidBy(resources.getString(R.string.employee));
            tecEntry.setPaidTo(tecEntry.getPaidTo());
            int id = isCreatedUser()?Integer.parseInt(userPref.getUserId()):tecEntry.getCreatedById();
            tecEntry.setPaidToId(id);

        } else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.misc)) || strClaimCategory.equalsIgnoreCase(resources.getString(R.string.repair_maintenance)) || strClaimCategory.equalsIgnoreCase(resources.getString(R.string.intl_travel)) || strClaimCategory.equalsIgnoreCase(resources.getString(R.string.fix_asset))) {
            tecEntry.setDate(billDate.getText().toString().trim());
            tecEntry.setDescription(misleniousDescription.getText().toString().trim());
            tecEntry.setPaidTo(misVendor.getText().toString().trim());
            tecEntry.setGstin(misGstin.getText().toString().trim());
            tecEntry.setBillNum(misBillNum.getText().toString().trim());
            tecEntry.setTotalQuantitty(1);

            String strAmount = miscBillAmount.getText().toString().trim();
            double amount = TextUtils.isEmpty(strAmount) ? 0 : Double.parseDouble(strAmount);
            tecEntry.setBillAmount(amount);
            tecEntry.setUnitPrice(amount);
            tecEntry.setAttachmentPath(mFile != null ? mFile.getName() : !tecEntry.getAttachmentPath().isEmpty() ? tecEntry.getAttachmentPath() : "");
            tecEntry.setPaidBy(resources.getString(R.string.employee));


        } else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.fuel_expense))) {
            tecEntry.setDate(fuelDate.getText().toString().trim());
            tecEntry.setTravelMode(typeVehicleSpinner.getSelectedItem().toString());
            String fromLocation = fromFuelSpinner.getSelectedItem().toString();
            String strUserFrom = fromLocation.equalsIgnoreCase(resources.getString(R.string.other)) ? otherFormFuel.getText().toString() : fromLocation;
            tecEntry.setFromLocation((tecEntry.getCreatedById() == Integer.parseInt(userPref.getUserId())) ? strUserFrom : tecEntry.getFromLocation());
            String toLocation = toFuelSpinner.getSelectedItem().toString();
            String strUserTo = toLocation.equalsIgnoreCase(resources.getString(R.string.other)) ? otherToFuel.getText().toString() : toLocation;
            tecEntry.setToLocation((tecEntry.getCreatedById() == Integer.parseInt(userPref.getUserId())) ? strUserTo : tecEntry.getToLocation());
            String strAmount = fuelBillAmount.getText().toString();
            double amount = TextUtils.isEmpty(strAmount) ? 0 : Double.parseDouble(strAmount);
            tecEntry.setBillAmount(amount);
            tecEntry.setUnitPrice(amount);

            String strTravel = travelDistance.getText().toString();
            tecEntry.setKiloMeter(TextUtils.isEmpty(strTravel) ? 0 : Double.parseDouble(strTravel));
            tecEntry.setPaidBy(resources.getString(R.string.employee));
            tecEntry.setPaidTo(tecEntry.getPaidTo());
            tecEntry.setMileage(Double.parseDouble(mileage.getText().toString().trim()));
            int id = isCreatedUser()?Integer.parseInt(userPref.getUserId()):tecEntry.getCreatedById();
            tecEntry.setPaidToId(id);

        } else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.lodging))) {
            tecEntry.setDate(hotelBillDate.getText().toString().trim());
            tecEntry.setPaidTo(cityHotel.getText().toString().trim());
            tecEntry.setDepartureDate(hotelStayFirst.getText().toString().trim());
            tecEntry.setArrivalDate(hotelStayLast.getText().toString().trim());
            tecEntry.setGstin(hotelGstin.getText().toString().trim());
            tecEntry.setBillNum(hotelBillNum.getText().toString().trim());
            String qty = hotelNights.getText().toString().trim();
            tecEntry.setTotalQuantitty(TextUtils.isEmpty(qty) ? 0 : Double.parseDouble(qty));
            String price = hotelRate.getText().toString().trim();
            if (!price.isEmpty())
                tecEntry.setUnitPrice(Double.parseDouble(price));

            int selectedRadio = hotelPaidRadioGroup.getCheckedRadioButtonId();
            if (selectedRadio != -1) {
                tecEntry.setPaidBy(((RadioButton) findViewById(selectedRadio)).getText().toString());
            }
            String strAmount = hotelBillAmount.getText().toString().trim();
            tecEntry.setBillAmount(strAmount.isEmpty() ? 0 : Double.parseDouble(strAmount));
            tecEntry.setAttachmentPath(mFile != null ? mFile.getName() : tecEntry.getAttachmentPath());
        } else {
            tecEntry.setAttachmentPath(mFile != null ? mFile.getName() : tecEntry.getAttachmentPath());
            tecEntry.setPaidTo(tecEntry.getPaidTo());
            tecEntry.setPaidBy(resources.getString(R.string.employee));
            int id = isCreatedUser()?Integer.parseInt(userPref.getUserId()):tecEntry.getCreatedById();
            tecEntry.setPaidToId(id);

        }
        tecEntry.setModifiedById(Integer.parseInt(userPref.getUserId()));
        tecEntry.setEntryCategory(strClaimCategory);

        if (validate()) {
            if (connection.isNetworkAvailable(this)) {
                dialog.showDialog();
                Map<String, RequestBody> myMap = new HashMap<>();
                if (mFile != null) {
                    String extension = mFile.getName().substring(mFile.getName().lastIndexOf(".") + 1);
                    RequestBody fileBody = RequestBody.create(MediaType.parse("application/" + extension), mFile);
                    myMap.put("file\"; filename=\"" + mFile.getName(), fileBody);
                }

                Gson gson = new Gson();
                String tecEntryJson = gson.toJson(tecEntry);
                RequestBody rbTecJSON = RequestBody.create(MediaType.parse("text/plain"),tecEntryJson);
                myMap.put(ConstantVariable.Tec.TEC_JSON, rbTecJSON);
                RequestBody action = RequestBody.create(MediaType.parse("text/plain"),ConstantVariable.MIX_ID.UPDATE);
                myMap.put(ConstantVariable.MIX_ID.ACTION,action);
                Call<StringResponse> call = api.approveDraftEntry(myMap);
                call.enqueue(new Callback<StringResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                        dialog.dismissDialog();
                        if (response.isSuccessful()) {

                            StringResponse addResponse = response.body();
                            if (addResponse != null) {
                                if (!addResponse.isError()) {
                                    tecEntry.setAttachmentPath(mFile != null ? ConstantVariable.BASE_TEC_URL + tecId + "_" + tecEntry.getId() + "_" + tecEntry.getEntryCategory() + ".pdf" : tecEntry.getAttachmentPath());
                                    startTecEntry(ConstantVariable.MIX_ID.UPDATE);
                                }
                                showMessage(addResponse.getMessage());
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
    }

    private boolean isCreatedUser() {
        return tecEntry.getCreatedById() == Integer.parseInt(userPref.getUserId());
    }

    private void startTecEntry(final String actionDone) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.putExtra(resources.getString(R.string.tec_entry), tecEntry);
                intent.putExtra(ConstantVariable.MIX_ID.ACTION,actionDone);
                intent.putExtra(ConstantVariable.MIX_ID.ID, position);
                intent.putExtra(ConstantVariable.MIX_ID.HEADER_POSITION, headerPosition);
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
        }, 500);
    }


    private boolean validate() {
        boolean valid = true;
        if (tecEntry.getEntryCategory().isEmpty()) {
            showMessage("Please select tec claim category");
            valid = false;
        } else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.intercity_travel))) {
            if (tecEntry.getFromLocation().isEmpty()) {
                showMessage("From location is required");
                valid = false;
            } else if (tecEntry.getToLocation().isEmpty()) {
                showMessage("To location is required");
                valid = false;
            } else if (tecEntry.getTravelMode().isEmpty()) {
                showMessage("Travel mode is required");
                valid = false;
            } else if (tecEntry.getDepartureDate().isEmpty()) {
                showMessage("Departure date is required");
                valid = false;
            } else if (tecEntry.getDepartureTime().isEmpty()) {
                showMessage("Departure time is required");
                valid = false;
            } else if (tecEntry.getArrivalDate().isEmpty()) {
                showMessage("Arrival date is required");
                valid = false;
            } else if (tecEntry.getArrivalTime().isEmpty()) {
                showMessage("Arrival time is required");
                valid = false;
            } else if (tecEntry.getPaidTo().isEmpty()) {
                showMessage("Vendor is required");
                valid = false;
            } else if (tecEntry.getPaidBy().isEmpty()) {
                showMessage("Please select paid by");
                valid = false;
            } else if (tecEntry.getBillAmount() <= 0) {
                showMessage("Bill amount is required");
                valid = false;
            } else if ( /*(userPref.getRoleId() == createById) && mFile == null*/tecEntry.getAttachmentPath().isEmpty() && tecEntry.getBillAmount() >= 100) {
                if (((tecEntry.getTravelMode().equalsIgnoreCase("Metro") && tecEntry.getBillAmount() <= 100) || (tecEntry.getTravelMode().equalsIgnoreCase("Auto") && tecEntry.getBillAmount() <= 150))) {
                    return true;
                } else {
                    showMessage("Please attach bill");
                    valid = false;
                }
            }
        } else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.per_diem))) {
            if (tecEntry.getLocation().isEmpty()) {
                showMessage("City is required");
                valid = false;
            } else if (tecEntry.getDepartureDate().isEmpty()) {
                showMessage("From date is required");
                valid = false;
            } else if (tecEntry.getArrivalDate().isEmpty()) {
                showMessage("To date is required");
                valid = false;
            } else if (tecEntry.getTotalQuantitty() <= 0) {
                showMessage("Days is required");
                valid = false;
            } else if (tecEntry.getUnitPrice() <= 0) {
                showMessage("Rate is required");
                valid = false;
            } else if (tecEntry.getPaidBy().isEmpty()) {
                showMessage("Please select paid by");
                valid = false;
            } else if (tecEntry.getBillAmount() <= 0) {
                showMessage("Bill amount is required");
                valid = false;
            } else if (/*(userPref.getRoleId() == createById) && mFile == null*/tecEntry.getAttachmentPath().isEmpty() && tecEntry.getBillAmount() >= 100 && !tecEntry.getEntryCategory().equalsIgnoreCase(claimCategoryList.get(1))) {
                showMessage("Please attach bill");
                valid = false;
            }

        } else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.local_travel))) {

            if (tecEntry.getDepartureDate().isEmpty()) {
                showMessage("From date is required");
                valid = false;
            } else if (tecEntry.getArrivalDate().isEmpty()) {
                showMessage("To date is required");
                valid = false;
            } else if (tecEntry.getTotalQuantitty() <= 0) {
                showMessage("Days is required");
                valid = false;
            } else if (tecEntry.getUnitPrice() == 0) {
                showMessage("Actual is required");
                valid = false;
            } else if (tecEntry.getDescription().isEmpty()) {
                showMessage("Description is required");
                valid = false;
            } else if (tecEntry.getDescription().length() < 30) {
                showMessage("Description must have 30 characters");
                valid = false;
            } else if (tecEntry.getPaidBy().isEmpty()) {
                showMessage("Please select paid by");
                valid = false;
            } else if (tecEntry.getBillAmount() <= 0) {
                showMessage("Bill amount is required");
                valid = false;
            } /*else if (mFile == null && tecEntry.getBillAmount() >= 100 && !tecEntry.getEntryCategory().equalsIgnoreCase(claimCategoryList.get(1))) {
                showMessage("Please attach bill");
                valid = false;
            }*/

        } else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.misc)) || strClaimCategory.equalsIgnoreCase(resources.getString(R.string.intl_travel)) || strClaimCategory.equalsIgnoreCase(resources.getString(R.string.repair_maintenance)) || strClaimCategory.equalsIgnoreCase(resources.getString(R.string.fix_asset))) {
            if (tecEntry.getDate().isEmpty()) {
                showMessage("Bill date is required");
                valid = false;
            } else if (tecEntry.getDescription().isEmpty()) {
                showMessage("Description is required");
                valid = false;
            } else if (tecEntry.getPaidTo().isEmpty()) {
                showMessage("Vendor is required");
                valid = false;
            } else if (tecEntry.getGstin().isEmpty()) {
                showMessage("GST number is required");
                valid = false;
            } else if (tecEntry.getBillNum().isEmpty()) {
                showMessage("Bill number is required");
                valid = false;
            } else if (tecEntry.getPaidBy().isEmpty()) {
                showMessage("Please select paid by");
                valid = false;
            } else if (tecEntry.getBillAmount() <= 0) {
                showMessage("Bill amount is required");
                valid = false;
            } else if (/*(userPref.getRoleId() == createById) && mFile == null*/tecEntry.getAttachmentPath().isEmpty() && tecEntry.getBillAmount() >= 100 && !tecEntry.getEntryCategory().equalsIgnoreCase(claimCategoryList.get(1))) {
                showMessage("Please attach bill");
                valid = false;
            }

        } else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.fuel_expense))) {
            if (tecEntry.getDate().isEmpty()) {
                showMessage("Date is required");
                valid = false;
            } else if (tecEntry.getTravelMode().isEmpty()) {
                showMessage("Travel mode is required");
                valid = false;
            } else if (tecEntry.getFromLocation().isEmpty()) {
                showMessage("From location is required");
                valid = false;
            } else if (tecEntry.getToLocation().isEmpty()) {
                showMessage("To location is required");
                valid = false;
            } else if (tecEntry.getKiloMeter() <= 0) {
                showMessage("Travel distance is required");
                valid = false;
            } else if (tecEntry.getMileage() <= 0) {
                showMessage("Mileage is required");
                valid = false;
            } else if (tecEntry.getBillAmount() <= 0) {
                showMessage("Bill amount is required");
                valid = false;
            }

        } else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.lodging))) {
            if (tecEntry.getDate().isEmpty()) {
                showMessage("Bill date is required");
                valid = false;
            } else if (tecEntry.getPaidTo().isEmpty()) {
                showMessage("Vendor is required");
                valid = false;
            } else if (tecEntry.getGstin().isEmpty()) {
                showMessage("GST number is required");
                valid = false;
            } else if (tecEntry.getBillNum().isEmpty()) {
                showMessage("Bill number is required");
                valid = false;
            } else if (tecEntry.getDepartureDate().isEmpty()) {
                showMessage("Check-in date is required");
                valid = false;
            } else if (tecEntry.getArrivalDate().isEmpty()) {
                showMessage("Check-out date is required");
                valid = false;
            } else if (tecEntry.getTotalQuantitty() <= 0) {
                showMessage("Night is required");
                valid = false;
            } else if (tecEntry.getUnitPrice() <= 0) {
                showMessage("Rate is required");
                valid = false;
            } else if (tecEntry.getPaidBy().isEmpty()) {
                showMessage("Please select paid by");
                valid = false;
            } else if (tecEntry.getBillAmount() <= 0) {
                showMessage("Bill amount is required");
                valid = false;
            } else if (/*(userPref.getRoleId() == createById) && mFile == null*/tecEntry.getAttachmentPath().isEmpty() && tecEntry.getBillAmount() >= 100 && !tecEntry.getEntryCategory().equalsIgnoreCase(claimCategoryList.get(1))) {
                showMessage("Please attach bill");
                valid = false;
            }
        } else {
            if (/*(userPref.getRoleId() == createById) && mFile == null*/ tecEntry.getAttachmentPath().isEmpty()) {
                showMessage("Please attach bill");
                valid = false;
            }
        }

        return valid;
    }


    @OnClick({R.id.intercityEditAttachment, R.id.hotelEditAttachment, R.id.foodEditAttachment, R.id.miscEditAttachment, R.id.onlyEditAttachment})
    protected void onAttachment() {
        if (createById == Integer.parseInt(userPref.getUserId())) {
            if (Permissions.hasPermissions(this, PERMISSIONS)) {
                showImageCaptureDialog();
            } else {
                ActivityCompat.requestPermissions(this, PERMISSIONS, RC_PERMISSIONS);
            }
        } else {
            showMessage("You can't update tec attachment");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean valid = true;
        if (requestCode == RC_PERMISSIONS && grantResults.length > 0) {
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_GRANTED)
                    valid = true;
                else {
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    valid = false;
                }
            }

            if (valid) {
                showImageCaptureDialog();
            }
        }
    }

    private void showImageCaptureDialog() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    takePhoto();
                } else if (items[item].equals("Choose from Library")) {
                    showDocDialog();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mFile = new File(getOutputMediaFile(),
                String.valueOf(System.currentTimeMillis()) + ".jpg");
        if (Build.VERSION.SDK_INT < 24) {
            mFileUri = Uri.fromFile(mFile);
        } else {
            mFileUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".my.package.name.provider", mFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mFileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, RC_CAPTURE);
    }


    private String getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Ess");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        return mediaStorageDir.getPath();
    }

    private void showDocDialog() {
        Intent intent = new Intent();
        intent.setType("*/*");
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent = Intent.createChooser(intent, "Select file");
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            String[] mimetypes = {"*/*"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
        }
        startActivityForResult(intent, RC_PICK);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VENDOR && resultCode == Activity.RESULT_OK && data != null) {
            String vendor = data.getStringExtra(ConstantVariable.MIX_ID.VENDOR);
            String gstNum = data.getStringExtra(ConstantVariable.Tec.GSTIN);
            misGstin.setText(gstNum.isEmpty() ? "NA" : gstNum);
            misVendor.setText(vendor);
            tecEntry.setPaidTo(vendor);
            tecEntry.setPaidToId(data.getIntExtra(ConstantVariable.MIX_ID.ID, 0));

        } else if (requestCode == CITY_HOTEL && resultCode == Activity.RESULT_OK && data != null) {
            String vendor = data.getStringExtra(ConstantVariable.MIX_ID.VENDOR);
            String location = data.getStringExtra(ConstantVariable.Tec.SITE_LOCATION);
            tecEntry.setPaidTo(vendor);
            tecEntry.setLocation(location);
            cityHotel.setText(vendor);
            tecEntry.setPaidToId(data.getIntExtra(ConstantVariable.MIX_ID.ID, 0));

        } else if (requestCode == INTERCITY_VENDOR && resultCode == Activity.RESULT_OK && data != null) {
            String vendor = data.getStringExtra(ConstantVariable.MIX_ID.VENDOR);
            intercityVendor.setText(vendor);
            tecEntry.setPaidTo(vendor);
            tecEntry.setPaidToId(data.getIntExtra(ConstantVariable.MIX_ID.ID, 0));

        } else if (requestCode == RC_PICK && resultCode == Activity.RESULT_OK && data != null) {
            TextView textView = getTextView();
            Uri mainPath = data.getData();
            if (mainPath != null) {
                String mimeType = FileNamePath.getMimeType(this, mainPath);
                if (mimeType.equalsIgnoreCase("pdf")) {
                    String path = FileNamePath.getPathFromUri(this, mainPath);
                    setupFile(path, textView);
                } else if (mimeType.equalsIgnoreCase("png") || mimeType.equalsIgnoreCase("jpg") || mimeType.equalsIgnoreCase("jpeg")) {
                    Intent intent = new Intent(this, CropImageActivity.class);
                    intent.putExtra("uri", mainPath.toString());
                    startActivityForResult(intent, RC_CONVERT);
                } else {
                    showMessage("Invalid File");
                }
            }

        } else if (requestCode == RC_CAPTURE && resultCode == Activity.RESULT_OK) {
            mFile = reduceFileSize(mFile);
            Intent intent = new Intent(this, CropImageActivity.class);
            intent.putExtra("uri", mFileUri.toString());
            startActivityForResult(intent, RC_CONVERT);

        } else if (requestCode == RC_CONVERT && resultCode == Activity.RESULT_OK && data != null) {

            TextView textView = getTextView();
            String path = data.getStringExtra("uri");
            if (path != null) {
                setupFile(path, textView);
            } else
                showMessage("Invalid File");
        }
    }

    private TextView getTextView() {
        TextView textView;
        if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.misc)) || strClaimCategory.equalsIgnoreCase(resources.getString(R.string.fix_asset)) || strClaimCategory.equalsIgnoreCase(resources.getString(R.string.intl_travel)) || strClaimCategory.equalsIgnoreCase(resources.getString(R.string.repair_maintenance)))
            textView = miscEditAttachment;

        else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.lodging)))
            textView = hotelEditAttachment;

        else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.intercity_travel)))
            textView = intercityEditAttachment;

        else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.per_diem)))
            textView = foodEditAttachment;
        else
            textView = onlyEditAttachment;
        return textView;
    }


    public File reduceFileSize(File file) {
        try {

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            FileInputStream inputStream = new FileInputStream(file);
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            final int REQUIRED_SIZE = 75;

            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            return file;

        } catch (Exception e) {
            return null;
        }
    }


    private void setupFile(String path, TextView attachment) {
        String extension = path.substring(path.lastIndexOf(".") + 1);
        if (!extension.equalsIgnoreCase("pdf")) {
            showMessage("Invalid file");
            attachment.setText(resources.getString(R.string.invalid_file));
            attachment.setTextColor(Color.RED);
        } else {
            mFile = new File(path);
            if (mFile.exists()) {
                long fileSize = mFile.length() / 1024;
                if (fileSize > 2048) {
                    showMessage("File Size error");
                    attachment.setTextColor(Color.RED);
                    attachment.setText(resources.getString(R.string.invalid_file_size));
                    mFile = null;
                } else {
                    attachment.setText(resources.getString(R.string.bill_attached));
                    attachment.setTextColor(resources.getColor(R.color.colorPrimary));
                    tecEntry.setAttachmentPath(mFile.getName());
                }
            } else {
                showMessage("Unknown file. Please move file internal storage");
            }
        }
    }


    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (view != null) {
            view.setPadding(2, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
            ((TextView) view).setTextColor(Color.BLACK);
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.mediumTextSize));

            switch (adapterView.getId()) {
                case R.id.fromSpinner:
                    String strFrom = fromSpinner.getSelectedItem().toString();
                    otherFormLayout.setVisibility(strFrom.equalsIgnoreCase(resources.getString(R.string.other)) ? View.VISIBLE : View.GONE);
                    break;

                case R.id.toSpinner:
                    String strTo = toSpinner.getSelectedItem().toString();
                    otherToLayout.setVisibility(strTo.equalsIgnoreCase(resources.getString(R.string.other)) ? View.VISIBLE : View.GONE);
                    break;

                case R.id.fromFuelSpinner:
                    String strFromFuel = fromFuelSpinner.getSelectedItem().toString();
                    otherFormFuelLayout.setVisibility(strFromFuel.equalsIgnoreCase(resources.getString(R.string.other)) ? View.VISIBLE : View.GONE);
                    break;

                case R.id.toFuelSpinner:
                    String strToFuel = toFuelSpinner.getSelectedItem().toString();
                    otherToFuelLayout.setVisibility(strToFuel.equalsIgnoreCase(resources.getString(R.string.other)) ? View.VISIBLE : View.GONE);
                    break;

                case R.id.typeVehicleSpinner:
                    String strVechicleType = typeVehicleSpinner.getSelectedItem().toString();
                    if (strVechicleType.equalsIgnoreCase("4 wheeler")) {
                        mileage.setText(resources.getString(R.string.four_wheeler));
                    } else {
                        mileage.setText(resources.getString(R.string.two_wheeler));
                    }
                    String mileageValue = mileage.getText().toString().trim();
                    String kiloMeterValue = travelDistance.getText().toString().trim();
                    calculateFuelBillAmount(kiloMeterValue.isEmpty() ? 0 : Double.parseDouble(kiloMeterValue), mileageValue);
                    break;

                case R.id.city:
                    BookingMode bookingMode = (BookingMode) city.getSelectedItem();
                    calculateFoodPerDiem(bookingMode.getValue());
                    if (bookingMode.getTitle().equalsIgnoreCase(resources.getString(R.string.other))) {
                        otherCityLayout.setVisibility(View.VISIBLE);
                    } else {
                        otherCityLayout.setVisibility(View.GONE);
                    }
                    break;

            }
        }
    }

    private void calculateFoodPerDiem(String value) {
        int selectedCityId = foodStayModeGroup.getCheckedRadioButtonId();
        if (selectedCityId != -1)
            tecEntry.setTravelMode(((RadioButton) findViewById(selectedCityId)).getText().toString());
        tecEntry.setCityMetroValue(value);

        String perDiemValue = "0";
        if (!TextUtils.isEmpty(tecEntry.getCityMetroValue()) && newTec.getIsInternational() == 0) {
            if (tecEntry.getTravelMode().equalsIgnoreCase(resources.getString(R.string.hotel)) && tecEntry.getCityMetroValue().equalsIgnoreCase(resources.getString(R.string.metro))) {
                perDiemValue = "450";
            } else if (tecEntry.getTravelMode().equalsIgnoreCase(resources.getString(R.string.hotel)) && !tecEntry.getCityMetroValue().equalsIgnoreCase(resources.getString(R.string.metro))) {
                perDiemValue = "350";
            } else if (tecEntry.getTravelMode().equalsIgnoreCase(resources.getString(R.string.guesthouse)) /*&& tecEntry.getCityMetroValue().equalsIgnoreCase(resources.getString(R.string.ncr))*/) {
                perDiemValue = "550";
            } else if (tecEntry.getTravelMode().equalsIgnoreCase(resources.getString(R.string.self)) && tecEntry.getCityMetroValue().equalsIgnoreCase(resources.getString(R.string.metro))) {
                perDiemValue = "900";
            } else if (tecEntry.getTravelMode().equalsIgnoreCase(resources.getString(R.string.self)) && !tecEntry.getCityMetroValue().equalsIgnoreCase(resources.getString(R.string.metro))) {
                perDiemValue = "800";
            }
            rate.setText(perDiemValue);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        BookingMode bookingMode = (BookingMode) city.getSelectedItem();
        calculateFoodPerDiem(bookingMode.getValue());
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

                case R.id.intercityBillingDate:
                    String strIntercityBillingDate = intercityBillingDate.getText().toString().trim();
                    if (!strIntercityBillingDate.isEmpty())
                        new CustomDate(DepartureDate, EditTecClaimActivity.this, strIntercityBillingDate, DateCal.getAddOnDate(strIntercityBillingDate, 7));
                    break;

                case R.id.DepartureDate:
                    String strDepartureDate = DepartureDate.getText().toString().trim();
                    String strBillingDate = intercityBillingDate.getText().toString().trim();
                    if (!strDepartureDate.isEmpty() && !strBillingDate.isEmpty()) {
                        new CustomDate(arrivalDate, EditTecClaimActivity.this, strDepartureDate, DateCal.getAddOnDate(strBillingDate, 7));
                    }
                    break;

                case R.id.hotelNights:
                    String name = hotelNights.getText().toString().trim();
                    if (!name.isEmpty()) {
                        tecEntry.setTotalQuantitty(Double.parseDouble(name));
                    } else {
                        tecEntry.setTotalQuantitty(0);
                    }
                    calculateBillAmount(tecEntry.getTotalQuantitty(), tecEntry.getUnitPrice(), view.getId());
                    break;

                case R.id.hotel_rate:
                    String add = hotelRate.getText().toString().trim();
                    if (!add.isEmpty()) {
                        tecEntry.setUnitPrice(Double.parseDouble(add));
                    } else {
                        tecEntry.setUnitPrice(0);
                    }
                    calculateBillAmount(tecEntry.getTotalQuantitty(), tecEntry.getUnitPrice(), view.getId());
                    break;

                case R.id.days:
                    String dist = days.getText().toString().trim();
                    if (!dist.isEmpty()) {
                        tecEntry.setTotalQuantitty(Double.parseDouble(dist));
                    } else {
                        tecEntry.setTotalQuantitty(0);
                    }
                    calculateBillAmount(tecEntry.getTotalQuantitty(), tecEntry.getUnitPrice(), view.getId());
                    break;

                case R.id.rate:
                    String state = rate.getText().toString().trim();
                    if (!state.isEmpty()) {
                        tecEntry.setUnitPrice(Double.parseDouble(state));
                    } else {
                        tecEntry.setUnitPrice(0);
                    }
                    calculateBillAmount(tecEntry.getTotalQuantitty(), tecEntry.getUnitPrice(), view.getId());
                    break;

                case R.id.conveyanceDays:
                    String lat = conveyanceDays.getText().toString().trim();
                    if (!lat.isEmpty()) {
                        tecEntry.setTotalQuantitty(Double.parseDouble(lat));
                    } else {
                        tecEntry.setTotalQuantitty(0);
                    }
                    calculateBillAmount(tecEntry.getTotalQuantitty(), tecEntry.getUnitPrice(), view.getId());
                    break;

                case R.id.actual:
                    String postal = actual.getText().toString().trim();
                    if (!postal.isEmpty()) {
                        tecEntry.setUnitPrice(Double.parseDouble(postal));
                    } else {
                        tecEntry.setUnitPrice(0);
                    }
                    calculateBillAmount(tecEntry.getTotalQuantitty(), tecEntry.getUnitPrice(), view.getId());
                    break;

                case R.id.travel_distance:
                    String strTravel = travelDistance.getText().toString().trim();
                    if (strTravel.isEmpty()) {
                        tecEntry.setKiloMeter(0);
                    } else {
                        tecEntry.setKiloMeter(Double.parseDouble(strTravel));
                    }
                    calculateFuelBillAmount(tecEntry.getKiloMeter(), mileage.getText().toString().trim());
                    break;

                case R.id.hotelStayFirst:
                    calculateNights();
                    String date = hotelStayFirst.getText().toString().trim();
                    if (!date.isEmpty())
                        new CustomDate(hotelStayLast, EditTecClaimActivity.this, date, null);
                    break;

                case R.id.hotelStayLast:
                    calculateNights();
                    break;

                case R.id.signPerDiemDate:
                    String strSignDate = signPerDiemDate.getText().toString().trim();
                    if (!strSignDate.isEmpty()) {
                        new CustomDate(foodFromDate, EditTecClaimActivity.this, strClaimDate, strSignDate);
                    }
                    break;

                case R.id.foodFromDate:
                    String strFoodFromDate = foodFromDate.getText().toString().trim();
                    String strSigDate = signPerDiemDate.getText().toString().trim();
                    if (!strFoodFromDate.isEmpty() && !strSigDate.isEmpty())
                        new CustomDate(foodToDate, EditTecClaimActivity.this, strFoodFromDate, strSigDate);
                    calculateFoodDays();
                    break;

                case R.id.foodToDate:
                    calculateFoodDays();
                    break;

                case R.id.non_working_days:
                    calculateFoodDays();
                    break;

                case R.id.siteFromDate:
                    String strSiteFromDate = siteToDate.getText().toString().trim();
                    if (!strSiteFromDate.isEmpty())
                        new CustomDate(siteToDate, EditTecClaimActivity.this, strSiteFromDate, null);
                    calculateSiteDays();
                    break;


                case R.id.siteToDate:
                    calculateSiteDays();
                    break;

                case R.id.miscQuantity:
                    String qty = miscQuantity.getText().toString().trim();
                    tecEntry.setTotalQuantitty(TextUtils.isEmpty(qty) ? 0 : Double.parseDouble(qty));
                    calculateMiscAmount();
                    break;

                case R.id.miscRate:
                    String strRate = miscRate.getText().toString().trim();
                    tecEntry.setUnitPrice(TextUtils.isEmpty(strRate) ? 0 : Double.parseDouble(strRate));
                    calculateMiscAmount();
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


    private void calculateMiscAmount() {
        tecEntry.setBillAmount(tecEntry.getTotalQuantitty() * tecEntry.getUnitPrice());
        miscBillAmount.setText(String.valueOf(tecEntry.getBillAmount()));

    }

    private void calculateFoodDays() {

        String strHotelFirst = foodFromDate.getText().toString().trim();
        String strHotelLast = foodToDate.getText().toString().trim();
        String strNonWorkingDays = nonWorkingDays.getText().toString().trim();
        if (!TextUtils.isEmpty(strHotelFirst) && !TextUtils.isEmpty(strHotelLast)) {
            double workingDays = DateCal.getSiteDays(strHotelFirst, strHotelLast);
            double nonWorkingDays = TextUtils.isEmpty(strNonWorkingDays) ? 0 : Double.parseDouble(strNonWorkingDays);
            days.setText(resources.getString(R.string.integer_value,(workingDays-nonWorkingDays)));
        }
    }

    private void calculateSiteDays() {
        String strHotelFirst = siteFromDate.getText().toString().trim();
        String strHotelLast = siteToDate.getText().toString().trim();
        if (!TextUtils.isEmpty(strHotelFirst) && !TextUtils.isEmpty(strHotelLast)) {
            conveyanceDays.setText(String.valueOf(DateCal.getSiteDays(strHotelFirst, strHotelLast)));
        }
    }


    private void calculateNights() {
        String strHotelFirst = hotelStayFirst.getText().toString().trim();
        String strHotelLast = hotelStayLast.getText().toString().trim();
        if (!TextUtils.isEmpty(strHotelFirst) && !TextUtils.isEmpty(strHotelLast)) {
            hotelNights.setText(String.valueOf(DateCal.getDays(strHotelFirst, strHotelLast)));
        }
    }


    private void calculateFuelBillAmount(double kiloMeter, String typeVehicle) {
        if (kiloMeter == 0) {
            fuelBillAmount.setText("0");
        } else {
            Double rate = Double.parseDouble(typeVehicle);
            String km = resources.getString(R.string.kilometer_value, kiloMeter - Math.floor(kiloMeter));
            int intKm = (int) kiloMeter;
            double totalAmount = intKm * rate + Double.parseDouble(km) * rate;
            fuelBillAmount.setText(String.valueOf(totalAmount));
        }
    }

    private void calculateBillAmount(double totalQuantity, double unitPrice, int viewId) {
        double amount = 0;
        if (totalQuantity > 0 && unitPrice > 0) {
            tecEntry.setBillAmount(totalQuantity * unitPrice);
            amount = totalQuantity * unitPrice;
        }

        if (amount > 0) {
            if (viewId == R.id.hotel_rate || viewId == R.id.hotelNights) {
                hotelBillAmount.setText(String.valueOf(amount));
            } else if (viewId == R.id.conveyanceDays || viewId == R.id.actual) {
                siteBillAmount.setText(String.valueOf(amount));
            } else if (viewId == R.id.days || viewId == R.id.rate) {
                foodBillAmount.setText(String.valueOf(amount));
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_attendance_leave, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_leave);
        menuItem.setTitle(resources.getString(R.string.delete));
        menuItem.setIcon(resources.getDrawable(R.drawable.ic_delete));
        menuItem.setVisible(action.equalsIgnoreCase(ConstantVariable.MIX_ID.UPDATE));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_leave) {
            showDeleteDialog(tecEntry.getId());
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    private void showDeleteDialog(final int tripId) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        builder.setTitle(resources.getString(R.string.delete));
        builder.setMessage(R.string.delete_tec_entry_message);
        builder.setCancelable(true);
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton(getResources().getString(R.string.proceed), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                onProceed(tripId);

            }
        });

        builder.show();

    }

    private void onProceed(int tecEntryId) {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<StringResponse> call = api.deleteTecEntry(tecEntryId, userPref.getUserId(),ConstantVariable.MIX_ID.DELETE);
            call.enqueue(new Callback<StringResponse>() {
                @Override
                public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        StringResponse assignProject = response.body();
                        if (assignProject != null) {
                            if (!assignProject.isError()){
                                startTecEntry(ConstantVariable.MIX_ID.DELETE);
                            }
                            showMessage(assignProject.getMessage());
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
