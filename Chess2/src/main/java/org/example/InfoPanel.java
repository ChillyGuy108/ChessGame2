package org.example;



import org.example.COLOUR;
import  org.example.GameController;
import  org.example.MoveHistoryManager;
import  org.example.Coordinate;
import  org.example.Pieces;
import  org.example.Piece;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Панель информации для отображения истории ходов и состояния шахматной игры.
 * Предоставляет интерфейс для просмотра ходов, отображения результатов игры
 * и управления сохранением игровой сессии.
 */
public class InfoPanel extends JPanel {
    private final JTextPane movePane = new JTextPane();
    private final JTextPane matePane = new JTextPane();
    private final JButton saveButton = new JButton("Сохранить игру");

    private final Color infoColour = new Color(51,51,51);
    private final MoveHistoryManager moveHistoryManager;

    private GameController gameController;

    /**
     * Конструктор панели информации.
     * Инициализирует менеджер истории ходов и создает пользовательский интерфейс.
     */
    public InfoPanel() {
        this.moveHistoryManager = new MoveHistoryManager();
        initializePanel();
    }

    /**
     * Устанавливает ссылку на игровой контроллер для координации действий.
     *
     * @param gameController игровой контроллер для обработки событий сохранения игры
     */
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Инициализирует пользовательский интерфейс панели информации.
     * Создает и настраивает все компоненты: область отображения ходов,
     * кнопку сохранения и область отображения результатов игры.
     */
    private void initializePanel() {
        setLayout(new GridBagLayout());
        setBackground(infoColour);
        setPreferredSize(new Dimension(300, 800));

        GridBagConstraints gbc = new GridBagConstraints();

        // Настройка movePane
        movePane.setEditable(false);
        movePane.setForeground(Color.white);
        movePane.setBackground(infoColour);
        movePane.setFont(new Font("Arial", Font.BOLD, 14));
        movePane.setBorder(new EmptyBorder(40, 20, 40, 20));
        JScrollPane scrollMoves = new JScrollPane(movePane);

        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.4;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollMoves, gbc);

        // Настройка кнопки сохранения
        saveButton.setBackground(new Color(128,128,128));
        saveButton.setForeground(Color.white);
        saveButton.setOpaque(true);
        saveButton.setContentAreaFilled(true);
        saveButton.setBorderPainted(false);
        saveButton.addActionListener(e -> {
            if (gameController != null) {
                gameController.saveGame();
            }
        });

        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.NONE;
        add(saveButton, gbc);


        matePane.setEditable(false);
        matePane.setForeground(Color.white);
        matePane.setBackground(infoColour);
        matePane.setFont(new Font("Arial", Font.BOLD, 20));
        matePane.setBorder(new EmptyBorder(0, 80, 40, 80));

        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(matePane, gbc);
    }

    /**
     * Записывает выполненный ход в историю и обновляет отображение.
     * Форматирует ход в соответствии с шахматной нотацией и добавляет его
     * в историю с учетом номера хода для белых фигур.
     *
     * @param coordinate координата, на которую переместилась фигура
     * @param piece фигура, сделавшая ход
     * @param pieces объект, содержащий текущее состояние шахматных фигур
     * @param turn цвет игрока, сделавшего ход
     */
    public void recordMove(Coordinate coordinate, Piece piece, Pieces pieces, COLOUR turn) {
        moveHistoryManager.recordMove(coordinate, piece, pieces, turn);
        movePane.setText(moveHistoryManager.getFullMoveHistory());
    }




    /**
     * Отображает результат игры в специальной области панели.
     * Используется для показа мата, пата или ничьей по завершении партии.
     *
     * @param result текстовое описание результата игры
     */
    public void setGameResult(String result) {
        matePane.setText(result);
    }

    /**
     * Возвращает полную историю ходов текущей игры в текстовом формате.
     *
     * @return строка, содержащая все ходы игры в формате шахматной нотации
     */
    public String getMoveHistory() {
        return moveHistoryManager.getFullMoveHistory();
    }
}
