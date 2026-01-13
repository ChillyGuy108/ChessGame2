package org.example;



import org.example.ChessIO;
import org.example.COLOUR;
import org.example.Coordinate;
import org.example.Pieces;
import org.example.Piece;

/**
 * Менеджер для управления историей ходов шахматной игры.
 * Отвечает за запись, хранение и форматирование истории ходов в соответствии
 * со стандартной шахматной нотацией. Управляет нумерацией ходов и обеспечивает
 * корректное отображение последовательности ходов белых и черных фигур.
 */
public class MoveHistoryManager {
    private int numberOfTurns = 0;
    private final StringBuilder moveHistory = new StringBuilder();

    /**
     * Записывает выполненный ход в историю игры.
     * Форматирует ход в соответствии с шахматной нотацией и добавляет номер хода
     * для белых фигур. Для черных фигур ход добавляется без номера.
     *
     * @param coordinate координата, на которую переместилась фигура
     * @param piece фигура, сделавшая ход
     * @param pieces объект, содержащий текущее состояние шахматных фигур
     * @param turn цвет игрока, сделавшего ход
     */
    public void recordMove(Coordinate coordinate, Piece piece, Pieces pieces, COLOUR turn) {
        String moveString = ChessIO.moveString(pieces, coordinate, piece);

        if (turn == COLOUR.W) {
            numberOfTurns++;
            moveHistory.append(numberOfTurns).append(". ").append(moveString).append(" ");
        } else {
            moveHistory.append(moveString).append(" ");
        }

    }

    /**
     * Возвращает полную историю ходов текущей игры в текстовом формате.
     * История включает все ходы в формате стандартной шахматной нотации
     * с нумерацией для белых фигур.
     *
     * @return строка, содержащая все ходы игры в формате "1. e4 e5 2. Nf3 Nc6 ..."
     */
    public String getFullMoveHistory() {
        return moveHistory.toString();
    }

}