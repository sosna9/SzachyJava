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

    public char getPieceSymbol(){
        return symbol;
    }

    public Piece(PlayerColor color, char symbol) {
        this.color = color;
        this.hasMoved = false;
        this.symbol = symbol;
    }

    public abstract List<int[]> generatePossibleMoves(int startX, int startY, Board board);

    public boolean wouldThisMovePutKingInCheck(int startX, int startY, int endX, int endY, Board board) {
        // Create a copy of the board
        Board copiedBoard = new Board();
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                copiedBoard.setPiece(x, y,  board.getPiece(x,y));;
            }
        }

        // Make the move on the copied board
        copiedBoard.setPiece(endX, endY, copiedBoard.getPiece(startX, startY));
        copiedBoard.setPiece(startX,startY, null);

        // Find the position of the king on the copied board
        int kingX = -1, kingY = -1;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Piece piece = copiedBoard.getPiece(x,y);
                if (piece != null && piece.getColor() == this.getColor() && piece instanceof King) {
                    kingX = x;
                    kingY = y;
                    break;
                }
            }
            if (kingX != -1) {
                break;
            }
        }

        // If the king's position is not found, return false
        if (kingX == -1 || kingY == -1) {
            return false;
        }

        // Check if the king is in check on the copied board
        return copiedBoard.isKingInCheck(color);
    }

    public abstract boolean isValidMove(int startX, int startY, int endX, int endY, Board board);

    public PlayerColor getColor() {
        return color;
    }

    public abstract boolean threatensPosition(int x, int y, Board board);
}