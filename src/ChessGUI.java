import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import Pieces.*;
import GameElems.*;
import java.util.List;
import javax.swing.JFileChooser;
import java.io.*;

public class ChessGUI extends JFrame {
    private final JPanel chessboardPanel;
    private final JPanel menuPanel;
    private final JPanel moveListPanel;
    private JTextArea moveList;
    private JButton[][] squares;
    private JButton selectedSquare;
    private Player currentPlayer;
    private Board board;

    private MoveHandler logic;

    private JLabel statusLabel; // winner banner

    private Timer whiteTimer;
    private Timer blackTimer;
    private JLabel whiteTimeLabel;
    private JLabel blackTimeLabel;
    private int whiteTime;
    private int blackTime;

    private Chessgame chessgame;


    public ChessGUI(Board board, Player currentPlayer, Chessgame chessgame, MoveHandler logic) {
        this.board = board;
        this.logic = logic;
        this.currentPlayer = currentPlayer;
        this.chessgame = chessgame;

        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);

        whiteTime = 1800; // 30 minutes in seconds
        blackTime = 1800; // 30 minutes in seconds

        whiteTimeLabel = new JLabel();
        blackTimeLabel = new JLabel();
        updateTimerLabels();

        whiteTimer = new Timer(1000, e -> {
            whiteTime--;
            updateTimerLabels();
            if (whiteTime <= 0) {
                whiteTimer.stop();
                statusLabel.setText("Black Won");
            }
        });

        blackTimer = new Timer(1000, e -> {
            blackTime--;
            updateTimerLabels();
            if (blackTime <= 0) {
                blackTimer.stop();
                statusLabel.setText("White Won");
            }
        });

        menuPanel = createMenuPanel(); // Initialize menuPanel here
        menuPanel.add(whiteTimeLabel);
        menuPanel.add(blackTimeLabel);

        chessboardPanel = new JPanel(new GridLayout(9, 9));
        squares = new JButton[8][8];

        statusLabel = new JLabel(); // Initialize the label
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        menuPanel.add(statusLabel); // Add the label to the menu panel

        moveListPanel = new JPanel(new GridLayout(1, 1));
        moveListPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        moveListPanel.setPreferredSize(new Dimension(200, getHeight()));


        moveList = new JTextArea(); // Initialize JTextArea
        moveList.setEditable(false); // Prevent user from editing the text
        moveList.setLineWrap(true); // Set line wrap to true
        moveList.setWrapStyleWord(true); // Set word wrap to true
        moveListPanel.add(moveList); // Add JTextArea to the panel


