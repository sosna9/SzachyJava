#include "Pawn.h"
#include "Board.h"
#include <vector>
#include <cmath>

Pawn::Pawn(PlayerColor color) : Piece(color, 'P'), hasMovedTwo(false) {}

bool Pawn::getHasMovedTwo() {
    return hasMovedTwo;
}

void Pawn::setHasMovedTwo(bool hasMovedTwo) {
    this->hasMovedTwo = hasMovedTwo;
}

bool Pawn::isValidMove(int startX, int startY, int endX, int endY, Board& board) {
    int direction = (getColor() == PlayerColor::WHITE) ? 1 : -1; // Direction of movement depends on pawn's color

    // Check if the move is forward
    if (startY == endY && board.getPiece(endX, endY) == nullptr) {
        // Normal move
        if (startX + direction == endX) {
            return true;
        }
        // First move, pawn can move two squares forward
        return !this->hasMoved() && startX + 2 * direction == endX && board.getPiece(startX + direction, endY) == nullptr;
    }
    // Check if the move is a capture
    else if (std::abs(startY - endY) == 1 && startX + direction == endX) {
        // Check if there's an opponent's piece to capture
        if (board.getPiece(endX, endY) != nullptr && board.getPiece(endX, endY)->getColor() != this->getColor()) {
            return true;
        }
        // Check for en passant
        else if ((getColor() == PlayerColor::WHITE && startX == 4) || (getColor() == PlayerColor::BLACK && startX == 3)) {
            Piece* adjacentPiece = board.getPiece(startX, startY + (endY - startY));
            return adjacentPiece != nullptr && dynamic_cast<Pawn*>(adjacentPiece) && adjacentPiece->getColor() != getColor() && adjacentPiece->getHasMovedTwo();
        }
    }
    return false;
}

bool Pawn::threatensPosition(int x, int y, Board& board) {
    int direction = (getColor() == PlayerColor::WHITE) ? -1 : 1;
    int nextX = x + direction;
    return (nextX >= 0 && nextX < 8) && ((y - 1 >= 0 && board.getPiece(nextX, y - 1) == this) || (y + 1 < 8 && board.getPiece(nextX, y + 1) == this));
}

std::vector<std::vector<int>> Pawn::generatePossibleMoves(int startX, int startY, Board& board) {
    std::vector<std::vector<int>> possibleMoves;
    int direction = (getColor() == PlayerColor::WHITE) ? 1 : -1; // Direction of movement depends on pawn's color

    // Normal move
    if (startX + direction >= 0 && startX + direction < 8 && board.getPiece(startX + direction, startY) == nullptr) {
        possibleMoves.push_back({startX + direction, startY});
    }

    // First move, pawn can move two squares forward
    if (!this->hasMoved() && startX + 2 * direction >= 0 && startX + 2 * direction < 8 && board.getPiece(startX + direction, startY) == nullptr && board.getPiece(startX + 2 * direction, startY) == nullptr) {
        possibleMoves.push_back({startX + 2 * direction, startY});
    }

    // Capture moves
    if (startY - 1 >= 0 && board.getPiece(startX + direction, startY - 1) != nullptr && board.getPiece(startX + direction, startY - 1)->getColor() != this->getColor()) {
        possibleMoves.push_back({startX + direction, startY - 1});
    }
    if (startY + 1 < 8 && board.getPiece(startX + direction, startY + 1) != nullptr && board.getPiece(startX + direction, startY + 1)->getColor() != this->getColor()) {
        possibleMoves.push_back({startX + direction, startY + 1});
    }

    // En passant
    if ((getColor() == PlayerColor::WHITE && startX == 3) || (getColor() == PlayerColor::BLACK && startX == 4)) {
        // Check the squares to the left and right of the pawn
        for (int dy = -1; dy <= 1; dy += 2) {
            if (startY + dy >= 0 && startY + dy < 8) {
                Piece* adjacentPiece = board.getPiece(startX, startY + dy);
                // Check if the adjacent piece is a pawn of the opposite color that just moved two squares
                if (adjacentPiece != nullptr && dynamic_cast<Pawn*>(adjacentPiece) && adjacentPiece->getColor() != getColor() && adjacentPiece->getHasMovedTwo()) {
                    possibleMoves.push_back({startX + direction, startY + dy});
                }
            }
        }
    }

    return possibleMoves;
}