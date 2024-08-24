#include "Queen.h"
#include "Rook.h"
#include "Bishop.h"
#include "Board.h"

Queen::Queen(PlayerColor color) : Piece(color, 'Q') {}

bool Queen::isValidMove(int startX, int startY, int endX, int endY, const Board& board) const {
    Rook rook(getColor());
    Bishop bishop(getColor());
    return rook.isValidMove(startX, startY, endX, endY, board) || bishop.isValidMove(startX, startY, endX, endY, board);
}

bool Queen::threatensPosition(int x, int y, const Board& board) const {
    for (int i = 0; i < 8; ++i) {
        for (int j = 0; j < 8; ++j) {
            if (isValidMove(i, j, x, y, board)) {
                return true;
            }
        }
    }
    return false;
}

std::vector<std::array<int, 2>> Queen::generatePossibleMoves(int startX, int startY, const Board& board) const {
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