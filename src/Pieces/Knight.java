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
        if (wouldThisMovePutKingInCheck(startX, startY, endX, endY, board)) {
            return false;
        }
        int dx = Math.abs(startX - endX);
        int dy = Math.abs(startY - endY);

        // Knights move in an L-shaped pattern: two squares in one direction and one square perpendicular to that direction
        if (board.getPiece(endX, endY) == null || board.getPiece(endX,endY).getColor() != this.getColor()) {
            return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
        }
        return false;
    }
}