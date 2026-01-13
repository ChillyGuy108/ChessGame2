package org.example;


import org.example.COLOUR;
import org.example.ID;
import org.example.Bishop;
import org.example.King;
import org.example.Pawn;
import org.example.Piece;

import java.util.*;


/**
 * Класс для управления всеми фигурами на шахматной доске
 * Содержит логику игры, проверку шаха, мата, ничьей и управления ходами
 */
public class Pieces {

    private HashMap<Coordinate, Piece> pieces;
    private HashMap<Coordinate, Piece> previousPieces;
    private boolean isCapture;
    private boolean isGUIGame;
    private ArrayList<HashMap<Coordinate,Piece>> gameProgress = new ArrayList<>();

    /**
     * Конструктор, создающий начальную позицию доски
     */
    public Pieces() {
        pieces = Boards.getChessBoard();
        previousPieces = copyHashMap(pieces);
        gameProgress.add(copyHashMap(pieces));
        updatePotentials();
    }

    /**
     * Конструктор с заданной начальной позицией
     * @param newBoard карта координат и фигур для начальной позиции
     */
    public Pieces(HashMap<Coordinate, Piece> newBoard) {
        pieces = newBoard;
        previousPieces = copyHashMap(pieces);
        gameProgress.add(copyHashMap(pieces));
        updatePotentials();
    }

    /**
     * Конструктор копирования
     * @param original оригинальный объект Pieces для копирования
     */
    public Pieces (Pieces original) {
        this.pieces = copyHashMap(original.getPieces());
        this.previousPieces = original.previousPieces;
        this.isCapture = original.isCapture;
        this.isGUIGame = original.isGUIGame;
        this.gameProgress = copyArrayHash(original.getGameProgress());
    }

    /**
     * Создает глубокую копию карты координат и фигур
     * @param original оригинальная карта для копирования
     * @return новая карта с копиями всех фигур и координат
     */
    private HashMap<Coordinate, Piece> copyHashMap (HashMap<Coordinate, Piece> original) {

        HashMap<Coordinate, Piece> copyMap = new HashMap<>();
        for (Coordinate key : original.keySet()) {
            Coordinate newKey = new Coordinate(key);
            Piece newPiece = original.get(key).makeCopy();
            copyMap.put(newKey,newPiece);
        }

        return copyMap;
    }

    /**
     * Создает глубокую копию списка карт координат и фигур
     * @param original оригинальный список для копирования
     * @return новый список с копиями всех карт
     */
    private ArrayList<HashMap<Coordinate,Piece>> copyArrayHash (ArrayList<HashMap<Coordinate,Piece>> original) {
        ArrayList<HashMap<Coordinate,Piece>> copyList = new ArrayList<>();
        for (HashMap<Coordinate,Piece> game : original) {
            copyList.add(copyHashMap(game));
        }

        return copyList;
    }

    /**
     * Получает карту всех фигур на доске
     * @return карта координат и фигур
     */
    public HashMap<Coordinate, Piece> getPieces() {
        return pieces;
    }

    /**
     * Устанавливает карту фигур на доске
     * @param pieces карта координат и фигур
     */
    public void setPieces(HashMap<Coordinate,Piece> pieces) {this.pieces = pieces;}

    /**
     * Проверяет, был ли последний ход взятием фигуры
     * @return true если последний ход был взятием
     */
    public boolean getIsCapture() {
        return isCapture;
    }

    /**
     * Устанавливает статус взятия фигуры
     * @param captureStatus true если был взят фигура
     */
    public void setIsCapture(boolean captureStatus) {
        this.isCapture = captureStatus;
    }

    /**
     * Получает карту фигур предыдущего хода
     * @return карта координат и фигур предыдущего состояния
     */
    public HashMap<Coordinate, Piece> getPreviousPieces() {
        return previousPieces;
    }

    /**
     * Устанавливает карту фигур предыдущего хода
     * @param previousPieces карта координат и фигур предыдущего состояния
     */
    public void setPreviousPieces(HashMap<Coordinate, Piece> previousPieces) {
        this.previousPieces = copyHashMap(previousPieces);
    }

