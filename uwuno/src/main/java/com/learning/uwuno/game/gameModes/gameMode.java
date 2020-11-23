package com.learning.uwuno.game.gameModes;

import com.learning.uwuno.player;
import com.learning.uwuno.util.playerList;

import java.util.*;

public interface gameMode {

    String getModeName();
    ArrayList<player> determineWinner(player playerTurn, playerList players,
                                      HashMap<player, Integer> scores, int maxScore);
}