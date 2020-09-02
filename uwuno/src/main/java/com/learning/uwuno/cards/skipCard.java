package com.learning.uwuno.cards;

public class skipCard implements card {
    // Class Variables
    CardType cardType = CardType.Skip;
    Color color;

    public skipCard(Color color) {
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