    /**
     * Получает историю всех позиций партии
     * @return список карт координат и фигур для каждой позиции
     */
    public ArrayList<HashMap<Coordinate, Piece>> getGameProgress() {
        return gameProgress;
    }

    /**
     * Устанавливает режим игры (GUI или консольный)
     * @param GUIStatus true если игра в GUI режиме
     */
    public void setGUIGame (boolean GUIStatus) {
        isGUIGame = GUIStatus;
    }

    /**
     * Добавляет фигуру на указанную координату
     * @param coordinate координата для размещения фигуры
     * @param piece фигура для добавления
     */
    public void addPiece(Coordinate coordinate, Piece piece) {
        pieces.put(coordinate,piece);
    }

    /**
     * Находит координату указанной фигуры на доске
     * @param piece фигура для поиска
     * @return координата фигуры или пустая координата, если фигура не найдена
     */
    public Coordinate findPiece(Piece piece) {

        Objects.requireNonNull(piece, "Фигура не может быть null.");

        for (Coordinate key : pieces.keySet()) {
            if (pieces.get(key).equals(piece))
                return key;
        }
        System.err.println(piece.getName().toFullString() +" не найдена на доске.");
        return Coordinate.emptyCoordinate;
    }

    /**
     * Находит координату короля указанного цвета
     * @param colour цвет короля для поиска
     * @return координата короля или пустая координата, если король не найден
     */
    public Coordinate findKing(COLOUR colour) {
        for (Coordinate key : pieces.keySet()) {
            Piece potentialKing = pieces.get(key);
            if (potentialKing.getName().equals(ID.KING) && (potentialKing.getColour() == colour))
                return key;
        }
        String pieceNotInBoard = "Король не найден на доске.";
        System.err.println(pieceNotInBoard);
        return Coordinate.emptyCoordinate;
    }

    /**
     * Получает фигуру на указанной координате
     * @param coordinate координата для проверки
     * @return фигура на координате или пустая фигура, если координата пуста
     */
    public Piece getPiece(Coordinate coordinate) {

        Objects.requireNonNull(coordinate, "Координата не может быть null.");

        for (Coordinate key : pieces.keySet()) {
            if (key.equals(coordinate))
                return pieces.get(key);
        }
        System.err.println("На указанной координате нет фигуры.");
        return Piece.emptyPiece;
    }

    /**
     * Получает все фигуры указанного цвета
     * @param colour цвет фигур для получения
     * @return карта координат и фигур указанного цвета
     */
    public HashMap<Coordinate, Piece> getColourPieces(COLOUR colour) {
        HashMap<Coordinate,Piece> colours = new HashMap<>();
        for (Coordinate key : pieces.keySet()) {
            Piece piece = pieces.get(key);
            if (piece.getColour() == colour)
                colours.put(key,piece);
        }
        return colours;
    }

    /**
     * Получает все возможные ходы всех фигур указанного цвета (с учетом шаха)
     * @param colour цвет фигур
     * @return множество координат всех возможных ходов
     */
    public HashSet<Coordinate> allColouredPotentials (COLOUR colour) {
        HashSet<Coordinate> allMoves = new HashSet<>();
        HashMap<Coordinate, Piece> allColoured = getColourPieces(colour);
        for (Piece piece : allColoured.values()){
            allMoves.addAll(piece.getPotentialMoves());
        }
        return allMoves;
    }

    /**
     * Получает все возможные ходы всех фигур указанного цвета (без учета шаха)
     * @param colour цвет фигур
     * @return множество координат всех возможных ходов
     */
    public HashSet<Coordinate> allColouredRaws (COLOUR colour) {
        HashSet<Coordinate> allMoves = new HashSet<>();
        HashMap<Coordinate, Piece> allColoured = getColourPieces(colour);
        for (Piece piece : allColoured.values()){
            allMoves.addAll(piece.getRawMoves(this));
        }
        return allMoves;
    }

    /**
     * Обновляет предыдущие координаты всех пешек после хода
     */
    public void updatePreviousMovePawns () {
        for (Piece potentialPawn : pieces.values()){
            if (potentialPawn.getName() == ID.PAWN) {
                Pawn pawn = (Pawn) potentialPawn;
                pawn.setPreviousCoordinate(pawn.getCoords());
            }
        }
    }

