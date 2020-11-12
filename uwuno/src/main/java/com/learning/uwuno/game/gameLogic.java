package com.learning.uwuno.game;

import com.learning.uwuno.cards.basicCard;
import com.learning.uwuno.cards.card;
import com.learning.uwuno.cards.wildCard;
import com.learning.uwuno.errors.badRequest;
import com.learning.uwuno.player;
import com.learning.uwuno.room;

import java.util.ArrayList;

public final class gameLogic {
    private gameLogic() {}

//    Rules:
//      - Match by number, colour or symbol/action. Or can play wildcard.
//      - Player can choose not to play their card OR if they can't play a card => will need to draw 1 from deck
//      - After drawing, if card can be played, they can play that card
//      - First card, reshuffle if not number cards
//      - Stacking: Can mix +2 and +4
//
//    Game modes:
//      - Normal game mode:
//          - First to get rid of all your cards win
//          - If reached maxTurn before finishing the game,
//              calculate scores based on [(number of points in your hand) / (player with the most points)] * 100
//          - Player with the least points wins
//      - Point mode (multiple rounds):
//          - When you get rid of all your cards, you get points equal to the cards left in other players' hands
//          - First to reach the set number of pts wins (e.g. 500 pts)
//          - If reached maxTurn before finishing the game, calculate pts equal to the cards in other players' hands
//
//    Scoring:
//      - All number cards (0-9) ... Face value
//      - Draw 2 ... 20 pts
//      - Reverse ... 20 pts
//      - Skip ... 20 pts
//      - Wild ... 50 pts
//      - Wild Draw 4 ... 50 pts

    static public void skipTurn(player player, room room, gameResponse response) {
        ArrayList<card> cards = player.drawCards(1);
        // If the card is valid to play, return response with current pid and drawn card
        if (checkPlayable(cards.get(0), room.lastPlayedCard())) {
            response.setPlayerTurnResponse(player.getPid(), cards);
            return;
        }
        // If card cannot be played, return response with next player's pid and their playable cards
        endTurn(player, room, response);
    }

    // If the given player has won, return winning response
    // Otherwise, return response with next player's pid and their playable cards
    static public void endTurn(player player, room room, gameResponse response) {
        // TODO: also check whether maxTurn has been reached
        if (player.getCardList().isEmpty()) {
            response.setWinResponse(player);
        } else {
            player nextPlayer = room.nextPlayer();
            response.setPlayerTurnResponse(nextPlayer.getPid(), getPlayableCards(nextPlayer, room.lastPlayedCard()));
        }
    }

    static public boolean playCard(card toPlay, card lastPlayed, player player) {
        // Add the player's card to discard pile and remove it from the player's hand
        return checkPlayable(toPlay, lastPlayed) && player.playCard(toPlay);
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
