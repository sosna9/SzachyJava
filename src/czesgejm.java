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
        setSize(1200, 600);

        chessboardPanel = new JPanel(new GridLayout(9, 9));
        squares = new JButton[8][8];

        // Initialize the menu panel
        menuPanel = createMenuPanel();
        add(menuPanel, BorderLayout.EAST);

        // Initialize the move list panel
        moveListPanel = new JPanel(new GridLayout(1, 1));
        moveListPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        //Initialize the move list
        DefaultListModel<String> moveListModel = new DefaultListModel<>();
        moveList = new JList<>(moveListModel);
        moveListPanel.add(new JScrollPane(moveList));


        currentPlayer = new Player(PlayerColor.WHITE); // Start with white player
        board = new Board();
        System.out.println(board.getPieces()[1][1]);


        JLabel space = new JLabel(String.valueOf((char)(' ')));
        space.setHorizontalAlignment(SwingConstants.CENTER);
        chessboardPanel.add(space);
        for (int i = 0; i < 8; i++) {
            JLabel label = new JLabel(String.valueOf((char)('A' + i)));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            chessboardPanel.add(label);
        }

        // Add squares with buttons
        for (int row = 0; row < 8; row++) {
            // Add row number
            JLabel rowLabel = new JLabel(String.valueOf(8 - row));
            rowLabel.setHorizontalAlignment(SwingConstants.CENTER);
            chessboardPanel.add(rowLabel);

            for (int col = 0; col < 8; col++) {
                JButton square = new JButton();
                square.setPreferredSize(new Dimension(75, 75));
                square.setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.GREEN.darker());
                square.addActionListener(new SquareClickListener(row, col, board, moveListModel));
                squares[row][col] = square;
                chessboardPanel.add(square);
            }
        }

        // Initialize the chess pieces
        //initializePieces();

        // Add the chessboard panel to the frame
        add(chessboardPanel);
        add(moveListPanel,  BorderLayout.WEST);
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
        private DefaultListModel<String> moveListModel; // Add moveListModel as a member

        public SquareClickListener(int row, int col, Board board, DefaultListModel<String> moveListModel) {
            this.row = row;
            this.col = col;
            this.board = board;
            this.moveListModel = moveListModel; // Initialize moveListModel
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedSquare = (JButton) e.getSource();
            if (selectedSquare == null) {
                if (board.pieces[row][col] != null && board.pieces[row][col].getColor() == currentPlayer.getColor()) {
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

                if (board.pieces[selectedRow][selectedCol].isValidMove(selectedRow, selectedCol, row, col, board.pieces)) {
                    // Convert coordinates to algebraic notation

                    String endSquare = String.format("%c%d", 'a' + col, 8 - row);
                    String move = "";

                    // Check for special moves (e.g., castling)
                    if (board.pieces[selectedRow][selectedCol] instanceof King && Math.abs(selectedCol - col) == 2) {
                        move = (col > selectedCol) ? "O-O" : "O-O-O";
                    } else {
                        // Determine piece symbol
                        char pieceSymbol = board.pieces[selectedRow][selectedCol].getPieceSymbol();
                        move = (pieceSymbol == 'P') ? endSquare : pieceSymbol + endSquare;
                    }

                    // Add move to the move list
                    moveListModel.addElement(move); // Use moveListModel here

                    // Make the move on the board
                    board.pieces[row][col] = board.pieces[selectedRow][selectedCol];
                    board.pieces[row][col].setHasMoved(true);
                    board.pieces[selectedRow][selectedCol] = null;

                    // Switch to the next player
                    currentPlayer = (currentPlayer.getColor() == PlayerColor.WHITE) ?
                            new Player(PlayerColor.BLACK) : new Player(PlayerColor.WHITE);
                }

                // Deselect the square
                selectedSquare.setBorder(null);
                selectedSquare = null;
                // Update the chessboard
                updateChessboard(board);
            }
        }
    }



    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(10, 2));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Add menu items
        JMenuItem startTimerItem = new JMenuItem("Start Timer");
        JMenuItem stopTimerItem = new JMenuItem("Stop Timer");
        JMenuItem startGameItem = new JMenuItem("Start Game");
        JMenuItem loadGameItem = new JMenuItem("Load Game");
        JMenuItem saveGameItem = new JMenuItem("Save Game");


        JMenu timerMenu = new JMenu("Timer");
        timerMenu.add(startTimerItem);
        timerMenu.add(stopTimerItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(timerMenu);
        menuBar.add(startGameItem);
        menuBar.add(loadGameItem);
        menuBar.add(saveGameItem);

        panel.add(menuBar);

        return panel;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            czesgejm chessGame = new czesgejm();
            chessGame.setVisible(true);
        });
    }
}



