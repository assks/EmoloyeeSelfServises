package in.technitab.ess.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.model.Vendor;
import in.technitab.ess.util.ConstantVariable;

import static in.technitab.ess.util.ConstantVariable.Value.getDistrictList;

public class AddVendorActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.first_name)
    EditText firstName;
    @BindView(R.id.last_name)
    EditText lastName;
    @BindView(R.id.display_name)
    EditText displayName;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.contact)
    EditText contact;
    @BindView(R.id.gstTreatmentSpinner)
    Spinner gstTreatmentSpinner;
    @BindView(R.id.placeOfSupplySpinner)
    Spinner placeOfSupplySpinner;
    /*@BindView(R.id.districtSpinner)
    Spinner districtSpinner;*/
    @BindView(R.id.paymentTermSpinner)
    Spinner paymentTermSpinner;

    List<String> gstTreatmentList;
    @BindView(R.id.gst)
    EditText gst;
    @BindView(R.id.gst_inout_layout)
    TextInputLayout gstInoutLayout;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.place_of_supply)
    TextView placeOfSupply;
    @BindView(R.id.vendorTypeSpinner)
    Spinner vendorTypeSpinner;
    @BindView(R.id.hotel_bill_amount)
    EditText hotelBillAmount;
    @BindView(R.id.hotel_bill_amount_layout)
    TextInputLayout hotelBillAmountLayout;
    @BindView(R.id.bank)
    RadioButton bank;
    @BindView(R.id.cash)
    RadioButton cash;
    @BindView(R.id.paymentMode)
    RadioGroup paymentMode;
    @BindView(R.id.credit_card)
    RadioButton creditCard;
    private Vendor vendor;
    private String action = "";

    List<String> placeOfSupplyList, paymentTermList, vendorTypeList,districtList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vendor);
        ButterKnife.bind(this);

        setSpinner();
        init();
        setToolbar();
    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            action = bundle.getString(ConstantVariable.MIX_ID.ACTION);
            if (action.equalsIgnoreCase(ConstantVariable.MIX_ID.UPDATE) || action.equalsIgnoreCase(ConstantVariable.MIX_ID.APPROVE) || action.equalsIgnoreCase(ConstantVariable.MIX_ID.VIEW)) {
                vendor = bundle.getParcelable(ConstantVariable.MIX_ID.VENDOR);

                String strFirstName = vendor.getFirstName();
                firstName.setText(strFirstName);
                lastName.setText(vendor.getLastName());
                displayName.setText(vendor.getCompanyName());
                email.setText(vendor.getEmail());
                contact.setText(vendor.getContact());

                if (getResources().getString(R.string.registered_business).equalsIgnoreCase(vendor.getGst_treatment()))
                    gstTreatmentSpinner.setSelection(0);
                else {
                    gstTreatmentSpinner.setSelection(1);
                }

                gst.setText(vendor.getGstNumber());

                for (int i = 0; i < placeOfSupplyList.size(); i++) {
                    if (placeOfSupplyList.get(i).substring(1, 3).equalsIgnoreCase(vendor.getPlaceOfSupply()))
                        placeOfSupplySpinner.setSelection(i);
                }

                for (int i = 0; i < paymentTermList.size(); i++) {
                    if (paymentTermList.get(i).equalsIgnoreCase(vendor.getGst_treatment()))
                        paymentTermSpinner.setSelection(i);
                }

                int vendorPosition = vendorTypeList.indexOf(vendor.getVendorType());
                vendorTypeSpinner.setSelection(vendorPosition < 0 ? 0 : vendorPosition);

                if (vendor.getVendorType().equalsIgnoreCase(getResources().getString(R.string.hotel))) {
                    hotelBillAmountLayout.setVisibility(View.VISIBLE);
                    hotelBillAmount.setText(String.valueOf(vendor.getRate()));
                }

                if (vendor.getPaymentMode().equalsIgnoreCase(getResources().getString(R.string.bank)))
                    bank.setChecked(true);
                else if (vendor.getPaymentMode().equalsIgnoreCase(getResources().getString(R.string.cash)))
                    cash.setChecked(true);
                else {
                    creditCard.setChecked(true);
                }
            } else {
                vendor = new Vendor();
            }
        }
    }


    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void setSpinner() {
        Resources resources = getResources();
        districtList = new ArrayList<>();
        gstTreatmentList = Arrays.asList(resources.getStringArray(R.array.gstArray));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gstTreatmentList);
        gstTreatmentSpinner.setAdapter(adapter);
        gstTreatmentSpinner.setOnItemSelectedListener(this);

        districtList.addAll(getDistrictList());
        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, districtList);
        //districtSpinner.setAdapter(stateAdapter);
        //districtSpinner.setOnItemSelectedListener(this);

        placeOfSupplyList = Arrays.asList(resources.getStringArray(R.array.placeSupplyArray));
        ArrayAdapter<String> placeSupplyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, placeOfSupplyList);
        placeOfSupplySpinner.setAdapter(placeSupplyAdapter);
        placeOfSupplySpinner.setOnItemSelectedListener(this);

        paymentTermList = Arrays.asList(getResources().getStringArray(R.array.paymentTermArray));
        ArrayAdapter<String> paymentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, paymentTermList);
        paymentTermSpinner.setAdapter(paymentAdapter);
        paymentTermSpinner.setOnItemSelectedListener(this);

        vendorTypeList = Arrays.asList(getResources().getStringArray(R.array.vendorTypeArray));
        ArrayAdapter<String> vendorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, vendorTypeList);
        vendorTypeSpinner.setAdapter(vendorAdapter);
        vendorTypeSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (view != null) {
            view.setPadding(2, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
            ((TextView) view).setTextColor(Color.BLACK);
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.mediumTextSize));

            switch (adapterView.getId()) {
                case R.id.gstTreatmentSpinner:
                    String gstValue = gstTreatmentSpinner.getSelectedItem().toString();
                    setupGST(gstValue);
                    break;

                case R.id.vendorTypeSpinner:
                    String vendorType = vendorTypeSpinner.getSelectedItem().toString();
                    vendor.setVendorType(vendorType);
                    if (vendorType.equalsIgnoreCase(getResources().getString(R.string.hotel))) {
                        hotelBillAmountLayout.setVisibility(View.VISIBLE);
                    } else {
                        hotelBillAmountLayout.setVisibility(View.GONE);
                        vendor.setRate(0);
                    }
            }
        }
    }

    private void setupGST(String gstValue) {
        if (gstValue.equalsIgnoreCase(gstTreatmentList.get(0))) {
            gstInoutLayout.setVisibility(View.VISIBLE);
            placeOfSupplySpinner.setVisibility(View.VISIBLE);

        } else if (gstValue.equalsIgnoreCase(gstTreatmentList.get(1))) {
            gstInoutLayout.setVisibility(View.GONE);
            gst.setText("");
            vendor.setGstNumber("");
            placeOfSupply.setVisibility(View.GONE);
            placeOfSupplySpinner.setVisibility(View.GONE);

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @OnClick(R.id.submit)
    protected void onSubmit() {
        String strFirstName = firstName.getText().toString().trim();
        String strLastName = lastName.getText().toString().trim();
        String strCompanyName = displayName.getText().toString().trim();
        String strEmail = email.getText().toString().trim();
        String strContact = contact.getText().toString().trim();
        //String strDistrict = districtSpinner.getSelectedItem().toString();
        String strGstTreatment = gstTreatmentSpinner.getSelectedItem().toString();
        String strGstNum = gst.getText().toString().trim();
        String strPlaceSupply = placeOfSupplySpinner.getSelectedItem().toString();
        String strPaymentTerm = paymentTermSpinner.getSelectedItem().toString();
        String strRate = hotelBillAmount.getText().toString().trim();
        double billAmount = strRate.isEmpty() ? 0 : Double.parseDouble(strRate);


        String strPaymentMode = "";
        if (paymentMode.getCheckedRadioButtonId() != -1) {
            strPaymentMode = ((RadioButton) findViewById(paymentMode.getCheckedRadioButtonId())).getText().toString();
        }

        if (invalidate(strFirstName, strEmail, strCompanyName, strContact, strGstTreatment, strGstNum, strPlaceSupply, strPaymentTerm, billAmount, strPaymentMode)) {
            vendor.setFirstName(strFirstName);
            vendor.setLastName(strLastName);
            vendor.setCompanyName(strCompanyName);
            vendor.setContact(strContact);
            vendor.setEmail(strEmail);
            vendor.setGst_treatment(strGstTreatment);
            vendor.setPaymentTerm(strPaymentTerm);
            vendor.setPaymentMode(strPaymentMode);
            vendor.setRate(billAmount);

            if (strGstTreatment.equalsIgnoreCase(gstTreatmentList.get(0))) {
                vendor.setPlaceOfSupply(strPlaceSupply.substring(1, 3));
                vendor.setGstNumber(strGstNum);
            }

            Intent intent = new Intent(this, AddressActivity.class);
            intent.putExtra(ConstantVariable.MIX_ID.VENDOR, vendor);
            intent.putExtra(ConstantVariable.MIX_ID.ACTION, action);
            startActivityForResult(intent,1);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK){
                finish();
            }
        }
    }

    private boolean invalidate(String strFirstName, String email, String strCompanyName, String strContact, String strGstTreatment, String strGstNum, String strPlaceSupply, String strPaymentTerm, double strRate, String strPaymentMode) {
        boolean valid = true;

        if (strFirstName.isEmpty() && strCompanyName.isEmpty()) {
            valid = false;
            showSnackBar("Company name or name is required");
        } else if (strContact.isEmpty()) {
            valid = false;
            showSnackBar("contact is required");
        } else if (strContact.length() != 10) {
            valid = false;
            showSnackBar("Contact is invalid");
        } else if (!email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            valid = false;
            showSnackBar("Email is invalid");
        } /*else if (strDistrict.isEmpty()) {
            valid = false;
            showSnackBar("District is invalid");
        }*/ else if (strGstTreatment.equalsIgnoreCase(gstTreatmentList.get(0))) {
            if (strGstNum.isEmpty()) {
                valid = false;
                showSnackBar("GST number is required");
            } else if (!validGSTNumber(strGstNum)) {
                valid = false;
                showSnackBar("Invalid GST number");
            } else if (strPlaceSupply.isEmpty()) {
                valid = false;
                showSnackBar("Place of Supply is required");
            }
        } else if (!strGstTreatment.equalsIgnoreCase(gstTreatmentList.get(0)) && strPaymentTerm.isEmpty()) {
            valid = false;
            showSnackBar("Payment term is required");
        } else if (strRate < 1 && vendor.getVendorType().equalsIgnoreCase(getResources().getString(R.string.hotel))) {
            valid = false;
            showSnackBar("Hotel rate is required");
        } else if (strPaymentMode.isEmpty()) {
            valid = false;
            showSnackBar("Payment Mode is required");
        }
        return valid;
    }


    private static boolean validGSTNumber(String strPanNum) {
        Pattern pattern = Pattern.compile("[0-9]{2}[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9A-Za-z]{1}[Z]{1}[0-9a-zA-Z]{1}");
        Matcher matcher = pattern.matcher(strPanNum);
        return matcher.matches();
    }


    private void showSnackBar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
