package com.learning.uwuno.unit;

import com.learning.uwuno.controllers.roomController;
import com.learning.uwuno.player;
import com.learning.uwuno.room;
import com.learning.uwuno.services.gameService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(roomController.class)
@AutoConfigureMockMvc
public class roomControllerTests {
    @Autowired
    private MockMvc mock;

    @Autowired
    private roomController controller;

    @MockBean
    private gameService service;

    room room = new room("testRoom", false);
    player player = new player("testPlayer");

    @Test
    public void GET_empty_roomList() throws Exception {
        ArrayList<room> roomList = new ArrayList<>();
        when(service.getRoomList()).thenReturn(roomList);

        mock.perform(get("/rooms")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"))
                .andDo(print());
    }

    @Test
    public void GET_roomList() throws Exception {
        ArrayList<room> roomList = new ArrayList<>();
        roomList.add(room);
        when(service.getRoomList()).thenReturn(roomList);

        mock.perform(get("/rooms")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]['name']").value("testRoom"))
                .andExpect(jsonPath("$.length()").value(1))
                .andDo(print());
    }

    @Test
    public void GET_room() throws Exception {
        room.addPlayer(player);
        when(service.getRoom(anyString())).thenReturn(room);

        mock.perform(get("/rooms/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(room.getName()))
                .andExpect(jsonPath("$.uid").value(room.getUid()))
                .andExpect(jsonPath("$.players", hasSize(1)))
                .andExpect(jsonPath("$.players[0].name").value(room.getPlayers().get(0).getName()))
                .andExpect(jsonPath("$.players[0].pid").value(room.getPlayers().get(0).getPid()))
                .andDo(print());
    }

    @Test
    public void GET_room_players() throws Exception {
        room.addPlayer(player);
        when(service.getRoom(anyString())).thenReturn(room);

        mock.perform(get("/rooms/2/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(room.getPlayers().get(0).getName()))
                .andExpect(jsonPath("$[0].pid").value(room.getPlayers().get(0).getPid()))
                .andExpect(jsonPath("$.length()").value(room.getPlayers().size()))
                .andDo(print());
    }

    @Test
    public void POST_add_room() throws Exception {
        when(service.addRoom(anyString(), anyBoolean())).thenReturn(room);

        String content = testUtils.createJSON(
                new ArrayList<>(List.of("roomName", "useBlankCards")),
                new ArrayList<>(Arrays.asList(room.getName(), "false")));

        mock.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(room.getName()))
                .andExpect(jsonPath("$.uid").value(room.getUid()))
                .andExpect(jsonPath("$.players", hasSize(0)))
                .andDo(print());
    }

    @Test
    public void POST_add_room_internal_server_err() throws Exception {
        String invalidJSON = "{" + "]";

        mock.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

    @Test
    public void POST_add_room_bad_req_err() throws Exception {
        // 1. No RoomName in JSON
        String missingRoomName = testUtils.createJSON("useBlankCards", "false");

        mock.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(missingRoomName)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
        // 2. No UseBlankCards in JSON
        String missingBlankCards = testUtils.createJSON("roomName", "test");

        mock.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(missingBlankCards)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
        // 3. Missing both fields in JSON
        mock.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void GET_players_in_empty_room() throws Exception {
        when(service.getRoom(room.getUid())).thenReturn(room);

        mock.perform(get("/rooms/" + room.getUid() + "/players")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andDo(print());
    }

    @Test
    public void PUT_update_room_name() throws Exception {
        room.setRoomName("newName");
        when(service.getRoom(room.getUid())).thenReturn(room);

        String content = testUtils.createJSON(
                new ArrayList<>(List.of("uid", "roomName", "roomStatus")),
                new ArrayList<>(Arrays.asList(room.getUid(), room.getName(), room.getRoomStatus().toString())));

        mock.perform(put("/rooms/" + room.getUid())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uid").value(room.getUid()))
                .andExpect(jsonPath("$.name").value("newName"))
                .andExpect(jsonPath("$.roomStatus").value("Lobby"))
                .andDo(print());
    }

    @Test
    public void PUT_update_room_name_bad_request() throws Exception {
        String content = testUtils.createJSON(
                new ArrayList<>(List.of("invalidKey", "invalidKey", "invalidKey")),
                new ArrayList<>(Arrays.asList(room.getUid(), room.getName(), room.getRoomStatus().toString())));

        mock.perform(put("/rooms/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void DELETE_room() throws Exception {
        mock.perform(delete("/rooms/2"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
