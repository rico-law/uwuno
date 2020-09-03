package com.learning.uwuno.cards;

// Class handles all special cards that are not black
public class sColorCard implements card {
    // Class Variables
    final private card.CardType cardType;
    final private card.Color color;

    public sColorCard(card.CardType cardType, card.Color color) {
        this.cardType = cardType;
        this.color = color;
    }

    // Interface Functions
    public card.Color getColor() {
        return color;
    }

    public card.CardType getType() {
        return cardType;
    }
}
