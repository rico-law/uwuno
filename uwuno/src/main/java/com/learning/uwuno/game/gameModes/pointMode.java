package com.learning.uwuno.game.gameModes;

import com.learning.uwuno.cards.*;
import com.learning.uwuno.player;
import com.learning.uwuno.util.playerList;

import java.util.*;

/**
 * Rules and special settings for Point mode.
 */
public class pointMode implements gameMode {
    final private int SPECIAL_CARDS_POINTS = 20;   // Reverse, Skip, +2
    final private int WILD_CARDS_POINTS = 50;      // Wild, Wild +4
    final private String NAME = "POINT";

    @Override
    public String getModeName() {
        return NAME;
    }

    @Override
    public ArrayList<player> determineWinner(player playerTurn, HashMap<player, Integer> scores, int maxScore) {
        ArrayList<player> winners = new ArrayList<>();
        // Calculate points
        for (Map.Entry<player, Integer> entry : scores.entrySet()) {
            player player = entry.getKey();
            int currentPlayerScore = entry.getValue();
            for (card card : player.getCardList()) {
                if (card instanceof wildCard)
                    currentPlayerScore += WILD_CARDS_POINTS;
                else if (card instanceof sColorCard)
                    currentPlayerScore += SPECIAL_CARDS_POINTS;
                else
                    currentPlayerScore += ((basicCard) card).getValue();
            }
            scores.put(player, currentPlayerScore);
        }
        // Save winners who reached maxScore, else return empty list (to indicate next round)
        for (Map.Entry<player, Integer> entry : scores.entrySet()) {
            if (entry.getValue() >= maxScore)
                winners.add(entry.getKey());
        }
        return winners;
    }
}
