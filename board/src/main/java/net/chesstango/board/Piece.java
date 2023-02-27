package net.chesstango.board;

/**
 * @author Mauricio Coria
 */
public enum Piece {
    PAWN_WHITE(Color.WHITE),
    PAWN_BLACK(Color.BLACK),

    KNIGHT_WHITE(Color.WHITE),
    KNIGHT_BLACK(Color.BLACK),

    BISHOP_WHITE(Color.WHITE),
    BISHOP_BLACK(Color.BLACK),

    ROOK_WHITE(Color.WHITE),
    ROOK_BLACK(Color.BLACK),

    QUEEN_WHITE(Color.WHITE),
    QUEEN_BLACK(Color.BLACK),

    KING_WHITE(Color.WHITE),
    KING_BLACK(Color.BLACK);

    private final Color color;

    Piece(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public static Piece getKing(Color color) {
        switch (color) {
            case WHITE:
                return KING_WHITE;
            case BLACK:
                return KING_BLACK;
            default:
                throw new RuntimeException("Invalid color");
        }
    }

    public static Piece getQueen(Color color) {
        switch (color) {
            case WHITE:
                return QUEEN_WHITE;
            case BLACK:
                return QUEEN_BLACK;
            default:
                throw new RuntimeException("Invalid color");
        }
    }

    public static Piece getBishop(Color color) {
        switch (color) {
            case WHITE:
                return BISHOP_WHITE;
            case BLACK:
                return BISHOP_BLACK;
            default:
                throw new RuntimeException("Invalid color");
        }
    }

    public static Piece getRook(Color color) {
        switch (color) {
            case WHITE:
                return ROOK_WHITE;
            case BLACK:
                return ROOK_BLACK;
            default:
                throw new RuntimeException("Invalid color");
        }
    }

    public static Piece getKnight(Color color) {
        switch (color) {
            case WHITE:
                return KNIGHT_WHITE;
            case BLACK:
                return KNIGHT_BLACK;
            default:
                throw new RuntimeException("Invalid color");
        }
    }

    public static Piece getPawn(Color color) {
        switch (color) {
            case WHITE:
                return PAWN_WHITE;
            case BLACK:
                return PAWN_BLACK;
            default:
                throw new RuntimeException("Invalid color");
        }
    }

    public Piece getOpposite() {
        return switch (this) {
            case PAWN_WHITE -> PAWN_BLACK;
            case PAWN_BLACK -> PAWN_WHITE;
            case KNIGHT_WHITE -> KNIGHT_BLACK;
            case KNIGHT_BLACK -> KNIGHT_WHITE;
            case BISHOP_WHITE -> BISHOP_BLACK;
            case BISHOP_BLACK -> BISHOP_WHITE;
            case ROOK_WHITE -> ROOK_BLACK;
            case ROOK_BLACK -> ROOK_WHITE;
            case QUEEN_WHITE -> QUEEN_BLACK;
            case QUEEN_BLACK -> QUEEN_WHITE;
            case KING_WHITE -> KING_BLACK;
            case KING_BLACK -> KING_WHITE;
        };
    }

}
