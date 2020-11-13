package com.learning.uwuno.game.gameModes;

import com.learning.uwuno.player;
import com.learning.uwuno.util.playerList;

import java.util.ArrayList;
import java.util.HashMap;

public class normalMode implements gameMode {
    final private int SPECIAL_CARDS_POINTS = 20;   // Reverse, Skip, +2
    final private int WILD_CARDS_POINTS = 50;      // Wild, Wild +4
    final private String NAME = "NORMAL";

    @Override
    public String getModeName() {
        return NAME;
    }

    @Override
    public ArrayList<player> determineWinner(player playerTurn, playerList players,
                                             HashMap<player, Integer> scores, int maxScore) {
        ArrayList<player> winners = new ArrayList<player>();
        // If player has no hand cards, they win
        if (playerTurn.getCardList().isEmpty()) {
            winners.add(playerTurn);
        } else {
            // Otherwise, calculate all the points and return player with the least points as the winner.
            // If a tie, choose player with least cards

        }
        return winners;
    }
}
