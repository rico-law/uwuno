package com.learning.uwuno.game;

import com.learning.uwuno.cards.card;
import com.learning.uwuno.player;

import java.util.ArrayList;
import java.util.HashMap;

public class gameResponse {
    private ArrayList<card> playableCards;
    private String playerTurnPid;
    private ArrayList<player> winners;
    private HashMap<player, Integer> scores;

    public gameResponse() {
        playerTurnPid = "";
        playableCards = new ArrayList<>();
        winners = new ArrayList<>();
        scores = new HashMap<>();
    }

    public ArrayList<card> getPlayableCards() {
        return playableCards;
    }

    public String getPlayerTurnPid() {
        return playerTurnPid;
    }

    public void setPlayerTurnResponse(String pid, ArrayList<card> playableCards) {
        this.playerTurnPid = pid;
        this.playableCards = playableCards;
    }

    public void setWinResponse(ArrayList<player> players) {
        winners = players;
        this.playableCards = new ArrayList<>();
    }

    public void setNextRoundResponse(HashMap<player, Integer> scores) {
        this.playerTurnPid = "";
        this.scores = scores;
        this.playableCards = new ArrayList<>();
    }
}
