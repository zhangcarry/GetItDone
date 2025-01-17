package com.example.getitdone;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.example.getitdone.MainActivity.CHANNEL_ID;

/**
 * reminder
 */
public class myAlarm extends BroadcastReceiver {
    /**
     * create channel for notification
     * @param context
     */
    public void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Reminder", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Reminder");
            NotificationManager manager = getSystemService(context, NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    /**
     * send notification
     * @param context
     * @param title
     */
    public void sendNotification(Context context, String title) {
        Intent notificationIntent = new Intent(context,MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,0,notificationIntent,0);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        Notification notification = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_bell_check)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(title + " is due now")
                .setContentIntent(contentIntent)
                .setColor(Color.rgb(37,5,109))
                .build();

        notificationManagerCompat.notify(1,notification);
    }

    /**
     * send notification when alarm triggered
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("name");
        sendNotification(context, name );
    }
}
