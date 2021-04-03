package com.learning.uwuno.cards;

import com.learning.uwuno.errors.badRequest;

// Handles all wild cards
public class wildCard extends card {
    // Class Variables
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

    @Override
    public int hashCode() {
        int result = 17;
        if (cardType != null)
            result = 31 * result + cardType.hashCode();
        if (color != null)
            result = 31 * result + color.hashCode();
        return result;
    }

    // Class Functions
    public wildCard(card.CardType cardType) {
        this.cardType = cardType;
        this.color = Color.Black;
    }

    public void setTempColor(card.Color color) {
        // Cannot set next card color to be black
        if (color != card.Color.Black)
            lastChosenColor = color;
        else
            throw new badRequest("Cannot set new color of Wild Card to black");
    }

    // Reset tempColour after sending to discardPile
    public void resetColor() {
        lastChosenColor = Color.Black;
    }

    // If this returns black, means first use of this card,
    // can use this to check if this is a new card if that is ever needed
    public card.Color getTempColor() {
        return lastChosenColor;
    }
}
