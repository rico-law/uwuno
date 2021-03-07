package com.learning.uwuno.unit;

import com.learning.uwuno.cards.*;
import com.learning.uwuno.game.gameModes.*;
import com.learning.uwuno.player;
import com.learning.uwuno.util.playerList;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.*;

/**
 * Test gameMode method determineWinner() for both normal mode and point mode.
 */
@ExtendWith(MockitoExtension.class)
public class determineWinnerTests {
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

    /**
     * Test winner is determined in normal mode via no hand cards condition.
     */
    @Test
    public void testNormalModeWinnerByNoHandCards() {
        player spyPlayer = spy(new player("spyPlayer"));
        doReturn(new ArrayList<>()).when(spyPlayer).getCardList();
        winners = normalMode.determineWinner(spyPlayer, playerScores, MAX_SCORE);

        assertThat(winners.size(), is(1));
        assertThat(winners.get(0), is(spyPlayer));
    }

    /**
     * Test winner is determined in normal mode via least points in hand condition.
     * This occurs when max turn has been reached and all players still have cards in hand.
     * TODO: override card points in case values change in gameMode classes
     */
    @Test
    public void testNormalModeWinnerByLeastPoints() {
        populatePlayersAndScores(new ArrayList<>(Collections.nCopies(NUM_PLAYERS, 0)));
        playerHandCards.addAll(new ArrayList<>(
                Arrays.asList(
                        new ArrayList<>(Arrays.asList(new wildCard(card.CardType.Draw4))),
                        new ArrayList<>(Arrays.asList(new sColorCard(card.CardType.Reverse, card.Color.Yellow))),
                        new ArrayList<>(Arrays.asList(new sColorCard(card.CardType.Skip, card.Color.Green),
                                new basicCard(10, card.Color.Red)))
                ))
        );
        populateHandCards();
        winners = normalMode.determineWinner(playerList.get(0), playerScores, MAX_SCORE);

        assertThat(winners.size(), is(1));
        assertThat(winners.get(0).getName(), is("player2"));
    }

    /**
     * Test winner is determined in normal mode via least hand cards condition.
     * This occurs to resolve tiebreakers when calculation by least points leads to a tie.
     */
    @Test
    public void testNormalModeWinnerByLeastCards() {
        populatePlayersAndScores(new ArrayList<>(Collections.nCopies(NUM_PLAYERS, 0)));
        playerHandCards.addAll(new ArrayList<>(
                Arrays.asList(
                        new ArrayList<>(Arrays.asList(new wildCard(card.CardType.Draw4))),
                        new ArrayList<>(Arrays.asList(new sColorCard(card.CardType.Reverse, card.Color.Yellow),
                                new basicCard(5, card.Color.Green),
                                new basicCard(5, card.Color.Red))),
                        new ArrayList<>(Arrays.asList(new sColorCard(card.CardType.Skip, card.Color.Green),
                                new basicCard(10, card.Color.Red)))
                ))
        );
        populateHandCards();
        winners = normalMode.determineWinner(playerList.get(0),playerScores, MAX_SCORE);

        assertThat(winners.size(), is(1));
        assertThat(winners.get(0).getName(), is("player3"));
    }

    /**
     * Test result in more than 1 winner in normal mode if all winning conditions result in a tie.
     */
    @Test
    public void testNormalModeMoreThanOneWinner() {
        populatePlayersAndScores(new ArrayList<>(Collections.nCopies(NUM_PLAYERS, 0)));
        playerHandCards.addAll(new ArrayList<>(
                Arrays.asList(
                        new ArrayList<>(Arrays.asList(new wildCard(card.CardType.Draw4))),
                        new ArrayList<>(Arrays.asList(new wildCard(card.CardType.ChangeColor))),
                        new ArrayList<>(Arrays.asList(new sColorCard(card.CardType.Skip, card.Color.Green),
                                new sColorCard(card.CardType.Skip, card.Color.Green),
                                new basicCard(10, card.Color.Red)))
                ))
        );
        populateHandCards();
        winners = normalMode.determineWinner(playerList.get(0),playerScores, MAX_SCORE);

        assertThat(winners.size(), is(2));
        assertThat(winners.get(0).getName(), is("player1"));
        assertThat(winners.get(1).getName(), is("player2"));
    }

    /**
     * Test winner is determined in point mode via reaching maxScore.
     * Note: Can have more than 1 winner.
     * TODO: override card points in case values change in gameMode classes
     */
    @Test
    public void testPointModeWinnerByReachingMaxScore() {
        populatePlayersAndScores(new ArrayList<>(Collections.nCopies(NUM_PLAYERS, 499)));
        playerHandCards.addAll(new ArrayList<>(
                Arrays.asList(
                        new ArrayList<>(Arrays.asList(new wildCard(card.CardType.Draw4))),
                        new ArrayList<>(Arrays.asList(new sColorCard(card.CardType.Reverse, card.Color.Yellow))),
                        new ArrayList<>(Arrays.asList(new sColorCard(card.CardType.Skip, card.Color.Green),
                                new basicCard(10, card.Color.Red)))
                ))
        );
        populateHandCards();
        winners = pointMode.determineWinner(playerList.get(0), playerScores, MAX_SCORE);

        assertThat(playerScores.get(playerList.get(0)), is(499 + 50));
        assertThat(playerScores.get(playerList.get(1)), is(499 + 20));
        assertThat(playerScores.get(playerList.get(2)), is(499 + 30));
        assertThat(winners.size(), is(3));
        assertThat(winners.contains(playerList.get(0)), is(true));
        assertThat(winners.contains(playerList.get(1)), is(true));
        assertThat(winners.contains(playerList.get(2)), is(true));
    }

    /**
     * Test when no player has reached maxScore, no players are determined winners in point mode.
     * (In this case, the score is kept and gameState resets for next round until a player reaches maxScore, cumulatively.)
     * Pre-condition: when a player has no hand card or maxTurn has been reached.
     */
    @Test
    public void testPointModeNoWinner() {
        populatePlayersAndScores(new ArrayList<>(Collections.nCopies(NUM_PLAYERS, 200)));
        playerHandCards.addAll(new ArrayList<>(
                Arrays.asList(
                        new ArrayList<>(Arrays.asList(new wildCard(card.CardType.Draw4))),
                        new ArrayList<>(Arrays.asList(new sColorCard(card.CardType.Reverse, card.Color.Yellow))),
                        new ArrayList<>(Arrays.asList(new sColorCard(card.CardType.Skip, card.Color.Green),
                                new basicCard(10, card.Color.Red)))
                ))
        );
        populateHandCards();
        winners = pointMode.determineWinner(playerList.get(0), playerScores, MAX_SCORE);

        assertThat(playerScores.get(playerList.get(0)), is(200 + 50));
        assertThat(playerScores.get(playerList.get(1)), is(200 + 20));
        assertThat(playerScores.get(playerList.get(2)), is(200 + 30));
        assertThat(winners.size(), is(0));
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
            doReturn(playerHandCards.get(i)).when(playerList.get(i)).getCardList();
        }
    }
}
