#ifndef BOARD_H
#define BOARD_H

#include "Piece.h"
#include <vector>

class Board {
private:
    Piece* pieces[8][8];

public:
    Board();
    void initializePieces();
    bool isKingInCheck(PlayerColor color);
    void flipBoard();
    bool isCheckmate(PlayerColor color);
    Piece* getPiece(int row, int col);
    void setPiece(int row, int col, Piece* piece);
};

#endif // BOARD_H