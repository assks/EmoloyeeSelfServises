package in.technitab.ess.activity;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.database.ESSdb;
import in.technitab.ess.model.StringResponse;
import in.technitab.ess.util.CustomDate;
import in.technitab.ess.util.CustomEditText;
import in.technitab.ess.util.DateCal;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.SetTime;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.technitab.ess.util.DateCal.getHMFromData;
import static in.technitab.ess.util.DateCal.getHours;
import static in.technitab.ess.util.DateCal.subtractTimeDuration;

public class ManualPunchActivity extends AppCompatActivity {

    @BindView(R.id.date)
    CustomEditText date;
    @BindView(R.id.punchIn)
    CustomEditText punchIn;

    @BindView(R.id.punchOut)
    CustomEditText punchOut;
    @BindView(R.id.remark)
    CustomEditText remark;
    private NetConnection connection;
    private Dialog dialog;
    private RestApi api;
    private UserPref userPref;
    private ESSdb db;
    private Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_punch);
        ButterKnife.bind(this);

        init();
        setToolBar();
        new CustomDate(date, this, null, DateCal.getDate(System.currentTimeMillis()));
        new SetTime(punchIn, this);
        new SetTime(punchOut, this);
        date.addTextChangedListener(new MyTextWatcher(date));
    }

    private void init() {
        connection = new NetConnection();
        resources = getResources();
        dialog = new Dialog(this);
        userPref = new UserPref(this);
        db = new ESSdb(this);
        api = APIClient.getClient().create(RestApi.class);
    }

    private void setToolBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(resources.getString(R.string.request_attendance));
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    @OnClick(R.id.submit)
    protected void onSubmit() {
        final String strDate = date.getText().toString().trim();
        String strPunchIn = punchIn.getText().toString().trim();
        String strPunchOut = punchOut.getText().toString().trim();
        String strRemark = remark.getText().toString().trim();


        if (invalidate(strDate, strPunchIn, strPunchOut,strRemark)) {

            strPunchIn = strDate + " " + strPunchIn + ":00";
            final String PunchIn = strPunchIn;
            final String PunchOut = strDate + " " + strPunchOut + ":00";
            final String spentHours = getHMFromData(this, PunchIn, PunchOut);
            final String attendance = getAttendanceValue(spentHours, PunchIn, PunchOut);

            Log.d("Attendance ", strPunchIn+ " m "+ strPunchOut);

            if (connection.isNetworkAvailable(this)) {
                dialog.showDialog();

                Call<StringResponse> call = api.manualAttendance(userPref.getUserId(), strDate, PunchIn, PunchOut, attendance, spentHours,strRemark,"manual_attendance");
                call.enqueue(new Callback<StringResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {

                        dialog.dismissDialog();
                        if (response.isSuccessful()) {
                            StringResponse stringResponse = response.body();
                            if (stringResponse != null) {
                                showMessage(stringResponse.getMessage());
                                if (!stringResponse.isError()) {
                                    db.addManualAttendance(strDate, attendance, "", PunchIn, DateCal.getDateTime(System.currentTimeMillis()), "", "", "", "", PunchOut, DateCal.getDateTime(System.currentTimeMillis()), "", "", "", "", spentHours, 2, "");
                                }
                            }

                        } else {
                            showMessage(resources.getString(R.string.problem_to_connect));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<StringResponse> call, @NonNull Throwable t) {
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
    }

    private boolean invalidate(String strDate, String strPunchIn, String strPunchOut,String strRemark) {
        boolean valid = true;
        if (strDate.isEmpty()) {
            valid = false;
            showMessage("Date is required");
        } else if (strPunchIn.isEmpty()) {
            valid = false;
            showMessage("Punch-in is required");
        } else if (strPunchOut.isEmpty()) {
            valid = false;
            showMessage("Punch-out is required");
        } else if (subtractTimeDuration(strPunchIn, strPunchOut) <= 0) {
            valid = false;
            showMessage("Punch-out is not less than punch-in");
        }else if (TextUtils.isEmpty(strRemark)){
            valid = false;
            showMessage("Remark is required");
        }
        return valid;
    }

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }


    private String getAttendanceValue(String spentHours, String punchInTime, String punchOutTime) {
        String attendance;
        String[] parts = spentHours.split(":");
        int hours = Integer.parseInt(parts[0]);
        if (hours > 4) {
            attendance = resources.getString(R.string.full_day_present);
        } else {
            int in = getHours(punchInTime);

            if (in < 13) {
                attendance = resources.getString(R.string.first_half);
            } else {
                attendance = resources.getString(R.string.second_half);
            }

        }
        return attendance;
    }

    class MyTextWatcher implements TextWatcher {
        View view;

        MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            switch (view.getId()) {
                case R.id.date:
                    String strDate = date.getText().toString().trim();
                    if (charSequence.length() > 0) {
                        String strPunchIn = db.getPunchInTime(strDate);
                        if (!strPunchIn.isEmpty()) {
                            String str = strPunchIn.substring(strPunchIn.lastIndexOf(" ") + 1);
                            str = str.substring(0, 5);
                            punchIn.setText(str);
                        } else {
                            punchIn.setText("");
                            punchOut.setText("");
                        }
                    }
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
