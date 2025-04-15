package net.chesstango.board.representations.syzygy;

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
    static final String[] tbSuffix = {".rtbw", ".rtbm", ".rtbz"};

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
}
