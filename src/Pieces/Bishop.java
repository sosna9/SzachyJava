package Pieces;
import GameElems.Board;

import java.util.List;
import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(PlayerColor color) {
        super(color, 'B');
    }

    @Override
    public boolean isValidMove(int startX, int startY, int endX, int endY, Board board) {
        if (wouldThisMovePutKingInCheck(startX, startY, endX, endY, board)) {
            return false;
        }
        
        // Check if the move is valid for a bishop (can move diagonally)
        if (Math.abs(endX - startX) == Math.abs(endY - startY)) {
            int xDirection = Integer.compare(endX, startX);
            int yDirection = Integer.compare(endY, startY);
            int x = startX + xDirection;
            int y = startY + yDirection;
            while (x != endX && y != endY) {
                if (board.getPiece(x, y)  != null) {
                    return false; // There's a piece in the way
                }
                x += xDirection;
                y += yDirection;
            }
            // Check if the end position is empty or contains an opponent's piece
            return board.getPiece(endX, endY) == null || board.getPiece(endX, endY).getColor() != this.getColor();
        }
        return false;
    }

}