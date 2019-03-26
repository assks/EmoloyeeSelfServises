package in.technitab.ess.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.adapter.FindProjectAdapter;
import in.technitab.ess.adapter.SpinAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.listener.RecyclerViewItemClickListener;
import in.technitab.ess.model.Customer;
import in.technitab.ess.model.Project;
import in.technitab.ess.model.StringResponse;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindCustomerActivity extends AppCompatActivity implements RecyclerViewItemClickListener, AdapterView.OnItemSelectedListener {


    @BindView(R.id.stateLayout)
    TextInputLayout stateLayout;
    @BindView(R.id.districtLayout)
    TextInputLayout districtLayout;

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @BindView(R.id.itemRecyclerView)
    RecyclerView itemRecyclerView;
    @BindView(R.id.countrySpinner)
    Spinner country;
    @BindView(R.id.state)
    AutoCompleteTextView state;
    @BindView(R.id.customerName)
    Spinner customerName;
    @BindView(R.id.district)
    AutoCompleteTextView district;
    @BindView(R.id.clientName)
    TextView clientName;
    @BindView(R.id.clientLocation)
    TextView clientLocation;
    @BindView(R.id.projectCollapseView)
    LinearLayout projectCollapseView;
    @BindView(R.id.expandView)
    RelativeLayout expandView;
    private ArrayList<Object> mProjectArrayList;
    private FindProjectAdapter adapter;
    private Resources resources;
    private UserPref userPref;
    private NetConnection connection;
    private Dialog dialog;
    RestApi api;
    private ArrayList<Object> mCustomerList;
    private String strCountry = "", strState = "", strDistrict = "", strCustomerName = "";
    private int custId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_project);
        ButterKnife.bind(this);

        init();
        setToolbar();
        bindAutoCompleteTextView();
        customerName.setOnItemSelectedListener(this);
    }

    private void bindAutoCompleteTextView() {
        List<String> reasonList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.countryArray)));
        reasonList.add(0, resources.getString(R.string.select_country));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reasonList);
        country.setAdapter(adapter);
        country.setOnItemSelectedListener(this);

        state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                strState = String.valueOf(adapterView.getItemAtPosition(position));
                hideKeyboard();

                List<String> districtList = ConstantVariable.Value.getList(strState);
                ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(FindCustomerActivity.this, R.layout.layout_autocomplete_item, districtList);
                district.setThreshold(1);
                district.setAdapter(districtAdapter);
            }
        });

        district.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                strDistrict = String.valueOf(adapterView.getItemAtPosition(position));
                hideKeyboard();
                getCustomerName(strState);
            }
        });
    }

    private void init() {
        resources = getResources();
        connection = new NetConnection();
        dialog = new Dialog(this);
        userPref = new UserPref(this);
        api = APIClient.getClient().create(RestApi.class);
        mCustomerList = new ArrayList<>();
        mProjectArrayList = new ArrayList<>();

        itemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemRecyclerView.setHasFixedSize(true);
        adapter = new FindProjectAdapter(this, mProjectArrayList);
        itemRecyclerView.setAdapter(adapter);
        adapter.setListener(this);

        Bundle bundle = getIntent().getExtras();

    }

    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(resources.getString(R.string.find_project));
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void getCustomerName(String strState) {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<ArrayList<Customer>> call = api.fetchCustomer(strState);
            call.enqueue(new Callback<ArrayList<Customer>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Customer>> call, @NonNull Response<ArrayList<Customer>> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        ArrayList<Customer> list = response.body();
                        if (list != null) {
                            if (!mCustomerList.isEmpty()) {
                                mCustomerList.clear();
                            }
                            setSpinner(list);
                        }
                    } else {
                        showToast(resources.getString(R.string.problem_to_connect));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Customer>> call, @NonNull Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        dialog.dismissDialog();
                        showToast(resources.getString(R.string.slow_internet_connection));
                    }
                }
            });
        } else {
            showToast(resources.getString(R.string.internet_not_available));
        }
    }

    private void setSpinner(ArrayList<Customer> list) {
        if (list.isEmpty()) {
            mCustomerList.add(resources.getString(R.string.no_customer));
        }
        mCustomerList.addAll(list);
        SpinAdapter suggestionAdapter = new SpinAdapter(this, android.R.layout.simple_list_item_1, mCustomerList);
        customerName.setAdapter(suggestionAdapter);
    }

    private void showToast(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (view != null) {
            view.setPadding(2, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
            ((TextView) view).setTextColor(Color.BLACK);
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.mediumTextSize));

            switch (adapterView.getId()) {

                case R.id.customerName:
                    if (mCustomerList.get(position) instanceof Customer) {
                        custId = ((Customer) mCustomerList.get(position)).getId();
                        strCustomerName = ((Customer) mCustomerList.get(position)).getCustomerName();
                    } else {
                        custId = 0;
                        strCustomerName = "";
                    }
                    break;

                case R.id.countrySpinner:
                    strCountry = country.getSelectedItem().toString();
                    if (strCountry.equalsIgnoreCase("India")) {
                        stateLayout.setVisibility(View.VISIBLE);
                        districtLayout.setVisibility(View.VISIBLE);
                        state.setText("");
                        district.setText("");
                        List<String> stateList = Arrays.asList(resources.getStringArray(R.array.stateArray));
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(FindCustomerActivity.this, R.layout.layout_autocomplete_item, stateList);
                        state.setThreshold(1);
                        state.setAdapter(adapter);
                    } else {
                        stateLayout.setVisibility(View.GONE);
                        districtLayout.setVisibility(View.GONE);

                        if (!strCountry.equalsIgnoreCase(resources.getString(R.string.select_country))) {
                            getCustomerName(resources.getString(R.string.international));
                        }
                    }
                    break;

            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @OnClick(R.id.findProject)
    protected void onFindProject() {
        if (invalidate(strCountry, strState, custId)) {
            projectCollapseView.setVisibility(View.GONE);
            expandView.setVisibility(View.VISIBLE);
            clientName.setText(strCustomerName);
            clientLocation.setText(strCountry + " | " + strState + " | " + strDistrict);
            getProjectList(strCountry, strState, custId);
        }
    }

    @OnClick(R.id.expandButtonView)
    protected void onExpandButtonView() {
        mProjectArrayList.clear();
        adapter.notifyDataSetChanged();
        expandView.setVisibility(View.GONE);
        projectCollapseView.setVisibility(View.VISIBLE);
    }

    private boolean invalidate(String strCountry, String strState, int custId) {
        boolean valid = true;
        if (strCountry.isEmpty()) {
            showToast("Country is required");
            valid = false;
        } else if (strState.isEmpty()) {
            showToast("Country is required");
            valid = false;
        } else if (custId == 0) {
            showToast("Customer name is required");
            valid = false;
        }
        return valid;
    }

    private void getProjectList(String strCountry, String strState, int custId) {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<ArrayList<Project>> call = api.fetchUnassignedProject(custId, userPref.getUserId(), userPref.getRoleId(), strState, strCountry);
            call.enqueue(new Callback<ArrayList<Project>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Project>> call, @NonNull Response<ArrayList<Project>> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {

                        ArrayList<Project> list = response.body();
                        if (list != null) {
                            if (!mProjectArrayList.isEmpty()) {
                                mProjectArrayList.clear();
                                adapter.notifyDataSetChanged();
                            }
                            mProjectArrayList.add(0, resources.getString(R.string.create_new_project));
                            mProjectArrayList.addAll(list);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        showToast(resources.getString(R.string.problem_to_connect));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Project>> call, @NonNull Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        dialog.dismissDialog();
                        showToast(resources.getString(R.string.slow_internet_connection));
                    }
                }
            });
        } else {
            showToast(resources.getString(R.string.internet_not_available));
        }
    }

    @Override
    public void onClickListener(RecyclerView.ViewHolder viewHolder, final int position) {
        if (mProjectArrayList.get(position) instanceof String) {
            Intent intent;
            if (userPref.getAccessControlId() != resources.getInteger(R.integer.admin)) {
                intent = new Intent(this, AddProjectActivity.class);
                intent.putExtra(ConstantVariable.MIX_ID.ACTION, ConstantVariable.MIX_ID.SUBMIT);
            }
            else {
                intent = new Intent(this, ApproveProjectActivity.class);
                intent.putExtra(ConstantVariable.MIX_ID.ACTION, ConstantVariable.MIX_ID.ADD);
            }
            intent.putExtra(ConstantVariable.UserPrefVar.USER_ID, custId);
            intent.putExtra(ConstantVariable.UserPrefVar.NAME, strCustomerName);
            intent.putExtra(ConstantVariable.Project.COUNTRY, strCountry);
            String state = (!strCountry.equalsIgnoreCase(resources.getString(R.string.default_country))) ? "International" : strState;
            intent.putExtra(ConstantVariable.Project.STATE, state);
            String district = (!strCountry.equalsIgnoreCase(resources.getString(R.string.default_country))) ? "International" : strDistrict;
            intent.putExtra(ConstantVariable.Project.DISTRICT, district);

            startActivityForResult(intent, 1);

        } else {
            final Project project = (Project) mProjectArrayList.get(position);
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.create();
            builder.setMessage(resources.getString(R.string.request_project_message));
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
                    projectRequest(project.getId(),project.getProjectName());
                }
            });
            builder.show();
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            setResult(Activity.RESULT_OK,data);
            finish();
        }
    }

    private void projectRequest(int projectId,String projectName) {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<StringResponse> call = api.sendProjectRequest("request project",projectId,projectName, userPref.getUserId());
            call.enqueue(new Callback<StringResponse>() {
                @Override
                public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {

                        StringResponse stringResponse = response.body();
                        if (stringResponse != null) {
                            showToast(stringResponse.getMessage());
                        }
                    } else {
                        showToast(resources.getString(R.string.problem_to_connect));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<StringResponse> call, @NonNull Throwable t) {
                    dialog.dismissDialog();
                    if (t instanceof SocketTimeoutException) {
                        showToast(resources.getString(R.string.slow_internet_connection));
                    }
                }
            });
        } else {
            showToast(resources.getString(R.string.internet_not_available));
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
