package Pieces;
import GameElems.Board;

import java.util.List;
import java.util.ArrayList;
public class Queen extends Piece {
    public Queen(PlayerColor color) {
        super(color, 'Q');
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Board board) {
        // Check if the move is valid for a queen (can move horizontally, vertically, or diagonally)
        // Similar to the logic of Rook and Bishop
        return new Rook(getColor()).isValidMove(startX, startY, endX, endY, board) ||
                new Bishop(getColor()).isValidMove(startX, startY, endX, endY, board);
    }

    @Override
    public boolean threatensPosition(int x, int y, Board board) {
        // The queen threatens a position if it can move to that position
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (isValidMove(i, j, x, y, board)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<int[]> generatePossibleMoves(int startX, int startY, Board board) {
        List<int[]> possibleMoves = new ArrayList<>();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (isValidMove(startX, startY, x, y, board)) {
                    possibleMoves.add(new int[]{x, y});
                }
            }
        }
        return possibleMoves;
    }
}
