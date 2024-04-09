package Pieces;

public class Rook extends Piece {
    public Rook(Color color) {
        super(color);
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Piece[][] board) {
        // Rook can move horizontally or vertically any number of squares
        if (startX == endX || startY == endY) {
            // Check if there are any pieces in the way
            if (startX == endX) { // Vertical move
                int direction = Integer.compare(endY, startY);
                for (int y = startY + direction; y != endY; y += direction) {
                    if (board[startX][y] != null) {
                        return false;
                    }
                }
            } else { // Horizontal move
                int direction = Integer.compare(endX, startX);
                for (int x = startX + direction; x != endX; x += direction) {
                    if (board[x][startY] != null) {
                        return false;
                    }
                }
            }
            // Check if the end position is occupied by an opponent's piece or is empty
            return board[endX][endY] == null || board[endX][endY].getColor() != this.getColor();
        }
        return false;
    }

    @Override
    public boolean threatensPosition(int x, int y, Piece[][] board) {
        // The rook threatens a position if it can move to that position
        for (int i = 0; i < 8; i++) {
            if (isValidMove(x, y, x, i, board) || isValidMove(x, y, i, y, board)) {
                return true;
            }
        }
        return false;
    }
}

