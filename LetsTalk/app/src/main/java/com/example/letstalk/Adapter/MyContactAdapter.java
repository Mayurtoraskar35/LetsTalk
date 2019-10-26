package com.example.letstalk.Adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
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
import com.example.letstalk.LTModel.User;
import com.example.letstalk.LTModel.UserContact;
import com.example.letstalk.MessageActivity;
import com.example.letstalk.R;

import java.util.List;

public class MyContactAdapter extends RecyclerView.Adapter<MyContactAdapter.MyViewHolder>{

    private static final String TAG = "MyContactAdapter";

    private List<UserContact> mUserList;

    private Context mContext;

    UserContact user;

    public MyContactAdapter(Context context, List<UserContact> userList){
        mUserList =userList;
        Log.d(TAG, "MyContactAdapter: "+mUserList);
        mContext = context;
        Log.d(TAG, "MyContactAdapter: "+mContext);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(mContext).inflate(R.layout.contact_cardview, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        //Log.d(TAG, "onBindViewHolder: ");

        user = mUserList.get(position);

        String name = user.getUserName().toString().trim().substring(0,1).toUpperCase() + user.getUserName().toString().trim().substring(1).toLowerCase();

        //Log.d(TAG, "onBindViewHolder: name "+name);

        String surname = user.getUserSurname().toString().trim().substring(0,1).toUpperCase() + user.getUserSurname().toString().trim().substring(1).toLowerCase();

        //Log.d(TAG, "onBindViewHolder: surname "+surname);

        String fullName = name + " " + surname;

        String mobile = user.getUserMobile().toString();

        String uID = user.getUserId().toString();

        String s =fullName.substring(0,1);

        //Log.d(TAG, "onBindViewHolder: s = "+s);

        myViewHolder.txtUserName.setText(fullName);
        myViewHolder.txtUserContact.setText(mobile);

        ColorGenerator generator = ColorGenerator.DEFAULT;

        int color = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder().buildRound(s, color);
        myViewHolder.image.setImageDrawable(drawable);

        String umob = user.getUserMobile().toString();

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick: ReciverUserId "+user.getUserId());
                Log.d(TAG, "onClick: ReciverUserId "+uID);

                Log.d(TAG, "onClick: number "+user.getUserMobile());
                Log.d(TAG, "onClick: number "+mobile);

                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("ReciverUserID",uID);
                intent.putExtra("number",mobile);
                intent.putExtra("name",fullName);
                mContext.startActivity(intent);
            }
        });
    }

    public void setContactList(List<UserContact> contactList)
    {
        if(mUserList != null)
            mUserList.clear();

        mUserList = contactList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtUserName;
        TextView txtUserContact;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "MyViewHolder: ");
            txtUserName = itemView.findViewById(R.id.username);
            txtUserContact = itemView.findViewById(R.id.contact_number);
            image = itemView.findViewById(R.id.user_image);
        }
    }
}
