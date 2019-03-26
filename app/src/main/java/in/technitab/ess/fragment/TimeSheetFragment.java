package in.technitab.ess.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.technitab.ess.R;
import in.technitab.ess.activity.MainActivity;
import in.technitab.ess.activity.TimesheetActivity;
import in.technitab.ess.activity.TimesheetHistoryActivity;
import in.technitab.ess.adapter.CalenderGridAdapter;
import in.technitab.ess.adapter.ProjectAdapter;
import in.technitab.ess.api.APIClient;
import in.technitab.ess.api.RestApi;
import in.technitab.ess.model.TimeSheetCalender;
import in.technitab.ess.model.TimesheetDataResponse;
import in.technitab.ess.model.TimesheetProject;
import in.technitab.ess.util.ConstantVariable;
import in.technitab.ess.util.Dialog;
import in.technitab.ess.util.ExpandableHeightGridView;
import in.technitab.ess.util.NetConnection;
import in.technitab.ess.util.UserPref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimeSheetFragment extends Fragment {

    @BindView(R.id.display_current_date)
    TextView displayCurrentDate;
    @BindView(R.id.calendar)
    ExpandableHeightGridView calendarGrid;
    @BindView(R.id.daySummeryRecyclerView)
    RecyclerView daySummeryRecyclerView;

    @BindView(R.id.rootLayout)
    CoordinatorLayout rootLayout;
    @BindView(R.id.dateDetailLayout)
    LinearLayout dateDetailLayout;

    private UserPref userPref;
    private Dialog dialog;
    private NetConnection connection;
    private RestApi api;
    private Activity mActivity;
    private Resources resources;
    private ArrayList<TimesheetDataResponse> dataResponses;
    private ArrayList<TimesheetProject> timesheetProjects;
    private String selectYear = "", selectedMonth = "";
    private static final int MAX_CALENDAR_COLUMN = 42;
    List<Date> dayValueInCells;
    private boolean isDateFromPicker = false;
    private ProjectAdapter mProjectAdapter;
    Calendar calendar = Calendar.getInstance();
    Unbinder unbinder;

    public TimeSheetFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_sheet, container, false);
        unbinder = ButterKnife.bind(this, view);

        init();
        initRecyclerView();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        selectedMonth = month < 10 ? "0" + month : "" + month;
        selectYear = String.valueOf(year);
        getTimeSheetList(selectYear, selectedMonth);
        return view;
    }

    private void initRecyclerView() {
        daySummeryRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        daySummeryRecyclerView.setHasFixedSize(false);
        daySummeryRecyclerView.setNestedScrollingEnabled(false);
        mProjectAdapter = new ProjectAdapter(timesheetProjects);
        daySummeryRecyclerView.setAdapter(mProjectAdapter);
    }

    private void init() {
        mActivity = getActivity();
        resources = getResources();
        calendarGrid.setExpanded(true);
        dataResponses = new ArrayList<>();
        timesheetProjects = new ArrayList<>();
        userPref = new UserPref(mActivity);
        connection = new NetConnection();
        dialog = new Dialog(mActivity);
        api = APIClient.getClient().create(RestApi.class);

        ((MainActivity) mActivity).setToolbar(resources.getString(R.string.timesheet));
        ((MainActivity)mActivity).setToolBarSubtitle(null);
    }

    private void getTimeSheetList(final String selectYear, final String selectedMonth) {

        if (!timesheetProjects.isEmpty())
            timesheetProjects.clear();

        if (connection.isNetworkAvailable(mActivity)) {
            dialog.showDialog();
            Call<ArrayList<TimeSheetCalender>> call = api.fetchTimesheetList(userPref.getUserId(), selectYear, selectedMonth);
            call.enqueue(new Callback<ArrayList<TimeSheetCalender>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<TimeSheetCalender>> call, @NonNull Response<ArrayList<TimeSheetCalender>> response) {
                    if (response.isSuccessful()) {
                        dialog.dismissDialog();
                        ArrayList<TimeSheetCalender> list = response.body();

                        if (list != null) {
                            if (!dataResponses.isEmpty())
                                dataResponses.clear();
                            dataResponses.addAll(list.get(0).getDataResponses());
                            setupCalendar(selectYear, selectedMonth, dataResponses);
                            timesheetProjects.addAll(list.get(0).getProjectResponses());
                            mProjectAdapter.notifyDataSetChanged();

                            if (dataResponses.isEmpty()) {
                                dateDetailLayout.setVisibility(View.GONE);
                                showMessage(resources.getString(R.string.no_timesheet_history));
                            } else {
                                dateDetailLayout.setVisibility(View.VISIBLE);
                            }
                        } else {
                            showMessage(resources.getString(R.string.no_timesheet_history));
                        }
                    }else{
                        dialog.dismissDialog();
                        showMessage(resources.getString(R.string.no_timesheet_history));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<TimeSheetCalender>> call, @NonNull Throwable t) {
                    dialog.dismissDialog();
                    if (t instanceof SocketTimeoutException) {
                        showMessage(resources.getString(R.string.slow_internet_connection));
                    }
                }
            });

        } else {
            showMessage(resources.getString(R.string.internet_not_available));
            setupCalendar(selectYear, selectedMonth, dataResponses);
            mProjectAdapter.notifyDataSetChanged();
        }
    }

    private void setupCalendar(String selectYear, String selectedMonth, ArrayList<TimesheetDataResponse> dataResponses) {
        dayValueInCells = new ArrayList<>();
        Calendar cal = getCalender(selectYear, selectedMonth);
        Calendar mCal = (Calendar) cal.clone();

        mCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;
        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);

        while (dayValueInCells.size() < MAX_CALENDAR_COLUMN) {
            dayValueInCells.add(mCal.getTime());
            mCal.add(Calendar.DAY_OF_MONTH, 1);
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MMMM", Locale.getDefault());
        String sDate = format.format(cal.getTime());
        if (!sDate.isEmpty()) {
            displayCurrentDate.setText(sDate);
        }
        CalenderGridAdapter adapter = new CalenderGridAdapter(mActivity, dayValueInCells, cal, dataResponses);
        calendarGrid.setAdapter(adapter);
    }

    private Calendar getCalender(String selectYear, String selectedMonth) {
        int year = Integer.parseInt(selectYear);
        String strMonth = selectedMonth.startsWith("0") ? selectedMonth.substring(1) : selectedMonth;
        int month = Integer.parseInt(strMonth);
        int modifiedMonth = isDateFromPicker ? month - 1 : month - 1;
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(Calendar.DAY_OF_MONTH, 1);
        currentDate.set(Calendar.YEAR, year);
        currentDate.set(Calendar.MONTH, modifiedMonth);

        return currentDate;
    }


    private void showMessage(String message) {
        Snackbar.make(rootLayout, message, Snackbar.LENGTH_LONG).show();
    }


    @OnClick({R.id.add_timesheet, R.id.month_year_picker, R.id.project_view, R.id.date_view})
    protected void onActionPerform(View view) {
        switch (view.getId()) {
            case R.id.add_timesheet:
                startActivity(new Intent(getActivity(), TimesheetActivity.class));
                break;
            case R.id.month_year_picker:
                showFilterDialog();
                break;

            case R.id.project_view: {
                Intent intent = new Intent(getActivity(), TimesheetHistoryActivity.class);
                intent.putExtra(ConstantVariable.MIX_ID.MONTH, selectedMonth);
                intent.putExtra(ConstantVariable.MIX_ID.YEAR, selectYear);
                intent.putExtra(ConstantVariable.MIX_ID.VIEW_TYPE, resources.getString(R.string.project));
                startActivity(intent);

                break;
            }

            case R.id.date_view: {
                Intent intent = new Intent(getActivity(), TimesheetHistoryActivity.class);
                intent.putExtra(ConstantVariable.MIX_ID.MONTH, selectedMonth);
                intent.putExtra(ConstantVariable.MIX_ID.YEAR, selectYear);
                intent.putExtra(ConstantVariable.MIX_ID.VIEW_TYPE, resources.getString(R.string.date));
                startActivity(intent);
                break;
            }
        }
    }

    private void showFilterDialog() {

        final String[] yearLists = mActivity.getResources().getStringArray(R.array.yearArray);
        final AlertDialog builder = new AlertDialog.Builder(mActivity).create();
        View pickerView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_year_month_picker, null);
        builder.setView(pickerView);

        final NumberPicker month = pickerView.findViewById(R.id.month);
        NumberPicker year = pickerView.findViewById(R.id.year);
        Button done = pickerView.findViewById(R.id.done);
        Button cancel = pickerView.findViewById(R.id.cancel);

        month.setMinValue(0);
        month.setMaxValue(11);
        month.setDisplayedValues(yearLists);

        year.setMinValue(2016);
        year.setMaxValue(calendar.get(Calendar.YEAR));

        year.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                selectYear = String.valueOf(newVal);
            }
        });


        month.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                newVal = newVal + 1;
                selectedMonth = newVal < 10 ? "0" + (newVal) : "" + (newVal);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
                isDateFromPicker = true;
                getTimeSheetList(selectYear, selectedMonth);

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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
