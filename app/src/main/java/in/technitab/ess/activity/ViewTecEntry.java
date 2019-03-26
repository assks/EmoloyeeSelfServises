package in.technitab.ess.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.model.NewTecEntry;
import in.technitab.ess.util.ConstantVariable;

public class ViewTecEntry extends AppCompatActivity {

    private static final String TAG = ViewTecEntry.class.getSimpleName();
    @BindView(R.id.claimSpinner)
    TextView claimSpinner;
    @BindView(R.id.travelModeSpinner)
    TextView travelModeSpinner;
    @BindView(R.id.adminFrom)
    TextView adminFrom;
    @BindView(R.id.adminTo)
    TextView adminTo;
    @BindView(R.id.DepartureDate)
    TextView DepartureDate;
    @BindView(R.id.DepartureTime)
    TextView DepartureTime;
    @BindView(R.id.arrivalDate)
    TextView arrivalDate;
    @BindView(R.id.arrivalTime)
    TextView arrivalTime;
    @BindView(R.id.intercity_vendor)
    TextView intercityVendor;
    @BindView(R.id.bill_amount)
    TextView billAmount;
    @BindView(R.id.intercityViewAttachment)
    TextView intercityViewAttachment;
    @BindView(R.id.paid_by)
    TextView paidBy;
    @BindView(R.id.intercity_travel_cost)
    LinearLayout intercityTravelCost;
    @BindView(R.id.hotelBillDate)
    TextView hotelBillDate;
    @BindView(R.id.cityHotel)
    TextView cityHotel;
    @BindView(R.id.hotel_gstin)
    TextView hotelGstin;
    @BindView(R.id.hotelBillNum)
    TextView hotelBillNum;
    @BindView(R.id.hotelStayFirst)
    TextView hotelStayFirst;
    @BindView(R.id.hotelStayLast)
    TextView hotelStayLast;
    @BindView(R.id.hotelNights)
    TextView hotelNights;
    @BindView(R.id.hotel_rate)
    TextView hotelRate;
    @BindView(R.id.hotel_bill_amount)
    TextView hotelBillAmount;
    @BindView(R.id.hotelAttachment)
    TextView hotelAttachment;
    @BindView(R.id.hotelPaidRadioGroup)
    TextView hotelPaidRadioGroup;
    @BindView(R.id.layoutHotel)
    LinearLayout layoutHotel;
    @BindView(R.id.city)
    TextView city;
    @BindView(R.id.attachment)
    TextView attachment;
    @BindView(R.id.foodFromDate)
    TextView foodFromDate;
    @BindView(R.id.foodToDate)
    TextView foodToDate;
    @BindView(R.id.days)
    TextView days;
    @BindView(R.id.rate)
    TextView rate;
    @BindView(R.id.food_bill_amount)
    TextView foodBillAmount;
    @BindView(R.id.layoutFoodBoardingIem)
    LinearLayout layoutFoodBoardingIem;
    @BindView(R.id.siteFromDate)
    TextView siteFromDate;
    @BindView(R.id.siteToDate)
    TextView siteToDate;
    @BindView(R.id.conveyanceDays)
    TextView conveyanceDays;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.actual)
    TextView actual;
    @BindView(R.id.site_bill_amount)
    TextView siteBillAmount;
    @BindView(R.id.layoutSiteConveyance)
    LinearLayout layoutSiteConveyance;
    @BindView(R.id.bill_date)
    TextView billDate;
    @BindView(R.id.intercityBillingDate)
    TextView intercityBillingDate;
    @BindView(R.id.signPerDiemDate)
    TextView signPerDiemDate;
    @BindView(R.id.miscQuantity)
    TextView miscQuantity;
    @BindView(R.id.miscRate)
    TextView miscRate;
    @BindView(R.id.mislenious_description)
    TextView misleniousDescription;
    @BindView(R.id.mis_vendor)
    TextView misVendor;
    @BindView(R.id.mis_gstin)
    TextView misGstin;
    @BindView(R.id.misBillNum)
    TextView misBillNum;
    @BindView(R.id.misc_bill_amount)
    TextView miscBillAmount;
    @BindView(R.id.misAttachment)
    TextView misAttachment;
    @BindView(R.id.mislleniousLaoyut)
    LinearLayout mislleniousLaoyut;
    @BindView(R.id.fuelDate)
    TextView fuelDate;
    @BindView(R.id.typeVehicleSpinner)
    TextView typeVehicleSpinner;
    @BindView(R.id.fromFuelSpinner)
    TextView fromFuelSpinner;
    @BindView(R.id.toFuelSpinner)
    TextView toFuelSpinner;
    @BindView(R.id.userFuelLocation)
    RelativeLayout userFuelLocation;
    @BindView(R.id.travel_distance)
    TextView travelDistance;
    @BindView(R.id.mileage)
    TextView mileage;
    @BindView(R.id.fuel_bill_amount)
    TextView fuelBillAmount;
    @BindView(R.id.fuelLayout)
    LinearLayout fuelLayout;
    @BindView(R.id.onlyAttachment)
    TextView onlyAttachment;
    @BindView(R.id.attachmentLayout)
    LinearLayout attachmentLayout;
    @BindView(R.id.attachmentDate)
    TextView attachmentDate;
    @BindView(R.id.attachmentDescription)
    TextView attachmentDescription;


    private Resources resources;
    private String projectName = "";
    private List<String> claimCategoryList;
    NewTecEntry tecEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tec_entry);
        ButterKnife.bind(this);

        init();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            projectName = bundle.getString(ConstantVariable.Tec.PROJECT_NAME);
            tecEntry = bundle.getParcelable(resources.getString(R.string.tec_entry));
            setToolbar();
            showHide(tecEntry);
        }

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

    private void init() {
        resources = getResources();
        claimCategoryList = new ArrayList<>();
        claimCategoryList = Arrays.asList(resources.getStringArray(R.array.tecCategoryArray));
        tecEntry = new NewTecEntry();
    }


    @OnClick({R.id.onlyAttachment, R.id.attachment, R.id.intercityViewAttachment, R.id.misAttachment, R.id.hotelAttachment})
    protected void onViewFile() {
        Intent intent = new Intent(this, ProjectListActivity.class);
        intent.putExtra(ConstantVariable.MIX_ID.ACTION, resources.getString(R.string.tec));
        intent.putExtra(getResources().getString(R.string.project), projectName);
        intent.putExtra("file_url", tecEntry.getAttachmentPath());
        startActivity(intent);
    }

    private void showHide(NewTecEntry tecEntry) {
        if (tecEntry != null) {
            String strClaimCategory = tecEntry.getEntryCategory();
            claimSpinner.setText(strClaimCategory);

            if (strClaimCategory.equalsIgnoreCase(claimCategoryList.get(0))) {
                intercityTravelCost.setVisibility(View.VISIBLE);
                 travelModeSpinner.setText(tecEntry.getTravelMode());
                DepartureDate.setText(tecEntry.getDepartureDate());
                DepartureTime.setText(tecEntry.getDepartureTime());
                arrivalDate.setText(tecEntry.getArrivalDate());
                intercityBillingDate.setText(tecEntry.getDate());
                arrivalTime.setText(tecEntry.getArrivalTime());
                adminFrom.setText(tecEntry.getFromLocation());
                adminTo.setText(tecEntry.getToLocation());
                intercityVendor.setText(tecEntry.getPaidTo());
                String fileName = tecEntry.getAttachmentPath();
                if (!fileName.isEmpty())
                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1);

                intercityViewAttachment.setText(fileName);
                intercityViewAttachment.setTextColor(resources.getColor(R.color.colorPrimary));
                hotelPaidRadioGroup.setText(tecEntry.getPaidBy());
                billAmount.setText(String.valueOf(tecEntry.getBillAmount()));


            } else if (strClaimCategory.equalsIgnoreCase(claimCategoryList.get(1))) {

                layoutFoodBoardingIem.setVisibility(View.VISIBLE);
                city.setText(tecEntry.getLocation());
                days.setText(String.valueOf(tecEntry.getTotalQuantitty()));
                rate.setText(String.valueOf(tecEntry.getUnitPrice()));
                foodBillAmount.setText(String.valueOf(tecEntry.getBillAmount()));
                foodFromDate.setText(tecEntry.getDepartureDate());
                foodToDate.setText(tecEntry.getArrivalDate());
                signPerDiemDate.setText(tecEntry.getDate());
                String fileName = tecEntry.getAttachmentPath();
                if (!fileName.isEmpty())
                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1);

                attachment.setText(fileName);
                attachment.setTextColor(resources.getColor(R.color.colorPrimary));

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
                misleniousDescription.setText(tecEntry.getDescription());
                misVendor.setText(tecEntry.getPaidTo());
                misBillNum.setText(tecEntry.getBillNum());
                misGstin.setText(tecEntry.getGstin());
                miscQuantity.setText(String.valueOf(tecEntry.getTotalQuantitty()));
                miscRate.setText(String.valueOf(tecEntry.getUnitPrice()));
                miscBillAmount.setText(String.valueOf(tecEntry.getBillAmount()));
                String fileName = tecEntry.getAttachmentPath();
                if (!fileName.isEmpty())
                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                misAttachment.setText(fileName);
                misAttachment.setTextColor(resources.getColor(R.color.colorPrimary));

            } else if (strClaimCategory.equalsIgnoreCase(claimCategoryList.get(7))) {
                layoutHotel.setVisibility(View.VISIBLE);
                hotelBillDate.setText(tecEntry.getDate());
                cityHotel.setText(tecEntry.getVendor());
                hotelGstin.setText(tecEntry.getGstin());
                hotelBillNum.setText(tecEntry.getBillNum());
                hotelNights.setText(String.valueOf(tecEntry.getTotalQuantitty()));
                hotelRate.setText(String.valueOf(tecEntry.getUnitPrice()));
                hotelBillAmount.setText(String.valueOf(tecEntry.getBillAmount()));
                hotelStayFirst.setText(tecEntry.getDepartureDate());
                hotelStayLast.setText(tecEntry.getArrivalDate());
                String fileName = tecEntry.getAttachmentPath();
                if (!fileName.isEmpty())
                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                hotelAttachment.setText(fileName);
                hotelAttachment.setTextColor(resources.getColor(R.color.colorPrimary));
                hotelPaidRadioGroup.setText(tecEntry.getPaidBy());

            } else if (strClaimCategory.equalsIgnoreCase(claimCategoryList.get(6))) {
                fuelLayout.setVisibility(View.VISIBLE);
                fuelDate.setText(tecEntry.getDate());
                typeVehicleSpinner.setText(tecEntry.getTravelMode());
                fuelBillAmount.setText(String.valueOf(tecEntry.getBillAmount()));
                travelDistance.setText(String.valueOf(tecEntry.getKiloMeter()));
                mileage.setText(String.valueOf(tecEntry.getMileage()));
                fromFuelSpinner.setText(tecEntry.getFromLocation());
                toFuelSpinner.setText(tecEntry.getToLocation());
            } else {
                String fileName = tecEntry.getAttachmentPath();
                if (!fileName.isEmpty())
                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1);

                attachmentLayout.setVisibility(View.VISIBLE);
                onlyAttachment.setText(fileName);
                onlyAttachment.setTextColor(resources.getColor(R.color.colorPrimary));
                attachmentDate.setText(tecEntry.getDate());
                attachmentDescription.setText(tecEntry.getDescription());
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
