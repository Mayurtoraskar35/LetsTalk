package com.example.letstalk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.example.letstalk.Adapter.MyRecieverMessageAdapter;
import com.example.letstalk.AppConstant.AppConstant;
import com.example.letstalk.Database.DatabaseHandler;
import com.example.letstalk.LTModel.Message;
import com.example.letstalk.Retrofit.BaseApplication;
import com.example.letstalk.Retrofit.Data;
import com.example.letstalk.Retrofit.FCMAPI;
import com.example.letstalk.Retrofit.MessageEntity;
import com.example.letstalk.Utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MessageActivity extends AppCompatActivity {

    private static final String TAG = "MessageActivity";

    public static final String MYFILTER = "com.my.broadcast.RECEIVER";

    ImageView userImage;

    TextView userName;

    String reciverID, name, mobile, txtmessage, senderID, messageID;

    private long timeStamp;

    FloatingActionButton btnSend;

    EditText message;

    Context context;

    List<Message> messageList;

    private RecyclerView mMessageRecyclerView;

    private MyRecieverMessageAdapter mMyRecieverMessageAdapter;

    Toolbar toolbar;

    DatabaseHandler messageDatabaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        toolbar = findViewById(R.id.msgtoolbar);

        userImage = findViewById(R.id.userimage);
        userName = findViewById(R.id.username);

        btnSend = findViewById(R.id.sender);
        message = findViewById(R.id.Emessage);
        mMessageRecyclerView = findViewById(R.id.message_reciever_recycler_view);

        context = this;
        Intent intent = getIntent();
        reciverID = intent.getStringExtra("ReciverUserID");
        mobile = intent.getStringExtra("number");
        name = getIntent().getStringExtra("name");

        SharedPreferences preferences =getSharedPreferences(AppConstant.PREFERENCE_FILE_NAME,MODE_PRIVATE);
        senderID = preferences.getString(AppConstant.LOGGED_IN_USER_ID, "");

        messageDatabaseHandler = new DatabaseHandler(this);
        userName.setText(name);

        messageList = new ArrayList<Message>();

        //Broadcast
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MYFILTER);
        registerReceiver(broadcastReceiver,intentFilter);

        //Setup recyclerview
        mMessageRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        messageList = messageDatabaseHandler.getMessageData(reciverID);

        Log.d(TAG, "onCreate: "+messageList);

        //mMyRecieverMessageAdapter = new MyRecieverMessageAdapter(context, new ArrayList<>());
        mMyRecieverMessageAdapter = new MyRecieverMessageAdapter(context,messageList);
        mMessageRecyclerView.setAdapter(mMyRecieverMessageAdapter);

        //Drawable drawablle =getResources().getDrawable(R.drawable.ic_send_black_24dp);
        Drawable drawable = btnSend.getDrawable();
        drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        //setupToolBar
        setupToolbar();

    }

    public void setupToolbar(){
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
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
                Message msg = handler.getMessageById(msgId);
                Log.d(TAG, "onReceive: conversationId "+msg.getConversionId());
                mMyRecieverMessageAdapter.addMessageToAdapter(msg);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home :
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
