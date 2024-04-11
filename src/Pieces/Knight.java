package Pieces;

public class Knight extends Piece {
    public Knight(PlayerColor color) {
        super(color);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board) {
        int dx = Math.abs(startX - endX);
        int dy = Math.abs(startY - endY);

        // Knights move in an L-shaped pattern: two squares in one direction and one square perpendicular to that direction
        return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
    }

    @Override
    public boolean threatensPosition(int x, int y, Piece[][] board) {
        int[][] moves = { {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1} };

        for (int[] move : moves) {
            int newX = x + move[0];
            int newY = y + move[1];

            // Check if the new position is within the board bounds and is empty or contains an opponent's piece
            if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8 &&
                    (board[newX][newY] == null || board[newX][newY].getColor() != this.getColor())) {
                return true;
            }
        }
        return false;
    }
}
