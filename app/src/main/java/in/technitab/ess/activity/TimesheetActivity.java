package in.technitab.ess.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.adapter.ProjectTaskAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.database.ESSdb;
import in.technitab.ess.model.ProjectTask;
import in.technitab.ess.model.StringResponse;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.technitab.ess.util.DateCal.addTimeDuration;
import static in.technitab.ess.util.DateCal.getAmPmTime;
import static in.technitab.ess.util.DateCal.getHMFromData;
import static in.technitab.ess.util.DateCal.subtractTimeDuration;

public class TimesheetActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, ProjectTaskAdapter.ProjectTaskListener {

    private static final String TAG = TimesheetActivity.class.getSimpleName();
    @BindView(R.id.start_date)
    EditText startDate;
    @BindView(R.id.out_time)
    TextView outTime;
    @BindView(R.id.in_time)
    TextView inTime;
    @BindView(R.id.log_hours)
    TextView logHours;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.task_Recycler_view)
    RecyclerView taskSpinner;
    @BindView(R.id.submit)
    Button submit;

    private DatePickerDialog startDataPicker;
    private ArrayList<ProjectTask> mProjectTaskArrayList;
    private ArrayList<ProjectTask> mSelectedProjectTaskArrayList;
    private ProjectTaskAdapter adapter;
    String projectLogHours = "";
    private ESSdb db;
    private UserPref userPref;
    private Dialog dialog;
    private NetConnection connection;
    private RestApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timesheet);
        ButterKnife.bind(this);

        init();
        setToolbar();
        initialiseDatePicker();
        updateUI();
        getAssignmentProject();

    }

    private void getAssignmentProject() {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Call<ArrayList<ProjectTask>> call = api.fetchProjectAssignList(userPref.getUserId());
            call.enqueue(new Callback<ArrayList<ProjectTask>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<ProjectTask>> call, @NonNull Response<ArrayList<ProjectTask>> response) {
                    if (response.isSuccessful()) {
                        dialog.dismissDialog();
                        ArrayList<ProjectTask> list = response.body();
                        if (list != null) {
                            mProjectTaskArrayList.addAll(list);
                            adapter.notifyDataSetChanged();
                        } else {
                            showMessage(getResources().getString(R.string.no_projet_assigned));
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<ProjectTask>> call, @NonNull Throwable t) {
                    dialog.dismissDialog();
                    if (t instanceof SocketTimeoutException) {
                        showMessage(getResources().getString(R.string.slow_internet_connection));
                    }
                }
            });
        } else {
            showMessage(getResources().getString(R.string.internet_not_available));
        }
    }


    private void updateUI() {
        String date = startDate.getText().toString().trim();
        if (!date.isEmpty()) {
            String punchIn = db.getPunchInTime(date);
            String punchOut = db.getPunchOUTTime(date);


            if (punchIn.isEmpty() || punchOut.isEmpty()) {

                if (punchIn.isEmpty()) {
                    punchIn = db.getManualPunchInTime(date);
                    punchOut = db.getManualPunchOutTime(date);

                    Log.d(TAG," "+punchIn+" "+punchOut);

                    if (punchIn.isEmpty() || punchOut.isEmpty()){
                        submit.setVisibility(View.GONE);
                    }else {
                        submit.setVisibility(View.VISIBLE);
                    }
                }else{
                    submit.setVisibility(View.GONE);
                }
            } else {
                submit.setVisibility(View.VISIBLE);
            }

            projectLogHours = (punchIn.isEmpty() || punchOut.isEmpty()) ? "00:00" : getHMFromData(this,punchIn, punchOut);
            punchIn = (!punchIn.isEmpty()) ? getAmPmTime(punchIn) : "00:00";
            punchOut = (!punchOut.isEmpty()) ? getAmPmTime(punchOut) : "00:00";
            inTime.setText(punchIn);
            outTime.setText(punchOut);
            logHours.setText(projectLogHours);
        }
    }

    private void init() {
        db = new ESSdb(this);

        userPref = new UserPref(this);
        connection = new NetConnection();
        dialog = new Dialog(this);
        api = APIClient.getClient().create(RestApi.class);
        mProjectTaskArrayList = new ArrayList<>();
        mSelectedProjectTaskArrayList = new ArrayList<>();

        adapter = new ProjectTaskAdapter(this, mProjectTaskArrayList);
        taskSpinner.setLayoutManager(new LinearLayoutManager(this));
        taskSpinner.setHasFixedSize(true);
        taskSpinner.setNestedScrollingEnabled(false);
        taskSpinner.setAdapter(adapter);
        adapter.setTaskListener(this);
    }

    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void initialiseDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        startDataPicker = new DatePickerDialog(this, this, year, month, day);
        startDate.setText(getResources().getString(R.string.set_date_value, year, month + 1, day));

    }

    @OnClick(R.id.submit)
    protected void OnSubmit() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        builder.setMessage(R.string.timesheet_dialog);
        builder.setCancelable(true);
        builder.setNegativeButton(getResources().getString(R.string.timesheet_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton(getResources().getString(R.string.timesheet_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                onProceed();

            }
        });

        builder.show();

    }

    private void onProceed() {
        mSelectedProjectTaskArrayList.clear();

        String selectedProjectLogHours = "00:00";
        for (ProjectTask task : mProjectTaskArrayList) {
            if (!task.getSpentHours().equalsIgnoreCase(getResources().getString(R.string.blank_duration)) && !task.getSpentHours().isEmpty()) {
                selectedProjectLogHours = addTimeDuration(selectedProjectLogHours, task.getSpentHours());
                mSelectedProjectTaskArrayList.add(task);
            }
        }
        if (subtractTimeDuration(selectedProjectLogHours, projectLogHours) > 0) {
             addTimesheet(String.valueOf(selectedProjectLogHours));

        } else {
            showMessage("Total spent time exceed from log hour");
        }
    }

    private void addTimesheet(final String timesheetLogHours) {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            Gson gson = new Gson();
            String json = gson.toJson(mSelectedProjectTaskArrayList);
            Call<StringResponse> call = api.addTimesheet(userPref.getEmail(), Integer.parseInt(userPref.getUserId()), userPref.getUserId(), userPref.getName(), timesheetLogHours, startDate.getText().toString().trim(), json);

            call.enqueue(new Callback<StringResponse>() {
                @Override
                public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                    dialog.dismissDialog();
                    if (response.isSuccessful()) {
                        StringResponse stringResponse = response.body();
                        if (stringResponse != null) {
                            showMessage(stringResponse.getMessage());
                            if (!stringResponse.isError()) {
                                db.updateTimesheetLogHours(timesheetLogHours, startDate.getText().toString().trim());
                            }
                            mSelectedProjectTaskArrayList.clear();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<StringResponse> call, @NonNull Throwable t) {
                    dialog.dismissDialog();
                    if (t instanceof SocketTimeoutException) {
                        showMessage(getResources().getString(R.string.slow_internet_connection));
                    }
                }
            });
        } else {
            showMessage(getResources().getString(R.string.internet_not_available));
        }
    }

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.start_date)
    protected void OnDate() {
        startDataPicker.show();
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int monthOfDay) {
        startDate.setText(getResources().getString(R.string.set_date_value, year, month + 1, monthOfDay));
        updateUI();
    }

    @Override
    public void onTextChange(final RecyclerView.ViewHolder viewHolder, final int position) {
        View view = getLayoutInflater().inflate(R.layout.bottomsheet_description, null);
        final EditText notes = view.findViewById(R.id.notes);
        ImageView send = view.findViewById(R.id.send);
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();

        if (mProjectTaskArrayList.get(position).getDescription() != null)
            notes.setText(mProjectTaskArrayList.get(position).getDescription());

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strNotes = notes.getText().toString().trim();
                if (!strNotes.isEmpty()) {
                    mProjectTaskArrayList.get(position).setDescription(strNotes);
                }

                dialog.dismiss();
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
