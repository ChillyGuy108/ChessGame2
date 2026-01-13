package org.example;

import org.example.BOARD;
import org.example.GameController;
import org.example.Coordinate;
import org.example.Pieces;
import org.example.Piece;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.HashSet;

/**
 * Панель для отображения и взаимодействия с шахматной доской.
 * Отвечает за визуальное представление шахматной доски, фигур и возможных ходов.
 * Управляет созданием клеток доски, отображением фигур и обработкой пользовательского ввода.
 */
public class BoardPanel extends JPanel {
    private final int dimension = 8; // Изменено с BOARD.LAST_RANK.getRankVal() на 8
    private final int firstRank = 1; // Изменено с BOARD.FIRST_RANK.getRankVal() на 1
    private final char firstFile = 'a'; // Изменено с BOARD.FIRST_FILE.getFileVal() на 'a'
    private final char lastFile = 'h'; // Изменено с BOARD.LAST_FILE.getFileVal() на 'h'
    private final char charFile = (char) (firstFile - 1); // Будет '`' (символ перед 'a')

    private final JButton[][] board = new JButton[dimension][dimension];
    private final Color brown = new Color(150, 75, 0);
    private final Color pastel = new Color(255, 222, 173);
    private final Color intermediate = new Color(255, 255, 153);
    private static final int tileSize = 88;

    private final BufferedImage invisible = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
    private final ImageIcon invisibleIcon = new ImageIcon(invisible);

    private GameController gameController;
    private boolean isBoardEnabled = true;

    /**
     * Конструктор панели шахматной доски.
     * Инициализирует доску с заданным начальным состоянием фигур.
     *
     * @param pieces объект, содержащий начальное состояние шахматных фигур
     */
    public BoardPanel(Pieces pieces) {
        setLayout(new BorderLayout());
        initializeBoard(pieces);
    }

    /**
     * Устанавливает ссылку на игровой контроллер для обработки событий.
     *
     * @param gameController игровой контроллер для обработки кликов по доске
     */
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Инициализирует шахматную доску, создавая все необходимые компоненты.
     * Создает клетки доски, добавляет метки файлов и рангов, настраивает компоновку.
     *
     * @param pieces объект, содержащий начальное состояние шахматных фигур
     */
    private void initializeBoard(Pieces pieces) {
        JPanel boardPanel = new JPanel(new GridLayout(dimension, dimension));

        for (int rank = dimension; rank >= firstRank; rank--) {
            for (int file = 1; file <= dimension; file++) {
                char fileChar = (char) (charFile + file);
                Coordinate tileCoord = new Coordinate(fileChar, rank);
                board[rank - 1][file - 1] = createBoardButton(tileCoord, pieces);
                board[rank - 1][file - 1].addActionListener(new BoardButtonListener());
                boardPanel.add(board[rank - 1][file - 1]);
            }
        }

        add(createFileLabelsTop(), BorderLayout.NORTH);
        add(createRankLabelsLeft(), BorderLayout.WEST);
        add(boardPanel, BorderLayout.CENTER);
        add(createRankLabelsRight(), BorderLayout.EAST);
        add(createFileLabelsBottom(), BorderLayout.SOUTH);
    }

    /**
     * Создает кнопку для клетки шахматной доски.
     * Настраивает внешний вид кнопки, устанавливает иконку фигуры если необходимо.
     *
     * @param coordinate координата клетки на доске
     * @param pieces объект, содержащий состояние шахматных фигур
     * @return настроенная кнопка для клетки доски
     */
    private JButton createBoardButton(Coordinate coordinate, Pieces pieces) {
        JButton button = new JButton();
        backgroundSetter(coordinate, button);

        Piece piece = pieces.getPieces().get(coordinate);
        if (piece != null) {
            ImageIcon icon = UploadFigureUtils.getIcon(piece);
            button.setIcon(icon);
            // Сохраняем оригинальную иконку для использования при отключении
            button.setDisabledIcon(icon);
        } else {
            button.setIcon(invisibleIcon);
            button.setDisabledIcon(invisibleIcon);
        }

        formatButton(button);
        return button;
    }

    /**
     * Устанавливает цвет фона для кнопки клетки в зависимости от ее позиции.
     * Чередует цвета для создания шахматного узора.
     *
     * @param coordinate координата клетки на доске
     * @param button кнопка, для которой устанавливается фон
     */
    private void backgroundSetter(Coordinate coordinate, JButton button) {
        int signature = coordinate.getFile() - charFile + coordinate.getRank();
        if (signature % 2 == 0) {
            button.setBackground(brown);
        } else {
            button.setBackground(pastel);
        }
    }

