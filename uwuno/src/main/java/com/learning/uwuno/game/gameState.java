package com.learning.uwuno.game;

import com.learning.uwuno.cards.basicCard;
import com.learning.uwuno.cards.card;
import com.learning.uwuno.cards.wildCard;
import com.learning.uwuno.player;
import com.learning.uwuno.room;
import com.learning.uwuno.util.playerList;

import java.util.ArrayList;
import java.util.HashMap;

public class gameState {
    private int cardsToDraw;    // Keeps track of +4, +2 stacking
    // TODO: increment turnsTaken
    private int turnsTaken;
    private boolean turnDirection; // true if forward, otherwise reverse direction
    private HashMap<player, Integer> scores;

    // To keep track of points and other current game properties
    public gameState(playerList playerList) {
        cardsToDraw = 0;
        turnsTaken = 0;
        turnDirection = true;
        scores = new HashMap<>();

        for (player player : playerList) {
            scores.put(player, 0);
        }
    }

    public void applyEffect(card.CardType type) {
        if (type == card.CardType.Draw2) {
            cardsToDraw += 2;
        } else if (type == card.CardType.Draw4) {
            cardsToDraw += 4;
        } else if (type == card.CardType.Reverse) {
            turnDirection = !turnDirection;
        }
    }

    public void skipTurn(player player, room room, gameResponse response) {
        // Check if there are extra cards to draw from stacking effect
        if (cardsToDraw > 0) {
            ArrayList<card> playableCards = new ArrayList<>();
            ArrayList<card> cards = player.drawCards(cardsToDraw);
            cards.addAll(player.getCardList());
            cardsToDraw = 0;
            // If the card is valid to play, return response with current pid and playable cards
            for (card card : cards) {
                if (checkPlayable(card, room.lastPlayedCard())) {
                    playableCards.add(card);
                }
            }
            // Need to send response here because player has not taken their turn yet. Can still draw 1 card to skip.
            response.setPlayerTurnResponse(player.getPid(), playableCards);
            return;
        }
        ArrayList<card> cards = player.drawCards(1);
        if (checkPlayable(cards.get(0), room.lastPlayedCard())) {
            response.setPlayerTurnResponse(player.getPid(), cards);
            // If card cannot be played, return response with next player's pid and their playable cards
            endTurn(player, room, response);
        }
    }

    // If the given player has won, return winning response
    // Otherwise, return response with next player's pid and their playable cards
    public void endTurn(player player, room room, gameResponse response) {
        gameSettings gameSettings = room.getGameSettings();
        if (player.getCardList().isEmpty() || turnsTaken == gameSettings.getMaxTurn()) {
            ArrayList<player> winner = gameSettings.getGameMode().determineWinner(player, room.getPlayers(),
                    scores, gameSettings.getMaxScore());
            if (winner.isEmpty()) {
                // TODO: have trigger somewhere to reset game state except scores and restart game (should only be for point mode)
                response.setNextRoundResponse(scores);
            } else {
                response.setWinResponse(player);
            }
        } else {
            player nextPlayer = room.nextPlayer(turnDirection);
            response.setPlayerTurnResponse(nextPlayer.getPid(), getPlayableCards(nextPlayer, room.lastPlayedCard()));
        }
    }

    public boolean playCard(card toPlay, card lastPlayed, player player) {
        // Add the player's card to discard pile and remove it from the player's hand
        if (checkPlayable(toPlay, lastPlayed) && player.playCard(toPlay)) {
            applyEffect(toPlay.getType());
            return true;
        }
        return false;
    }

    // Returns list of playable cards from given player's hand based on given last played card
    static public ArrayList<card> getPlayableCards(player player, card lastPlayed) {
        ArrayList<card> playableCards = new ArrayList<>();
        for (card card : player.getCardList()) {
            if (checkPlayable(card, lastPlayed)) {
                playableCards.add(card);
            }
        }
        return playableCards;
    }

    // Function to check if card is playable when compared to last played card
    static public boolean checkPlayable(card toPlay, card lastPlayed) {
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