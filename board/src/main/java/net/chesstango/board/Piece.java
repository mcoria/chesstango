package net.chesstango.board;

import lombok.Getter;

/**
 * @author Mauricio Coria
 */
@Getter
public enum Piece {
    PAWN_WHITE(Color.WHITE, 1),
    PAWN_BLACK(Color.BLACK, 2),

    KNIGHT_WHITE(Color.WHITE, 3),
    KNIGHT_BLACK(Color.BLACK, 4),

    BISHOP_WHITE(Color.WHITE, 5),
    BISHOP_BLACK(Color.BLACK, 6),

    ROOK_WHITE(Color.WHITE, 7),
    ROOK_BLACK(Color.BLACK, 8),

    QUEEN_WHITE(Color.WHITE, 9),
    QUEEN_BLACK(Color.BLACK, 10),

    KING_WHITE(Color.WHITE, 11),
    KING_BLACK(Color.BLACK, 12);

    private final Color color;
    private final short binaryEncodedTo;
    private final short binaryEncodedFrom;

    Piece(Color color, int binaryEncodedFrom) {
        this.color = color;
        this.binaryEncodedFrom = (short) (binaryEncodedFrom << 8);
        this.binaryEncodedTo = (short) binaryEncodedFrom;
    }

    public boolean isPawn(){
        return PAWN_WHITE.equals(this) || PAWN_BLACK.equals(this);
    }

    public boolean isKing() {
        return KING_WHITE.equals(this) || KING_BLACK.equals(this);
    }

    public static Piece getKing(Color color) {
        return switch (color) {
            case WHITE -> KING_WHITE;
            case BLACK -> KING_BLACK;
            default -> throw new RuntimeException("Invalid color");
        };
    }

    public static Piece getQueen(Color color) {
        return switch (color) {
            case WHITE -> QUEEN_WHITE;
            case BLACK -> QUEEN_BLACK;
            default -> throw new RuntimeException("Invalid color");
        };
    }

    public static Piece getBishop(Color color) {
        return switch (color) {
            case WHITE -> BISHOP_WHITE;
            case BLACK -> BISHOP_BLACK;
            default -> throw new RuntimeException("Invalid color");
        };
    }

    public static Piece getRook(Color color) {
        return switch (color) {
            case WHITE -> ROOK_WHITE;
            case BLACK -> ROOK_BLACK;
            default -> throw new RuntimeException("Invalid color");
        };
    }

    public static Piece getKnight(Color color) {
        return switch (color) {
            case WHITE -> KNIGHT_WHITE;
            case BLACK -> KNIGHT_BLACK;
            default -> throw new RuntimeException("Invalid color");
        };
    }

    public static Piece getPawn(Color color) {
        return switch (color) {
            case WHITE -> PAWN_WHITE;
            case BLACK -> PAWN_BLACK;
            default -> throw new RuntimeException("Invalid color");
        };
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
