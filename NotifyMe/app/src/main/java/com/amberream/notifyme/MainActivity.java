package com.amberream.notifyme;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    // constant for the notification action button
    private static final String ACTION_UPDATE_NOTIFICATION = "com.amberream.notifyme.ACTION_UPDATE_NOTIFICATION";

    private Button buttonNotify;
    private Button buttonUpdate;
    private Button buttonCancel;

    private NotificationManager mNotificationManager;

    private NotificationReceiver mNotificationReceiver = new NotificationReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonNotify = findViewById(R.id.notify);
        buttonUpdate = findViewById(R.id.update);
        buttonCancel = findViewById(R.id.cancel);

        setNotificationState(true, false, false);

        createNotificationChannel();

        // register the broadcast receiver to receive the update action from the notification
        registerReceiver(mNotificationReceiver, new IntentFilter(ACTION_UPDATE_NOTIFICATION));
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

        // set up a pending intent to update the notification later
        Intent intent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = getNotificationBuilder();
        builder.addAction(R.drawable.ic_update, getString(R.string.update_notification), updatePendingIntent);

        // deliver the notification
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());

        // enable update and cancel
        setNotificationState(false, true, true);
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

    public void cancelNotification(View view) {
        mNotificationManager.cancel(NOTIFICATION_ID);
        setNotificationState(true, false, false);
    }

    public void updateNotification(View view) {
        Bitmap androidImage = BitmapFactory.decodeResource(getResources(), R.drawable.mascot_1);
        NotificationCompat.Builder builder = getNotificationBuilder();

        // use big picture Style
        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.bigPicture(androidImage);
        style.setBigContentTitle(getString(R.string.notification_updated));
        builder.setStyle(style);
//        builder.setLargeIcon(androidImage);

        // deliver the notification
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());

        // update the buttons
        setNotificationState(false, false, true);
    }

    public void setNotificationState(boolean notify, boolean update, boolean cancel)
    {
        buttonNotify.setEnabled(notify);
        buttonUpdate.setEnabled(update);
        buttonCancel.setEnabled(cancel);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mNotificationReceiver);
        super.onDestroy();
    }

    class NotificationReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateNotification(null);
        }
    }


}
