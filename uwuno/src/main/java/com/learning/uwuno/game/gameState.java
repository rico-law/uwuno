package com.learning.uwuno.game;

import com.learning.uwuno.player;
import com.learning.uwuno.util.playerList;

import java.util.HashMap;

public class gameState {
    // Constants (can set custom point system for future)
    final private int SPECIAL_CARDS = 20;   // Reverse, Skip, +2
    final private int WILD_CARDS = 50;      // Wild, Wild +4

    private int cardsToDraw;    // Keeps track of +4, +2 stacking
    private int turnsTaken;
    private HashMap<String, Float> scores;

    // To keep track of points and other current game properties
    public gameState(playerList playerList) {
        cardsToDraw = 0;
        turnsTaken = 0;
        scores = new HashMap<>();

        for (player player : playerList) {
            scores.put(player.getPid(), (float) 0);
        }
    }
}