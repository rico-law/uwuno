package com.learning.uwuno.services;

import com.learning.uwuno.cards.*;
import com.learning.uwuno.errors.badRequest;
import com.learning.uwuno.player;
import com.learning.uwuno.room;
import com.learning.uwuno.util.utils;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class gameService {
    // Singleton for holding anything we need everywhere, should handle all business logic

    // Variables
    private ArrayList<room> roomList = new ArrayList<room>();

    // Class Functions
    // POSTS
    public String addRoom(String roomName, boolean useBlankCards) {
        room newRoom = new room(roomName, useBlankCards);
        roomList.add(newRoom);
        return newRoom.getUid();
    }

    public String addRoom(room newRoom) {
        roomList.add(newRoom);
        return newRoom.getUid();
    }

    public String addPlayer(String name, String uid) {
        player newPlayer = new player(name);
        getRoom(uid).addPlayer(newPlayer);
        return newPlayer.getPid();
    }

    // GETS
    public ArrayList<room> getRoomList() {
        return roomList;
    }

    // Callees must handle NoSuchElementException
    public room getRoom(String uid) {
        return roomList.stream().filter(t -> t.getUid().equals(uid)).findFirst().get();
    }

    public player getPlayer(String uid, String pid) {
        return getRoom(uid).getPlayer(pid);
    }

    // PUTS
    public void updateRoomName(String uid, String newName) {
        roomList.stream().filter(t -> t.getUid().equals(uid)).findFirst().get().setRoomName(newName);
    }

    public void updatePlayerName(String uid, String pid, String newName) {
        getRoom(uid).getPlayer(pid).setName(newName);
    }

    public ArrayList<card> drawCards(String uid, String pid, int numCards) {
        room room = getRoom(uid);
        ArrayList<card> drawnCards = room.drawCards(numCards);
        room.getPlayer(pid).addCards(drawnCards);
        return drawnCards;
    }

    // Should handle both taking card away from player and adding it back into deck
    public void playCard(String uid, String pid, String type, String color, String value) {
        card toPlay = utils.inputToCard(type, color, value);
        if(!utils.checkPlayable(toPlay, getRoom(uid).lastPlayedCard()))
            throw new badRequest();
        room curRoom = getRoom(uid);
        curRoom.getPlayer(pid).removeCard(toPlay);
        curRoom.addToDiscard(toPlay);
    }

    // DELETES
    public void deleteRoom(String uid) {
        roomList.removeIf(t -> t.getUid().equals(uid));
    }

}
