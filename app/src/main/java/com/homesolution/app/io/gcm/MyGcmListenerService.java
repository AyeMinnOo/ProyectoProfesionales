package com.homesolution.app.io.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.homesolution.app.ui.activity.TalkActivity;
import com.youtube.sorcjc.proyectoprofesionales.R;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "Test/GcmListener";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.d(TAG, "From: " + from);

        // Type of notification
        final String type = data.getString("type");
        Log.d(TAG, "Type: " + type);

        if (type!=null && type.equals("01")) {
            final String title = data.getString("title");
            final String uid = data.getString("uid");

            // Send a broadcast to the TalkActivity
            Intent intent = new Intent("chat-message");
            intent.putExtra("uid", uid);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            // Create a notification if the required chat isn't open
            if (! TalkActivity.isOpened.equals(uid)) {

                // Intent that will be launched when the user taps the notification
                Intent chat = new Intent(this, TalkActivity.class);
                chat.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                Bundle bundle = new Bundle();
                bundle.putString("uid", uid);

                chat.putExtras(bundle);
                sendNotification(title, chat);
            }

        }

    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message, Intent intent) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("Home Solution")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
