package com.learning.uwuno.cards;

import java.util.ArrayList;
import java.util.Collections;

public class deck {
    // Class variables
    // Assume the top of the list is the next card to be drawn
    ArrayList<card> activeDeck;
    ArrayList<card> discardPile;
    boolean useBlank;

    // Class Functions
    public deck(boolean useBlank) {
        activeDeck = createDeck();
        discardPile = new ArrayList<card>();
        this.useBlank = useBlank;
    }

    // Creates cards, fills active deck and shuffles it
    public ArrayList<card> createDeck() {
        ArrayList<card> deck = new ArrayList<>();
        for (card.Color color : card.Color.values()) {
            if (color != card.Color.Black) {
                // Handle basic numeric cards
                for (int i = 0; i < 10; i++) {
                    deck.add(new basicCard(i, color));
                    if (i != 0)
                        deck.add(new basicCard(i, color));
                }
                // Handle skip, reverse and draw2's
                for (int i = 0; i < 2; i++) {
                    deck.add(new skipCard(color));
                    deck.add(new reverseCard(color));
                    deck.add(new draw2Card(color));
                }
            }
        }
        // Handle draw4's, changeColors and blanks
        for (int i = 0; i < 4; i++) {
            deck.add(new draw4Card());
            deck.add(new changeColorCard());
            if (useBlank)
                deck.add(new blankCard());
        }
        Collections.shuffle(deck);
        return deck;
    }

    // Shuffle Discard pile into activeDeck
    public void reshuffle() {
        if (!discardPile.isEmpty()) {
            Collections.shuffle(discardPile);
            activeDeck.addAll(discardPile);
            discardPile.clear(); // Faster than remove all
        }
    }
}
