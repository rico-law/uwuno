package com.learning.uwuno.game;

import com.learning.uwuno.cards.card;
import com.learning.uwuno.player;

import java.util.ArrayList;

public class gameResponse {
    private ArrayList<card> playableCards;
    private String playerTurnPid;
    private String winnerPid;
    private String winnerName;

    public gameResponse() {
        playableCards = new ArrayList<>();
        winnerPid = "";
        winnerName = "";
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
        this.setPlayableCards(player.getCardList());
    }

    private void setPlayableCards(ArrayList<card> playableCards) {
        this.playableCards = playableCards;
    }

    private void setPlayerTurnPid(String pid) {
        this.playerTurnPid = pid;
    }
}
