package com.learning.uwuno;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Vector;

@RestController
public class controllerTest {
    // Should do this when going on localhost:8080/test
    // This type of mapping should
    @RequestMapping("test")
    public String test() {
        return roomContainer.getInstance().getRoom(0).getName();
    }

    // Returns .json formatted vector of rooms, private variables are shown (room name)
    @RequestMapping("rooms")
    public Vector<room> rooms() {
        return roomContainer.getInstance().getRoomList();
    }



}



