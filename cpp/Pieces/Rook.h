#ifndef ROOK_H
#define ROOK_H

#include "Pieces.h"
#include "GameElems.h"
#include <vector>

class Rook : public Piece {
public:
    Rook(PlayerColor color);

    bool isValidMove(int startX, int startY, int endX, int endY, Board& board) override;
    bool threatensPosition(int x, int y, Board& board) override;
    std::vector<std::vector<int>> generatePossibleMoves(int startX, int startY, Board& board) override;
};

#endif // ROOK_H