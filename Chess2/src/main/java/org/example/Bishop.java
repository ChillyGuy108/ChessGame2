package org.example;

import java.util.ArrayList;

import org.example.Coordinate;
import org.example.Move;
import org.example.Pieces;
import org.example.COLOUR;
import org.example.ID;


/**
 * Класс, представляющий слона в шахматах
 */
public class Bishop extends Piece {

    /**
     * Конструктор слона
     * @param colour цвет слона
     * @param OGcoord исходная координата слона
     */
    public Bishop(COLOUR colour, Coordinate OGcoord) {
        super(ID.BISHOP, colour, OGcoord);
    }

    /**
     * Конструктор копирования слона
     * @param original оригинальный слон для копирования
     */
    public Bishop(Bishop original) {
        super(original);
    }

    @Override
    public Bishop makeCopy() {
        return new Bishop(this);
    }

    @Override
    public ArrayList<Coordinate> getRawMoves(Pieces pieces) {
        ArrayList<Coordinate> frontRDig = Move.frontRDigFree(pieces, this,dimension);
        ArrayList<Coordinate> backRDig = Move.backRDigFree(pieces, this, dimension);
        ArrayList<Coordinate> backLDig = Move.backLDigFree(pieces, this,dimension);
        ArrayList<Coordinate> frontLDig = Move.frontLDigFree(pieces, this, dimension);

        frontRDig.addAll(backRDig);
        backLDig.addAll(frontLDig);
        frontRDig.addAll(backLDig);

        return frontRDig;
    }

}