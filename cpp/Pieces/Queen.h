#ifndef QUEEN_H
#define QUEEN_H

#include "Piece.h"
#include "GameElems.h"
#include <vector>

class Queen : public Piece {
public:
    Queen(PlayerColor color);

    bool isValidMove(int startX, int startY, int endX, int endY, Board& board) override;
    bool threatensPosition(int x, int y, Board& board) override;
    std::vector<std::vector<int>> generatePossibleMoves(int startX, int startY, Board& board) override;
};

#endif // QUEEN_H