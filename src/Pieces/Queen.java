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
        if (wouldThisMovePutKingInCheck(startX, startY, endX, endY, board)) {
            return false;
        }
        // Check if the move is valid for a queen (can move horizontally, vertically, or diagonally)
        // Similar to the logic of Rook and Bishop
        return new Rook(getColor()).isValidMove(startX, startY, endX, endY, board) ||
                new Bishop(getColor()).isValidMove(startX, startY, endX, endY, board);
    }

}
