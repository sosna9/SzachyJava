#include "Board.h"
#include "Piece.h"
#include "King.h"
#include "Queen.h"
#include "Bishop.h"
#include "Knight.h"
#include "Rook.h"
#include "Pawn.h"
#include <vector>

Board::Board() {
    initializePieces();
}

void Board::initializePieces() {
    // Reset the board to nulls
    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            pieces[i][j] = nullptr;
        }
    }

    // Initialize pawns
    for (int i = 0; i < 8; i++) {
        pieces[1][i] = new Pawn(PlayerColor::WHITE);
        pieces[6][i] = new Pawn(PlayerColor::BLACK);
    }

    // Initialize pieces for white player
    pieces[0][0] = new Rook(PlayerColor::WHITE);
    pieces[0][1] = new Knight(PlayerColor::WHITE);
    pieces[0][2] = new Bishop(PlayerColor::WHITE);
    pieces[0][3] = new Queen(PlayerColor::WHITE);
    pieces[0][4] = new King(PlayerColor::WHITE);
    pieces[0][5] = new Bishop(PlayerColor::WHITE);
    pieces[0][6] = new Knight(PlayerColor::WHITE);
    pieces[0][7] = new Rook(PlayerColor::WHITE);

    // Initialize pieces for black player
    pieces[7][0] = new Rook(PlayerColor::BLACK);
    pieces[7][1] = new Knight(PlayerColor::BLACK);
    pieces[7][2] = new Bishop(PlayerColor::BLACK);
    pieces[7][3] = new Queen(PlayerColor::BLACK);
    pieces[7][4] = new King(PlayerColor::BLACK);
    pieces[7][5] = new Bishop(PlayerColor::BLACK);
    pieces[7][6] = new Knight(PlayerColor::BLACK);
    pieces[7][7] = new Rook(PlayerColor::BLACK);
}

bool Board::isKingInCheck(PlayerColor color) {
    int kingX = -1, kingY = -1;
    // Find the king of the given color
    for (int x = 0; x < 8; x++) {
        for (int y = 0; y < 8; y++) {
            Piece* piece = pieces[x][y];
            if (piece != nullptr && piece->getColor() == color && dynamic_cast<King*>(piece)) {
                kingX = x;
                kingY = y;
                break;
            }
        }
        if (kingX != -1) {
            break;
        }
    }
    // Check if the king is in check
    for (int x = 0; x < 8; x++) {
        for (int y = 0; y < 8; y++) {
            Piece* piece = pieces[x][y];
            if (piece != nullptr && piece->getColor() != color && piece->isValidMove(x, y, kingX, kingY, *this)) {
                return true;
            }
        }
    }
    return false;
}

void Board::flipBoard() {
    Piece* newPieces[8][8];
    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            newPieces[i][j] = pieces[7 - i][7 - j];
        }
    }
    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            pieces[i][j] = newPieces[i][j];
        }
    }
}

bool Board::isCheckmate(PlayerColor color) {
    if (!isKingInCheck(color)) {
        return false;
    }

    for (int x = 0; x < 8; x++) {
        for (int y = 0; y < 8; y++) {
            Piece* piece = pieces[x][y];
            if (piece != nullptr && piece->getColor() == color) {
                std::vector<std::vector<int>> possibleMoves = piece->generatePossibleMoves(x, y, *this);
                for (const auto& move : possibleMoves) {
                    // Create a copy of the board
                    Board copiedBoard;
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            copiedBoard.setPiece(i, j, this->getPiece(i, j));
                        }
                    }

                    // Make the move on the copied board
                    copiedBoard.setPiece(move[0], move[1], copiedBoard.getPiece(x, y));
                    copiedBoard.setPiece(x, y, nullptr);

                    // Check if the king is still in check
                    if (!copiedBoard.isKingInCheck(color)) {
                        return false;
                    }
                }
            }
        }
    }
    return true;
}

Piece* Board::getPiece(int row, int col) {
    return pieces[row][col];
}

void Board::setPiece(int row, int col, Piece* piece) {
    pieces[row][col] = piece;
}