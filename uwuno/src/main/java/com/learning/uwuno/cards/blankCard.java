package com.learning.uwuno.cards;

public class blankCard implements card {
    // Class Variables
    card.CardType cardType = CardType.Blank;
    card.Color color = card.Color.Black;

    public blankCard() {
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
