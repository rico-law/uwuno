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

    // Override Functions
    // Overrides equal to compare CardType, Colour and CardValue
    @Override
    public boolean equals(Object obj) {
        // Default checks
        if (this == obj)
            return true;
        else if (obj == null || getClass() != obj.getClass())
            return false;
        // Extra checks, need cardType, cardColor and cardValue to be the same
        basicCard toTest = (basicCard) obj;
        return this.cardType == toTest.getType() && this.color == toTest.getColor() && this.value == toTest.getValue();
    }
}
