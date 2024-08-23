#ifndef CHESSGUI_H
#define CHESSGUI_H

#include "GameElems.h"
#include "Pieces.h"

class Chessgame;

class ChessGUI {
private:
    Board& board;
    Player& currentPlayer;
    Chessgame& game;

public:
    ChessGUI(Board& board, Player& currentPlayer, Chessgame& game);
    void setVisible(bool visible);
};

#endif // CHESSGUI_H