    /**
     * Проверяет, есть ли другая фигура того же типа в том же файле
     * @param piece фигура для проверки
     * @return true если есть другая фигура того же типа в том же файле
     */
    public boolean pieceInSameFile (Piece piece) {

        if (piece.getName() == ID.KING)
            return false;

        HashMap <Coordinate, Piece> coloured = getColourPieces(piece.getColour());
        for (Piece value : coloured.values()) {
            if (value.getName() == piece.getName() && value.getFile() == piece.getFile() && !value.equals(piece))
                return true;
        }
        return false;
    }

    /**
     * Проверяет, есть ли другая фигура того же типа в том же ранге
     * @param piece фигура для проверки
     * @return true если есть другая фигура того же типа в том же ранге
     */
    public boolean pieceInSameRank (Piece piece) {

        if (piece.getName() == ID.KING)
            return false;

        HashMap <Coordinate, Piece> coloured = getColourPieces(piece.getColour());
        for (Piece value : coloured.values()) {
            if (value.getName() == piece.getName() && value.getRank() == piece.getRank() && !value.equals(piece))
                return true;
        }
        return false;
    }

    /**
     * Проверяет, может ли другая фигура того же типа сделать ход на ту же координату
     * @param coordinate координата для проверки
     * @param piece фигура для проверки
     * @return true если другая фигура того же типа может сделать ход на эту координату
     */
    public boolean pieceToSameCoordinate (Coordinate coordinate, Piece piece) {
        assert piece.getPotentialMoves().contains(coordinate);

        if (piece.getName() == ID.KING)
            return false;

        HashMap <Coordinate, Piece> coloured = getColourPieces(piece.getColour());
        for (Piece value : coloured.values()) {
            if (value.getName() == piece.getName() && value.getPotentialMoves().contains(coordinate) && !value.equals(piece))
                return true;
        }
        return false;
    }

    /**
     * Проверяет, находится ли король указанного цвета под шахом
     * @param colour цвет короля для проверки
     * @return true если король под шахом
     * @throws IllegalArgumentException если король не найден на доске
     */
    public boolean isCheck(COLOUR colour) {
        Coordinate kingPosition = findKing(colour);

        if (kingPosition.equals(Coordinate.emptyCoordinate))
            throw new IllegalArgumentException("Король не найден на доске!");

        HashSet<Coordinate> dangerMoves = allColouredPotentials(COLOUR.not(colour));
        return (dangerMoves.contains(kingPosition));
    }

    /**
     * Проверяет, находится ли король указанного цвета под матом
     * @param colour цвет короля для проверки
     * @return true если король под матом
     */
    public boolean isMate(COLOUR colour) {
        HashSet<Coordinate> allMoves = allColouredPotentials(colour);
        return isCheck(colour) && (allMoves.size() == 0);
    }

    /**
     * Проверяет, является ли текущая позиция ничьей
     * Учитывает случаи: только короли, король против короля и слона/коня,
     * король и слон против короля и слона на полях одного цвета, троекратное повторение позиции
     * @return true если позиция является ничьей
     */
    public boolean isDraw() {

        int n = gameProgress.size();

        boolean twoKings = !findKing(COLOUR.B).equals(Coordinate.emptyCoordinate) && !findKing(COLOUR.W).equals(Coordinate.emptyCoordinate);

        if (getPieces().size() == 2)
            return twoKings;
        else if (getPieces().size() == 3) {
            int counter = 0;
            for (Piece piece : this.getPieces().values()) {
                if (piece.getName() == ID.BISHOP || piece.getName() == ID.KNIGHT)
                    counter++;
            }
            return twoKings && counter == 1;
        }
        else if (getPieces().size() == 4) {
            int counterB = 0;
            Bishop bishopB = null;
            int counterW = 0;
            Bishop bishopW = null;
            for (Piece piece : this.getPieces().values()) {
                if (piece.getName() == ID.BISHOP) {
                    if (piece.getColour() == COLOUR.B) {
                        bishopB = (Bishop) piece;
                        counterB++;
                    }
                    else {
                        bishopW = (Bishop) piece;
                        counterW++;
                    }
                }
            }

            boolean sameColourBishops = counterB == 1 &&
                    counterW == 1 &&
                    bishopB.getOGcoord().getFile() != bishopW.getOGcoord().getFile();

            return twoKings && sameColourBishops;
        }
        else if (n >= 3){
            for (HashMap<Coordinate, Piece> currentGame : gameProgress) {
                int counter = 0;
                for (HashMap<Coordinate, Piece> checkGame : gameProgress) {
                    if (currentGame.equals(checkGame)) {
                        counter++;
                    }
                }
                if (counter == 3)
                    return true;
            }

        }

        return false;

    }

