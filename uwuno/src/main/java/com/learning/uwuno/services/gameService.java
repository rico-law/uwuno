package com.learning.uwuno.services;

import com.learning.uwuno.cards.*;
import com.learning.uwuno.errors.*;
import com.learning.uwuno.player;
import com.learning.uwuno.room;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@Service
public class gameService {
    // Singleton for holding anything we need everywhere, should handle all business logic

    // Variables
    private final ArrayList<room> roomList = new ArrayList<>();

    // Class Functions
    // POSTS
    public room addRoom(String roomName, boolean useBlankCards) {
        if (roomName.isBlank())
            throw new badRequest("Room name cannot be blank");
        room newRoom = new room(roomName, useBlankCards);
        roomList.add(newRoom);
        return newRoom;
    }

    public player addPlayer(String name, String uid) {
        if (name.isBlank())
            throw new badRequest("Player name cannot be blank");
        player newPlayer = new player(name);
        getRoom(uid).addPlayer(newPlayer);
        return newPlayer;
    }

    // GETS
    public ArrayList<room> getRoomList() {
        return roomList;
    }

    public room getRoom(String uid) {
        return roomList.stream().filter(t -> t.getUid().equals(uid)).findFirst()
                .orElseThrow(() -> new errorNotFound("Room ID could not be found"));
    }

    public player getPlayer(String uid, String pid) {
        try {
            return getRoom(uid).getPlayer(pid);
        }
        catch (NoSuchElementException e) {
            throw new errorNotFound("Player ID could not be found");
        }
    }

    // PUTS
    public void updateRoomName(String uid, String newName) {
        if (newName.isBlank())
            throw new badRequest("Room name cannot be blank");
        getRoom(uid).setRoomName(newName);
    }

    public void updateRoomStatus(String uid, String status) {
        if (status.isBlank())
            throw new badRequest("Status cannot be blank");
        room room = getRoom(uid);
        room.Status roomStatus = serviceUtils.stringToRoomState(status);

        if (validStatusChange(room, roomStatus)) {
            room.setRoomStatus(roomStatus);
            serviceUtils.setUpGameState(room, roomStatus);
        }
        else {
            // TODO: Figure out if this
            throw new badRequest("Failed to change room status");
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
        throw new internalServerError("Room Status should never be " + status.toString()); // Should not reach here
    }

    public player updatePlayerName(String uid, String pid, String newName) {
        if (newName.isBlank())
            throw new badRequest("Player name cannot be blank");
        player player = getPlayer(uid, pid);
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
        // Ensure card information passed in is viable and does not contain any extra information
        if (color.equals(card.Color.Black.toString()) && setWildColor.isBlank() ||
            color.isBlank() && !setWildColor.isBlank() ||
            value.isBlank() && setWildColor.isBlank() ||
            !color.equals(card.Color.Black.toString()) && !setWildColor.isBlank()) {
            throw new badRequest("Card cannot be created");
        }

        // Create a reference card for comparison with given parameters
        // and ensure the card is compatible with the last played card
        card toPlay = serviceUtils.inputToCard(type, color, value);
        if(!serviceUtils.checkPlayable(toPlay, getRoom(uid).lastPlayedCard()))
            throw new badRequest("Card cannot be played");

        // Add the player's card to discard pile
        player player = getPlayer(uid, pid);
        if (!player.playCard(toPlay))
            throw new internalServerError("Card to play does not exist in player's hand");
        return player;
    }

    // DELETES
    public void deleteRoom(String uid) {
        if (roomList.isEmpty())
            throw new badRequest("No Rooms to delete");
        else if (!roomList.removeIf(t -> t.getUid().equals(uid)))
            throw new errorNotFound("Room ID could not be found");
    }

    public void deletePlayer(String uid, String pid) {
        if (!getRoom(uid).deletePlayer(pid))
            throw new errorNotFound("Player ID could not be found");
    }
}
