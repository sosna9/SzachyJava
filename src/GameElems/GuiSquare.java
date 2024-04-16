package GameElems;

import Pieces.Piece;

import java.awt.*;

public class GuiSquare {
    public Color color;
    public Piece piece;
    public Boolean highlighted;

    public GuiSquare() {
        this.color = null;
        this.piece = null;
        this.highlighted = false;
    }
}
