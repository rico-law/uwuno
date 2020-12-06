package com.learning.uwuno.integration.tests;

import static com.learning.uwuno.integration.testUtil.createPlayer;

import com.learning.uwuno.integration.testUtil;
import io.restassured.response.Response;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class startGameTest {
    private static String roomId;
    private static final String roomName = "room";
    private static final String testStatus = "Start";
    private static final int startingHandCardSize = 7;
    private static final int totalCards = 108;
    private static final int drawNumCards = 5;

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        roomId = testUtil.createRoom(roomName).path("uid");
    }

    @AfterEach
    public void cleanUp() {
        testUtil.deleteRoom(roomId);
    }

    // PUT request to Start game
    @Test
    public void putValidStartGame200() throws FileNotFoundException {
        createPlayer("player_1", roomId);
        createPlayer("player_2", roomId);
        Response response = testUtil.putRoom(roomName, roomId, testStatus);

        List<Object> cardList1 = response.jsonPath().getList("players[0].cardList");
        List<Object> cardList2 = response.jsonPath().getList("players[1].cardList");

        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("roomStatus"), is(equalTo(testStatus)));
        assertThat(response.path("activeDeckSize"), is(equalTo(totalCards - startingHandCardSize*2 - 1)));
        assertThat(response.path("lastPlayedCard"), is(not(equalTo(null))));
        assertThat(response.jsonPath().getList("discardPile").size(), is(equalTo(0)));
        assertThat(cardList1.size(), is(equalTo(startingHandCardSize)));
        assertThat(cardList2.size(), is(equalTo(startingHandCardSize)));
    }

    // PUT invalid Start game request - via no players
    @Test
    public void putStartNoPlayers400() throws FileNotFoundException {
        Response response = testUtil.putRoom(roomName, roomId, testStatus);
        Response getResponse = testUtil.getRoom(roomId);

        assertThat(response.statusCode(), is(equalTo(400)));
        assertThat(getResponse.path("roomStatus"), is("Lobby"));
    }

    // PUT draw card
    @Test
    public void putPlayerDrawCards200() throws FileNotFoundException {
        // Change room status to Start
        Response player1 = createPlayer("player_1", roomId);
        createPlayer("player_2", roomId);
        testUtil.putRoom(roomName, roomId, testStatus);

        // Request
        Response response = testUtil.playerDrawCards(String.valueOf(drawNumCards), roomId, player1.path("pid"));
        Response getResponse = testUtil.getRoom(roomId);

        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.jsonPath().getList("cardList").size(), is(equalTo(startingHandCardSize + drawNumCards)));
        assertThat(getResponse.path("activeDeckSize"), is(equalTo(totalCards - startingHandCardSize*2 - drawNumCards - 1)));
    }

    // PUT invalid draw card - via non-numeric number
    @Test
    public void putInvalidDrawCards400() throws FileNotFoundException {
        // Change room status to Start
        Response player1 = createPlayer("player_1", roomId);
        createPlayer("player_2", roomId);
        testUtil.putRoom(roomName, roomId, testStatus);

        // Request
        Response response = testUtil.playerDrawCards("invalid", roomId, player1.path("pid"));
        assertThat(response.statusCode(), is(equalTo(400)));
    }
}
