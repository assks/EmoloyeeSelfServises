package in.technitab.ess.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.model.Vendor;
import in.technitab.ess.util.ConstantVariable;

public class AddressActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    @BindView(R.id.billing_address)
    EditText billingAddress;
    @BindView(R.id.billing_city)
    EditText billingCity;
    @BindView(R.id.billingStateSpinner)
    Spinner billingStateSpinner;
    @BindView(R.id.billing_zip_code)
    EditText billingZipCode;
    @BindView(R.id.billing_country)
    EditText billingCountry;
    @BindView(R.id.billing_phone)
    EditText billingPhone;
    @BindView(R.id.same_address)
    CheckBox sameAddress;
    @BindView(R.id.shipping_address)
    EditText shippingAddress;
    @BindView(R.id.shipping_city)
    EditText shippingCity;
    @BindView(R.id.shippingStateSpinner)
    Spinner shippingState;
    @BindView(R.id.shipping_zip_code)
    EditText shippingZipCode;
    @BindView(R.id.shipping_country)
    EditText shippingCountry;
    @BindView(R.id.shipping_phone)
    EditText shippingPhone;
    @BindView(R.id.submit)
    Button submit;
    Vendor vendor;
    @BindView(R.id.billing_district)
    AutoCompleteTextView billingDistrict;
    @BindView(R.id.shipping_district)
    EditText shippingDistrict;
    private Resources resources;
    private List<String> mBillingStateList;
    private String action;
    private String strBillingDistrict = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);

        resources = getResources();

        setupSpinner();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            vendor = bundle.getParcelable(ConstantVariable.MIX_ID.VENDOR);
            action = bundle.getString(ConstantVariable.MIX_ID.ACTION);
            if (vendor != null) {
                billingPhone.setText(vendor.getContact());
                billingAddress.setText(vendor.getBillingAddress());
                billingCity.setText(vendor.getBillingCity());
                int billingPosition = mBillingStateList.indexOf(vendor.getBillingState());
                billingStateSpinner.setSelection(billingPosition < 0 ? 0 : billingPosition);
                billingZipCode.setText(vendor.getBillingZipCode());
                billingDistrict.setText(vendor.getDistrict());
                billingCountry.setText(TextUtils.isEmpty(vendor.getBillingCountry()) ? resources.getString(R.string.default_country) : vendor.getBillingCountry());
                billingPhone.setText(vendor.getBillingPhone());
                shippingAddress.setText(vendor.getShippingAddress());

                shippingCity.setText(vendor.getShippingCity());
                int shippingPosition = mBillingStateList.indexOf(vendor.getShippingState());
                shippingState.setSelection(shippingPosition < 0 ? 0 : shippingPosition);
                shippingZipCode.setText(vendor.getShippingZipCode());
                shippingCountry.setText(vendor.getShippingCountry());
                shippingPhone.setText(vendor.getShippingPhone());
            } else {
                finish();
            }

            sameAddress.setOnCheckedChangeListener(this);
        }

        setToolbar();

    }

    private void setupSpinner() {
        Resources resources = getResources();

        mBillingStateList = new ArrayList<>();
        mBillingStateList.add(resources.getString(R.string.select_state));
        mBillingStateList = Arrays.asList(resources.getStringArray(R.array.stateArray));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mBillingStateList);
        billingStateSpinner.setAdapter(adapter);
        billingStateSpinner.setOnItemSelectedListener(this);
        shippingState.setAdapter(adapter);
        shippingState.setOnItemSelectedListener(this);
    }


    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @OnClick(R.id.submit)
    protected void OnSubmit() {

        String strBillingAddress = billingAddress.getText().toString().trim();
        String strBillingCity = billingCity.getText().toString().trim();
        String strBillingPhone = billingPhone.getText().toString().trim();
        String strBillingCountry = billingCountry.getText().toString().trim();
        String strBillingState = billingStateSpinner.getSelectedItem().toString();
        String strBillingZipCode = billingZipCode.getText().toString().trim();
        String strShippingAddress = shippingAddress.getText().toString().trim();
        String strShippingCity = shippingCity.getText().toString().trim();
        String strShippingPhone = shippingPhone.getText().toString().trim();
        String strShippingCountry = shippingCountry.getText().toString().trim();
        String strShippingState = shippingState.getSelectedItem().toString();
        String strShippingZipCode = shippingZipCode.getText().toString().trim();

        vendor.setBillingAddress(strBillingAddress);
        vendor.setBillingCity(strBillingCity);
        vendor.setBillingPhone(strBillingPhone);
        vendor.setBillingCountry(strBillingCountry);
        vendor.setBillingState(strBillingState.equalsIgnoreCase(resources.getString(R.string.select_state)) ? "" : strBillingState);
        vendor.setDistrict(strBillingDistrict);
        vendor.setBillingZipCode(strBillingZipCode);
        vendor.setShippingAddress(strShippingAddress);
        vendor.setShippingCity(strShippingCity);
        vendor.setShippingPhone(strShippingPhone);
        vendor.setShippingCountry(strShippingCountry);
        vendor.setShippingState(strShippingState.equalsIgnoreCase(resources.getString(R.string.select_state)) ? "" : strShippingState);
        vendor.setShippingZipCode(strShippingZipCode);

        if (invalidate()) {
            Intent intent = new Intent(this, CustomFieldActivity.class);
            intent.putExtra(ConstantVariable.MIX_ID.VENDOR, vendor);
            intent.putExtra(ConstantVariable.MIX_ID.ACTION, action);
            startActivityForResult(intent, 1);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        }
    }

    private boolean invalidate() {
        boolean valid = true;
        if (vendor.getBillingAddress().isEmpty()) {
            showMessage("Billing address is required");
            valid = false;
        } else if (vendor.getBillingCity().isEmpty()) {
            showMessage("Billing city is required");
            valid = false;
        } else if (vendor.getBillingState().isEmpty()) {
            showMessage("Billing state is required");
            valid = false;
        } else if (TextUtils.isEmpty(vendor.getDistrict())) {
            showMessage("Billing district is required");
            valid = false;
        } else if (vendor.getBillingZipCode().isEmpty()) {
            showMessage("Billing zipcode is required");
            valid = false;
        } else if (vendor.getBillingCountry().isEmpty()) {
            showMessage("Billing country is required");
            valid = false;
        } else if (vendor.getBillingPhone().isEmpty()) {
            showMessage("Billing phone is required");
            valid = false;
        }
        return valid;
    }

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            shippingAddress.setText(billingAddress.getText().toString().trim());
            shippingCity.setText(billingCity.getText().toString().trim());
            shippingCountry.setText(billingCountry.getText().toString().trim());
            shippingPhone.setText(billingPhone.getText().toString());
            int billingStatePosition = billingStateSpinner.getSelectedItemPosition();
            shippingDistrict.setText(strBillingDistrict);
            shippingState.setSelection(billingStatePosition);
            shippingZipCode.setText(billingZipCode.getText().toString().trim());
        } else {
            shippingAddress.setText("");
            shippingCity.setText("");
            shippingDistrict.setText("");
            shippingCountry.setText("");
            shippingPhone.setText("");
            shippingState.setSelection(0);
            shippingZipCode.setText("");

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (view != null) {
            view.setPadding(2, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
            ((TextView) view).setTextColor(Color.BLACK);
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.mediumTextSize));

            if (adapterView.getId() == R.id.billingStateSpinner) {
                String state = adapterView.getSelectedItem().toString();
                billingDistrict.setText(vendor.getDistrict());
                strBillingDistrict = vendor.getDistrict();
                if (!state.equalsIgnoreCase(resources.getString(R.string.select_state))) {
                    List<String> districtList = ConstantVariable.Value.getList(state);
                    ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(AddressActivity.this, R.layout.layout_autocomplete_item, districtList);
                    billingDistrict.setThreshold(1);
                    billingDistrict.setAdapter(districtAdapter);
                    billingDistrict.setOnItemClickListener(this);
                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        strBillingDistrict = adapterView.getItemAtPosition(i).toString();
    }
}
