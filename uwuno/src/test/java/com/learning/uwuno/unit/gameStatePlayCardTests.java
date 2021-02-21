package com.learning.uwuno.unit;

import com.learning.uwuno.cards.*;
import com.learning.uwuno.game.gameState;
import com.learning.uwuno.player;
import com.learning.uwuno.util.playerList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class gameStatePlayCardTests {
    @Mock private player mockPlayer;

    private gameState gameState;
    private final playerList playerList = new playerList("123");

    @BeforeEach
    public void setUp() {
        playerList.add(mockPlayer);
        gameState = new gameState(playerList);
    }

    /**
     * Tests gameState.playCard().
     * No error occurs when non-wild card matches the colour of the current card in play.
     * TODO:
     *  - Colour matches//
     *  - Check card removed from hand (1 less card, other cards not removed)
     *  - Check card replaces card in play
     */
    @Test
    public void testNoErrorWhenCardColourMatches() {
        card toPlay = new basicCard(3, card.Color.Yellow);
        card lastPlayed = new sColorCard(card.CardType.Reverse, card.Color.Yellow);
        when(mockPlayer.playCard(toPlay)).thenReturn(true);

        boolean result = gameState.playCard(toPlay, lastPlayed, mockPlayer);
        assertThat(result, is(true));
    }

    /**
     * Tests gameState.playCard().
     * No error occurs when non-wild card matches the card number or card type in play.
     */
    @Test
    public void testNoErrorWhenCardNumberOrTypeMatches() {
        card basicToPlay = new basicCard(2, card.Color.Green);
        card basicLastPlayed = new basicCard(2, card.Color.Yellow);
        when(mockPlayer.playCard(basicToPlay)).thenReturn(true);

        card cardTypeToPlay = new sColorCard(card.CardType.Reverse, card.Color.Green);
        card cardTypeLastPlayed = new sColorCard(card.CardType.Reverse, card.Color.Yellow);
        when(mockPlayer.playCard(cardTypeToPlay)).thenReturn(true);

        boolean basicCardResult = gameState.playCard(basicToPlay, basicLastPlayed, mockPlayer);
        boolean cardTypeResult = gameState.playCard(cardTypeToPlay, cardTypeLastPlayed, mockPlayer);
        assertThat(basicCardResult, is(true));
        assertThat(cardTypeResult, is(true));
    }

    /**
     * Test return false when card played does not match the colour of the current card in play.
     */
    @Test
    public void testFalseWhenMismatchCardColour() {
        card toPlay = new basicCard(3, card.Color.Yellow);
        card lastPlayed = new sColorCard(card.CardType.Reverse, card.Color.Green);

        boolean result = gameState.playCard(toPlay, lastPlayed, mockPlayer);
        assertThat(result, is(false));
    }

    /**
     * Test return false when card played does not match the number or card type of the current card in play.
     */
    @Test
    public void testFalseWhenMismatchCardNumberOrType() {
        card basicToPlay = new basicCard(3, card.Color.Green);
        card basicLastPlayed = new basicCard(2, card.Color.Yellow);

        card cardTypeToPlay = new sColorCard(card.CardType.Draw2, card.Color.Green);
        card cardTypeLastPlayed = new sColorCard(card.CardType.Reverse, card.Color.Yellow);

        boolean basicCardResult = gameState.playCard(basicToPlay, basicLastPlayed, mockPlayer);
        boolean cardTypeResult = gameState.playCard(cardTypeToPlay, cardTypeLastPlayed, mockPlayer);
        assertThat(basicCardResult, is(false));
        assertThat(cardTypeResult, is(false));
    }

    /**
     * No error occurs when wild card is played and colour can be changed.
     * TODO:
     *  - Draw4 and changeColour can both be played on top of another black card and any coloured card //
     *  - Put correct normal colour on top after change
     *  - Put wrong normal colour on top after change
     */
    @Test
    public void testNoErrorWhenPlayWildCard() {
        card toPlay = new wildCard(card.CardType.Draw4);
        card lastPlayed = new sColorCard(card.CardType.Reverse, card.Color.Yellow);
        when(mockPlayer.playCard(toPlay)).thenReturn(true);

        boolean result = gameState.playCard(toPlay, lastPlayed, mockPlayer);
        assertThat(result, is(true));
    }

    /**
     * No error occurs when correct coloured card place on top of wild card set to the same colour.
     */
    @Test
    public void testNoErrorWhenCorrectColourPlacedOnWildCardAfterColourChange() {
        card toPlay = new sColorCard(card.CardType.Reverse, card.Color.Yellow);
        wildCard lastPlayed = new wildCard(card.CardType.Draw4);
        lastPlayed.setTempColor(card.Color.Yellow);
        when(mockPlayer.playCard(toPlay)).thenReturn(true);

        boolean result = gameState.playCard(toPlay, lastPlayed, mockPlayer);
        assertThat(result, is(true));
    }

    /**
     * Test return false when wrong coloured card place on top of wild card set to a different colour.
     */
    @Test
    public void testFalseWhenWrongColourPlacedOnWildCardAfterColourChange() {
        card toPlay = new sColorCard(card.CardType.Reverse, card.Color.Yellow);
        wildCard lastPlayed = new wildCard(card.CardType.Draw4);
        lastPlayed.setTempColor(card.Color.Red);

        boolean result = gameState.playCard(toPlay, lastPlayed, mockPlayer);
        assertThat(result, is(false));
    }

    /**
     * Tests skip turn card effect applied.
     */
    @Test
    public void testSkipCardEffectApplies() {
//        card toPlay = new sColorCard(card.CardType.Skip, card.Color.Yellow);
//        card lastPlayed = new basicCard(2, card.Color.Yellow);
//        when(mockPlayer.playCard(toPlay)).thenReturn(true);
//
//        boolean result = gameState.playCard(toPlay, lastPlayed, mockPlayer);
//        assertThat(result, is(true));
    }

    /**
     * Tests reverse card effect applied.
     */

    /**
     * Tests player draws penalty cards from draw2 and draw4 effects.
     */
}
