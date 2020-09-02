package com.learning.uwuno;

import java.util.ArrayList;

public class room {
    // Class Variables
    final private int uid;
    private String roomName;
    private ArrayList<player> playerList = new ArrayList<player>();

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

    public void addPlayer(player newPlayer) {
        playerList.add(newPlayer);
    }

    public ArrayList<player> getPlayers() {
        return playerList;
    }

    public player getPlayer(int pid) {
        return playerList.stream().filter(t -> t.getUid() == pid).findFirst().get();
    }

    public void deletePlayer(int pid) {
        playerList.removeIf(t -> t.getUid() == pid);
    }
}
