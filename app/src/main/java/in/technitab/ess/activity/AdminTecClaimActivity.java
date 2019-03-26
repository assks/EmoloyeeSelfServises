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
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.model.AddResponse;
import in.technitab.ess.model.NewTec;
import in.technitab.ess.model.NewTecEntry;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.CustomDate;
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

public class AdminTecClaimActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    @BindView(R.id.claimSpinner)
    Spinner claimSpinner;
    @BindView(R.id.travelModeSpinner)
    Spinner travelModeSpinner;
    @BindView(R.id.from)
    TextView from;
    @BindView(R.id.fromSpinner)
    Spinner fromSpinner;
    @BindView(R.id.toSpinner)
    Spinner toSpinner;
    @BindView(R.id.DepartureDate)
    EditText DepartureDate;
    @BindView(R.id.DepartureTime)
    EditText departureTime;
    @BindView(R.id.arrivalDate)
    EditText arrivalDate;
    @BindView(R.id.arrivalTime)
    EditText arrivalTime;
    @BindView(R.id.intercity_vendor)
    EditText intercityVendor;
    @BindView(R.id.bill_amount)
    EditText billAmount;
    @BindView(R.id.intercityAttachment)
    TextView intercityAttachment;
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
    @BindView(R.id.layoutHotel)
    LinearLayout layoutHotel;
    @BindView(R.id.city)
    EditText city;
    @BindView(R.id.days)
    EditText days;
    @BindView(R.id.rate)
    EditText rate;
    @BindView(R.id.attachment)
    TextView attachment;
    @BindView(R.id.layoutFoodBoardingIem)
    LinearLayout layoutFoodBoardingIem;
    @BindView(R.id.conveyanceDays)
    EditText conveyanceDays;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.actual)
    EditText actual;
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
    @BindView(R.id.mislleniousLaoyut)
    LinearLayout mislleniousLaoyut;
    @BindView(R.id.fuelDate)
    EditText fuelDate;
    @BindView(R.id.typeVehicleSpinner)
    Spinner typeVehicleSpinner;

    @BindView(R.id.fromFuelSpinner)
    Spinner fromFuelSpinner;
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
    @BindView(R.id.onlyAttachment)
    TextView onlyAttachment;
    @BindView(R.id.add)
    Button add;
    @BindView(R.id.otherForm)
    EditText otherForm;
    @BindView(R.id.otherFormLayout)
    TextInputLayout otherFormLayout;
    @BindView(R.id.otherTo)
    EditText otherTo;
    @BindView(R.id.otherToLayout)
    TextInputLayout otherToLayout;
    @BindView(R.id.otherFormFuel)
    EditText otherFormFuel;
    @BindView(R.id.otherFormFuelLayout)
    TextInputLayout otherFormFuelLayout;
    @BindView(R.id.otherToFuel)
    EditText otherToFuel;
    @BindView(R.id.otherToFuelLayout)
    TextInputLayout otherToFuelLayout;
    @BindView(R.id.food_bill_amount)
    EditText foodBillAmount;
    @BindView(R.id.hotel_bill_amount)
    EditText hotelBillAmount;
    @BindView(R.id.site_bill_amount)
    EditText siteBillAmount;
    @BindView(R.id.hotelAttachment)
    TextView hotelAttachment;
    @BindView(R.id.misc_bill_amount)
    EditText miscBillAmount;
    @BindView(R.id.misAttachment)
    TextView misAttachment;
    @BindView(R.id.hotelPaidRadioGroup)
    RadioGroup hotelPaidRadioGroup;
    @BindView(R.id.userLocation)
    RelativeLayout userLocation;
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
    @BindView(R.id.siteFromDate)
    EditText siteFromDate;
    @BindView(R.id.siteToDate)
    EditText siteToDate;

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
    private int tecId = 0;
    private String projectName = "";
    private String strBaseLocation = "";
    private String strClaimDate = "";
    private String strTravelDate = "";
    private File mFile = null;
    private NewTec newTec;
    NewTecEntry tecEntry;
    private Uri mFileUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tec_claim);
        ButterKnife.bind(this);

        init();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            newTec = bundle.getParcelable(resources.getString(R.string.tec));
            projectName = newTec.getProjectName();
            strBaseLocation = newTec.getBaseLocation();
            String strSiteLocation = newTec.getSiteLocation();
            strTravelDate = newTec.getClaimStartDate();
            tecEntry.setTecId(newTec.getTecId());
            setFromToSpinner(strSiteLocation, strBaseLocation);

        } else {
            finish();
        }
        setToolbar();
        setSpinner();
        setListener();

        new SetTime(departureTime, this);
        new SetTime(arrivalTime, this);
        new CustomDate(fuelDate, this, strClaimDate, null);
        new CustomDate(DepartureDate, this, strClaimDate, null);
        new CustomDate(arrivalDate, this, strClaimDate, null);
        new CustomDate(billDate, this, null, null);
        new CustomDate(fuelDate, this, strClaimDate, null);
        new CustomDate(hotelBillDate, this, strClaimDate, null);
        new CustomDate(hotelStayFirst, this, strClaimDate, null);
        new CustomDate(foodFromDate, this, strClaimDate, null);
        new CustomDate(foodToDate, this, strClaimDate, null);
        new CustomDate(siteFromDate, this, strClaimDate, null);
        new CustomDate(siteToDate, this, strClaimDate, null);

    }


    private void setFromToSpinner(String strSiteLocation, String strBaseLocation) {
        userLocation.setVisibility(View.VISIBLE);
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

    private void setListener() {
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
        foodFromDate.addTextChangedListener(new MyWatcher(foodFromDate));
        foodToDate.addTextChangedListener(new MyWatcher(foodToDate));
        siteFromDate.addTextChangedListener(new MyWatcher(siteFromDate));
        siteToDate.addTextChangedListener(new MyWatcher(siteToDate));
    }

    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(projectName);
            actionBar.setTitle(resources.getString(R.string.fill_tec));
            actionBar.setBackgroundDrawable(new ColorDrawable());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
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

    private void setSpinner() {
        claimCategoryList = Arrays.asList(getResources().getStringArray(R.array.tecCategoryAdminArray));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, claimCategoryList);
        claimSpinner.setAdapter(adapter);
        claimSpinner.setOnItemSelectedListener(this);

        List<String> travelModeList = Arrays.asList(resources.getStringArray(R.array.travelModeArray));
        ArrayAdapter<String> travelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, travelModeList);
        travelModeSpinner.setAdapter(travelAdapter);
        travelModeSpinner.setOnItemSelectedListener(this);

        List<String> vehicleList = Arrays.asList(resources.getStringArray(R.array.typeVehicleArray));
        ArrayAdapter<String> vehicleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, vehicleList);
        typeVehicleSpinner.setAdapter(vehicleAdapter);
        typeVehicleSpinner.setOnItemSelectedListener(this);
    }

    private void init() {
        connection = new NetConnection();
        resources = getResources();
        dialog = new Dialog(this);
        userPref = new UserPref(this);
        claimCategoryList = new ArrayList<>();
        api = APIClient.getClient().create(RestApi.class);
        tecEntry = new NewTecEntry();
    }

    @OnClick(R.id.add)
    protected void onSubmit() {
        if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.intercity_travel))) {
            tecEntry.setTravelMode(travelModeSpinner.getSelectedItem().toString());
            String fromLocation = fromSpinner.getSelectedItem().toString();
            tecEntry.setFromLocation(fromLocation.equalsIgnoreCase(resources.getString(R.string.other)) ? otherForm.getText().toString() : fromLocation);
            String toLocation = toSpinner.getSelectedItem().toString();
            tecEntry.setToLocation(toLocation.equalsIgnoreCase(resources.getString(R.string.other)) ? otherTo.getText().toString() : toLocation);
            tecEntry.setDepartureDate(DepartureDate.getText().toString().trim());
            tecEntry.setDepartureTime(departureTime.getText().toString().trim());
            tecEntry.setArrivalDate(arrivalDate.getText().toString().trim());
            tecEntry.setArrivalTime(arrivalTime.getText().toString().trim());
            int selectedRadio = intercityPaidRadioGroup.getCheckedRadioButtonId();

            if (selectedRadio != -1) {
                tecEntry.setPaidBy(((RadioButton) findViewById(selectedRadio)).getText().toString());
            }

            String amount = billAmount.getText().toString().trim();
            if (!amount.isEmpty()) {
                tecEntry.setBillAmount(Double.parseDouble(amount));
            }

            tecEntry.setPaidTo(intercityVendor.getText().toString().trim());
            tecEntry.setAttachmentPath(mFile != null ? mFile.getName() : "");


        } else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.per_diem))) {
            tecEntry.setLocation(city.getText().toString().trim());
            String strDays = days.getText().toString().trim();
            if (!strDays.isEmpty()) {
                tecEntry.setTotalQuantitty(Double.parseDouble(strDays));
            }

            String strRate = rate.getText().toString();
            if (!strRate.isEmpty())
                tecEntry.setUnitPrice(Double.parseDouble(strRate));

            String strAmount = foodBillAmount.getText().toString();
            if (!strAmount.isEmpty()) {
                tecEntry.setBillAmount(Double.parseDouble(strAmount));
            }

            tecEntry.setDepartureDate(foodFromDate.getText().toString().trim());
            tecEntry.setArrivalDate(foodToDate.getText().toString().trim());
            tecEntry.setPaidBy(resources.getString(R.string.account));
            tecEntry.setPaidTo(userPref.getName());
            tecEntry.setAttachmentPath(mFile != null ? mFile.getName() : "");

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
            tecEntry.setPaidBy(resources.getString(R.string.account));
            tecEntry.setPaidTo(userPref.getName());

        } else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.misc)) || strClaimCategory.equalsIgnoreCase(resources.getString(R.string.repair_maintenance)) || strClaimCategory.equalsIgnoreCase(resources.getString(R.string.intl_travel)) || strClaimCategory.equalsIgnoreCase(resources.getString(R.string.fix_asset))) {
            tecEntry.setDate(billDate.getText().toString().trim());
            tecEntry.setDescription(misleniousDescription.getText().toString().trim());
            tecEntry.setPaidTo(misVendor.getText().toString().trim());
            tecEntry.setGstin(misGstin.getText().toString().trim());
            tecEntry.setBillNum(misBillNum.getText().toString().trim());
            String strAmount = miscBillAmount.getText().toString().trim();
            if (!strAmount.isEmpty()) {
                tecEntry.setBillAmount(Double.parseDouble(strAmount));
            }

            tecEntry.setAttachmentPath(mFile != null ? mFile.getName() : "");
            tecEntry.setPaidBy(resources.getString(R.string.account));

        } else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.fuel_expense))) {
            tecEntry.setDate(fuelDate.getText().toString().trim());
            tecEntry.setTravelMode(typeVehicleSpinner.getSelectedItem().toString());
            String fromLocation = fromFuelSpinner.getSelectedItem().toString();
            tecEntry.setFromLocation(fromLocation.equalsIgnoreCase(resources.getString(R.string.other)) ? otherFormFuel.getText().toString() : fromLocation);
            String toLocation = toFuelSpinner.getSelectedItem().toString();
            tecEntry.setToLocation(toLocation.equalsIgnoreCase(resources.getString(R.string.other)) ? otherToFuel.getText().toString() : toLocation);
            String strAmount = fuelBillAmount.getText().toString();
            if (!strAmount.isEmpty()) {
                tecEntry.setBillAmount(Double.parseDouble(strAmount));
            }

            String strTravel = travelDistance.getText().toString();
            if (!strTravel.isEmpty()) {
                tecEntry.setKiloMeter(Double.parseDouble(strTravel));
            }
            tecEntry.setPaidBy(resources.getString(R.string.account));
            tecEntry.setPaidTo(userPref.getName());
            tecEntry.setMileage(Double.parseDouble(mileage.getText().toString().trim()));

        } else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.lodging))) {
            tecEntry.setDate(hotelBillDate.getText().toString().trim());
            String strCityHotel = cityHotel.getText().toString().trim();
            String[] city = strCityHotel.split(",");
            tecEntry.setLocation(city[0] != null ? city[0] : "");
            tecEntry.setGstin(hotelGstin.getText().toString().trim());
            tecEntry.setBillNum(hotelBillNum.getText().toString().trim());
            tecEntry.setGstin(hotelNights.getText().toString().trim());
            String price = hotelRate.getText().toString().trim();
            tecEntry.setDepartureDate(hotelStayFirst.getText().toString().trim());
            tecEntry.setArrivalDate(hotelStayLast.getText().toString().trim());

            if (!price.isEmpty())
                tecEntry.setUnitPrice(Double.parseDouble(price));

            int selectedRadio = hotelPaidRadioGroup.getCheckedRadioButtonId();
            if (selectedRadio != -1) {
                tecEntry.setPaidBy(((RadioButton) findViewById(selectedRadio)).getText().toString());
            }
            String strAmount = hotelBillAmount.getText().toString().trim();
            tecEntry.setBillAmount(strAmount.isEmpty() ? 0 : Double.parseDouble(strAmount));
            tecEntry.setAttachmentPath(mFile != null ? mFile.getName() : "");
        } else {
            tecEntry.setAttachmentPath(mFile != null ? mFile.getName() : "");
        }

        tecEntry.setEntryCategory(strClaimCategory);

        if (validate()) {
            proceed();
        }
    }


    private void proceed() {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Map<String, RequestBody> myMap = new HashMap<>();
            if (mFile != null) {
                String extension = mFile.getName().substring(mFile.getName().lastIndexOf(".") + 1);
                RequestBody fileBody = RequestBody.create(MediaType.parse("application/" + extension), mFile);
                myMap.put("file\"; filename=\"" + mFile.getName(), fileBody);
            }

            RequestBody rbTecId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(newTec.getTecId()));
            myMap.put(ConstantVariable.Tec.TEC_ID, rbTecId);
            RequestBody rbClaimDate = RequestBody.create(MediaType.parse("text/plain"), strClaimDate);
            myMap.put(ConstantVariable.Tec.CLAIM_DATE, rbClaimDate);
            RequestBody rbBaseLocation = RequestBody.create(MediaType.parse("text/plain"), strBaseLocation);
            myMap.put(ConstantVariable.Tec.BASE_LOCATION, rbBaseLocation);
            RequestBody rbTravelDate = RequestBody.create(MediaType.parse("text/plain"), strTravelDate);
            myMap.put(ConstantVariable.Tec.TRAVEL_DATE, rbTravelDate);
            RequestBody rbFromLocation = RequestBody.create(MediaType.parse("text/plain"), tecEntry.getFromLocation());
            myMap.put(ConstantVariable.Tec.FROM_LOCATION, rbFromLocation);

            RequestBody rbToLocation = RequestBody.create(MediaType.parse("text/plain"), tecEntry.getToLocation());
            myMap.put(ConstantVariable.Tec.TO_LOCATION, rbToLocation);

            RequestBody rbKilometer = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(tecEntry.getKiloMeter()));
            myMap.put(ConstantVariable.Tec.KILO_METER, rbKilometer);
            RequestBody rbMileage = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(tecEntry.getMileage()));
            myMap.put(ConstantVariable.Tec.MILEAGE, rbMileage);
            RequestBody rbEntryId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(newTec.getTecId()));
            myMap.put(ConstantVariable.Tec.ENTRY_ID, rbEntryId);
            RequestBody rbRoleId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userPref.getRoleId()));
            myMap.put(ConstantVariable.UserPrefVar.ROLE_ID, rbRoleId);
            RequestBody rbTravleMode = RequestBody.create(MediaType.parse("text/plain"), tecEntry.getTravelMode());
            myMap.put(ConstantVariable.Tec.TRAVEL_MODE, rbTravleMode);
            RequestBody rbDepartureDate = RequestBody.create(MediaType.parse("text/plain"), tecEntry.getDepartureDate());
            myMap.put(ConstantVariable.Tec.DEPARTURE_DATE, rbDepartureDate);
            RequestBody rbDepartureTime = RequestBody.create(MediaType.parse("text/plain"), tecEntry.getDepartureTime());
            myMap.put(ConstantVariable.Tec.DEPARTURE_TIME, rbDepartureTime);
            RequestBody rbArrivalTime = RequestBody.create(MediaType.parse("text/plain"), tecEntry.getArrivalTime());
            myMap.put(ConstantVariable.Tec.ARRIVAL_TIME, rbArrivalTime);
            RequestBody rbArrivalDate = RequestBody.create(MediaType.parse("text/plain"), tecEntry.getArrivalDate());
            myMap.put(ConstantVariable.Tec.ARRIVAL_DATE, rbArrivalDate);

            RequestBody rbEntryCategory = RequestBody.create(MediaType.parse("text/plain"), tecEntry.getEntryCategory());
            myMap.put(ConstantVariable.Tec.ENTRY_CATEGORY, rbEntryCategory);
            RequestBody rbLocation = RequestBody.create(MediaType.parse("text/plain"), tecEntry.getLocation());
            myMap.put(ConstantVariable.Tec.LOCATION, rbLocation);
            RequestBody rbUnitPrice = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(tecEntry.getUnitPrice()));
            myMap.put(ConstantVariable.Tec.UNIT_PRICE, rbUnitPrice);

            RequestBody rbTotalQty = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(tecEntry.getTotalQuantitty()));
            myMap.put(ConstantVariable.Tec.TOTAL_QUANTITTY, rbTotalQty);
            RequestBody rbDescription = RequestBody.create(MediaType.parse("text/plain"), tecEntry.getDescription());
            myMap.put(ConstantVariable.Tec.DESCRIPTION, rbDescription);
            RequestBody rbDate = RequestBody.create(MediaType.parse("text/plain"), tecEntry.getDate());
            myMap.put(ConstantVariable.Tec.DATE, rbDate);
            RequestBody rbPaidTo = RequestBody.create(MediaType.parse("text/plain"), tecEntry.getPaidTo());
            myMap.put(ConstantVariable.Tec.PAID_TO, rbPaidTo);

            RequestBody rbPaidBy = RequestBody.create(MediaType.parse("text/plain"), tecEntry.getPaidBy());
            myMap.put(ConstantVariable.Tec.PAID_BY, rbPaidBy);
            RequestBody rbGstin = RequestBody.create(MediaType.parse("text/plain"), tecEntry.getGstin());
            myMap.put(ConstantVariable.Tec.GSTIN, rbGstin);
            RequestBody rbAmount = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(tecEntry.getBillAmount()));
            myMap.put(ConstantVariable.Tec.BILL_AMOUNT, rbAmount);
            RequestBody rbBillNum = RequestBody.create(MediaType.parse("text/plain"), tecEntry.getBillNum());
            myMap.put(ConstantVariable.Tec.BILL_NUM, rbBillNum);
            RequestBody rbCreatedById = RequestBody.create(MediaType.parse("text/plain"), userPref.getUserId());
            myMap.put(ConstantVariable.Tec.CREATED_BY_ID, rbCreatedById);


            Call<AddResponse> call = api.saveTECEntry(myMap);
            call.enqueue(new Callback<AddResponse>() {
                @Override
                public void onResponse(@NonNull Call<AddResponse> call, @NonNull Response<AddResponse> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        AddResponse addResponse = response.body();
                        if (addResponse != null) {
                            if (!addResponse.isError()) {
                                tecId = addResponse.getTecId();
                                if (tecId != 0) {
                                    Intent intent = new Intent();
                                    intent.putExtra(ConstantVariable.Tec.TEC_ID, addResponse.getTecId());
                                    setResult(Activity.RESULT_OK, intent);
                                    finish();
                                }
                            }
                            showMessage(addResponse.getMessage());
                        }
                    } else {
                        showMessage("Not saved");
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
        } else {
            showMessage(resources.getString(R.string.internet_not_available));
        }

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
            } else if (mFile == null && tecEntry.getBillAmount() >= 100) {
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
            } else if (mFile == null && tecEntry.getBillAmount() >= 100) {
                showMessage("Please attach service timesheet");
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
            } else if (tecEntry.getUnitPrice() <= 0) {
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
            } else if (mFile == null && tecEntry.getBillAmount() >= 100 && !tecEntry.getEntryCategory().equalsIgnoreCase(claimCategoryList.get(1))) {
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
            } else if (tecEntry.getLocation().isEmpty()) {
                showMessage("City/hotel is required");
                valid = false;
            } else if (tecEntry.getGstin().isEmpty()) {
                showMessage("GST number is required");
                valid = false;
            } else if (tecEntry.getDepartureDate().isEmpty()) {
                showMessage("Check-in date is required");
                valid = false;
            } else if (tecEntry.getArrivalDate().isEmpty()) {
                showMessage("Check-out date is required");
                valid = false;
            } else if (tecEntry.getBillNum().isEmpty()) {
                showMessage("Bill number is required");
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
            } else if (mFile == null && tecEntry.getBillAmount() >= 100 && !tecEntry.getEntryCategory().equalsIgnoreCase(claimCategoryList.get(1))) {
                showMessage("Please attach bill");
                valid = false;
            }
        } else {
            if (mFile == null) {
                showMessage("Please attach bill");
                valid = false;
            }
        }

        return valid;
    }

    @OnClick({R.id.attachment, R.id.intercityAttachment, R.id.hotelAttachment, R.id.misAttachment, R.id.onlyAttachment})
    protected void onAttachment() {
        if (Permissions.hasPermissions(this, PERMISSIONS)) {
            showImageCaptureDialog();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, RC_PERMISSIONS);
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
            misVendor.setText(vendor);
            tecEntry.setPaidTo(vendor);
            misGstin.setText(gstNum.isEmpty() ? "NA" : gstNum);

        } else if (requestCode == CITY_HOTEL && resultCode == Activity.RESULT_OK && data != null) {
            String vendor = data.getStringExtra(ConstantVariable.MIX_ID.VENDOR);
            String location = data.getStringExtra(ConstantVariable.Tec.SITE_LOCATION);
            tecEntry.setPaidTo(vendor);
            tecEntry.setLocation(resources.getString(R.string.city_hotel_value, location, vendor));
            cityHotel.setText(resources.getString(R.string.city_hotel_value, location, vendor));

        } else if (requestCode == INTERCITY_VENDOR && resultCode == Activity.RESULT_OK && data != null) {
            String vendor = data.getStringExtra(ConstantVariable.MIX_ID.VENDOR);
            intercityVendor.setText(vendor);
            tecEntry.setPaidTo(vendor);
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
                    showMessage("Invalid file");
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
                String extension = path.substring(path.lastIndexOf(".") + 1);
                if (extension.equalsIgnoreCase("pdf")) {
                    setupFile(path, textView);
                }
            } else
                showMessage("Unknown Path. Please move file into internal storage");
        }
    }

    private TextView getTextView(){
        TextView textView;
        if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.misc)))
            textView = misAttachment;

        else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.lodging)))
            textView = hotelAttachment;

        else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.intercity_travel)))
            textView = intercityAttachment;

        else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.per_diem)))
            textView = attachment;
        else
            textView = onlyAttachment;
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
                showMessage("Unknown Path. Please move file internal storage");
                mFile = null;
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

                case R.id.claimSpinner:
                    strClaimCategory = claimSpinner.getSelectedItem().toString();
                    switch (strClaimCategory) {
                        case "Intercity Travel cost":
                            intercityTravelCost.setVisibility(View.VISIBLE);
                            layoutHotel.setVisibility(View.GONE);
                            layoutFoodBoardingIem.setVisibility(View.GONE);
                            mislleniousLaoyut.setVisibility(View.GONE);
                            layoutSiteConveyance.setVisibility(View.GONE);
                            fuelLayout.setVisibility(View.GONE);
                            onlyAttachment.setVisibility(View.GONE);
                            intercityAttachment.setVisibility(View.VISIBLE);
                            intercityAttachment.setText(resources.getString(R.string.attachment));
                            intercityAttachment.setTextColor(resources.getColor(R.color.colorPrimaryText));
                            mFile = null;
                            intercityVendor.setText("");
                            tecEntry.setAttachmentPath("");

                            break;

                        case "Food - Boarding - Per Diem":
                            intercityTravelCost.setVisibility(View.GONE);
                            layoutHotel.setVisibility(View.GONE);
                            layoutFoodBoardingIem.setVisibility(View.VISIBLE);
                            mislleniousLaoyut.setVisibility(View.GONE);
                            layoutSiteConveyance.setVisibility(View.GONE);
                            fuelLayout.setVisibility(View.GONE);
                            onlyAttachment.setVisibility(View.GONE);
                            attachment.setText(resources.getString(R.string.attachment));
                            attachment.setVisibility(View.VISIBLE);
                            attachment.setTextColor(resources.getColor(R.color.colorPrimaryText));
                            mFile = null;
                            tecEntry.setAttachmentPath("");
                            break;

                        case "Local Travel - Public transport":
                            intercityTravelCost.setVisibility(View.GONE);
                            layoutHotel.setVisibility(View.GONE);
                            layoutFoodBoardingIem.setVisibility(View.GONE);
                            mislleniousLaoyut.setVisibility(View.GONE);
                            layoutSiteConveyance.setVisibility(View.VISIBLE);
                            fuelLayout.setVisibility(View.GONE);
                            onlyAttachment.setVisibility(View.GONE);
                            mFile = null;
                            tecEntry.setAttachmentPath("");
                            break;

                        case "Miscellaneous":
                            intercityTravelCost.setVisibility(View.GONE);
                            layoutHotel.setVisibility(View.GONE);
                            layoutFoodBoardingIem.setVisibility(View.GONE);
                            mislleniousLaoyut.setVisibility(View.VISIBLE);
                            layoutSiteConveyance.setVisibility(View.GONE);
                            fuelLayout.setVisibility(View.GONE);
                            onlyAttachment.setVisibility(View.GONE);

                            misAttachment.setText(resources.getString(R.string.attachment));
                            misAttachment.setVisibility(View.VISIBLE);
                            misAttachment.setTextColor(resources.getColor(R.color.colorPrimaryText));
                            mFile = null;
                            tecEntry.setAttachmentPath("");
                            misVendor.setText("");
                            break;

                        case "Repairs and Maintenance":
                            intercityTravelCost.setVisibility(View.GONE);
                            layoutHotel.setVisibility(View.GONE);
                            layoutFoodBoardingIem.setVisibility(View.GONE);
                            mislleniousLaoyut.setVisibility(View.VISIBLE);
                            layoutSiteConveyance.setVisibility(View.GONE);
                            fuelLayout.setVisibility(View.GONE);
                            onlyAttachment.setVisibility(View.GONE);

                            misVendor.setText("");
                            misAttachment.setText(resources.getString(R.string.attachment));
                            misAttachment.setVisibility(View.VISIBLE);
                            misAttachment.setTextColor(resources.getColor(R.color.colorPrimaryText));
                            mFile = null;
                            tecEntry.setAttachmentPath("");
                            break;


                        case "Intl Travel Insurance":
                            intercityTravelCost.setVisibility(View.GONE);
                            layoutHotel.setVisibility(View.GONE);
                            layoutFoodBoardingIem.setVisibility(View.GONE);
                            mislleniousLaoyut.setVisibility(View.VISIBLE);
                            layoutSiteConveyance.setVisibility(View.GONE);
                            fuelLayout.setVisibility(View.GONE);
                            onlyAttachment.setVisibility(View.GONE);

                            misVendor.setText("");
                            misAttachment.setText(resources.getString(R.string.attachment));
                            misAttachment.setVisibility(View.VISIBLE);
                            misAttachment.setTextColor(resources.getColor(R.color.colorPrimaryText));
                            mFile = null;
                            tecEntry.setAttachmentPath("");
                            break;


                        case "Fuel/Mileage Expenses - Own transport":
                            intercityTravelCost.setVisibility(View.GONE);
                            layoutHotel.setVisibility(View.GONE);
                            layoutFoodBoardingIem.setVisibility(View.GONE);
                            mislleniousLaoyut.setVisibility(View.GONE);
                            layoutSiteConveyance.setVisibility(View.GONE);
                            fuelLayout.setVisibility(View.VISIBLE);
                            onlyAttachment.setVisibility(View.GONE);

                            mFile = null;
                            tecEntry.setAttachmentPath("");
                            break;


                        case "Lodging - Hotels":
                            intercityTravelCost.setVisibility(View.GONE);
                            layoutHotel.setVisibility(View.VISIBLE);
                            layoutFoodBoardingIem.setVisibility(View.GONE);
                            mislleniousLaoyut.setVisibility(View.GONE);
                            layoutSiteConveyance.setVisibility(View.GONE);
                            fuelLayout.setVisibility(View.GONE);
                            onlyAttachment.setVisibility(View.GONE);

                            cityHotel.setText("");
                            hotelAttachment.setText(resources.getString(R.string.attachment));
                            hotelAttachment.setVisibility(View.VISIBLE);
                            hotelAttachment.setTextColor(resources.getColor(R.color.colorPrimaryText));
                            mFile = null;
                            tecEntry.setAttachmentPath("");

                            break;


                        case "Fixed Asset":
                            intercityTravelCost.setVisibility(View.GONE);
                            layoutHotel.setVisibility(View.GONE);
                            layoutFoodBoardingIem.setVisibility(View.GONE);
                            mislleniousLaoyut.setVisibility(View.VISIBLE);
                            layoutSiteConveyance.setVisibility(View.GONE);
                            fuelLayout.setVisibility(View.GONE);
                            onlyAttachment.setVisibility(View.GONE);

                            misVendor.setText("");
                            misAttachment.setText(resources.getString(R.string.attachment));
                            misAttachment.setVisibility(View.VISIBLE);
                            misAttachment.setTextColor(resources.getColor(R.color.colorPrimaryText));
                            mFile = null;
                            tecEntry.setAttachmentPath("");

                            break;

                        default:
                            intercityTravelCost.setVisibility(View.GONE);
                            layoutHotel.setVisibility(View.GONE);
                            layoutFoodBoardingIem.setVisibility(View.GONE);
                            mislleniousLaoyut.setVisibility(View.GONE);
                            layoutSiteConveyance.setVisibility(View.GONE);
                            fuelLayout.setVisibility(View.GONE);
                            onlyAttachment.setVisibility(View.VISIBLE);

                            onlyAttachment.setText(resources.getString(R.string.attachment));
                            onlyAttachment.setVisibility(View.VISIBLE);
                            onlyAttachment.setTextColor(resources.getColor(R.color.colorPrimaryText));
                            mFile = null;
                            tecEntry.setAttachmentPath("");

                    }

                    break;

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
                case R.id.DepartureDate:
                    String strDepartureDate = DepartureDate.getText().toString().trim();
                    if (!strDepartureDate.isEmpty())
                        new CustomDate(arrivalDate, AdminTecClaimActivity.this, strDepartureDate, null);
                    break;
                case R.id.hotelNights:
                    String name = s.toString().trim();
                    if (!name.isEmpty()) {
                        tecEntry.setTotalQuantitty(Double.parseDouble(name));
                        String rate = hotelRate.getText().toString();
                        tecEntry.setUnitPrice(rate.isEmpty() ? 0 : Double.parseDouble(rate));
                    } else {
                        tecEntry.setTotalQuantitty(0);
                    }
                    calculateBillAmount(tecEntry.getTotalQuantitty(), tecEntry.getUnitPrice(), view.getId());
                    break;

                case R.id.hotel_rate:
                    String add = s.toString().trim();
                    if (!add.isEmpty()) {
                        tecEntry.setUnitPrice(Double.parseDouble(add));
                        String nights = hotelNights.getText().toString();
                        tecEntry.setTotalQuantitty(nights.isEmpty() ? 0 : Double.parseDouble(nights));
                    } else {
                        tecEntry.setUnitPrice(0);
                    }
                    calculateBillAmount(tecEntry.getTotalQuantitty(), tecEntry.getUnitPrice(), view.getId());
                    break;

                case R.id.days:
                    String dist = s.toString().trim();
                    if (!dist.isEmpty()) {
                        tecEntry.setTotalQuantitty(Double.parseDouble(dist));
                        String strRate = rate.getText().toString();
                        tecEntry.setUnitPrice(strRate.isEmpty() ? 0 : Double.parseDouble(strRate));
                    } else {
                        tecEntry.setTotalQuantitty(0);
                    }
                    calculateBillAmount(tecEntry.getTotalQuantitty(), tecEntry.getUnitPrice(), view.getId());
                    break;

                case R.id.rate:
                    String state = s.toString().trim();
                    if (!state.isEmpty()) {
                        tecEntry.setUnitPrice(Double.parseDouble(state));
                        String strDays = days.getText().toString();
                        tecEntry.setTotalQuantitty(strDays.isEmpty() ? 0 : Double.parseDouble(strDays));

                    } else {
                        tecEntry.setUnitPrice(0);
                    }
                    calculateBillAmount(tecEntry.getTotalQuantitty(), tecEntry.getUnitPrice(), view.getId());
                    break;

                case R.id.conveyanceDays:
                    String lat = s.toString().trim();
                    if (!lat.isEmpty()) {
                        tecEntry.setTotalQuantitty(Double.parseDouble(lat));
                        String strRate = actual.getText().toString();
                        tecEntry.setUnitPrice(strRate.isEmpty() ? 0 : Double.parseDouble(strRate));

                    } else {
                        tecEntry.setTotalQuantitty(0);
                    }
                    calculateBillAmount(tecEntry.getTotalQuantitty(), tecEntry.getUnitPrice(), view.getId());
                    break;

                case R.id.actual:
                    String postal = s.toString().trim();
                    if (!postal.isEmpty()) {
                        tecEntry.setUnitPrice(Double.parseDouble(postal));
                        String strDays = conveyanceDays.getText().toString();
                        tecEntry.setTotalQuantitty(strDays.isEmpty() ? 0 : Double.parseDouble(strDays));
                    } else {
                        tecEntry.setUnitPrice(0);
                    }
                    calculateBillAmount(tecEntry.getTotalQuantitty(), tecEntry.getUnitPrice(), view.getId());
                    break;

                case R.id.travel_distance:
                    String strTravel = s.toString().trim();
                    if (strTravel.isEmpty()) {
                        tecEntry.setKiloMeter(0);
                    } else {
                        tecEntry.setKiloMeter(Double.parseDouble(strTravel));
                    }
                    calculateFuelBillAmount(tecEntry.getKiloMeter(), mileage.getText().toString().trim());
                    break;

                case R.id.hotelStayFirst:
                    String date = hotelStayFirst.getText().toString().trim();
                    if (!date.isEmpty())
                        new CustomDate(hotelStayLast, AdminTecClaimActivity.this, date, null);
                    calculateNights();
                    break;


                case R.id.hotelStayLast:
                    calculateNights();
                    break;

                case R.id.foodFromDate:
                    String strFoodFromDate = foodFromDate.getText().toString().trim();
                    if (!strFoodFromDate.isEmpty())
                        new CustomDate(foodToDate, AdminTecClaimActivity.this, strFoodFromDate, null);
                    calculateFoodDays();
                    break;


                case R.id.foodToDate:
                    calculateFoodDays();
                    break;

                case R.id.siteFromDate:
                    String strSiteFromDate = siteToDate.getText().toString().trim();
                    if (!strSiteFromDate.isEmpty())
                        new CustomDate(siteToDate, AdminTecClaimActivity.this, strSiteFromDate, null);
                    calculateSiteDays();
                    break;


                case R.id.siteToDate:
                    calculateSiteDays();
                    break;

            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    }

    private void calculateFoodDays() {
        String strHotelFirst = foodFromDate.getText().toString().trim();
        String strHotelLast = foodToDate.getText().toString().trim();
        if (strHotelFirst.isEmpty() || strHotelLast.isEmpty()) {
            days.setText(String.valueOf(0));
        } else {
            days.setText(String.valueOf(DateCal.getSiteDays(strHotelFirst, strHotelLast)));
        }
    }

    private void calculateSiteDays() {
        String strHotelFirst = siteFromDate.getText().toString().trim();
        String strHotelLast = siteToDate.getText().toString().trim();
        if (strHotelFirst.isEmpty() || strHotelLast.isEmpty()) {
            conveyanceDays.setText(String.valueOf(0));
        } else {
            conveyanceDays.setText(String.valueOf(DateCal.getSiteDays(strHotelFirst, strHotelLast)));
        }
    }

    private void calculateNights() {
        String strHotelFirst = hotelStayFirst.getText().toString().trim();
        String strHotelLast = hotelStayLast.getText().toString().trim();
        if (strHotelFirst.isEmpty() || strHotelLast.isEmpty()) {
            hotelNights.setText(String.valueOf(0));
        } else {
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
        double amount;
        if (totalQuantity == 0 || unitPrice == 0) {
            tecEntry.setBillAmount(0);
            amount = 0;
        } else {
            tecEntry.setBillAmount(totalQuantity * unitPrice);
            amount = totalQuantity * unitPrice;
        }

        if (viewId == R.id.hotel_rate || viewId == R.id.hotelNights) {
            hotelBillAmount.setText(String.valueOf(amount));
        } else if (viewId == R.id.conveyanceDays || viewId == R.id.actual) {
            siteBillAmount.setText(String.valueOf(amount));
        } else if (viewId == R.id.days || viewId == R.id.rate) {
            foodBillAmount.setText(String.valueOf(amount));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
