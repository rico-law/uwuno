package com.learning.uwuno;

import com.learning.uwuno.cards.*;
import com.learning.uwuno.util.utils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
public class UwunoApplication {

    public static void main(String[] args) {
        // Need to pass application class to this and the args from the main to get this to work
        // Starts tomcat server and scans the rest of the path for sprint annotations
        SpringApplication.run(UwunoApplication.class, args);

        // Tests for checking previous functionaility breaks, need to test via debugger for now
//        deck testdeck = new deck(false, 5);
//        ArrayList<card> hand = testdeck.drawHand();
//        player testplayer = new player("test");
//        testplayer.setCurDeck(testdeck);
//        testplayer.drawCards(2);
//        card cardRemove = testplayer.getCardList().get(0);
//        testplayer.removeCard(cardRemove);
//        testdeck.addToDiscard(cardRemove);
//        card lastCard = testdeck.lastPlayedCard();
//        card.CardType type = lastCard.getType();
//        card.Color color = lastCard.getColor();
//
//        card testCreate = utils.inputToCard("Skip", "Blue", "");
//        card testCreate1 = utils.inputToCard("Skip", "Black", ""); // throws
//
//        card c1 = new basicCard(4, card.Color.Blue);
//        card c2 = new basicCard(2, card.Color.Green);
//        card c3 = new sColorCard(card.CardType.Draw2, card.Color.Blue);
//        card c4 = new wildCard(card.CardType.Draw4);
//
//        boolean t = utils.checkPlayable(c1, c1); // true
//        boolean t2 = utils.checkPlayable(c1, c2); // false
//        boolean t3 = utils.checkPlayable(c1, c3); // true
//        boolean t4 = utils.checkPlayable(c2, c3); // false
//        boolean t5 = utils.checkPlayable(c1, c4); // false
//        boolean t6 = utils.checkPlayable(c4, c1); // true
//
//        wildCard c5 = new wildCard(card.CardType.Draw4);
//        c5.setTempColor(card.Color.Blue);
//        wildCard c6 = new wildCard(card.CardType.Draw4);
//        c6.setTempColor(card.Color.Green);
//        boolean t7 = utils.checkPlayable(c5, c6); // true
//        boolean t8 = utils.checkPlayable(c4, c5); // true
//        boolean t9 = c5.equals(c6); // true
//
//        hand.add(new wildCard(card.CardType.Draw4));
//        hand.remove(c5); // Should work with overriden equals()
//
//        int f = 3;

        room r = new room("test", false);
        r.addPlayer(new player("1"));
        for (int i = 0; i < 4; i++) {
            r.addPlayer(new player("test"));
        }
        r.addPlayer(new player("2"));
        int f = 3;
    }
}
