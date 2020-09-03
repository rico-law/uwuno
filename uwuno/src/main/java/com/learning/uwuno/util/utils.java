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
                    return new sColorCard(type, color);
                case Draw4:
                case ChangeColor:
                case Blank:
                    return new wildCards(type);
            }
        }
        throw new badRequest();
    }

    // Function to check if card is playable when compared to last played card
    static public boolean checkPlayable(card toPlay, card lastPlayed) {
        // Check if current and previous card has the same value
        if (toPlay instanceof basicCard && lastPlayed instanceof basicCard &&
                ((basicCard) toPlay).getValue() == ((basicCard) lastPlayed).getValue()) {
            return true;
        }
        else {
            return toPlay.getColor() == lastPlayed.getColor() || toPlay.getType() == lastPlayed.getType();
        }
    }
}
