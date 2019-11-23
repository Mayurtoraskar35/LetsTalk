package com.example.letstalk.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.letstalk.Adapter.MyChatAdapter;
import com.example.letstalk.Database.DatabaseHandler;
import com.example.letstalk.LTModel.Chat;
import com.example.letstalk.MessageActivity;
import com.example.letstalk.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    private static final String TAG = "ChatFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView mRecyclerView;

    Context mContext;

    List<Chat> mychatlist;



    Boolean isExists;

    int indexToremove;

    MyChatAdapter chatAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        mContext = container.getContext();

        mRecyclerView = view.findViewById(R.id.chat_recyclerview);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

        DatabaseHandler databaseHandler = new DatabaseHandler(mContext);

        mychatlist=new ArrayList<>(  );
        mychatlist=databaseHandler.getChatList();

        Log.d( TAG, "list: "+mychatlist);

        Log.d(TAG, "list" + databaseHandler.getChatList());

        //ChatAdapter mchatAdapter = new ChatAdapter(mcontext, databaseHandler.getChatList());
        //Log.d(TAG, " data : "+mchatAdapter);

        //mrecyclerView.setAdapter(mchatAdapter);//
        //Log.d(TAG, "onCreateView: 123456"+mrecyclerView);


        //Broadcast
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MessageActivity.MYFILTER);
        mContext.registerReceiver(broadcastReceiver,intentFilter);

        chatAdapter=new MyChatAdapter(mContext,mychatlist);

        mRecyclerView.setAdapter(chatAdapter);
        Log.d(TAG, "Chat Fragnment onCreateView: "+databaseHandler.getChatList());
        return view;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String ConversionID = bundle.getString( "conversionId" );
            String MessageID = bundle.getString( "messageId" );
            Log.d(TAG, "messageId12: "+MessageID);
            Log.d( TAG, "conversion12: " + ConversionID );
            Receiver( ConversionID, MessageID );
        }
    };

    public void Receiver(String ConversionID, String MessageID) {
        DatabaseHandler databaseHelper=new DatabaseHandler( mContext );

        Chat currentChat = databaseHelper.getChatByConversationId(  ConversionID);

        for (Chat chat : mychatlist) {

            if (chat.message.getConversionId().equals( ConversionID )) {
                isExists = true;
                indexToremove = mychatlist.indexOf( chat );
                Log.d( TAG, "Receiver153: " + indexToremove );
                break;
            }

        }

        Log.d( TAG, "Final Indext to update: " + indexToremove );
        chatAdapter.updateChatList( currentChat, indexToremove , isExists );

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContext.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
