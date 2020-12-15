/*
 * Copyright (c) 2020. Hackdroid https://github.com/raza11409652
 */

package com.project.mlmpro.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.project.mlmpro.R;
import com.project.mlmpro.utils.Constant;
import com.project.mlmpro.utils.SessionHandler;

import java.util.Map;

public class FcmService extends FirebaseMessagingService {
    String TAG = FcmService.class.getSimpleName() ;
    SessionHandler sessionHandler ;
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Constant.DEVICE_TOKEN = s ;

        Log.w(TAG, "onNewToken: Device Token generated "  + s );
        sessionHandler  =new SessionHandler(getApplicationContext());
        sessionHandler.setUserDeviceToken(s);

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            if (remoteMessage.getData() != null) {
                showNotification(remoteMessage);
            }

        }
    }

    private void showNotification(RemoteMessage remoteMessage) {
        Map<String, String> map = remoteMessage.getData();
        String title = map.get("title");
        String body = map.get("content");
//        String imageUri = map.get("image");
        Log.d(TAG, "showNotification: " + map);

        //To get a Bitmap image from the URL received
//        bitmap = getBitmapfromUrl(imageUri);
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        String notificationChannelId = "DelstoUserApp";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(notificationChannelId,
                    "Delstouserapp", NotificationManager.IMPORTANCE_MAX);
            notificationChannel.setDescription("MLM_PRO");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);

        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, notificationChannelId);
//        if (bitmap == null) {
//
//        } else {
//            builder.setAutoCancel(true)
//                    .setDefaults(Notification.DEFAULT_ALL)
//                    .setWhen(System.currentTimeMillis())
//                    .setSmallIcon(R.mipmap.icon_logo_round)
//                    .setTicker("Heart")
//                    .setContentTitle(title)
//                    .setStyle(new NotificationCompat.BigPictureStyle()
//                            .bigPicture(bitmap))/*Notification with Image*/
//                    .setContentText(body)
//                    .setContentInfo("Info")
//            ;
//        }
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.logo_circle)
                .setTicker("Heart")
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("Info")
        ;
        notificationManager.notify(1, builder.build());
    }
}
