package com.example.letstalk.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.letstalk.LTModel.Chat;
import com.example.letstalk.LTModel.Message;
import com.example.letstalk.LTModel.UserContact;
import com.example.letstalk.MessageActivity;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="letstalk.db";

    private static final String TAG="DatabaseHandler";

    public static final int DATABASE_VERSION=1;

    String messageId;

    Context context;

    public DatabaseHandler( Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context=context;
    }

    public static class Users{
        public static final String TABLE_NAME="contact";
        public static final String ID="id";
        public static final String USER_NAME="user_name";
        public static final String USER_SUR_NAME="user_sur_name";
        public static final String USER_MOBILE="user_mobile";
        public static final String USER_ID="user_id";
    }

    public static class Messages {
        public static final String TABLE_NAME = "message";
        public static final String ID = "id";
        public static final String SENDER_ID ="sender_id";
        public static final String CONVERSION_ID = "conversion_id";
        public static final String MESSAGE_ID = "message_id";
        public static final String BODY = "body";
        public static final String TIME_STAMP = "time_stamp";
    }

    public static class Chats {
        public static final String TABLE_NAME = "chat";
        public static final String CHATID = "chatid";
        public static final String CONVERSION_ID = "conversion_id";
        public static final String MESSAGE_ID = "message_id";
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate: DatabaseHandler");
        String createContactQuery="create table "+Users.TABLE_NAME+" ( "+
                Users.ID+" integer primary key AUTOINCREMENT, "+
                Users.USER_NAME+" text, "+
                Users.USER_SUR_NAME +" text, "+
                Users.USER_MOBILE+" text UNIQUE, "+
                Users.USER_ID +" text );";
        sqLiteDatabase.execSQL(createContactQuery);

        String createMessageQuery="create table "+ Messages.TABLE_NAME+" ( "+
                Messages.ID+" integer primary key AUTOINCREMENT, "+
                Messages.SENDER_ID +" text, "+
                Messages.CONVERSION_ID +" text, "+
                Messages.MESSAGE_ID +" text, "+
                Messages.BODY +" text, "+
                Messages.TIME_STAMP+" text);";
        sqLiteDatabase.execSQL(createMessageQuery);

        String createChatQuery="create table "+ Chats.TABLE_NAME+" ( "+
                Chats.CHATID+" integer primary key AUTOINCREMENT, "+
                Chats.CONVERSION_ID +" text UNIQUE, "+
                Chats.MESSAGE_ID +" text, " +
                "FOREIGN KEY(" + Chats.MESSAGE_ID +") REFERENCES "+Messages.TABLE_NAME+"("+Messages.MESSAGE_ID+"));";
        sqLiteDatabase.execSQL(createChatQuery);
    }

    public void insertMessage(Message message)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();

        messageId = message.getMessageId().toString();

        Log.d(TAG, "insertMessage: msgID"+messageId);

        contentValues.put(Messages.MESSAGE_ID,message.getMessageId());
        contentValues.put(Messages.CONVERSION_ID,message.getConversionId());
        contentValues.put(Messages.BODY,message.getBody());
        contentValues.put(Messages.TIME_STAMP,message.getTimeStamp());
        contentValues.put(Messages.SENDER_ID,message.getSenderId());
        long row= database.insertWithOnConflict(Messages.TABLE_NAME,null,contentValues,SQLiteDatabase.CONFLICT_IGNORE);
        Log.d(TAG,"Inside insertStudent() -> Row : "+row);

        Chat chat = new Chat();
        chat.unreadCount=0;
        chat.message = message;
        insertChat(chat);

        Intent intent = new Intent();
        intent.setAction(MessageActivity.MYFILTER);
        intent.putExtra("messageId",messageId);
        context.sendBroadcast(intent);



    }

    public void insertChat(Chat chat)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(Chats.CONVERSION_ID,chat.message.getConversionId());
        contentValues.put(Chats.MESSAGE_ID,chat.message.getMessageId());
        long row= database.insertWithOnConflict(Chats.TABLE_NAME,null,contentValues,SQLiteDatabase.CONFLICT_IGNORE);
        Log.d(TAG,"Inside insertChat() -> Row : "+row);
    }

    public List<UserContact> displayUserContact()
    {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + Users.TABLE_NAME+";";
        Cursor cursor = database.rawQuery(query, null);
        Log.d(TAG, "Cursor Count contact : " + cursor.getCount());
        List<UserContact> userContactList = new ArrayList<>();

        while (cursor.moveToNext()) {
            UserContact user = new UserContact();
            Log.d(TAG, "getStudentData: ");
            String id = cursor.getString(cursor.getColumnIndex(Users.USER_ID));
            String userName = cursor.getString(cursor.getColumnIndex(Users.USER_NAME));
            String userSurName = cursor.getString(cursor.getColumnIndex(Users.USER_SUR_NAME));
            String userMobile = cursor.getString(cursor.getColumnIndex(Users.USER_MOBILE));
            user.setUserId(id);
            user.setUserName(userName);
            user.setUserSurname(userSurName);
            user.setUserMobile(userMobile);
            userContactList.add(user);
        }
        Log.d(TAG, "studentlist: "+userContactList);
        cursor.close();
        return userContactList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    public void insertUser(UserContact userContact)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put(Users.USER_NAME,userContact.getUserName());
        contentValues.put(Users.USER_SUR_NAME,userContact.getUserSurname());
        contentValues.put(Users.USER_MOBILE,userContact.getUserMobile());
        contentValues.put(Users.USER_ID,userContact.getUserId());
        long row= database.insertWithOnConflict(Users.TABLE_NAME,null,contentValues,SQLiteDatabase.CONFLICT_IGNORE);
        Log.d(TAG,"Inside insertContact() -> Row : "+row);
    }

    public Message getMessageById(String messageId) {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + Messages.TABLE_NAME+" WHERE "+ Messages.MESSAGE_ID +" = '"+messageId+"' ;";
        Cursor cursor = database.rawQuery(query, null);
        Log.d(TAG, "Cursor Count : msg " + cursor.getCount());
        Message message = new Message();

        while (cursor.moveToNext()) {
            Log.d(TAG, "getStudentData: ");
            String senderId = cursor.getString(cursor.getColumnIndex(Messages.SENDER_ID));
            String conversionId = cursor.getString(cursor.getColumnIndex(Messages.CONVERSION_ID));
            String messageID = cursor.getString(cursor.getColumnIndex(Messages.MESSAGE_ID));
            String body = cursor.getString(cursor.getColumnIndex(Messages.BODY));
            String timeStamp =cursor.getString(cursor.getColumnIndex(Messages.TIME_STAMP));
            message.setSenderId(senderId);
            message.setConversionId(conversionId);
            message.setMessageId(messageID);
            message.setBody(body);
            message.setTimeStamp(timeStamp);
        }
        cursor.close();
        return message;
    }

    public List<Message> getMessageData(String conversationID) {

        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + Messages.TABLE_NAME+" WHERE "+ Messages.CONVERSION_ID +" = '"+conversationID+"' ;";
        Cursor cursor = database.rawQuery(query, null);
        Log.d(TAG, "Cursor Count message : " + cursor.getCount());
        List<Message> messagelist= new ArrayList<>();

        while (cursor.moveToNext()) {
            Message message = new Message();
            Log.d(TAG, "getStudentData: ");
            String senderId = cursor.getString(cursor.getColumnIndex(Messages.SENDER_ID));
            String conversionId = cursor.getString(cursor.getColumnIndex(Messages.CONVERSION_ID));
            String messageID = cursor.getString(cursor.getColumnIndex(Messages.MESSAGE_ID));
            String body = cursor.getString(cursor.getColumnIndex(Messages.BODY));
            String timeStamp =cursor.getString(cursor.getColumnIndex(Messages.TIME_STAMP));
            message.setSenderId(senderId);
            message.setConversionId(conversionId);
            message.setMessageId(messageID);
            message.setBody(body);
            message.setTimeStamp(timeStamp);
            messagelist.add(message);
            Log.d(TAG, "getMessageData: Msg data "+body);
            Log.d(TAG, "getMessageData: messagelist"+ messagelist.toString());
        }
        cursor.close();
        return messagelist;
    }

    public List<Chat> getChatList()
    {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + Chats.TABLE_NAME+
                " JOIN "+ Users.TABLE_NAME +
                " ON "+ Users.USER_ID +" = "+Chats.TABLE_NAME + "." + Chats.CONVERSION_ID +
                " JOIN "+ Messages.TABLE_NAME +
                " ON "+Messages.TABLE_NAME + "." + Messages.MESSAGE_ID +" = "+ Chats.TABLE_NAME + "." + Chats.MESSAGE_ID
                +" ;";
        Log.d(TAG, "getChatList: Query"+query);
        Cursor cursor = database.rawQuery(query, null);
        Log.d(TAG, "Cursor Count chat : " + cursor.getCount());
        List<Chat> chatList= new ArrayList<>();

        while (cursor.moveToNext()) {
            Chat chat = new Chat();
            Log.d(TAG, "getStudentData: ");
            String chatId = cursor.getString(cursor.getColumnIndex(Chats.CHATID));
            String conversionId = cursor.getString(cursor.getColumnIndex(Chats.CONVERSION_ID));
            String messageID = cursor.getString(cursor.getColumnIndex(Chats.MESSAGE_ID));
            chat.setChatId(Integer.parseInt(chatId));
            chat.message.setConversionId(conversionId);
            chat.message.setMessageId(messageID);
            Log.d(TAG, "getChatData: Chat ID "+chatId);
            Log.d(TAG, "getChatData: message ID"+ messageID);
            Log.d(TAG, "getChatData: conversation ID"+ conversionId);
            chatList.add(chat);
        }
        cursor.close();
        return chatList;
    }
}