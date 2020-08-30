package com.learning.uwuno;

import java.util.Vector;

public class roomContainer {
    public roomContainer() {
        originalRoomContainer = this;
        roomList = new Vector<room>();
    }

    // Variables
    private Vector<room> roomList;
    static roomContainer originalRoomContainer;

    // Class Functions
    // So that we can grab the original instance of roomContainer anywhere unless it's unloaded
    static public roomContainer getInstance() {
        if (originalRoomContainer != null)
            return originalRoomContainer;
        return new roomContainer();
    }

    // Creates uid for a room
    // TODO: make it check if uid already exists later
    private int createUid() {
        return roomList.size();
    }

    public boolean addRoom(String roomName) {
        return roomList.add(new room(createUid(), roomName));
    }

    public room getRoom(int uid) {
        return roomList.get(0);
    }

    public Vector<room> getRoomList() {
        return roomList;
    }

}
