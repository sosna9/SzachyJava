package GameElems;

import Pieces.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MoveHandler {

    private final Board board;
    private SelectedSquare selectedSquare = null;
    private GuiSquare[][] guiBoard;
    private List<String> moveStrings = new ArrayList<>(); // List to store the move strings
    public static PlayerColor turn = PlayerColor.WHITE;


    public GuiSquare getSquare(int x, int y){
        return this.guiBoard[x][y];
    }

    public MoveHandler(Board board) {
        this.board = board;
        initializeGuiBoard();
    }

    private void initializeGuiBoard() {
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

    public void handleClick(int row, int col){
        resetHighlights();
        Piece piece = board.getPiece(row, col);
        if (piece != null && piece.getColor() == turn){
            handlePieceSelection(row, col, piece);
        } else {
            handleMove(row, col);
        }
    }

    public List<String> getMoveStrings() {
        return moveStrings;
    }
    public void clearMoveStrings() {
        moveStrings.clear();
    }
    private void handlePieceSelection(int row, int col, Piece piece) {
        selectedSquare = new SelectedSquare(row, col);
        List<int[]> possibleMoves = piece.generatePossibleMoves(row, col, board);
        for (int[] move : possibleMoves) {
            guiBoard[move[0]][move[1]].highlighted = true;
        }
    }

    private void handleMove(int row, int col) {
        if (selectedSquare != null) {
            Piece piece = board.getPiece(selectedSquare.row, selectedSquare.col);
            if (piece.isValidMove(selectedSquare.row, selectedSquare.col, row, col, board)
            && !piece.wouldThisMovePutKingInCheck(selectedSquare.row, selectedSquare.col, row, col, board)) {
                executeMove(selectedSquare.row, selectedSquare.col, row, col);
                // Check if the piece is a pawn and has moved two squares forward
                if (piece instanceof Pawn && Math.abs(selectedSquare.row - row) == 2) {
                    piece.setHasMovedTwo(true);
                }
                turn = (turn == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE);
                resetHighlights();
            }
            selectedSquare = null; //unselect Square
        }
    }

    private void executeMove(int startX, int startY, int endX, int endY) {
        Piece startPiece = board.getPiece(startX, startY);
        Piece capturedPiece = board.getPiece(endX, endY);

        String startSquare = (char) ('a' + startY) + Integer.toString(8 - startX);
        String endSquare = (char) ('a' + endY) + Integer.toString(8 - endX);
        String moveString;

        handleEnPassant(startX, startY, endX, endY);
        handleCastling(startX, startY, endX, endY);
        handlePawnPromotion(startX, startY, endX, endY);

        board.setPiece(endX, endY, startPiece);
        if (startPiece != null) {
            startPiece.setHasMoved(true); // Set hasMoved to true after moving the piece
        }
        board.setPiece(startX, startY, null);

        if (startPiece instanceof King && Math.abs(startY - endY) == 2) {
            if (endY > startY) {
                moveString = "O-O"; // Kingside castling
            } else {
                moveString = "O-O-O"; // Queenside castling
            }
        } else {
            if (startPiece instanceof Pawn) {
                if (capturedPiece != null) { // Check if there's a piece at the end square
                    moveString = startSquare.charAt(0) + "x" + endSquare; // Pawn capture
                } else {
                    moveString = endSquare; // Pawn move
                }
            } else {
                if (capturedPiece != null) {
                    moveString = startPiece.getPieceSymbol() + "x" + endSquare; // Piece capture
                } else {
                    moveString = startPiece.getPieceSymbol() + endSquare; // Piece move
                }
            }
        }

        moveStrings.add(moveString); // Add the move string to the list
    }

    //when move is en passant, remove the captured pawn
    private void handleEnPassant(int startX, int startY, int endX, int endY) {
        Piece piece = board.getPiece(startX, startY);
        if (piece instanceof Pawn && Math.abs(startY - endY) == 1 && board.getPiece(endX, endY) == null) {
            board.setPiece(startX, endY, null);
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

    //when a pawn reaches the end of the board, promote it to a queen
    private void handlePawnPromotion(int startX, int startY, int endX, int endY) {
        Piece piece = board.getPiece(startX, startY);
        if (piece instanceof Pawn && (endX == 0 || endX == 7)) {
            board.setPiece(endX, endY, new Queen(piece.getColor()));
        }
    }

    public PlayerColor getCurrentTurn() {
        return turn;
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