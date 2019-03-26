package in.technitab.ess.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import in.technitab.ess.model.AttendanceCalendar;
import in.technitab.ess.util.ConstantVariable;


public class ESSdb extends SQLiteOpenHelper {

    private String TAG = ESSdb.class.getSimpleName();
    private static final String DB_NAME = "ESSdb";
    private static final int VERSION = 2;
    private static final String TBL_TASK = "TBL_TASK";
    private static final String TBL_ATTENDANCE = "TBL_ATTENDANCE";
    private static final String TBL_LEAVE = "tbl_leave";
    private static final String TBL_TIMESHEET = "tbl_timesheet";

    public ESSdb(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASK_TABLE = "CREATE TABLE " + TBL_TASK + "(" + ConstantVariable.Tbl_Task.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + ConstantVariable.Tbl_Task.NAME + " VARCHAR2(20),"
                + ConstantVariable.Tbl_Task.IS_DONE + " INTEGER DEFAULT 0," + ConstantVariable.Tbl_Task.DATE + " VARCHAR2(10))";
        db.execSQL(CREATE_TASK_TABLE);

        String CREATE_ATTENDANCE_TABLE = "CREATE TABLE " + TBL_ATTENDANCE + " ( " + ConstantVariable.Tbl_Attendance.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + ConstantVariable.Tbl_Attendance.ATTENDANCE + " VARCHAR2(20) DEFAULT 'not_present'," + ConstantVariable.Tbl_Attendance.PUNCH_IN + " VARCHAR2(20) DEFAULT '' ,"
                + ConstantVariable.Tbl_Attendance.PUNCH_OUT + " VARCHAR2(20) DEFAULT ''," + ConstantVariable.Tbl_Attendance.DATE + " VARCHAR2(10),"
                + ConstantVariable.Tbl_Attendance.SPENT_TIME + " VARCHAR(6), " + ConstantVariable.Tbl_Attendance.MANUAL_PUNCH_IN + " VARCHAR2(20)," + ConstantVariable.Tbl_Attendance.MANUAL_PUNCH_OUT + " VARCHAR2(20),"
                + ConstantVariable.Tbl_Attendance.MANUAL_PUNCH_SYS_IN + " VARCHAR(20)," + ConstantVariable.Tbl_Attendance.MANUAL_PUNCH_SYS_OUT + " VARCHAR2(20), " + ConstantVariable.Tbl_Attendance.IN_PHOTO_PATH + " TEXT,"
                + ConstantVariable.Tbl_Attendance.OUT_PHOTO_PATH + " TEXT, " + ConstantVariable.Tbl_Attendance.IN_ADDRESS + " TEXT," + ConstantVariable.Tbl_Attendance.OUT_ADDRESS + " TEXT," + ConstantVariable.Tbl_Attendance.IN_LOCATION + " TEXT," +
                ConstantVariable.Tbl_Attendance.OUT_LOCATION + " TEXT," + ConstantVariable.Tbl_Attendance.SUMMERY + " VARCHAR2(500) DEFAULT ''," + ConstantVariable.Tbl_Attendance.PUNCH_STATUS + " INTEGER DEFAULT 0," + ConstantVariable.Tbl_Attendance.MONTH_YEAR + " VARCHAR2(10),"+ ConstantVariable.Tbl_Attendance.TIMESHEET_HOURS+ " VARCHAR2(6) DEFAULT '00:00')";

        db.execSQL(CREATE_ATTENDANCE_TABLE);

        String CREATE_TBL_LEAVE = "CREATE TABLE " + TBL_LEAVE + " ( " + ConstantVariable.Tbl_Task.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + ConstantVariable.Tbl_LEAVE.REQUESTED_DATE + " VARCHAR2(10)," + ConstantVariable.Tbl_LEAVE.START_DATE + " VARCHAR2(10),"
                + ConstantVariable.Tbl_LEAVE.START_DATE_LEAVE_TYPE + " VARCHAR2(15)," + ConstantVariable.Tbl_LEAVE.END_DATE + " VARCHAR2(10),"
                + ConstantVariable.Tbl_LEAVE.END_DATE_LEAVE_TYPE + " VARCHAR(15), " + ConstantVariable.Tbl_LEAVE.REASON + " VARCHAR2(15)," + ConstantVariable.Tbl_LEAVE.DESCRIPTION + " VARCHAR2(200),"
                + ConstantVariable.Tbl_LEAVE.LEAVE_LOCATION + " VARCHAR(30)," + ConstantVariable.Tbl_LEAVE.STATUS + " VARCHAR2(10) DEFAULT 'pending')";

        db.execSQL(CREATE_TBL_LEAVE);


        String CREATE_TBL_TIMESHEET = "CREATE TABLE " + TBL_TIMESHEET + " ( " + ConstantVariable.Tbl_TimeSheet.DATE + " VARCHAR2(15) NOT NULL,"
                + ConstantVariable.Tbl_TimeSheet.PROJECT_ID + " INTEGER(10) DEFAULT 0," + ConstantVariable.Tbl_TimeSheet.PROJECT + " VARCHAR2(30) DEFAULT '',"
                + ConstantVariable.Tbl_TimeSheet.ACTIVITY_TYPE_ID + " INTEGER(10) DEFAULT 0," + ConstantVariable.Tbl_TimeSheet.ACTIVITY + " VARCHAR2(30) DEFAULT '',"
                + ConstantVariable.Tbl_TimeSheet.TIME_SPENT + " VARCHAR(10) DEFAULT '0.0', " + ConstantVariable.Tbl_TimeSheet.DESCRIPTION + " VARCHAR2(100) DEFAULT '')";

        db.execSQL(CREATE_TBL_TIMESHEET);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        switch (oldVersion) {

            case 1:
                String ALTER_ATTENDANCE = "ALTER TABLE "+ TBL_ATTENDANCE+ " ADD COLUMN "+ ConstantVariable.Tbl_Attendance.TIMESHEET_HOURS + " VARCHAR2(6) DEFAULT '0' ";
                db.execSQL(ALTER_ATTENDANCE);
        }

    }

