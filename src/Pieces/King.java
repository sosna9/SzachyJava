package Pieces;
import GameElems.Board;

public class King extends Piece {
    private boolean hasMoved; // Keep track if the king has moved

    public King(PlayerColor color) {
        super(color, 'K');
        this.hasMoved = false;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    @Override
    public boolean isValidMove(int startCol, int startRow, int endCol, int endY, Board board) {
        return isAdjacentMove(startCol, startRow, endCol, endY, board) ||
                isKingSideCastle(startCol, startRow, endCol, endY, board) ||
                isQueenSideCastle(startCol, startRow, endCol, endY, board);
    }

    private boolean isAdjacentMove(int startCol, int startRow, int endCol, int endY, Board board) {
        int dRows = Math.abs(startCol - endCol);
        int dCol = Math.abs(startRow - endY);

        return dRows <= 1 && dCol <= 1 && (board.getPiece(endCol, endY)== null || board.getPiece(endCol, endY).getColor() != this.getColor());
    }

    private boolean isKingSideCastle(int startCol, int startRow, int endCol, int endRow, Board board) {
        boolean isKingSideCastle = !hasMoved && startRow - endRow == -2  && endRow == 6 &&
                startCol == endCol &&
                board.getPiece(startCol, startRow + 1) == null &&
                board.getPiece(startCol, startRow + 2) == null &&
                !board.isKingInCheck(this.getColor()) &&
                !wouldThisMovePutKingInCheck(startCol, startRow, startCol, startRow + 1, board);
        if (isKingSideCastle) {
            Piece rook = board.getPiece(startCol, 7);
            isKingSideCastle = rook instanceof Rook && !rook.hasMoved() && rook.getColor() == this.getColor();
        }
        return isKingSideCastle;
    }


    private boolean isQueenSideCastle(int startCol, int startRow, int endCol, int endRow, Board board) {
        boolean isQueenSideCastling = !hasMoved && startRow - endRow == 2 &&
                startCol == endCol &&
                board.getPiece(startCol, startRow -1) == null &&
                board.getPiece(startCol, startRow -2) == null &&
                board.getPiece(startCol, startRow -3) == null &&
                !board.isKingInCheck(this.getColor()) &&
                !wouldThisMovePutKingInCheck(startCol, startRow, startCol, startRow - 1, board);
        if (isQueenSideCastling) {
            Piece rook = board.getPiece(startCol, 0);
            isQueenSideCastling = rook instanceof Rook && !rook.hasMoved() && rook.getColor()== this.getColor();
        }
        return isQueenSideCastling;
    }
}