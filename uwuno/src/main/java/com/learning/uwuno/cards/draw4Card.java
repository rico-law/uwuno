package com.learning.uwuno.cards;

public class draw4Card implements card {
    // Class Variables
    card.CardType cardType = card.CardType.Draw4;
    card.Color color = Color.Black;

    public draw4Card() {
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
