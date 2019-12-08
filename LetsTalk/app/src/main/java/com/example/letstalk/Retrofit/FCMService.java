package com.example.letstalk.Retrofit;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.letstalk.Database.DatabaseHandler;
import com.example.letstalk.LTModel.Message;
import com.example.letstalk.MessageActivity;
import com.example.letstalk.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

public class FCMService extends FirebaseMessagingService {

    private static final String TAG = "FCMService";

    String name, messageBody;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived: " + remoteMessage.getData());

        DatabaseHandler messageDatabaseHandler = new DatabaseHandler(this);

        try {
            //Message
            Log.d(TAG, "onMessageReceived: ");

            JSONObject jsonObject = new JSONObject(remoteMessage.getData().toString());
            name = jsonObject.getString("name");
            String senderId = jsonObject.getString("senderId");
            messageBody = jsonObject.getString("body");
            String messageId = jsonObject.getString("messageId");
            String conversationId = senderId;
            String timeStamp = jsonObject.getString("timeStamp");

            Message message = new Message();

            message.setMessageId(messageId);
            message.setConversionId(conversationId);
            message.setBody(messageBody);
            message.setTimeStamp(timeStamp);
            message.setSenderId(senderId);

            Log.d(TAG, "MessageEntity Class: "+message.toString());

            messageDatabaseHandler.insertMessage(message);


        }
        catch (Exception e)
        {
            Log.e(TAG, "onMessageReceived: Exception " + e.getMessage());
        }
        //Notification

        Log.d(TAG, "onMessageReceived: name " + name);
        Log.d(TAG, "onMessageReceived: message " + messageBody);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(name)
                .setContentText(messageBody)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setSound(RingtoneManager.getDefaultUri( RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                ( NotificationManager ) getSystemService( Context.NOTIFICATION_SERVICE);

        notificationManager.notify(99, notificationBuilder.build());

    }
}
