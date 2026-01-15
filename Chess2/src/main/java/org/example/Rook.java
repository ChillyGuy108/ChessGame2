package org.example;



import org.example.Coordinate;
import org.example.Move;
import org.example.Pieces;
import org.example.COLOUR;
import org.example.ID;


import java.util.ArrayList;

/**
 * Класс, представляющий ладью в шахматах
 */
public class Rook extends Piece {

    private Coordinate castleCoordRook;

    /**
     * Конструктор ладьи
     * @param colour цвет ладьи
     * @param OGcoord исходная координата ладьи
     */
    public Rook(COLOUR colour, Coordinate OGcoord) {
        super(ID.ROOK, colour, OGcoord);
    }

    /**
     * Конструктор копирования ладьи
     * @param original оригинальная ладья для копирования
     */
    public Rook (Rook original) {
        super(original);
    }

    /**
     * Получает координату для рокировки
     * @return координата для рокировки
     */
    public Coordinate getCastleCoordRook() {
        return castleCoordRook;
    }

    /**
     * Устанавливает координату для рокировки
     * @param castleCoordRook координата для рокировки
     */
    public void setCastleCoordRook(Coordinate castleCoordRook) {
        this.castleCoordRook = castleCoordRook;
    }

    @Override
    public Rook makeCopy() {
        return new Rook(this);
    }

    @Override
    public ArrayList<Coordinate> getRawMoves(Pieces pieces) {
        ArrayList<Coordinate> front = Move.frontFree(pieces,this,dimension);
        ArrayList<Coordinate> right = Move.rightFree(pieces,this,dimension);
        ArrayList<Coordinate> back = Move.backFree(pieces,this,dimension);
        ArrayList<Coordinate> left = Move.leftFree(pieces,this,dimension);

        front.addAll(right);
        back.addAll(left);
        front.addAll(back);

        return front;
    }




}