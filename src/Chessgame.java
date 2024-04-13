import Pieces.*;
import GameElems.*;
import javax.swing.*;

public class Chessgame {
    private Player currentPlayer;
    private Board board;
    private ChessGUI gui;

    public Chessgame() {
        currentPlayer = new Player(PlayerColor.WHITE);
        board = new Board();
        gui = new ChessGUI(board, currentPlayer);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Chessgame chessGame = new Chessgame();
            chessGame.gui.setVisible(true);
        });
    }
}