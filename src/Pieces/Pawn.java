package Pieces;
import GameElems.Board;

import java.util.List;
import java.util.ArrayList;
public class Pawn extends Piece {

    private boolean hasMovedTwo;
    public Pawn(PlayerColor color) {
        super(color, 'P');
        this.hasMovedTwo = false;
    }

    @Override
    public boolean getHasMovedTwo() {
        return hasMovedTwo;
    }
    public void setHasMovedTwo(boolean hasMovedTwo) {
        this.hasMovedTwo = hasMovedTwo;
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Board board) {
        int direction = (getColor() == PlayerColor.WHITE) ? 1 : -1; // Direction of movement depends on pawn's color

        // Check if the move is forward
        if (startY == endY && board.getPiece(endX,endY) == null) {
            // Normal move
            if (startX + direction == endX) {
                return true;
            }
            // First move, pawn can move two squares forward
            return !this.hasMoved() && startX + 2 * direction == endX && board.getPiece(startX + direction, endY) == null;
        }
        // Check if the move is a capture
        else if (Math.abs(startY - endY) == 1 && startX + direction == endX) {
            // Check if there's an opponent's piece to capture
            if (board.getPiece(endX, endY) != null && board.getPiece(endX,endY).getColor() != this.getColor()) {
                return true;
            }
            // Check for en passant
            else if ((getColor() == PlayerColor.WHITE && startX == 4) || (getColor() == PlayerColor.BLACK && startX == 3)) {
                Piece adjacentPiece = board.getPiece(startX, startY + endY - startY);
                return adjacentPiece instanceof Pawn && adjacentPiece.getColor() != getColor() && adjacentPiece.getHasMovedTwo();
            }
        }
        return false;
    }

}

