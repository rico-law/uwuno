package com.learning.uwuno.unit;

import com.learning.uwuno.room;
import com.learning.uwuno.services.gameService;
import com.learning.uwuno.player;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.hamcrest.Matchers.contains;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class gameServiceTest {

    @Mock
    player player;

    @Mock
    room room;

    @InjectMocks
    private gameService gameService;

    @Test
    public void testGameService_addRoom() throws Exception {
        gameService.addRoom("testRoom", false);
        ArrayList<room> rooms = gameService.getRoomList();
        assertThat(rooms.get(0), contains("testRoom"));
    }


}
