package com.learning.uwuno.services;
import com.learning.uwuno.room;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@Service
public class roomContainerService {
    // Singleton for holding anything we need everywhere

    // Variables
    private ArrayList<room> roomList = new ArrayList<room>();

    // Only here for testing purposes TODO: Remove this after api for setting up new room is done
    boolean test = addRoom("1");
    boolean test1 = addRoom("2");
    boolean test2 = addRoom("3");


    // Class Functions
    // TODO: make it check if uid already exists later + Change/Encrypt it
    public int createUid() {
        return roomList.size();
    }

    public boolean addRoom(String roomName) {
        return roomList.add(new room(createUid(), roomName));
    }

    public boolean addRoom(room newRoom) {
        return roomList.add(newRoom);
    }

    public ArrayList<room> getRoomList() {
        return roomList;
    }

    // Callees must handle NoSuchElementException
    public room getRoom(int uid) {
        return roomList.stream().filter(t -> t.getUid() == uid).findFirst().get();
    }

    public void updateRoomName(int uid, String newName) {
        roomList.stream().filter(t -> t.getUid() == uid).findFirst().get().setRoomName(newName);
    }

    public void deleteRoom(int uid) {
        roomList.removeIf(t -> t.getUid() == uid);
    }
}
