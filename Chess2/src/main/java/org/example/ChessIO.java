package org.example;



import org.example.COLOUR;
import org.example.ID;
import org.example.Coordinate;
import org.example.Pieces;
import org.example.King;
import org.example.Pawn;
import org.example.Piece;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Класс для работы с вводом-выводом шахматных данных
 * Содержит методы для форматирования ходов и сохранения игры
 */
public class ChessIO {

    private static final String errorSave = "$$$";

    /**
     * Форматирует ход в строковое представление в шахматной нотации
     * @param pieces текущее состояние доски
     * @param coordinate координата назначения
     * @param piece фигура, делающая ход
     * @return строковое представление хода
     */
    public static String moveString (Pieces pieces, Coordinate coordinate, Piece piece) {

        boolean isCastle = false;

        StringBuilder str = new StringBuilder();
        Pieces previousBoard = new Pieces(pieces.getPreviousPieces());
        Coordinate previousCoordinate = previousBoard.findPiece(piece);
        Piece previousPiece = previousBoard.getPiece(previousCoordinate);

        if (piece.getName() != ID.KING) {
            str.append(piece.getName().toString());
        }
        else {
            King king = (King) piece;
            King previousKing = (King) previousPiece;

            if (coordinate.equals(king.getCastleCoordKingQ()) && previousKing.canCastleQueen(previousBoard)) {
                str.append("O-O-O");
                isCastle = true;
            }
            else if (coordinate.equals(king.getCastleCoordKingK()) && previousKing.canCastleKing(previousBoard)) {
                str.append("O-O");
                isCastle = true;
            }
            else
                str.append(piece.getName().toString());
        }

        str.append(removeAmbiguous(previousBoard, coordinate, previousPiece));

        if (pieces.getIsCapture()) {
            if (piece.getName() == ID.PAWN) {
                assert piece instanceof Pawn;
                Pawn pawn = (Pawn) piece;
                str.append(pawn.getPreviousCoordinate().getFile()).append("x");
            }
            else
                str.append("x");
        }

        if (!isCastle) {
            str.append(coordinate.toString());
        }

        if (piece.getName() == ID.PAWN) {
            Pawn pawn = (Pawn) piece;
            if (pawn.canPromoteBlack(coordinate) || pawn.canPromoteWhite(coordinate))
                str.append("=").append(pawn.getPromotedPiece().getName().toString());
        }

        if (pieces.isMate(COLOUR.not(piece.getColour())))
            str.append("#");
        else if (pieces.isCheck(COLOUR.not(piece.getColour())))
            str.append("+");

        return str.toString();
    }

    public static String removeAmbiguous (Pieces pieces, Coordinate coordinate, Piece piece) {
        if (pieces.pieceToSameCoordinate(coordinate, piece)) {
            if (pieces.pieceInSameRank(piece))
                return piece.getFile() + "";
            else if (pieces.pieceInSameFile(piece))
                return piece.getRank() + "";
            else
                return "";
        }
        else
            return "";
    }

    /**
     * Преобразует имя файла в формат .txt
     * @param filePath путь к файлу
     * @return путь к файлу с расширением .txt или код ошибки
     */
    public static String toTxt (String filePath) {

        filePath = filePath.replaceAll("\\s","");

        if (filePath.length() == 0)
            return errorSave;

        int periodCheck = filePath.lastIndexOf(".");

        if (periodCheck == -1)
            return filePath + ".txt";
        else if (filePath.substring(periodCheck).length() == 4)
            return filePath;
        else
            return errorSave;
    }

    /**
     * Проверяет, является ли строка кодом ошибки сохранения
     * @param potentialFile строка для проверки
     * @return true если это код ошибки
     */
    public static boolean isErrorSave (String potentialFile) {
        return errorSave.equals(potentialFile);
    }

    /**
     * Сохраняет партию в файл
     * @param game строковое представление партии
     * @param saveFile путь к файлу для сохранения
     * @return true если сохранение прошло успешно
     */
    public static boolean saveGame(String game, Path saveFile) {

        Objects.requireNonNull(game,"  !");
        Objects.requireNonNull(saveFile,"  !");

        String path = String.valueOf(saveFile);
        File gameFile = new File(path);
        if (!gameFile.exists()) {
            try {
                FileWriter writer = new FileWriter(path);
                writer.write(game);
                writer.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}