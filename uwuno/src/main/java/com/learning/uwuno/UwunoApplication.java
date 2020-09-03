package com.learning.uwuno;

import com.learning.uwuno.cards.card;
import com.learning.uwuno.cards.deck;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
public class UwunoApplication {

    public static void main(String[] args) {
        // Need to pass application class to this + the args from the main to get this to work
        // Starts tomcat server and scans the rest of the path for sprint annotations
        SpringApplication.run(UwunoApplication.class, args);

//        deck test = new deck(false);
//        card draw = test.drawCard();
//        int f = 3;
//        ArrayList<card> hand = test.drawHand();
//        int ff = 3;
    }
}
