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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@WebMvcTest(roomController.class)
@AutoConfigureMockMvc
public class roomControllerTests {
    @Autowired
    private MockMvc mock;

    @MockBean
    private gameService service;

    @Test
    public void GET_empty_roomlist() throws Exception {
        ArrayList<room> roomList = new ArrayList<>();
        when(service.getRoomList()).thenReturn(roomList);
        mock.perform(get("/rooms")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"))
                .andDo(print());
    }

    @Test
    public void GET_roomlist() throws Exception {
        ArrayList<room> roomList = new ArrayList<>();
        room room = new room("test", false);
        roomList.add(room);
        when(service.getRoomList()).thenReturn(roomList);

        mock.perform(get("/rooms")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]['name']").value("test"))
                .andExpect(jsonPath("$.length()").value(1))
                .andDo(print());
    }

    @Test
    public void GET_room() throws Exception {
        room room = new room("test", false);
        when(service.getRoom(anyString())).thenReturn(room);
        mock.perform(get("/rooms/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(room.getName()))
                .andExpect(jsonPath("$.uid").value(room.getUid()))
                .andExpect(jsonPath("$.players").value(room.getPlayers()))
                .andDo(print());
    }

    @Test
    public void GET_room_not_found() throws Exception {
        when(service.getRoom(anyString())).thenThrow(NoSuchElementException.class);
        mock.perform(get("/rooms/2"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void POST_add_room() throws Exception {
        room room = new room("test", false);
        when(service.addRoom(anyString(), anyBoolean())).thenReturn(room);

        ArrayList<String> keys = new ArrayList<>();
        keys.add("roomName");
        keys.add("useBlankCards");

        ArrayList<String> values = new ArrayList<>();
        values.add("test");
        values.add("false");

        mock.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.createJSON(keys, values))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(room.getName()))
                .andExpect(jsonPath("$.uid").value(room.getUid()))
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
        when(service.addRoom(anyString(), anyBoolean())).thenThrow(new com.learning.uwuno.errors.badRequest());

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
        // 4. Empty RoomName (error thrown by service)
        ArrayList<String> keys = new ArrayList<>();
        keys.add("roomName");
        keys.add("useBlankCards");

        ArrayList<String> values = new ArrayList<>();
        values.add("");
        values.add("false");

        mock.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.createJSON(keys, values))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void GET_players_in_empty_room() throws Exception {
        room room = new room("test", false);
        when(service.getRoom(room.getUid())).thenReturn(room);

        mock.perform(get("/rooms/" + room.getUid() + "/players")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andDo(print());
    }

    @Test
    public void GET_players_in_room() throws Exception {
        room room = new room("test", false);
        player player = new player("tester");
        room.addPlayer(player);
        when(service.getRoom(room.getUid())).thenReturn(room);

        mock.perform(get("/rooms/" + room.getUid() + "/players")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value(player.getName()))
                .andDo(print());
    }

    @Test
    public void GET_players_in_room_not_found_err() throws Exception {
        player player = new player("tester");
        when(service.getRoom(anyString())).thenThrow(new com.learning.uwuno.errors.errorNotFound());

        mock.perform(get("/rooms/2/players")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void PUT_update_room_name() throws Exception {
        room room = new room("test", false);
        when(service.getRoom(room.getUid())).thenReturn(room);

        mock.perform(get("/rooms/" + room.getUid())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test"))
                .andDo(print());

        room.setRoomName("newName");
//        when(service.updateRoomName(room.getUid(), room.getName())).thenReturn(room);
//        mock.perform(put("/rooms/" + room.getUid())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(testUtils.createJSON("roomName", room.getName()))
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("newName"))
//                .andDo(print());
    }

    @Test
    public void PUT_update_room_name_not_found_err() throws Exception {
//        when(service.updateRoomName(anyString(), anyString())).thenThrow(new com.learning.uwuno.errors.errorNotFound());
//
//        mock.perform(put("/rooms/2")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(testUtils.createJSON("roomName", "3"))
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andDo(print());
    }

    @Test
    public void PUT_update_room_name_bad_req_err() throws Exception {
//        when(service.updateRoomName(anyString(), anyString())).thenThrow(new com.learning.uwuno.errors.badRequest());
//
//        mock.perform(put("/rooms/2")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(testUtils.createJSON("roomName", "")))
//                .andExpect(status().isBadRequest())
//                .andDo(print());
    }

    @Test
    public void DELETE_room() throws Exception {
        mock.perform(delete("/rooms/2"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void DELETE_room_not_found_err() throws Exception {
        doThrow(new com.learning.uwuno.errors.errorNotFound()).when(service).deleteRoom(anyString());

        mock.perform(delete("/rooms/2"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

}
