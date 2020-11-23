package com.learning.uwuno;

import com.learning.uwuno.cards.basicCard;
import com.learning.uwuno.cards.card;
import com.learning.uwuno.cards.deck;
import com.learning.uwuno.game.*;
import com.learning.uwuno.util.playerList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.*;

public class room {
    // Constants
    final private int MAX_HAND_SIZE = 7;
    final private int MAX_PLAYERS = 10;
    final private int MIN_PLAYERS = 2;
    public enum Status {
        Lobby,
        Start,
        Restart
    }

    // Class Variables
    final private String uid;
    private String roomName;
    private playerList playerList;
    private deck deck;
    private Status roomStatus;
    private player playerTurn;
    private gameSettings gameSettings;
    private gameState gameState;

    // Class functions
    // TODO: remove useBlankCards from param (useBlankCards moved to gameSettings)
    public room(String roomName, boolean useBlankCards, String uid) {
        this.uid = uid;
        this.playerList = new playerList(this.uid);
        this.roomName = roomName;
        this.roomStatus = Status.Lobby;
        this.deck = new deck(useBlankCards);
        this.gameSettings = new gameSettings();
        this.gameState = new gameState(playerList);
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

    public gameSettings getGameSettings() {
        return gameSettings;
    }

    public gameState getGameState() {
        return gameState;
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

    // TODO: refactor room (e.g. too many reference to deck properties)
    public int getActiveDeckSize() {
        return deck.getActiveDeck().size();
    }

    // Player Functions
    public playerList getPlayers() {
        return playerList;
    }

    public player getPlayer(String pid) {
        return playerList.stream().filter(t -> t.getPid().equals(pid)).findFirst().get();
    }

    public player addPlayer(player newPlayer) {
        playerList.add(newPlayer);
        return newPlayer;
    }

    // Only use this function to edit player names
    public player updatePlayerName(String pid, String newName) {
        player player = getPlayer(pid);
        playerList.changeName(player, newName);
        return player;
    }

    public boolean deletePlayer(String pid) {
        return playerList.removeIf(t -> t.getPid().equals(pid));
    }

    public void shufflePlayers() {
        Collections.shuffle(playerList);
        this.playerTurn = playerList.getFirst();
    }

    // Sets next player and returns it
    public player nextPlayer(boolean turnDirection) {
        playerTurn = (turnDirection) ? playerList.next() : playerList.prev();
        return playerTurn;
    }

    // Only use this to create new deck as this will ensure each player gets the same reference deck
    public void setupDeck() {
        for (player curPlayer : playerList) {
            curPlayer.setCurDeck(this.deck);
        }
    }

    public card flipTopCard() {
        return this.deck.drawStart();
    }

    public card getLastPlayedCard() {
        return deck.getLastPlayedCard();
    }

    public ArrayList<card> getDiscardPile() {
        return deck.getDiscardPile();
    }

    // Restarts game after cards have been dealt/played. Does not include final step of flipping top card.
    public void reshuffleDeck() {
        // Place last played card back in deck
        if (getLastPlayedCard() != null)
            getDiscardPile().add(getLastPlayedCard());

        // Place hand cards back in deck
        for (player player : getPlayers()) {
            ArrayList<card> handCards = player.getCardList();
            getDiscardPile().addAll(handCards);
        }

        // Deal hand cards
        deck.reshuffle();
        for (player player : getPlayers()) {
            player.drawCards(getMaxHandSize());
        }

        setValidFirstCard();
    }

    public void setValidFirstCard() {
        card card = flipTopCard();

        // Reshuffle if flipped card is not a Basic card
        if (!(card instanceof basicCard))
            reshuffleDeck();
    }

    public void resetGameState() {
        this.gameState = new gameState(playerList);
    }

    // For Point Mode: resets game state fields except scores
    public void resetRoundGameState() {
        gameState.resetRound();
    }
}
