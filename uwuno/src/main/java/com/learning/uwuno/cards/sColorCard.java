package com.learning.uwuno.cards;

// Class handles all special cards that are not black
public class sColorCard extends card {
    public sColorCard(card.CardType cardType, card.Color color) {
        this.cardType = cardType;
        this.color = color;
    }

    // Override Functions
    // Overrides equal to compare only CardType and Color
    @Override
    public boolean equals(Object obj) {
        // Default checks
        if (this == obj)
            return true;
        else if (obj == null || getClass() != obj.getClass())
            return false;
        // Extra checks, only need cardType and cardColor to be the same
        sColorCard toTest = (sColorCard) obj;
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
}
