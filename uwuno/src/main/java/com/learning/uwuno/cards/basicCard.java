package com.learning.uwuno.cards;

public class basicCard extends card {
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
}