    /**
     * Форматирует внешний вид кнопки клетки доски.
     * Устанавливает размер, прозрачность и другие визуальные свойства.
     *
     * @param button кнопка для форматирования
     */
    private void formatButton(JButton button) {
        button.setSize(tileSize, tileSize);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setVisible(true);
    }

    /**
     * Подсвечивает возможные ходы для выбранной фигуры.
     * Изменяет цвет фона клеток, на которые можно переместить фигуру.
     *
     * @param potentials множество координат, куда фигура может переместиться
     */
    public void highlightPossibleMoves(HashSet<Coordinate> potentials) {
        if (!isBoardEnabled) return;

        for (int rank = 1; rank <= dimension; rank++) {
            for (char file = firstFile; file <= lastFile; file++) {
                int processedRank = rank - firstRank;
                int processedFile = file - firstFile;
                Coordinate potentialCoord = new Coordinate(file, rank);
                if (potentials.contains(potentialCoord))
                    board[processedRank][processedFile].setBackground(intermediate);
                else
                    backgroundSetter(potentialCoord, board[processedRank][processedFile]);
            }
        }
    }

    /**
     * Сбрасывает цвета доски к исходному состоянию.
     * Убирает подсветку возможных ходов, восстанавливая стандартную раскраску.
     */
    public void resetBoardColors() {
        for (int rank = 1; rank <= dimension; rank++) {
            for (char file = firstFile; file <= lastFile; file++) {
                int processedRank = rank - firstRank;
                int processedFile = file - firstFile;
                Coordinate potentialCoord = new Coordinate(file, rank);
                backgroundSetter(potentialCoord, board[processedRank][processedFile]);
            }
        }
    }

    /**
     * Обновляет отображение доски в соответствии с текущим состоянием фигур.
     * Перерисовывает фигуры на их текущих позициях после выполнения хода.
     *
     * @param pieces объект, содержащий текущее состояние шахматных фигур
     */
    public void updateBoard(Pieces pieces) {
        for (int rank = 1; rank <= dimension; rank++) {
            for (char file = firstFile; file <= lastFile; file++) {
                int processedRank = rank - firstRank;
                int processedFile = file - firstFile;
                Coordinate potentialCoord = new Coordinate(file, rank);
                backgroundSetter(potentialCoord, board[processedRank][processedFile]);

                Piece updatePiece = pieces.getPieces().get(potentialCoord);
                if (updatePiece != null) {
                    ImageIcon icon = UploadFigureUtils.getIcon(updatePiece);
                    board[processedRank][processedFile].setIcon(icon);
                    // Обновляем иконку для отключенного состояния
                    board[processedRank][processedFile].setDisabledIcon(icon);
                } else {
                    board[processedRank][processedFile].setIcon(invisibleIcon);
                    board[processedRank][processedFile].setDisabledIcon(invisibleIcon);
                }
            }
        }
    }

    /**
     * Отключает взаимодействие с доской после окончания игры.
     * Удаляет обработчики событий с кнопок, предотвращая дальнейшие ходы.
     * Сохраняет визуальное представление иконок без потускнения.
     */
    public void disableBoard() {
        isBoardEnabled = false;
        for (int row = 0; row < dimension; row++) {
            for (int file = 0; file < dimension; file++) {
                // Вместо setEnabled(false) просто удаляем обработчики событий
                // Это предотвратит потускнение иконок
                ActionListener[] listeners = board[row][file].getActionListeners();
                for (ActionListener listener : listeners) {
                    board[row][file].removeActionListener(listener);
                }
            }
        }
    }

    /**
     * Создает панель с метками рангов (горизонтальных номеров) для левой стороны доски.
     *
     * @return панель с метками рангов
     */
    private JPanel createRanks() {
        JPanel ranks = new JPanel(new GridLayout(dimension, 0));
        ranks.setBackground(pastel);
        int rankPad = 4;
        for (int rank = dimension; rank >= firstRank; rank--) {
            JLabel rankLabel = new JLabel(String.valueOf(rank));
            rankLabel.setFont(new Font("TimesRoman", Font.BOLD, 23));
            rankLabel.setForeground(brown);
            rankLabel.setBorder(new EmptyBorder(0, rankPad, 0, rankPad));
            ranks.add(rankLabel);
        }
        return ranks;
    }

