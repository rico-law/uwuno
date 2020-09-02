package com.learning.uwuno.cards;

public class reverseCard implements card {
    // Class Variables
    CardType cardType = CardType.Reverse;
    Color color;

    public reverseCard(Color color) {
        this.color = color;
    }

    // Interface Functions
    public Color getColor() {
        return color;
    }

    public CardType getType() {
        return cardType;
    }

    public void playCard() {
    }
}
