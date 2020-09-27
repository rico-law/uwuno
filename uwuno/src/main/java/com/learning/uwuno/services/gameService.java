package com.learning.uwuno.services;

import com.learning.uwuno.cards.*;
import com.learning.uwuno.errors.badRequest;
import com.learning.uwuno.errors.errorNotFound;
import com.learning.uwuno.errors.internalServerError;
import com.learning.uwuno.player;
import com.learning.uwuno.room;
import com.learning.uwuno.util.utils;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;

@Service
public class gameService {
    // Singleton for holding anything we need everywhere, should handle all business logic

    // Variables
    private ArrayList<room> roomList = new ArrayList<room>();

    // Class Functions
    // POSTS
    public room addRoom(String roomName, boolean useBlankCards) {
        if (roomName.isBlank())
            throw new badRequest();
        room newRoom = new room(roomName, useBlankCards);
        roomList.add(newRoom);
        return newRoom;
    }

    public player addPlayer(String name, String uid) {
        if (name.isBlank())
            throw new badRequest();
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
    public void updateRoomName(String uid, String newName) {
        if (newName.isBlank())
            throw new badRequest();
        room room = roomList.stream().filter(t -> t.getUid().equals(uid)).findFirst().get();
        room.setRoomName(newName);
    }

    public void updateRoomStatus(String uid, String status) {
        if (status.isBlank())
            throw new badRequest();
        room room = roomList.stream().filter(t -> t.getUid().equals(uid)).findFirst().get();
        room.Status roomStatus = utils.stringToRoomState(status);

        if (validStatusChange(room, roomStatus)) {
            room.setRoomStatus(roomStatus);
            setUpGameState(room, roomStatus);
        } else {
            throw new badRequest();
        }
    }

    // TODO: Only has Lobby -> Start check. May need to add other states as necessary.
    public boolean validStatusChange(room room, room.Status status) {
        switch (status) {
            case Lobby -> {
                return true;
            }
            case Start -> {
                return room.getPlayers().size() >= room.getMinPlayers() &&
                        room.getPlayers().size() <= room.getMaxPlayers();
            }
            case End -> {
                return false;
            }
        }
        throw new internalServerError(); // Should not reach here
    }

    // TODO: Only Start state. May need to add other states as necessary.
    public void setUpGameState(room room, room.Status status) {
        switch (status) {
            case Lobby -> {
                // Restart game
                return;
            }
            case Start -> {
                setUpStartGame(room);
                return;
            }
            case End -> {
                return;
            }
        }
        throw new badRequest();
    }

    // Is helper for setUpGameState. Should use through there.
    public void setUpStartGame(room room) {
        room.shufflePlayers();
        room.setupDeck();
        LinkedList<player> playerList = room.getPlayers();
        for (player player:playerList) {
            player.drawCards(room.getMaxHandSize());
        }
        room.flipTopCard();
    }

    public player updatePlayerName(String uid, String pid, String newName) {
        if (newName.isBlank())
            throw new badRequest();
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
    public player playCard(String uid, String pid, String type, String color, String value, String setWildColor) {
        if (color.equals(card.Color.Black.toString()) && setWildColor.isBlank() ||
            color.isBlank() && !setWildColor.isBlank() ||
            !color.equals(card.Color.Black.toString()) && !setWildColor.isBlank()) {
            throw new badRequest();
        }
        card toPlay = utils.inputToCard(type, color, value);
        if(!utils.checkPlayable(toPlay, getRoom(uid).lastPlayedCard()))
            throw new badRequest();
        player player = getPlayer(uid, pid);
        player.playCard(toPlay);
        return player;
    }

    // DELETES
    public void deleteRoom(String uid) {
        if (roomList.isEmpty())
            throw new badRequest();
        else if (!roomList.removeIf(t -> t.getUid().equals(uid)))
            throw new errorNotFound();
    }

    public void deletePlayer(String uid, String pid) {
        getRoom(uid).deletePlayer(pid);
    }

}
