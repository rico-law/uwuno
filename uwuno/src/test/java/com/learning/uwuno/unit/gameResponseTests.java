package com.learning.uwuno.unit;

import com.learning.uwuno.cards.*;
import com.learning.uwuno.errors.badRequest;
import com.learning.uwuno.game.*;
import com.learning.uwuno.player;
import com.learning.uwuno.room;
import com.learning.uwuno.services.gameService;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.mockito.Mockito.*;

import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

/**
 * Verify gameResponse starting from gameService.takeTurn().
 */
@ExtendWith(MockitoExtension.class)
public class gameResponseTests {
    private final int NUM_PLAYERS = 3;
    private ArrayList<player> players;

    @Spy private gameService gameService;
    private room spyRoom;
    private player spyPlayer;

    @BeforeEach
    public void setUp() {
        room room = new room("room", false, "123");
        players = new ArrayList<>();
        populateRoomWithSpyPlayers(room);

        spyPlayer = room.getPlayerTurn();
        spyRoom = spy(room);
    }


    /**
     * Throw bad request when invalid card is played
     * (i.e. Card played does not exist in hand, but matches lastPlayed).
     */
    @Test
    public void testBadRequest400WhenPlayNonExistentCard() {
        card toPlay = new basicCard(1, card.Color.Red);
        card lastPlayed = new basicCard(1, card.Color.Green);

        doReturn(spyRoom).when(gameService).getRoom("123");
        doReturn(lastPlayed).when(spyRoom).getLastPlayedCard();
        doReturn(false).when(spyPlayer).playCard(toPlay);

        Exception exception = assertThrows(badRequest.class, () -> gameService.takeTurn(
                "123", spyPlayer.getPid(), "Basic", "Red", "1", "", "false"
        ));
        assertThat(exception.getMessage(), is("Invalid card played"));
    }

    /**
     * Throw bad request when invalid card is played
     * (i.e. Card does not match lastPlayed, but exists in hand).
     */
    @Test
    public void testBadRequest400WhenPlayNonMatchingLastCard() {
        card lastPlayed = new basicCard(2, card.Color.Green);

        doReturn(spyRoom).when(gameService).getRoom("123");
        doReturn(lastPlayed).when(spyRoom).getLastPlayedCard();

        Exception exception = assertThrows(badRequest.class, () -> gameService.takeTurn(
                "123", spyPlayer.getPid(), "Basic", "Red", "1", "", "false"
        ));
        assertThat(exception.getMessage(), is("Invalid card played"));
    }

    /**
     * Test endTurn with proper nextPlayer and playableCards set in gameResponse.
     */
    @Test
    public void testEndTurnGameResponse() {
        card toPlay = new basicCard(1, card.Color.Red);
        card lastPlayed = new basicCard(1, card.Color.Green);

        doReturn(spyRoom).when(gameService).getRoom("123");
        doReturn(lastPlayed).when(spyRoom).getLastPlayedCard();
        doReturn(true).when(spyPlayer).playCard(toPlay);

        // Set non-empty hand (doesn't matter what the cards are)
        doReturn(new ArrayList<>(Arrays.asList(toPlay, lastPlayed))).when(spyPlayer).getCardList();
        // Set next player's hand (card matters)
        player nextPlayer = players.get(1);
        doReturn(new ArrayList<>(Arrays.asList(
                new wildCard(card.CardType.Draw4),
                new basicCard(2, card.Color.Blue),
                new sColorCard(card.CardType.Skip, card.Color.Green)
        ))).when(nextPlayer).getCardList();

        gameResponse result = gameService.takeTurn(
                "123", spyPlayer.getPid(), "Basic", "Red", "1", "", "false"
        );
        // LastPlayed card should still be Green-1 because playCard was mocked
        assertThat(result.getPlayerTurnPid(), is(nextPlayer.getPid()));
        assertThat(result.getPlayableCards(), is(new ArrayList<>(Arrays.asList(
                new wildCard(card.CardType.Draw4), new sColorCard(card.CardType.Skip, card.Color.Green)))));
    }

//    /**
//     *
//     */
//    @Test
//    public void playCard() {
//
//    }
//
//    /**
//     * Verify gameResponse when player skips turn with no +4, +2 penalty and draws 1 playable card.
//     */
//    @Test
//    public void skipTurnWithNoPenaltyAndDrawOnePlayableCard() {
//
//    }
//
//    /**
//     * Verify gameResponse when player skips turn with no +4, +2 penalty and draws 1 non-playable card.
//     */
//    @Test
//    public void skipTurnWithNoPenaltyAndDrawNoPlayableCard() {
//
//    }
//
//    /**
//     * Verify gameResponse when player skips turn with playable +4, +2 penalty cards drawn.
//     */
//    @Test
//    public void skipTurnWithPlayablePenaltyCards() {
//
//    }
//
//    /**
//     * Verify gameResponse when player skips turn with no playable +4, +2 penalty cards drawn.
//     */
//    @Test
//    public void skipTurnWithNoPlayablePenaltyCard() {
//
//    }
//
//    // Checks proper gameResponse value for skipTurn
//    // 1) skipTurn => no penalty => draw 1 with playable cards
//    // 2) skipTurn => no penalty => draw 1 with no playable cards => endTurn see below
//    // 3) skipTurn => penalty => draws playable cards
//    // 4) skipTurn => penalty => no playable cards drawn => draw 1 => same as 1, 2
//
//    // Checks proper gameResponse value for endTurn
//    // 1) endTurn => if there's a winner => winResponse
//    // 2) endTurn => if there's no winner => nextRoundResponse
//    // 3) endTurn => if there's skip Turn => proper next player
//    // 4) endTurn => if no skip turn => proper next player
//    // 5) endTurn => if reverse => proper next player
//
//
//
//    @Test
//    public void endTurnWhenReverseCardPlayed() {
//
//    }
//
//    /**
//     *
//     */
//    @Test
//    public void endTurnWhenSkipCardPlayed() {
//
//    }
//
//
//
//
//    private void populatePlayersAndScores(ArrayList<Integer> scores) {
//        for (int i = 1; i < NUM_PLAYERS + 1; i++ ) {
//            player spyPlayer = spy(new player("player" + i));
//            playerList.add(spyPlayer);
//            playerScores.put(spyPlayer, scores.get(i - 1));
//        }
//    }
//
//    private void populateHandCards() {
//        for (int i = 0; i < NUM_PLAYERS; i++) {
//            doReturn(playersHandCards.get(i)).when(playerList.get(i)).getCardList();
//        }
//    }
    private void populateRoomWithSpyPlayers(room room) {
        player firstPlayer = spy(new player("player1"));
        room.addPlayer(firstPlayer);
        players.add(firstPlayer);
        room.shufflePlayers();

        for (int i = 1; i < NUM_PLAYERS; i++ ) {
            player spyPlayer = spy(new player("player" + (i + 1)));
            room.addPlayer(spyPlayer);
            players.add(spyPlayer);
        }
    }
}
