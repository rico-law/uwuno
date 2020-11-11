package com.learning.uwuno;

import com.learning.uwuno.cards.basicCard;
import com.learning.uwuno.cards.card;
import com.learning.uwuno.cards.wildCard;
import com.learning.uwuno.errors.badRequest;
import com.learning.uwuno.errors.internalServerError;

import java.util.ArrayList;

public final class gameLogic {
    private gameLogic() {}

//     Rules:
//         - Match by number, colour or symbol/action. Or can play wildcard.
//         - Player can choose not to play their card OR if they can't play a card => will need to draw 1 from deck
//         - After drawing, if card can be played, they can play that card
//         - First card, reshuffle if not number cards
//         - Stacking: Can mix +2 and +4
//
//     Scoring:
//         - Normal game mode:
//                - When you get rid of all your cards, you get points equal to the cards left in other players' hands
//                - First to 500 pts is the winner
//         - Modified game mode (& for max turns):
//                - [(number of points in your hand) / (player with the most points)] * 100
//                - Player with the least points wins
//         - All number cards (0-9) ... Face value
//         - Draw 2 ... 20 pts
//         - Reverse ... 20 pts
//         - Skip ... 20 pts
//         - Wild ... 50 pts
//         - Wild Draw 4 ... 50 pts

    // TODO: have option for max turns and game mode: original UNO or point based UNO.
    //  If game ends by max turns, winner determined by points.

    static public void skipTurn(player player, room room, gameResponse response) {
        ArrayList<card> cards = player.drawCards(1);
        // If the card is valid to play, return response with current pid and drawn card
        if (checkPlayable(cards.get(0), room.lastPlayedCard())) {
            response.setPlayerTurnPid(player.getPid());
            response.setPlayableCards(cards);
            return;
        }
        // If card cannot be played, return response with next player's pid and their playable cards
        endTurn(room.getNextPlayer(), room, response);
    }

    static public void endTurn(player player, room room, gameResponse response) {
        response.setPlayerTurnPid(player.getPid());
        response.setPlayableCards(getPlayableCards(player, room.lastPlayedCard()));
        return;
    }

    static public boolean playCard(card toPlay, card lastPlayed, player player) {
        // Add the player's card to discard pile and remove it from the player's hand
        return checkPlayable(toPlay, lastPlayed) && player.playCard(toPlay);
    }

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
        // If current card is a black card, can always be played
        if (toPlay instanceof wildCard) {
            return true;
        }
        // Check if current and previous card has the same value
        else if (toPlay instanceof basicCard && lastPlayed instanceof basicCard &&
                ((basicCard) toPlay).getValue() == ((basicCard) lastPlayed).getValue()) {
            return true;
        }
        // If last played card is a wild card need to check the temp color
        else if (lastPlayed instanceof wildCard) {
            return ((wildCard) lastPlayed).getTempColor() == toPlay.getColor();
        }
        // Check if same color or same type, if same type the type must not be a basic numeric card
        else {
            return toPlay.getColor() == lastPlayed.getColor() ||
                    (toPlay.getType() == lastPlayed.getType() && lastPlayed.getType() != card.CardType.Basic);
        }
    }
}
