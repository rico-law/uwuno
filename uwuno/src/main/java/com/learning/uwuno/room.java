package com.learning.uwuno;

import java.util.ArrayList;
import java.util.UUID;

public class room {
    // Class Variables
    final private String uid;
    private String roomName;
    private ArrayList<player> playerList = new ArrayList<player>();

    // Class functions
    public room(String roomName) {
        this.uid = UUID.randomUUID().toString();
        this.roomName = roomName;
    }

    public String getName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getUid() {
        return uid;
    }

    public boolean addPlayer(player newPlayer) {
        return playerList.add(newPlayer);
    }

    public ArrayList<player> getPlayers() {
        return playerList;
    }

    public player getPlayer(String pid) {
        return playerList.stream().filter(t -> t.getUid().equals(pid)).findFirst().get();
    }

    public void deletePlayer(String pid) {
        playerList.removeIf(t -> t.getUid().equals(pid));
    }
}