package net.chesstango.board.representations.syzygy;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.*;

/**
 * @author Mauricio Coria
 */
public class Chess {
    static final short MOVE_STALEMATE = (short) 0xFFFF;
    static final short MOVE_CHECKMATE = (short) 0xFFFE;

    static final long BOARD_RANK_EDGE = 0x8181818181818181L;
    static final long BOARD_FILE_EDGE = 0xFF000000000000FFL;
    static final long BOARD_EDGE = (BOARD_RANK_EDGE | BOARD_FILE_EDGE);
    static final long BOARD_RANK_1 = 0x00000000000000FFL;
    static final long BOARD_FILE_A = 0x8080808080808080L;

    static long poplsb(long b) {
        return b & (b - 1);
    }

    static int lsb(long b) {
        return Long.numberOfTrailingZeros(b);
    }

    static boolean is_valid(BitPosition pos) {
        return true;
    }

    static short move_from(short move) {
        return (short) ((move >>> 6) & 0x3F);
    }

    static short move_to(short move) {
        return (short) (move & 0x3F);
    }

    static short move_promotes(short move) {
        return (short) ((move >>> 12) & 0x7);
    }

    static long board(int square) {
        return 1L << square;
    }

    static int square(int r, int f) {
        return (8 * (r) + (f));
    }

    static int rank(int s) {
        return s >> 3;
    }

    static int file(int s) {
        return s & 0x07;
    }

    static int rank2index(long b, int r) {
        b >>= (8 * r);
        b >>= 1;
        return (int) b;
    }

    static int file2index(long b, int f) {
        b >>= f;
        b *= 0x0102040810204080L;
        b >>= 56;
        b >>= 1;
        return (int) b;
    }

    static int diag2index(long b) {
        b *= 0x0101010101010101L;
        b >>= 56;
        b >>= 1;
        return (int) b;
    }

    static int anti2index(long b) {
        return diag2index(b);
    }

    static long rank2board(int r) {
        return 0xFFL << (8 * r);
    }

    static long file2board(int f) {
        return 0x0101010101010101L << f;
    }

    static int diag(int s) {
        return square2diag_table[s];
    }

    static int anti(int s) {
        return square2anti_table[s];
    }

    static long diag2board(int d) {
        return diag2board_table[(d)];
    }

    static long anti2board(int a) {
        return anti2board_table[(a)];
    }

    static boolean is_en_passant(BitPosition pos, short move) {
        short from = move_from(move);
        short to = move_to(move);
        long us = (pos.turn ? pos.white : pos.black);
        if (pos.ep == 0)
            return false;
        if (to != pos.ep) {
            return false;
        }
        return (board(from) & us & pos.pawns) != 0;
    }

    /*
     * Test if the king is in check.
     */
    static boolean is_check(BitPosition pos) {
        long occ = pos.white | pos.black;
        long us = (pos.turn ? pos.white : pos.black),
                them = (pos.turn ? pos.black : pos.white);
        long king = pos.kings & us;
        assert (king != 0);
        int sq = lsb(king);
        long ratt = rook_attacks(sq, occ);
        long batt = bishop_attacks(sq, occ);
        if ((ratt & (pos.rooks & them)) != 0)
            return true;
        if ((batt & (pos.bishops & them)) != 0)
            return true;
        if (((ratt | batt) & (pos.queens & them)) != 0)
            return true;
        if ((knight_attacks(sq) & (pos.knights & them)) != 0)
            return true;
        if ((pawn_attacks(sq, pos.turn) & (pos.pawns & them)) != 0)
            return true;
        return false;
    }


    static boolean legal_move(BitPosition pos, short move) {
        BitPosition pos1 = new BitPosition();
        return do_move(pos1, pos, move);
    }

    static long do_bb_move(long b, int from, int to) {
        return (((b) & (~board(to)) & (~board(from))) |
                ((((b) >> (from)) & 0x1) << (to)));
    }

