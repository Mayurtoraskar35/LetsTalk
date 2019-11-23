package com.example.letstalk.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letstalk.AppConstant.AppConstant;
import com.example.letstalk.LTModel.Message;
import com.example.letstalk.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MyMessageAdapter extends RecyclerView.Adapter<MyMessageAdapter.MyMessageViewHolder> {

    String senderID;

    public static final int MSG_TYPE_LEFT = 0;

    public static final int MSG_TYPE_RIGHT = 1;

    private static final String TAG = "MyMessageAdapter";

    Context mContext;

    List<Message> mMessageList;

    public MyMessageAdapter(Context context, List<Message> messageList){
        mMessageList = messageList;
        mContext = context;
    }

    @NonNull
    @Override
    public MyMessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        Log.d("123 ", "onCreateViewHolder: senderID : "+senderID);

        //viewGroup.setClipToPadding(false);

        if (viewType == MSG_TYPE_RIGHT)
        {
            Log.d("123",  " If onCreateViewHolder: ");
            View view = LayoutInflater.from(mContext).inflate(R.layout.sender_message_recyclerview, viewGroup, false);
            return new MyMessageAdapter.MyMessageViewHolder(view);
        }
        else {
            Log.d("123", "Else onCreateViewHolder: ");
            View view = LayoutInflater.from(mContext).inflate(R.layout.reciever_message_recyclerview, viewGroup, false);
            return new MyMessageAdapter.MyMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyMessageViewHolder holder, int position) {
        Message message = mMessageList.get(position);
        holder.txtReciverMessgeBody.setText(message.getBody().replace("_"," "));

        Long time = Long.parseLong(message.getTimeStamp());
        DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        Date result = new Date(time);

        holder.txtTimeStamp.setText(dateFormat.format(result));
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public void addMessageToAdapter(Message message) {
        mMessageList.add(message);
        this.notifyItemInserted(mMessageList.size() - 1);
    }

    @Override
    public int getItemViewType(int position) {

        SharedPreferences preferences = mContext.getSharedPreferences(AppConstant.PREFERENCE_FILE_NAME,MODE_PRIVATE);
        senderID = preferences.getString(AppConstant.LOGGED_IN_USER_ID, "");

        Log.d("123", "getItemViewType : senderID "+senderID);
        Log.d("123", "getItemViewType : Message SenderID "+mMessageList.get(position).getSenderId());
        if(mMessageList.get(position).getSenderId().equals(senderID))
        {
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }
    }

    class MyMessageViewHolder extends RecyclerView.ViewHolder{
        TextView txtReciverMessgeBody;
        TextView txtTimeStamp;
        public MyMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            txtReciverMessgeBody = itemView.findViewById(R.id.messagebody);
            txtTimeStamp = itemView.findViewById(R.id.timestamp);
        }
    }
}