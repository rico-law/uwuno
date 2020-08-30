package com.learning.uwuno;

import java.util.List;

public class room {
    public room(int id, String roomName) {
        this.uid = id;
        this.roomName = roomName;
    }

    // Class Variables
    final private int uid;
    private String roomName;
    private List<player> playerList;

    // Class functions

    public String getName() {
        return roomName;
    }
}
