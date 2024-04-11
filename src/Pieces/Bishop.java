package Pieces;

public class Bishop extends Piece {
    public Bishop(PlayerColor color) {
        super(color);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board) {
        // Check if the move is valid for a bishop (can move diagonally)
        if (Math.abs(endX - startX) == Math.abs(endY - startY)) {
            int xDirection = Integer.compare(endX, startX);
            int yDirection = Integer.compare(endY, startY);
            int x = startX + xDirection;
            int y = startY + yDirection;
            while (x != endX && y != endY) {
                if (board[x][y] != null) {
                    return false; // There's a piece in the way
                }
                x += xDirection;
                y += yDirection;
            }
            // Check if the end position is empty or contains an opponent's piece
            return board[endX][endY] == null || board[endX][endY].getColor() != this.getColor();
        }
        return false;
    }

    @Override
    public boolean threatensPosition(int x, int y, Piece[][] board) {
        // The bishop threatens a position if it can move to that position
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isValidMove(i, j, x, y, board)) {
                    return true;
                }
            }
        }
        return false;
    }
}
