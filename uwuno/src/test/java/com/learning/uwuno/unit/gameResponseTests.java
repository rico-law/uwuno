package com.learning.uwuno.unit;

import com.learning.uwuno.cards.*;
import com.learning.uwuno.game.*;
import com.learning.uwuno.game.gameModes.gameMode;
import com.learning.uwuno.game.gameModes.normalMode;
import com.learning.uwuno.game.gameModes.pointMode;
import com.learning.uwuno.player;
import com.learning.uwuno.room;
import com.learning.uwuno.util.playerList;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.mockito.Mockito.*;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class gameResponseTests {
    private final int NUM_PLAYERS = 3;
    private final int MAX_SCORE = 500;
    private playerList playerList;
    private HashMap<player, Integer> playerScores;
    private ArrayList<player> winners;
    private ArrayList<ArrayList<card>> playerHandCards;
    private final gameMode normalMode = new normalMode();
    private final gameMode pointMode = new pointMode();

    @BeforeEach
    public void setUp() {
        playerList = new playerList("123");
        playerScores = new HashMap<>();
        winners = new ArrayList<>();
        playerHandCards = new ArrayList<>();
    }

    // Checks

    private void populatePlayersAndScores(ArrayList<Integer> scores) {
        for (int i = 1; i < NUM_PLAYERS + 1; i++ ) {
            player spyPlayer = spy(new player("player" + i));
            playerList.add(spyPlayer);
            playerScores.put(spyPlayer, scores.get(i - 1));
        }
    }

    private void populateHandCards() {
        for (int i = 0; i < NUM_PLAYERS; i++) {
            doReturn(playerHandCards.get(i)).when(playerList.get(i)).getCardList();
        }
    }
}
