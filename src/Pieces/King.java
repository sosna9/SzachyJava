package Pieces;
import GameElems.Board;

import java.util.List;
import java.util.ArrayList;
public class King extends Piece {
    private boolean hasMoved; // Keep track if the king has moved
    public King(PlayerColor color) {
        super(color, 'K');
        this.hasMoved = false;
    }

    public boolean hasMoved() {
        return hasMoved;
    }


    // In King.java
    @Override
    public boolean isValidMove(int startCol, int startRow, int endCol, int endRow, Board board) {
        if (wouldThisMovePutKingInCheck(startCol, startRow, endCol, endRow, board)) {
            return false;
        }
        // Check if the move is valid for a king (can move one square in any direction)
        int dRows = Math.abs(startCol - endCol);
        int dCol = Math.abs(startRow - endRow);

        // The move is valid if the destination square is adjacent and empty or contains an opponent's piece
        boolean isAdjacentMove = dRows <= 1 && dCol <= 1 && (board.getPiece(endCol, endRow)== null || board.getPiece(endCol, endRow).getColor() != this.getColor());

        // Check for castling
        boolean isKingSideCastle = !hasMoved && dRows == 0 && endRow == 6 &&
                board.getPiece(startCol, startRow + 1) == null &&
                board.getPiece(startCol, startRow + 2) == null;
        if (isKingSideCastle) {
            Piece rook = board.getPiece(startCol, startRow + dCol / 2 * 3);
            isKingSideCastle = rook instanceof Rook && !rook.hasMoved() && rook.getColor()== this.getColor();
        }
        boolean isQueenSideCastling = !hasMoved && dRows == 0 && endRow == 2 &&
                board.getPiece(startCol, startRow -1) == null &&
                board.getPiece(startCol, startRow -2) == null &&
                board.getPiece(startCol, startRow -3) == null;
        if (isQueenSideCastling) {
            Piece rook = board.getPiece(startCol, 0);
            isQueenSideCastling = rook instanceof Rook && !rook.hasMoved() && rook.getColor()== this.getColor();
        }

        return (isAdjacentMove || isKingSideCastle || isQueenSideCastling);
    }

}
