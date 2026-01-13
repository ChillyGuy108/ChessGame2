package org.example;



import org.example.COLOUR;
import org.example.Coordinate;
import org.example.ChessIO;
import org.example.King;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Paths;

/**
 * Обработчик сохранения шахматной игры в файл.
 * Предоставляет функциональность для сохранения истории ходов в текстовый файл
 * с использованием диалоговых окон для взаимодействия с пользователем.
 * Включает проверку корректности имени файла и обработку ошибок сохранения.
 */
public class GameSaveHandler {
    private static final Color INFO_COLOR = new Color(51,51,51);

    /**
     * Обрабатывает процесс сохранения игры в файл.
     * Отображает диалоговое окно для ввода имени файла, проверяет его корректность
     * и сохраняет историю ходов. В случае успеха или ошибки показывает соответствующие
     * сообщения пользователю.
     *
     * @param moveHistory строка, содержащая историю ходов для сохранения в формате шахматной нотации
     */
    public static void handleSaveGame(String moveHistory) {
        ImageIcon icon = UploadFigureUtils.getIcon(
                new King(COLOUR.W, new Coordinate('f', 1)));

        UIManager.put("OptionPane.background", INFO_COLOR);
        UIManager.put("Panel.background", INFO_COLOR);
        UIManager.put("OptionPane.messageForeground", Color.white);

        String fileSave = (String) JOptionPane.showInputDialog(null,
                "Введите имя файла, чтобы сохранить игру:",
                "Сохранить игру",
                JOptionPane.INFORMATION_MESSAGE,
                icon,null,null);

        if (fileSave != null) {
            String filePath = ChessIO.toTxt(fileSave);

            if (ChessIO.isErrorSave(filePath)) {
                JOptionPane.showMessageDialog(null,
                        fileSave + " не подходит для файла.",
                        "Ошибка сохранения.",
                        JOptionPane.ERROR_MESSAGE,
                        icon);
            } else {
                if (ChessIO.saveGame(moveHistory, Paths.get(filePath)))
                    JOptionPane.showMessageDialog(null,
                            "Игра была сохранена на файл " + filePath,
                            "Успешное сохранение.",
                            JOptionPane.INFORMATION_MESSAGE,
                            icon);
                else
                    JOptionPane.showMessageDialog(null,
                            "Произошла ошибка при сохранении игры на файл" + filePath + ". Убедитесь, что файл не существует.",
                            "Ошибка сохранения.",
                            JOptionPane.ERROR_MESSAGE,
                            icon);
            }
        }
    }
}