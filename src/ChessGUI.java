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

    private void newBetterUpdateChessboard(GuiSquare[][] guiBoard) {
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
                if (guiBoard[row][col].getPiece() != null) {
                    String imagePath = "obrazki/" + guiBoard[row][col].getPiece().getColor().toString().toLowerCase() +
                            guiBoard[row][col].getPiece().getClass().getSimpleName() + ".png";
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
            statusLabel.setText(currentPlayer.getColor() + " Won");
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
                Piece piece = board.getPiece(row, col);
                if (piece != null) {
                    String imagePath = "obrazki/" + piece.getColor().toString().toLowerCase() +
                            piece.getClass().getSimpleName() + ".png";
                    ImageIcon icon = new ImageIcon(imagePath);
                    Image image = icon.getImage();
                    Image scaledImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            if (logic.guiBoard[i][j].highlighted) {
                                squares[i][j].setBackground(Color.YELLOW);
                            }
                            else {
                                squares[i][j].setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.GREEN.darker());
                            }
                        }
                    }
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

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedSquare = (JButton) e.getSource();
            logic.handleClick(row, col);
            //newBetterUpdateChessboard(logic.guiBoard);
            updateChessboard(board);

            if (board.isCheckmate(currentPlayer.getColor())) {
                if (currentPlayer.getColor() == PlayerColor.WHITE){
                    statusLabel.setText("Black Won");
                } else {
                    statusLabel.setText("White Won");
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