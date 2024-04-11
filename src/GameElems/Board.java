package GameElems;
import Pieces.*;
public class Board {
    public Piece[][] pieces;

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
