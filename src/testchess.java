import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import Pieces.*;
import GameElems.*;

public class testchess extends JFrame {
    private JPanel chessboardPanel;
    private JButton[][] squares;
    private JButton selectedSquare;
    private Piece[][] pieces;
    private Player currentPlayer;

    public testchess() {
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);

        chessboardPanel = new JPanel(new GridLayout(8, 8));
        squares = new JButton[8][8];
        pieces = new Piece[8][8];
        currentPlayer = new Player(PlayerColor.WHITE); // Start with white player

        Board board = new Board();

        // Initialize the chessboard squares
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton square = new JButton();
                square.setPreferredSize(new Dimension(75, 75));
                square.setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.GREEN.darker());
                square.addActionListener(new SquareClickListener(row, col));
                squares[row][col] = square;
                chessboardPanel.add(square);
            }
        }

        // Initialize the chess pieces

        // Add the chessboard panel to the frame
        add(chessboardPanel);
        updateChessboard();
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
                    ImageIcon icon = new ImageIcon(imagePath);
                    Image image = icon.getImage();
                    Image scaledImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    squares[row][col].setIcon(new ImageIcon(scaledImage));
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
            System.out.println(e.getSource());
            JButton clickedSquare = (JButton) e.getSource();
            if (selectedSquare == null) {
                if (pieces[row][col] != null && pieces[row][col].getColor() == currentPlayer.getColor()) {
                    selectedSquare = clickedSquare;
                    selectedSquare.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
                    System.out.println("has moved po kliku? " + pieces[row][col].hasMoved());
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
                System.out.println(pieces[selectedRow][selectedCol].isValidMove(selectedRow, selectedCol, row, col, pieces));
                System.out.println("selectedRow:" + selectedRow);
                System.out.println("selectedCol:" + selectedCol);
                System.out.println("row:" + row + "col:" + col);
                System.out.println("has moved przed ruchem? " + pieces[selectedRow][selectedCol].hasMoved());
                if (pieces[selectedRow][selectedCol].isValidMove(selectedRow, selectedCol, row, col, pieces)) {
                    pieces[row][col] = pieces[selectedRow][selectedCol];
                    pieces[row][col].setHasMoved(true);
                    pieces[selectedRow][selectedCol] = null;
                    currentPlayer = (currentPlayer.getColor() == PlayerColor.WHITE ? new Player(PlayerColor.BLACK) : new Player(PlayerColor.WHITE));
                    System.out.println("has moved po ruchu? " + pieces[row][col].hasMoved());
                }


                // Move the piece to the clicked square


                // Deselect the square
                selectedSquare.setBorder(null);
                selectedSquare = null;

                // Update the chessboard
                updateChessboard();
            }
        }
    }
}