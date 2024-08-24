#include "Bishop.h"
#include "Board.h"

Bishop::Bishop(PlayerColor color) : Piece(color, 'B') {}

bool Bishop::isValidMove(int startX, int startY, int endX, int endY, Board& board) const {
    if (abs(endX - startX) == abs(endY - startY)) {
        int xDirection = (endX > startX) ? 1 : -1;
        int yDirection = (endY > startY) ? 1 : -1;
        int x = startX + xDirection;
        int y = startY + yDirection;
        while (x != endX && y != endY) {
            if (board.getPiece(x, y) != nullptr) {
                return false;
            }
            x += xDirection;
            y += yDirection;
        }
        return board.getPiece(endX, endY) == nullptr || board.getPiece(endX, endY)->getColor() != this->getColor();
    }
    return false;
}

bool Bishop::threatensPosition(int x, int y, const Board& board) const {
    for (int i = -7; i <= 7; ++i) {
        if (isValidMove(x, y, x + i, y + i, board) || isValidMove(x, y, x + i, y - i, board)) {
            return true;
        }
    }
    return false;
}

std::vector<std::array<int, 2>> Bishop::generatePossibleMoves(int startX, int startY, const Board& board) const {
    std::vector<std::array<int, 2>> possibleMoves;
    int dx[4] = {-1, -1, 1, 1};
    int dy[4] = {-1, 1, -1, 1};
    for (int direction = 0; direction < 4; ++direction) {
        for (int x = startX + dx[direction], y = startY + dy[direction]; x >= 0 && x < 8 && y >= 0 && y < 8; x += dx[direction], y += dy[direction]) {
            if (board.getPiece(x, y) == nullptr || board.getPiece(x, y)->getColor() != this->getColor()) {
                possibleMoves.push_back({x, y});
            }
            if (board.getPiece(x, y) != nullptr) {
                break;
            }
        }
    }
    return possibleMoves;
}