#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include "Pieces.h"
#include "GameElems.h"
#include "ChessGUI.h"

class Chessgame {
private:
    Player currentPlayer;
    Board board;
    ChessGUI gui;

public:
    Chessgame() : currentPlayer(PlayerColor::WHITE), board(), gui(board, currentPlayer, *this) {}

    void saveGame(const std::string& filename) {
        std::ofstream writer(filename);
        if (writer.is_open()) {
            // Save the current player
            writer << static_cast<int>(currentPlayer.getColor()) << std::endl;

            // Save the state of the board
            for (int i = 0; i < 8; ++i) {
                for (int j = 0; j < 8; ++j) {
                    Piece* piece = board.getPiece(i, j);
                    if (piece != nullptr) {
                        // Save the piece's color, type, and position
                        writer << static_cast<int>(piece->getColor()) << " " << piece->getPieceType() << " " << i << " " << j << std::endl;
                    }
                }
            }
            writer.close();
        } else {
            std::cerr << "File not found: " << filename << std::endl;
        }
    }

    void loadGame(const std::string& filename) {
        std::ifstream reader(filename);
        if (reader.is_open()) {
            // Load the current player
            int color;
            reader >> color;
            currentPlayer = Player(static_cast<PlayerColor>(color));

            // Clear the board
            board = Board();

            // Load the state of the board
            int pieceColor, x, y;
            std::string pieceType;
            while (reader >> pieceColor >> pieceType >> x >> y) {
                Piece* piece = nullptr;
                if (pieceType == "Pawn") {
                    piece = new Pawn(static_cast<PlayerColor>(pieceColor));
                } else if (pieceType == "Rook") {
                    piece = new Rook(static_cast<PlayerColor>(pieceColor));
                } else if (pieceType == "Knight") {
                    piece = new Knight(static_cast<PlayerColor>(pieceColor));
                } else if (pieceType == "Bishop") {
                    piece = new Bishop(static_cast<PlayerColor>(pieceColor));
                } else if (pieceType == "Queen") {
                    piece = new Queen(static_cast<PlayerColor>(pieceColor));
                } else if (pieceType == "King") {
                    piece = new King(static_cast<PlayerColor>(pieceColor));
                } else {
                    throw std::invalid_argument("Invalid piece type: " + pieceType);
                }
                board.setPiece(x, y, piece);
            }
            reader.close();
        } else {
            std::cerr << "File not found: " << filename << std::endl;
        }
    }

    static void main() {
        Chessgame chessGame;
        chessGame.gui.setVisible(true);
    }
};

int main() {
    Chessgame::main();
    return 0;
}