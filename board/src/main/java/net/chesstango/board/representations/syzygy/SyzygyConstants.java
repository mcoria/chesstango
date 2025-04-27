package net.chesstango.board.representations.syzygy;

import lombok.Getter;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Mauricio Coria
 */
class SyzygyConstants {

    static final long PRIME_WHITE_QUEEN = Long.parseUnsignedLong("11811845319353239651");
    static final long PRIME_WHITE_ROOK = Long.parseUnsignedLong("10979190538029446137");
    static final long PRIME_WHITE_BISHOP = Long.parseUnsignedLong("12311744257139811149");
    static final long PRIME_WHITE_KNIGHT = Long.parseUnsignedLong("15202887380319082783");
    static final long PRIME_WHITE_PAWN = Long.parseUnsignedLong("17008651141875982339");
    static final long PRIME_BLACK_QUEEN = Long.parseUnsignedLong("15484752644942473553");
    static final long PRIME_BLACK_ROOK = Long.parseUnsignedLong("18264461213049635989");
    static final long PRIME_BLACK_BISHOP = Long.parseUnsignedLong("15394650811035483107");
    static final long PRIME_BLACK_KNIGHT = Long.parseUnsignedLong("13469005675588064321");
    static final long PRIME_BLACK_PAWN = Long.parseUnsignedLong("11695583624105689831");

    static final char[] piece_to_char = " PNBRQK  pnbrqk".toCharArray();

    static final int TB_LOSS = 0;               /* LOSS */
    static final int TB_BLESSED_LOSS = 1;       /* LOSS but 50-move draw */
    static final int TB_DRAW = 2;               /* DRAW */
    static final int TB_CURSED_WIN = 3;         /* WIN but 50-move draw  */
    static final int TB_WIN = 4;                /* WIN  */

    static final int TB_PROMOTES_NONE = 0;
    static final int TB_PROMOTES_QUEEN = 1;
    static final int TB_PROMOTES_ROOK = 2;
    static final int TB_PROMOTES_BISHOP = 3;
    static final int TB_PROMOTES_KNIGHT = 4;

    static final byte TB_PAWN = 1;
    static final byte TB_KNIGHT = 2;
    static final byte TB_BISHOP = 3;
    static final byte TB_ROOK = 4;
    static final byte TB_QUEEN = 5;
    static final byte TB_KING = 6;

    static final byte TB_WPAWN = TB_PAWN;
    static final byte TB_BPAWN = (TB_PAWN | 8);

    static final byte WHITE_KING = (TB_WPAWN + 5);
    static final byte WHITE_QUEEN = (TB_WPAWN + 4);
    static final byte WHITE_ROOK = (TB_WPAWN + 3);
    static final byte WHITE_BISHOP = (TB_WPAWN + 2);
    static final byte WHITE_KNIGHT = (TB_WPAWN + 1);
    static final byte WHITE_PAWN = TB_WPAWN;
    static final byte BLACK_KING = (TB_BPAWN + 5);
    static final byte BLACK_QUEEN = (TB_BPAWN + 4);
    static final byte BLACK_ROOK = (TB_BPAWN + 3);
    static final byte BLACK_BISHOP = (TB_BPAWN + 2);
    static final byte BLACK_KNIGHT = (TB_BPAWN + 1);
    static final byte BLACK_PAWN = TB_BPAWN;

    static final char TB_PIECES = 7;
    static final char TB_MAX_PIECE = (TB_PIECES < 7 ? 254 : 650);
    static final char TB_MAX_PAWN = (TB_PIECES < 7 ? 256 : 861);
    static final char TB_MAX_SYMS = 4096;
    static final char TB_HASHBITS = (TB_PIECES < 7 ? 11 : 12);

    static final int TB_MAX_MOVES = (192 + 1);
    static final int TB_MAX_CAPTURES = 64;

    static final int TB_RESULT_CHECKMATE = TB_SET_WDL(0, TB_WIN);
    static final int TB_RESULT_STALEMATE = TB_SET_WDL(0, TB_DRAW);
    static final int TB_RESULT_FAILED = 0xFFFFFFFF;


