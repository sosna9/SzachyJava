package Pieces;
import GameElems.Board;


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
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Board board) {
        int direction = (getColor() == PlayerColor.BLACK) ? 1 : -1; // Direction of movement depends on pawn's color
        // Check if the move is forward
        if (startCol == endCol && board.getPiece(endRow,endCol) == null) {
            // Normal move
            if (startRow + direction == endRow) {
                return true;
            }
            // First move, pawn can move two squares forward
            return !this.hasMoved() && startRow + 2 * direction == endRow && board.getPiece(startRow + direction, endCol) == null;
        }
        // Check if the move is a capture
        else if (Math.abs(startCol - endCol) == 1 && startRow + direction == endRow) {
            // Check if there's an opponent's piece to capture
            if (board.getPiece(endRow, endCol) != null && board.getPiece(endRow,endCol).getColor() != this.getColor()) {
                return true;
            }
            // Check for en passant
            else if ((getColor() == PlayerColor.BLACK && startRow == 4) || (getColor() == PlayerColor.WHITE && startRow == 3)) {
                Piece adjacentPiece = board.getPiece(startRow, startCol + endCol - startCol);
                return adjacentPiece instanceof Pawn && adjacentPiece.getColor() != getColor() && adjacentPiece.getHasMovedTwo();
            }
        }
        return false;
    }

}

