#ifndef PIECES_H
#define PIECES_H

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

#endif // PIECES_H