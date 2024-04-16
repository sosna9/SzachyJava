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

    public boolean isInCheck(int x, int y, Board board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board.getPiece(i,j);
                if (piece != null && piece.getColor() != this.getColor() && piece.isValidMove(i, j, x, y, board)) {
                    return true;
                }
            }
        }
        return false;
    }

    // In King.java
    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Board board) {
        // Check if the move is valid for a king (can move one square in any direction)
        int dx = Math.abs(startX - endX);
        int dy = Math.abs(startY - endY);

        // The move is valid if the destination square is adjacent and empty or contains an opponent's piece
        boolean isAdjacentMove = dx <= 1 && dy <= 1 && (board.getPiece(endX, endY)== null || board.getPiece(endX, endY).getColor() != this.getColor());

        // Check if the destination square is under attack
        boolean isUnderAttack = wouldThisMovePutKingInCheck(startX, startY, endX, endY, board);

        // Check for castling
        boolean isCastlingMove = !hasMoved && dx == 0 && dy == 2 && board.getPiece(startX, startY + dy / 2) == null && board.getPiece(startX, startY + dy) == null;
        if (isCastlingMove) {
            Piece rook = board.getPiece(startX, startY + dy / 2 * 3);
            isCastlingMove = rook instanceof Rook && !rook.hasMoved();
        }

        return (isAdjacentMove || isCastlingMove) && !isUnderAttack;
    }

}