    static boolean do_move(BitPosition pos, BitPosition pos0, short move) {
        int from = move_from(move);
        int to = move_to(move);
        int promotes = move_promotes(move);
        pos.turn = !pos0.turn;
        pos.white = do_bb_move(pos0.white, from, to);
        pos.black = do_bb_move(pos0.black, from, to);
        pos.kings = do_bb_move(pos0.kings, from, to);
        pos.queens = do_bb_move(pos0.queens, from, to);
        pos.rooks = do_bb_move(pos0.rooks, from, to);
        pos.bishops = do_bb_move(pos0.bishops, from, to);
        pos.knights = do_bb_move(pos0.knights, from, to);
        pos.pawns = do_bb_move(pos0.pawns, from, to);
        pos.ep = 0;
        if (promotes != TB_PROMOTES_NONE) {
            pos.pawns &= ~board(to);       // Promotion
            switch (promotes) {
                case TB_PROMOTES_QUEEN:
                    pos.queens |= board(to);
                    break;
                case TB_PROMOTES_ROOK:
                    pos.rooks |= board(to);
                    break;
                case TB_PROMOTES_BISHOP:
                    pos.bishops |= board(to);
                    break;
                case TB_PROMOTES_KNIGHT:
                    pos.knights |= board(to);
                    break;
            }
            pos.rule50 = 0;
        } else if ((board(from) & pos0.pawns) != 0) {
            pos.rule50 = 0;                // Pawn move
            if (rank(from) == 1 && rank(to) == 3 &&
                    (pawn_attacks(from + 8, true) & pos0.pawns & pos0.black) != 0)
                pos.ep = from + 8;
            else if (rank(from) == 6 && rank(to) == 4 &&
                    (pawn_attacks(from - 8, false) & pos0.pawns & pos0.white) != 0)
                pos.ep = from - 8;
            else if (to == pos0.ep) {
                int ep_to = (pos0.turn ? to - 8 : to + 8);
                long ep_mask = ~board(ep_to);
                pos.white &= ep_mask;
                pos.black &= ep_mask;
                pos.pawns &= ep_mask;
            }
        } else if ((board(to) & (pos0.white | pos0.black)) != 0)
            pos.rule50 = 0;                // Capture
        else
            pos.rule50 = pos0.rule50 + 1; // Normal move
        if (!is_legal(pos))
            return false;
        return true;
    }

    /*
     * Test if the given position is legal.
     * (Pawns on backrank? Can the king be captured?)
     */
    static boolean is_legal(BitPosition pos) {
        long occ = pos.white | pos.black;
        long us = (pos.turn ? pos.black : pos.white),
                them = (pos.turn ? pos.white : pos.black);
        long king = pos.kings & us;
        if (king == 0)
            return false;
        int sq = lsb(king);
        if ((king_attacks(sq) & (pos.kings & them)) != 0)
            return false;
        long ratt = rook_attacks(sq, occ);
        long batt = bishop_attacks(sq, occ);
        if ((ratt & (pos.rooks & them)) != 0)
            return false;
        if ((batt & (pos.bishops & them)) != 0)
            return false;
        if (((ratt | batt) & (pos.queens & them)) != 0)
            return false;
        if ((knight_attacks(sq) & (pos.knights & them)) != 0)
            return false;
        if ((pawn_attacks(sq, !pos.turn) & (pos.pawns & them)) != 0)
            return false;
        return true;
    }

    /*
     * Test if the given move is a capture.
     */
    static boolean is_capture(BitPosition pos, short move) {
        long to = move_to(move);
        long them = (pos.turn ? pos.black : pos.white);
        return (them & board((int) to)) != 0 || is_en_passant(pos, move);
    }

    /*
     * TABLES
     */

    static final long[][] rank_attacks_table = new long[64][64];
    static final long[][] file_attacks_table = new long[64][64];
    static final long[] diag2board_table = new long[]
            {
                    0x8040201008040201L,
                    0x0080402010080402L,
                    0x0000804020100804L,
                    0x0000008040201008L,
                    0x0000000080402010L,
                    0x0000000000804020L,
                    0x0000000000008040L,
                    0x0000000000000080L,
                    0x0100000000000000L,
                    0x0201000000000000L,
                    0x0402010000000000L,
                    0x0804020100000000L,
                    0x1008040201000000L,
                    0x2010080402010000L,
                    0x4020100804020100L,
            };

    static final long[] anti2board_table = new long[]
            {
                    0x0102040810204080L,
                    0x0204081020408000L,
                    0x0408102040800000L,
                    0x0810204080000000L,
                    0x1020408000000000L,
                    0x2040800000000000L,
                    0x4080000000000000L,
                    0x8000000000000000L,
                    0x0000000000000001L,
                    0x0000000000000102L,
                    0x0000000000010204L,
                    0x0000000001020408L,
                    0x0000000102040810L,
                    0x0000010204081020L,
                    0x0001020408102040L,
            };

