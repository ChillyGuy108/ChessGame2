package org.example;



import org.example.Coordinate;
import org.example.Pawn;
import org.example.Queen;

import javax.swing.*;
import java.awt.*;

/**
 * Обработчик продвижения пешки в ферзи при достижении последней горизонтали.
 * Предоставляет функциональность для визуального оповещения пользователя о продвижении пешки
 * и автоматического преобразования пешки в ферзя. Использует диалоговые окна для интерактивного
 * взаимодействия с пользователем.
 */
public class PawnPromotionHandler {
    private static final Color INFO_COLOR = new Color(51,51,51);

    /**
     * Обрабатывает процесс продвижения пешки в ферзи.
     * Отображает информационное диалоговое окно с иконкой ферзя соответствующего цвета
     * и устанавливает пешке новую фигуру - ферзя. Используется когда пешка достигает
     * последней горизонтали доски.
     *
     * @param pawn пешка, достигшая последней горизонтали и подлежащая продвижению
     * @param promotionSquare координата поля, на котором произошло продвижение пешки
     */
    public static void handlePawnPromotion(Pawn pawn, Coordinate promotionSquare) {
        ImageIcon icon = UploadFigureUtils.getIcon(new Queen(pawn.getColour(), promotionSquare));

        UIManager.put("OptionPane.background", INFO_COLOR);
        UIManager.put("Panel.background", INFO_COLOR);
        UIManager.put("OptionPane.messageForeground", Color.white);

        JOptionPane.showOptionDialog(
                null,
                "Ваша пешка стала ферзём.",
                "Повышение ранга",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                icon,
                null,
                null
        );

        pawn.setPromotedPiece(new Queen(pawn.getColour(), promotionSquare));
    }
}