package Pieces;

abstract public class Piece {
    private Color color;
    // Add any other common properties or methods for pieces here

    public Piece(Color color) {
        this.color = color;
    }

    // Abstract method to check if a move is valid for the piece
    public abstract boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board);

    public Color getColor() {
        return color;
    }

    public abstract boolean threatensPosition(int x, int y, Piece[][] board);

    public enum Color {
        WHITE,
        BLACK
    }
}
