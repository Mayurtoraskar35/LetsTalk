package com.example.letstalk.LTModel;

public class Chat {

    public int chatId;

    public int unreadCount;

    public Message message;

    public User userContact;

    public User getUserContact() {
        return userContact;
    }

    public void setUserContact(User userContact) {
        this.userContact = userContact;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
