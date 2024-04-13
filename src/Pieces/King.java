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

    // Override setHasMoved to update hasMoved
    @Override
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
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



    @Override
    public boolean threatensPosition(int x, int y, Board board) {
        // The king threatens a position if it can move to that position
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int newX = x + dx;
                int newY = y + dy;
                if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8 && (board.getPiece(newX, newY) == null || board.getPiece(newX, newY).getColor() != this.getColor())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<int[]> generatePossibleMoves(int startX, int startY, Board board) {
        List<int[]> possibleMoves = new ArrayList<>();
        // All 8 directions
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};
        for (int direction = 0; direction < 8; direction++) {
            int x = startX + dx[direction], y = startY + dy[direction];
            if (x >= 0 && x < 8 && y >= 0 && y < 8 && (board.getPiece(x, y) == null || board.getPiece(x, y).getColor() != this.getColor())) {
                possibleMoves.add(new int[]{x, y});
            }
        }
        return possibleMoves;
    }
}
