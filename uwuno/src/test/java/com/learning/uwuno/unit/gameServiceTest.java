package com.learning.uwuno.unit;

import com.learning.uwuno.errors.badRequest;
import com.learning.uwuno.errors.errorNotFound;
import com.learning.uwuno.room;
import com.learning.uwuno.room.Status;
import com.learning.uwuno.services.gameService;
import com.learning.uwuno.player;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@ExtendWith(MockitoExtension.class)
public class gameServiceTest {
    @Mock
    player player;

    @Mock
    room room;

    @InjectMocks
    private gameService gameService;

    final private String testRoomName = "testRoom";
    final private String testPlayerName = "testPlayer";
    final private String newName = "newName";
    final private String wrongId = "id";
    final private String newStatus = "Start";

    @Test
    public void testGameService_addRoom() throws Exception {
        assertThrows(badRequest.class, () ->{
            gameService.addRoom("", false);
        });

        gameService.addRoom(testRoomName, false);
        ArrayList<room> rooms = gameService.getRoomList();
        assertThat(rooms.get(0).getName(), is(testRoomName));
    }

    @Test
    public void testGameService_addPlayer() throws Exception {
        room room = gameService.addRoom(testRoomName, false);
        assertThrows(NoSuchElementException.class, () -> {
            gameService.addPlayer(testPlayerName, wrongId);
        });

        assertThrows(badRequest.class, () -> {
            gameService.addPlayer("", room.getUid());
        });

        gameService.addPlayer(testPlayerName, room.getUid());
        ArrayList<room> rooms = gameService.getRoomList();
        assertThat(rooms.get(0).getPlayers().get(0).getName(), is(testPlayerName));
    }

    @Test
    public void testGameService_getRoomList() throws Exception {
        ArrayList<room> rooms = gameService.getRoomList();
        assertThat(rooms, hasSize(0));

        for (int i = 0; i < 3; i++)
            gameService.addRoom(testRoomName, false);
        assertThat(rooms, hasSize(3));
    }

    @Test
    public void testGameService_getRoom() throws Exception{
        gameService.addRoom("extraRoom", false);
        room room = gameService.addRoom(testRoomName, false);
        gameService.addRoom("extraRoom", false);
        assertThat(gameService.getRoom(room.getUid()).getName(), equalTo(testRoomName));

        assertThrows(NoSuchElementException.class, () -> {
            gameService.getRoom(wrongId);
        });
    };

    @Test
    public void testGameService_getPlayer() throws Exception {
        room room = gameService.addRoom(testRoomName, false);
        assertThrows(NoSuchElementException.class, () -> {
            gameService.getPlayer(room.getUid(), wrongId);
        });

        player player = gameService.addPlayer(testPlayerName, room.getUid());
        assertThat(gameService.getPlayer(room.getUid(), player.getPid()).getName(), equalTo(testPlayerName));
    }

    @Test
    public void testGameService_updateRoomName() throws Exception {
        room room = gameService.addRoom(testRoomName, false);

        assertThrows(badRequest.class, () -> {
            gameService.updateRoomName(room.getUid(), "");
        });

        gameService.updateRoomName(room.getUid(), newName);
        assertThat(room.getName(), equalTo(newName));
    }

    @Test
    public void testGameService_updateRoomStatus() throws Exception {
        room room = gameService.addRoom(testRoomName, false);

        assertThrows(badRequest.class, () -> {
            gameService.updateRoomStatus(room.getUid(), "");
        });

        assertThrows(badRequest.class, () -> {
            gameService.updateRoomStatus(room.getUid(), "invalid_status");
        });

        gameService.updateRoomStatus(room.getUid(), newStatus);
        assertThat(room.getRoomStatus(), equalTo(Status.Start));
    }

    @Test
    public void testGameService_updatePlayerName() throws Exception {
        room room = gameService.addRoom(testRoomName, false);
        player player = gameService.addPlayer(testPlayerName, room.getUid());

        assertThrows(badRequest.class, () -> {
            gameService.updatePlayerName(room.getUid(), player.getPid(), "");
        });

        player newPlayer = gameService.updatePlayerName(room.getUid(), player.getPid(), newName);
        assertThat(newPlayer.getName(), equalTo(newName));
    }

    @Test
    public void testGameService_deleteRoom() throws Exception {
        room room = gameService.addRoom(testRoomName, false);

        assertThrows(errorNotFound.class, () -> {
            gameService.deleteRoom(wrongId);
        });

        gameService.deleteRoom(room.getUid());
        assertThat(gameService.getRoomList(), hasSize(0));

        assertThrows(badRequest.class, () -> {
            gameService.deleteRoom(wrongId);
        });
    }

    @Test
    public void testGameService_deletePlayer() throws Exception {
        room room = gameService.addRoom(testRoomName, false);
        player player = gameService.addPlayer(testPlayerName, room.getUid());
        assertThrows(NoSuchElementException.class, () -> {
            gameService.deletePlayer(wrongId,  player.getPid());
        });

        assertThrows(errorNotFound.class, () -> {
            gameService.deletePlayer(room.getUid(), wrongId);
        });

        final String playerID = player.getPid();
        gameService.deletePlayer(room.getUid(), player.getPid());
        assertThat(room.getPlayers(), hasSize(0));
    }

}
