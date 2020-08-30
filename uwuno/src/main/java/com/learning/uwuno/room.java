package com.learning.uwuno;

import java.util.List;

public class room {
    // Class Variables
    final private int uid;
    private String roomName;
    private List<player> playerList;

    // Class functions
    public room(int id, String roomName) {
        this.uid = id;
        this.roomName = roomName;
    }

    public String getName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getUid() {
        return uid;
    }


}
