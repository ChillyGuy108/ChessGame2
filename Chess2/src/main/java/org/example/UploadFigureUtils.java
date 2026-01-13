package org.example;



import org.example.COLOUR;
import org.example.ID;
import org.example.Piece;

import javax.swing.ImageIcon;
import java.util.HashMap;
import java.util.Map;

/**
 * Утилита для загрузки иконок фигур
 */
public class UploadFigureUtils {

    private static final String RESOURCES_PATH = "/images/";
    private static final Map<String, ImageIcon> iconCache = new HashMap<>();

    /**
     * Получает иконку для фигуры
     * @param piece фигура
     * @return ImageIcon иконка фигуры
     */
    public static ImageIcon getIcon(Piece piece) {
        if (piece == null) {
            return null;
        }

        String iconKey = getIconKey(piece);

        if (iconCache.containsKey(iconKey)) {
            return iconCache.get(iconKey);
        }

        String iconPath = getIconPath(piece);
        ImageIcon icon = loadIcon(iconPath);

        if (icon != null) {
            iconCache.put(iconKey, icon);
        }

        return icon;
    }

    /**
     * Получает путь к иконке для фигуры
     * @param piece фигура
     * @return путь к иконке
     */
    private static String getIconPath(Piece piece) {
        ID pieceId = piece.getName();
        COLOUR colour = piece.getColour();

        String colourPrefix = (colour == COLOUR.B) ? "B" : "W";
        String pieceName = getPieceName(pieceId);

        return RESOURCES_PATH + colourPrefix + pieceName + ".png";
    }

    /**
     * Получает имя файла иконки для типа фигуры
     * @param pieceId тип фигуры
     * @return имя файла
     */
    private static String getPieceName(ID pieceId) {
        switch (pieceId) {
            case KING:
                return "King";
            case QUEEN:
                return "Queen";
            case ROOK:
                return "Rook";
            case BISHOP:
                return "Bishop";
            case KNIGHT:
                return "Knight";
            case PAWN:
                return "Pawn";
            default:
                return "Unknown";
        }
    }

    /**
     * Загружает иконку из ресурсов
     * @param path путь к ресурсу
     * @return ImageIcon или null если не найдено
     */
    private static ImageIcon loadIcon(String path) {
        try {
            java.net.URL imageURL = UploadFigureUtils.class.getResource(path);
            if (imageURL != null) {
                return new ImageIcon(imageURL);
            } else {
                System.err.println("Не удалось найти иконку: " + path);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке иконки: " + path + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * Генерирует ключ для кэша иконок
     * @param piece фигура
     * @return ключ кэша
     */
    private static String getIconKey(Piece piece) {
        // Используем name() вместо toString(), так как toString() возвращает пустую строку
        // для PAWN, VIZAR, WARCAR, GIRAFFE, что приводит к конфликтам в кэше
        return piece.getColour().name() + "_" + piece.getName().name();
    }

    /**
     * Очищает кэш иконок
     */
    public static void clearCache() {
        iconCache.clear();
    }
}