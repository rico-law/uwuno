package com.learning.uwuno.game;

import com.learning.uwuno.cards.basicCard;
import com.learning.uwuno.cards.card;
import com.learning.uwuno.cards.wildCard;
import com.learning.uwuno.player;
import com.learning.uwuno.room;
import com.learning.uwuno.util.playerList;

import java.util.*;

/**
 * State of the game per round. Keeps track of:
 *
 * - The players' cumulative score at the end of each round,
 * - Number of turns taken in the current round,
 * - Whether the turn order is clockwise or counterclockwise and
 * - The number of cards to draw from the +4, +2 stacking effect.
 *
 * After each round, all properties except ```scores``` are reset.
 */
public class gameState {
    private int cardsToDraw;    // Keeps track of +4, +2 stacking
    private int turnsTaken;
    private boolean turnClockwise; // true if forward, otherwise reverse direction
    private boolean skipNextPlayer;
    private final HashMap<player, Integer> scores;

    public int getCardsToDraw() {
        return cardsToDraw;
    }

    public int getTurnsTaken() {
        return turnsTaken;
    }

    public boolean isTurnClockwise() {
        return turnClockwise;
    }

    public HashMap<player, Integer> getScores() {
        return scores;
    }

    /**
     * gameState constructor. To keep track of points and other current game properties.
     * @param playerList: playerList
     */
    public gameState(playerList playerList) {
        cardsToDraw = 0;
        turnsTaken = 0;
        turnClockwise = true;
        skipNextPlayer = false;
        scores = new HashMap<>();

        for (player player : playerList) {
            scores.put(player, 0);
        }
    }

    /**
     * For Point Mode: resets game state fields except scores.
     */
    public void resetRound() {
        cardsToDraw = 0;
        turnsTaken = 0;
        turnClockwise = true;
        skipNextPlayer = false;
    }

    /**
     * Apply +4, +2 or reverse card effects.
     * @param type: CardType (i.e. Draw2, Draw4 or Reverse)
     */
    public void applyCardEffect(card.CardType type) {
        if (type == card.CardType.Draw2) {
            cardsToDraw += 2;
        } else if (type == card.CardType.Draw4) {
            cardsToDraw += 4;
        } else if (type == card.CardType.Reverse) {
            turnClockwise = !turnClockwise;
        } else if (type == card.CardType.Skip) {
            skipNextPlayer = true;
        }
    }

    /**
     * Update given gameResponse when player skips their turn.
     * - Apply penalty if any (i.e. +4, +2). If the drawn cards are playable, update response with those cards.
     * - Otherwise, draw 1 card and update response with the card if playable.
     * - If no drawn cards are playable, end turn.
     * @param player: player of current turn
     * @param room: room
     * @param response: gameResponse
     */
    public void skipTurn(player player, room room, gameResponse response) {
        // Check if there are extra cards to draw from stacking effect
        if (cardsToDraw > 0) {
            ArrayList<card> playableCards = new ArrayList<>();
            ArrayList<card> cards = player.drawCards(cardsToDraw);
            cards.addAll(player.getCardList());
            cardsToDraw = 0;
            // If the card is valid to play, return response with current pid and playable cards
            // If there are no valid cards to play, draw 1 more card.
            for (card card : cards) {
                if (checkPlayable(card, room.getLastPlayedCard())) {
                    playableCards.add(card);
                }
            }
            if (!playableCards.isEmpty()) {
                response.setPlayerTurnResponse(player.getPid(), playableCards);
                return;
            }
        }
        ArrayList<card> cards = player.drawCards(1);
        if (checkPlayable(cards.get(0), room.getLastPlayedCard()))
            response.setPlayerTurnResponse(player.getPid(), cards);
        else
            // If card cannot be played, return response with next player's pid and their playable cards
            endTurn(player, room, response);
    }

    /**
     * Update given gameResponse when player ends their turn.
     * - If player has no cards or max turn have been taken, update response to reset round or determine winners.
     * - Otherwise, update response with next player's pid and playable cards.
     * @param player: player of current turn
     * @param room: room
     * @param response: gameResponse
     */
    public void endTurn(player player, room room, gameResponse response) {
        ++turnsTaken;
        gameSettings gameSettings = room.getGameSettings();
        // If the given player has won, return winning response
        if (player.getCardList().isEmpty() || turnsTaken == gameSettings.getMaxTurn()) {
            ArrayList<player> winners = gameSettings.getGameMode().determineWinner(player, room.getPlayers(),
                    scores, gameSettings.getMaxScore());
            if (winners.isEmpty()) {
                // No winners should only happen in Point Mode
                // TODO: better way to trigger reset game state (except scores) for next round?
                //  Right now, using PUT room gameStatus "Restart" to indicate restarting round
                response.setNextRoundResponse(scores);
            } else {
                response.setWinResponse(winners);
            }
        } else {
            // Otherwise, return response with next player's pid and their playable cards
            // Check whether "Skip Turn" effect should be applied
            player nextPlayer = room.nextPlayer(turnClockwise);
            nextPlayer = (skipNextPlayer) ? room.nextPlayer(turnClockwise) : nextPlayer;
            skipNextPlayer = false;
            response.setPlayerTurnResponse(nextPlayer.getPid(), getPlayableCards(nextPlayer, room.getLastPlayedCard()));
        }
    }

    /**
     * Play given card toPlay and it apply its effect if applicable.
     * @param toPlay: card
     * @param lastPlayed: card
     * @param player: player
     * @return true if card is playable, else false.
     */
    public boolean playCard(card toPlay, card lastPlayed, player player) {
        // Add the player's card to discard pile and remove it from the player's hand
        if (checkPlayable(toPlay, lastPlayed) && player.playCard(toPlay)) {
            applyCardEffect(toPlay.getType());
            return true;
        }
        return false;
    }

    /**
     * @param player: player
     * @param lastPlayed: card
     * @return ArrayList of playable cards from given player's hand based on given last played card.
     */
    public ArrayList<card> getPlayableCards(player player, card lastPlayed) {
        ArrayList<card> playableCards = new ArrayList<>();
        for (card card : player.getCardList()) {
            if (checkPlayable(card, lastPlayed)) {
                playableCards.add(card);
            }
        }
        return playableCards;
    }

    /**
     * Check whether given toPlay card is playable when compared to given lastPlayed card.
     * @param toPlay: card
     * @param lastPlayed: card
     * @return true if card is playable, else false.
     */
    public boolean checkPlayable(card toPlay, card lastPlayed) {
        // Get colour of last played card
        card.Color lastPlayedColour = (lastPlayed instanceof wildCard) ?
                ((wildCard) lastPlayed).getTempColor() : lastPlayed.getColor();
        // If current card is an unset black card, can always be played. Otherwise, check whether the colour matches.
        if (toPlay instanceof wildCard && ((wildCard) toPlay).getTempColor() == card.Color.Black ||
                toPlay instanceof wildCard && ((wildCard) toPlay).getTempColor() == lastPlayedColour) {
            return true;
        }
        // Check if current and previous card has the same value
        else if (toPlay instanceof basicCard && lastPlayed instanceof basicCard &&
                ((basicCard) toPlay).getValue() == ((basicCard) lastPlayed).getValue()) {
            return true;
        }
        // Check if same color or same type, if same type the type must not be a basic numeric card
        else {
            return toPlay.getColor() == lastPlayedColour ||
                    (toPlay.getType() == lastPlayed.getType() && lastPlayed.getType() != card.CardType.Basic);
        }
    }
}