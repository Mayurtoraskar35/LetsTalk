package com.example.letstalk.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letstalk.LTModel.Message;
import com.example.letstalk.R;

import java.util.List;

public class MySenderMessageAdapter extends RecyclerView.Adapter<MySenderMessageAdapter.MyMessageViewHolder>{

    Context mContext;

    List<Message> mMessageList;

    public MySenderMessageAdapter(Context context, List<Message> messageList){
        mMessageList = messageList;
        mContext = context;

    }

    @NonNull
    @Override
    public MyMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.sender_message_recyclerview, parent, false);
        return new MySenderMessageAdapter.MyMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyMessageViewHolder holder, int position) {
        Message message = mMessageList.get(position);
        holder.txtSenderMessgeBody.setText(message.body.replace("_"," "));
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public void addMessageToAdapter(Message message) {
        mMessageList.add(message);
        this.notifyItemInserted(mMessageList.size() - 1);
    }

    class MyMessageViewHolder extends RecyclerView.ViewHolder{
        TextView txtSenderMessgeBody;

        public MyMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSenderMessgeBody = itemView.findViewById(R.id.sendermessagebody);

        }
    }
}
