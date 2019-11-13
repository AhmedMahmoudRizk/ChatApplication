package com.example.chatapplication;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.core.app.NotificationCompat;


public class MyService extends FirebaseMessagingService {
    private Context context = this;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        FirebaseInstanceId.getInstance().getToken();
        Log.d("zzzzz", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        sendRegistrationToServer(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.i("notificationReceived", "----->" + remoteMessage.getData().toString());
//        if (remoteMessage.getData().size() > 0) {
//            sendUserNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("text"));
//        }

    }

    private void sendNotification(Bitmap bitmap) {


        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.bigPicture(bitmap);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_MAX);

            //Configure Notification Channel
            notificationChannel.setDescription("Game Notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("title")
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentText("content")
                .setContentIntent(pendingIntent)
                .setStyle(style)
                .setLargeIcon(bitmap)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX);


        notificationManager.notify(1, notificationBuilder.build());


    }

//    private void sendUserNotification(String title, String mess) {
//        int notifyID = 1;
//        Intent intent;
//        NotificationChannel mChannel;
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        intent = new Intent(context, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        String CHANNEL_ID = context.getPackageName();// The id of the channel.
//        CharSequence name = "Sample one";// The User-visible name of the channel.
//        int importance = 0;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            importance = NotificationManager.IMPORTANCE_HIGH;
//        }
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
//        notificationBuilder.setContentTitle(title);
//        notificationBuilder.setAutoCancel(true);
//        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
//        notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//        notificationBuilder.setContentIntent(pendingIntent);
//        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(mess));
//        notificationBuilder.setContentText(mess);
//        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
//        notificationBuilder.setSmallIcon(getNotificationIcon(notificationBuilder));
//
//        NotificationManager notificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
//            notificationManager.createNotificationChannel(mChannel);
//        }
//        if (notificationManager != null) {
//            notificationManager.notify(notifyID /* ID of notification */, notificationBuilder.build());
//        }
//
//
//    }

    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = 0x036085;
            notificationBuilder.setColor(color);
            return R.mipmap.ic_launcher;

        } else {
            return R.mipmap.ic_launcher;
        }
    }

}