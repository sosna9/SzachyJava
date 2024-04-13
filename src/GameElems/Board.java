package GameElems;
import Pieces.*;

public class Board {
    private Piece[][] pieces;

    public Board() {
        pieces = new Piece[8][8];
        initializePieces();
    }

    private void initializePieces() {
        // Initialize pawns
        for (int i = 0; i < 8; i++) {
            pieces[1][i] = new Pawn(PlayerColor.WHITE);
            pieces[6][i] = new Pawn(PlayerColor.BLACK);
        }

        // Initialize pieces for white player
        pieces[0][0] = new Rook(PlayerColor.WHITE);
        pieces[0][1] = new Knight(PlayerColor.WHITE);
        pieces[0][2] = new Bishop(PlayerColor.WHITE);
        pieces[0][3] = new Queen(PlayerColor.WHITE);
        pieces[0][4] = new King(PlayerColor.WHITE);
        pieces[0][5] = new Bishop(PlayerColor.WHITE);
        pieces[0][6] = new Knight(PlayerColor.WHITE);
        pieces[0][7] = new Rook(PlayerColor.WHITE);

        // Initialize pieces for black player
        pieces[7][0] = new Rook(PlayerColor.BLACK);
        pieces[7][1] = new Knight(PlayerColor.BLACK);
        pieces[7][2] = new Bishop(PlayerColor.BLACK);
        pieces[7][3] = new Queen(PlayerColor.BLACK);
        pieces[7][4] = new King(PlayerColor.BLACK);
        pieces[7][5] = new Bishop(PlayerColor.BLACK);
        pieces[7][6] = new Knight(PlayerColor.BLACK);
        pieces[7][7] = new Rook(PlayerColor.BLACK);
    }

    public boolean isKingInCheck(PlayerColor color) {
        int kingX = -1, kingY = -1;
        // Find the king of the given color
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = pieces[x][y];
                if (piece != null && piece.getColor() == color && piece instanceof King) {
                    kingX = x;
                    kingY = y;
                    break;
                }
            }
            if (kingX != -1) {
                break;
            }
        }

        // Check if the king is in check
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = pieces[x][y];
                if (piece != null && piece.getColor() != color && piece.isValidMove(x, y, kingX, kingY, this)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Piece[][] getPieces() {
        return this.pieces;
    }

    public Piece getPiece(int row, int col) {
        return pieces[row][col];
    }

    public void setPiece(int row, int col, Piece piece) {
        pieces[row][col] = piece;
    }
}