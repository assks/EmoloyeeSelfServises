package in.technitab.ess.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import in.technitab.ess.R;

public class DateCal {

    private static final String TAG = DateCal.class.getSimpleName();
    @SuppressLint("DefaultLocale")
    public static String getHMSFromDates(String startDate, String endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String cal = "00:00";
        try {
            Date oldDate = dateFormat.parse(startDate);
            Date currentDate = dateFormat.parse(endDate);

            long diff = currentDate.getTime() - oldDate.getTime();
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            diff = diff % daysInMilli;
            long elapsedHours = diff / hoursInMilli;
            diff = diff % hoursInMilli;

            long elapsedMinutes = diff / minutesInMilli;
            diff = diff % minutesInMilli;

            long elapsedSeconds = diff / secondsInMilli;

            if (oldDate.before(currentDate)) {
                cal = String.format("%02d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);
            }

        } catch (ParseException e) {

            e.printStackTrace();
        }
        return cal;
    }

    public static String convertDateToDM(String date){
        String newDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            Date d = dateFormat.parse(date);
            SimpleDateFormat timeFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
            newDate = timeFormat.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    public static int getDays(String startDate,String dateEvent) {
        int days;
        Calendar sDate = toCalendar(getLongDate(startDate));
        Calendar eDate = toCalendar(getLongDate(dateEvent));

        if (!sDate.equals(eDate)) {
            long milis1 = sDate.getTimeInMillis();
            long milis2 = eDate.getTimeInMillis();
            long diff = Math.abs(milis2 - milis1);
           days =  (int)(diff / (24 * 60 * 60 * 1000));
        }else {
            days = 1;
        }
        return days;
    }

    public static String getLastDateOfMonth(String date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date convertedDate;
        try {
            convertedDate = dateFormat.parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(convertedDate);
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));

            Date d = c.getTime();
            date = dateFormat.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    public static String getNextMonth(String nowDate) {
        String strDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            Date date = dateFormat.parse(nowDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.MONTH, 1);
            c.set(Calendar.DATE, c.getMaximum(Calendar.DATE));
            Date nextDate = c.getTime();
            strDate = dateFormat.format(nextDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate;
    }

    public static String getAddOnDate(String nowDate,int days){
        String strDate = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            Date date = dateFormat.parse(nowDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, days);
            Date nextDate = c.getTime();
            strDate = dateFormat.format(nextDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate;
    }

    public static int getSiteDays(String startDate,String dateEvent) {
        int days;
        Calendar sDate = toCalendar(getLongDate(startDate));
        Calendar eDate = toCalendar(getLongDate(dateEvent));

        if (!sDate.equals(eDate)) {
            long milis1 = sDate.getTimeInMillis();
            long milis2 = eDate.getTimeInMillis();
            long diff = Math.abs(milis2 - milis1);
            days =  (int)(diff / (24 * 60 * 60 * 1000)) +1;
        }else {
            days = 1;
        }
        return days;
    }


    private static Calendar toCalendar(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    @SuppressLint("DefaultLocale")
    public static String getHMFromData(Context context, String startDate, String endDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String cal = "00:00";
        try {
            Date oldDate = dateFormat.parse(startDate);
            Date currentDate = dateFormat.parse(endDate);

            long diff = currentDate.getTime() - oldDate.getTime();
            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            diff = diff % daysInMilli;
            long elapsedHours = diff / hoursInMilli;
            diff = diff % hoursInMilli;

            long elapsedMinutes = diff / minutesInMilli;

            if (oldDate.before(currentDate)) {
                cal = context.getResources().getString(R.string.min_hours_value, elapsedHours, elapsedMinutes);
            }

            Log.d(TAG,"log hours "+cal);

        } catch (ParseException e) {

            e.printStackTrace();
        }
        return cal;
    }


    public static String addTimeDuration(String startTime, String endTime){

        String addTime = "00:00";
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm",Locale.getDefault());
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date1;
            date1 = timeFormat.parse(startTime);
            Date date2 = timeFormat.parse(endTime);

            long sum = date1.getTime() + date2.getTime();
             addTime = timeFormat.format(new Date(sum));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return addTime;
    }


    public static long subtractTimeDuration(String startTime, String endTime){

        long duration = 0;
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm",Locale.getDefault());
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date1;
            date1 = timeFormat.parse(startTime);
            Date date2 = timeFormat.parse(endTime);
            duration = date2.getTime() - date1.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return duration;
    }


    public static String getDateTime(long milliSecond) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(milliSecond);
    }


    public static long getLongDate(String strDate){
        long duration = 0;
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date = timeFormat.parse(strDate);
            duration = date.getTime();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return duration;
    }

    public static String getDate(long milli) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(milli);
    }

    public static String getAmPmTime(String date) {
        String time = "00:00";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        try {
            Date d = dateFormat.parse(date);
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

            time = timeFormat.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }


    public static int getHours(String date) {
        int time = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        try {
            Date d = dateFormat.parse(date);
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH", Locale.getDefault());
            String strTime = timeFormat.format(d).replaceAll(" ","");
            time = Integer.parseInt(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
}
