package com.learning.uwuno.unit;

import com.learning.uwuno.cards.*;
import com.learning.uwuno.game.gameState;
import com.learning.uwuno.player;
import com.learning.uwuno.util.playerList;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class playerAndDeckTests {
    private playerList playerList = new playerList("123");
    private gameState gameState;
    private deck spyDeck;
    private player spyPlayer;
    private final ArrayList<card> handCards = new ArrayList<>(Arrays.asList(
            new basicCard(3, card.Color.Yellow),
            new basicCard(4, card.Color.Green),
            new basicCard(5, card.Color.Blue),
            new sColorCard(card.CardType.Reverse, card.Color.Blue),
            new wildCard(card.CardType.Draw4)
    ));

    @BeforeEach
    public void setUp() {
        spyPlayer = spy(new player("player1"));
        spyDeck = spy(new deck(false));
        spyPlayer.setCurDeck(spyDeck);

        // Set up hand cards (for player playCard tests)
        doReturn(handCards).when(spyDeck).drawCards(handCards.size());
        ArrayList<card> drawnCards = spyPlayer.drawCards(handCards.size());
        assertThat(drawnCards, is(handCards));

        // Set up gameState and populate playerList with mock player (for checkPlayable tests)
        playerList.add(spyPlayer);
        gameState = new gameState(playerList);
    }

    /**
     * Test player can play card they have from hand.
     */
    @Test
    public void testPlayerCanPlayCardFromHand() {
        boolean result = spyPlayer.playCard(new basicCard(3, card.Color.Yellow));
        ArgumentCaptor<card> argument = ArgumentCaptor.forClass(card.class);

        assertThat(result, is(true));
        verify(spyDeck, times(1)).addToDiscard(argument.capture());
        assertThat(spyPlayer.getCardList().size(), is(4));
        assertThat(spyDeck.getLastPlayedCard(), is(argument.getValue()));
    }

    /**
     * Test player cannot play card they do not have.
     */
    @Test
    public void testPlayerCannotPlayNonexistentCardFromHand() {
        // Play card that does not exist in hand
        boolean result = spyPlayer.playCard(new basicCard(2, card.Color.Yellow));
        ArgumentCaptor<card> argument = ArgumentCaptor.forClass(card.class);

        assertThat(result, is(false));
        verify(spyDeck, times(0)).addToDiscard(argument.capture());
        assertThat(spyPlayer.getCardList().size(), is(5));
    }

    /**
     * Test card can be played when compared to lastPlayed card.
     */
    @Test
    public void testCardCanBePlayedOverLastPlayedCard() {
        card toPlay = new basicCard(3, card.Color.Yellow);
        wildCard lastPlayed = new wildCard(card.CardType.ChangeColor);
        lastPlayed.setTempColor(card.Color.Yellow);;
        boolean result = gameState.checkPlayable(toPlay, lastPlayed);

        assertThat(result, is(true));
    }

    /**
     * Test card cannot be played when compared to lastPlayed card.
     */
    @Test
    public void testCardCannotBePlayedOverLastPlayedCard() {
        card toPlay = new basicCard(3, card.Color.Yellow);
        card lastPlayed = new sColorCard(card.CardType.Draw2, card.Color.Green);
        boolean result = gameState.checkPlayable(toPlay, lastPlayed);

        assertThat(result, is(false));
    }
}
