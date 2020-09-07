package com.learning.uwuno.unit;

import com.learning.uwuno.controllers.roomController;
import com.learning.uwuno.room;
import com.learning.uwuno.services.gameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        mock.perform(get("/rooms"))
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

        mock.perform(get("/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]['name']").value("test"))
                .andExpect(jsonPath("$.length()").value(1))
                .andDo(print());
    }

    @Test
    public void GET_room_not_found() throws Exception {
        when(service.getRoom("2")).thenThrow(NoSuchElementException.class);
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
    public void POST_add_room_bad_req_err_1() throws Exception {
        when(service.addRoom(isNull(), anyBoolean())).thenThrow(HttpClientErrorException.BadRequest.class);

        ArrayList<String> keys = new ArrayList<>();
        keys.add("roomName");
        keys.add("useBlankCards");

        ArrayList<String> values = new ArrayList<>();
        values.add(null);
        values.add("false");

        mock.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.createJSON(keys, values))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void POST_add_room_bad_req_err_2() throws Exception {
        when(service.addRoom(anyString(), isNull())).thenThrow(com.learning.uwuno.errors.badRequest.class);

        ArrayList<String> keys = new ArrayList<>();
        keys.add("roomName");
        keys.add("useBlankCards");

        ArrayList<String> values = new ArrayList<>();
        values.add("test");
        values.add(null);

        mock.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testUtils.createJSON(keys, values))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void GET_players_in_empty_room() throws Exception {

    }

    @Test
    public void GET_players_in_room() throws Exception{

    }

    @Test
    public void GET_players_in_room_not_found_err() throws Exception {

    }

    @Test
    public void PUT_update_room_name() throws Exception {

    }

    @Test
    public void PUT_update_room_name_not_found_err() throws Exception {

    }

    @Test
    public void PUT_update_room_name_bad_req_err() throws Exception {

    }

    @Test
    public void DELETE_room() throws Exception {

    }

    @Test
    public void DELETE_room_not_found_err() throws Exception {

    }

}
