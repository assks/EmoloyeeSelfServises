package in.technitab.ess.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class ReminderData {

    private static final String APP_SHARED_PREFS = "RemindMePref";

    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    private static final String reminderStatus="reminderStatus";
    private static final String punchInHour="punchInHour";
    private static final String punchInMin="punchInMin";
    private static final String punchOutHour="punchOutHour";
    private static final String punchOutMin="punchOutMin";

    @SuppressLint("CommitPrefEdits")
    public ReminderData(Context context)
    {
        this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    // Settings Page Set Reminder

    public boolean getReminderStatus()
    {
        return appSharedPrefs.getBoolean(reminderStatus, false);
    }

    public void setReminderStatus(boolean status) {
        prefsEditor.putBoolean(reminderStatus, status);
        prefsEditor.commit();
    }

    // Settings Page Reminder Time (Hour)

    public int getPunchOutHour() {
        return appSharedPrefs.getInt(punchOutHour, 18);
    }

    public void setPunchOutHour(int h) {
        prefsEditor.putInt(punchOutHour, h);
        prefsEditor.apply();
    }

    // Settings Page Reminder Time (Minutes)

    public int getPunchOutMin() {
        return appSharedPrefs.getInt(punchOutMin, 0);
    }

    public void setPunchOutMin(int m) {
        prefsEditor.putInt(punchOutMin, m);
        prefsEditor.apply();
    }

    public int getPunchInHour() {
        return appSharedPrefs.getInt(punchInHour, 9);
    }

    public void setPunchInHour(int h) {
        prefsEditor.putInt(punchInHour, h);
        prefsEditor.apply();
    }

    // Settings Page Reminder Time (Minutes)

    public int getPunchInMin() {
        return appSharedPrefs.getInt(punchInMin, 0);
    }

    public void setPunchInMin(int m) {
        prefsEditor.putInt(punchInMin, m);
        prefsEditor.apply();
    }


    public void reset()
    {
        prefsEditor.clear();
        prefsEditor.apply();

    }

}

