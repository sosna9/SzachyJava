package GameElems;

import Pieces.*;

//import javax.swing.*;


public class Player {
    private PlayerColor color;

    public Player(PlayerColor color) {
        this.color = color;
    }

    public PlayerColor getColor() {
        return color;
    }

    public void setColor(PlayerColor currentTurn) {
        this.color = currentTurn;
    }
}



