package in.technitab.ess.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.technitab.ess.R;
import in.technitab.ess.model.TimesheetDataResponse;

public class CalenderGridAdapter extends ArrayAdapter {
    private LayoutInflater mInflater;
    private List<Date> monthlyDates;
    private Calendar currentDate;
    private List<TimesheetDataResponse> allEvents;
    private int dateValue = 0;

    public CalenderGridAdapter(Context context, List<Date> monthlyDates, Calendar currentDate, List<TimesheetDataResponse> allEvents) {
        super(context, R.layout.layout_calender_cell);
        this.monthlyDates = monthlyDates;
        this.currentDate = currentDate;
        this.allEvents = allEvents;
        mInflater = LayoutInflater.from(context);

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

        if (displayMonth == currentMonth && displayYear == currentYear) {
            if (allEvents.size() > dateValue) {
                int value = getDate(allEvents.get(dateValue).getName());
                if (dayValue == value) {

                    String minHourValue = allEvents.get(dateValue).getSpentTime();
                    punchIn.setText(minHourValue);
                    dateValue++;
                }
            }


        } else {
            view.setVisibility(View.GONE);
        }
        cellNumber.setText(String.valueOf(dayValue));
        return view;
    }

    private int getDate(String name) {
        int d = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
        try {
            Date date = format.parse(name);
            d = date.getDate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
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
