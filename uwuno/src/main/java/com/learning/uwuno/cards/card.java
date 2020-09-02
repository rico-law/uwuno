package com.learning.uwuno.cards;

interface card {
    enum CardType {
        Basic, // Numbers 1 "0", 2 of each other number
        Skip, // 2 of each color
        Reverse, // 2 of each color
        Draw2, // 2 of each color
        Draw4, // 4 of these
        ChangeColor, // 4 of these
        Blank // 4 of these - to implement additional rules with
    }

    enum Color {
        Blue,
        Green,
        Red,
        Yellow,
        Black // Assume all wild cards are of color black
    }

    // Interface Functions
    public Color getColor();
    public CardType getType();
    public void playCard();
}
