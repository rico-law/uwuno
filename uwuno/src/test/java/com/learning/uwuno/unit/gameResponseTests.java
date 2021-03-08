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

/**
 * Verify gameResponse starting from gameService.takeTurn().
 */
@ExtendWith(MockitoExtension.class)
public class gameResponseTests {
    private final int NUM_PLAYERS = 3;
    private final int MAX_SCORE = 500;
    private playerList playerList;
    private HashMap<player, Integer> playerScores;
    private ArrayList<player> winners;
    private ArrayList<ArrayList<card>> playersHandCards;

    @BeforeEach
    public void setUp() {
        playerList = new playerList("123");
        playerScores = new HashMap<>();
        winners = new ArrayList<>();
        playersHandCards = new ArrayList<>();
    }

    // takeTurn
    // 1) playCard => endTurn proper next player & playable cards
    // 2) if invalid played card => throw error

    /**
     * Throw bad request when invalid card is played.
     */
    @Test
    public void error400WhenPlayInvalidCard() {
//        playersHandCards.addAll(new ArrayList<>(
//                Arrays.asList(
//                        new ArrayList<>(Arrays.asList(new wildCard(card.CardType.Draw4))),
//                        new ArrayList<>(Arrays.asList(new sColorCard(card.CardType.Reverse, card.Color.Yellow))),
//                        new ArrayList<>(Arrays.asList(new sColorCard(card.CardType.Skip, card.Color.Green),
//                                new basicCard(10, card.Color.Red)))
//                ))
//        );
//        populateHandCards();
//
//        assertThat(winners.size(), is(1));
//        assertThat(winners.get(0).getName(), is("player2"));
    }

    /**
     *
     */
    @Test
    public void playCard() {

    }

    /**
     * Verify gameResponse when player skips turn with no +4, +2 penalty and draws 1 playable card.
     */
    @Test
    public void skipTurnWithNoPenaltyAndDrawOnePlayableCard() {

    }

    /**
     * Verify gameResponse when player skips turn with no +4, +2 penalty and draws 1 non-playable card.
     */
    @Test
    public void skipTurnWithNoPenaltyAndDrawNoPlayableCard() {

    }

    /**
     * Verify gameResponse when player skips turn with playable +4, +2 penalty cards drawn.
     */
    @Test
    public void skipTurnWithPlayablePenaltyCards() {

    }

    /**
     * Verify gameResponse when player skips turn with no playable +4, +2 penalty cards drawn.
     */
    @Test
    public void skipTurnWithNoPlayablePenaltyCard() {

    }

    // Checks proper gameResponse value for skipTurn
    // 1) skipTurn => no penalty => draw 1 with playable cards
    // 2) skipTurn => no penalty => draw 1 with no playable cards => endTurn see below
    // 3) skipTurn => penalty => draws playable cards
    // 4) skipTurn => penalty => no playable cards drawn => draw 1 => same as 1, 2

    // Checks proper gameResponse value for endTurn
    // 1) endTurn => if there's a winner => winResponse
    // 2) endTurn => if there's no winner => nextRoundResponse
    // 3) endTurn => if there's skip Turn => proper next player
    // 4) endTurn => if no skip turn => proper next player
    // 5) endTurn => if reverse => proper next player



    @Test
    public void endTurnWhenReverseCardPlayed() {

    }

    /**
     *
     */
    @Test
    public void endTurnWhenSkipCardPlayed() {

    }




    private void populatePlayersAndScores(ArrayList<Integer> scores) {
        for (int i = 1; i < NUM_PLAYERS + 1; i++ ) {
            player spyPlayer = spy(new player("player" + i));
            playerList.add(spyPlayer);
            playerScores.put(spyPlayer, scores.get(i - 1));
        }
    }

    private void populateHandCards() {
        for (int i = 0; i < NUM_PLAYERS; i++) {
            doReturn(playersHandCards.get(i)).when(playerList.get(i)).getCardList();
        }
    }
}
