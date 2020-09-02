package com.learning.uwuno.cards;

public class draw2Card implements card {
    // Class Variables
    card.CardType cardType = CardType.Draw2;
    card.Color color;

    public draw2Card(card.Color color) {
        this.color = color;
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
