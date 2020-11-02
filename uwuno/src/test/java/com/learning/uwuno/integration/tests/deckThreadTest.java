package com.learning.uwuno.integration.tests;

import com.learning.uwuno.*;
import com.learning.uwuno.cards.deck;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class deckThreadTest {
    private final static int NUM_THREAD = 100;
    private final LinkedList<player> players = new LinkedList<>();
    private deck deck;

    @BeforeEach
    public void setUp() {
        deck = new deck(false);
        IntStream.range(0, 10).forEach(i -> {
            player p = new player(String.valueOf(i));
            p.setCurDeck(deck);
            players.add(p);
        });
    }

    @AfterEach
    public void cleanUp() {
        players.clear();
    }

    @Test
    public void singlePlayerDrawCard() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(NUM_THREAD);
        IntStream.range(0, 108)
                .forEach(i -> service.execute(() -> players.get(0).drawCards(1)));
        service.awaitTermination(1000, TimeUnit.MILLISECONDS);
        assertThat(deck.getActiveDeck().size(), is(0));
        assertThat(players.get(0).getCardList().size(), is(108));
    }

    @Test
    public void multiplePlayersDrawCard() throws InterruptedException {
        ArrayList<Thread> threads = new ArrayList<>();
        players
                .forEach(p -> threads.add(new Thread(() -> IntStream.range(0, 10)
                .forEach(i -> p.drawCards(1)))));
        threads.forEach(Thread::start);
        Thread.sleep(1000);
        assertThat(deck.getActiveDeck().size(), is(8));
        players.forEach(p -> assertThat(p.getCardList().size(), is(10)));
    }

    @Test
    public void singlePlayerPlayCard() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(NUM_THREAD);
        IntStream.range(0, 108).forEach(i -> service.execute(() -> {
            players.get(0).drawCards(1);
            players.get(0).playCard(players.get(0).getCardList().get(0));
        }));
        service.awaitTermination(1000, TimeUnit.MILLISECONDS);
        assertThat(deck.getActiveDeck().size(), is(0));
        assertThat(deck.getDiscardPile().size(), is(108));
        assertThat(players.get(0).getCardList().size(), is(0));
    }

//    @Test
//    public void multiplePlayersPlayCard() throws InterruptedException {
//        ArrayList<Thread> threads = new ArrayList<>();
//        players.forEach(p -> threads.add(new Thread(() -> IntStream.range(0, 10).forEach(i -> p.drawCards(1)))));
//        threads.forEach(Thread::start);
//        Thread.sleep(1000);
//        assertThat(deck.getActiveDeck().size(), is(8));
//        players.forEach(p -> assertThat(p.getCardList().size(), is(10)));
//    }
}