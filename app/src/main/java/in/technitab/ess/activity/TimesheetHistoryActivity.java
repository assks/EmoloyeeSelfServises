package in.technitab.ess.activity;

import android.app.Activity;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.technitab.ess.R;
import in.technitab.ess.adapter.TimesheetHeaderAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.model.TimeSheetDate;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimesheetHistoryActivity extends AppCompatActivity {


    @BindView(R.id.timesheetRecyclerView)
    RecyclerView timesheetRecyclerView;

    Unbinder unbinder;
    private ArrayList<TimeSheetDate> mTaskArrayList;
    private TimesheetHeaderAdapter adapter;
    private UserPref userPref;
    private Dialog dialog;
    private NetConnection connection;
    private RestApi api;
    private Activity mActivity;
    private Resources resources;
    ActionBar actionBar;
    private String selectYear = "", selectedMonth = "", selectViewType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timesheet_history);
        unbinder = ButterKnife.bind(this);
        init();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            selectedMonth = bundle.getString(ConstantVariable.MIX_ID.MONTH);
            selectYear = bundle.getString(ConstantVariable.MIX_ID.YEAR);
            selectViewType = bundle.getString(ConstantVariable.MIX_ID.VIEW_TYPE);
        } else {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            selectedMonth = month < 10 ? "0" + month : "" + month;
            selectYear = String.valueOf(year);
        }

        setToolBar();
        setRecyclerView();

        if (selectViewType.equalsIgnoreCase(resources.getString(R.string.project))) {
            getProjectWiseTS(selectYear, selectedMonth);
        } else if (selectViewType.equalsIgnoreCase(resources.getString(R.string.date))) {
            getTimsheeetList(selectYear, selectedMonth);
        }


    }

    private void getProjectWiseTS(String selectYear, String selectedMonth) {

        if (connection.isNetworkAvailable(mActivity)) {
            dialog.showDialog();
            Call<ArrayList<TimeSheetDate>> call = api.fetchProjectWiseTimesheetList(userPref.getUserId(), selectYear, selectedMonth);
            call.enqueue(new Callback<ArrayList<TimeSheetDate>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<TimeSheetDate>> call, @NonNull Response<ArrayList<TimeSheetDate>> response) {
                    if (response.isSuccessful()) {
                        dialog.dismissDialog();
                        ArrayList<TimeSheetDate> list = response.body();

                        if (list != null) {
                            if (!mTaskArrayList.isEmpty())
                                mTaskArrayList.clear();
                            mTaskArrayList.addAll(list);
                            adapter.notifyDataSetChanged();
                        }
                        if (mTaskArrayList.isEmpty()) {
                            showMessage(resources.getString(R.string.no_timesheet_history));
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<TimeSheetDate>> call, @NonNull Throwable t) {
                    dialog.dismissDialog();
                    if (t instanceof SocketTimeoutException) {
                        showMessage(getResources().getString(R.string.slow_internet_connection));
                    }
                }
            });

        } else {
            showMessage(resources.getString(R.string.internet_not_available));
        }

    }

    private void setRecyclerView() {
        mTaskArrayList = new ArrayList<>();
        timesheetRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        timesheetRecyclerView.setHasFixedSize(false);
        adapter = new TimesheetHeaderAdapter(selectViewType, mTaskArrayList);
        timesheetRecyclerView.setAdapter(adapter);
    }

    private void getTimsheeetList(String selectYear, String selectedMonth) {


        if (connection.isNetworkAvailable(mActivity)) {
            dialog.showDialog();
            Call<ArrayList<TimeSheetDate>> call = api.fetchDateWiseTimesheetList(userPref.getUserId(), selectYear, selectedMonth);

            call.enqueue(new Callback<ArrayList<TimeSheetDate>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<TimeSheetDate>> call, @NonNull Response<ArrayList<TimeSheetDate>> response) {
                    if (response.isSuccessful()) {
                        dialog.dismissDialog();
                        ArrayList<TimeSheetDate> list = response.body();
                        if (list != null) {
                            if (!mTaskArrayList.isEmpty())
                                mTaskArrayList.clear();
                            mTaskArrayList.addAll(list);
                            adapter.notifyDataSetChanged();
                        }
                        if (mTaskArrayList.isEmpty()) {
                            showMessage(resources.getString(R.string.no_timesheet_history));
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<TimeSheetDate>> call, @NonNull Throwable t) {
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

    private void showMessage(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    private void setToolBar() {
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(resources.getString(R.string.timesheet));
            actionBar.setSubtitle(getYearMonth(selectedMonth, selectYear));
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }


    private void setToolbarSubtitle(String selectedMonth, String selectYear) {
        if (actionBar != null) {
            actionBar.setSubtitle(getYearMonth(selectedMonth, selectYear));
        }
    }

    private String getYearMonth(String selectedMonth, String selectYear) {
        String str = selectYear + "-" + selectedMonth;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        try {
            Date d = format.parse(str);
            SimpleDateFormat strFormat = new SimpleDateFormat("MMM, yyyy", Locale.getDefault());
            str = strFormat.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    private void init() {
        mActivity = this;
        resources = getResources();
        userPref = new UserPref(mActivity);
        connection = new NetConnection();
        dialog = new Dialog(mActivity);
        api = APIClient.getClient().create(RestApi.class);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timesheet_fliter, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.filter) {
            showFilterDialog();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    private void showFilterDialog() {
        final String[] yearLists = mActivity.getResources().getStringArray(R.array.yearArray);
        selectYear = "2018";
        selectedMonth = yearLists[0];
        final AlertDialog builder = new AlertDialog.Builder(mActivity).create();
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_year_month_picker, null);
        builder.setView(view);

        final NumberPicker month = view.findViewById(R.id.month);
        NumberPicker year = view.findViewById(R.id.year);
        Button done = view.findViewById(R.id.done);
        Button cancel = view.findViewById(R.id.cancel);

        month.setMinValue(0);
        month.setMaxValue(11);
        month.setDisplayedValues(yearLists);

        year.setMinValue(2016);
        year.setMaxValue(2018);

        year.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectYear = String.valueOf(newVal);
            }
        });


        month.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                newVal += 1;
                selectedMonth = newVal < 10 ? "0" + newVal : "" + newVal;
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();

                setToolbarSubtitle(selectedMonth, selectYear);
                if (selectViewType.equalsIgnoreCase(resources.getString(R.string.project))) {
                    getProjectWiseTS(selectYear, selectedMonth);
                } else if (selectViewType.equalsIgnoreCase(resources.getString(R.string.date))) {
                    getTimsheeetList(selectYear, selectedMonth);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
            }
        });

        builder.show();


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
