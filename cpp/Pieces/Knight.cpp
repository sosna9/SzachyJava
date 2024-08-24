#include "Knight.h"
#include "Board.h"

Knight::Knight(PlayerColor color) : Piece(color, 'N') {}

bool Knight::isValidMove(int startX, int startY, int endX, int endY, const Board& board) const {
    int dx = abs(startX - endX);
    int dy = abs(startY - endY);
    return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
}

bool Knight::threatensPosition(int x, int y, const Board& board) const {
    int moves[8][2] = {{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, {1, -2}, {1, 2}, {2, -1}, {2, 1}};
    for (auto& move : moves) {
        int newX = x + move[0];
        int newY = y + move[1];
        if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8 &&
            (board.getPiece(newX, newY) == nullptr || board.getPiece(newX, newY)->getColor() != this->getColor())) {
            return true;
        }
    }
    return false;
}

std::vector<std::array<int, 2>> Knight::generatePossibleMoves(int startX, int startY, const Board& board) const {
    std::vector<std::array<int, 2>> possibleMoves;
    int moves[8][2] = {{-2, -2}, {-2, 2}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}, {2, -2}, {2, 2}};
    for (auto& move : moves) {
        int x = startX + move[0];
        int y = startY + move[1];
        if (x >= 0 && x < 8 && y >= 0 && y < 8 &&
            (board.getPiece(x, y) == nullptr || board.getPiece(x, y)->getColor() != this->getColor())) {
            possibleMoves.push_back({x, y});
        }
    }
    return possibleMoves;
}