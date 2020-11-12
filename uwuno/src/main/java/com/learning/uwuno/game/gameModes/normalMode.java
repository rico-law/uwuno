package com.learning.uwuno.game.gameModes;

public class normalMode implements gameMode {
    final private String NAME = "NORMAL";

    @Override
    public String getModeName() {
        return NAME;
    }

    @Override
    public void calculateScore() {
    }

    @Override
    public String determineWinner() {
        return "";
    }
}
