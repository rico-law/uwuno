package com.learning.uwuno;

import com.learning.uwuno.cards.card;
import com.learning.uwuno.cards.deck;
import com.learning.uwuno.errors.badRequest;
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
        End
    }

    // Class Variables
    final private String uid;
    private String roomName;
    private playerList playerList;
    private deck deck;
    private Status roomStatus;
    private player playerTurn;
    private boolean turnDirection; // true if forward, otherwise reverse direction
    private gameMode gameMode;
    private gameState gameState;

    // Class functions
    public room(String roomName, boolean useBlankCards, String uid, String gameMode) {
        this.uid = uid;
        this.playerList = new playerList(this.uid);
        this.roomName = roomName;
        this.roomStatus = Status.Lobby;
        this.deck = new deck(useBlankCards);
        this.turnDirection = true;
        this.gameMode = getGameMode(gameMode);
        this.gameState = new gameState();
    }

    // Room Functions
    private gameMode getGameMode(String mode) {
        return switch (mode.toLowerCase()) {
            case "normal" -> new normalMode();
            case "point" -> new pointMode();
            default -> throw new badRequest("Requesting invalid game mode");
        };
    }

    public void setGameMode(String mode) {
        this.gameMode = getGameMode(mode);
    }

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

    public void changeDirection() {
        turnDirection = !turnDirection;
    }

    // TODO: replace this placeholder function until next/prev functionality is implemented in playerList
    // Sets next player and returns it
    public player nextPlayer() {
        ListIterator<player> players = playerList.listIterator();
        while (players.hasNext()) {
            if (playerTurn.equals(players.next())) {
                if (turnDirection) {
                    if (players.hasNext()) {
                        playerTurn = players.next();
                        break;
                    } else {
                        playerTurn = playerList.getFirst();
                        break;
                    }
                } else {
                    if (players.hasPrevious()) {
                        playerTurn = players.previous();
                        break;
                    } else {
                        playerTurn = playerList.getLast();
                        break;
                    }
                }
            }
        }
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

    public card lastPlayedCard() {
        return deck.getLastPlayedCard();
    }

    public ArrayList<card> getDiscardPile() {
        return deck.getDiscardPile();
    }

    // Restarts game after cards have been dealt/played. Does not include final step of flipping top card.
    public void reshuffleDeck() {
        // Place last played card back in deck
        if (lastPlayedCard() != null)
            getDiscardPile().add(lastPlayedCard());

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
    }
}
