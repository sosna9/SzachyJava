import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import Pieces.*;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.InputStream;
import javafx.scene.layout.StackPane;
//import javax.swing.*;

import java.io.File;


public class ChessGame extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize the game
        Board board = new Board();
        Player player1 = new Player(Piece.Color.WHITE);
        Player player2 = new Player(Piece.Color.BLACK);
        Game game = new Game(player1, player2, board);

        // Initialize the GUI
        ChessGUI gui = new ChessGUI(game);

        // Set up the primary stage
        primaryStage.setTitle("Chess Game");
        primaryStage.setScene(new Scene(gui, 800, 800));
        primaryStage.show();
    }
}

class Game {
    private Player player1;
    private Player player2;
    private Board board;

    public Game(Player player1, Player player2, Board board) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }
}

class Player {
    private Piece.Color color;
    public Player(Piece.Color color) {
        this.color = color;
    }
    // Add methods for player actions here
}

class Board {
    private Piece[][] squares;

    public Board() {
        squares = new Piece[8][8];
        initializePieces();
    }

    private void initializePieces() {
        // Initialize pawns
        for (int i = 0; i < 8; i++) {
            squares[1][i] = new Pawn(Piece.Color.WHITE);
            squares[6][i] = new Pawn(Piece.Color.BLACK);
        }

        // Initialize pieces for white player
        squares[0][0] = new Rook(Piece.Color.WHITE);
        squares[0][1] = new Knight(Piece.Color.WHITE);
        squares[0][2] = new Bishop(Piece.Color.WHITE);
        squares[0][3] = new Queen(Piece.Color.WHITE);
        squares[0][4] = new King(Piece.Color.WHITE);
        squares[0][5] = new Bishop(Piece.Color.WHITE);
        squares[0][6] = new Knight(Piece.Color.WHITE);
        squares[0][7] = new Rook(Piece.Color.WHITE);

        // Initialize pieces for black player
        squares[7][0] = new Rook(Piece.Color.BLACK);
        squares[7][1] = new Knight(Piece.Color.BLACK);
        squares[7][2] = new Bishop(Piece.Color.BLACK);
        squares[7][3] = new Queen(Piece.Color.BLACK);
        squares[7][4] = new King(Piece.Color.BLACK);
        squares[7][5] = new Bishop(Piece.Color.BLACK);
        squares[7][6] = new Knight(Piece.Color.BLACK);
        squares[7][7] = new Rook(Piece.Color.BLACK);
        System.out.println(squares[1][1].getClass() +" " + squares[1][1].getColor());
    }

    public Piece getPiece(int row, int col) {
        return squares[row][col];
    }

}


// Define other piece classes similarly (e.g., Rook, Knight, Bishop, Queen, King)

class ChessGUI extends GridPane {
    private Game game;

    public ChessGUI(Game game) {
        this.game = game;
        // Initialize GUI components and set up the board display
        getChildren().add(createChessboard(game));
    }

    private GridPane createChessboard(Game game) {
        GridPane chessboard = new GridPane();
        int squareSize = 75;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                StackPane squareAndPiecePane = new StackPane(); // StackPane to overlay the square and piece
                squareAndPiecePane.setPrefSize(squareSize, squareSize);

                ImageView square = new ImageView();
                square.setFitWidth(squareSize);
                square.setFitHeight(squareSize);
                String imageUrl = ((row + col) % 2 == 0) ? "obraski/BlackSquare.png" : "obraski/WhiteSquare.png";
                square.setImage(new Image(new File(imageUrl).toURI().toString()));

                squareAndPiecePane.getChildren().add(square); // Add the square to the StackPane

                Piece piece = game.getBoard().getPiece(row, col);
                if (piece != null) { // If there's a piece at this position
                    String pieceImageUrl = getImageUrlForPiece(piece);
                    ImageView pieceImageView = new ImageView(new Image(new File(pieceImageUrl).toURI().toString()));
                    pieceImageView.setFitWidth(55);
                    pieceImageView.setFitHeight(55);
                    squareAndPiecePane.getChildren().add(pieceImageView); // Add the piece on top of the square
                }

                chessboard.add(squareAndPiecePane, col, row); // Add the StackPane to the chessboard
            }
        }
        return chessboard;
    }

    private String getImageUrlForPiece(Piece piece) {
        String color = (piece.getColor() == Piece.Color.WHITE) ? "White" : "Black";
        String type = piece.getClass().getSimpleName();
        return "obraski/" + color + type + ".png";
    }
}