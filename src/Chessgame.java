import Pieces.*;
import GameElems.*;
import javax.swing.*;
import java.io.*;

public class Chessgame {
    private Player currentPlayer;
    private Board board;
    private ChessGUI gui;

    public Chessgame() {
        currentPlayer = new Player(PlayerColor.WHITE);
        board = new Board();
        gui = new ChessGUI(board, currentPlayer, this);
    }

    public void saveGame(String filename) {
        try (PrintWriter writer = new PrintWriter(new File(filename))) {
            // Save the current player
            writer.println(currentPlayer.getColor());

            // Save the state of the board
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    Piece piece = board.getPiece(i, j);
                    if (piece != null) {
                        // Save the piece's color, type, and position
                        writer.println(piece.getColor() + " " + piece.getClass().getSimpleName() + " " + i + " " + j);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadGame(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(filename)))) {
            // Load the current player
            String line = reader.readLine();
            currentPlayer = new Player(PlayerColor.valueOf(line));

            // Clear the board
            board = new Board();

            // Load the state of the board
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                PlayerColor color = PlayerColor.valueOf(parts[0]);
                String pieceType = parts[1];
                int x = Integer.parseInt(parts[2]);
                int y = Integer.parseInt(parts[3]);

                // Create the piece and place it on the board
                Piece piece;
                switch (pieceType) {
                    case "Pawn":
                        piece = new Pawn(color);
                        break;
                    case "Rook":
                        piece = new Rook(color);
                        break;
                    case "Knight":
                        piece = new Knight(color);
                        break;
                    case "Bishop":
                        piece = new Bishop(color);
                        break;
                    case "Queen":
                        piece = new Queen(color);
                        break;
                    case "King":
                        piece = new King(color);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid piece type: " + pieceType);
                }
                board.setPiece(x, y, piece);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Chessgame chessGame = new Chessgame();
            chessGame.gui.setVisible(true);
        });
    }
}