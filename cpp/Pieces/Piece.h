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

public:
    Piece(PlayerColor color, char symbol);
    virtual ~Piece() = default;

    virtual bool hasMoved(); 
    virtual bool getHasMovedTwo();
    virtual void setHasMoved(bool hasMoved);
    char getPieceSymbol();
    PlayerColor getColor();

    virtual std::vector<std::vector<int>> generatePossibleMoves(int startX, int startY, Board& board) = 0;
    virtual bool isValidMove(int startX, int startY, int endX, int endY, Board& board) = 0;
    virtual bool threatensPosition(int x, int y, Board& board) = 0;
    bool wouldThisMovePutKingInCheck(int startX, int startY, int endX, int endY, Board& board);
};

class Pawn : public Piece {
    // Pawn-specific members and methods
};

class King : public Piece {
    // King-specific members and methods
};

#endif // PIECES_H