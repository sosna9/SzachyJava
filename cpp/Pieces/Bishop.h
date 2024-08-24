#ifndef BISHOP_H
#define BISHOP_H

#include "Pieces.h"
#include "GameElems.h"
#include <vector>

class Bishop : public Piece {
public:
    Bishop(PlayerColor color);

    bool isValidMove(int startX, int startY, int endX, int endY, Board& board) override;
    bool threatensPosition(int x, int y, Board& board) override;
    std::vector<std::vector<int>> generatePossibleMoves(int startX, int startY, Board& board) override;
};

#endif // BISHOP_H