    static final int TB_RESULT_WDL_MASK = 0x0000000F;
    static final int TB_RESULT_TO_MASK = 0x000003F0;
    static final int TB_RESULT_FROM_MASK = 0x0000FC00;
    static final int TB_RESULT_PROMOTES_MASK = 0x00070000;
    static final int TB_RESULT_EP_MASK = 0x00080000;
    static final int TB_RESULT_DTZ_MASK = 0xFFF00000;
    static final int TB_RESULT_WDL_SHIFT = 0;
    static final int TB_RESULT_TO_SHIFT = 4;
    static final int TB_RESULT_FROM_SHIFT = 10;
    static final int TB_RESULT_PROMOTES_SHIFT = 16;
    static final int TB_RESULT_EP_SHIFT = 19;
    static final int TB_RESULT_DTZ_SHIFT = 20;

    @Getter
    enum PieceType {
        PAWN(1), KNIGHT(2), BISHOP(3), ROOK(4), QUEEN(5), KING(6);

        private final int value;

        PieceType(int value) {
            this.value = value;
        }

        static PieceType char_to_piece_type(char c) {
            return switch (c) {
                case 'P', 'p' -> PAWN;
                case 'N', 'n' -> KNIGHT;
                case 'B', 'b' -> BISHOP;
                case 'R', 'r' -> ROOK;
                case 'Q', 'q' -> QUEEN;
                case 'K', 'k' -> KING;
                default -> throw new IllegalArgumentException("Invalid piece type: " + c);
            };
        }

        static PieceType typeOfPiece(int piece) {
            return switch (piece) {
                case WHITE_PAWN, BLACK_PAWN -> PAWN;
                case WHITE_KNIGHT, BLACK_KNIGHT -> KNIGHT;
                case WHITE_BISHOP, BLACK_BISHOP -> BISHOP;
                case WHITE_ROOK, BLACK_ROOK -> ROOK;
                case WHITE_QUEEN, BLACK_QUEEN -> QUEEN;
                case WHITE_KING, BLACK_KING -> KING;
                default -> throw new IllegalArgumentException("Invalid piece type: " + piece);
            };
        }
    }

    @Getter
    enum Piece {
        W_PAWN(1), W_KNIGHT(2), W_BISHOP(3), W_ROOK(4), W_QUEEN(5), W_KING(6),
        B_PAWN(9), B_KNIGHT(10), B_BISHOP(11), B_ROOK(12), B_QUEEN(13), B_KING(14);

        private final int value;

        Piece(int value) {
            this.value = value;
        }
    }

    enum Encoding {PIECE_ENC, FILE_ENC, RANK_ENC}

    enum Color {
        BLACK, WHITE;

        static Color colorOfPiece(int piece) {
            return piece >>> 3 != 0 ? Color.BLACK : Color.WHITE;
        }

        public Color oposite() {
            if (this == WHITE) {
                return BLACK;
            } else {
                return WHITE;
            }
        }
    }


    static char pchr(int i) {
        return piece_to_char[PieceType.QUEEN.getValue() - i];
    }


    static boolean test_tb(String basePath, String fileName, String suffix) {
        Path path = Paths.get(basePath, String.format("%s%s", fileName, suffix));
        if (!path.toFile().exists()) {
            //System.out.println("File not found: " + path);
            return false;
        }
        return true;
    }


