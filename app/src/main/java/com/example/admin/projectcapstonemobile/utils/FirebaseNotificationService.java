package com.example.admin.projectcapstonemobile.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.admin.projectcapstonemobile.R;
import com.example.admin.projectcapstonemobile.activity.HomeActivity;
import com.example.admin.projectcapstonemobile.activity.TaskDetailActivity;
import com.example.admin.projectcapstonemobile.fragment.AssignedTaskFragment;
import com.example.admin.projectcapstonemobile.fragment.ViewNotificationFragment;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.core.app.NotificationCompat;

public class FirebaseNotificationService extends FirebaseMessagingService {
    public FirebaseNotificationService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

        // Check if message contains a data payload.

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            System.out.println("Day la remoteMessage ko co data " + remoteMessage.getNotification());
        }
        String notiTitle = remoteMessage.getNotification().getTitle();
        String notiContent = remoteMessage.getNotification().getBody();
        System.out.println("Title cua no la:" + notiTitle);
        System.out.println("Content cua no la " + notiContent);
        sendNotification(remoteMessage.getNotification());

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        System.out.println("Dang o service ne`");
        System.out.println(remoteMessage.getData().toString());
    }
    private void sendNotification(RemoteMessage.Notification notification) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                0);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
