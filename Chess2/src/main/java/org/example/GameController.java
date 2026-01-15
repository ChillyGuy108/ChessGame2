package org.example;

import org.example.COLOUR;
import org.example.ID;
import org.example.GameSaveHandler;
import org.example.PawnPromotionHandler;
import org.example.BoardPanel;
import org.example.InfoPanel;
import org.example.Coordinate;
import org.example.Pieces;
import org.example.Pawn;
import org.example.Piece;
import org.example.King;
import org.example.Rook;

/**
 * Главный контроллер игры, управляющий логикой шахматного приложения.
 * Координирует взаимодействие между пользовательским интерфейсом и игровым движком.
 * Обрабатывает ходы игроков, проверяет правила игры и управляет состоянием игры.
 */
public class GameController {
    private final Pieces pieces;
    private final BoardPanel boardPanel;
    private final InfoPanel infoPanel;

    private COLOUR currentTurn = COLOUR.W;
    private Piece selectedPiece;
    private int clickCounter = 0;
    private boolean gameActive = true;

    /**
     * Конструктор игрового контроллера.
     * Инициализирует контроллер с необходимыми компонентами игры.
     *
     * @param pieces объект, управляющий шахматными фигурами и их состоянием
     * @param boardPanel панель отображения шахматной доски
     * @param infoPanel панель отображения информации о ходе игры
     */
    public GameController(Pieces pieces, BoardPanel boardPanel, InfoPanel infoPanel) {
        this.pieces = pieces;
        this.boardPanel = boardPanel;
        this.infoPanel = infoPanel;
    }

    /**
     * Обрабатывает клик пользователя по клетке шахматной доски.
     * Определяет логику выбора фигуры и выполнения хода.
     * Управляет состоянием выбора фигуры и проверяет допустимость ходов.
     *
     * @param coordinate координата клетки, по которой был произведен клик
     * @see Coordinate
     * @see Piece
     */
    public void handleTileClick(Coordinate coordinate) {
        if (!gameActive) return;

        Piece clickedPiece = pieces.getPieces().get(coordinate);

        if (clickCounter == 0) {
            // Первый клик - выбор фигуры
            if (clickedPiece != null && clickedPiece.getColour() == currentTurn) {
                selectPiece(clickedPiece);
            }
        } else {
            // Второй клик - попытка хода
            if (selectedPiece != null) {
                // Проверяем, является ли ход рокировкой
                if (selectedPiece.getName() == ID.KING && isCastlingMove(coordinate)) {
                    handleCastlingMove(coordinate);
                }
                // Проверяем обычный ход
                else if (selectedPiece.isValidMove(coordinate, currentTurn)) {
                    handleMove(coordinate);
                } else {
                    // Если кликнули на другую свою фигуру - выбираем её
                    if (clickedPiece != null && clickedPiece.getColour() == currentTurn) {
                        selectPiece(clickedPiece);
                    } else {
                        resetSelection();
                    }
                }
            }
        }
    }

    /**
     * Проверяет, является ли ход рокировкой
     * @param targetCoordinate целевая координата
     * @return true если это ход рокировки
     */
    private boolean isCastlingMove(Coordinate targetCoordinate) {
        if (selectedPiece.getName() != ID.KING) {
            return false;
        }

        King king = (King) selectedPiece;
        Coordinate currentCoord = king.getCoords();

        // Проверяем, что король перемещается на две клетки по горизонтали
        int fileDiff = Math.abs(targetCoordinate.getFile() - currentCoord.getFile());
        int rankDiff = Math.abs(targetCoordinate.getRank() - currentCoord.getRank());

        return fileDiff == 2 && rankDiff == 0;
    }

    /**
     * Обрабатывает ход рокировки
     * @param targetCoordinate целевая координата для короля
     */
    private void handleCastlingMove(Coordinate targetCoordinate) {
        King king = (King) selectedPiece;
        Coordinate currentCoord = king.getCoords();

        // Определяем сторону рокировки
        boolean isKingside = targetCoordinate.getFile() > currentCoord.getFile();

        // Получаем координаты для рокировки
        Coordinate kingTargetCoord;
        Rook rook;
        Coordinate rookTargetCoord;

        if (isKingside) {
            if (!king.canCastleKing(pieces)) {
                resetSelection();
                return;
            }
            kingTargetCoord = king.getCastleCoordKingK();
            rook = king.getRookKing();
            rookTargetCoord = rook.getCastleCoordRook(); // F1/F8 для короткой рокировки
        } else {
            if (!king.canCastleQueen(pieces)) {
                resetSelection();
                return;
            }
            kingTargetCoord = king.getCastleCoordKingQ();
            rook = king.getRookQueen();
            rookTargetCoord = rook.getCastleCoordRook(); // D1/D8 для длинной рокировки
        }

        if (!targetCoordinate.equals(kingTargetCoord)) {
            resetSelection();
            return;
        }

        // Сохраняем исходные координаты
        Coordinate kingOriginalCoord = king.getCoords();
        Coordinate rookOriginalCoord = rook.getCoords();

        // Удаляем фигуры с исходных позиций
        pieces.getPieces().remove(kingOriginalCoord);
        pieces.getPieces().remove(rookOriginalCoord);

        // Обновляем координаты фигур
        king.setCoords(kingTargetCoord);
        rook.setCoords(rookTargetCoord);

        // Размещаем фигуры на новых позициях
        pieces.addPiece(kingTargetCoord, king);
        pieces.addPiece(rookTargetCoord, rook);

        king.setHasMoved();
        rook.setHasMoved();

        // Обновляем потенциальные ходы
        pieces.updatePotentials();
        boardPanel.updateBoard(pieces);

        // Создаем специальную координату для рокировки
        Coordinate castlingNotationCoord = isKingside
                ? new Coordinate('O', 0)  // O-O
                : new Coordinate('Q', 0); // O-O-O (Q для Queen's side)

        infoPanel.recordMove(castlingNotationCoord, king, pieces, currentTurn);

        switchTurn();
        checkGameState();
        resetSelection();
    }
    /**
     * Выбирает фигуру для последующего хода.
     * Подсвечивает возможные ходы для выбранной фигуры на доске.
     *
     * @param piece фигура, выбранная игроком для хода
     */
    private void selectPiece(Piece piece) {
        this.selectedPiece = piece;
        this.clickCounter = 1;

        // Получаем все возможные ходы
        var possibleMoves = piece.getPotentialMoves();

        // Если это король, добавляем координаты рокировки в возможные ходы
        if (piece.getName() == ID.KING) {
            King king = (King) piece;

            // Проверяем рокировку на королевский фланг
            if (king.canCastleKing(pieces)) {
                possibleMoves.add(king.getCastleCoordKingK());
            }

            // Проверяем рокировку на ферзевый фланг
            if (king.canCastleQueen(pieces)) {
                possibleMoves.add(king.getCastleCoordKingQ());
            }
        }

        boardPanel.highlightPossibleMoves(possibleMoves);
    }

