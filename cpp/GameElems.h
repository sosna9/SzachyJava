#ifndef GAMEELEMS_H
#define GAMEELEMS_H

#include "Pieces.h"
#include <vector>

class Board {
private:
    Piece* board[8][8];

public:
    Board();
    Piece* getPiece(int x, int y);
    void setPiece(int x, int y, Piece* piece);
    bool isKingInCheck(PlayerColor color);
};

class Player {
private:
    PlayerColor color;

public:
    Player(PlayerColor color);
    PlayerColor getColor();
};

#endif // GAMEELEMS_H