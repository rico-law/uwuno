package com.learning.uwuno.game.gameModes;

public interface gameMode {

    String getModeName();
    void calculateScore();
    String determineWinner();

}
