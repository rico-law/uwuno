package com.learning.uwuno;

import com.learning.uwuno.cards.card;
import com.learning.uwuno.cards.deck;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.learning.uwuno.errors.internalServerError;

import java.util.*;

public class room {
    // Constants
    final private int MAX_HAND_SIZE = 7;
    final private int MAX_PLAYERS = 10;
    final private int MIN_PLAYERS = 2;
    public enum Status {
        Lobby,
        Start,
        End
    }

    // Class Variables
    final private String uid;
    private String roomName;
    private LinkedList<player> playerList = new LinkedList<player>();
    private deck deck;
    private Status roomStatus;
    private player playerTurn;

    // Class functions
    public room(String roomName, boolean useBlankCards) {
        this.uid = UUID.randomUUID().toString();
        this.roomName = roomName;
        this.roomStatus = Status.Lobby;
        this.deck = new deck(useBlankCards);
    }

    // Room Functions
    public String getName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setRoomStatus(Status status) {
        roomStatus = status;
    }

    public Status getRoomStatus() {
        return roomStatus;
    }

    public String getUid() {
        return uid;
    }

    // Ignores the cardList field in the response JSON. We only need player id and name.
    @JsonIgnoreProperties("cardList")
    public player getPlayerTurn() {
        return playerTurn;
    }

    public int getMaxHandSize() {
        return MAX_HAND_SIZE;
    }

    public int getMaxPlayers() {
        return MAX_PLAYERS;
    }

    public int getMinPlayers() {
        return MIN_PLAYERS;
    }

    // Player Functions
    public LinkedList<player> getPlayers() {
        return playerList;
    }

    public player getPlayer(String pid) {
        return playerList.stream().filter(t -> t.getPid().equals(pid)).findFirst().get();
    }

    // TODO: Need to change all the add players to use this instead? So names are checked
    public player addPlayer(player newPlayer) {
        // Check if name is already used and automatically add a hash to the end
        // Create a hash map of all names in the playerList
        HashMap<String, Boolean> map = new HashMap<>();
        for (player cur : playerList) {
            // Check that a player cannot be added to a room more than once by comparing PID's
            if (cur.getPid().equals(newPlayer.getPid())) {
                throw new internalServerError("Attempted to add a player to a room more than once");
            }
            // Create map of all the names currently in the room, assumes the current list is valid with no repeats
            map.put(cur.getName(), true);
        }
        // Check if newPlayer name already exists add a 4 digit hash to their name
        boolean nameAlreadyUsed = map.containsKey(newPlayer.getName());
        String newName = newPlayer.getName();
        while (nameAlreadyUsed) {
            String hash = "#" + String.valueOf((Math.random() * 9999) + 1000).substring(0, 4);
            newName = newPlayer.getName() + hash;
            nameAlreadyUsed = map.containsKey(newName);
        }
        newPlayer.setName(newName);
        playerList.add(newPlayer);
        return newPlayer;
    }

    public boolean deletePlayer(String pid) {
        return playerList.removeIf(t -> t.getPid().equals(pid));
    }

    public void shufflePlayers() {
        Collections.shuffle(playerList);
        this.playerTurn = playerList.getFirst();
    }

    // Only use this to create new deck as this will ensure each player gets the same reference deck
    public void setupDeck() {
        for (player curPlayer : playerList) {
            curPlayer.setCurDeck(this.deck);
        }
    }

    public void flipTopCard() {
        this.deck.drawStart();
    }

    public card lastPlayedCard() {
        return deck.getLastPlayedCard();
    }
}
