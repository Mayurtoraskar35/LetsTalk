package com.example.letstalk.LTModel;

public class User {

    private static final String TAG = "User";

    String userId;
    String userName;
    String userSurname;
    String userMobile;

    public User(){

    }

    public User(String userId, String userName, String userSurname, String userMobile) {
        this.userId = userId;
        this.userName = userName;
        this.userSurname = userSurname;
        this.userMobile = userMobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }



}
