#include "Piece.h"
#include "GameElems.h"
#include "Rook.h" // Include the Rook header file
#include <vector>

class King : public Piece {
private:
    bool hasMoved;

public:
    King(PlayerColor color) : Piece(color, 'K'), hasMoved(false) {}

    bool hasMoved() override {
        return hasMoved;
    }

    void setHasMoved(bool hasMoved) override {
        this->hasMoved = hasMoved;
    }

    bool isInCheck(int x, int y, Board& board) {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                Piece* piece = board.getPiece(i, j);
                if (piece != nullptr && piece->getColor() != this->getColor() && piece->isValidMove(i, j, x, y, board)) {
                    return true;
                }
            }
        }
        return false;
    }

    bool isValidMove(int startX, int startY, int endX, int endY, Board& board) override {
        int dx = std::abs(startX - endX);
        int dy = std::abs(startY - endY);

        bool isAdjacentMove = dx <= 1 && dy <= 1 && (board.getPiece(endX, endY) == nullptr || board.getPiece(endX, endY)->getColor() != this->getColor());
        bool isUnderAttack = wouldThisMovePutKingInCheck(startX, startY, endX, endY, board);

        bool isCastlingMove = !hasMoved && dx == 0 && dy == 2 && board.getPiece(startX, startY + dy / 2) == nullptr && board.getPiece(startX, startY + dy) == nullptr;
        if (isCastlingMove) {
            Piece* piece = board.getPiece(startX, startY + dy / 2 * 3);
            isCastlingMove =  && !rook->hasMoved();
        }

        return (isAdjacentMove || isCastlingMove) && !isUnderAttack;
    }

    bool threatensPosition(int x, int y, Board& board) override {
        for (int dx = -1; dx <= 1; ++dx) {
            for (int dy = -1; dy <= 1; ++dy) {
                int newX = x + dx;
                int newY = y + dy;
                if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8 && (board.getPiece(newX, newY) == nullptr || board.getPiece(newX, newY)->getColor() != this->getColor())) {
                    return true;
                }
            }
        }
        return false;
    }

    std::vector<std::vector<int>> generatePossibleMoves(int startX, int startY, Board& board) override {
        std::vector<std::vector<int>> possibleMoves;
        int dx[] = {-1, -1, -1, 0, 0, 1, 1, 1};
        int dy[] = {-1, 0, 1, -1, 1, -1, 0, 1};
        for (int direction = 0; direction < 8; ++direction) {
            int x = startX + dx[direction];
            int y = startY + dy[direction];
            if (x >= 0 && x < 8 && y >= 0 && y < 8 && (board.getPiece(x, y) == nullptr || board.getPiece(x, y)->getColor() != this->getColor())) {
                possibleMoves.push_back({x, y});
            }
        }
        return possibleMoves;
    }
};