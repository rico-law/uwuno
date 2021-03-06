package com.learning.uwuno.game;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.learning.uwuno.cards.card;
import com.learning.uwuno.player;

import java.util.*;

/**
 * gameResponse creates JSON response used in-game. The response can contain:
 *
 * - The current or next player's pid and their playable cards OR
 * - A list of winners (to indicate the end of the game) OR
 * - In the case of Point Mode, a hashmap of current scores to carry over into the next round. Indicates the end of the round.
 *
 */
public class gameResponse {
    private List<card> playableCards;
    private String playerTurnPid;
    private ArrayList<player> winners;
    private HashMap<player, Integer> scores;

    public gameResponse() {
        playerTurnPid = "";
        playableCards = new ArrayList<>();
        winners = new ArrayList<>();
        scores = new HashMap<>();
    }

    public List<card> getPlayableCards() {
        return playableCards;
    }

    public String getPlayerTurnPid() {
        return playerTurnPid;
    }

    public ArrayList<player> getWinners() {
        return winners;
    }

    public HashMap<player, Integer> getScores() {
        return scores;
    }

    public void setPlayerTurnResponse(String pid, List<card> playableCards) {
        this.playerTurnPid = pid;
        this.playableCards = playableCards;
    }

    public void setWinResponse(ArrayList<player> players) {
        winners = players;
        // TODO: figure out how to use JSON ignore to omit playerTurnPid and playableCards
        this.playableCards = new ArrayList<>();
    }

    public void setNextRoundResponse(HashMap<player, Integer> scores) {
        this.playerTurnPid = "";
        this.scores = scores;
        this.playableCards = new ArrayList<>();
    }
}
