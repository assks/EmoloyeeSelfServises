package in.technitab.ess.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.database.ESSdb;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestoreDataActivity extends AppCompatActivity {

    @BindView(R.id.restore)
    Button restore;
    @BindView(R.id.progressDialog)
    ProgressBar progressDialog;
    @BindView(R.id.finish)
    Button finish;
    private UserPref userPref;
    private NetConnection connection;
    RestApi api;
    private ESSdb db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_data);
        ButterKnife.bind(this);
        initialise();
    }

    private void initialise() {
        userPref = new UserPref(this);
        connection = new NetConnection();
        api = APIClient.getClient().create(RestApi.class);
        db = new ESSdb(this);
    }


    @OnClick(R.id.finish)
    protected void onFinish() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @OnClick(R.id.restore)
    protected void onRestore() {
        if (connection.isNetworkAvailable(this)) {
            progressDialog.setVisibility(View.VISIBLE);
            Call<String> call = api.fetchEmployeeData(userPref.getUserId());
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        runOnThread(response.body());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                    if (t instanceof SocketTimeoutException){
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


    private void runOnThread(final String response) {
        Handler mHandler = new Handler(Looper.getMainLooper());

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                showJson(response);
            }
        });

    }

    private void showJson(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray attendanceArray = jsonObject.getJSONArray("attendance");
            JSONArray leavesArray = jsonObject.getJSONArray("leave_response");

            if (attendanceArray.length() < 1 && leavesArray.length() < 1) {
                showMessage("You don't have previous data");
            } else {
                storeAttendance(attendanceArray);

            }
            startHomeActivity();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startHomeActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.setVisibility(View.GONE);
                finish.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }

    private void storeAttendance(JSONArray attendanceArray) {
        for (int i = 0; i < attendanceArray.length(); i++) {
            try {
                JSONObject jsonObject = attendanceArray.getJSONObject(i);
                String startDate = jsonObject.getString(ConstantVariable.Tbl_Attendance.DATE);
                String attendance = jsonObject.getString(ConstantVariable.Tbl_Attendance.ATTENDANCE);
                String spentTime = jsonObject.getString(ConstantVariable.Tbl_Attendance.SPENT_TIME);
                String punchIn = jsonObject.getString(ConstantVariable.Tbl_Attendance.PUNCH_IN);
                String punchOut = jsonObject.getString(ConstantVariable.Tbl_Attendance.PUNCH_OUT);
                String manualPunchIn = jsonObject.getString(ConstantVariable.Tbl_Attendance.MANUAL_PUNCH_IN);
                String manualPunchOut = jsonObject.getString(ConstantVariable.Tbl_Attendance.MANUAL_PUNCH_OUT);
                String manualPunchSysIn = jsonObject.getString(ConstantVariable.Tbl_Attendance.MANUAL_PUNCH_SYS_IN);
                String manualPunchSysOut = jsonObject.getString(ConstantVariable.Tbl_Attendance.MANUAL_PUNCH_SYS_OUT);
                String inAddress = jsonObject.getString(ConstantVariable.Tbl_Attendance.IN_ADDRESS);
                String outAddress = jsonObject.getString(ConstantVariable.Tbl_Attendance.OUT_ADDRESS);
                String inLocation = jsonObject.getString(ConstantVariable.Tbl_Attendance.IN_LOCATION);
                String outLocation = jsonObject.getString(ConstantVariable.Tbl_Attendance.OUT_LOCATION);
                String inPhotoPath = jsonObject.getString(ConstantVariable.Tbl_Attendance.IN_PHOTO_PATH);
                String outPhotoPath = jsonObject.getString(ConstantVariable.Tbl_Attendance.OUT_PHOTO_PATH);
                String summery = jsonObject.getString(ConstantVariable.Tbl_Attendance.SUMMERY);
                int status = jsonObject.getInt(ConstantVariable.Tbl_Attendance.PUNCH_STATUS);
                String timesheetLogHours = jsonObject.getString(ConstantVariable.Tbl_Attendance.TIMESHEET_HOURS);
                db.addAttendance(startDate, attendance, punchIn, manualPunchIn, manualPunchSysIn, inPhotoPath, inLocation, inAddress, punchOut, manualPunchOut, manualPunchSysOut, outPhotoPath, outLocation, outAddress, summery, spentTime,status,timesheetLogHours);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
