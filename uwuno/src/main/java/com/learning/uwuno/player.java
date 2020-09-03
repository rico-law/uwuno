package com.learning.uwuno;

import com.learning.uwuno.cards.card;
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

    // Adds card to hand ie. part of drawing a card
    public void addCards(ArrayList<card> newCards) {
        cardList.addAll(newCards);
    }

    // Used for in the case a card is used by a player, shouldn't handle game logic here
    public void removeCard(card card) {
        if(!cardList.remove(card))
            throw new badRequest();
    }
}