    /**
     * Создает панель с метками файлов (вертикальных букв) для верхней/нижней стороны доски.
     *
     * @return панель с метками файлов
     */
    private JPanel createFiles() {
        JPanel files = new JPanel(new GridLayout(0, dimension));
        files.setBackground(pastel);
        int filePad = 42;
        for (char file = firstFile; file <= lastFile; file++) {
            JLabel fileLabel = new JLabel(String.valueOf(file));
            fileLabel.setFont(new Font("TimesRoman", Font.BOLD, 23));
            fileLabel.setForeground(brown);
            fileLabel.setBorder(new EmptyBorder(0, filePad, 0, filePad));
            files.add(fileLabel);
        }
        return files;
    }

    /**
     * Создает панель-границу для оформления краев доски.
     *
     * @return панель границы
     */
    private JPanel createBorder() {
        JPanel border = new JPanel();
        border.setBackground(brown);
        return border;
    }

    /**
     * Создает угловую границу для оформления меток файлов и рангов.
     *
     * @return объект границы
     */
    private Border createCorner() {
        int borderPad = 20;
        return new MatteBorder(0, borderPad, 0, borderPad, pastel);
    }

    /**
     * Создает панель с метками рангов для левой стороны доски с границей.
     *
     * @return панель с метками рангов и границей
     */
    private JPanel createRankLabelsLeft() {
        JPanel rankLabels = new JPanel(new BorderLayout());
        rankLabels.add(createRanks(), BorderLayout.WEST);
        rankLabels.add(createBorder(), BorderLayout.EAST);
        return rankLabels;
    }

    /**
     * Создает панель с метками файлов для нижней стороны доски с границей.
     *
     * @return панель с метками файлов и границей
     */
    private JPanel createFileLabelsBottom() {
        JPanel fileLabels = new JPanel(new BorderLayout());
        fileLabels.add(createFiles(), BorderLayout.SOUTH);
        fileLabels.add(createBorder(), BorderLayout.NORTH);
        fileLabels.setBorder(createCorner());
        return fileLabels;
    }

    /**
     * Создает панель с метками рангов для правой стороны доски с границей.
     *
     * @return панель с метками рангов и границей
     */
    private JPanel createRankLabelsRight() {
        JPanel rankLabels = new JPanel(new BorderLayout());
        rankLabels.add(createRanks(), BorderLayout.EAST);
        rankLabels.add(createBorder(), BorderLayout.WEST);
        return rankLabels;
    }

    /**
     * Создает панель с метками файлов для верхней стороны доски с границей.
     *
     * @return панель с метками файлов и границей
     */
    private JPanel createFileLabelsTop() {
        JPanel fileLabels = new JPanel(new BorderLayout());
        fileLabels.add(createFiles(), BorderLayout.NORTH);
        fileLabels.add(createBorder(), BorderLayout.SOUTH);
        fileLabels.setBorder(createCorner());
        return fileLabels;
    }

    /**
     * Внутренний класс-обработчик событий для кнопок шахматной доски.
     * Преобразует координаты кнопки в шахматные координаты и передает их контроллеру.
     */
    private class BoardButtonListener implements java.awt.event.ActionListener {
        /**
         * Преобразует индексы массива кнопок в шахматные координаты.
         *
         * @param row индекс строки в массиве кнопок
         * @param column индекс столбца в массиве кнопок
         * @return объект Coordinate с шахматными координатами
         */
        private Coordinate toCoordinate(int row, int column) {
            int rank = row + firstRank;
            char file = (char) (column + firstFile);
            return new Coordinate(file, rank);
        }

        /**
         * Обрабатывает событие нажатия на кнопку клетки доски.
         * Определяет координату клетки и передает ее игровому контроллеру.
         *
         * @param actionEvent событие нажатия на кнопку
         */
        @Override
        public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
            if (!isBoardEnabled || gameController == null) return;

            Coordinate coordinate = null;
            JButton source = (JButton) actionEvent.getSource();

            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    if (board[i][j].equals(source)) {
                        coordinate = toCoordinate(i, j);
                    }
                }
            }

            if (coordinate != null && Coordinate.inBoard(coordinate)) {
                gameController.handleTileClick(coordinate);
            }
        }
    }
}