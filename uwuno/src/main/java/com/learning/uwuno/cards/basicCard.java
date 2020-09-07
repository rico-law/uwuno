package com.learning.uwuno.cards;

public class basicCard extends card {
    // Variables
    private int value;

    // Class Functions
    public basicCard(int value, Color color) {
        this.cardType = CardType.Basic;
        this.color = color;
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