    // Given a position, produce a text string of the form KQPvKRP, where
    // "KQP" represents the white pieces if flip == false and the black pieces
    // if flip == true.
    static String prt_str(BitPosition bitPosition, boolean flip) {
        var whiteKings = Long.bitCount(bitPosition.white & bitPosition.kings);
        var whiteQueens = Long.bitCount(bitPosition.white & bitPosition.queens);
        var whiteRooks = Long.bitCount(bitPosition.white & bitPosition.rooks);
        var whiteBishops = Long.bitCount(bitPosition.white & bitPosition.bishops);
        var whiteKnights = Long.bitCount(bitPosition.white & bitPosition.knights);
        var whitePawns = Long.bitCount(bitPosition.white & bitPosition.pawns);
        var whiteStr = piecesToString(whiteKings, whiteQueens, whiteRooks, whiteBishops, whiteKnights, whitePawns);

        var blackKings = Long.bitCount(bitPosition.black & bitPosition.kings);
        var blackQueens = Long.bitCount(bitPosition.black & bitPosition.queens);
        var blackRooks = Long.bitCount(bitPosition.black & bitPosition.rooks);
        var blackBishops = Long.bitCount(bitPosition.black & bitPosition.bishops);
        var blackKnights = Long.bitCount(bitPosition.black & bitPosition.knights);
        var blackPawns = Long.bitCount(bitPosition.black & bitPosition.pawns);
        var blackStr = piecesToString(blackKings, blackQueens, blackRooks, blackBishops, blackKnights, blackPawns);

        return flip ? blackStr + "v" + whiteStr : whiteStr + "v" + blackStr;
    }

    static String piecesToString(int kings, int queens, int rooks, int bishops, int knights, int pawns) {
        return "K".repeat(kings) +
                "Q".repeat(queens) +
                "R".repeat(rooks) +
                "B".repeat(bishops) +
                "N".repeat(knights) +
                "P".repeat(pawns);
    }

    static int dtz_to_wdl(int cnt50, int dtz) {
        int wdl = 0;
        if (dtz > 0)
            wdl = (dtz + cnt50 <= 100 ? 2 : 1);
        else if (dtz < 0)
            wdl = (-dtz + cnt50 <= 100 ? -2 : -1);
        return wdl + 2;
    }


    static int TB_GET_WDL(int _res) {
        return ((_res) & TB_RESULT_WDL_MASK) >>> TB_RESULT_WDL_SHIFT;
    }

    static int TB_GET_DTZ(int _res) {
        return ((_res) & TB_RESULT_DTZ_MASK) >> TB_RESULT_DTZ_SHIFT;
    }

    static int TB_GET_FROM(int _res) {
        return ((_res) & TB_RESULT_FROM_MASK) >> TB_RESULT_FROM_SHIFT;
    }

    static int TB_GET_TO(int _res) {
        return ((_res) & TB_RESULT_TO_MASK) >> TB_RESULT_TO_SHIFT;
    }

    static int TB_SET_WDL(int _res, int _wdl) {
        return (((_res) & ~TB_RESULT_WDL_MASK) |
                (((_wdl) << TB_RESULT_WDL_SHIFT) & TB_RESULT_WDL_MASK));
    }

    static int TB_SET_TO(int _res, int _to) {
        return (((_res) & ~TB_RESULT_TO_MASK) |
                (((_to) << TB_RESULT_TO_SHIFT) & TB_RESULT_TO_MASK));
    }

    static int TB_SET_FROM(int _res, int _from) {
        return (((_res) & ~TB_RESULT_FROM_MASK) |
                (((_from) << TB_RESULT_FROM_SHIFT) & TB_RESULT_FROM_MASK));
    }

    static int TB_SET_PROMOTES(int _res, int _promotes) {
        return (((_res) & ~TB_RESULT_PROMOTES_MASK) |
                (((_promotes) << TB_RESULT_PROMOTES_SHIFT) & TB_RESULT_PROMOTES_MASK));
    }

    static int TB_SET_EP(int _res, int _ep) {
        return (((_res) & ~TB_RESULT_EP_MASK) |
                (((_ep) << TB_RESULT_EP_SHIFT) & TB_RESULT_EP_MASK));
    }

    static int TB_SET_DTZ(int _res, int _dtz) {
        return (((_res) & ~TB_RESULT_DTZ_MASK) |
                (((_dtz) << TB_RESULT_DTZ_SHIFT) & TB_RESULT_DTZ_MASK));
    }
}
