package com.learning.uwuno.util;

import com.learning.uwuno.cards.*;
import com.learning.uwuno.errors.badRequest;

public final class utils {
    // Should not be instantiated
    private utils() {
        // Should not run
    }

    // Function to create the proper card interface given cardType, cardColor, cardValue as strings
    // This function is case sensitive, for exact qualifiers look in card.java
    static public card inputToCard(String cardType, String cardColor, String cardValue) {
        card.CardType type = card.CardType.valueOf(cardType);
        card.Color color = card.Color.valueOf(cardColor);
        if (!cardValue.isBlank()) {
            int value = Integer.parseInt(cardValue);
            if (value < 0 || value > 9)
                throw new badRequest();
            return new basicCard(value, color);
        }
        else {
            switch (type) {
                case Skip:
                case Reverse:
                case Draw2:
                    if (color != card.Color.Black)
                        return new sColorCard(type, color);
                    throw new badRequest();
                case Draw4:
                case ChangeColor:
                case Blank:
                    if (color == card.Color.Black)
                        return new wildCard(type);
                    throw new badRequest();
            }
        }
        throw new badRequest();
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
        // Check if same color or same type, if same type the type must not be a basic numeric card
        else {
            return toPlay.getColor() == lastPlayed.getColor() ||
                    (toPlay.getType() == lastPlayed.getType() && lastPlayed.getType() != card.CardType.Basic);
        }
    }
}
