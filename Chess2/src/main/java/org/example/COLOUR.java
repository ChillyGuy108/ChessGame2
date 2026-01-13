package org.example;


/**
 * Перечисление цветов фигур в шахматах
 */
public enum COLOUR {
    /** Черный цвет */
    B {
        @Override
        public String toString() {
            return "Чёрные";
        }

        @Override
        public String toSmallString() {
            return "b";
        }
    },

    /** Белый цвет */
    W {
        @Override
        public String toString() {
            return "Белые";
        }

        @Override
        public String toSmallString() {
            return "w";
        }
    };

    /**
     * Возвращает сокращенное строковое представление цвета
     * @return строка с сокращенным обозначением цвета
     */
    public abstract String toSmallString();

    /**
     * Возвращает противоположный цвет
     * @param colour текущий цвет
     * @return противоположный цвет (B -> W, W -> B)
     */
    public static COLOUR not(COLOUR colour) {
        if (colour == COLOUR.B)
            return COLOUR.W;
        else
            return COLOUR.B;

    }

}
