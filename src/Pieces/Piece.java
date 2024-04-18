package Pieces;
import GameElems.Board;

import java.util.ArrayList;
import java.util.List;

abstract public class Piece {
    private PlayerColor color;
    protected char symbol = ' ';
    private boolean hasMoved = false;

    public boolean hasMoved() {
        return hasMoved;
    }
    public boolean getHasMovedTwo() {
        return false;
    }
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
    public void setHasMovedTwo(boolean hasMovedTwo) {
    }
    public boolean getHasMoved() {
        return this.hasMoved;
    }

    public char getPieceSymbol(){
        return symbol;
    }

    public Piece(PlayerColor color, char symbol) {
        this.color = color;
        this.hasMoved = false;
        this.symbol = symbol;
    }

    public List<int[]> generatePossibleMoves(int startX, int startY, Board board){
        List<int[]> possibleMoves = new ArrayList<>();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (board.getPiece(startX,startY).isValidMove(startX, startY, x, y, board) && !wouldThisMovePutKingInCheck(startX, startY, x, y, board)) {
                    System.out.println("Adding move: " + x + " " + y);
                    possibleMoves.add(new int[]{x, y});
                }
            }
        }
        return possibleMoves;
    }

    public boolean wouldThisMovePutKingInCheck(int startX, int startY, int endX, int endY, Board board) {
        // Create a copy of the board
        Board copiedBoard = new Board();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                copiedBoard.setPiece(x, y,  board.getPiece(x,y));
            }
        }
        // Make the move on the copied board
        copiedBoard.setPiece(endX, endY, copiedBoard.getPiece(startX, startY));
        copiedBoard.setPiece(startX,startY, null);

        // Check if the king is in check on the copied board
        return copiedBoard.isKingInCheck(board.getPiece(startX,startY).getColor());
    }



    public abstract boolean isValidMove(int startX, int startY, int endX, int endY, Board board);


    public PlayerColor getColor() {
        return color;
    }
}