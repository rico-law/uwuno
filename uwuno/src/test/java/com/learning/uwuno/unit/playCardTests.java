package com.learning.uwuno.unit;

import com.learning.uwuno.cards.deck;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class playCardTests {
    @Mock
    deck deck;

    /**
     * No error occurs when non-wild card matches the colour of the current card in play.
     * TODO:
     *  - Colour matches
     *  - Check card removed from hand (1 less card, other cards not removed)
     *  - Check card replaces card in play
     */

    /**
     * No error occurs when non-wild card matches the card number or card type in play.
     * TODO:
     *  - Number matches
     *  - Special card of each type matches (i.e. skip, reverse, draw2)
     */

    /**
     * Exception occurs when card played does not match the colour of the current card in play.
     */

    /**
     * Exception occurs when card played does not match the number or card type of the current card in play.
     */

    /**
     * No error occurs when wild card is played and colour can be changed.
     * TODO:
     *  - Draw4 and changeColour can both be played on top of another black card and any coloured card
     *  - Put correct normal colour on top after change
     *  - Put wrong normal colour on top after change
     */

    /**
     * Tests skip turn card.
     */

    /**
     * Tests reverse card.
     */

    /**
     * Tests player draws penalty cards from draw2 and draw4 effects.
     */
}
