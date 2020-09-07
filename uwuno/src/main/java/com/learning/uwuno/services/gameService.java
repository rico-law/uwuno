package com.learning.uwuno.services;

import com.learning.uwuno.cards.*;
import com.learning.uwuno.errors.badRequest;
import com.learning.uwuno.errors.errorNotFound;
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
    public room addRoom(String roomName, boolean useBlankCards) {
        if (roomName.isEmpty()) throw new badRequest();
        room newRoom = new room(roomName, useBlankCards);
        roomList.add(newRoom);
        return newRoom;
    }

    public String addRoom(room newRoom) {
        roomList.add(newRoom);
        return newRoom.getUid();
    }

    public player addPlayer(String name, String uid) {
        if (name.isEmpty()) throw new badRequest();
        player newPlayer = new player(name);
        getRoom(uid).addPlayer(newPlayer);
        return newPlayer;
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
    public room updateRoomName(String uid, String newName) {
        if (newName.isEmpty()) throw new badRequest();
        room room = roomList.stream().filter(t -> t.getUid().equals(uid)).findFirst().get();
        room.setRoomName(newName);
        return room;
    }

    public player updatePlayerName(String uid, String pid, String newName) {
        if (newName.isEmpty()) throw new badRequest();
        player player = getRoom(uid).getPlayer(pid);
        player.setName(newName);
        return player;
    }

    public player drawCards(String uid, String pid, int numCards) {
        player player = getPlayer(uid, pid);
        player.drawCards(numCards);
        return player;
    }

    // Should handle both taking card away from player and adding it back into deck
    // type = cardType, color = cardColor, value = number on card, setWildColor = color to set wild card to
    public void playCard(String uid, String pid, String type, String color, String value, String setWildColor) {
        card toPlay = utils.inputToCard(type, color, value);
        if(!utils.checkPlayable(toPlay, getRoom(uid).lastPlayedCard()))
            throw new badRequest();
        getPlayer(uid, pid).playCard(toPlay);
    }

    // DELETES
    public void deleteRoom(String uid) {
        if (!roomList.removeIf(t -> t.getUid().equals(uid))) throw new errorNotFound();
    }

    public void deletePlayer(String uid, String pid) {
        getRoom(uid).deletePlayer(pid);
    }

}
