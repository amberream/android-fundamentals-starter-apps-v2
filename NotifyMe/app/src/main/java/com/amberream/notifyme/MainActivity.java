package com.amberream.notifyme;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    // notification channels are used in api 26+
    private static final String NOTIFICATION_CHANNEL_ID = "primary_notification_channel";

    // associate the notification with a notification ID so we can update or cancel the notification in the future
    private static final int NOTIFICATION_ID = 0;

    private Button buttonNotify;

    private NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonNotify = findViewById(R.id.notify);
        createNotificationChannel();
    }

    private void createNotificationChannel(){
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // notification channels are available in verions 26 and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Mascot Notification", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setDescription("Notification from Mascot");
            mNotificationManager.createNotificationChannel(channel);
        }
    }

    public void sendNotification(View view) {
        mNotificationManager.notify(NOTIFICATION_ID, getNotificationBuilder().build());
    }

    private NotificationCompat.Builder getNotificationBuilder()
    {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendingItent = PendingIntent.getActivity(this, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle("My notification title");
        builder.setContentText("My notification text");
        builder.setSmallIcon(R.drawable.ic_android);
        builder.setContentIntent(notificationPendingItent);

        // set priority and defaults to get the notification to drop down
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);

        // close the notification when the user taps it
        builder.setAutoCancel(true);
        return builder;
    }
}
