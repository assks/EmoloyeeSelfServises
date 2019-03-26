package in.technitab.ess.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import in.technitab.ess.R;
import in.technitab.ess.activity.MainActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyNotification {
    private Context context;

    public MyNotification(Context context) {
        this.context = context;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(ConstantVariable.CHANNEL_ID, ConstantVariable.CHANNEL_NAME, importance);
            mChannel.setDescription(ConstantVariable.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }

    }

    public void displayNotification(String title, String body,Class activity) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, ConstantVariable.CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_group_work)
                        .setContentTitle(title)
                        .setAutoCancel(true)
                        .setVibrate(new long[]{1000, 1000})
                        .setContentText(body)
                        .setColor(context.getResources().getColor(R.color.colorPrimary));

        Intent resultIntent;
        if (title.equalsIgnoreCase(ConstantVariable.REQUEST_LEAVE)) {
            resultIntent = new Intent(context,activity);
        } else if (title.equalsIgnoreCase(ConstantVariable.TEC_)) {
            resultIntent = new Intent(context, activity);
        } else if (title.equalsIgnoreCase(ConstantVariable.LEAVE_APPROVED) || title.equalsIgnoreCase(ConstantVariable.LEAVE_REJECTED)) {
            resultIntent = new Intent(context, activity);
        } else if (title.equalsIgnoreCase("Vendor Request")) {
            resultIntent = new Intent(context, activity);
        }else if (title.equalsIgnoreCase("Request Assign Project")) {
            resultIntent = new Intent(context, activity);
        } else {
            resultIntent = new Intent(context, MainActivity.class);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (mNotifyMgr != null) {
            mNotifyMgr.notify(1, mBuilder.build());
        }
    }
}
