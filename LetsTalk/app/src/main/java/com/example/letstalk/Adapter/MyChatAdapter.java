package com.example.letstalk.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.letstalk.LTModel.Chat;
import com.example.letstalk.MessageActivity;
import com.example.letstalk.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyChatAdapter extends RecyclerView.Adapter<MyChatAdapter.MyChatViewHolder> {

    private static final String TAG = "MyChatAdapter";

    List<Chat> mChatList;

    Context mContext;

    Chat chat;

    public MyChatAdapter(Context context, List<Chat> chatList){
        mChatList =chatList;
        Log.d(TAG, "MyContactAdapter: "+mChatList);
        mContext = context;
        Log.d(TAG, "MyContactAdapter: "+mContext);
    }

    @NonNull
    @Override
    public MyChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_layout, viewGroup, false);
        return new MyChatAdapter.MyChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyChatViewHolder myChatViewHolder, int position) {

        chat = mChatList.get(position);

        String name = chat.userContact.getUserName().toString().trim().substring(0,1).toUpperCase() + chat.userContact.getUserName().toString().trim().substring(1).toLowerCase();

        Log.d(TAG, "onBindViewHolder: name "+name);

        String surname = chat.userContact.getUserSurname().toString().trim().substring(0,1).toUpperCase() + chat.userContact.getUserSurname().toString().trim().substring(1).toLowerCase();

        Log.d(TAG, "onBindViewHolder: surname "+surname);

        String fullName = name + " " + surname;

        String s =fullName.substring(0,1);

        Log.d(TAG, "onBindViewHolder: s = "+s);

        myChatViewHolder.txtUserName.setText(fullName);

        ColorGenerator generator = ColorGenerator.DEFAULT;

        int color = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder().buildRound(s, color);
        myChatViewHolder.image.setImageDrawable(drawable);

        String message = chat.message.getBody().toString();

        Log.d(TAG, "onBindViewHolder: Body "+message);

        myChatViewHolder.txtMessage.setText(message);

        Long time = Long.parseLong(chat.message.getTimeStamp());
        DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        Date result = new Date(time);

        myChatViewHolder.txtTimestamp.setText(dateFormat.format(result));

        int unreadMessage = chat.getUnreadCount();

        String uId = chat.userContact.getUserId();

        String userMobile = chat.userContact.getUserMobile();

        Log.d(TAG, "onBindViewHolder: unreadCount"+unreadMessage);

        myChatViewHolder.txtUnread.setText(String.valueOf(unreadMessage));

        myChatViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick: ReciverUserId "+chat.userContact.getUserId());
                Log.d(TAG, "onClick: number "+chat.userContact.getUserMobile());

                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("ReciverUserID",uId);
                intent.putExtra("number",userMobile);
                intent.putExtra("name",fullName);
                mContext.startActivity(intent);
            }
        });

    }

    public void updateChatList(Chat chat, int indexToRemove, boolean isAlreaduExits) {
        Log.d( TAG, "isAlreaduExits: "+isAlreaduExits );
        if(isAlreaduExits){
            mChatList.remove( indexToRemove);
        }
        mChatList.add( 0, chat );
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }

    class MyChatViewHolder extends RecyclerView.ViewHolder{

        TextView txtUserName, txtMessage, txtTimestamp, txtUnread;

        ImageView image;

        public MyChatViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.userprofile);
            txtUserName = itemView.findViewById(R.id.txtusername);
            txtMessage = itemView.findViewById(R.id.txtmessage);
            txtTimestamp = itemView.findViewById(R.id.txttimestamp);
            txtUnread = itemView.findViewById(R.id.txtUnread);
        }
    }
}