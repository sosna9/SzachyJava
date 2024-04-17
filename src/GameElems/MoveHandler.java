package GameElems;

import Pieces.Piece;
import Pieces.PlayerColor;

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



    public void handleClick(int row, int col){
        if (selectedSquare == null) {
            Piece piece = board.getPiece(row, col);
            if (board.getPiece(row, col) != null && board.getPiece(row, col).getColor() == turn){
                selectedSquare = new SelectedSquare(row, col);
                System.out.println("Clicked on  i zaznaczam:  row" + row + ", col " + col);
                List<int[]> possibleMoves = board.getPiece(row, col).generatePossibleMoves(row, col, board);
                for (int[] move : possibleMoves) {
                    guiBoard[move[0]][move[1]].highlighted = true;
                }
            }
        } else {
            if (board.getPiece(row, col).getColor() == turn) {
                selectedSquare = new SelectedSquare(row, col);
            } else {
                Piece piece = board.getPiece(row, col);
                System.out.println("Zaznaczony row przed clickiem" + selectedSquare.row + ", col " + selectedSquare.col);
                System.out.println("i teraz Clicked on row" + row + ", col " + col);
                System.out.println("is king in check " + board.isKingInCheck(turn) + turn);
                if (board.getPiece(selectedSquare.row, selectedSquare.col).isValidMove(selectedSquare.row, selectedSquare.col, row, col, board)) {
                    System.out.println("Zaznaczony");
                    board.setPiece(row, col, board.getPiece(selectedSquare.row, selectedSquare.col));
                    board.setPiece(selectedSquare.row, selectedSquare.col, null);
                    turn = (turn == PlayerColor.WHITE ? PlayerColor.BLACK : PlayerColor.WHITE);
                selectedSquare = null;
                }
                selectedSquare = null;
            }
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
