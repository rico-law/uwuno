package com.learning.uwuno.unit;

import com.learning.uwuno.cards.*;
import com.learning.uwuno.game.*;
import com.learning.uwuno.player;
import com.learning.uwuno.room;
import com.learning.uwuno.util.playerList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
public class gameStatePlayCardTests {
    @Mock private player mockPlayer;
    @Mock gameResponse mockResponse;

    private gameState gameState;
    private final playerList playerList = new playerList("123");
    private room spyRoom;
    private player spyPlayer;
    private gameSettings spySettings;

    @BeforeEach
    public void setUp() {
        playerList.add(mockPlayer);
        gameState = new gameState(playerList);

        spyRoom = spy(new room("room123", false, "111"));
        populateRoomWithPlayers(spyRoom);
        spyPlayer = spy(spyRoom.getPlayers().cur());
        spySettings = spy(spyRoom.getGameSettings());
    }

    /**
     * Tests gameState.playCard().
     * No error occurs when non-wild card matches the colour of the current card in play.
     * TODO:
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
        card toPlay = new sColorCard(card.CardType.Skip, card.Color.Yellow);
        card lastPlayed = new basicCard(2, card.Color.Yellow);

        doReturn(true).when(spyPlayer).playCard(toPlay);
        doReturn(new ArrayList<>(Arrays.asList(toPlay, lastPlayed))).when(spyPlayer).getCardList();
        doReturn(spySettings).when(spyRoom).getGameSettings();
        doReturn(30).when(spySettings).getMaxTurn();

        boolean result = gameState.playCard(toPlay, lastPlayed, spyPlayer);
        assertThat(result, is(true));

        gameState.endTurn(spyPlayer, spyRoom, mockResponse);
        verify(spyRoom, times(2)).nextPlayer(true);
        assertThat(spyRoom.getPlayerTurn().getName(), is("player3"));
    }

    /**
     * Tests reverse card effect applied.
     */
    @Test
    public void testReverseCardEffectApplies() {
        card toPlay = new sColorCard(card.CardType.Reverse, card.Color.Yellow);
        card lastPlayed = new basicCard(2, card.Color.Yellow);

        doReturn(true).when(spyPlayer).playCard(toPlay);
        doReturn(new ArrayList<>(Arrays.asList(toPlay, lastPlayed))).when(spyPlayer).getCardList();
        doReturn(spySettings).when(spyRoom).getGameSettings();
        doReturn(30).when(spySettings).getMaxTurn();

        boolean result = gameState.playCard(toPlay, lastPlayed, spyPlayer);
        assertThat(result, is(true));

        gameState.endTurn(spyPlayer, spyRoom, mockResponse);
        verify(spyRoom, times(1)).nextPlayer(false);
        assertThat(spyRoom.getPlayerTurn().getName(), is("player4"));
    }

    /**
     * Tests player draws penalty cards from draw2 and draw4 effects.
     */
    @Test
    public void testDraw2Draw4CardEffectApply() {
        card firstToPlay = new sColorCard(card.CardType.Draw2, card.Color.Yellow);
        card secondToPlay = new wildCard(card.CardType.Draw4);
        card lastPlayed = new basicCard(2, card.Color.Yellow);
        doReturn(true).when(spyPlayer).playCard(firstToPlay);
        doReturn(true).when(spyPlayer).playCard(secondToPlay);
        gameState.playCard(firstToPlay, lastPlayed, spyPlayer);
        gameState.playCard(secondToPlay, lastPlayed, spyPlayer);

        doReturn(new ArrayList<>(Arrays.asList(lastPlayed, lastPlayed))).when(spyPlayer).drawCards(6);
        doReturn(lastPlayed).when(spyRoom).getLastPlayedCard();

        gameState.skipTurn(spyPlayer, spyRoom, mockResponse);
        verify(spyPlayer, times(1)).drawCards(6);
    }

    private void populateRoomWithPlayers(room room) {
        for (int i = 1; i < 5; i++ ) {
            room.addPlayer(new player("player" + i));
        }
    }
}
