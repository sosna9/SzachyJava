#include "Rook.h"
#include "Board.h"
#include <iostream>

Rook::Rook(PlayerColor color) : Piece(color, 'R'), hasMoved(false) {}

bool Rook::hasMoved() const {
    return hasMoved;
}

void Rook::setHasMoved(bool hasMoved) {
    this->hasMoved = hasMoved;
}

bool Rook::isValidMove(int startX, int startY, int endX, int endY, const Board& board) const {
    if (startX == endX || startY == endY) {
        if (startX == endX) { // Vertical move
            int direction = (endY > startY) ? 1 : -1;
            for (int y = startY + direction; y != endY; y += direction) {
                if (board.getPiece(startX, y) != nullptr) {
                    return false;
                }
            }
        } else { // Horizontal move
            int direction = (endX > startX) ? 1 : -1;
            for (int x = startX + direction; x != endX; x += direction) {
                if (board.getPiece(x, startY) != nullptr) {
                    return false;
                }
            }
        }
        return board.getPiece(endX, endY) == nullptr || board.getPiece(endX, endY)->getColor() != this->getColor();
    }
    return false;
}

bool Rook::threatensPosition(int x, int y, const Board& board) const {
    for (int i = 0; i < 8; ++i) {
        if (i != y && isValidMove(x, y, x, i, board)) {
            std::cout << "Rook threatens position " << x << " " << y << " " << i << " " << y << std::endl;
            return true;
        }
        if (i != x && isValidMove(x, y, i, y, board)) {
            std::cout << "Rook threatens position " << x << " " << y << " " << i << " " << y << std::endl;
            return true;
        }
    }
    return false;
}

std::vector<std::array<int, 2>> Rook::generatePossibleMoves(int startX, int startY, const Board& board) const {
    std::vector<std::array<int, 2>> possibleMoves;
    for (int x = 0; x < 8; ++x) {
        for (int y = 0; y < 8; ++y) {
            if (isValidMove(startX, startY, x, y, board)) {
                possibleMoves.push_back({x, y});
            }
        }
    }
    return possibleMoves;
}