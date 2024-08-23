#include "Pieces.h"
#include "GameElems.h"
#include <vector>

class Pawn : public Piece {
private:
    bool hasMovedTwo;

public:
    Pawn(PlayerColor color) : Piece(color, 'P'), hasMovedTwo(false) {}

    bool getHasMovedTwo() override {
        return hasMovedTwo;
    }

    void setHasMovedTwo(bool hasMovedTwo) {
        this->hasMovedTwo = hasMovedTwo;
    }

    bool isValidMove(int startX, int startY, int endX, int endY, Board& board) override {
        int direction = (getColor() == PlayerColor::WHITE) ? 1 : -1;

        if (startY == endY && board.getPiece(endX, endY) == nullptr) {
            if (startX + direction == endX) {
                return true;
            }
            return !this->hasMoved() && startX + 2 * direction == endX && board.getPiece(startX + direction, endY) == nullptr;
        } else if (std::abs(startY - endY) == 1 && startX + direction == endX) {
            if (board.getPiece(endX, endY) != nullptr && board.getPiece(endX, endY)->getColor() != this->getColor()) {
                return true;
            } else if ((getColor() == PlayerColor::WHITE && startX == 4) || (getColor() == PlayerColor::BLACK && startX == 3)) {
                Piece* adjacentPiece = board.getPiece(startX, startY + endY - startY);
                return adjacentPiece != nullptr && dynamic_cast<Pawn*>(adjacentPiece) && adjacentPiece->getColor() != getColor() && adjacentPiece->getHasMovedTwo();
            }
        }
        return false;
    }

    bool threatensPosition(int x, int y, Board& board) override {
        int direction = (getColor() == PlayerColor::WHITE) ? -1 : 1;
        int nextX = x + direction;
        return (nextX >= 0 && nextX < 8) && ((y - 1 >= 0 && board.getPiece(nextX, y - 1) == this) || (y + 1 < 8 && board.getPiece(nextX, y + 1) == this));
    }

    std::vector<std::vector<int>> generatePossibleMoves(int startX, int startY, Board& board) override {
        std::vector<std::vector<int>> possibleMoves;
        int direction = (getColor() == PlayerColor::WHITE) ? 1 : -1;

        if (startX + direction >= 0 && startX + direction < 8 && board.getPiece(startX + direction, startY) == nullptr) {
            possibleMoves.push_back({startX + direction, startY});
        }

        if (!this->hasMoved() && startX + 2 * direction >= 0 && startX + 2 * direction < 8 && board.getPiece(startX + direction, startY) == nullptr && board.getPiece(startX + 2 * direction, startY) == nullptr) {
            possibleMoves.push_back({startX + 2 * direction, startY});
        }

        if (startY - 1 >= 0 && board.getPiece(startX + direction, startY - 1) != nullptr && board.getPiece(startX + direction, startY - 1)->getColor() != this->getColor()) {
            possibleMoves.push_back({startX + direction, startY - 1});
        }
        if (startY + 1 < 8 && board.getPiece(startX + direction, startY + 1) != nullptr && board.getPiece(startX + direction, startY + 1)->getColor() != this->getColor()) {
            possibleMoves.push_back({startX + direction, startY + 1});
        }

        if ((getColor() == PlayerColor::WHITE && startX == 3) || (getColor() == PlayerColor::BLACK && startX == 4)) {
            for (int dy = -1; dy <= 1; dy += 2) {
                if (startY + dy >= 0 && startY + dy < 8) {
                    Piece* adjacentPiece = board.getPiece(startX, startY + dy);
                    if (adjacentPiece != nullptr && dynamic_cast<Pawn*>(adjacentPiece) && adjacentPiece->getColor() != getColor() && adjacentPiece->getHasMovedTwo()) {
                        possibleMoves.push_back({startX + direction, startY + dy});
                    }
                }
            }
        }

        return possibleMoves;
    }
};