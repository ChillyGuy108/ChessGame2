package org.example;


/**
 * Перечисление для определения границ шахматной доски
 */
public enum BOARD {

    /** Первая вертикаль (файл) доски */
    FIRST_FILE('a'),
    /** Последняя вертикаль (файл) доски */
    LAST_FILE('j'),
    /** Первая горизонталь (ранг) доски */
    FIRST_RANK(1),
    /** Последняя горизонталь (ранг) доски */
    LAST_RANK(8);

    private char fileVal;
    private int rankVal;

    /**
     * Конструктор для файлов (вертикалей)
     * @param file символ файла
     */
    BOARD(char file) {
        fileVal = file;
    }

    /**
     * Конструктор для рангов (горизонталей)
     * @param rank номер ранга
     */
    BOARD(int rank) {
        rankVal = rank;
    }

    /**
     * Получает значение файла (вертикали)
     * @return символ файла
     */
    public char getFileVal() {
        return fileVal;
    }

    /**
     * Получает значение ранга (горизонтали)
     * @return номер ранга
     */
    public int getRankVal() {
        return rankVal;
    }
}