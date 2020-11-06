package com.learning.uwuno;

public final class gameLogic {
    private gameLogic() {}

//     Rules:
//         - Match by number, colour or symbol/action. Or can play wildcard.
//         - Player can choose not to play their card OR if they can't play a card => will need to draw 1 from deck
//         - After drawing, if card can be played, they can play that card
//         - Must say UNO before playing 2nd last card
//                - If you forget and another player catches you, DRAW 4
//                - If no one catches you before next player plays/draw a card, there is no penalty
//         - First card, reshuffle if not number cards
//         - Stacking: Can mix +2 and +4
//
//     Scoring:
//         - Normal game mode:
//                - When you get rid of all your cards, you get points equal to the cards left in other players' hands
//                - First to 500 pts is the winner
//         - Modified game mode (& for max turns):
//                - [(number of points in your hand) / (player with the most points)] * 100
//                - Player with the least points wins
//         - All number cards (0-9) ... Face value
//         - Draw 2 ... 20 pts
//         - Reverse ... 20 pts
//         - Skip ... 20 pts
//         - Wild ... 50 pts
//         - Wild Draw 4 ... 50 pts

    // TODO: have option for max turns and game mode: original UNO or point based UNO.
    //  If game ends by max turns, winner determined by points.
}
