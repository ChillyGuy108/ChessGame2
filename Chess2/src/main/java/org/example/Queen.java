package org.example;



import org.example.Coordinate;
import org.example.Move;
import org.example.Pieces;
import org.example.COLOUR;
import org.example.ID;


import java.util.ArrayList;

/**
 * Класс, представляющий ферзя в шахматах
 */
public class Queen extends Piece {

    /**
     * Конструктор ферзя
     * @param colour цвет ферзя
     * @param OGcoord исходная координата ферзя
     */
    public Queen(COLOUR colour, Coordinate OGcoord) {
        super(ID.QUEEN, colour, OGcoord);
    }

    /**
     * Конструктор копирования ферзя
     * @param original оригинальный ферзь для копирования
     */
    public Queen(Queen original) {
        super(original);
    }

    @Override
    public Queen makeCopy() {
        return new Queen(this);
    }

    @Override
    public ArrayList<Coordinate> getRawMoves(Pieces pieces) {

        ArrayList<Coordinate> front = Move.frontFree(pieces,this,dimension);
        ArrayList<Coordinate> right = Move.rightFree(pieces,this,dimension);
        ArrayList<Coordinate> back = Move.backFree(pieces,this,dimension);
        ArrayList<Coordinate> left = Move.leftFree(pieces,this,dimension);
        ArrayList<Coordinate> frontRDig = Move.frontRDigFree(pieces, this,dimension);
        ArrayList<Coordinate> backRDig = Move.backRDigFree(pieces, this, dimension);
        ArrayList<Coordinate> backLDig = Move.backLDigFree(pieces, this,dimension);
        ArrayList<Coordinate> frontLDig = Move.frontLDigFree(pieces, this, dimension);

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