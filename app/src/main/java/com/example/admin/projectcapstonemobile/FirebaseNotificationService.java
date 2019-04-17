package com.example.admin.projectcapstonemobile;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

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
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(FirebaseNotificationService.this.getApplicationContext(),
                        "Ban nhan dc 1 thong bao tu` " + notiTitle + " voi noi dung la " +
                                notiContent
                        ,Toast.LENGTH_SHORT).show();
            }
        });

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        System.out.println("Dang o service ne`");
        System.out.println(remoteMessage.getData().toString());
    }
}
