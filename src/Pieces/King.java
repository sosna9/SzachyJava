package Pieces;

public class King extends Piece {
    private boolean hasMoved; // Keep track if the king has moved
    public King(Color color) {
        super(color);
        this.hasMoved = false;
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board) {
        // Check if the move is valid for a king (can move one square in any direction)
        int dx = Math.abs(startX - endX);
        int dy = Math.abs(startY - endY);

        // The move is valid if the destination square is adjacent and empty or contains an opponent's piece
        return dx <= 1 && dy <= 1 && (board[endX][endY] == null || board[endX][endY].getColor() != this.getColor());
    }

    @Override
    public boolean threatensPosition(int x, int y, Piece[][] board) {
        // The king threatens a position if it can move to that position
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int newX = x + dx;
                int newY = y + dy;
                if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8 && (board[newX][newY] == null || board[newX][newY].getColor() != this.getColor())) {
                    return true;
                }
            }
        }
        return false;
    }
}
