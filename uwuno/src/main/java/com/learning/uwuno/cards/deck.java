package com.learning.uwuno.cards;

import com.learning.uwuno.errors.internalServerError;

import java.util.*;

public class deck {
    // Class variables
    // Assume the top of the list is the next card to be drawn
    private final LinkedList<card> activeDeck;
    private final ArrayList<card> discardPile;
    final private boolean useBlankCards;
    private card lastCardPlayed;

    // Class Functions
    public deck(boolean useBlankCards) {
        activeDeck = createDeck();
        discardPile = new ArrayList<>();
        this.useBlankCards = useBlankCards;
    }

    public LinkedList<card> getActiveDeck() {
        return activeDeck;
    }

    public ArrayList<card> getDiscardPile() {
        return discardPile;
    }

    public boolean isUseBlankCards() {
        return useBlankCards;
    }

    public card getLastPlayedCard() {
        return lastCardPlayed;
    }

    // Creates cards, fills active deck and shuffles it
    public LinkedList<card> createDeck() {
        LinkedList<card> deck = new LinkedList<>();
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
    public synchronized void reshuffle() {
        if (!discardPile.isEmpty()) {
            activeDeck.addAll(discardPile);
            Collections.shuffle(discardPile);
            discardPile.clear(); // Faster than removeAll
        }
    }

    // Draw numCard cards, assume first item in the ArrayList is the top card
    // don't ever use this function directly, use implementation inside gameService to ensure
    // cards are removed from deck without moving it into a player's hand
    public synchronized ArrayList<card> drawCards(int numCards) {
        if (activeDeck.isEmpty() || activeDeck.size() < numCards)
            reshuffle();
        try {
            ArrayList<card> ret = new ArrayList<>();
            for (int i = 0; i < numCards; i++) {
                ret.add(activeDeck.pop());
            }
            return ret;
        }
        catch (NoSuchElementException e) {
            // Should never happen
            throw new internalServerError("Tried to access non-existent element in active deck");
        }
    }

    // Should use this function when starting a game as it correctly sets the lastCardPlayed
    public card drawStart() {
        card start =  activeDeck.pop();
        lastCardPlayed = start;
        discardPile.add(start);
        return start;
    }

    // Place given card into the discard pile at the end of the list, does not perform
    // any checks whether the card is actually owned by the deck, should not handle game logic
    // TODO: Might need to find another way to do this, this doesn't guarantee destruction of
    //  previous card might cause debugging hell
    public void addToDiscard(card card) {
        lastCardPlayed = card;
        discardPile.add(card);
    }
}
