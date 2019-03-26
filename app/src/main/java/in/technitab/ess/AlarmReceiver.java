package in.technitab.ess;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import in.technitab.ess.activity.MainActivity;
import in.technitab.ess.util.ReminderData;

public class AlarmReceiver extends BroadcastReceiver {
    String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                ReminderData localData = new ReminderData(context);
                NotificationScheduler.setPunchInReminder(context, AlarmReceiver.class, localData.getPunchInHour(), localData.getPunchInMin());
                NotificationScheduler.setPunchOutReminder(context, AlarmReceiver.class, localData.getPunchOutHour(), localData.getPunchOutMin());
                return;
            }
        }

        Log.d(TAG, "onReceive: ");

        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.HOUR_OF_DAY) < 12)
        NotificationScheduler.showNotification(context, MainActivity.class,
                "PunchIn Reminder", "Do your punch in");
        else
            NotificationScheduler.showNotification(context, MainActivity.class,
                    "PunchOut Reminder", "Do your punch out ");
    }

}
