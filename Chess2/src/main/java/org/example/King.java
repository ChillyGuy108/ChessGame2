package org.example;

import org.example.Coordinate;
import org.example.Move;
import org.example.Pieces;
import org.example.BOARD;
import org.example.COLOUR;
import org.example.ID;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Класс, представляющий короля в шахматах
 */
public class King extends Piece {

    private Coordinate castleCoordKingK;
    private Coordinate castleCoordKingQ;
    private Coordinate transitionCoordKingK;
    private Coordinate transitionCoordKingQ;
    private Rook rookKing;
    private Rook rookQueen;

    /**
     * Конструктор короля
     * @param colour цвет короля
     * @param OGcoord исходная координата короля
     */
    public King(COLOUR colour, Coordinate OGcoord) {
        super(ID.KING, colour, OGcoord);
    }

    /**
     * Конструктор копирования короля
     * @param original оригинальный король для копирования
     */
    public King(King original) {
        super(original);
    }

    /**
     * Получает координату для рокировки в сторону королевского фланга
     * @return координата для рокировки
     */
    public Coordinate getCastleCoordKingK() {
        return castleCoordKingK;
    }

    /**
     * Получает координату для рокировки в сторону ферзевого фланга
     * @return координата для рокировки
     */
    public Coordinate getCastleCoordKingQ() {
        return castleCoordKingQ;
    }

    /**
     * Получает переходную координату для рокировки в сторону королевского фланга
     * @return переходная координата
     */
    public Coordinate getTransitionCoordKingK() {
        return transitionCoordKingK;
    }

    /**
     * Получает переходную координату для рокировки в сторону ферзевого фланга
     * @return переходная координата
     */
    public Coordinate getTransitionCoordKingQ() {
        return transitionCoordKingQ;
    }

    /**
     * Получает ладью на королевском фланге для рокировки
     * @return ладья на королевском фланге
     */
    public Rook getRookKing() {
        return rookKing;
    }

    /**
     * Получает ладью на ферзевом фланге для рокировки
     * @return ладья на ферзевом фланге
     */
    public Rook getRookQueen() {
        return rookQueen;
    }

    /**
     * Проверяет возможность рокировки в сторону королевского фланга
     * @param pieces текущее состояние доски
     * @return true если рокировка возможна
     */
    public boolean canCastleKing (Pieces pieces) {

        if (pieces.isCheck(getColour()))
            return false;

        HashMap<Coordinate, Piece> colouredPieces = pieces.getColourPieces(getColour());

        for (Piece value : colouredPieces.values()) {
            if (value.getName() == ID.ROOK && value.getFile() == BOARD.LAST_FILE.getFileVal())
                rookKing = (Rook) value;
        }

        int distanceRookKing = 2;
        ArrayList<Coordinate> castleCoords;

        if (getColour() == COLOUR.B)
            castleCoords = Move.leftFree(pieces, this, dimension);
        else
            castleCoords = Move.rightFree(pieces, this, dimension);

        boolean isSpace = castleCoords.size() == distanceRookKing;

        boolean canCastle = rookKing != null &&
                !rookKing.getHasMoved() &&
                !getHasMoved() &&
                isSpace;

        if (canCastle) {
            castleCoordKingK = castleCoords.get(1);
            transitionCoordKingK = castleCoords.get(0);
            rookKing.setCastleCoordRook(castleCoords.get(0));
            return true;
        }
        return false;
    }

    /**
     * Проверяет возможность рокировки в сторону ферзевого фланга
     * @param pieces текущее состояние доски
     * @return true если рокировка возможна
     */
    public boolean canCastleQueen (Pieces pieces) {

        if (pieces.isCheck(getColour()))
            return false;

        HashMap<Coordinate,Piece> colouredPieces = pieces.getColourPieces(getColour());

        for (Piece value : colouredPieces.values()) {
            if (value.getName() == ID.ROOK && value.getFile() == BOARD.FIRST_FILE.getFileVal())
                rookQueen = (Rook) value;
        }

        int distanceRookQueen = 3;
        ArrayList<Coordinate> castleCoords;

        if (getColour() == COLOUR.W) {
            castleCoords = Move.leftFree(pieces, this, dimension);
        }
        else {
            castleCoords = Move.rightFree(pieces, this, dimension);
        }

        boolean isSpace = castleCoords.size() == distanceRookQueen;


        boolean canCastle = rookQueen != null &&
                !rookQueen.getHasMoved() &&
                !getHasMoved() &&
                isSpace;

        if (canCastle) {
            castleCoordKingQ = castleCoords.get(1);
            transitionCoordKingQ = castleCoords.get(0);
            rookQueen.setCastleCoordRook(castleCoords.get(0));
            return true;
        }
        return false;
    }

    @Override
    public King makeCopy() {
        return new King(this);
    }

    @Override
    public ArrayList<Coordinate> getRawMoves(Pieces pieces) {
        ArrayList<Coordinate> front = Move.frontFree(pieces,this,single);
        ArrayList<Coordinate> right = Move.rightFree(pieces,this,single);
        ArrayList<Coordinate> back = Move.backFree(pieces,this,single);
        ArrayList<Coordinate> left = Move.leftFree(pieces,this,single);
        ArrayList<Coordinate> frontRDig = Move.frontRDigFree(pieces, this,single);
        ArrayList<Coordinate> backRDig = Move.backRDigFree(pieces, this, single);
        ArrayList<Coordinate> backLDig = Move.backLDigFree(pieces, this,single);
        ArrayList<Coordinate> frontLDig = Move.frontLDigFree(pieces, this, single);

        front.addAll(right);
        back.addAll(left);
        front.addAll(back);

        frontRDig.addAll(backRDig);
        backLDig.addAll(frontLDig);
        frontRDig.addAll(backLDig);

        front.addAll(frontRDig);

        return front;
    }

}