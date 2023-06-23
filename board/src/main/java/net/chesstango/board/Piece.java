package net.chesstango.board;

/**
 * @author Mauricio Coria
 */
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

    public Color getColor() {
        return color;
    }

    public boolean isPawn(){
        return PAWN_WHITE.equals(this) || PAWN_BLACK.equals(this);
    }

    public boolean isKing() {
        return KING_WHITE.equals(this) || KING_BLACK.equals(this);
    }

    public short getBinaryEncodedTo() {
        return binaryEncodedTo;
    }

    public short getBinaryEncodedFrom() {
        return binaryEncodedFrom;
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
