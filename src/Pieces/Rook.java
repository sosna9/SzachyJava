package Pieces;
import GameElems.Board;
import java.util.List;
import java.util.ArrayList;
public class Rook extends Piece {
    private boolean hasMoved; // Keep track if the rook has moved

    public Rook(PlayerColor color) {
        super(color, 'R');
        this.hasMoved = false;
    }

    // Add a getter for hasMoved
    @Override
    public boolean hasMoved() {
        return hasMoved;
    }

    // Override setHasMoved to update hasMoved
    @Override
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Board board) {
        // Rook can move horizontally or vertically any number of squares
        if (startX == endX || startY == endY) {
            // Check if there are any pieces in the way
            if (startX == endX) { // Vertical move
                int direction = Integer.compare(endY, startY);
                for (int y = startY + direction; y != endY; y += direction) {
                    if (board.getPiece(startX,y) != null) {
                        return false;
                    }
                }
            } else { // Horizontal move
                int direction = Integer.compare(endX, startX);
                for (int x = startX + direction; x != endX; x += direction) {
                    if (board.getPiece(x, startY) != null) {
                        return false;
                    }
                }
            }
            // Check if the end position is occupied by an opponent's piece or is empty
            return board.getPiece(endX,endY) == null || board.getPiece(endX,endY).getColor() != this.getColor();
        }
        return false;
    }
}

