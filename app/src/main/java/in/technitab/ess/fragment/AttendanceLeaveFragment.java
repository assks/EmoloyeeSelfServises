package in.technitab.ess.fragment;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.technitab.ess.R;
import in.technitab.ess.activity.LeaveListActivity;
import in.technitab.ess.activity.MainActivity;
import in.technitab.ess.adapter.GridAdapter;
import in.technitab.ess.database.ESSdb;
import in.technitab.ess.listener.DaySummeryListener;
import in.technitab.ess.model.AttendanceCalendar;
import in.technitab.ess.util.ExpandableHeightGridView;

import static in.technitab.ess.util.DateCal.getAmPmTime;


public class AttendanceLeaveFragment extends Fragment implements DaySummeryListener {

    @BindView(R.id.dateDetailLayout)
    LinearLayout dateDetailLayout;

    Unbinder unbinder;
    @BindView(R.id.display_current_date)
    TextView displayCurrentDate;
    @BindView(R.id.previous_month)
    ImageView previousMonth;
    @BindView(R.id.next_month)
    ImageView nextMonth;
    @BindView(R.id.summeryDate)
    RelativeLayout summeryDate;
    @BindView(R.id.punch_in)
    TextView punchIn;
    @BindView(R.id.punch_out)
    TextView punchOut;
    @BindView(R.id.total_time)
    TextView totalTime;
    @BindView(R.id.project_hours)
    TextView projectHours;
    @BindView(R.id.daySummeryRecyclerView)
    RecyclerView daySummeryRecyclerView;
    @BindView(R.id.calendar_grid)
    ExpandableHeightGridView calendarGrid;
    @BindView(R.id.activity_custom_calendar)
    LinearLayout activityCustomCalendar;

    @BindView(R.id.summeryCurrentDate)
    TextView summeryCurrentDate;
    @BindView(R.id.summeryPrevMonth)
    ImageView summeryPrevMonth;
    @BindView(R.id.summeryNextMonth)
    ImageView summeryNextMonth;

    private static final int MAX_CALENDAR_COLUMN = 42;
    @BindView(R.id.summery)
    TextView summery;
    @BindView(R.id.view_more)
    TextView viewMore;
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    private GridAdapter mAdapter;

    List<AttendanceCalendar> mEvents;
    List<Integer> mEventDates;
    private ESSdb db;
    private Activity mContext;
    private Resources resources;
    List<Date> dayValueInCells;
    private int selectedCalenderPosition = -1;

