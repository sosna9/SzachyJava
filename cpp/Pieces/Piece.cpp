#include "Piece.h"
#include "King.h"

Piece::Piece(PlayerColor color, char symbol) : color(color), symbol(symbol), hasMoved(false) {}

bool Piece::hasMoved() const {
    return hasMoved;
}

bool Piece::getHasMovedTwo() const {
    return false;
}

void Piece::setHasMoved(bool hasMoved) {
    this->hasMoved = hasMoved;
}

char Piece::getPieceSymbol() const {
    return symbol;
}

PlayerColor Piece::getColor() const {
    return color;
}

bool Piece::wouldThisMovePutKingInCheck(int startX, int startY, int endX, int endY, Board& board) const {
    // Create a copy of the board
    Board copiedBoard;
    for (int x = 0; x < 8; ++x) {
        for (int y = 0; y < 8; ++y) {
            copiedBoard.setPiece(x, y, board.getPiece(x, y));
        }
    }

    // Make the move on the copied board
    copiedBoard.setPiece(endX, endY, copiedBoard.getPiece(startX, startY));
    copiedBoard.setPiece(startX, startY, nullptr);

    // Find the position of the king on the copied board
    int kingX = -1, kingY = -1;
    for (int x = 0; x < 8; ++x) {
        for (int y = 0; y < 8; ++y) {
            Piece* piece = copiedBoard.getPiece(x, y);
            if (piece != nullptr && piece->getColor() == this->getColor() && dynamic_cast<King*>(piece)) {
                kingX = x;
                kingY = y;
                break;
            }
        }
        if (kingX != -1) {
            break;
        }
    }

    // If the king's position is not found, return false
    if (kingX == -1 || kingY == -1) {
        return false;
    }

    // Check if the king is in check on the copied board
    return copiedBoard.isKingInCheck(color);
}