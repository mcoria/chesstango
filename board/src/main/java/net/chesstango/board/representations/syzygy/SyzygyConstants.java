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

    static final int[] tbMagic = {0x5d23e871, 0x88ac504b, 0xa50c66d7};

    static final int TB_PIECES = 7;

    static final int TB_PAWN = 1;
    static final int TB_KNIGHT = 2;
    static final int TB_BISHOP = 3;
    static final int TB_ROOK = 4;
    static final int TB_QUEEN = 5;
    static final int TB_KING = 6;

    static final int TB_WPAWN = TB_PAWN;
    static final int TB_BPAWN = (TB_PAWN | 8);

    static final int WHITE_KING = (TB_WPAWN + 5);
    static final int WHITE_QUEEN = (TB_WPAWN + 4);
    static final int WHITE_ROOK = (TB_WPAWN + 3);
    static final int WHITE_BISHOP = (TB_WPAWN + 2);
    static final int WHITE_KNIGHT = (TB_WPAWN + 1);
    static final int WHITE_PAWN = TB_WPAWN;
    static final int BLACK_KING = (TB_BPAWN + 5);
    static final int BLACK_QUEEN = (TB_BPAWN + 4);
    static final int BLACK_ROOK = (TB_BPAWN + 3);
    static final int BLACK_BISHOP = (TB_BPAWN + 2);
    static final int BLACK_KNIGHT = (TB_BPAWN + 1);
    static final int BLACK_PAWN = TB_BPAWN;

    static final int TB_MAX_PIECE = (TB_PIECES < 7 ? 254 : 650);
    static final int TB_MAX_PAWN = (TB_PIECES < 7 ? 256 : 861);
    static final int TB_MAX_SYMS = 4096;

    static final int TB_HASHBITS = (TB_PIECES < 7 ? 11 : 12);

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

    @Getter
    enum Table {
        WDL(".rtbw"), DTM(".rtbm"), DTZ(".rtbz");

        private final String suffix;

        Table(String suffix) {
            this.suffix = suffix;
        }
    }

    enum Encoding {PIECE_ENC, FILE_ENC, RANK_ENC}



    static char pchr(int i) {
        return piece_to_char[PieceType.QUEEN.getValue() - i];
    }

    static int[] tableName_to_pcs(String tbName) {
        char[] tbNameChars = tbName.toCharArray();
        int[] pcs = new int[16];
        int color = 0;
        for (char c : tbNameChars) {
            if (c == 'v') {
                color = 8;
            } else {
                PieceType piece_type = PieceType.char_to_piece_type(c);
                assert ((piece_type.getValue() | color) < 16);
                pcs[piece_type.getValue() | color]++;
            }
        }
        return pcs;
    }


    static long calc_key_from_pcs(int[] pcs, boolean mirror) {
        int theMirror = (mirror ? 8 : 0);
        return pcs[WHITE_QUEEN ^ theMirror] * PRIME_WHITE_QUEEN +
                pcs[WHITE_ROOK ^ theMirror] * PRIME_WHITE_ROOK +
                pcs[WHITE_BISHOP ^ theMirror] * PRIME_WHITE_BISHOP +
                pcs[WHITE_KNIGHT ^ theMirror] * PRIME_WHITE_KNIGHT +
                pcs[WHITE_PAWN ^ theMirror] * PRIME_WHITE_PAWN +
                pcs[BLACK_QUEEN ^ theMirror] * PRIME_BLACK_QUEEN +
                pcs[BLACK_ROOK ^ theMirror] * PRIME_BLACK_ROOK +
                pcs[BLACK_BISHOP ^ theMirror] * PRIME_BLACK_BISHOP +
                pcs[BLACK_KNIGHT ^ theMirror] * PRIME_BLACK_KNIGHT +
                pcs[BLACK_PAWN ^ theMirror] * PRIME_BLACK_PAWN;
    }

    static long calcKey(BitPosition bitPosition) {
        return Long.bitCount(bitPosition.white() & bitPosition.queens()) * PRIME_WHITE_QUEEN +
                Long.bitCount(bitPosition.white() & bitPosition.rooks()) * PRIME_WHITE_ROOK +
                Long.bitCount(bitPosition.white() & bitPosition.bishops()) * PRIME_WHITE_BISHOP +
                Long.bitCount(bitPosition.white() & bitPosition.knights()) * PRIME_WHITE_KNIGHT +
                Long.bitCount(bitPosition.white() & bitPosition.pawns()) * PRIME_WHITE_PAWN +
                Long.bitCount(bitPosition.black() & bitPosition.queens()) * PRIME_BLACK_QUEEN +
                Long.bitCount(bitPosition.black() & bitPosition.rooks()) * PRIME_BLACK_ROOK +
                Long.bitCount(bitPosition.black() & bitPosition.bishops()) * PRIME_BLACK_BISHOP +
                Long.bitCount(bitPosition.black() & bitPosition.knights()) * PRIME_BLACK_KNIGHT +
                Long.bitCount(bitPosition.black() & bitPosition.pawns()) * PRIME_BLACK_PAWN;
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
        var whiteKings = Long.bitCount(bitPosition.white() & bitPosition.kings());
        var whiteQueens = Long.bitCount(bitPosition.white() & bitPosition.queens());
        var whiteRooks = Long.bitCount(bitPosition.white() & bitPosition.rooks());
        var whiteBishops = Long.bitCount(bitPosition.white() & bitPosition.bishops());
        var whiteKnights = Long.bitCount(bitPosition.white() & bitPosition.knights());
        var whitePawns = Long.bitCount(bitPosition.white() & bitPosition.pawns());
        var whiteStr = piecesToString(whiteKings, whiteQueens, whiteRooks, whiteBishops, whiteKnights, whitePawns);

        var blackKings = Long.bitCount(bitPosition.black() & bitPosition.kings());
        var blackQueens = Long.bitCount(bitPosition.black() & bitPosition.queens());
        var blackRooks = Long.bitCount(bitPosition.black() & bitPosition.rooks());
        var blackBishops = Long.bitCount(bitPosition.black() & bitPosition.bishops());
        var blackKnights = Long.bitCount(bitPosition.black() & bitPosition.knights());
        var blackPawns = Long.bitCount(bitPosition.black() & bitPosition.pawns());
        var blackStr = piecesToString(blackKings, blackQueens, blackRooks, blackBishops, blackKnights, blackPawns);

        return flip ? blackStr + "v" + whiteStr : whiteStr + "v" + blackStr;
    }

    private static String piecesToString(int kings, int queens, int rooks, int bishops, int knights, int pawns) {
        return "K".repeat(kings) +
                "Q".repeat(queens) +
                "R".repeat(rooks) +
                "B".repeat(bishops) +
                "N".repeat(knights) +
                "P".repeat(pawns);
    }
}
