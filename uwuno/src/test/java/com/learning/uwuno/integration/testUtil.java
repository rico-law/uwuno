package com.learning.uwuno.integration;

import static com.learning.uwuno.integration.constants.BASE_URL;
import static com.learning.uwuno.integration.constants.JSON_REQUESTS_PATH;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

import java.io.FileNotFoundException;

public final class testUtil {
    private static final String postPutPlayerPath = JSON_REQUESTS_PATH + "/postPutPlayer.json";

    private testUtil() {
        // Should not run
    }

    // Create player - returns player response
    public static Response createPlayer(String playerName, String roomId) throws FileNotFoundException {
        String request = jsonUtil.createPostPutPlayerJson(playerName, postPutPlayerPath);
        return given().contentType(ContentType.JSON).pathParam("uid", roomId)
                .when().body(request).post(BASE_URL + "/rooms/{uid}/players")
                .then().extract().response();
    }
}
