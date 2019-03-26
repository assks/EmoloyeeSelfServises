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
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import in.technitab.ess.model.NewTec;
import in.technitab.ess.model.NewTecEntry;
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

public class TecClaimActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {


    private static final String TAG = TecClaimActivity.class.getSimpleName();
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
    @BindView(R.id.intercityBillingDate)
    EditText intercityBillingDate;
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
    Spinner city;
    @BindView(R.id.otherCityLayout)
    TextInputLayout otherCityLayout;
    @BindView(R.id.otherCity)
    EditText otherCity;
    @BindView(R.id.foodStayModeGroup)
    RadioGroup foodStayModeGroup;
    @BindView(R.id.foodBillable)
    CheckBox foodBillable;
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
    @BindView(R.id.non_working_days)
    EditText nonWorkingDays;
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
    @BindView(R.id.attachmentDate)
    EditText attachmentDate;
    @BindView(R.id.attachmentDescription)
    EditText attachmentDescription;
    @BindView(R.id.attachmentLayout)
    LinearLayout attachmentLayout;
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
    @BindView(R.id.miscQuantity)
    EditText miscQuantity;
    @BindView(R.id.miscRate)
    EditText miscRate;
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
    @BindView(R.id.signPerDiemDate)
    CustomEditText signPerDiemDate;
    @BindView(R.id.siteFromDate)
    EditText siteFromDate;
    @BindView(R.id.siteToDate)
    EditText siteToDate;

    String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @BindView(R.id.foodHotel)
    RadioButton foodHotel;
    @BindView(R.id.foodGuesthouse)
    RadioButton foodGuesthouse;
    @BindView(R.id.foodSelf)
    RadioButton foodSelf;
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
    private String projectName = "", strBaseLocation = "", strSiteLocation = "", strClaimDate = "", strTravelDate = "";
    private File mFile = null;
    private NewTec newTec;
    NewTecEntry tecEntry;
    private Uri mFileUri;

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
            strSiteLocation = newTec.getSiteLocation();
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
        new CustomDate(intercityBillingDate, this, strClaimDate, null);
        new CustomDate(attachmentDate, this, strClaimDate, null);
        new CustomDate(fuelDate, this, strClaimDate, null);
        new CustomDate(arrivalDate, this, strClaimDate, null);
        new CustomDate(billDate, this, null, null);
        new CustomDate(fuelDate, this, strClaimDate, null);
        new CustomDate(hotelBillDate, this, strClaimDate, null);
        new CustomDate(hotelStayFirst, this, strClaimDate, null);
        new CustomDate(signPerDiemDate, this, strClaimDate, null);
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
        miscQuantity.addTextChangedListener(new MyWatcher(miscQuantity));
        miscRate.addTextChangedListener(new MyWatcher(miscRate));
        siteFromDate.addTextChangedListener(new MyWatcher(siteFromDate));
        siteToDate.addTextChangedListener(new MyWatcher(siteToDate));
        nonWorkingDays.addTextChangedListener(new MyWatcher(nonWorkingDays));
        foodStayModeGroup.setOnCheckedChangeListener(this);
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

