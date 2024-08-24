#ifndef KNIGHT_H
#define KNIGHT_H

#include "Pieces.h"
#include "GameElems.h"
#include <vector>

class Knight : public Piece {
public:
    Knight(PlayerColor color);

    bool isValidMove(int startX, int startY, int endX, int endY, Board& board) override;
    bool threatensPosition(int x, int y, Board& board) override;
    std::vector<std::vector<int>> generatePossibleMoves(int startX, int startY, Board& board) override;
};

#endif // KNIGHT_H