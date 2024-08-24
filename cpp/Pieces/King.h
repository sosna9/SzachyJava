#ifndef KING_H
#define KING_H

#include "Pieces.h"
#include "GameElems.h"
#include <vector>

class King : public Piece {
private:
    bool hasMoved;

public:
    King(PlayerColor color);

    bool hasMoved() override;
    void setHasMoved(bool hasMoved) override;
    bool isInCheck(int x, int y, Board& board);
    bool isValidMove(int startX, int startY, int endX, int endY, Board& board) override;
    bool threatensPosition(int x, int y, Board& board) override;
    std::vector<std::vector<int>> generatePossibleMoves(int startX, int startY, Board& board) override;
};

#endif // KING_H