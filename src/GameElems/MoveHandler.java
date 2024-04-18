package GameElems;

import Pieces.*;

import java.awt.*;
import java.util.List;

public class MoveHandler {

    private Board board;
    private SelectedSquare selectedSquare = null;
    public GuiSquare[][] guiBoard;
    public static PlayerColor turn = PlayerColor.WHITE;

    public GuiSquare getSquare(int x, int y){
        return this.guiBoard[x][y];
    }



    public MoveHandler(Board board) {
        this.board = board;
        guiBoard = new GuiSquare[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                guiBoard[i][j] = new GuiSquare();
                guiBoard[i][j].piece = board.getPiece(i, j);
                guiBoard[i][j].color = ((i + j) % 2 == 0 ? Color.WHITE : Color.GREEN.darker());
                guiBoard[i][j].highlighted = false;
            }
        }
    }


    private void resetHighlights() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                guiBoard[i][j].highlighted = false;
            }
        }
    }
    public void printBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = board.getPiece(i,j);
                if (piece != null) {
                    System.out.print(piece.getPieceSymbol() + " ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }

    public void handleClick(int row, int col){
        resetHighlights();
        Piece piece = board.getPiece(row, col);
        if (piece != null && piece.getColor() == turn){
            selectedSquare = new SelectedSquare(row, col);
            List<int[]> possibleMoves = piece.generatePossibleMoves(row, col, board);
            for (int[] move : possibleMoves) {
                guiBoard[move[0]][move[1]].highlighted = true;
            }
        } else {
            if (selectedSquare != null) {
                if (board.getPiece(selectedSquare.row, selectedSquare.col).isValidMove(selectedSquare.row, selectedSquare.col, row, col, board)) {
                    handleEnPassant(selectedSquare.row, selectedSquare.col, row, col);
                    handleCastling(selectedSquare.row, selectedSquare.col, row, col);
                    // Check if the piece is a pawn and if it has moved two squares forward
                    if (board.getPiece(selectedSquare.row, selectedSquare.col) instanceof Pawn && Math.abs(selectedSquare.row - row) == 2) {
                        ((Pawn) board.getPiece(selectedSquare.row, selectedSquare.col)).setHasMovedTwo(true);
                    }
                    board.setPiece(row, col, board.getPiece(selectedSquare.row, selectedSquare.col));
                    board.getPiece(row, col).setHasMoved(true); // Set hasMoved to true after moving the piece
                    board.setPiece(selectedSquare.row, selectedSquare.col, null);
                    if (board.getPiece(row, col) instanceof Pawn && (row == 0 || row == 7)) {
                        board.setPiece(row, col, new Queen(board.getPiece(row, col).getColor()));
                    }
                    turn = (turn == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE);
                    selectedSquare = null;
                    resetHighlights();
                }
            }
        }
        printBoard();
    }

    //when move is en passant, remove the captured pawn
    private void handleEnPassant(int startX, int startY, int endX, int endY) {
        Piece piece = board.getPiece(startX, startY);
        if (piece instanceof Pawn && Math.abs(startY - endY) == 1 && board.getPiece(endX, endY) == null) {
            int capturedPawnRow = startX;
            int capturedPawnCol = endY;
            board.setPiece(capturedPawnRow, capturedPawnCol, null);
        }
    }

    //when move is castling, move the rook also
    private void handleCastling(int startX, int startY, int endX, int endY) {
        Piece piece = board.getPiece(startX, startY);
        if (piece instanceof King && Math.abs(startY - endY) == 2) {
            int rookStartCol = (endY > startY) ? 7 : 0;
            int rookEndCol = (endY > startY) ? 5 : 3;
            board.setPiece(endX, rookEndCol, board.getPiece(endX, rookStartCol));
            board.setPiece(endX, rookStartCol, null);
        }
    }

    private class SelectedSquare {
        public int row;
        public int col;
        SelectedSquare(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

}
