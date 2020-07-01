package com.example.iwimapplication.Modal;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

public class Notification {
    private String Id_sender;
    private Timestamp Time;
    private String Text;
    private String Name_sender;
    private String NotificationID;
    private boolean Is_read;

    public String getNotificationID() {
        return NotificationID;
    }

    public void setNotificationID(String notificationID) {
        NotificationID = notificationID;
    }

    public Notification(String id_sender, Timestamp time, String text, String name_sender, String notificationID, boolean is_read) {
        Id_sender = id_sender;
        Time = time;
        Text = text;
        Name_sender = name_sender;
        NotificationID = notificationID;
        Is_read = is_read;
    }

    public String getName_sender() {
        return Name_sender;
    }

    public void setName_sender(String name_sender) {
        Name_sender = name_sender;
    }

    public String getId_sender() {
        return Id_sender;
    }

    public void setId_sender(String id_sender) {
        Id_sender = id_sender;
    }

    public Timestamp getTime() {
        return Time;
    }

    public void setTime(Timestamp time) {
        Time = time;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public boolean isIs_read() {
        return Is_read;
    }

    public void setIs_read(boolean is_read) {
        Is_read = is_read;
    }
    public Notification(){

    }

}
