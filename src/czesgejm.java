import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import Pieces.*;
import GameElems.*;

public class czesgejm extends JFrame {
    private JPanel chessboardPanel;

    private JPanel menuPanel;
    private JPanel moveListPanel;
    private JList<String> moveList;
    private JButton[][] squares;
    private JButton selectedSquare;
    //private Piece[][] pieces;
    private Player currentPlayer;
    private Board board;

    public czesgejm() {
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);

        chessboardPanel = new JPanel(new GridLayout(8, 8));
        squares = new JButton[8][8];

        // Initialize the menu panel
        menuPanel = new JPanel(new GridLayout(10, 1));
        menuPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Initialize the move list panel
        moveListPanel = new JPanel(new GridLayout(1, 1));
        moveListPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Initialize the move list
        DefaultListModel<String> moveListModel = new DefaultListModel<>();
        moveList = new JList<>(moveListModel);
        moveListPanel.add(new JScrollPane(moveList));


        currentPlayer = new Player(PlayerColor.WHITE); // Start with white player
        board = new Board();
        System.out.println(board.getPieces()[1][1]);



        // Initialize the chessboard squares
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton square = new JButton();
                square.setPreferredSize(new Dimension(75, 75));
                square.setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.GREEN.darker());
                square.addActionListener(new SquareClickListener(row, col, board));
                squares[row][col] = square;
                chessboardPanel.add(square);
            }
        }


        // Initialize the chess pieces
        //initializePieces();

        // Add the chessboard panel to the frame
        add(chessboardPanel);
        updateChessboard(board);
    }


    private void updateChessboard(Board board) {
        // Clear the chessboard
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].setIcon(null);
            }
        }
        // Add pieces to the chessboard
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board.getPieces()[row][col] != null) {
                    String imagePath = "obraski/" + board.getPieces()[row][col].getColor().toString().toLowerCase() +
                            board.getPieces()[row][col].getClass().getSimpleName() + ".png";
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
        private Board board;
        public SquareClickListener(int row, int col, Board board) {
            this.row = row;
            this.col = col;
            this.board = board;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(e.getSource());
            JButton clickedSquare = (JButton) e.getSource();
            if (selectedSquare == null) {
                if (board.pieces[row][col] != null && board.pieces[row][col].getColor() == currentPlayer.getColor()) {
                    selectedSquare = clickedSquare;
                    selectedSquare.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
                    System.out.println("has moved po kliku? " + board.pieces[row][col].hasMoved());
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

                System.out.println("selectedRow:" + selectedRow);
                System.out.println("selectedCol:" + selectedCol);
                System.out.println("row:" +  row + "col:"+  col);
                System.out.println("has moved przed ruchem? " + board.pieces[selectedRow][selectedCol].hasMoved());
                if(board.pieces[selectedRow][selectedCol].isValidMove(selectedRow, selectedCol, row, col, board.pieces)) {
                    board.pieces[row][col] = board.pieces[selectedRow][selectedCol];
                    board.pieces[row][col].setHasMoved(true);
                    board.pieces[selectedRow][selectedCol] = null;
                    System.out.println("has moved po ruchu? " + board.pieces[row][col].hasMoved());
                    currentPlayer = (currentPlayer.getColor() == PlayerColor.WHITE ? new Player(PlayerColor.BLACK) : new Player(PlayerColor.WHITE));
                }


                // Move the piece to the clicked square


                // Deselect the square
                selectedSquare.setBorder(null);
                selectedSquare = null;
                // Update the chessboard
                updateChessboard(board);
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



