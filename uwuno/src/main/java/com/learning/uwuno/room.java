package com.learning.uwuno;

import com.learning.uwuno.cards.card;
import com.learning.uwuno.cards.deck;

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
        this.deck = new deck(useBlankCards, MAX_HAND_SIZE);
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

    public void deletePlayer(String pid) {
        playerList.removeIf(t -> t.getPid().equals(pid));
    }

    // Deck Functions, do not return deck and handle everything here so as there's no way to control what is
    // done with the reference of the deck
    public ArrayList<card> drawCards(int numCards) {
        return deck.drawCard(numCards);
    }

    public card lastPlayedCard() {
        return deck.lastPlayedCard();
    }

    public void addToDiscard(card played) {
        deck.addToDiscard(played);
    }
}
