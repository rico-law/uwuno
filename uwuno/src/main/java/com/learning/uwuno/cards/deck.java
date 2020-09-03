package com.learning.uwuno.cards;

import com.learning.uwuno.errors.badRequest;
import com.learning.uwuno.errors.internalServerError;

import java.util.*;

public class deck {
    // Constants
    final private int MAX_HAND_SIZE = 7;

    // Class variables
    // Assume the top of the list is the next card to be drawn
    private LinkedList<card> activeDeck;
    private ArrayList<card> discardPile;
    final private boolean useBlankCards;

    // Class Functions
    public deck(boolean useBlankCards) {
        activeDeck = createDeck();
        discardPile = new ArrayList<card>();
        this.useBlankCards = useBlankCards;
    }

    // Creates cards, fills active deck and shuffles it
    public LinkedList<card> createDeck() {
        LinkedList<card> deck = new LinkedList<card>();
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
                    deck.add(new sColorCard(card.CardType.Skip, color));
                    deck.add(new sColorCard(card.CardType.Reverse, color));
                    deck.add(new sColorCard(card.CardType.Draw2, color));
                }
            }
        }
        // Handle draw4's, changeColors and blanks
        for (int i = 0; i < 4; i++) {
            deck.add(new wildCard(card.CardType.Draw4));
            deck.add(new wildCard(card.CardType.ChangeColor));
            if (useBlankCards)
                deck.add(new wildCard(card.CardType.Blank));
        }
        Collections.shuffle(deck);
        return deck;
    }

    // Add discard pile to active deck and shuffle it
    public void reshuffle() {
        if (!discardPile.isEmpty()) {
            activeDeck.addAll(discardPile);
            Collections.shuffle(discardPile);
            discardPile.clear(); // Faster than removeAll
        }
    }

    // Draw numCard cards, assume first item in the ArrayList is the top card
    // don't ever use this function directly, use implementation inside gameService to ensure
    public ArrayList<card> drawCard(int numCards) {
        if (activeDeck.isEmpty() || activeDeck.size() < numCards)
            reshuffle();
        try {
            ArrayList<card> ret = new ArrayList<card>();
            for (int i = 0; i < numCards; i++) {
                ret.add(activeDeck.pop());
            }
            return ret;
        }
        catch (NoSuchElementException e) {
            // TODO: Discuss what happens here, this is technically possible?
            throw new internalServerError();
        }
    }

    // Should only ever be called once at the beginning of the game
    public ArrayList<card> drawHand() {
        // Starting hand draws 7 cards, subList returns a view, have to create a new copy with same contents
        ArrayList<card> ret = new ArrayList<card>();
        for (int i = 0; i < MAX_HAND_SIZE; i++) {
            ret.add(activeDeck.pop());
        }
        return ret;
    }

    // Place given card into the discard pile at the end of the list, does not perform
    // any checks whether the card is actually owned by the deck, should not handle game logic
    // TODO: Might need to find another way to do this, this doesn't guarantee destruction of
    //  previous card might cause debugging hell
    public void addToDiscard(card card) {
        discardPile.add(card);
    }

    public card lastPlayedCard() {
        if (!discardPile.isEmpty())
            return discardPile.get(discardPile.size() - 1);
        throw new badRequest();
    }
}