    public AttendanceLeaveFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance_leave, container, false);
        unbinder = ButterKnife.bind(this, view);

        initialize();

        ((MainActivity) mContext).setToolbar(resources.getString(R.string.attendance));

        setUpCalendarAdapter();
        setPreviousButtonClickEvent();
        setNextButtonClickEvent();
        setPreviousDateClickEvent();
        setNextDateClickEvent();
        setGridCellClickEvents();

        return view;
    }

    private void setNextDateClickEvent() {
        summeryNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedCalenderPosition += 1;
                cal.add(Calendar.DAY_OF_MONTH, 1);
                updateSummeryLayout(cal.getTime());
                mAdapter = new GridAdapter(mContext, dayValueInCells, selectedCalenderPosition, cal, mEvents, mEventDates);
                calendarGrid.setAdapter(mAdapter);
            }
        });
    }

    private void setPreviousDateClickEvent() {
        summeryPrevMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedCalenderPosition -= 1;
                cal.add(Calendar.DAY_OF_MONTH, -1);
                updateSummeryLayout(cal.getTime());
                mAdapter = new GridAdapter(mContext, dayValueInCells, selectedCalenderPosition, cal, mEvents, mEventDates);
                calendarGrid.setAdapter(mAdapter);
            }

        });
    }


    private void initialize() {
        mContext = getActivity();
        if (mContext != null) {
            resources = mContext.getResources();
            db = new ESSdb(mContext);
            calendarGrid.setExpanded(true);
            mEventDates = new ArrayList<>();
            mEvents = new ArrayList<>();
            ((MainActivity)mContext).setToolBarSubtitle(null);
        }
    }

    private void setPreviousButtonClickEvent() {
        previousMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCalenderPosition = -1;
                cal.add(Calendar.MONTH, -1);
                mEvents.clear();
                mEventDates.clear();
                setUpCalendarAdapter();
            }
        });
    }

    private void setNextButtonClickEvent() {
        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCalenderPosition = -1;
                cal.add(Calendar.MONTH, 1);
                mEvents.clear();
                mEventDates.clear();
                setUpCalendarAdapter();
            }
        });
    }

    private void setGridCellClickEvents() {
        calendarGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Date selectedDate = dayValueInCells.get(position);
                updateSummeryLayout(selectedDate);
                selectedCalenderPosition = position;
                mAdapter = new GridAdapter(mContext, dayValueInCells, selectedCalenderPosition, cal, mEvents, mEventDates);
                calendarGrid.setAdapter(mAdapter);

            }
        });
    }

    private void updateSummeryLayout(Date selectedDate) {

        String d = DateFormat.getDateInstance(DateFormat.LONG).format(selectedDate);

        summeryCurrentDate.setText(d);

        String strPunchIn, strPunchOut, strManualPunchIn, strManualPunchOut;
        String strSpentHours, strPunchInValue = "", strPunchOutValue = "",strProjectHours;
        if (mEventDates.contains(selectedDate.getDate())) {

            dateDetailLayout.setVisibility(View.VISIBLE);
            int index = mEventDates.indexOf(selectedDate.getDate());
            AttendanceCalendar row = mEvents.get(index);

            if (!(row.getAttendanceType().equalsIgnoreCase(resources.getString(R.string.full_day)) || row.getAttendanceType().equalsIgnoreCase(resources.getString(R.string.weekend)))) {

                strPunchIn = row.getPunchIn();
                strPunchOut = row.getPunchOut();
                strManualPunchIn = row.getManualPunchIn();
                strManualPunchOut = row.getManualPunchOut();
                strSpentHours = row.getSpentTime() == null ? "00:00" : row.getSpentTime();
                  strProjectHours = row.getTimesheetHours();

                /*if ( minHours != 0) {

                    int hours =  minHours/ 60;
                    int minutes = minHours % 60;
                    strProjectHours = mContext.getResources().getString(R.string.min_hours_value,hours,minutes);
                    punchIn.setText(strProjectHours);
                }else{
                    strProjectHours = "00:00";
                }*/


                if (!strPunchIn.isEmpty()) {
                    strPunchInValue = getAmPmTime(strPunchIn);
                    strPunchOutValue = !strPunchOut.isEmpty() ? getAmPmTime(strPunchOut) : "00:00";
                } else if (!strManualPunchIn.isEmpty()) {
                    strPunchInValue = getAmPmTime(strManualPunchIn);
                    strPunchOutValue = !strManualPunchOut.isEmpty() ? getAmPmTime(strManualPunchOut) : "00:00";
                } else {
                    strPunchInValue = "00:00";
                    strPunchOutValue = "00:00";
                }

                if (!row.getSummery().isEmpty()) {
                    summery.setText(row.getSummery());
                    summery.setVisibility(View.VISIBLE);
                    viewMore.setVisibility(View.GONE);
                } else {
                    hideSummeryLayout();
                }
            } else {
                strSpentHours = "00:00";
                strProjectHours = "00:00";
                hideSummeryLayout();
            }

            punchIn.setText(strPunchInValue);
            punchOut.setText(strPunchOutValue);
            totalTime.setText(strSpentHours);
            projectHours.setText(strProjectHours);
        } else {
            hideSummeryLayout();
            dateDetailLayout.setVisibility(View.GONE);
        }

    }

    private void hideSummeryLayout() {
        summery.setVisibility(View.GONE);
        viewMore.setVisibility(View.GONE);

    }

    private void setUpCalendarAdapter() {

        dayValueInCells = new ArrayList<>();
        Calendar mCal = (Calendar) cal.clone();
        mCal.set(Calendar.DAY_OF_MONTH, 1);

        mEventDates = db.getAttendanceDate(getDateValue(mCal.get(Calendar.MONTH) + 1), getDateValue(mCal.get(Calendar.YEAR)));
        mEvents = db.fetchAttendance(getDateValue(mCal.get(Calendar.MONTH) + 1), getDateValue(mCal.get(Calendar.YEAR)));
        updateSummeryLayout(cal.getTime());

        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;
        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        while (dayValueInCells.size() < MAX_CALENDAR_COLUMN) {
            dayValueInCells.add(mCal.getTime());
            mCal.add(Calendar.DAY_OF_MONTH, 1);
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MMMM", Locale.getDefault());
        String sDate = format.format(cal.getTime());
        displayCurrentDate.setText(sDate);
        mAdapter = new GridAdapter(mContext, dayValueInCells, selectedCalenderPosition, cal, mEvents, mEventDates);

        calendarGrid.setAdapter(mAdapter);
    }

    private String getDateValue(int value) {
        return value < 10 ? "0" + value : "" + value;
    }


    @Override
    public void onDelete(RecyclerView.ViewHolder viewHolder, int position) {

    }

    @Override
    public void onEdit(RecyclerView.ViewHolder viewHolder, int position) {

    }

    @Override
    public void onAddNewEntry(RecyclerView.ViewHolder viewHolder, int position) {

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_attendance_leave, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_leave) {
            startActivity(new Intent(getActivity(), LeaveListActivity.class));
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
