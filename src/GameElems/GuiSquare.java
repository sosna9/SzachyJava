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

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
    public Piece getPiece() {
        return this.piece;
    }
    public Color getColor() {
        return this.color;
    }
    public boolean isHighlighted() {
        return this.highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }
}
