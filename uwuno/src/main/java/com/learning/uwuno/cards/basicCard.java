package com.learning.uwuno.cards;

public class basicCard implements card {
    // Variables
    final private CardType cardType = CardType.Basic;
    private Color color;
    private int value;

    // Class Functions
    public basicCard(int value, Color color) {
        this.value = value;
        this.color = color;
    }

    public int getValue() {
        return value;
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
