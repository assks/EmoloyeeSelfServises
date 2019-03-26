package in.technitab.ess.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.technitab.ess.R;
import in.technitab.ess.adapter.AutoCompleteAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.listener.RecyclerViewItemClickListener;

import in.technitab.ess.model.Customer;
import in.technitab.ess.model.Project;
import in.technitab.ess.model.User;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchCustomNameActivity extends AppCompatActivity implements RecyclerViewItemClickListener {

    private static final int RC_ADD_PROJECT = 1;
    private static final String TAG = FetchCustomNameActivity.class.getSimpleName();
    @BindView(R.id.project_budget_hours)
    EditText projectBudgetHours;
    @BindView(R.id.customerRecyclerView)
    RecyclerView customerRecyclerView;

    ArrayList<Object> mObjectArrayList;
    private AutoCompleteAdapter adapter;
    private NetConnection connection;
    private Dialog dialog;
    RestApi api;
    private Handler handler;
    private Runnable runnable;
    private String actionViewType,action="";
    private Resources resources;
    private UserPref userPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_custom_name);
        ButterKnife.bind(this);

        init();
        setToolbar();

        projectBudgetHours.addTextChangedListener(new MyCustomerNameWatcher(projectBudgetHours));
    }


    private void init() {
        connection = new NetConnection();
        dialog = new Dialog(this);
        api = APIClient.getClient().create(RestApi.class);
        handler = new Handler();
        mObjectArrayList = new ArrayList<>();
        userPref = new UserPref(this);
        resources = getResources();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            actionViewType = bundle.getString(ConstantVariable.MIX_ID.ACTION_VIEW_TYPE);
            action = bundle.getString(ConstantVariable.MIX_ID.ACTION);
            Log.d(TAG,"action "+action);
        }

        customerRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        customerRecyclerView.setHasFixedSize(false);
        adapter = new AutoCompleteAdapter(actionViewType, mObjectArrayList);
        customerRecyclerView.setAdapter(adapter);
        adapter.setonItemClickListener(this);
    }


    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            actionBar.setTitle(actionViewType);
            projectBudgetHours.setHint(actionViewType.equalsIgnoreCase(getResources().getString(R.string.add_timesheet))?getResources().getString(R.string.user_name):actionViewType);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void onClickListener(RecyclerView.ViewHolder viewHolder, int position) {

        if (actionViewType.equalsIgnoreCase(getResources().getString(R.string.customer_name))) {

            Customer customer = (Customer) mObjectArrayList.get(position);
            Intent intent;
            if (userPref.getAccessControlId() == resources.getInteger(R.integer.admin))
                intent = new Intent(this, ApproveProjectActivity.class);
            else
                intent = new Intent(this, AddProjectActivity.class);
            intent.putExtra(ConstantVariable.UserPrefVar.USER_ID, customer.getId());
            intent.putExtra(ConstantVariable.UserPrefVar.NAME, customer.getCustomerName());
            intent.putExtra(ConstantVariable.MIX_ID.ACTION,action);
            startActivityForResult(intent,RC_ADD_PROJECT);

        } else if (actionViewType.equalsIgnoreCase(getResources().getString(R.string.project_name))) {
            Project project = (Project) mObjectArrayList.get(position);
            Intent intent = new Intent(this, ActToProjectActivity.class);
            intent.putExtra(ConstantVariable.Project.ID, project.getId());
            intent.putExtra(ConstantVariable.Project.PROJECT_NAME, project.getProjectName());
            intent.putExtra(ConstantVariable.Project.PROJECT_TYPE_ID, project.getProjectTypeId());
            startActivity(intent);
            finish();
        } else if (actionViewType.equalsIgnoreCase(getResources().getString(R.string.user_name))) {
            User user = (User) mObjectArrayList.get(position);
            Intent intent = new Intent(this, AssignLeaveActivity.class);
            intent.putExtra(ConstantVariable.UserPrefVar.USER_ID, user.getId());
            intent.putExtra(ConstantVariable.UserPrefVar.NAME, user.getName());
            startActivity(intent);
            finish();
        } else if (actionViewType.equalsIgnoreCase(getResources().getString(R.string.project))) {
            Project project = (Project) mObjectArrayList.get(position);
            Intent intent = new Intent(this, ApproveTimeActivity.class);
            intent.putExtra(ConstantVariable.Project.ID, project.getId());
            intent.putExtra(ConstantVariable.Project.PROJECT_NAME, project.getProjectName());
            startActivity(intent);
            finish();
        } else if (actionViewType.equalsIgnoreCase(getResources().getString(R.string.add_timesheet))) {
            User user = (User) mObjectArrayList.get(position);
            Intent intent = new Intent(this, AddTimesheetActivity.class);
            intent.putExtra(ConstantVariable.UserPrefVar.USER_ID, user.getId());
            intent.putExtra(ConstantVariable.UserPrefVar.NAME, user.getName());
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_ADD_PROJECT && resultCode == Activity.RESULT_OK && data != null){
            setResult(Activity.RESULT_OK,data);
            finish();
        }
    }

    private class MyCustomerNameWatcher implements TextWatcher {
        private View view;

        public MyCustomerNameWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(final CharSequence charSequence, int i, int i1, int i2) {
            handler.removeCallbacks(runnable);
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (charSequence.length() > 0) {
                        if (actionViewType.equalsIgnoreCase(getResources().getString(R.string.customer_name))) {
                            getCustomName(charSequence.toString().trim());
                        } else if (actionViewType.equalsIgnoreCase(getResources().getString(R.string.project_name))) {
                            getProjectNames(charSequence.toString().trim());
                        } else if (actionViewType.equalsIgnoreCase(getResources().getString(R.string.user_name))) {
                            getUserName(charSequence.toString().trim());
                        } else if (actionViewType.equalsIgnoreCase(getResources().getString(R.string.project))) {
                            getProjectNamesAsTL(charSequence.toString().trim());
                        } else if (actionViewType.equalsIgnoreCase(getResources().getString(R.string.add_timesheet))) {
                            getUserName(charSequence.toString().trim());
                        }
                    }
                }
            };
            handler.postDelayed(runnable, 500);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    private void getProjectNamesAsTL(String projectHint) {
        mObjectArrayList.clear();
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<ArrayList<Project>> call = api.fetchApproveTs(projectHint);
            call.enqueue(new Callback<ArrayList<Project>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Project>> call, @NonNull Response<ArrayList<Project>> response) {
                    if (response.isSuccessful()) {
                        dialog.dismissDialog();
                        ArrayList<Project> customers = response.body();
                        if (customers != null) {
                            hideKeyboard();
                            mObjectArrayList.addAll(customers);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Project>> call, @NonNull Throwable t) {
                    dialog.dismissDialog();
                    if (t instanceof SocketTimeoutException) {
                        showSnackBar(getResources().getString(R.string.slow_internet_connection));
                    }
                }
            });
        } else {
            showSnackBar(getResources().getString(R.string.internet_not_available));
        }
    }

    private void getProjectNames(String projectHint) {
        mObjectArrayList.clear();
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<ArrayList<Project>> call = api.fetchProjectList(projectHint);
            call.enqueue(new Callback<ArrayList<Project>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Project>> call, @NonNull Response<ArrayList<Project>> response) {
                    if (response.isSuccessful()) {
                        dialog.dismissDialog();
                        ArrayList<Project> customers = response.body();
                        if (customers != null) {
                            hideKeyboard();
                            mObjectArrayList.addAll(customers);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Project>> call, @NonNull Throwable t) {
                    dialog.dismissDialog();
                    if (t instanceof SocketTimeoutException) {
                        showSnackBar(getResources().getString(R.string.slow_internet_connection));
                    }
                }
            });
        } else {
            showSnackBar(getResources().getString(R.string.internet_not_available));
        }
    }


    private void getCustomName(String str) {
        mObjectArrayList.clear();

        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<ArrayList<Customer>> call = api.fetchCustomerList(str);
            call.enqueue(new Callback<ArrayList<Customer>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Customer>> call, @NonNull Response<ArrayList<Customer>> response) {
                    if (response.isSuccessful()) {
                        dialog.dismissDialog();
                        ArrayList<Customer> customers = response.body();
                        if (customers != null) {
                            hideKeyboard();
                            mObjectArrayList.addAll(customers);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Customer>> call, @NonNull Throwable t) {
                    dialog.dismissDialog();
                    if (t instanceof SocketTimeoutException) {
                        showSnackBar(getResources().getString(R.string.slow_internet_connection));
                    }
                }
            });
        } else {
            showSnackBar(getResources().getString(R.string.internet_not_available));
        }
    }


    private void getUserName(String str) {
        mObjectArrayList.clear();

        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<ArrayList<User>> call = api.fetchUserNames(str);
            call.enqueue(new Callback<ArrayList<User>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<User>> call, @NonNull Response<ArrayList<User>> response) {
                    if (response.isSuccessful()) {
                        dialog.dismissDialog();
                        ArrayList<User> customers = response.body();
                        if (customers != null) {
                            hideKeyboard();
                            mObjectArrayList.addAll(customers);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<User>> call, @NonNull Throwable t) {
                    dialog.dismissDialog();
                    if (t instanceof SocketTimeoutException) {
                        showSnackBar(getResources().getString(R.string.slow_internet_connection));
                    }
                }
            });
        } else {
            showSnackBar(getResources().getString(R.string.internet_not_available));
        }
    }


    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
