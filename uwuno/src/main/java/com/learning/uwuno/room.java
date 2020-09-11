package com.learning.uwuno;

import com.learning.uwuno.cards.card;
import com.learning.uwuno.cards.deck;
import com.learning.uwuno.errors.errorNotFound;

import java.util.LinkedList;
import java.util.UUID;

public class room {
    // Constants
    final private int MAX_HAND_SIZE = 7;
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

    // Class functions
    public room(String roomName, boolean useBlankCards) {
        this.uid = UUID.randomUUID().toString();
        this.roomName = roomName;
        this.roomStatus = Status.Lobby;
        this.deck = new deck(useBlankCards, MAX_HAND_SIZE);
    }

    // Room Functions
    public String getName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setRoomStatus(Status status) {
        this.roomStatus = status;
    }

    public Status getRoomStatus() {
        return roomStatus;
    }

    public String getUid() {
        return uid;
    }

    // Player Functions
    public LinkedList<player> getPlayers() {
        return playerList;
    }

    public player getPlayer(String pid) {
        return playerList.stream().filter(t -> t.getPid().equals(pid)).findFirst().get();
    }

    public void addPlayer(player newPlayer) {
        playerList.add(newPlayer);
    }

    public void deletePlayer(String pid) {
        if (!playerList.removeIf(t -> t.getPid().equals(pid)))
            throw new errorNotFound();
    }

    // Only use this to create new deck as this will ensure each player gets the same reference deck
    public void setupDeck() {
        for (player curPlayer : playerList) {
            curPlayer.setCurDeck(this.deck);
        }
    }

    public card lastPlayedCard() {
        return deck.lastPlayedCard();
    }
}
