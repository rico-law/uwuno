package com.learning.uwuno.game;

import com.learning.uwuno.cards.card;
import com.learning.uwuno.player;

import java.util.ArrayList;
import java.util.HashMap;

public class gameResponse {
    private ArrayList<card> playableCards;
    private String playerTurnPid;
    private String winnerPid;
    private String winnerName;
    private HashMap<player, Integer> scores;

    public gameResponse() {
        playerTurnPid = "";
        playableCards = new ArrayList<>();
        winnerPid = "";
        winnerName = "";
        scores = new HashMap<>();
    }

    public ArrayList<card> getPlayableCards() {
        return playableCards;
    }

    public String getPlayerTurnPid() {
        return playerTurnPid;
    }

    public String getWinnerPid() {
        return winnerPid;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setPlayerTurnResponse(String pid, ArrayList<card> playableCards) {
        setPlayerTurnPid(pid);
        setPlayableCards(playableCards);
    }

    public void setWinResponse(player player) {
        this.winnerPid = player.getPid();
        this.winnerName = player.getName();
        setPlayableCards(player.getCardList());
    }

    public void setNextRoundResponse(HashMap<player, Integer> scores) {
        setPlayerTurnPid("");
        this.scores = scores;
        setPlayableCards(new ArrayList<>());
    }

    private void setPlayableCards(ArrayList<card> playableCards) {
        this.playableCards = playableCards;
    }

    private void setPlayerTurnPid(String pid) {
        this.playerTurnPid = pid;
    }
}
