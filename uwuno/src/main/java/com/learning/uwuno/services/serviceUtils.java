package com.learning.uwuno.services;

import com.learning.uwuno.cards.basicCard;
import com.learning.uwuno.cards.card;
import com.learning.uwuno.cards.sColorCard;
import com.learning.uwuno.cards.wildCard;
import com.learning.uwuno.errors.badRequest;
import com.learning.uwuno.errors.internalServerError;
import com.learning.uwuno.room;

import java.util.ArrayList;
import java.util.UUID;

public final class serviceUtils {
    private serviceUtils() {
    }

    static public String createUID(ArrayList<room> roomList) {
        String uid = UUID.randomUUID().toString();
        boolean isUidInUse = checkUidExists(roomList, uid);
        while (isUidInUse) {
            uid = UUID.randomUUID().toString();
            isUidInUse = checkUidExists(roomList, uid);
        }
        return uid;
    }

    // Function to check if uid already exists
    static public boolean checkUidExists(ArrayList<room> roomList, String uid) {
        boolean ret = false;
        for (room room : roomList) {
            if (room.getUid().equals(uid)) {
                ret = true;
                break;
            }
        }
        return ret;
    }

    // Function to create the proper card interface given cardType, cardColor, cardValue as strings
    // This function is case sensitive, for exact qualifiers look in card.java
    // Only use this function for player PUT request for turn taking.
    static public card generateCard(String cardType, String cardColor, String cardValue, String setWildCard) {
        card.CardType type;
        card.Color color;
        try {
            type = card.CardType.valueOf(cardType);
            color = card.Color.valueOf(cardColor);
        }
        catch (IllegalArgumentException e) {
            throw new badRequest("Error with creating card from JSON");
        }
        // Basic card
        if (!cardValue.isBlank() && cardType.equals("Basic") && !cardColor.equals("Black") && setWildCard.isBlank()) {
            try {
                int value = Integer.parseInt(cardValue);
                if (value < 0 || value > 9)
                    throw new badRequest("Error with creating card from JSON");
                return new basicCard(value, color);
            }
            catch (NumberFormatException e) {
                throw new badRequest("Error with creating card from JSON");
            }
        }
        // Special Card or Wild Card (sets wild card temp colour)
        else if (cardValue.isBlank() && !cardType.equals("Basic")) {
            switch (type) {
                case Skip, Reverse, Draw2 -> {
                    if (color != card.Color.Black && setWildCard.isBlank())
                        return new sColorCard(type, color);
                }
                case Draw4, ChangeColor, Blank -> {
                    if (color == card.Color.Black && !setWildCard.isBlank()) {
                        wildCard wildCard = new wildCard(type);
                        card.Color setColor = card.Color.valueOf(setWildCard);
                        wildCard.setTempColor(setColor);
                        return wildCard;
                    }
                }
            }
        }
        throw new badRequest("Error with creating card from JSON");
    }

    // Returns room state from string
    static public room.Status stringToRoomState(String status) {
        try {
            return room.Status.valueOf(status);
        }
        catch (IllegalArgumentException e) {
            throw new badRequest(status + " is not a room status");
        }
    }

    // TODO: Only Start state. May need to add other states as necessary.
    static public void setUpGameState(room room, room.Status status) {
        switch (status) {
            case Lobby -> {
                // Lobby state needed for changing game settings
                return;
            }
            case Start -> {
                serviceUtils.setUpStartGame(room);
                return;
            }
            case Restart -> {
                // For Point Mode: restart game for next round
                // Should not change game settings
                serviceUtils.setUpNextRound(room);
                return;
            }
        }
        throw new badRequest(status.toString() + " is not a room status");
    }

    // Is helper for setUpGameState. Should use through there.
    static public void setUpStartGame(room room) {
        room.shufflePlayers();
        room.setupDeck();
        room.reshuffleDeck();
    }

    // Is helper for setUpGameState. Should use through there.
    // Resets game for next round except the scores.
    static public void setUpNextRound(room room) {
        room.resetRoundGameState();
        room.reshuffleDeck();
    }

    // TODO: Only has Lobby -> Start check. May need to add other states as necessary.
    static public boolean validStatusChange(room room, room.Status status) {
        switch (status) {
            case Lobby -> {
                return true;
            }
            case Start -> {
                return room.getPlayers().size() >= room.getMinPlayers() &&
                        room.getPlayers().size() <= room.getMaxPlayers();
            }
            case Restart -> {
                // TODO: see if there is anything to validate. Otherwise don't need this case.
                return false;
            }
        }
        throw new internalServerError("Room Status should never be " + status.toString()); // Should not reach here
    }

}
