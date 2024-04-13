import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import Pieces.*;
import GameElems.*;
import java.util.List;
import java.util.ArrayList;

public class Chessgame extends JFrame {
    private final JPanel chessboardPanel;
    private JPanel menuPanel;
    private JPanel moveListPanel;
    private JList<String> moveList;
    private JButton[][] squares;
    private JButton selectedSquare;
    private Player currentPlayer;
    private Board board;

    public Chessgame() {
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);

        chessboardPanel = new JPanel(new GridLayout(9, 9));
        squares = new JButton[8][8];

        menuPanel = createMenuPanel();
        add(menuPanel, BorderLayout.EAST);

        moveListPanel = new JPanel(new GridLayout(1, 1));
        moveListPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        moveListPanel.setPreferredSize(new Dimension(200, getHeight()));

        DefaultListModel<String> moveListModel = new DefaultListModel<>();
        moveList = new JList<>(moveListModel);
        moveListPanel.add(new JScrollPane(moveList));

        currentPlayer = new Player(PlayerColor.WHITE);
        board = new Board();

        JLabel space = new JLabel(String.valueOf((char)(' ')));
        space.setHorizontalAlignment(SwingConstants.CENTER);
        chessboardPanel.add(space);
        for (int i = 0; i < 8; i++) {
            JLabel label = new JLabel(String.valueOf((char)('A' + i)));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            chessboardPanel.add(label);
        }

        for (int row = 0; row < 8; row++) {
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

        add(chessboardPanel);
        add(moveListPanel,  BorderLayout.WEST);
        updateChessboard(board);
    }

    private void updateChessboard(Board board) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].setIcon(null);
            }
        }

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board.getPiece(row, col) != null) {
                    String imagePath = "obraski/" + board.getPiece(row, col).getColor().toString().toLowerCase() +
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
        private final DefaultListModel<String> moveListModel;

        public SquareClickListener(int row, int col, Board board, DefaultListModel<String> moveListModel) {
            this.row = row;
            this.col = col;
            this.board = board;
            this.moveListModel = moveListModel;
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
            if (selectedSquare == null) {
                if (board.getPiece(row, col) != null && board.getPiece(row, col).getColor() == currentPlayer.getColor()) {
                    selectedSquare = clickedSquare;
                    selectedSquare.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
                    List<int[]> possibleMoves = board.getPiece(row, col).generatePossibleMoves(row, col, board);
                    for (int[] move : possibleMoves) {
                        // Temporarily move the piece
                        Piece temp = board.getPiece(move[0], move[1]);
                        board.setPiece(move[0], move[1], board.getPiece(row, col));
                        board.setPiece(row, col, null);
                        // Check if the move would put the king in check
                        if (!board.isKingInCheck(currentPlayer.getColor())) {
                            squares[move[0]][move[1]].setBackground(Color.YELLOW);
                        }
                        // Move the piece back
                        board.setPiece(row, col, board.getPiece(move[0], move[1]));
                        board.setPiece(move[0], move[1], temp);
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

                    if (board.getPiece(selectedRow, selectedCol) instanceof King && Math.abs(selectedCol - col) == 2) {
                        moveStringg = (col > selectedCol) ? "O-O" : "O-O-O";
                    } else {
                        char pieceSymbol = board.getPiece(selectedRow, selectedCol).getPieceSymbol();
                        if (board.getPiece(row, col) != null) {
                            moveStringg = (pieceSymbol == 'P') ? startSquare + "x" + endSquare : pieceSymbol + "x" + endSquare;
                        } else {
                            moveStringg = (pieceSymbol == 'P') ? endSquare : pieceSymbol + endSquare;
                        }
                    }

                    moveListModel.addElement(moveStringg);

                    if (board.getPiece(selectedRow, selectedCol) instanceof Pawn && Math.abs(selectedRow - row) == 2) {
                        ((Pawn) board.getPiece(selectedRow, selectedCol)).setHasMovedTwo(true);
                    } else if (board.getPiece(selectedRow, selectedCol) instanceof Pawn) {
                        ((Pawn) board.getPiece(selectedRow, selectedCol)).setHasMovedTwo(false);
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
        }
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(10, 2));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

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
            Chessgame chessGame = new Chessgame();
            chessGame.setVisible(true);
        });
    }
}