    /**
     * Обрабатывает выполнение хода выбранной фигурой.
     * Проверяет специальные правила (продвижение пешки), выполняет ход
     * и обновляет состояние игры.
     *
     * @param targetCoordinate целевая координата для перемещения фигуры
     * @see PawnPromotionHandler
     * @see Pieces#makeMove(Coordinate, Piece)
     */
    private void handleMove(Coordinate targetCoordinate) {
        // Проверяем превращение пешки ДО выполнения хода
        if (selectedPiece.getName() == ID.PAWN) {
            Pawn pawn = (Pawn) selectedPiece;

            // Проверяем, является ли это ходом на две клетки
            Coordinate currentCoord = selectedPiece.getCoords();
            int distance = Math.abs(targetCoordinate.getRank() - currentCoord.getRank());

            if (distance == 2) {
                pawn.setHasMovedTwo(); // Устанавливаем флаг хода на две клетки
            }


            if (pawn.canPromoteBlack(targetCoordinate) || pawn.canPromoteWhite(targetCoordinate)) {

                PawnPromotionHandler.handlePawnPromotion(pawn, targetCoordinate);
            }


            if (!pawn.getHasMoved()) {
                pawn.setHasMovedTwo();
            }
        }

        // Выполняем ход
        pieces.makeMove(targetCoordinate, selectedPiece);

        // Обновляем флаг перемещения для фигуры (кроме пешек, у которых своя логика)
        if (selectedPiece.getName() != ID.PAWN) {
            selectedPiece.setHasMoved();
        }

        // Обновляем UI
        boardPanel.updateBoard(pieces);
        infoPanel.recordMove(targetCoordinate, selectedPiece, pieces, currentTurn);

        // Меняем ход и проверяем состояние игры
        switchTurn();
        checkGameState();

        resetSelection();
    }


    /**
     * Сбрасывает состояние выбора фигуры.
     * Убирает подсветку возможных ходов и обнуляет счетчик кликов.
     */
    private void resetSelection() {
        this.selectedPiece = null;
        this.clickCounter = 0;
        boardPanel.resetBoardColors();
    }

    /**
     * Переключает очередность хода между игроками.
     * После выполнения хода текущим игроком передает ход сопернику.
     *
     * @see COLOUR#not(COLOUR)
     */
    private void switchTurn() {
        currentTurn = COLOUR.not(currentTurn);
    }

    /**
     * Проверяет текущее состояние игры на наличие завершающих условий.
     * Определяет мат, пат и ничью, соответствующим образом завершая игру.
     *
     * @see Pieces#isMate(COLOUR)
     * @see Pieces#isStalemate(COLOUR)
     * @see Pieces#isDraw()
     */
    private void checkGameState() {
        if (pieces.isMate(currentTurn)) {
            infoPanel.setGameResult(COLOUR.not(currentTurn).toString() + " выиграли, поставив мат.");
            endGame();
        } else if (pieces.isStalemate(COLOUR.not(currentTurn))) {
            infoPanel.setGameResult("Игра в ничью закончена.");
            endGame();
        } else if (pieces.isDraw()) {
            infoPanel.setGameResult("Ничья.");
            endGame();
        }
    }

    /**
     * Завершает игру, отключая возможность дальнейших ходов.
     * Устанавливает флаг завершения игры и блокирует взаимодействие с доской.
     */
    private void endGame() {
        gameActive = false;
        boardPanel.disableBoard();
    }

    /**
     * Инициирует процесс сохранения текущей игры.
     * Вызывает обработчик сохранения игры с историей ходов.
     *
     * @see GameSaveHandler#handleSaveGame(String)
     * @see InfoPanel#getMoveHistory()
     */
    public void saveGame() {
        GameSaveHandler.handleSaveGame(infoPanel.getMoveHistory());
    }
}