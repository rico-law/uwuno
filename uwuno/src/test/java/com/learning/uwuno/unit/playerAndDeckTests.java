package com.learning.uwuno.unit;

import com.learning.uwuno.cards.*;
import com.learning.uwuno.cards.deck;
import com.learning.uwuno.player;

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
    }

    /**
     * Test player can play card from hand.
     */
    @Test
    public void testPlayerCanPlayCardFromHand() {
        // Set up hand cards
        doReturn(handCards).when(spyDeck).drawCards(handCards.size());
        ArrayList<card> drawnCards = spyPlayer.drawCards(handCards.size());
        assertThat(drawnCards, is(handCards));

        boolean result = spyPlayer.playCard(new basicCard(3, card.Color.Yellow));
        assertThat(result, is(true));
        ArgumentCaptor<card> argument = ArgumentCaptor.forClass(card.class);
        verify(spyDeck, times(1)).addToDiscard(argument.capture());
        assertThat(spyPlayer.getCardList().size(), is(4));
        assertThat(spyDeck.getLastPlayedCard(), is(argument.getValue()));
    }

    /**
     * Test player cannot play card they do not have.
     */
    @Test
    public void testPlayerCannotPlayNonexistentCardFromHand() {
        boolean result = spyPlayer.playCard(new basicCard(2, card.Color.Yellow));
        assertThat(result, is(false));
        //TODO: lmao figure out how to mock arguments properly
        ArgumentCaptor<card> argument = ArgumentCaptor.forClass(card.class);
        verify(spyDeck, times(0)).addToDiscard(argument.capture());
    }
}
