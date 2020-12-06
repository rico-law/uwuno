package com.learning.uwuno.integration;

import io.micrometer.core.instrument.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;

public final class jsonUtil {
    private static final String ROOM_NAME = "$roomName";
    private static final String ROOM_UID = "$roomUid";
    private static final String ROOM_STATUS = "$roomStatus";
    private static final String BLANK_CARDS = "$useBlankCards";
    private static final String PLAYER_NAME = "$playerName";
    private static final String NUM_CARDS = "$numCards";
    private static final String CARD_TYPE = "$cardType";
    private static final String CARD_COLOR = "$cardColor";
    private static final String CARD_VALUE = "$cardValue";
    private static final String SET_WILD_COLOR = "$setWildCard";
    private static final String SKIP = "$skip";
    private static final String GAME_MODE = "$gameMode";
    private static final String MAX_TURN = "$maxTurn";
    private static final String MAX_SCORE = "$maxScore";

    private jsonUtil() {
        // Should not run
    }
    public static String jsonFileToString(String jsonPath) throws FileNotFoundException {
        File file = new File(jsonPath);
        FileInputStream inputStream = new FileInputStream(file);
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }

    // Create Room JSON String
    public static String createPostRoomJson(String roomName, String useBlank, String jsonPath)
            throws FileNotFoundException {
        return jsonFileToString(jsonPath).replace(ROOM_NAME, roomName)
                .replace(BLANK_CARDS, useBlank);
    }

    public static String createPutRoomJson(String roomName, String uid, String status, String jsonPath)
            throws FileNotFoundException {
        return jsonFileToString(jsonPath).replace(ROOM_NAME, roomName).replace(ROOM_UID, uid)
                .replace(ROOM_STATUS, status);
    }

    public static String createPutGameSettings(String uid, String gameMode, String maxTurn,
                                               String maxScore, String useBlank, String jsonPath)
            throws  FileNotFoundException {
        return jsonFileToString(jsonPath).replace(ROOM_UID, uid).replace(GAME_MODE, gameMode).replace(MAX_TURN, maxTurn)
                .replace(MAX_SCORE, maxScore).replace(BLANK_CARDS, useBlank);
    }

    // Create Player JSON String
    public static String createPostPutPlayerJson(String playerName, String jsonPath) throws FileNotFoundException {
        return jsonFileToString(jsonPath).replace(PLAYER_NAME, playerName);
    }

    public static String createPutPlayerDrawCardJson(String numCards, String jsonPath) throws FileNotFoundException {
        return jsonFileToString(jsonPath).replace(NUM_CARDS, numCards);
    }

    public static String createPutPlayerPlayCardJson(String cardType, String cardColor, String cardValue,
                                                     String setWildColor,String skip, String jsonPath)
            throws FileNotFoundException {
        return jsonFileToString(jsonPath).replace(CARD_TYPE, cardType).replace(CARD_COLOR, cardColor)
                .replace(CARD_VALUE, cardValue).replace(SET_WILD_COLOR, setWildColor).replace(SKIP, skip);
    }
}
