package com.learning.uwuno.game.gameModes;

import com.learning.uwuno.cards.basicCard;
import com.learning.uwuno.cards.card;
import com.learning.uwuno.cards.sColorCard;
import com.learning.uwuno.cards.wildCard;
import com.learning.uwuno.player;
import com.learning.uwuno.util.playerList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class pointMode implements gameMode {
    final private int SPECIAL_CARDS_POINTS = 20;   // Reverse, Skip, +2
    final private int WILD_CARDS_POINTS = 50;      // Wild, Wild +4
    final private String NAME = "POINT";

    @Override
    public String getModeName() {
        return NAME;
    }

    @Override
    public ArrayList<player> determineWinner(player playerTurn, playerList players,
                                             HashMap<player, Integer> scores, int maxScore) {
        ArrayList<player> winners = new ArrayList<>();
        HashMap<player, Integer> pointsInHand = new HashMap<>();
        int total = 0;
        // Calculate points
        for (player player : players) {
            int points = 0;
            for (card card : player.getCardList()) {
                if (card instanceof wildCard) {
                    points += WILD_CARDS_POINTS;
                    total += WILD_CARDS_POINTS;
                } else if (card instanceof sColorCard) {
                    points += SPECIAL_CARDS_POINTS;
                    total += SPECIAL_CARDS_POINTS;
                } else {
                    points += ((basicCard) card).getValue();
                    total += ((basicCard) card).getValue();
                }
            }
            pointsInHand.put(player, points);
        }
        // Update current score
        for (Map.Entry<player, Integer> entry : scores.entrySet()) {
            player player = entry.getKey();
            int score = total - pointsInHand.get(player) + entry.getValue();
            // If reached maxScore, save in winners
            if (score >= maxScore)
                winners.add(player);
            scores.put(player, score);
        }
        return winners;
    }
}
