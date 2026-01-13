package org.example;

/**
 * Перечисление типов шахматных фигур
 */
public enum ID {
    /** Король */
    KING {
        @Override
        public String toString() {
            return "K";
        }
        public String toFullString() {
            return "King";
        }
    },
    /** Ферзь */
    QUEEN {
        @Override
        public String toString() {
            return "Q";
        }
        public String toFullString() {
            return "Queen";
        }
    },
    /** Ладья */
    ROOK {
        @Override
        public String toString() {
            return "R";
        }
        public String toFullString() {
            return "Rook";
        }
    },
    /** Слон */
    BISHOP {
        @Override
        public String toString() {
            return "B";
        }
        public String toFullString() {
            return "Bishop";
        }
    },
    /** Конь */
    KNIGHT {
        @Override
        public String toString() {
            return "N";
        }
        public String toFullString() {
            return "Knight";
        }
    },
    /** Пешка */
    PAWN {
        @Override
        public String toString() {
            return "";
        }
        public String toFullString() {
            return "Pawn";
        }
    };

    /**
     * Возвращает полное строковое представление типа фигуры
     * @return полное имя типа фигуры
     */
    public abstract String toFullString();
}