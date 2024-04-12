package Pieces;

public class Pawn extends Piece {

    public Pawn(PlayerColor color) {
        super(color);
    }


    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board) {
        int direction = (getColor() == PlayerColor.WHITE) ? 1 : -1; // Direction of movement depends on pawn's color

        // Check if the move is forward
        if (startY == endY && board[endX][endY] == null) {
            // Normal move
            if (startX + direction == endX) {
                return true;
            }
            // First move, pawn can move two squares forward
            if (!this.hasMoved() && startX + 2 * direction == endX && board[startX + direction][endY] == null) {
                return true;
            }
        }
        // Check if the move is a capture
        else if (Math.abs(startY - endY) == 1 && startX + direction == endX) {
            // Check if there's an opponent's piece to capture
            if (board[endX][endY] != null && board[endX][endY].getColor() != this.getColor()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean threatensPosition(int x, int y, Piece[][] board) {
        int direction = (getColor() == PlayerColor.WHITE) ? -1 : 1;
        int nextX = x + direction;
        return (nextX >= 0 && nextX < 8) && (y - 1 >= 0 && board[nextX][y - 1] == this || y + 1 < 8 && board[nextX][y + 1] == this);
    }
}

