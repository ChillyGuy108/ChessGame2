package org.example;



import org.example.Coordinate;
import org.example.Move;
import org.example.Pieces;
import org.example.BOARD;
import org.example.COLOUR;
import org.example.ID;

import java.util.ArrayList;

/**
 * Класс, представляющий пешку в шахматах
 */
public class Pawn extends Piece {

    private boolean hasMovedTwo = false;
    private boolean enPassantLeft = false;
    private boolean enPassantRight = false;
    private Coordinate previousCoordinate = new Coordinate();
    private Piece promotedPiece;

    /**
     * Конструктор пешки
     * @param colour цвет пешки
     * @param OGcoord исходная координата пешки
     */
    public Pawn(COLOUR colour, Coordinate OGcoord) {
        super(ID.PAWN, colour, OGcoord);
    }

    /**
     * Конструктор копирования пешки
     * @param original оригинальная пешка для копирования
     */
    public Pawn(Pawn original) {
        super(original);
    }

    /**
     * Устанавливает предыдущую координату пешки
     * @param previousCoordinate предыдущая координата
     */
    public void setPreviousCoordinate(Coordinate previousCoordinate) {
        this.previousCoordinate = previousCoordinate;
    }

    /**
     * Получает предыдущую координату пешки
     * @return предыдущая координата
     */
    public Coordinate getPreviousCoordinate() {
        return previousCoordinate;
    }

    /**
     * Отмечает, что пешка сделала ход на две клетки
     */
    public void setHasMovedTwo() {
        this.hasMovedTwo = true;
    }

    /**
     * Проверяет, делала ли пешка ход на две клетки
     * @return true если пешка делала ход на две клетки
     */
    public boolean getHasMovedTwo() {
        return hasMovedTwo;
    }

    /**
     * Проверяет возможность взятия на проходе слева
     * @return true если возможно взятие на проходе слева
     */
    public boolean getEnPassantLeft() {
        return enPassantLeft;
    }

    /**
     * Проверяет возможность взятия на проходе справа
     * @return true если возможно взятие на проходе справа
     */
    public boolean getEnPassantRight() {
        return enPassantRight;
    }

    /**
     * Проверяет, может ли пешка съесть фигуру по диагонали слева
     * @param pieces текущее состояние доски
     * @return true если может съесть слева
     */
    private boolean canEatLeftDig(Pieces pieces) {

        int factorV;
        int factorH;

        if (getColour().equals(COLOUR.B)) {
            factorV = -1;
            factorH = 1;
        } else {
            factorV = 1;
            factorH = -1;
        }

        char newFile = (char) (getFile() + factorH);
        int newRank = getRank() + factorV;
        Coordinate leftDig = new Coordinate(newFile, newRank);

        return Move.tileFull(pieces, leftDig) && Move.isNotTileColour(pieces, leftDig, getColour());
    }

    /**
     * Проверяет, может ли пешка съесть фигуру по диагонали справа
     * @param pieces текущее состояние доски
     * @return true если может съесть справа
     */
    private boolean canEatRightDig(Pieces pieces) {

        int factorV;
        int factorH;

        if (getColour().equals(COLOUR.B)) {
            factorV = -1;
            factorH = -1;
        } else {
            factorV = 1;
            factorH = 1;
        }

        char newFile = (char) (getFile() + factorH);
        int newRank = getRank() + factorV;
        Coordinate rightDig = new Coordinate(newFile, newRank);

        return Move.tileFull(pieces, rightDig) && Move.isNotTileColour(pieces, rightDig, getColour());
    }

    /**
     * Получает возможные ходы пешки вперед
     * @param pieces текущее состояние доски
     * @return список координат возможных ходов вперед
     */
    private ArrayList<Coordinate> pawnForward(Pieces pieces) {

        ArrayList<Coordinate> potentialForward = Move.frontFree(pieces, this, 1);
        ArrayList<Coordinate> actualForward = new ArrayList<>();

        if (potentialForward.size() > 0) {

            Coordinate front1 = potentialForward.get(0);

            if (Move.tileFull(pieces, front1))
                return actualForward;
            else {
                actualForward.add(front1);
            }
        }
        return actualForward;
    }

    /**
     * Создает фигуру для продвижения пешки (по умолчанию ферзь)
     * @param promotionSquare координата поля продвижения
     * @return фигура для замены пешки
     */
    public Piece promotionQuery(Coordinate promotionSquare) {
        Piece promotee = new Queen(getColour(), promotionSquare);
        return promotee;
    }

    /**
     * Проверяет, может ли черная пешка продвинуться
     * @param coordinate координата для проверки
     * @return true если черная пешка может продвинуться на этой координате
     */
    public boolean canPromoteBlack (Coordinate coordinate) {
        return this.getColour() == COLOUR.B && coordinate.getRank() == BOARD.FIRST_RANK.getRankVal();
    }

    /**
     * Проверяет, может ли белая пешка продвинуться
     * @param coordinate координата для проверки
     * @return true если белая пешка может продвинуться на этой координате
     */
    public boolean canPromoteWhite (Coordinate coordinate) {
        return this.getColour() == COLOUR.W && coordinate.getRank() == BOARD.LAST_RANK.getRankVal();
    }

    /**
     * Получает фигуру для продвижения пешки
     * @return фигура для замены пешки
     */
    public Piece getPromotedPiece() {
        return promotedPiece;
    }

    /**
     * Устанавливает фигуру для продвижения пешки
     * @param piece фигура для замены пешки
     */
    public void setPromotedPiece(Piece piece) {
        this.promotedPiece = piece;
    }

    /**
     * Запрашивает продвижение пешки в GUI режиме
     * @param promotionSquare координата поля продвижения
     */
    public void requestGUIPromotion(Coordinate promotionSquare) {
        // По умолчанию просто создаем ферзя
        promotedPiece = new Queen(getColour(), promotionSquare);
    }

    @Override
    public Pawn makeCopy() {
        return new Pawn(this);
    }

    @Override
    public ArrayList<Coordinate> getRawMoves(Pieces pieces) {

        ArrayList<Coordinate> pawnMoves = new ArrayList<>();

        if (canEatLeftDig(pieces))
            pawnMoves.addAll(Move.frontLDigFree(pieces, this, 1));

        pawnMoves.addAll(pawnForward(pieces));

        if (canEatRightDig(pieces))
            pawnMoves.addAll(Move.frontRDigFree(pieces, this, 1));

        return pawnMoves;
    }

}