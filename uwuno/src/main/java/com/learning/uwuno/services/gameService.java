package com.learning.uwuno.services;

import com.learning.uwuno.cards.*;
import com.learning.uwuno.errors.badRequest;
import com.learning.uwuno.errors.errorNotFound;
import com.learning.uwuno.game.gameLogic;
import com.learning.uwuno.game.gameResponse;
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
        room newRoom = new room(roomName, useBlankCards, serviceUtils.createUID(roomList));
        roomList.add(newRoom);
        return newRoom;
    }

    public player addPlayer(String name, String uid) {
        if (name.isBlank())
            throw new badRequest("Player name cannot be blank");
        player newPlayer = getRoom(uid).addPlayer(new player(name));
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

        if (serviceUtils.validStatusChange(room, roomStatus)) {
            room.setRoomStatus(roomStatus);
            serviceUtils.setUpGameState(room, roomStatus);
        }
        else {
            throw new badRequest("Failed to change room status");
        }
    }

    public player updatePlayerName(String uid, String pid, String newName) {
        if (newName.isBlank())
            throw new badRequest("Player name cannot be blank");
        return getRoom(uid).updatePlayerName(pid, newName);
    }

    public player drawCards(String uid, String pid, int numCards) {
        player player = getPlayer(uid, pid);
        player.drawCards(numCards);
        return player;
    }

    // Should handle both taking card away from player and adding it back into deck
    // type = cardType, color = cardColor, value = number on card, setWildColor = color to set wild card to
    public gameResponse takeTurn(String uid, String pid, String type, String color,
                           String value, String setWildColor, String skip) {
        player player = getPlayer(uid, pid);
        room room = getRoom(uid);
        gameResponse response = new gameResponse();
        // Check whether player is skipping turn
        if (Boolean.parseBoolean(skip)) {
            if (type.isBlank() && color.isBlank() && value.isBlank() && setWildColor.isBlank()) {
                // If player can play drawn card, return response with same pid and the drawn card
                // If player cannot play drawn card, return response with next player's pid and their playable cards
                gameLogic.skipTurn(player, room, response);
            } else {
                throw new badRequest("Invalid skip turn request");
            }
        } else {
            // Create a reference card for comparison with given parameters
            // and ensure the card is compatible with the last played card
            card toPlay = serviceUtils.generateCard(type, color, value, setWildColor);
            if (gameLogic.playCard(toPlay, room.lastPlayedCard(), player))
                // If played card is valid, return response with next player's pid and their playable cards
                // Or declare the winner
                gameLogic.endTurn(player, room, response);
            else {
                // TODO: somehow return gameResponse without modifying it (since no card has been played,
                //  the properties should be the same as before)
                // If played an invalid card, return response with same pid and their playable cards
                response.setPlayerTurnResponse(player.getPid(),
                        gameLogic.getPlayableCards(player, room.lastPlayedCard()));
            }
        }
        return response;
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