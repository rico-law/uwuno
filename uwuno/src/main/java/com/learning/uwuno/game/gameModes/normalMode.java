package com.learning.uwuno.game.gameModes;

import com.learning.uwuno.cards.*;
import com.learning.uwuno.player;
import com.learning.uwuno.util.playerList;

import java.util.*;

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
        // If player has no hand cards, they win
        if (playerTurn.getCardList().isEmpty())
            return new ArrayList<>(Arrays.asList(playerTurn));

        // Otherwise, calculate all the points and return player with the least points as the winner.
        // If a tie, choose player with least cards
        ArrayList<player> nominees = new ArrayList<player>();
        ArrayList<player> winners = new ArrayList<player>();
        int minScore = Integer.MAX_VALUE;
        for (player player : players) {
            int points = 0;
            for (card card : player.getCardList()) {
                if (card instanceof wildCard) {
                    points += WILD_CARDS_POINTS;
                } else if (card instanceof sColorCard) {
                    points += SPECIAL_CARDS_POINTS;
                } else {
                    points += ((basicCard) card).getValue();
                }
            }
            if (points > minScore)
                continue;
            if (points < minScore)
                nominees.clear();
            nominees.add(player);
            minScore = points;
        }
        if (nominees.size() > 1) {
            int minCards = Integer.MAX_VALUE;
            for (player player : nominees) {
                int size = player.getCardList().size();
                if (size > minCards)
                    continue;
                if (size < minCards)
                    winners.clear();
                winners.add(player);
                minCards = size;
            }
        } else {
            winners = nominees;
        }
        return winners;
    }
}
