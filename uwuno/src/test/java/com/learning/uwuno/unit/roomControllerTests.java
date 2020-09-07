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
    public void GET_empty_beginning() throws Exception {
        mock.perform(get("/rooms"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"))
                .andDo(print());
    }

    @Test
    public void GET_empty_beginning_room() throws Exception {
        when(service.getRoom("2")).thenThrow(NoSuchElementException.class);
        mock.perform(get("/rooms/2"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void POST_first_room() throws Exception {
        room room = new room("test", false);
        when(service.addRoom(anyString(), anyBoolean())).thenReturn(room);

        mock.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"roomName\": \"test\", \"useBlankCards\": \"false\" }"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("test"))
                .andDo(print());
    }

    @Test
    public void POST_first_room_bad_req_1() throws Exception {
        when(service.addRoom(isNull(), anyBoolean())).thenThrow(HttpClientErrorException.BadRequest.class);

        mock.perform(post("/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"roomName\": \"null\", \"useBlankCards\": \"false\" }"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

}
