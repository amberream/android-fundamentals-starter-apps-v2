package com.amberream.standup;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.ToggleButton;

public class AlarmReceiver extends BroadcastReceiver {
    // for Android 8 (api 27) you need a notification channel
    private static final String NOTIFICATION_CHANNEL = "notificationChannel";
    private static final int NOTIFICATION_ID = 0;

    NotificationManager mNotificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        deliverNotification(context);
    }

    private void deliverNotification(Context context)
    {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL);
        builder.setSmallIcon(R.drawable.ic_standup);
        builder.setContentTitle("Stand Up Alert");
        builder.setContentText("You should stand up and walk around now");
        builder.setContentIntent(pendingIntent);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setAutoCancel(true);

        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
