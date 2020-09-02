package com.learning.uwuno;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class player {
    // Class Variables
    @JsonIgnore
    final private String uid;
    private String name;

    public player(String name) {
        this.uid = UUID.randomUUID().toString();
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
