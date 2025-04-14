package net.chesstango.board.representations.syzygy;

import lombok.Getter;
import net.chesstango.board.Color;
import net.chesstango.board.position.BitBoard;
import net.chesstango.board.position.Position;
import net.chesstango.board.position.PositionState;

/**
 * @author Mauricio Coria
 */
public class Syzygy {
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

    static final int TB_PIECES = 7;
    static final int TB_HASHBITS = (TB_PIECES < 7 ? 11 : 12);

    TbHashEntry[] tbHash = new TbHashEntry[1 << TB_HASHBITS];

    public void probeTable(Position chessPosition) {
        BitPosition bitPosition = toPosition(chessPosition);
        long key = calcKey(bitPosition);
        int idx = (int) (key >>> (64 - TB_HASHBITS));

        System.out.println(idx);
    }

    void init_tb(String tbName) {
        int[] pcs = toPcsArray(tbName);
    }

    int[] toPcsArray(String tbName) {
        char[] tbNameChars = tbName.toCharArray();
        int[] pcs = new int[16];
        int color = 0;
        for (char c : tbNameChars) {
            if (c == 'v') {
                color = 8;
            } else {
                PieceType piece_type = PieceType.char_to_piece_type(c);
                assert ((piece_type.value | color) < 16);
                pcs[piece_type.value | color]++;
            }
        }
        return pcs;
    }


    BitPosition toPosition(Position chessPosition) {
        BitBoard bitBoard = chessPosition.getBitBoard();
        PositionState positionState = chessPosition.getPositionState();
        long white = bitBoard.getPositions(Color.WHITE);
        long black = bitBoard.getPositions(Color.BLACK);
        long kings = bitBoard.getKingPositions();
        long queens = bitBoard.getQueenPositions();
        long rooks = bitBoard.getRookPositions();
        long bishops = bitBoard.getBishopPositions();
        long knights = bitBoard.getKnightPositions();
        long pawns = bitBoard.getPawnPositions();

        byte rule50 = 0;
        byte ep = 0;
        if (positionState.getEnPassantSquare() != null) {
            ep = (byte) positionState.getEnPassantSquare().toIdx();
        }
        boolean turn = positionState.getCurrentTurn() == Color.WHITE;

        return new BitPosition(white,
                black,
                kings,
                queens,
                rooks,
                bishops,
                knights,
                pawns,
                rule50,
                ep,
                turn);
    }

    long calcKey(BitPosition bitPosition) {
        return Long.bitCount(bitPosition.white & bitPosition.queens) * PRIME_WHITE_QUEEN +
                Long.bitCount(bitPosition.white & bitPosition.rooks) * PRIME_WHITE_ROOK +
                Long.bitCount(bitPosition.white & bitPosition.bishops) * PRIME_WHITE_BISHOP +
                Long.bitCount(bitPosition.white & bitPosition.knights) * PRIME_WHITE_KNIGHT +
                Long.bitCount(bitPosition.white & bitPosition.pawns) * PRIME_WHITE_PAWN +
                Long.bitCount(bitPosition.black & bitPosition.queens) * PRIME_BLACK_QUEEN +
                Long.bitCount(bitPosition.black & bitPosition.rooks) * PRIME_BLACK_ROOK +
                Long.bitCount(bitPosition.black & bitPosition.bishops) * PRIME_BLACK_BISHOP +
                Long.bitCount(bitPosition.black & bitPosition.knights) * PRIME_BLACK_KNIGHT +
                Long.bitCount(bitPosition.black & bitPosition.pawns) * PRIME_BLACK_PAWN;
    }


    record BitPosition(long white,
                       long black,
                       long kings,
                       long queens,
                       long rooks,
                       long bishops,
                       long knights,
                       long pawns,
                       byte rule50,
                       byte ep,
                       boolean turn) {
    }

    record TbHashEntry(long key) {
    }


    enum PieceType {
        PAWN(1), KNIGHT(2), BISHOP(3), ROOK(4), QUEEN(5), KING(6);

        @Getter
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

    enum Piece {
        W_PAWN(1), W_KNIGHT(2), W_BISHOP(3), W_ROOK(4), W_QUEEN(5), W_KING(6),
        B_PAWN(9), B_KNIGHT(10), B_BISHOP(11), B_ROOK(12), B_QUEEN(13), B_KING(14);

        @Getter
        private final int value;

        Piece(int value) {
            this.value = value;
        }
    }

}
