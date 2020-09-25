package com.learning.uwuno;

import com.learning.uwuno.cards.card;
import com.learning.uwuno.cards.deck;
import com.learning.uwuno.errors.errorNotFound;

import java.util.ArrayList;
import java.util.UUID;

public class room {
    // Constants
    final private int MAX_HAND_SIZE = 7;

    // Class Variables
    final private String uid;
    private String roomName;
    private ArrayList<player> playerList = new ArrayList<player>();
    private deck deck;

    // Class functions
    public room(String roomName, boolean useBlankCards) {
        this.uid = UUID.randomUUID().toString();
        this.roomName = roomName;
        setupDeck(new deck(useBlankCards, MAX_HAND_SIZE));
    }

    // Room Functions
    public String getName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getUid() {
        return uid;
    }

    // Player Functions
    public ArrayList<player> getPlayers() {
        return playerList;
    }

    public player getPlayer(String pid) {
        return playerList.stream().filter(t -> t.getPid().equals(pid)).findFirst().get();
    }

    public void addPlayer(player newPlayer) {
        playerList.add(newPlayer);
    }

    public boolean deletePlayer(String pid) {
        return playerList.removeIf(t -> t.getPid().equals(pid));
    }

    // Only use this to create new deck as this will ensure each player gets the same reference deck
    public void setupDeck(deck newDeck) {
        deck = newDeck;
        for (player curPlayer : playerList) {
            curPlayer.setCurDeck(newDeck);
        }
    }

    public card lastPlayedCard() {
        return deck.lastPlayedCard();
    }
}
