package com.learning.uwuno;

import com.learning.uwuno.cards.card;
import com.learning.uwuno.cards.deck;
import com.learning.uwuno.errors.badRequest;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.UUID;

public class player {
    // Class Variables
    @JsonIgnore
    final private String pid;
    private String name;
    private ArrayList<card> cardList = new ArrayList<card>(); // TODO: Start game must check to make sure this value is not empty
    @JsonIgnore
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

    public ArrayList<card> getCardList() {
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

    public ArrayList<card> drawCards(int numCards) {
        ArrayList<card> drawnCards = curDeck.drawCards(numCards);
        cardList.addAll(drawnCards);
        return drawnCards;
    }

    public void playCard(card toPlay) {
        curDeck.addToDiscard(toPlay);
        removeCard(toPlay);
    }

    // Used for in the case a card is used by a player, shouldn't handle game logic here
    public void removeCard(card card) {
        if(!cardList.remove(card))
            throw new badRequest();
    }
}
