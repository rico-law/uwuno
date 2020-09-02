package com.learning.uwuno.cards;

public class changeColorCard implements card {
    // Class Variables
    card.CardType cardType = CardType.ChangeColor;
    card.Color color = Color.Black;

    public changeColorCard() {
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
