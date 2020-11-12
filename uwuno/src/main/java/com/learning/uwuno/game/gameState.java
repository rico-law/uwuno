package com.learning.uwuno.game;

import java.util.HashMap;

public class gameState {
    // Constants (can set custom point system for future)
    final private int SPECIAL_CARDS = 20;   // Reverse, Skip, +2
    final private int WILD_CARDS = 50;      // Wild, Wild +4

    private int cardsToDraw;
    private int maxTurn;
    private int maxScore;
    private HashMap<String, Float> scores;

    // To keep track of points and other game properties
    public gameState() {

    }
}
