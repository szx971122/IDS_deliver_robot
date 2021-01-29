package com.example.myapplication;

public class UserData {
    private String depRoom;
    private String destRoom;

    public String getDepRoom() {
        return depRoom;
    }

    public String getDestRoom() {
        return destRoom;
    }
    public UserData(String depRoom, String destRoom){
        super();
        this.depRoom=depRoom;
        this.destRoom=destRoom;
    }

}