    static final int[] square2diag_table = new int[]
            {
                    0, 1, 2, 3, 4, 5, 6, 7,
                    14, 0, 1, 2, 3, 4, 5, 6,
                    13, 14, 0, 1, 2, 3, 4, 5,
                    12, 13, 14, 0, 1, 2, 3, 4,
                    11, 12, 13, 14, 0, 1, 2, 3,
                    10, 11, 12, 13, 14, 0, 1, 2,
                    9, 10, 11, 12, 13, 14, 0, 1,
                    8, 9, 10, 11, 12, 13, 14, 0
            };

    static final int[] square2anti_table = new int[]
            {
                    8, 9, 10, 11, 12, 13, 14, 0,
                    9, 10, 11, 12, 13, 14, 0, 1,
                    10, 11, 12, 13, 14, 0, 1, 2,
                    11, 12, 13, 14, 0, 1, 2, 3,
                    12, 13, 14, 0, 1, 2, 3, 4,
                    13, 14, 0, 1, 2, 3, 4, 5,
                    14, 0, 1, 2, 3, 4, 5, 6,
                    0, 1, 2, 3, 4, 5, 6, 7
            };

    static final long[][] diag_attacks_table = new long[64][64];
    static final long[][] anti_attacks_table = new long[64][64];
    static final long[] king_attacks_table = new long[64];
    static final long[] knight_attacks_table = new long[64];

    /*
     * ATTACK METHODS
     */

    static long king_attacks(int s) {
        return king_attacks_table[s];
    }

    static long knight_attacks(int s) {
        return knight_attacks_table[s];
    }

    static long[][] pawn_attacks_table = new long[2][64];

    static long pawn_attacks(int s, boolean c) {
        return pawn_attacks_table[c ? 1 : 0][s];
    }

    static long queen_attacks(int s, long occ) {
        return rook_attacks(s, occ) | bishop_attacks(s, occ);
    }

    static long rook_attacks(int sq, long occ) {
        occ &= ~board(sq);
        int r = rank(sq);
        int f = file(sq);
        long r_occ = occ & (rank2board(r) & ~BOARD_RANK_EDGE);
        long f_occ = occ & (file2board(f) & ~BOARD_FILE_EDGE);
        int r_idx = rank2index(r_occ, r);
        int f_idx = file2index(f_occ, f);
        long r_attacks = rank_attacks_table[sq][r_idx];
        long f_attacks = file_attacks_table[sq][f_idx];
        return r_attacks | f_attacks;
    }

    static long bishop_attacks(int sq, long occ) {
        occ &= ~board(sq);
        int d = diag(sq), a = anti(sq);
        long d_occ = occ & (diag2board(d) & ~BOARD_EDGE);
        long a_occ = occ & (anti2board(a) & ~BOARD_EDGE);
        int d_idx = diag2index(d_occ);
        int a_idx = anti2index(a_occ);
        long d_attacks = diag_attacks_table[sq][d_idx];
        long a_attacks = anti_attacks_table[sq][a_idx];
        return d_attacks | a_attacks;
    }

    /*
     * Generate all moves.
     */
    static int gen_moves(BitPosition pos, short[] moves) {
        long occ = pos.white | pos.black;
        long us = (pos.turn ? pos.white : pos.black),
                them = (pos.turn ? pos.black : pos.white);
        long b, att;
        int idx = 0;
        {
            int from = lsb(pos.kings & us);
            for (att = king_attacks(from) & ~us; att != 0; att = poplsb(att)) {
                int to = lsb(att);
                idx = add_move(moves, idx, false, from, to);
            }
        }
        for (b = us & pos.queens; b != 0; b = poplsb(b)) {
            int from = lsb(b);
            for (att = queen_attacks(from, occ) & ~us; att != 0; att = poplsb(att)) {
                int to = lsb(att);
                idx = add_move(moves, idx, false, from, to);
            }
        }
        for (b = us & pos.rooks; b != 0; b = poplsb(b)) {
            int from = lsb(b);
            for (att = rook_attacks(from, occ) & ~us; att != 0; att = poplsb(att)) {
                int to = lsb(att);
                idx = add_move(moves, idx, false, from, to);
            }
        }
        for (b = us & pos.bishops; b != 0; b = poplsb(b)) {
            int from = lsb(b);
            for (att = bishop_attacks(from, occ) & ~us; att != 0; att = poplsb(att)) {
                int to = lsb(att);
                idx = add_move(moves, idx, false, from, to);
            }
        }
        for (b = us & pos.knights; b != 0; b = poplsb(b)) {
            int from = lsb(b);
            for (att = knight_attacks(from) & ~us; att != 0; att = poplsb(att)) {
                int to = lsb(att);
                idx = add_move(moves, idx, false, from, to);
            }
        }
        for (b = us & pos.pawns; b != 0; b = poplsb(b)) {
            int from = lsb(b);
            int next = from + (pos.turn ? 8 : -8);
            att = pawn_attacks(from, pos.turn);
            if (pos.ep != 0 && ((att & board(pos.ep)) != 0)) {
                int to = pos.ep;
                idx = add_move(moves, idx, false, from, to);
            }
            att &= them;
            if ((board(next) & occ) == 0) {
                att |= board(next);
                int next2 = from + (pos.turn ? 16 : -16);
                if ((pos.turn ? rank(from) == 1 : rank(from) == 6) &&
                        ((board(next2) & occ) == 0))
                    att |= board(next2);
            }
            for (; att != 0; att = poplsb(att)) {
                int to = lsb(att);
                idx = add_move(moves, idx, (rank(to) == 7 || rank(to) == 0), from,
                        to);
            }
        }
        return idx;
    }

