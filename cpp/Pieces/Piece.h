#ifndef PIECE_H
#define PIECE_H

#include "GameElems.h"
#include <vector>

enum class PlayerColor { WHITE, BLACK };

class Piece {
protected:
    PlayerColor color;
    char symbol;
    bool hasMoved;
    bool hasMovedTwo;

public:
    Piece(PlayerColor color, char symbol);

    PlayerColor getColor() const;
    char getPieceSymbol() const;
    bool getHasMoved() const;
    bool getHasMovedTwo() const;
    bool wouldThisMovePutKingInCheck(int startX, int startY, int endX, int endY, Board& board);
    void setHasMoved(bool hasMoved);
    virtual bool isValidMove(int startX, int startY, int endX, int endY, Board& board) = 0;
    virtual bool threatensPosition(int x, int y, Board& board) = 0;
    virtual std::vector<std::vector<int>> generatePossibleMoves(int startX, int startY, Board& board) = 0;
};

#endif // PIECE_H