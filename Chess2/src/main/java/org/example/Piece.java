package org.example;


import org.example.Coordinate;
import org.example.Pieces;
import org.example.BOARD;
import org.example.COLOUR;
import org.example.ID;

import java.util.*;


/**
 * Абстрактный базовый класс для всех шахматных фигур
 */
public abstract class Piece{
    private final ID name;
    private final COLOUR colour;
    private Coordinate coords;
    private final Coordinate OGcoord;
    private final String pieceID;
    private HashSet<Coordinate> potentialMoves = new HashSet<>();
    public int dimension = BOARD.LAST_RANK.getRankVal();
    public int single = BOARD.FIRST_RANK.getRankVal();
    private boolean hasMoved = false;
    public static Piece emptyPiece = new Rook(COLOUR.W,Coordinate.emptyCoordinate);

    /**
     * Конструктор фигуры
     * @param name тип фигуры
     * @param colour цвет фигуры
     * @param OGcoord исходная координата фигуры
     */
    public Piece (ID name, COLOUR colour, Coordinate OGcoord) {

        Objects.requireNonNull(name, "Тип фигуры должен быть определен в enum ID");
        Objects.requireNonNull(colour, "Цвет фигуры должен быть определен.");
        Objects.requireNonNull(OGcoord, "Исходная координата фигуры должна быть определена.");

        this.name = name;
        this.colour = colour;
        this.OGcoord = OGcoord;
        coords = OGcoord;
        pieceID = "*"+name.toString()+"*"+colour.toString()+"*"+OGcoord.getFile()+"*";
    }

    /**
     * Конструктор копирования фигуры
     * @param original оригинальная фигура для копирования
     */
    public Piece (Piece original) {
        Objects.requireNonNull(original,"Нельзя создать копию из null объекта");
        this.name = original.name;
        this.colour = original.colour;
        this.OGcoord = new Coordinate(original.OGcoord);
        this.coords = new Coordinate(original.coords);
        this.pieceID = original.pieceID;
        this.potentialMoves = new HashSet<>();

        for (Coordinate coord : original.getPotentialMoves()) {
            this.potentialMoves.add(new Coordinate(coord));
        }

        this.dimension = original.dimension;
        this.single = original.single;
        this.hasMoved = original.hasMoved;
    }

    /**
     * Получает текущие координаты фигуры
     * @return текущие координаты
     */
    public Coordinate getCoords() {
        return coords;
    }

    /**
     * Получает файл (вертикаль) текущей позиции фигуры
     * @return символ файла
     */
    public char getFile() {
        return getCoords().getFile();
    }

    /**
     * Получает ранг (горизонталь) текущей позиции фигуры
     * @return номер ранга
     */
    public int getRank() {
        return getCoords().getRank();
    }

    /**
     * Получает цвет фигуры
     * @return цвет фигуры
     */
    public COLOUR getColour() {
        return colour;
    }

    /**
     * Получает тип фигуры
     * @return тип фигуры
     */
    public ID getName() {
        return name;
    }

    /**
     * Получает исходную координату фигуры
     * @return исходная координата
     */
    public Coordinate getOGcoord() {
        return OGcoord;
    }

    /**
     * Получает уникальный идентификатор фигуры
     * @return строковый идентификатор фигуры
     */
    public String getPieceID() {
        return pieceID;
    }

    /**
     * Устанавливает новые координаты фигуры
     * @param coords новые координаты
     */
    public void setCoords(Coordinate coords) {
        this.coords = coords;
    }

    /**
     * Проверяет, делала ли фигура ход
     * @return true если фигура уже делала ход
     */
    public boolean getHasMoved() {return hasMoved;}

    /**
     * Отмечает, что фигура сделала ход
     */
    public void setHasMoved() {hasMoved = true;}

    /**
     * Добавляет возможные ходы к списку потенциальных ходов
     * @param someMoves список координат для добавления
     */
    public void addMoves(ArrayList<Coordinate> someMoves) {
        potentialMoves.addAll(someMoves);
    }

    /**
     * Очищает список потенциальных ходов
     */
    public void clearMoves() {
        potentialMoves.clear();
    }

    /**
     * Получает множество потенциальных ходов фигуры
     * @return множество координат возможных ходов
     */
    public HashSet<Coordinate> getPotentialMoves() {
        return potentialMoves;
    }

    /**
     * Удаляет из списка возможных ходов те, которые оставляют короля под шахом
     * @param pieces текущее состояние доски
     * @return список валидных ходов без шаха собственному королю
     */
    public ArrayList<Coordinate> removeOwnCheck(Pieces pieces) {

        ArrayList<Coordinate> potentials = getRawMoves(pieces);

        if (potentials.size() == 0)
            return potentials;

        Iterator<Coordinate> it = potentials.iterator();

        while (it.hasNext()) {
            Coordinate nextMove = it.next();
            Pieces p = new Pieces(pieces);
            p.pieceMove(nextMove, this.makeCopy());
            Coordinate kingPosition = p.findKing(getColour());
            HashSet<Coordinate> dangerMoves = p.allColouredRaws(COLOUR.not(getColour()));
            if (dangerMoves.contains(kingPosition))
                it.remove();
        }

        return potentials;
    }

    /**
     * Обновляет список потенциальных ходов фигуры с учетом шаха
     * @param pieces текущее состояние доски
     */
    public void updatePotentialMoves(Pieces pieces) {
        addMoves(removeOwnCheck(pieces));
    }

    /**
     * Проверяет, является ли ход валидным для данной фигуры
     * @param destination координата назначения
     * @param colour цвет фигуры, делающей ход
     * @return true если ход валиден
     */
    public boolean isValidMove(Coordinate destination, COLOUR colour) {
        return getPotentialMoves().contains(destination) && getColour() == colour;
    }

    @Override
    public String toString() {
        return name.toString() + coords.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return name == piece.name &&
                colour == piece.colour &&
                OGcoord.equals(piece.OGcoord) &&
                pieceID.equals(piece.pieceID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, colour, OGcoord, pieceID);
    }

    /**
     * Получает список всех возможных ходов фигуры без учета шаха
     * @param pieces текущее состояние доски
     * @return список координат возможных ходов
     */
    public abstract ArrayList<Coordinate> getRawMoves(Pieces pieces);

    /**
     * Создает копию фигуры
     * @return копия фигуры
     */
    public abstract Piece makeCopy();
}