    /*
     * Generate all captures, including all underpomotions
     */
    static int gen_captures(BitPosition pos, short[] moves) {
        long occ = pos.white | pos.black;
        long us = (pos.turn ? pos.white : pos.black),
                them = (pos.turn ? pos.black : pos.white);
        long b, att;
        int idx = 0;
        {
            int from = Long.numberOfTrailingZeros(pos.kings & us);
            assert (from < 64);
            for (att = king_attacks(from) & them; att != 0; att = poplsb(att)) {
                int to = Long.numberOfTrailingZeros(att);
                idx = add_move(moves, idx, false, from, to);
            }
        }
        for (b = us & pos.queens; b != 0; b = poplsb(b)) {
            int from = Long.numberOfTrailingZeros(b);
            for (att = queen_attacks(from, occ) & them; att != 0; att = poplsb(att)) {
                int to = Long.numberOfTrailingZeros(att);
                idx = add_move(moves, idx, false, from, to);
            }
        }
        for (b = us & pos.rooks; b != 0; b = poplsb(b)) {
            int from = Long.numberOfTrailingZeros(b);
            for (att = rook_attacks(from, occ) & them; att != 0; att = poplsb(att)) {
                int to = Long.numberOfTrailingZeros(att);
                idx = add_move(moves, idx, false, from, to);
            }
        }
        for (b = us & pos.bishops; b != 0; b = poplsb(b)) {
            int from = Long.numberOfTrailingZeros(b);
            for (att = bishop_attacks(from, occ) & them; att != 0; att = poplsb(att)) {
                int to = Long.numberOfTrailingZeros(att);
                idx = add_move(moves, idx, false, from, to);
            }
        }
        for (b = us & pos.knights; b != 0; b = poplsb(b)) {
            int from = Long.numberOfTrailingZeros(b);
            for (att = knight_attacks(from) & them; att != 0; att = poplsb(att)) {
                int to = Long.numberOfTrailingZeros(att);
                idx = add_move(moves, idx, false, from, to);
            }
        }
        for (b = us & pos.pawns; b != 0; b = poplsb(b)) {
            int from = Long.numberOfTrailingZeros(b);
            att = pawn_attacks(from, pos.turn);
            if (pos.ep != 0 && ((att & board(pos.ep)) != 0)) {
                int to = pos.ep;
                idx = add_move(moves, idx, false, from, to);
            }
            for (att = att & them; att != 0; att = poplsb(att)) {
                int to = Long.numberOfTrailingZeros(att);
                idx = add_move(moves, idx, (rank(to) == 7 || rank(to) == 0), from, to);
            }
        }

        return idx;
    }

    static int add_move(short[] moves, int idx, boolean promotes, int from, int to) {
        if (!promotes) {
            moves[idx++] = make_move(TB_PROMOTES_NONE, from, to);
        } else {
            moves[idx++] = make_move(TB_PROMOTES_QUEEN, from, to);
            moves[idx++] = make_move(TB_PROMOTES_KNIGHT, from, to);
            moves[idx++] = make_move(TB_PROMOTES_ROOK, from, to);
            moves[idx++] = make_move(TB_PROMOTES_BISHOP, from, to);
        }
        return idx;
    }

    static short make_move(int promote, int from, int to) {
        return (short) (((promote & 0x7) << 12) | ((from & 0x3F) << 6) | (to & 0x3F));
    }

