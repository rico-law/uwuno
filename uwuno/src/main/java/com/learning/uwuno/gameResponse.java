package com.learning.uwuno;

import com.learning.uwuno.cards.card;

import java.util.ArrayList;

public class gameResponse {
    private ArrayList<card> playableCards;
    private String playerTurnPid;

    public gameResponse() {
        playableCards = new ArrayList<>();
    }

    public ArrayList<card> getPlayableCards() {
        return playableCards;
    }

    public String getPlayerTurnPid() {
        return playerTurnPid;
    }

    public void setPlayableCards(ArrayList<card> playableCards) {
        this.playableCards = playableCards;
    }

    public void setPlayerTurnPid(String pid) {
        this.playerTurnPid = pid;
    }
}