    /**
     * Проверяет, является ли позиция патом для указанного цвета
     * @param colour цвет, для которого проверяется пат
     * @return true если позиция является патом (нет ходов, но нет шаха)
     */
    public boolean isStalemate(COLOUR colour) {
        HashSet<Coordinate> allMoves = allColouredPotentials(COLOUR.not(colour));
        return allMoves.size() == 0 && !isCheck(COLOUR.not(colour));

    }

    /**
     * Перемещает фигуру на указанную координату
     * @param coordinate координата назначения
     * @param piece фигура для перемещения
     */
    public void pieceMove (Coordinate coordinate, Piece piece) {
        Coordinate pieceCoord = findPiece(piece);
        addPiece(coordinate, piece);
        piece.setCoords(coordinate);
        piece.setHasMoved();
        pieces.remove(pieceCoord);
    }

    /**
     * Выполняет ход фигуры на указанную координату
     * Обрабатывает специальные случаи: рокировку, продвижение пешки
     * @param coordinate координата назначения
     * @param piece фигура, делающая ход
     */
    public void makeMove (Coordinate coordinate, Piece piece) {

        if (piece.isValidMove(coordinate, piece.getColour())) {
            setPreviousPieces(this.getPieces());
            isCapture = Move.tileFull(this, coordinate) && Move.isNotTileColour(this,coordinate, piece.getColour());
            if (piece.getName() == ID.KING) {
                King castleKing = (King) piece;
                pieceMove(coordinate, castleKing);
            }
            else if (piece.getName() == ID.PAWN) {
                Pawn pawn = (Pawn) piece;

                updatePreviousMovePawns();
                if (Math.abs(coordinate.getRank() - pawn.getRank()) == 2)
                    pawn.setHasMovedTwo();

                if (pawn.canPromoteBlack(coordinate) || pawn.canPromoteWhite(coordinate)) {
                    Piece toPromote;

                    if (isGUIGame) {
                        toPromote = pawn.getPromotedPiece();
                        if (toPromote == null) {
                            toPromote = pawn.promotionQuery(coordinate);
                        }
                    }
                    else {
                        toPromote = pawn.promotionQuery(coordinate);
                    }
                    Coordinate pieceCoord = findPiece(piece);
                    addPiece(coordinate, toPromote);
                    pieces.remove(pieceCoord);
                }
                else {
                    pieceMove(coordinate, pawn);
                }
            }
            else {
                pieceMove(coordinate, piece);
            }
        }
        else
            System.err.println(piece.getName().toFullString() + " не может сделать ход на " + coordinate.toString() + ".");

        gameProgress.add(copyHashMap(pieces));
        updatePotentials();

    }

    /**
     * Обновляет список возможных ходов для всех фигур на доске
     */
    public void updatePotentials() {

        for (Piece value : pieces.values()) {
            value.clearMoves();
            value.updatePotentialMoves(this);
        }
    }

    /**
     * Возвращает строковое представление всех фигур на доске
     * @return строковое представление с идентификаторами фигур и их координатами
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        pieces.forEach((coord, piece) -> str.append(piece.getPieceID())
                .append(" на ")
                .append(coord.toString())
                .append("\n"));

        return str.toString();
    }

    /**
     * Сравнивает два объекта Pieces на равенство
     * @param o объект для сравнения
     * @return true если объекты равны (содержат одинаковые фигуры)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pieces pieces1 = (Pieces) o;
        return Objects.equals(pieces, pieces1.pieces);
    }

    /**
     * Возвращает хэш-код объекта Pieces
     * @return хэш-код на основе карты фигур
     */
    @Override
    public int hashCode() {
        return Objects.hash(pieces);
    }
}
