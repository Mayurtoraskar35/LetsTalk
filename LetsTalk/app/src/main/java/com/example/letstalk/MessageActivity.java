package com.example.letstalk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.letstalk.Adapter.MyRecieverMessageAdapter;
import com.example.letstalk.Adapter.MySenderMessageAdapter;
import com.example.letstalk.AppConstant.AppConstant;
import com.example.letstalk.Database.DatabaseHandler;
import com.example.letstalk.LTModel.Message;
import com.example.letstalk.Retrofit.BaseApplication;
import com.example.letstalk.Retrofit.Data;
import com.example.letstalk.Retrofit.FCMAPI;
import com.example.letstalk.Retrofit.MessageEntity;
import com.example.letstalk.Utils.Utils;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MessageActivity extends AppCompatActivity {

    private static final String TAG = "MessageActivity";

    public static final String MYFILTER = "com.my.broadcast.RECEIVER";

    String reciverID, name, mobile, txtmessage, senderID, messageID;

    private long timeStamp;

    ImageButton btnSend;

    EditText message;

    Context context;

    private RecyclerView mMessageRecieverRecyclerView, mMessageSenderRecyclerView;

    private MyRecieverMessageAdapter mMyRecieverMessageAdapter;

    private MySenderMessageAdapter mMySenderMessageAdapter;

    DatabaseHandler messageDatabaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        btnSend = findViewById(R.id.sender);
        message = findViewById(R.id.Emessage);
        mMessageRecieverRecyclerView = findViewById(R.id.message_reciever_recycler_view);
        mMessageSenderRecyclerView = findViewById(R.id.message_sender_recycler_view);
        context = this;
        Intent intent = getIntent();
        reciverID = intent.getStringExtra("ReciverUserID");
        mobile = intent.getStringExtra("number");
        name = getIntent().getStringExtra("name");

        SharedPreferences preferences =getSharedPreferences(AppConstant.PREFERENCE_FILE_NAME,MODE_PRIVATE);
        senderID = preferences.getString(AppConstant.LOGGED_IN_USER_ID, "");

        //Broadcast
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MYFILTER);
        registerReceiver(broadcastReceiver,intentFilter);

        //Setup recyclerview
        mMessageRecieverRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mMyRecieverMessageAdapter = new MyRecieverMessageAdapter(context, new ArrayList<>());
        mMessageRecieverRecyclerView.setAdapter(mMyRecieverMessageAdapter);

        mMessageSenderRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mMySenderMessageAdapter = new MySenderMessageAdapter(context, new ArrayList<>());
        mMessageSenderRecyclerView.setAdapter(mMySenderMessageAdapter);


        messageDatabaseHandler = new DatabaseHandler(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {

                String msgId = bundle.getString("messageId");
                Log.d(TAG, "on BroadCast Receive: "+msgId);

                Log.d(TAG, "onReceive: senderID "+senderID);


                //Get message data from database using messageId
                DatabaseHandler handler = new DatabaseHandler(context);
                Message message = handler.getMessageById(msgId);

                Log.d(TAG, "onReceive: conversationId "+message.getConversionId());

                if(senderID.equals(message.getSenderId()))
                {
                    mMyRecieverMessageAdapter.addMessageToAdapter(message);
                }
                else{
                    mMySenderMessageAdapter.addMessageToAdapter(message);
                }


            }

        }
    };

    public void sendMessage(View view) {

        timeStamp = System.currentTimeMillis();

        String time= String.valueOf(timeStamp);

        txtmessage = message.getText().toString().replace(" ","_");

        messageID = Utils.generateUniqueMessageId();


        Log.d(TAG, "sendMessage: senderID"+senderID);
        Log.d(TAG, "sendMessage: recieverID"+reciverID);
        Log.d(TAG, "sendMessage: name"+ name);
        Retrofit retrofit = BaseApplication.getRetrofitInstance();
        FCMAPI api = retrofit.create(FCMAPI.class);
        MessageEntity messageEntity = new MessageEntity();
        Data data = new Data();
        data.senderId = senderID;
        data.receiverId = reciverID;
        data.body = txtmessage;
        data.messageId = messageID;
        data.timeStamp = timeStamp;
        messageEntity.data = data;
        messageEntity.to = "/topics/"+ reciverID;
        Log.d(TAG, "sendMessage: RecevierId : " + reciverID);

        saveMessage(senderID,reciverID,messageID,txtmessage,time);

        api.sendMessage(messageEntity).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.code() == 200)
                {
                    Log.d(TAG, "onResponse: MessageEntity send successfully");
                }
                else
                    {
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
        message.setText("");
    }

    public void saveMessage(String senderID, String reciverID, String messageID, String body, String timeStamp){

        Log.d(TAG, "saveMessage: ");
        Log.d(TAG, "saveMessage: sernderID "+senderID);
        Log.d(TAG, "saveMessage: reciverID "+reciverID);
        Log.d(TAG, "saveMessage: msgID "+messageID);
        Log.d(TAG, "saveMessage: msgBody "+body);
        Log.d(TAG, "saveMessage: timeStamp"+timeStamp);

        Message message = new Message();

        message.setMessageId(messageID);
        message.setConversionId(reciverID);
        message.setBody(body);
        message.setTimeStamp(timeStamp);
        message.setSenderId(senderID);

        Log.d(TAG, "MessageEntity Class: "+message.toString());

        messageDatabaseHandler.insertMessage(message);

    }
}