    public ArrayList<AttendanceCalendar> fetchAttendance(String month, String year) {
        ArrayList<AttendanceCalendar> list = new ArrayList<>();

        String month_year = year + "-" + month;
        Log.d(TAG, month_year);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from " + TBL_ATTENDANCE + " where " + ConstantVariable.Tbl_Attendance.MONTH_YEAR + "=?", new String[]{month_year});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String date = cursor.getString(cursor.getColumnIndex(ConstantVariable.Tbl_Attendance.DATE));
                String punchIn = cursor.getString(cursor.getColumnIndex(ConstantVariable.Tbl_Attendance.PUNCH_IN));
                String punchOut = cursor.getString(cursor.getColumnIndex(ConstantVariable.Tbl_Attendance.PUNCH_OUT));
                String attendanceType = cursor.getString(cursor.getColumnIndex(ConstantVariable.Tbl_Attendance.ATTENDANCE));
                String spentTime = cursor.getString(cursor.getColumnIndex(ConstantVariable.Tbl_Attendance.SPENT_TIME));
                String manualPunchIn = cursor.getString(cursor.getColumnIndex(ConstantVariable.Tbl_Attendance.MANUAL_PUNCH_IN));
                String manualPunchOut = cursor.getString(cursor.getColumnIndex(ConstantVariable.Tbl_Attendance.MANUAL_PUNCH_OUT));
                String summery = cursor.getString(cursor.getColumnIndex(ConstantVariable.Tbl_Attendance.SUMMERY));
                String timesheetHours = cursor.getString(cursor.getColumnIndex(ConstantVariable.Tbl_Attendance.TIMESHEET_HOURS));
                list.add(new AttendanceCalendar(getDate(date), attendanceType, spentTime, punchIn, punchOut, manualPunchIn, manualPunchOut, summery,timesheetHours));
            }
            cursor.close();
        }
        db.close();
        return list;
    }

    public ArrayList<Integer> getAttendanceDate(String month, String year) {
        ArrayList<Integer> mList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from " + TBL_ATTENDANCE + " where " + ConstantVariable.Tbl_Attendance.MONTH_YEAR + "=?", new String[]{year + "-" + month});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String date = cursor.getString(cursor.getColumnIndex(ConstantVariable.Tbl_Attendance.DATE));
                mList.add(getOnlyDate(date));
            }
            cursor.close();
        }
        db.close();
        return mList;
    }

    private int getOnlyDate(String date) {
        Date d = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            d = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d != null ? d.getDate() : 0;
    }

    private Date getDate(String date) {
        Date d = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            d = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }



    public boolean addPunchIN(String attendanceStatus, String time, String photoPath, String location, String address, int punchStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ConstantVariable.Tbl_Attendance.ATTENDANCE, attendanceStatus);
        cv.put(ConstantVariable.Tbl_Attendance.PUNCH_IN, time);
        cv.put(ConstantVariable.Tbl_Attendance.IN_PHOTO_PATH, photoPath);
        cv.put(ConstantVariable.Tbl_Attendance.IN_LOCATION, location);
        cv.put(ConstantVariable.Tbl_Attendance.IN_ADDRESS, address);
        cv.put(ConstantVariable.Tbl_Attendance.DATE, getCurrentDate());
        cv.put(ConstantVariable.Tbl_Attendance.PUNCH_STATUS, punchStatus);
        cv.put(ConstantVariable.Tbl_Attendance.MONTH_YEAR, getMonthYear());
        long result = db.insert(TBL_ATTENDANCE, null, cv);
        return result != -1;
    }


    public int getPunchStatus() {
        int punchStatus = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from " + TBL_ATTENDANCE + " WHERE " + ConstantVariable.Tbl_Attendance.DATE + "=?", new String[]{getCurrentDate()});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToNext();
            punchStatus = cursor.getInt(cursor.getColumnIndex(ConstantVariable.Tbl_Attendance.PUNCH_STATUS));
            cursor.close();
        }
        return punchStatus;
    }

    public String getPunchInTime(String date) {
        String punchInTime = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from " + TBL_ATTENDANCE + " WHERE " + ConstantVariable.Tbl_Attendance.DATE + "=?", new String[]{date});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToNext();
            punchInTime = cursor.getString(cursor.getColumnIndex(ConstantVariable.Tbl_Attendance.PUNCH_IN));
            cursor.close();
        }

        return punchInTime == null?"":punchInTime;
    }


    public String getManualPunchInTime(String date) {
        String punchInTime = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from " + TBL_ATTENDANCE + " WHERE " + ConstantVariable.Tbl_Attendance.DATE + "=?", new String[]{date});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToNext();
            punchInTime = cursor.getString(cursor.getColumnIndex(ConstantVariable.Tbl_Attendance.MANUAL_PUNCH_IN));
            cursor.close();
        }

        return punchInTime == null?"":punchInTime;
    }

    public String getManualPunchOutTime(String date) {
        String punchInTime = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from " + TBL_ATTENDANCE + " WHERE " + ConstantVariable.Tbl_Attendance.DATE + "=?", new String[]{date});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToNext();
            punchInTime = cursor.getString(cursor.getColumnIndex(ConstantVariable.Tbl_Attendance.MANUAL_PUNCH_OUT));
            cursor.close();
        }

        return punchInTime == null?"":punchInTime;
    }

    public String getPunchOUTTime(String date){
        String punchOut = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * from " + TBL_ATTENDANCE + " WHERE " + ConstantVariable.Tbl_Attendance.DATE + "=?", new String[]{date});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToNext();
            punchOut = cursor.getString(cursor.getColumnIndex(ConstantVariable.Tbl_Attendance.PUNCH_OUT));
            cursor.close();
        }

        return punchOut == null?"":punchOut;

    }


    public boolean addManualPunchIN(String attendanceStatus, String time, String sysTime, String photoPath, String location, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ConstantVariable.Tbl_Attendance.ATTENDANCE, attendanceStatus);
        cv.put(ConstantVariable.Tbl_Attendance.MANUAL_PUNCH_IN, time);
        cv.put(ConstantVariable.Tbl_Attendance.MANUAL_PUNCH_SYS_IN, sysTime);
        cv.put(ConstantVariable.Tbl_Attendance.IN_PHOTO_PATH, photoPath);
        cv.put(ConstantVariable.Tbl_Attendance.IN_LOCATION, location);
        cv.put(ConstantVariable.Tbl_Attendance.IN_ADDRESS, address);
        long result = db.insert(TBL_ATTENDANCE, null, cv);
        return result != -1;
    }

    public void addAttendance(String date, String attendanceStatus, String punchIn, String manualPunchIn, String manualPunchSysIn, String inPhotoPath, String inLocation, String inAddress, String punchOut, String manualPunchOut, String manualPunchSysOut, String outPhotoPath, String outLocation, String outAddress, String summery, String spentHours, int punchStatus,String timesheetLohHours) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ConstantVariable.Tbl_Attendance.ATTENDANCE, attendanceStatus);
        cv.put(ConstantVariable.Tbl_Attendance.PUNCH_IN, punchIn);
        cv.put(ConstantVariable.Tbl_Attendance.IN_PHOTO_PATH, inPhotoPath);
        cv.put(ConstantVariable.Tbl_Attendance.IN_LOCATION, inLocation);
        cv.put(ConstantVariable.Tbl_Attendance.IN_ADDRESS, inAddress);
        cv.put(ConstantVariable.Tbl_Attendance.DATE, date);
        cv.put(ConstantVariable.Tbl_Attendance.PUNCH_STATUS, punchStatus);
        cv.put(ConstantVariable.Tbl_Attendance.MONTH_YEAR, getMonthYear(date));
        cv.put(ConstantVariable.Tbl_Attendance.PUNCH_OUT, punchOut);
        cv.put(ConstantVariable.Tbl_Attendance.OUT_PHOTO_PATH, outPhotoPath);
        cv.put(ConstantVariable.Tbl_Attendance.OUT_LOCATION, outLocation);
        cv.put(ConstantVariable.Tbl_Attendance.OUT_ADDRESS, outAddress);
        cv.put(ConstantVariable.Tbl_Attendance.SUMMERY, summery);
        cv.put(ConstantVariable.Tbl_Attendance.MANUAL_PUNCH_IN, manualPunchIn);
        cv.put(ConstantVariable.Tbl_Attendance.MANUAL_PUNCH_OUT, manualPunchOut);
        cv.put(ConstantVariable.Tbl_Attendance.MANUAL_PUNCH_SYS_IN, manualPunchSysIn);
        cv.put(ConstantVariable.Tbl_Attendance.MANUAL_PUNCH_SYS_OUT, manualPunchSysIn);
        cv.put(ConstantVariable.Tbl_Attendance.SPENT_TIME, spentHours);
        cv.put(ConstantVariable.Tbl_Attendance.PUNCH_STATUS, punchStatus);//punch_in 1 punch_out 2  in this method 2
        cv.put(ConstantVariable.Tbl_Attendance.TIMESHEET_HOURS,timesheetLohHours);
        long result = db.insert(TBL_ATTENDANCE, null, cv);
    }

    public void addManualAttendance(String date, String attendanceStatus, String punchIn, String manualPunchIn, String manualPunchSysIn, String inPhotoPath, String inLocation, String inAddress, String punchOut, String manualPunchOut, String manualPunchSysOut, String outPhotoPath, String outLocation, String outAddress, String summery, String spentHours, int punchStatus,String timesheetLohHours) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ConstantVariable.Tbl_Attendance.ATTENDANCE, attendanceStatus);
        cv.put(ConstantVariable.Tbl_Attendance.PUNCH_IN, punchIn);
        cv.put(ConstantVariable.Tbl_Attendance.IN_PHOTO_PATH, inPhotoPath);
        cv.put(ConstantVariable.Tbl_Attendance.IN_LOCATION, inLocation);
        cv.put(ConstantVariable.Tbl_Attendance.IN_ADDRESS, inAddress);
        cv.put(ConstantVariable.Tbl_Attendance.DATE, date);
        cv.put(ConstantVariable.Tbl_Attendance.PUNCH_STATUS, punchStatus);
        cv.put(ConstantVariable.Tbl_Attendance.MONTH_YEAR, getMonthYear(date));
        cv.put(ConstantVariable.Tbl_Attendance.PUNCH_OUT, punchOut);
        cv.put(ConstantVariable.Tbl_Attendance.OUT_PHOTO_PATH, outPhotoPath);
        cv.put(ConstantVariable.Tbl_Attendance.OUT_LOCATION, outLocation);
        cv.put(ConstantVariable.Tbl_Attendance.OUT_ADDRESS, outAddress);
        cv.put(ConstantVariable.Tbl_Attendance.SUMMERY, summery);
        cv.put(ConstantVariable.Tbl_Attendance.MANUAL_PUNCH_IN, manualPunchIn);
        cv.put(ConstantVariable.Tbl_Attendance.MANUAL_PUNCH_OUT, manualPunchOut);
        cv.put(ConstantVariable.Tbl_Attendance.MANUAL_PUNCH_SYS_IN, manualPunchSysIn);
        cv.put(ConstantVariable.Tbl_Attendance.MANUAL_PUNCH_SYS_OUT, manualPunchSysIn);
        cv.put(ConstantVariable.Tbl_Attendance.SPENT_TIME, spentHours);
        cv.put(ConstantVariable.Tbl_Attendance.PUNCH_STATUS, punchStatus);//punch_in 1 punch_out 2  in this method 2
        cv.put(ConstantVariable.Tbl_Attendance.TIMESHEET_HOURS,timesheetLohHours);

        Cursor cursor = db.rawQuery("SELECT * from "+TBL_ATTENDANCE + " WHERE " + ConstantVariable.Tbl_Attendance.DATE+"=?", new String[]{date});
        long result;
        if (cursor != null && cursor.getCount() >0){
            result = db.update(TBL_ATTENDANCE, cv, ConstantVariable.Tbl_Attendance.DATE + "=?", new String[]{date});
        }else{
            result = db.insert(TBL_ATTENDANCE,null,cv);
        }
        Log.d(TAG,"result "+result);
        if (cursor != null){
            cursor.close();
        }
    }
    private String getMonthYear(String date) {
        String[] parts = date.split("-");
        return parts[0] + "-" + parts[1];
    }


    public boolean addPunchOUT(String time,String attendance, String photoPath, String location, String address, String summery, String spent_hours, int punchStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ConstantVariable.Tbl_Attendance.PUNCH_OUT, time);
        cv.put(ConstantVariable.Tbl_Attendance.ATTENDANCE, attendance);
        cv.put(ConstantVariable.Tbl_Attendance.OUT_PHOTO_PATH, photoPath);
        cv.put(ConstantVariable.Tbl_Attendance.OUT_LOCATION, location);
        cv.put(ConstantVariable.Tbl_Attendance.OUT_ADDRESS, address);
        cv.put(ConstantVariable.Tbl_Attendance.SUMMERY, summery);
        cv.put(ConstantVariable.Tbl_Attendance.SPENT_TIME, spent_hours);
        cv.put(ConstantVariable.Tbl_Attendance.PUNCH_STATUS, punchStatus);
        long result = db.update(TBL_ATTENDANCE, cv, ConstantVariable.Tbl_Attendance.DATE + "=?", new String[]{getCurrentDate()});
        return result != -1;
    }


    public void updateTimesheetLogHours(String timesheetLogHours, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ConstantVariable.Tbl_Attendance.TIMESHEET_HOURS,timesheetLogHours);
        db.update(TBL_ATTENDANCE, cv,ConstantVariable.Tbl_Attendance.DATE + "=?",new String[]{date});
    }



    private String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(System.currentTimeMillis());
    }

    private String getMonthYear() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        return format.format(System.currentTimeMillis());
    }

    private String getPrevDate() {
        Date mydate = new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(mydate);
    }

}
