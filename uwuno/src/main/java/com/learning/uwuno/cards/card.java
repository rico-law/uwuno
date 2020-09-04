package com.learning.uwuno.cards;

public abstract class card {
    public enum CardType {
        Basic, // Numbers 1 "0", 2 of each other number
        Skip, // 2 of each color
        Reverse, // 2 of each color
        Draw2, // 2 of each color
        Draw4, // 4 of these
        ChangeColor, // 4 of these
        Blank // 4 of these, To implement additional house rules with
    }

    public enum Color {
        Blue,
        Green,
        Red,
        Yellow,
        Black // Assume all wild cards are of color black
    }

    // Class Variables
    CardType cardType;
    Color color;

    // Base Functions
    public CardType getType() {
        return cardType;
    }

    public Color getColor() {
        return color;
    }
}
