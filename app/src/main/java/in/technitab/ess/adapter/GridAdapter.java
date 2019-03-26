package in.technitab.ess.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.technitab.ess.R;
import in.technitab.ess.model.AttendanceCalendar;

public class GridAdapter extends ArrayAdapter {

    private LayoutInflater mInflater;
    private List<Date> monthlyDates;
    private Calendar currentDate;
    private List<AttendanceCalendar> allEvents;
    private List<Integer> mEventDates;
    private int selectedPosition;
    private Context mContext;

    public GridAdapter(Context context, List<Date> monthlyDates, int selectedPosition, Calendar currentDate, List<AttendanceCalendar> allEvents, List<Integer> mEventDates) {
        super(context, R.layout.layout_calender_cell);
        this.monthlyDates = monthlyDates;
        this.currentDate = currentDate;
        this.allEvents = allEvents;
        this.mEventDates = mEventDates;
        mInflater = LayoutInflater.from(context);
        this.selectedPosition = selectedPosition;
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        Date mDate = monthlyDates.get(position);
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(mDate);
        int dayValue = dateCal.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCal.get(Calendar.MONTH) + 1;
        int displayYear = dateCal.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentYear = currentDate.get(Calendar.YEAR);

        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.layout_calender_cell, parent, false);
        }
        TextView cellNumber = view.findViewById(R.id.calendar_date);
        TextView punchIn = view.findViewById(R.id.punch_in);
        TextView punchOut = view.findViewById(R.id.punch_out);

        if (position == selectedPosition) {
            view.setBackgroundColor(mContext.getResources().getColor(R.color.colorDivider));
        }


        if (displayMonth == currentMonth && displayYear == currentYear) {

            if (mEventDates.size() > 0 && mEventDates.contains(mDate.getDate())) {
                int index = mEventDates.indexOf(mDate.getDate());
                AttendanceCalendar attendanceCalendar = allEvents.get(index);
                String attendanceType = attendanceCalendar.getAttendanceType();
                if (attendanceType.equalsIgnoreCase(mContext.getResources().getString(R.string.full_day_present)) || attendanceType.equalsIgnoreCase(mContext.getString(R.string.first_half))
                        || attendanceType.equalsIgnoreCase(mContext.getString(R.string.second_half))) {

                    String spentTime = attendanceCalendar.getSpentTime();
                    String minHourValue = attendanceCalendar.getTimesheetHours();
                    punchIn.setText(minHourValue);
                    punchOut.setText(spentTime);
                }

                updateUiBackground(dayValue, view, attendanceCalendar);
            }
        } else {
            view.setVisibility(View.GONE);
        }
        cellNumber.setText(String.valueOf(dayValue));
        return view;
    }

    private void updateUiBackground(int dayValue, View view, AttendanceCalendar attendanceCalendar) {
        int dateValue = attendanceCalendar.getDate().getDate();
        String attendanceType = attendanceCalendar.getAttendanceType();
        if (dateValue == dayValue && attendanceType.equalsIgnoreCase(mContext.getString(R.string.full_day))) {

            view.setBackgroundColor(mContext.getResources().getColor(R.color.colorLeaves));

        } else if (dateValue == dayValue && attendanceType.equalsIgnoreCase(mContext.getString(R.string.first_half))
                || attendanceType.equalsIgnoreCase(mContext.getString(R.string.second_half))) {
            view.setBackgroundColor(mContext.getResources().getColor(R.color.colorIstIInd));
        } else if (dateValue == dayValue && attendanceType.equalsIgnoreCase("")) {
            view.setEnabled(false);
        } else if (dateValue == dayValue && (attendanceType.equalsIgnoreCase(mContext.getResources().getString(R.string.weekend))
                || attendanceType.equalsIgnoreCase(mContext.getResources().getString(R.string.holiday)))) {
            view.setBackgroundColor(mContext.getResources().getColor(R.color.colorHolidayWeekend));
        }
    }

    @Override
    public int getCount() {
        return monthlyDates.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return monthlyDates.get(position);
    }

    @Override
    public int getPosition(Object item) {
        return monthlyDates.indexOf(item);
    }

}
