package com.learning.uwuno.integration.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learning.uwuno.integration.jsonUtil;
import com.learning.uwuno.util.parser;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.learning.uwuno.integration.constants.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class setUpBasicRoomTest {

    private static String roomId;
    private static String playerId;

    @BeforeAll
    public static void createValidRoom() throws IOException {
        String filePath = JSON_REQUESTS_PATH + "/postRoomNoBlank.json";
        String request = jsonUtil.jsonFileToString(filePath);
        String requestName = new parser(request).getValue("roomName");

        Response response = given().contentType(ContentType.JSON)
                .when().body(request).post(BASE_URL + "/rooms")
                .then().extract().response();

        System.out.println("Request: " + request);
        System.out.println("Response: " + response.getBody().asString());

        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("uid"), is(not(emptyString())));
        assertThat(response.path("name"), is(equalTo(requestName)));
        assertThat(response.path("players"), is(empty()));

        roomId = response.path("uid");
        System.out.println("first fn: " + roomId);
    }

    @Test
    public void createValidFirstPlayer() throws IOException {
        String filePath = JSON_REQUESTS_PATH + "/postPlayer.json";
        String request = jsonUtil.jsonFileToString(filePath);
        String requestName = new parser(request).getValue("name");

        System.out.println("second fn: " + roomId);

        Response response = given().contentType(ContentType.JSON)
                .when().body(request).post(BASE_URL + "/rooms" + "/" + roomId + "/players")
                .then().extract().response();

        System.out.println("Request: " + request);
        System.out.println("Response: " + response.asString());

        assertThat(response.statusCode(), is(equalTo(200)));
        assertThat(response.path("pid"), is(not(emptyString())));
        assertThat(response.path("name"), is(equalTo(requestName)));
        assertThat(response.path("cardList"), is(empty()));

        playerId = response.path("pid");
    }

    @Test
    public void anotherTest() {
        System.out.println("3rd func: " + playerId);
        Response response = given().contentType(ContentType.JSON)
                .when().get(BASE_URL + "/rooms" + "/" + roomId)
                .then().extract().response();

        assertThat(response.statusCode(), is(equalTo(200)));

        System.out.println("3rd func: " + response.getBody().asString());
    }

    // TODO: write AfterEach for clean up
}
