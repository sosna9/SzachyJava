package GameElems;

import Pieces.*;
import sun.awt.image.GifImageDecoder;

import java.awt.*;

public class BoardLogicHandler {

    private Board board;
    public GuiSquare[][] guiboard;

    public GuiSquare getSquare(int x, int y){
        return this.guiboard[x][y];
    }


    public BoardLogicHandler(Board board) {
        this.board = board;
        guiboard = new GuiSquare[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                guiboard[i][j] = new GuiSquare();
                guiboard[i][j].piece = board.getPiece(i, j);
                guiboard[i][j].color = ((i + j) % 2 == 0 ? Color.WHITE : Color.GREEN.darker());
                guiboard[i][j].highlighted = false;
            }
        }
    }


    public void handleClick(){

    }



}
