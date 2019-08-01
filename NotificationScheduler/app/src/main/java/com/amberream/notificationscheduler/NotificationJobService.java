package com.amberream.notificationscheduler;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class NotificationJobService extends JobService {

    public static final String NOTIFICATION_CHANNEL = "primary_notification_channel";

    private NotificationManager mNotificationManager;


    @Override
    public boolean onStartJob(JobParameters params) {
        createNotificationChannel();

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL);
        builder.setContentTitle("Job Service");
        builder.setContentText("Your Job is Running");
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.ic_job_running);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setAutoCancel(true);

        Notification notification = builder.build();

        mNotificationManager.notify(0, notification);

        // return false because the job is finished
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        // return true to reschedule the job in case it fails
        return true;
    }

    private void createNotificationChannel()
    {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // notification channels are available in Oreo and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            // create the notification channel
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL, "Job service notification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notifications from Job Service");

            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

}
