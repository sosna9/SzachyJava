#ifndef PAWN_H
#define PAWN_H

#include "Piece.h"
#include "GameElems.h"
#include <vector>

class Pawn : public Piece {

private:
    bool hasMovedTwo;

public:
    Pawn(PlayerColor color);
    bool getHasMovedTwo() override;
    void setHasMovedTwo(bool hasMovedTwo);
    bool isValidMove(int startX, int startY, int endX, int endY, Board& board) override;
    bool threatensPosition(int x, int y, Board& board) override;
    std::vector<std::vector<int>> generatePossibleMoves(int startX, int startY, Board& board) override;
};

#endif // PAWN_H