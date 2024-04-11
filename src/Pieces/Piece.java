package Pieces;
// attribution link to thank author of icons <a href="https://www.flaticon.com/free-icons/crown" title="crown icons">Chess pieces icons created by Stockio - Flaticon</a>
abstract public class Piece {
    private PlayerColor color;

    private boolean hasMoved = false;

    public boolean hasMoved() {
        return hasMoved;
    }
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }


    public Piece(PlayerColor color) {
        this.color = color;
        this.hasMoved = false;
    }

    // Abstract method to check if a move is valid for the piece
    public abstract boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board);

    public PlayerColor getColor() {
        return color;
    }

    public abstract boolean threatensPosition(int x, int y, Piece[][] board);

}
