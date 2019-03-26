package in.technitab.ess.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.technitab.ess.R;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.model.Leave;
import in.technitab.ess.model.StringResponse;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssignLeaveActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.user_name)
    EditText userName;
    @BindView(R.id.leave_type)
    Spinner leaveType;
    @BindView(R.id.leave_balance)
    TextView leaveBalance;
    @BindView(R.id.start_date)
    EditText startDate;
    @BindView(R.id.start_date_input_layout)
    TextInputLayout startDateInputLayout;
    @BindView(R.id.end_date)
    EditText endDate;
    @BindView(R.id.end_date_input_layout)
    TextInputLayout endDateInputLayout;
    @BindView(R.id.halfDayTV)
    TextView halfDayTV;
    @BindView(R.id.halfDaySpinner)
    Spinner halfDaySpinner;
    @BindView(R.id.durationTv)
    TextView durationTv;
    @BindView(R.id.durationSpinner)
    Spinner durationSpinner;
    @BindView(R.id.remaining_leave)
    TextView remainingLeave;
    @BindView(R.id.leave_location)
    EditText leaveLocation;
    @BindView(R.id.leave_location_input_layout)
    TextInputLayout leaveLocationInputLayout;
    @BindView(R.id.leave_reason)
    Spinner leaveReason;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.descriptionLayout)
    TextInputLayout descriptionLayout;
    @BindView(R.id.surety)
    CheckBox surety;
    @BindView(R.id.submit)
    Button submit;
    private NetConnection connection;
    private Dialog dialog;
    RestApi api;

    private UserPref userPref;
    private DatePickerDialog startDataPicker, endDatePicker;
    private Date newStartDate, newEndDate;
    private Leave leave;
    private int dateValue = 1, userId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_leave);
        ButterKnife.bind(this);


        init();
        setToolbar();
        setSpinner();
        initialiseDatePicker();
        setListener();

    }


    private void init() {
        connection = new NetConnection();
        dialog = new Dialog(this);
        userPref = new UserPref(this);
        api = APIClient.getClient().create(RestApi.class);
        leave = new Leave();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getInt(ConstantVariable.UserPrefVar.USER_ID);
            String strUserName = bundle.getString(ConstantVariable.UserPrefVar.NAME);
            userName.setText(strUserName);
        }
    }


    private void setToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getResources().getString(R.string.assign_leave));
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }


    private void setSpinner() {
        List<String> reasonList = Arrays.asList(getResources().getStringArray(R.array.leaveReasonArray));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reasonList);
        leaveReason.setAdapter(adapter);

        leaveReason.setOnItemSelectedListener(this);

        List<String> leaveTypeList = Arrays.asList(getResources().getStringArray(R.array.leaveTypeArray));
        ArrayAdapter<String> leaveAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, leaveTypeList);
        leaveType.setAdapter(leaveAdapter);

        leaveType.setOnItemSelectedListener(this);
    }


    private void setListener() {
        surety.setOnCheckedChangeListener(this);
        halfDaySpinner.setOnItemSelectedListener(this);
        durationSpinner.setOnItemSelectedListener(this);
    }


    private void initialiseDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        startDataPicker = new DatePickerDialog(this, this, year, month, day);
        endDatePicker = new DatePickerDialog(this, this, year, month, day);
    }


    @OnClick({R.id.start_date, R.id.end_date})
    protected void onDataChange(View view) {
        hideKeyboard();
        switch (view.getId()) {
            case R.id.start_date:
                dateValue = 1;
                startDataPicker.show();
                break;
            case R.id.end_date:
                dateValue = 2;
                endDatePicker.show();
                break;
        }
    }

    @OnClick(R.id.submit)
    protected void onSubmit() {
        leave.setStartDate(startDate.getText().toString().trim());
        leave.setEndDate(endDate.getText().toString().trim());
        leave.setDescription(description.getText().toString().trim());
        leave.setReason(leaveReason.getSelectedItem().toString());

        if (leave.getStartDate().equalsIgnoreCase(leave.getEndDate())) {
            leave.setShift(durationSpinner.getSelectedItem().toString());
            leave.setLeaveDuration("");
        } else {
            leave.setLeaveDuration(halfDaySpinner.getSelectedItem().toString());
            if (!leave.getLeaveDuration().equalsIgnoreCase("None")) {
                leave.setShift(durationSpinner.getSelectedItem().toString());
            } else {
                leave.setShift(getResources().getString(R.string.full_day));
            }

        }
        leave.setUserId(Integer.parseInt(userPref.getUserId()));
        leave.setLeaveLocation(leaveLocation.getText().toString().trim());
        leave.setLeaveType(leaveType.getSelectedItem().toString());
        leave.setCreateById(Integer.parseInt(userPref.getUserId()));

        if (invalidate()) {
                if (connection.isNetworkAvailable(this)) {
                    dialog.showDialog();

                    Gson gson = new Gson();
                    String leaveJson = gson.toJson(leave);

                    Call<StringResponse> call = api.leaveRequest("assign_leave", leaveJson);

                    call.enqueue(new Callback<StringResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<StringResponse> call, @NonNull Response<StringResponse> response) {
                            dialog.dismissDialog();
                            if (response.isSuccessful()) {
                                StringResponse stringResponse = response.body();
                                if (stringResponse != null) {
                                    if (!stringResponse.isError()) {
                                        startLeaveActivity();
                                    }
                                    showMessage(stringResponse.getMessage());
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
    }

    private void startLeaveActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onBackPressed();
                finish();
            }
        }, 1000);
    }

    private String getDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return simpleDateFormat.format(System.currentTimeMillis());

    }

    private boolean invalidate() {

        boolean valid = true;
        if (startDate.getText().toString().isEmpty()) {
            showMessage("Start date is required");
            valid = false;
        } else if (endDate.getText().toString().isEmpty()) {
            showMessage("End date is required");
            valid = false;
        } else if (description.getText().toString().isEmpty()) {
            showMessage("Description is required");
            valid = false;

        } else if (description.getText().toString().length() >= 300) {
            showMessage("Description exceed maximum character.");
            valid = false;
        } else if (description.getText().toString().length() < 30) {
            showMessage("Description required maximum 30 characters.");
            valid = false;
        } else if (leaveLocation.getText().toString().trim().isEmpty()) {
            showMessage("Location during leave is required");
            valid = false;
        } else if (leaveLocation.getText().toString().length() > 200) {
            showMessage("Location during leave doesn't exceed 200 character");
            valid = false;
        }
        return valid;
    }

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        if (dateValue == 1) {
            startDate.setText(getResources().getString(R.string.date_value, year, getActualValue(month + 1), getActualValue(day)));
            endDate.setText(getResources().getString(R.string.date_value, year, getActualValue(month + 1), getActualValue(day)));
            newStartDate = getDate(startDate.getText().toString().trim());
            endDatePicker.getDatePicker().setMinDate(newStartDate.getTime());

            if (newEndDate == null)
                newEndDate = getDate(startDate.getText().toString().trim());

        } else {
            endDate.setText(getResources().getString(R.string.date_value, year, getActualValue(month + 1), getActualValue(day)));
            newEndDate = getDate(endDate.getText().toString().trim());
        }

        setupLeaveDays(newStartDate, newEndDate);
        getLeaveBalance();
    }

    private void setupLeaveDays(Date newStartDate, Date newEndDate) {
        if (newStartDate.getTime() == newEndDate.getTime()) {
            halfDayTV.setVisibility(View.GONE);
            halfDaySpinner.setVisibility(View.GONE);
            setupSameDateAdapter("");

        } else {
            halfDayTV.setVisibility(View.VISIBLE);
            halfDaySpinner.setVisibility(View.VISIBLE);
            setupDifferentDateAdapter();
        }
    }

    private void setupDifferentDateAdapter() {
        List<String> mList = Arrays.asList(getResources().getStringArray(R.array.halfDaysArray));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mList);
        halfDaySpinner.setAdapter(adapter);
    }

    private void setupSameDateAdapter(String selectedSpinnerValue) {
        List<String> mList = null;
        if (selectedSpinnerValue.equalsIgnoreCase("None")) {
            durationSpinner.setVisibility(View.GONE);
            durationTv.setVisibility(View.GONE);

        } else if (selectedSpinnerValue.equalsIgnoreCase("")) {
            mList = Arrays.asList(getResources().getStringArray(R.array.DurationArray));

        } else {
            mList = Arrays.asList(getResources().getStringArray(R.array.halfDurationArray));
        }

        if (mList != null) {
            durationSpinner.setVisibility(View.VISIBLE);
            durationTv.setVisibility(View.VISIBLE);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mList);
            durationSpinner.setAdapter(adapter);
        }

    }


    private Date getDate(String date) {
        Date d = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            d = dateFormat.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }


    private String getActualValue(int dateValue) {
        return dateValue < 10 ? "0" + dateValue : "" + dateValue;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (view != null) {
            view.setPadding(2, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
            ((TextView) view).setTextColor(Color.BLACK);
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.mediumTextSize));

            switch (adapterView.getId()) {
                case R.id.leave_type:
                    getLeaveBalance();
                    break;

                case R.id.halfDaySpinner:
                    String spinnerSelectedValue = adapterView.getSelectedItem().toString();
                    setupSameDateAdapter(spinnerSelectedValue);
                    break;

                case R.id.durationSpinner:

            }
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        submit.setVisibility(compoundButton.isChecked() ? View.VISIBLE : View.GONE);
    }


    private void getLeaveBalance() {
        if (connection.isNetworkAvailable(this)) {
            dialog.showDialog();
            RequestBody srLeaveType = RequestBody.create(MediaType.parse("text/plain"), leaveType.getSelectedItem().toString());
            RequestBody empId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userId));
            RequestBody strStartDate = RequestBody.create(MediaType.parse("text/plain"), startDate.getText().toString().trim());
            RequestBody strEndDate = RequestBody.create(MediaType.parse("text/plain"), endDate.getText().toString().trim());

            Map<String, RequestBody> myMap = new HashMap<>();
            myMap.put(ConstantVariable.UserPrefVar.USER_ID, empId);
            myMap.put("leave_type", srLeaveType);
            myMap.put("start_date", strStartDate);
            myMap.put("end_date", strEndDate);

            Call<String> call = api.fetchLeaveBalance(myMap);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                    if (response.isSuccessful()) {
                        dialog.dismissDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(response.body());
                            double leaveBalanceValue = jsonObject.getDouble("balance_leave");
                            double remainingBalance = jsonObject.getDouble("remaining_leaves");
                            leaveBalance.setText(String.valueOf(leaveBalanceValue));

                            if (remainingBalance < 0) {
                                remainingLeave.setText(getResources().getString(R.string.insufficient_balance));
                                remainingLeave.setTextColor(Color.RED);
                                submit.setVisibility(View.GONE);
                            } else {
                                remainingLeave.setText(String.valueOf(remainingBalance));
                                remainingLeave.setTextColor(getResources().getColor(R.color.colorPrimaryText));
                                submit.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
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


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
}
