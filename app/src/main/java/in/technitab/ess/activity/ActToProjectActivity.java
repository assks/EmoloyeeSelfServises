package in.technitab.ess.activity;

import android.app.Activity;
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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.adapter.AutoCompleteAdapter;
import in.technitab.ess.adapter.UserAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.listener.RecyclerViewItemClickListener;
import in.technitab.ess.model.ProjectActivity;
import in.technitab.ess.model.StringResponse;
import in.technitab.ess.model.User;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActToProjectActivity extends AppCompatActivity implements RecyclerViewItemClickListener {

    @BindView(R.id.projectName)
    TextView projectName;

    @BindView(R.id.activityRecyclerView)
    RecyclerView activityRecyclerView;
    @BindView(R.id.user_name)
    EditText userName;
    @BindView(R.id.userRecyclerView)
    RecyclerView userRecyclerView;

    private ArrayList<Object> mProjectActivityList;
    private ArrayList<User> mUserArrayList;
    private AutoCompleteAdapter adapter;
    private UserAdapter mUserAdapter;
    private NetConnection connection;
    private Dialog dialog;
    private RestApi api;
    private UserPref userPref;
    private Handler handler;
    private Runnable runnable;
    private int projectId = 0, projectTypeId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_to_project);
        ButterKnife.bind(this);

        init();
        setToolbar();
        getActivityList();
        userName.addTextChangedListener(new MyCustomerNameWatcher());

    }


    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void getActivityList() {
        mProjectActivityList.clear();
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<ArrayList<ProjectActivity>> call = api.fetchActivities(projectId, projectTypeId);
            call.enqueue(new Callback<ArrayList<ProjectActivity>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<ProjectActivity>> call, @NonNull Response<ArrayList<ProjectActivity>> response) {
                    if (response.isSuccessful()) {
                        dialog.dismissDialog();
                        ArrayList<ProjectActivity> customers = response.body();
                        if (customers != null) {
                            mProjectActivityList.addAll(customers);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<ProjectActivity>> call, @NonNull Throwable t) {
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


    private void showSnackBar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    private void init() {
        connection = new NetConnection();
        userPref = new UserPref(this);
        dialog = new Dialog(this);
        api = APIClient.getClient().create(RestApi.class);
        handler = new Handler();
        mProjectActivityList = new ArrayList<>();
        mUserArrayList = new ArrayList<>();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            projectId = bundle.getInt(ConstantVariable.Project.ID);
            projectTypeId = bundle.getInt(ConstantVariable.Project.PROJECT_TYPE_ID);
            String strProjectName = bundle.getString(ConstantVariable.Project.PROJECT_NAME);
            projectName.setText(strProjectName);
        }

        activityRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        activityRecyclerView.setHasFixedSize(false);
        activityRecyclerView.setNestedScrollingEnabled(false);
        adapter = new AutoCompleteAdapter(getResources().getString(R.string.activity), mProjectActivityList);
        activityRecyclerView.setAdapter(adapter);
        adapter.setonItemClickListener(this);

        userRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        userRecyclerView.setHasFixedSize(false);
        userRecyclerView.setNestedScrollingEnabled(false);
        mUserAdapter = new UserAdapter(mUserArrayList);
        userRecyclerView.setAdapter(mUserAdapter);

    }


    @OnClick(R.id.submit)
    protected void onSubmit() {
        int userId = 0;
        for (User user : mUserArrayList) {
            if (user.isSelected()) {
                userId = user.getId();
            }
        }

        if (userId != 0) {
            Gson gson = new Gson();
            String projectActivityJson = gson.toJson(mProjectActivityList);
            if (connection.isNetworkAvailable(this)) {
                dialog.showDialog();
                Call<StringResponse> call = api.addActivityUser(userId, userPref.getUserId(), projectId, projectActivityJson);
                call.enqueue(new Callback<StringResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                        dialog.dismissDialog();
                        if (response.isSuccessful()) {
                            StringResponse stringResponse = response.body();
                            if (stringResponse != null) {
                                if (!stringResponse.isError()) {
                                    startHomeActivity();
                                }
                                showSnackBar(stringResponse.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<StringResponse> call, @NonNull Throwable t) {
                        dialog.dismissDialog();
                        if (t instanceof SocketTimeoutException) {
                            showSnackBar(getResources().getString(R.string.slow_internet_connection));
                        }
                    }
                });
            } else {
                showSnackBar(getResources().getString(R.string.internet_not_available));
            }
        } else {
            showSnackBar("User name is required");
        }
    }

    private void startHomeActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onBackPressed();
                finish();
            }
        }, 1000);
    }

    @Override
    public void onClickListener(RecyclerView.ViewHolder viewHolder, int position) {

    }


    private void getUserName(String str) {
        mUserArrayList.clear();

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
                            mUserArrayList.addAll(customers);
                            mUserAdapter.notifyDataSetChanged();
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


    private class MyCustomerNameWatcher implements TextWatcher {

        MyCustomerNameWatcher() {
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
                        getUserName(charSequence.toString().trim());
                    }
                }
            };
            handler.postDelayed(runnable, 500);

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
