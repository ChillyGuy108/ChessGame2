package org.example;



import org.example.GameController;
import org.example.BoardPanel;
import org.example.InfoPanel;
import org.example.Pieces;

import javax.swing.*;
import java.awt.*;


/**
 * Главное окно шахматного приложения "Великие шахматы".
 * Служит корневым контейнером для всех компонентов пользовательского интерфейса.
 * Инициализирует и связывает между собой панель доски, панель информации
 * и игровой контроллер, обеспечивая их корректное взаимодействие.
 * Отвечает за начальную настройку и компоновку основного интерфейса приложения.
 *
 * @see BoardPanel
 * @see InfoPanel
 * @see GameController
 * @see Pieces
 */
public class MainFrame extends JFrame {
    /**
     * Конструктор главного окна приложения.
     * Создает и инициализирует все компоненты интерфейса, устанавливает связи
     * между ними и настраивает основные свойства окна.
     *
     * @param pieces объект, содержащий начальное состояние шахматных фигур и игровую логику
     */
    public MainFrame(Pieces pieces) {
        setTitle("Шахматы");
        setBackground(Color.black);

        InfoPanel infoPanel = new InfoPanel();
        BoardPanel boardPanel = new BoardPanel(pieces);
        GameController gameController = new GameController(pieces, boardPanel, infoPanel);

        boardPanel.setGameController(gameController);
        infoPanel.setGameController(gameController);

        Container contents = getContentPane();
        contents.setLayout(new BorderLayout());
        contents.add(boardPanel, BorderLayout.WEST);
        contents.add(infoPanel, BorderLayout.EAST);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Точка входа в шахматное приложение.
     * Создает начальное состояние игровых фигур и запускает главное окно приложения.
     * Инициализирует игровой движок в режиме графического интерфейса.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        Pieces pieces = new Pieces();
        pieces.setGUIGame(true);
        new MainFrame(pieces);
    }
}
