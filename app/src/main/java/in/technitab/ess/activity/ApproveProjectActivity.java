package in.technitab.ess.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.model.AddResponse;
import in.technitab.ess.model.Project;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.CustomDate;
import in.technitab.ess.util.CustomEditText;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApproveProjectActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = ApproveProjectActivity.class.getSimpleName();
    @BindView(R.id.project_type)
    Spinner projectType;
    @BindView(R.id.billing_type)
    Spinner billingType;
    @BindView(R.id.budget_amount)
    EditText budgetAmount;
    @BindView(R.id.project_cost_input_layout)
    TextInputLayout projectCostInputLayout;
    @BindView(R.id.budget_type)
    Spinner budgetType;
    @BindView(R.id.project_budget_hours)
    EditText projectBudgetHours;
    @BindView(R.id.start_date)
    EditText startDate;
    @BindView(R.id.end_date)
    EditText endDate;
    @BindView(R.id.customerName)
    EditText customerName;
    @BindView(R.id.projectName)
    EditText projectName;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.location)
    EditText location;
    @BindView(R.id.district)
    CustomEditText district;
    @BindView(R.id.state)
    CustomEditText state;
    @BindView(R.id.country)
    CustomEditText country;
    @BindView(R.id.address)
    EditText address;
    @BindView(R.id.clientName)
    EditText clientName;
    @BindView(R.id.estimated_days)
    EditText estimatedDays;
    @BindView(R.id.job_action_sheet)
    CheckBox jobActionSheet;
    @BindView(R.id.system_backup)
    CheckBox systemBackup;
    private UserPref userPref;
    private NetConnection connection;
    private Dialog dialog;
    RestApi api;
    private int customerId = 0, projectTypeId = 1;
    private String strClientName, strBillingType, action;
    private Resources resources;
    private int position;
    private Project project;
    private List<String> projectTypeList, billingTypeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_project);
        ButterKnife.bind(this);

        init();
        setupSpinner();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            action = bundle.getString(ConstantVariable.MIX_ID.ACTION);
            if (action.equalsIgnoreCase(resources.getString(R.string.approve)) || action.equalsIgnoreCase(ConstantVariable.MIX_ID.UPDATE)) {
                project = bundle.getParcelable(resources.getString(R.string.project));
                position = bundle.getInt(ConstantVariable.MIX_ID.ID);
                showDetail(project);

            } else if (action.equalsIgnoreCase(ConstantVariable.MIX_ID.SUBMIT) || action.equalsIgnoreCase(ConstantVariable.MIX_ID.ADD)) {
                customerId = bundle.getInt(ConstantVariable.UserPrefVar.USER_ID);
                strClientName = bundle.getString(ConstantVariable.UserPrefVar.NAME);
                clientName.setText(strClientName);
                project.setCustomerId(customerId);
                strClientName = bundle.getString(ConstantVariable.UserPrefVar.NAME);
                project.setClientName(strClientName);
                clientName.setText(strClientName);
                country.setText(bundle.getString(ConstantVariable.Project.COUNTRY));
                project.setCountry(bundle.getString(ConstantVariable.Project.COUNTRY));
                state.setText(bundle.getString(ConstantVariable.Project.STATE));
                project.setState(bundle.getString(ConstantVariable.Project.STATE));
                district.setText(bundle.getString(ConstantVariable.Project.DISTRICT));
                project.setDistrict(bundle.getString(ConstantVariable.Project.DISTRICT));

            }
        }
        setToolbar();

    }


    private void init() {
        resources = getResources();
        userPref = new UserPref(this);
        connection = new NetConnection();
        dialog = new Dialog(this);
        api = APIClient.getClient().create(RestApi.class);
        project = new Project();
        new CustomDate(startDate, this, null, null);
        new CustomDate(endDate, this, null, null);
    }


    private void showDetail(Project project) {
        strClientName = project.getClientName();
        clientName.setText(strClientName);
        customerId = project.getCustomerId();
        projectName.setText(project.getProjectName());
        customerName.setText(project.getCustomerName());

        String projectType = project.getProjectType();
        if (projectTypeList.contains(projectType)) {
            int selectedProjectTypePosition = projectTypeList.indexOf(projectType);
            this.projectType.setSelection(selectedProjectTypePosition);
        }

        String billingType = project.getBillingType();
        if (billingTypeList.contains(billingType)) {
            int selectedBillingTypePosition = billingTypeList.indexOf(billingType);
            this.billingType.setSelection(selectedBillingTypePosition);
        }
        budgetAmount.setText(String.valueOf(project.getBudgetAmount()));
        projectBudgetHours.setText(project.getProjectBudgetHours());
        district.setText(project.getDistrict());
        country.setText(project.getCountry());
        state.setText(project.getState());
        address.setText(project.getAddress());
        location.setText(project.getLocation());
        startDate.setText(project.getPlannedStartDate());
        endDate.setText(project.getPlannedEndDate());
        description.setText(project.getDescription());
        estimatedDays.setText(String.valueOf(project.getEstimatedDays()));
        jobActionSheet.setChecked(true);
        systemBackup.setChecked(true);
        submit.setText(resources.getString(R.string.approve));
    }

    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(action.equalsIgnoreCase(ConstantVariable.MIX_ID.APPROVE) ? resources.getString(R.string.approve_project) : resources.getString(R.string.add_project));
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @OnClick(R.id.clientName)
    protected void onClientName() {
        Intent intent = new Intent(this, PickCustomerNameActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            customerId = data.getIntExtra(ConstantVariable.MIX_ID.ID, -1);
            strClientName = data.getStringExtra(ConstantVariable.UserPrefVar.NAME);
            clientName.setText(strClientName);
        }

    }

    private void setupSpinner() {
        projectTypeList = Arrays.asList(getResources().getStringArray(R.array.projectTypeArray));
        ArrayAdapter<String> projectTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, projectTypeList);
        projectType.setAdapter(projectTypeAdapter);
        projectType.setOnItemSelectedListener(this);

        billingTypeList = Arrays.asList(getResources().getStringArray(R.array.billingTypeArray));
        ArrayAdapter<String> billingTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, billingTypeList);
        billingType.setAdapter(billingTypeAdapter);
        billingType.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        view.setPadding(4, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        switch (adapterView.getId()) {

            case R.id.billing_type:
                strBillingType = billingType.getSelectedItem().toString();
                if (strBillingType.equalsIgnoreCase(resources.getString(R.string.fixCost))) {
                    projectCostInputLayout.setVisibility(View.VISIBLE);
                } else {
                    projectCostInputLayout.setVisibility(View.GONE);

                }
                break;

            case R.id.project_type:
                projectTypeId = projectType.getSelectedItemPosition() + 1;
                project.setProjectTypeId(projectTypeId);
                break;
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @OnClick(R.id.submit)
    protected void onSubmit() {
        project.setProjectName(projectName.getText().toString().trim());
        String strBudgetAmount = budgetAmount.getText().toString().trim();
        project.setBudgetAmount(TextUtils.isEmpty(strBudgetAmount) ? 0 : Double.parseDouble(strBudgetAmount));
        project.setProjectBudgetHours(projectBudgetHours.getText().toString().trim());
        project.setPlannedStartDate(startDate.getText().toString().trim());
        project.setPlannedEndDate(endDate.getText().toString().trim());
        project.setProjectType(projectType.getSelectedItem().toString());
        project.setDescription(description.getText().toString().trim());
        project.setLocation(location.getText().toString().trim());
        project.setCountry(country.getText().toString().trim());
        project.setIsInternational(project.getCountry().equalsIgnoreCase(resources.getString(R.string.default_country)) ? 0 : 1);
        project.setState(state.getText().toString().trim());
        project.setBillingType(billingType.getSelectedItem().toString());
        project.setDistrict(district.getText().toString().trim());
        project.setAddress(address.getText().toString().trim());
        project.setCustomerName(customerName.getText().toString().trim());
        String strEstimatedDays = estimatedDays.getText().toString().trim();
        project.setEstimatedDays(TextUtils.isEmpty(strEstimatedDays) ? 0 : Integer.parseInt(strEstimatedDays));
        boolean isJobAllocationSheet = jobActionSheet.isChecked();
        project.setIsJobActionSheet(isJobAllocationSheet ? 1 : 0);
        boolean isSystemBackUp = systemBackup.isChecked();
        project.setIsSystemBackup(isSystemBackUp ? 1 : 0);
        if (action.equalsIgnoreCase(ConstantVariable.MIX_ID.APPROVE)) {
            project.setModifiedById(Integer.parseInt(userPref.getUserId()));
        } else {
            project.setCreatedById(Integer.parseInt(userPref.getUserId()));
        }


        if (invalidate()) {
            onUpdate();
        }
    }

    private void onUpdate() {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();

            Gson gson = new Gson();
            String projectJson = gson.toJson(project);
            Log.d(TAG, projectJson);
            Call<AddResponse> call;
            call = api.approveProject(action, projectJson);
            call.enqueue(new Callback<AddResponse>() {
                @Override
                public void onResponse(@NonNull Call<AddResponse> call, @NonNull Response<AddResponse> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        AddResponse stringResponse = response.body();
                        if (stringResponse != null) {
                            if (!stringResponse.isError()) {
                                project.setId(stringResponse.getId());
                                Intent intent = new Intent();
                                intent.putExtra(resources.getString(R.string.project), project);
                                intent.putExtra(ConstantVariable.MIX_ID.ID, position);
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                            } else {
                                showMessage(stringResponse.getMessage());
                            }
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

        } else {
            showMessage(resources.getString(R.string.internet_not_available));
        }
    }

    private boolean invalidate() {
        boolean valid = true;

        if (TextUtils.isEmpty(project.getCustomerName())) {
            showMessage("Customer Name is required");
            valid = false;
        } else if (TextUtils.isEmpty(project.getProjectName())) {
            showMessage("Project name is required");
            valid = false;
        } else if (TextUtils.isEmpty(project.getLocation())) {
            showMessage("Project Location is required");
            valid = false;
        } else if (TextUtils.isEmpty(project.getCountry())) {
            showMessage("Country is required");
            valid = false;
        } else if (TextUtils.isEmpty(project.getAddress())) {
            showMessage("Address is required");
            valid = false;
        } else if (project.getBudgetAmount() == 0 && project.getBillingType().equalsIgnoreCase(resources.getString(R.string.fixCost))) {
            showMessage("Budget amount is required");
            valid = false;
        } else if (TextUtils.isEmpty(project.getPlannedStartDate())) {
            showMessage("Start date is required");
            valid = false;
        } else if (TextUtils.isEmpty(project.getPlannedEndDate())) {
            showMessage("Estimated end date is required");
            valid = false;
        } else if (TextUtils.isEmpty(project.getDescription())) {
            showMessage("Scope of project is required");
            valid = false;
        } /*else if (project.getDescription().length() < 50) {
            showMessage("Scope of work must have 50 characters");
            valid = false;
        }*/ else if (project.getEstimatedDays() < 1) {
            showMessage("Estimated days are required");
            valid = false;
        } else if (project.getEstimatedDays() == 0) {
            showMessage("Estimated days could not be zero");
            valid = false;
        } else if (project.getIsJobActionSheet() == 0) {
            showMessage("Do you have job allocation sheet");
            valid = false;
        } else if (project.getIsSystemBackup() == 0) {
            showMessage("Do you have system backup");
            valid = false;
        }
        return valid;
    }

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
