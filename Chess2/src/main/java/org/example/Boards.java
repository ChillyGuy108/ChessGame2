package org.example;



import org.example.COLOUR;
import org.example.Bishop;
import org.example.Knight;
import org.example.Pawn;
import org.example.Queen;
import org.example.King;
import org.example.Rook;
import org.example.Piece;
import java.util.HashMap;

/**
 * Класс для создания начальной позиции обычной шахматной доски
 */
public class Boards {

    /**
     * Создает начальную позицию всех фигур на доске для обычных шахмат
     * @return карта координат и фигур для начальной позиции
     */
    public static HashMap<Coordinate, Piece> getChessBoard() {

        HashMap<Coordinate, Piece> pieces = new HashMap<>();

        // Пешки черных на 7-й горизонтали (индекс 7 в 0-based системе)
        int blackPawnRank = 7;
        // Фигуры черных на 8-й горизонтали (индекс 8 в 0-based системе)
        int blackPieceRank = 8;
        // Пешки белых на 2-й горизонтали (индекс 2 в 0-based системе)
        int whitePawnRank = 2;
        // Фигуры белых на 1-й горизонтали (индекс 1 в 0-based системе)
        int whitePieceRank = 1;

        // Черные пешки (от a до h)
        for (char file = 'a'; file <= 'h'; file++) {
            pieces.put(new Coordinate(file, blackPawnRank),
                    new Pawn(COLOUR.B, new Coordinate(file, blackPawnRank)));
        }

        // Белые пешки (от a до h)
        for (char file = 'a'; file <= 'h'; file++) {
            pieces.put(new Coordinate(file, whitePawnRank),
                    new Pawn(COLOUR.W, new Coordinate(file, whitePawnRank)));
        }

        // Черные ладьи
        pieces.put(new Coordinate('a', blackPieceRank),
                new Rook(COLOUR.B, new Coordinate('a', blackPieceRank)));
        pieces.put(new Coordinate('h', blackPieceRank),
                new Rook(COLOUR.B, new Coordinate('h', blackPieceRank)));

        // Черные кони
        pieces.put(new Coordinate('b', blackPieceRank),
                new Knight(COLOUR.B, new Coordinate('b', blackPieceRank)));
        pieces.put(new Coordinate('g', blackPieceRank),
                new Knight(COLOUR.B, new Coordinate('g', blackPieceRank)));

        // Черные слоны
        pieces.put(new Coordinate('c', blackPieceRank),
                new Bishop(COLOUR.B, new Coordinate('c', blackPieceRank)));
        pieces.put(new Coordinate('f', blackPieceRank),
                new Bishop(COLOUR.B, new Coordinate('f', blackPieceRank)));

        // Черный ферзь
        pieces.put(new Coordinate('d', blackPieceRank),
                new Queen(COLOUR.B, new Coordinate('d', blackPieceRank)));

        // Черный король
        pieces.put(new Coordinate('e', blackPieceRank),
                new King(COLOUR.B, new Coordinate('e', blackPieceRank)));

        // Белые ладьи
        pieces.put(new Coordinate('a', whitePieceRank),
                new Rook(COLOUR.W, new Coordinate('a', whitePieceRank)));
        pieces.put(new Coordinate('h', whitePieceRank),
                new Rook(COLOUR.W, new Coordinate('h', whitePieceRank)));

        // Белые кони
        pieces.put(new Coordinate('b', whitePieceRank),
                new Knight(COLOUR.W, new Coordinate('b', whitePieceRank)));
        pieces.put(new Coordinate('g', whitePieceRank),
                new Knight(COLOUR.W, new Coordinate('g', whitePieceRank)));

        // Белые слоны
        pieces.put(new Coordinate('c', whitePieceRank),
                new Bishop(COLOUR.W, new Coordinate('c', whitePieceRank)));
        pieces.put(new Coordinate('f', whitePieceRank),
                new Bishop(COLOUR.W, new Coordinate('f', whitePieceRank)));

        // Белый ферзь
        pieces.put(new Coordinate('d', whitePieceRank),
                new Queen(COLOUR.W, new Coordinate('d', whitePieceRank)));

        // Белый король
        pieces.put(new Coordinate('e', whitePieceRank),
                new King(COLOUR.W, new Coordinate('e', whitePieceRank)));

        return pieces;
    }
}
