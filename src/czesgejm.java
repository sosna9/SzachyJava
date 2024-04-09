import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Pieces.*;

public class czesgejm extends JFrame {
    private JPanel chessboardPanel;
    private JButton[][] squares;
    private JButton selectedSquare;
    private Piece[][] pieces;

    public czesgejm() {
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);

        chessboardPanel = new JPanel(new GridLayout(8, 8));
        squares = new JButton[8][8];
        pieces = new Piece[8][8];

        // Initialize the chessboard squares
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton square = new JButton();
                square.setPreferredSize(new Dimension(75, 75));
                square.setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.GRAY);
                square.addActionListener(new SquareClickListener(row, col));
                squares[row][col] = square;
                chessboardPanel.add(square);
            }
        }

        // Initialize the chess pieces
        initializePieces();

        // Add the chessboard panel to the frame
        add(chessboardPanel);
    }

    private void initializePieces() {
        // Initialize pawns
        for (int col = 0; col < 8; col++) {
            pieces[1][col] = new Pawn(Piece.Color.WHITE);
            pieces[6][col] = new Pawn(Piece.Color.BLACK);
        }

        // Initialize pieces for white player
        pieces[0][0] = new Rook(Piece.Color.WHITE);
        pieces[0][1] = new Knight(Piece.Color.WHITE);
        pieces[0][2] = new Bishop(Piece.Color.WHITE);
        pieces[0][3] = new Queen(Piece.Color.WHITE);
        pieces[0][4] = new King(Piece.Color.WHITE);
        pieces[0][5] = new Bishop(Piece.Color.WHITE);
        pieces[0][6] = new Knight(Piece.Color.WHITE);
        pieces[0][7] = new Rook(Piece.Color.WHITE);

        // Initialize pieces for black player
        pieces[7][0] = new Rook(Piece.Color.BLACK);
        pieces[7][1] = new Knight(Piece.Color.BLACK);
        pieces[7][2] = new Bishop(Piece.Color.BLACK);
        pieces[7][3] = new Queen(Piece.Color.BLACK);
        pieces[7][4] = new King(Piece.Color.BLACK);
        pieces[7][5] = new Bishop(Piece.Color.BLACK);
        pieces[7][6] = new Knight(Piece.Color.BLACK);
        pieces[7][7] = new Rook(Piece.Color.BLACK);
    }

    private void updateChessboard() {
        // Clear the chessboard
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].setIcon(null);
            }
        }
        // Add pieces to the chessboard
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (pieces[row][col] != null) {
                    String imagePath = "obraski/" + pieces[row][col].getColor().toString().toLowerCase() +
                            pieces[row][col].getClass().getSimpleName() + ".png";
                    squares[row][col].setIcon(new ImageIcon(imagePath));
                }
            }
        }
    }

    private class SquareClickListener implements ActionListener {
        private int row;
        private int col;

        public SquareClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedSquare = (JButton) e.getSource();
            if (selectedSquare == null) {
                if (pieces[row][col] != null) {
                    selectedSquare = clickedSquare;
                    selectedSquare.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
                }
            } else {
                int selectedRow = -1;
                int selectedCol = -1;
                // Find the coordinates of the selected square
                for (int r = 0; r < 8; r++) {
                    for (int c = 0; c < 8; c++) {
                        if (squares[r][c] == selectedSquare) {
                            selectedRow = r;
                            selectedCol = c;
                            break;
                        }
                    }
                }
                // Move the piece to the clicked square
                pieces[row][col] = pieces[selectedRow][selectedCol];
                pieces[selectedRow][selectedCol] = null;
                // Deselect the square
                selectedSquare.setBorder(null);
                selectedSquare = null;
                // Update the chessboard
                updateChessboard();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            czesgejm chessGame = new czesgejm();
            chessGame.setVisible(true);
        });
    }
}



