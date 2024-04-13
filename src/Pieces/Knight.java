package Pieces;
import GameElems.Board;

import java.util.List;
import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(PlayerColor color) {
        super(color, 'N');
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Board board) {
        int dx = Math.abs(startX - endX);
        int dy = Math.abs(startY - endY);

        // Knights move in an L-shaped pattern: two squares in one direction and one square perpendicular to that direction
        return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
    }

    @Override
    public boolean threatensPosition(int x, int y, Board board) {
        int[][] moves = { {-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1} };

        for (int[] move : moves) {
            int newX = x + move[0];
            int newY = y + move[1];

            // Check if the new position is within the board bounds and is empty or contains an opponent's piece
            if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8 &&
                    (board.getPiece(newX, newY) == null || board.getPiece(newX, newY).getColor() != this.getColor())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<int[]> generatePossibleMoves(int startX, int startY, Board board) {
        List<int[]> possibleMoves = new ArrayList<>();
        // All 8 possible moves
        int[] dx = {-2, -2, -1, -1, 1, 1, 2, 2};
        int[] dy = {-1, 1, -2, 2, -2, 2, -1, 1};
        for (int move = 0; move < 8; move++) {
            int x = startX + dx[move], y = startY + dy[move];
            if (x >= 0 && x < 8 && y >= 0 && y < 8 && (board.getPiece(x, y) == null || board.getPiece(x, y).getColor() != this.getColor())) {
                possibleMoves.add(new int[]{x, y});
            }
        }
        return possibleMoves;
    }
}