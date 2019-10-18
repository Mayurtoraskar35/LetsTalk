package com.example.letstalk.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

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

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate: DatabaseHandler");
        String createContactQuery="create table "+Users.TABLE_NAME+" ( "+
                Users.ID+" integer primary key AUTOINCREMENT, "+
                Users.USER_NAME+" text, "+
                Users.USER_SUR_NAME +" text, "+
                Users.USER_MOBILE+" text, "+
                Users.USER_ID +" text);";
        sqLiteDatabase.execSQL(createContactQuery);

        String createMessageQuery="create table "+ Messages.TABLE_NAME+" ( "+
                Messages.ID+" BigInt(20), "+
                Messages.SENDER_ID +" text, "+
                Messages.CONVERSION_ID +" text, "+
                Messages.MESSAGE_ID +" text, "+
                Messages.BODY +" text, "+
                Messages.TIME_STAMP+" text);";
        sqLiteDatabase.execSQL(createMessageQuery);
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


        Intent intent = new Intent();
        intent.setAction(MessageActivity.MYFILTER);
        intent.putExtra("messageId",messageId);
        context.sendBroadcast(intent);
    }

    public List<String> displayUserID()
    {
        String id;
        List<String> userID = new ArrayList<>();
        SQLiteDatabase database= this.getReadableDatabase();
        String query="SELECT * FROM "+Users.TABLE_NAME;
        Cursor cursor=database.rawQuery(query,null);
        Log.d(TAG,"Cursor Count: "+cursor.getCount());
        while (cursor.moveToNext())
        {
            id = cursor.getString(cursor.getColumnIndex(Users.USER_ID));
            userID.add(id);
        }
        cursor.close();
        return userID;
    }

    public List<UserContact> displayUserContact()
    {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + Users.TABLE_NAME+";";
        Cursor cursor = database.rawQuery(query, null);
        Log.d(TAG, "Cursor Count : " + cursor.getCount());
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
        Log.d(TAG,"Inside insertStudent() -> Row : "+row);
    }


    public Message getMessageById(String messageId) {

        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + Messages.TABLE_NAME+" WHERE "+ Messages.MESSAGE_ID +" = '"+messageId+"' ;";
        Cursor cursor = database.rawQuery(query, null);
        Log.d(TAG, "Cursor Count : " + cursor.getCount());
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
}