    static void pawn_attacks_init() {
        for (int s = 0; s < 64; s++) {
            int r = rank(s);
            int f = file(s);

            long b = 0;
            if (r != 7) {
                if (f != 0)
                    b |= board(square(r + 1, f - 1));
                if (f != 7)
                    b |= board(square(r + 1, f + 1));
            }
            pawn_attacks_table[1][s] = b;

            b = 0;
            if (r != 0) {
                if (f != 0)
                    b |= board(square(r - 1, f - 1));
                if (f != 7)
                    b |= board(square(r - 1, f + 1));
            }
            pawn_attacks_table[0][s] = b;
        }
    }

    static void bishop_attacks_init() {
        for (int idx = 0; idx < 64; idx++) {
            int idx1 = idx << 1;
            for (int s = 0; s < 64; s++) {
                int r = rank(s);
                int f = file(s);
                long b = 0;
                for (int i = -1; f + i >= 0 && r + i >= 0; i--) {
                    int occ = (1 << (f + i));
                    b |= board(square(r + i, f + i));
                    if ((idx1 & occ) != 0)
                        break;
                }
                for (int i = 1; f + i <= 7 && r + i <= 7; i++) {
                    int occ = (1 << (f + i));
                    b |= board(square(r + i, f + i));
                    if ((idx1 & occ) != 0)
                        break;
                }
                diag_attacks_table[s][idx] = b;
            }
        }

        for (int idx = 0; idx < 64; idx++) {
            int idx1 = idx << 1;
            for (int s = 0; s < 64; s++) {
                int r = rank(s);
                int f = file(s);
                long b = 0;
                for (int i = -1; f + i >= 0 && r - i <= 7; i--) {
                    int occ = (1 << (f + i));
                    b |= board(square(r - i, f + i));
                    if ((idx1 & occ) != 0)
                        break;
                }
                for (int i = 1; f + i <= 7 && r - i >= 0; i++) {
                    int occ = (1 << (f + i));
                    b |= board(square(r - i, f + i));
                    if ((idx1 & occ) != 0)
                        break;
                }
                anti_attacks_table[s][idx] = b;
            }
        }
    }

    static void king_attacks_init() {
        for (int s = 0; s < 64; s++) {
            int r = rank(s);
            int f = file(s);
            long b = 0;
            if (r != 0 && f != 0)
                b |= board(square(r - 1, f - 1));
            if (r != 0)
                b |= board(square(r - 1, f));
            if (r != 0 && f != 7)
                b |= board(square(r - 1, f + 1));
            if (f != 7)
                b |= board(square(r, f + 1));
            if (r != 7 && f != 7)
                b |= board(square(r + 1, f + 1));
            if (r != 7)
                b |= board(square(r + 1, f));
            if (r != 7 && f != 0)
                b |= board(square(r + 1, f - 1));
            if (f != 0)
                b |= board(square(r, f - 1));
            king_attacks_table[s] = b;
        }
    }

    static void knight_attacks_init() {
        for (int s = 0; s < 64; s++) {
            int r1, r = rank(s);
            int f1, f = file(s);
            long b = 0;
            r1 = r - 1;
            f1 = f - 2;
            if (r1 >= 0 && f1 >= 0)
                b |= board(square(r1, f1));
            r1 = r - 1;
            f1 = f + 2;
            if (r1 >= 0 && f1 <= 7)
                b |= board(square(r1, f1));
            r1 = r - 2;
            f1 = f - 1;
            if (r1 >= 0 && f1 >= 0)
                b |= board(square(r1, f1));
            r1 = r - 2;
            f1 = f + 1;
            if (r1 >= 0 && f1 <= 7)
                b |= board(square(r1, f1));
            r1 = r + 1;
            f1 = f - 2;
            if (r1 <= 7 && f1 >= 0)
                b |= board(square(r1, f1));
            r1 = r + 1;
            f1 = f + 2;
            if (r1 <= 7 && f1 <= 7)
                b |= board(square(r1, f1));
            r1 = r + 2;
            f1 = f - 1;
            if (r1 <= 7 && f1 >= 0)
                b |= board(square(r1, f1));
            r1 = r + 2;
            f1 = f + 1;
            if (r1 <= 7 && f1 <= 7)
                b |= board(square(r1, f1));
            knight_attacks_table[s] = b;
        }
    }


    static {
        pawn_attacks_init();
        bishop_attacks_init();
        king_attacks_init();
        knight_attacks_init();
    }
}
