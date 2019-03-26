package in.technitab.ess.activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.adapter.ApproveTimeSheetAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.model.ApproveTimeSheet;
import in.technitab.ess.model.StringResponse;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApproveTimeActivity extends AppCompatActivity implements ApproveTimeSheetAdapter.ProjectTaskListener, DatePickerDialog.OnDateSetListener {

    private String TAG = ApproveTimeActivity.class.getSimpleName();

    @BindView(R.id.projectName)
    EditText projectName;
    @BindView(R.id.date)
    EditText date;
    @BindView(R.id.timeSheetRecyclerView)
    RecyclerView timeSheetRecyclerView;

    private ArrayList<ApproveTimeSheet> mApproveTimeSheetArrayList;
    private ApproveTimeSheetAdapter mAdapter;

    private NetConnection connection;
    private Dialog dialog;
    private RestApi api;
    private UserPref userPref;
    private int projectId = 0;
    private String strProjectName = "";
    private DatePickerDialog startDataPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_time);
        ButterKnife.bind(this);

        init();
        setToolbar();
        initialiseDatePicker();

    }


    private void init() {
        connection = new NetConnection();
        dialog = new Dialog(this);
        userPref = new UserPref(this);
        api = APIClient.getClient().create(RestApi.class);
        mApproveTimeSheetArrayList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            projectId = bundle.getInt(ConstantVariable.Project.ID);
            strProjectName = bundle.getString(ConstantVariable.Project.PROJECT_NAME);
            projectName.setText(strProjectName);
        }

        timeSheetRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        timeSheetRecyclerView.setHasFixedSize(false);
        timeSheetRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new ApproveTimeSheetAdapter(this, mApproveTimeSheetArrayList);
        timeSheetRecyclerView.setAdapter(mAdapter);
        mAdapter.setTaskListener(this);

    }


    private void initialiseDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        startDataPicker = new DatePickerDialog(this, this, year, month, day);
    }


    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }


    @OnClick(R.id.date)
    protected void onDate() {
        startDataPicker.show();
    }


    @OnClick(R.id.submit)
    protected void OnSubmit(){

        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Gson gson = new Gson();
            String json = gson.toJson(mApproveTimeSheetArrayList);
            Call<StringResponse> call = api.updateTimesheet(userPref.getUserId(), json);

            call.enqueue(new Callback<StringResponse>() {
                @Override
                public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        StringResponse stringResponse = response.body();
                        if (stringResponse != null) {
                            showSnackBar(stringResponse.getMessage());
                        }
                    }
                }
                @Override
                public void onFailure(@NonNull Call<StringResponse> call, @NonNull Throwable t) {
                    dialog.dismissDialog();
                    if (t instanceof SocketTimeoutException){
                        showSnackBar(getResources().getString(R.string.slow_internet_connection));
                    }
                }
            });
        }else {
            showSnackBar(getResources().getString(R.string.internet_not_available));
        }
    }


    @Override
    public void onTextChange(RecyclerView.ViewHolder viewHolder, int position) {
        View view = getLayoutInflater().inflate(R.layout.bottomsheet_description, null);
        final EditText notes = view.findViewById(R.id.notes);
        ImageView send = view.findViewById(R.id.send);
        notes.setFocusable(false);
        send.setBackground(getResources().getDrawable(R.drawable.ic_more));

        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();
        notes.setText(mApproveTimeSheetArrayList.get(position).getDescription());
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int monthOfDate) {
        date.setText(getResources().getString(R.string.set_date_value, year, (month + 1), monthOfDate));
        fetchUserTimeSheet();

    }

    private void fetchUserTimeSheet() {
        String strDate = date.getText().toString().trim();
        mApproveTimeSheetArrayList.clear();
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<ArrayList<ApproveTimeSheet>> call = api.fetchApproveTimesheet(projectId, strDate);
            call.enqueue(new Callback<ArrayList<ApproveTimeSheet>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<ApproveTimeSheet>> call, @NonNull Response<ArrayList<ApproveTimeSheet>> response) {
                    if (response.isSuccessful()) {
                        dialog.dismissDialog();
                        Log.d(TAG, "response " + response.body());
                        ArrayList<ApproveTimeSheet> customers = response.body();
                        if (customers != null) {
                            mApproveTimeSheetArrayList.addAll(customers);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<ApproveTimeSheet>> call, @NonNull Throwable t) {
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