        for (int row = 0; row < 8; row++) {
            JLabel rowLabel = new JLabel(String.valueOf(8 - row));
            rowLabel.setHorizontalAlignment(SwingConstants.CENTER);
            chessboardPanel.add(rowLabel);

            for (int col = 0; col < 8; col++) {
                JButton square = new JButton();
                square.setPreferredSize(new Dimension(75, 75));
                square.setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.GREEN.darker());
                square.addActionListener(new SquareClickListener(row, col, board));
                squares[row][col] = square;
                chessboardPanel.add(square);
            }
        }

        JLabel space = new JLabel(String.valueOf((char)(' ')));
        space.setHorizontalAlignment(SwingConstants.CENTER);
        chessboardPanel.add(space);
        for (int i = 0; i < 8; i++) {
            JLabel label = new JLabel(String.valueOf((char)('A' + i)));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            chessboardPanel.add(label);
        }

        add(chessboardPanel);
        add(moveListPanel,  BorderLayout.WEST);
        add(menuPanel, BorderLayout.EAST);
        updateChessboard(board);
    }

    private void updateTimerLabels() {
        whiteTimeLabel.setText("White: " + formatTime(whiteTime));
        blackTimeLabel.setText("Black: " + formatTime(blackTime));
    }

    private String formatTime(int timeInSeconds) {
        int minutes = timeInSeconds / 60;
        int seconds = timeInSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void newBetterUpdateChessboard(GuiSquare[][] guiboard) {
        if (board.isCheckmate(currentPlayer.getColor())) {
            statusLabel.setText(currentPlayer.getColor() + " Won"); // Update the label
            whiteTimer.stop();
            blackTimer.stop();
        } else {
            if (MoveHandler.turn == PlayerColor.WHITE) {
                whiteTimer.start();
                blackTimer.stop();
            } else {
                blackTimer.start();
                whiteTimer.stop();
            }
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].setIcon(null);
            }
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board.getPiece(row, col) != null) {
                    String imagePath = "obrazki/" + board.getPiece(row, col).getColor().toString().toLowerCase() +
                            board.getPiece(row, col).getClass().getSimpleName() + ".png";
                    ImageIcon icon = new ImageIcon(imagePath);
                    Image image = icon.getImage();
                    Image scaledImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    squares[row][col].setIcon(new ImageIcon(scaledImage));
                }
            }
        }
    }
    private void updateChessboard(Board board) {
        if (board.isCheckmate(currentPlayer.getColor())) {
            statusLabel.setText(currentPlayer.getColor() + " Won"); // Update the label
            whiteTimer.stop();
            blackTimer.stop();
        } else {
            if (currentPlayer.getColor() == PlayerColor.WHITE) {
                whiteTimer.start();
                blackTimer.stop();
            } else {
                blackTimer.start();
                whiteTimer.stop();
            }
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].setIcon(null);
            }
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board.getPiece(row, col) != null) {
                    String imagePath = "obrazki/" + board.getPiece(row, col).getColor().toString().toLowerCase() +
                            board.getPiece(row, col).getClass().getSimpleName() + ".png";
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

        public void clearHighlighting() {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    squares[row][col].setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.GREEN.darker());
                }
            }
        }

        public void actionGui(ActionEvent e){
            // new nowe move gui handle move
            Board mockboard = null;
            JButton clickedSquare = (JButton) e.getSource();
            if (board.getPiece(row, col) != null && board.getPiece(row, col).getColor() == currentPlayer.getColor()) {
                selectedSquare = clickedSquare;
                selectedSquare.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
                List<int[]> possibleMoves = board.getPiece(row, col).generatePossibleMoves(row, col, board);
                for (int[] move : possibleMoves) {
                    squares[move[0]][move[1]].setBackground(Color.YELLOW);
                }
            } else {
                clearHighlighting();
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedSquare = (JButton) e.getSource();
            logic.handleClick(row, col);

            if (selectedSquare == null) {
                if (board.getPiece(row, col) != null && board.getPiece(row, col).getColor() == currentPlayer.getColor()) {
                    selectedSquare = clickedSquare;
                    selectedSquare.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
                    List<int[]> possibleMoves = board.getPiece(row, col).generatePossibleMoves(row, col, board);
                    for (int[] move : possibleMoves) {
                        squares[move[0]][move[1]].setBackground(Color.YELLOW);
                    }
                }
            } else {
                clearHighlighting();
                int selectedRow = -1;
                int selectedCol = -1;
                for (int r = 0; r < 8; r++) {
                    for (int c = 0; c < 8; c++) {
                        if (squares[r][c] == selectedSquare) {
                            selectedRow = r;
                            selectedCol = c;
                            break;
                        }
                    }
                }

                if (board.getPiece(selectedRow, selectedCol).isValidMove(selectedRow, selectedCol, row, col, board)) {
                    String startSquare = String.format("%c%d", 'a' + selectedCol, 8 - selectedRow);
                    String endSquare = String.format("%c%d", 'a' + col, 8 - row);
                    String moveStringg = "";

                    // Check for castling
                    if (board.getPiece(selectedRow, selectedCol) instanceof King && Math.abs(selectedCol - col) == 2) {
                        if (col > selectedCol) {
                            moveStringg = "O-O"; // Kingside castling
                        } else {
                            moveStringg = "O-O-O"; // Queenside castling
                        }
                    } else {
                        // Standard move notation
                        if (board.getPiece(selectedRow, selectedCol) instanceof Pawn) {
                            if (board.getPiece(row, col) != null) {
                                moveStringg = startSquare.charAt(0) + "x" + endSquare; // Pawn capture
                            } else {
                                moveStringg = endSquare; // Pawn move
                            }
                        } else {
                            if (board.getPiece(row, col) != null) {
                                moveStringg = board.getPiece(selectedRow, selectedCol).getPieceSymbol() + "x" + endSquare; // Piece capture
                            } else {
                                moveStringg = board.getPiece(selectedRow, selectedCol).getPieceSymbol() + endSquare; // Piece move
                            }
                        }
                    }

                    moveList.append(moveStringg + ",  ");


                    // Check for castling
                    if (board.getPiece(selectedRow, selectedCol) instanceof King && Math.abs(selectedCol - col) == 2) {
                        int rookStartCol = (col > selectedCol) ? 7 : 0;
                        int rookEndCol = (col > selectedCol) ? 5 : 3;
                        board.setPiece(row, rookEndCol, board.getPiece(row, rookStartCol));
                        board.setPiece(row, rookStartCol, null);
                    }

                    // Check for en passant
                    if (board.getPiece(selectedRow, selectedCol) instanceof Pawn && Math.abs(selectedRow - row) == 1 && Math.abs(selectedCol - col) == 1 && board.getPiece(row, col) == null) {
                        int capturedPawnRow = selectedRow;
                        int capturedPawnCol = col;
                        board.setPiece(capturedPawnRow, capturedPawnCol, null);
                    }

                    if (board.getPiece(selectedRow, selectedCol) instanceof Pawn && Math.abs(selectedRow - row) == 2) {
                        ((Pawn) board.getPiece(selectedRow, selectedCol)).setHasMovedTwo(true);
                    } else if (board.getPiece(selectedRow, selectedCol) instanceof Pawn) {
                        // Add additional logic for Pawn here
                    }

                    board.setPiece(row, col, board.getPiece(selectedRow, selectedCol));
                    board.getPiece(row, col).setHasMoved(true);
                    board.setPiece(selectedRow, selectedCol, null);

                    currentPlayer = (currentPlayer.getColor() == PlayerColor.WHITE) ?
                            new Player(PlayerColor.BLACK) : new Player(PlayerColor.WHITE);
                }

                selectedSquare.setBorder(null);
                selectedSquare = null;
                updateChessboard(board);
            }
            if (board.isCheckmate(currentPlayer.getColor())) {
                if (currentPlayer.getColor() == PlayerColor.WHITE){
                    statusLabel.setText("Black Won"); // Update the label
                } else {
                    statusLabel.setText("White Won"); // Update the label
                }

            }
        }
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(10, 2));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JMenuItem startGameItem = new JMenuItem("Start Game");
        JMenuItem loadGameItem = new JMenuItem("Load Game");
        JMenuItem saveGameItem = new JMenuItem("Save Game");

        startGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Reset the board and current player
                board.initializePieces();
                currentPlayer = new Player(PlayerColor.WHITE);
                whiteTime = 1800; // 30 minutes in seconds
                blackTime = 1800; // 30 minutes in seconds
                updateTimerLabels();
                // Update the GUI
                updateChessboard(board);
            }
        });

        saveGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Save the game state to a file
                chessgame.saveGame("savegame.txt"); // Use the Chessgame instance to call saveGame
            }
        });

        loadGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a file chooser
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);

                // If a file was selected, load the game state from that file
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    chessgame.loadGame(selectedFile.getPath());
                    updateChessboard(board);
                }
            }
        });

        JMenuBar menuBar = new JMenuBar();

        menuBar.add(startGameItem);
        menuBar.add(loadGameItem);
        menuBar.add(saveGameItem);
        panel.add(menuBar);

        return panel;
    }

}