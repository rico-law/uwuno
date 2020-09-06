package com.learning.uwuno.unit;

import com.learning.uwuno.controllers.roomController;
import com.learning.uwuno.controllers.playerController;


import com.learning.uwuno.services.gameService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(roomController.class)
@AutoConfigureMockMvc
public class roomControllerTests {
    @Autowired
    private MockMvc mock;

    @MockBean
    private gameService service;


    @Test
    public void testRoom_POST() throws Exception {
        this.mock.perform(post("/Rooms")).andDo(print()).andExpect(status().isOk());
    }

}
