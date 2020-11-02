package com.learning.uwuno;

import com.learning.uwuno.cards.card;
import com.learning.uwuno.cards.deck;

import java.util.*;

public class player {
    // Class Variables
    final private String pid;
    private String name;
    private final List<card> cardList = Collections.synchronizedList(new ArrayList<>()); // TODO: Start game must check to make sure this value is not empty
    private deck curDeck;

    public player(String name) {
        this.pid = UUID.randomUUID().toString();
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public String getName() {
        return name;
    }

    public List<card> getCardList() {
        return cardList;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Used to keep a reference to the deck currently being used,
    // Try to use setDeck() in room to prevent having different copies of the deck for different players
    public void setCurDeck(deck newDeck) {
        this.curDeck = newDeck;
    }

    public synchronized ArrayList<card> drawCards(int numCards) {
        ArrayList<card> drawnCards = curDeck.drawCards(numCards);
        cardList.addAll(drawnCards);
        return drawnCards;
    }

    // TODO: synchronize deck
    public synchronized boolean playCard(card toPlay) {
        curDeck.addToDiscard(toPlay);
        return removeCard(toPlay);
    }

    // TODO: synchronize deck
    // Used for in the case a card is used by a player, shouldn't handle game logic here
    public synchronized boolean removeCard(card card) {
        return cardList.remove(card);
    }
}