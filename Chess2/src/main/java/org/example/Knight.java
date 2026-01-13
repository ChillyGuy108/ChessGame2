package org.example;



import org.example.Coordinate;
import org.example.Move;
import org.example.Pieces;
import org.example.BOARD;
import org.example.COLOUR;
import org.example.ID;

import java.util.ArrayList;

/**
 * Класс, представляющий коня в шахматах
 */
public class Knight extends Piece {

    /**
     * Конструктор коня
     * @param colour цвет коня
     * @param OGcoord исходная координата коня
     */
    public Knight(COLOUR colour, Coordinate OGcoord) {
        super(ID.KNIGHT, colour, OGcoord);
    }

    /**
     * Конструктор копирования коня
     * @param original оригинальный конь для копирования
     */
    public Knight(Knight original) {
        super(original);
    }

    @Override
    public Knight makeCopy() {
        return new Knight(this);
    }

    @Override
    public ArrayList<Coordinate> getRawMoves(Pieces pieces) {
        ArrayList<Coordinate> front = Move.frontKnight(pieces,this);
        ArrayList<Coordinate> right = Move.backKnight(pieces,this);
        ArrayList<Coordinate> back = Move.rightKnight(pieces,this);
        ArrayList<Coordinate> left = Move.leftKnight(pieces,this);

        front.addAll(right);
        back.addAll(left);
        front.addAll(back);

        return front;
    }

}