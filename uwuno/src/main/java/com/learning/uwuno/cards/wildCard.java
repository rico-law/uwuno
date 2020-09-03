package com.learning.uwuno.cards;

// Handles all wild cards
public class wildCard implements card {
    // Class Variables
    final private card.CardType cardType;
    final private card.Color color = Color.Black;

    public wildCard(card.CardType cardType) {
        this.cardType = cardType;
    }

    // Interface Functions
    public card.Color getColor() {
        return color;
    }

    public card.CardType getType() {
        return cardType;
    }

    public void playCard() {
    }
}
