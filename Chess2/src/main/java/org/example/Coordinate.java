package org.example;


import org.example.BOARD;
import java.util.Objects;

/**
 * Класс для представления координат на шахматной доске
 */
public class Coordinate {

    /** Файл (вертикаль) координаты */
    public char file;
    /** Ранг (горизонталь) координаты */
    public int rank;
    /** Пустая координата */
    public static Coordinate emptyCoordinate = new Coordinate((char) 0,0);

    /**
     * Конструктор координаты
     * @param file файл (вертикаль) - символ от 'a' до 'j'
     * @param rank ранг (горизонталь) - число от 1 до 10
     */
    public Coordinate (char file, int rank) {

        this.file = Character.toLowerCase(file);
        this.rank = rank;
    }

    /**
     * Конструктор копирования координаты
     * @param original оригинальная координата для копирования
     */
    public Coordinate (Coordinate original) {
        file = original.file;
        rank = original.rank;
    }

    /**
     * Конструктор координаты из строки (например, "a1", "j10")
     * @param coordinate строковое представление координаты
     */
    public Coordinate (String coordinate) {

        if (coordinate.length() == 2 && Character.isLetter(coordinate.charAt(0)) && Character.isDigit(coordinate.charAt(1))) {
            file = Character.toLowerCase(coordinate.charAt(0));
            rank = Character.getNumericValue(coordinate.charAt(1));
        }
        else {
            System.out.println("Некорректная координата. Используется пустая координата.");
            file = 0;
            rank = 0;
        }
    }

    /**
     * Конструктор пустой координаты
     */
    public Coordinate() {
        file = 0;
        rank = 0;
    }

    /**
     * Получает файл (вертикаль) координаты
     * @return символ файла
     */
    public char getFile() {
        return file;
    }

    /**
     * Получает ранг (горизонталь) координаты
     * @return номер ранга
     */
    public int getRank() {
        return rank;
    }

    /**
     * Проверяет, находится ли координата в пределах доски
     * @param coord координата для проверки
     * @return true если координата находится на доске
     */
    public static boolean inBoard (Coordinate coord) {
        char coordFile = coord.getFile();
        int coordRank = coord.getRank();
        return (coordFile >= BOARD.FIRST_FILE.getFileVal()
                && coordFile <= BOARD.LAST_FILE.getFileVal()
                && coordRank >= BOARD.FIRST_RANK.getRankVal()
                && coordRank <= BOARD.LAST_RANK.getRankVal());
    }

    @Override
    public String toString() {
        return file + "" + rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return file == that.file &&
                rank == that.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, rank);
    }


}