package com.learning.uwuno.services;
import com.learning.uwuno.room;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class roomContainerService {
    // Singleton for holding anything we need everywhere

    // Variables
    private ArrayList<room> roomList = new ArrayList<room>();

    // Class Functions
    public boolean addRoom(String roomName) {
        return roomList.add(new room(roomName));
    }

    public boolean addRoom(room newRoom) {
        return roomList.add(newRoom);
    }

    public ArrayList<room> getRoomList() {
        return roomList;
    }

    // Callees must handle NoSuchElementException
    public room getRoom(String uid) {
        return roomList.stream().filter(t -> t.getUid().equals(uid)).findFirst().get();
    }

    public void updateRoomName(String uid, String newName) {
        roomList.stream().filter(t -> t.getUid().equals(uid)).findFirst().get().setRoomName(newName);
    }

    public void deleteRoom(String uid) {
        roomList.removeIf(t -> t.getUid().equals(uid));
    }
}
