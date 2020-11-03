package com.learning.uwuno.unit;

import com.learning.uwuno.cards.card;
import com.learning.uwuno.cards.deck;
import com.learning.uwuno.controllers.playerController;
import com.learning.uwuno.player;
import com.learning.uwuno.services.gameService;

import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import static org.hamcrest.Matchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.*;

@WebMvcTest(playerController.class)
@AutoConfigureMockMvc
public class playerControllerTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private playerController controller;

    @MockBean
    private gameService gameService;

    player testPlayer = new player("testPlayer");

    @Test
    public void testPlayer_POST() throws Exception {
        when(gameService.addPlayer(anyString(), anyString())).thenReturn(testPlayer);

        mvc.perform(post("/rooms/123/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.createJSON("name", testPlayer.getName()))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pid").value(testPlayer.getPid()))
                .andExpect(jsonPath("$.name").value(testPlayer.getName()))
                .andExpect(jsonPath("$.cardList").isArray())
                .andExpect(jsonPath("$.cardList", hasSize(0)));
    }

    @Test
    public void testPlayer_POST_ERROR() throws Exception {
        mvc.perform(post("/rooms/123/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.createJSON("wrongKey", testPlayer.getName())))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPlayer_GET() throws Exception {
        testPlayer.setCurDeck(new deck(false));
        List<card> testDraw = testPlayer.drawCards(1);
        when(gameService.getPlayer(anyString(), anyString())).thenReturn(testPlayer);

        mvc.perform(get("/rooms/123/players/123/cards")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].type").value(testDraw.get(0).getType().toString()))
                .andExpect(jsonPath("$[*].color").value(testDraw.get(0).getColor().toString()));
    }

    @Test
    public void testPlayer_PUT_updateName() throws Exception {
        String newName = "newTestName";
        testPlayer.setName(newName);
        when(gameService.updatePlayerName(anyString(), anyString(), anyString())).thenReturn(testPlayer);

        mvc.perform(put("/rooms/123/players/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.createJSON("name", newName))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pid").value(testPlayer.getPid()))
                .andExpect(jsonPath("$.name").value(newName));
    }

    @Test
    public void testPlayer_PUT_updateName_ERROR() throws Exception {
        mvc.perform(put("/rooms/123/players/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.createJSON("wrongKey", "newName"))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPlayer_PUT_drawCard() throws Exception {
        testPlayer.setCurDeck(new deck(false));
        List<card> testDraw = testPlayer.drawCards(1);
        when(gameService.drawCards(anyString(), anyString(), anyInt())).thenReturn(testPlayer);

        mvc.perform(put("/rooms/123/players/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.createJSON("draw", "1"))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardList").isArray())
                .andExpect(jsonPath("$.cardList", hasSize(1)))
                .andExpect(jsonPath("$.cardList[0].color").value(testDraw.get(0).getColor().toString()))
                .andExpect(jsonPath("$.cardList[0].type").value(testDraw.get(0).getType().toString()));
    }

    @Test
    public void testPlayer_PUT_drawCard_ERROR() throws Exception {
        mvc.perform(put("/rooms/123/players/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.createJSON("wrongKey", "1"))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

        mvc.perform(put("/rooms/123/players/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.createJSON("draw", "wrongValue"))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPlayer_PUT_playCard() throws Exception {
        testPlayer.setCurDeck(new deck(false));
        testPlayer.drawCards(1);
        testPlayer.playCard(testPlayer.getCardList().get(0));
        when(gameService.playCard(anyString(), anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(testPlayer);

        String content = testUtils.createJSON(
                new ArrayList<>(List.of("cardType", "cardColor", "cardValue", "setWildColor")),
                new ArrayList<>(Arrays.asList("Basic", "Green", "6", "")));

        mvc.perform(put("/rooms/123/players/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardList").isArray())
                .andExpect(jsonPath("$.cardList", hasSize(0)));
    }

    @Test
    public void testPlayer_PUT_playCard_ERROR() throws Exception {
        String content = testUtils.createJSON(
                new ArrayList<>(List.of("cardType", "cardColor", "cardValue")),
                new ArrayList<>(Arrays.asList("Basic", "Green", "6")));

        mvc.perform(put("/rooms/123/players/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