    private boolean isClaimStartDate(String claimStartDate) {
        boolean valid = true;
        String strClaimEndDate = "2018-11-01";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date d2 = simpleDateFormat.parse(claimStartDate);
            Date d1 = simpleDateFormat.parse(strClaimEndDate);
            valid = d2.before(d1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return valid;
    }

    private void setSpinner() {

        if (isClaimStartDate(strTravelDate))
            claimCategoryList = Arrays.asList(getResources().getStringArray(R.array.tecCategoryArray));
        else
            claimCategoryList = Arrays.asList(getResources().getStringArray(R.array.newTecCategoryArray));

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

        List<Object> foodMetroCities = new ArrayList<>();
        ArrayList<BookingMode> list = ConstantValues.getPerDiemCity();
        foodMetroCities.addAll(list);
        SpinAdapter spinAdapter = new SpinAdapter(this, android.R.layout.simple_list_item_1, foodMetroCities);
        city.setAdapter(spinAdapter);
        city.setOnItemSelectedListener(this);


        if (newTec.getIsInternational() == 1){
            foodHotel.setEnabled(false);
            foodGuesthouse.setChecked(true);
            foodSelf.setEnabled(false);
        }
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
            tecEntry.setTotalQuantitty(1);
            tecEntry.setDate(intercityBillingDate.getText().toString().trim());
            int selectedRadio = intercityPaidRadioGroup.getCheckedRadioButtonId();
            if (selectedRadio != -1) {
                tecEntry.setPaidBy(((RadioButton) findViewById(selectedRadio)).getText().toString());
            }

            String amount = billAmount.getText().toString().trim();
            tecEntry.setBillAmount(TextUtils.isEmpty(amount) ? 0 : Double.parseDouble(amount));
            tecEntry.setUnitPrice(TextUtils.isEmpty(amount) ? 0 : Double.parseDouble(amount));
            tecEntry.setPaidTo(intercityVendor.getText().toString().trim());
            tecEntry.setAttachmentPath(mFile != null ? mFile.getName() : "");

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
            tecEntry.setPaidTo(userPref.getName());
            tecEntry.setAttachmentPath(mFile != null ? mFile.getName() : "");
            tecEntry.setIsBillable(foodBillable.isChecked() ? "Y" : "N");

        } else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.local_travel))) {

            String strActuals = actual.getText().toString().trim();
            tecEntry.setUnitPrice(TextUtils.isEmpty(strActuals) ? 0 : Double.parseDouble(strActuals));
            String strTotal = conveyanceDays.getText().toString().trim();
            tecEntry.setTotalQuantitty(TextUtils.isEmpty(strTotal) ? 0 : Double.parseDouble(strTotal));
            tecEntry.setDescription(description.getText().toString().trim());
            String strAmount = siteBillAmount.getText().toString();
            tecEntry.setBillAmount(TextUtils.isEmpty(strAmount) ? 0 : Double.parseDouble(strAmount));
            tecEntry.setDepartureDate(siteFromDate.getText().toString().trim());
            tecEntry.setArrivalDate(siteToDate.getText().toString().trim());
            tecEntry.setPaidBy(resources.getString(R.string.employee));
            tecEntry.setPaidTo(userPref.getName());

        } else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.misc)) || strClaimCategory.equalsIgnoreCase(resources.getString(R.string.repair_maintenance)) || strClaimCategory.equalsIgnoreCase(resources.getString(R.string.intl_travel)) || strClaimCategory.equalsIgnoreCase(resources.getString(R.string.fix_asset))) {
            tecEntry.setDate(billDate.getText().toString().trim());
            tecEntry.setDescription(misleniousDescription.getText().toString().trim());
            tecEntry.setPaidTo(misVendor.getText().toString().trim());
            tecEntry.setGstin(misGstin.getText().toString().trim());
            tecEntry.setBillNum(misBillNum.getText().toString().trim());
            String strAmount = miscBillAmount.getText().toString().trim();
            tecEntry.setBillAmount(TextUtils.isEmpty(strAmount) ? 0 : Double.parseDouble(strAmount));
            tecEntry.setAttachmentPath(mFile != null ? mFile.getName() : "");
            tecEntry.setPaidBy(resources.getString(R.string.employee));
            tecEntry.setTotalQuantitty(1);
            tecEntry.setUnitPrice(TextUtils.isEmpty(strAmount) ? 0 : Double.parseDouble(strAmount));

        } else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.fuel_expense))) {
            tecEntry.setDate(fuelDate.getText().toString().trim());
            tecEntry.setTravelMode(typeVehicleSpinner.getSelectedItem().toString());
            String fromLocation = fromFuelSpinner.getSelectedItem().toString();
            tecEntry.setFromLocation(fromLocation.equalsIgnoreCase(resources.getString(R.string.other)) ? otherFormFuel.getText().toString() : fromLocation);
            String toLocation = toFuelSpinner.getSelectedItem().toString();
            tecEntry.setToLocation(toLocation.equalsIgnoreCase(resources.getString(R.string.other)) ? otherToFuel.getText().toString() : toLocation);
            String strAmount = fuelBillAmount.getText().toString();
            tecEntry.setBillAmount(TextUtils.isEmpty(strAmount) ? 0 : Double.parseDouble(strAmount));
            String strTravel = travelDistance.getText().toString();
            tecEntry.setKiloMeter(TextUtils.isEmpty(strTravel) ? 0 : Double.parseDouble(strTravel));
            tecEntry.setPaidBy(resources.getString(R.string.employee));
            tecEntry.setPaidTo(userPref.getName());
            tecEntry.setMileage(Double.parseDouble(mileage.getText().toString().trim()));
            tecEntry.setTotalQuantitty(1);
            tecEntry.setUnitPrice(TextUtils.isEmpty(strAmount) ? 0 : Double.parseDouble(strAmount));

        } else if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.lodging))) {
            tecEntry.setDate(hotelBillDate.getText().toString().trim());
            String strCityHotel = cityHotel.getText().toString().trim();
            tecEntry.setPaidTo(strCityHotel);
            tecEntry.setGstin(hotelGstin.getText().toString().trim());
            tecEntry.setBillNum(hotelBillNum.getText().toString().trim());
            String nights = hotelNights.getText().toString().trim();
            tecEntry.setTotalQuantitty(TextUtils.isEmpty(nights) ? 0 : Double.parseDouble(nights));
            tecEntry.setDepartureDate(hotelStayFirst.getText().toString().trim());
            tecEntry.setArrivalDate(hotelStayLast.getText().toString().trim());
            String price = hotelRate.getText().toString().trim();
            tecEntry.setUnitPrice(TextUtils.isEmpty(price) ? 0 : Double.parseDouble(price));

            int selectedRadio = hotelPaidRadioGroup.getCheckedRadioButtonId();
            if (selectedRadio != -1) {
                tecEntry.setPaidBy(((RadioButton) findViewById(selectedRadio)).getText().toString());
            }
            String strAmount = hotelBillAmount.getText().toString().trim();
            tecEntry.setBillAmount(strAmount.isEmpty() ? 0 : Double.parseDouble(strAmount));
            tecEntry.setAttachmentPath(mFile != null ? mFile.getName() : "");
        } else {
            tecEntry.setAttachmentPath(mFile != null ? mFile.getName() : "");
            tecEntry.setPaidBy(resources.getString(R.string.employee));
            tecEntry.setDate(attachmentDate.getText().toString().trim());
            tecEntry.setDescription(attachmentDescription.getText().toString().trim());
            tecEntry.setPaidTo(userPref.getName());
        }

        tecEntry.setEntryCategory(strClaimCategory);
        tecEntry.setCreatedById(Integer.parseInt(userPref.getUserId()));
        if (validate()) {
            if (mFile == null && tecEntry.getEntryCategory().equalsIgnoreCase(claimCategoryList.get(1))) {
                showWarning();
            } else {
                proceed();
            }
        }
    }

    private void showWarning() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        builder.setMessage(R.string.service_sheet_warning);
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
                proceed();
            }
        });

        builder.show();
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
            Gson gson = new Gson();
            String tecEntryJson = gson.toJson(tecEntry);
            Log.d(TAG,"json "+tecEntryJson);
            RequestBody rbTecJSON = RequestBody.create(MediaType.parse("text/plain"),tecEntryJson);
            myMap.put(ConstantVariable.Tec.TEC_JSON, rbTecJSON);
            RequestBody action = RequestBody.create(MediaType.parse("text/plain"),ConstantVariable.MIX_ID.INSERT);
            myMap.put(ConstantVariable.MIX_ID.ACTION,action);

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
                                    startTecActivity(addResponse.getTecId());
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

    private void startTecActivity(final int tecId) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.putExtra(ConstantVariable.Tec.TEC_ID, tecId);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }, 300);

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
            } else if (mFile == null) {
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
            } else if (TextUtils.isEmpty(tecEntry.getDate())) {
                showMessage("Sign date is required");
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
            tecEntry.setPaidToId(data.getIntExtra(ConstantVariable.MIX_ID.ID, 0));
            misGstin.setText(gstNum.isEmpty() ? "NA" : gstNum);
        } else if (requestCode == CITY_HOTEL && resultCode == Activity.RESULT_OK && data != null) {
            String vendor = data.getStringExtra(ConstantVariable.MIX_ID.VENDOR);
            String location = data.getStringExtra(ConstantVariable.Tec.SITE_LOCATION);
            tecEntry.setPaidToId(data.getIntExtra(ConstantVariable.MIX_ID.ID, 0));
            tecEntry.setPaidTo(vendor);
            tecEntry.setLocation(location);
            cityHotel.setText(vendor);

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
                    //setupFile(path, textView);
                    setupFile(mainPath.toString(),textView);
                    setupFile(mainPath.toString(),textView);
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
                setupFile(path, textView);
            } else
                showMessage("Invalid file");
        }
    }


    private TextView getTextView() {
        TextView textView;
        if (strClaimCategory.equalsIgnoreCase(resources.getString(R.string.misc)) || strClaimCategory.equalsIgnoreCase(resources.getString(R.string.fix_asset)) || strClaimCategory.equalsIgnoreCase(resources.getString(R.string.intl_travel)) || strClaimCategory.equalsIgnoreCase(resources.getString(R.string.repair_maintenance)))
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
                            attachmentLayout.setVisibility(View.GONE);
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
                            attachmentLayout.setVisibility(View.GONE);
                            attachment.setText(resources.getString(R.string.attachment));
                            attachment.setVisibility(View.VISIBLE);
                            attachment.setTextColor(resources.getColor(R.color.colorPrimaryText));
                            mFile = null;
                            tecEntry.setAttachmentPath("");
                            if (newTec.getIsInternational() == 1) {
                                city.setEnabled(false);
                                rate.setFocusable(true);
                            } else {
                                city.setEnabled(true);
                                rate.setFocusable(false);
                            }
                            break;

                        case "Local Travel - Public transport":
                            intercityTravelCost.setVisibility(View.GONE);
                            layoutHotel.setVisibility(View.GONE);
                            layoutFoodBoardingIem.setVisibility(View.GONE);
                            mislleniousLaoyut.setVisibility(View.GONE);
                            layoutSiteConveyance.setVisibility(View.VISIBLE);
                            fuelLayout.setVisibility(View.GONE);
                            attachmentLayout.setVisibility(View.GONE);
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
                            attachmentLayout.setVisibility(View.GONE);

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
                            attachmentLayout.setVisibility(View.GONE);

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
                            attachmentLayout.setVisibility(View.GONE);

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
                            attachmentLayout.setVisibility(View.GONE);

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
                            attachmentLayout.setVisibility(View.GONE);

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
                            attachmentLayout.setVisibility(View.GONE);

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
                            attachmentLayout.setVisibility(View.VISIBLE);

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

                case R.id.city:
                    BookingMode bookingMode = (BookingMode) city.getSelectedItem();
                    if (newTec.getIsInternational() == 0)
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
    public void onCheckedChanged(RadioGroup radioGroup, int seledButtonId) {
        BookingMode bookingMode = (BookingMode) city.getSelectedItem();
        if (newTec.getIsInternational() == 0)
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
                        new CustomDate(DepartureDate, TecClaimActivity.this, strIntercityBillingDate, DateCal.getAddOnDate(strIntercityBillingDate, 7));
                    break;

                case R.id.DepartureDate:
                    String strDepartureDate = DepartureDate.getText().toString().trim();
                    String strBillingDate = intercityBillingDate.getText().toString().trim();
                    if (!strDepartureDate.isEmpty())
                        new CustomDate(arrivalDate, TecClaimActivity.this, strDepartureDate, DateCal.getAddOnDate(strBillingDate, 7));
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
                        new CustomDate(hotelStayLast, TecClaimActivity.this, date, null);
                    calculateNights();
                    break;


                case R.id.hotelStayLast:
                    calculateNights();
                    break;

                case R.id.signPerDiemDate:
                    String strSignDate = signPerDiemDate.getText().toString().trim();
                    if (!strSignDate.isEmpty()) {
                        new CustomDate(foodFromDate, TecClaimActivity.this, strClaimDate, strSignDate);
                    }
                    break;

                case R.id.foodFromDate:
                    String strFoodFromDate = foodFromDate.getText().toString().trim();
                    String strSigDate = signPerDiemDate.getText().toString().trim();
                    if (!strFoodFromDate.isEmpty() && !strSigDate.isEmpty())
                        new CustomDate(foodToDate, TecClaimActivity.this, strFoodFromDate, strSigDate);
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
                        new CustomDate(siteToDate, TecClaimActivity.this, strSiteFromDate, null);
                    calculateSiteDays();
                    break;


                case R.id.siteToDate:
                    calculateSiteDays();
                    break;


                case R.id.miscQuantity:
                    calculateMiscAmount();
                    break;
                case R.id.miscRate:
                    calculateMiscAmount();
                    break;

            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    }

    private void calculateMiscAmount() {
        String strQuantity = miscQuantity.getText().toString().trim();
        String strRate = miscRate.getText().toString().trim();
        tecEntry.setTotalQuantitty(TextUtils.isEmpty(strQuantity) ? 0 : Integer.parseInt(strQuantity));
        tecEntry.setUnitPrice(TextUtils.isEmpty(strRate) ? 0 : Double.parseDouble(strRate));
        tecEntry.setBillAmount(tecEntry.getTotalQuantitty() * tecEntry.getUnitPrice());
        miscBillAmount.setText(String.valueOf(tecEntry.getBillAmount()));

    }

    private void calculateFoodDays() {
        String strHotelFirst = foodFromDate.getText().toString().trim();
        String strHotelLast = foodToDate.getText().toString().trim();
        String strNonWorkingDays = nonWorkingDays.getText().toString().trim();
        if (strHotelFirst.isEmpty() || strHotelLast.isEmpty()) {
            days.setText(String.valueOf(0));
        } else {
            int workingDays = DateCal.getSiteDays(strHotelFirst, strHotelLast);
            int nonWorkingDays = TextUtils.isEmpty(strNonWorkingDays) ? 0 : Integer.parseInt(strNonWorkingDays);
            days.setText(String.valueOf(workingDays - nonWorkingDays));
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
