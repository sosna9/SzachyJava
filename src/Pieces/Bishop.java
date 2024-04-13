package Pieces;
import GameElems.Board;

import java.util.List;
import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(PlayerColor color) {
        super(color, 'B');
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Board board) {
        // Check if the move is valid for a bishop (can move diagonally)
        if (Math.abs(endX - startX) == Math.abs(endY - startY)) {
            int xDirection = Integer.compare(endX, startX);
            int yDirection = Integer.compare(endY, startY);
            int x = startX + xDirection;
            int y = startY + yDirection;
            while (x != endX && y != endY) {
                if (board.getPiece(x, y)  != null) {
                    return false; // There's a piece in the way
                }
                x += xDirection;
                y += yDirection;
            }
            // Check if the end position is empty or contains an opponent's piece
            return board.getPiece(endX, endY) == null || board.getPiece(endX, endY).getColor() != this.getColor();
        }
        return false;
    }

    @Override
    public boolean threatensPosition(int x, int y, Board board) {
        // Check the diagonals
        for (int i = -7; i <= 7; i++) {
            if (isValidMove(x, y, x + i, y + i, board) || isValidMove(x, y, x + i, y - i, board)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<int[]> generatePossibleMoves(int startX, int startY, Board board) {
        List<int[]> possibleMoves = new ArrayList<>();
        // Diagonal moves
        int[] dx = {-1, -1, 1, 1};
        int[] dy = {-1, 1, -1, 1};
        for (int direction = 0; direction < 4; direction++) {
            for (int x = startX + dx[direction], y = startY + dy[direction]; x >= 0 && x < 8 && y >= 0 && y < 8; x += dx[direction], y += dy[direction]) {
                if (board.getPiece(x, y) == null || board.getPiece(x, y).getColor() != this.getColor()) {
                    possibleMoves.add(new int[]{x, y});
                }
                if (board.getPiece(x, y) != null) {
                    break;
                }
            }
        }
        return possibleMoves;
    }
}