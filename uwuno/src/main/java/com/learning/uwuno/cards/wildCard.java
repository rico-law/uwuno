package com.learning.uwuno.cards;

import com.learning.uwuno.errors.badRequest;

// Handles all wild cards
public class wildCard implements card {
    // Class Variables
    final private card.CardType cardType;
    final private card.Color color = Color.Black;
    // Variable to keep track of the color chosen by user when played
    private card.Color lastChosenColor = Color.Black;

    // Override Functions
    // Overrides equal to compare only CardType and Color, ie ignore lastChosenColor
    @Override
    public boolean equals(Object obj) {
        // Default checks
        if (this == obj)
            return true;
        else if (obj == null || getClass() != obj.getClass())
            return false;
        // Extra checks, only need cardType and cardColor to be the same, lastChosenColor can be anything
        wildCard toTest = (wildCard) obj;
        return this.cardType == toTest.getType() && this.color == toTest.getColor();
    }

    // Class Functions
    public wildCard(card.CardType cardType) {
        this.cardType = cardType;
    }

    public void setTempColor(card.Color color) {
        // Cannot set next card color to be black
        if (color != card.Color.Black)
            lastChosenColor = color;
        else
            throw new badRequest();
    }

    // If this returns black, means first use of this card,
    // can use this to check if this is a new card if that is ever needed
    public card.Color getTempColor() {
        return lastChosenColor;
    }

    // Interface Functions
    public card.Color getColor() {
        return color;
    }

    public card.CardType getType() {
        return cardType;
    }
}
