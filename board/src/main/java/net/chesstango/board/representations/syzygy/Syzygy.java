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


    TbHashEntry[] tbHash = new TbHashEntry[1 << TB_HASHBITS];
    PieceEntry[] pieceEntry = new PieceEntry[TB_MAX_PIECE];
    PawnEntry[] pawnEntry = new PawnEntry[TB_MAX_PAWN];

    int tbNumPiece;
    int tbNumPawn;
    int TB_MaxCardinality;
    int TB_MaxCardinalityDTM;
    int TB_LARGEST;

    public Syzygy() {
        /*
         * Initialize the hash tables
         */
        for (int i = 0; i < (1 << TB_HASHBITS); i++) {
            tbHash[i] = new TbHashEntry();
            tbHash[i].key = 0;
            tbHash[i].ptr = null;
        }

        for (int i = 0; i < TB_MAX_PIECE; i++) {
            pieceEntry[i] = new PieceEntry();
            pieceEntry[i].be = new BaseEntry();
        }

        for (int i = 0; i < TB_MAX_PAWN; i++) {
            pawnEntry[i] = new PawnEntry();
            pawnEntry[i].be = new BaseEntry();
        }
    }


    public void probeTable(Position chessPosition) {
        BitPosition bitPosition = toPosition(chessPosition);
        long key = calcKey(bitPosition);
        int idx = (int) (key >>> (64 - TB_HASHBITS));

        System.out.println(idx);
    }

    public void tb_init(String path) {
        tbNumPiece = 0;
        tbNumPawn = 0;
        TB_MaxCardinality = 0;
        TB_MaxCardinalityDTM = 0;
        TB_LARGEST = 0;


        for (int i = 0; i < 5; i++) {
            String tableName = String.format("K%cvK", pchr(i));
            init_tb(tableName);
        }
    }

    void init_tb(String tbName) {
        int[] pcs = toPcsArray(tbName);
        long key = calc_key_from_pcs(pcs, false);
        long key2 = calc_key_from_pcs(pcs, true);

        boolean hasPawns = (pcs[Piece.W_PAWN.value] | pcs[Piece.B_PAWN.value]) != 0;

        BaseEntry be = hasPawns ? pawnEntry[tbNumPawn++].be : pieceEntry[tbNumPiece++].be;

        be.hasPawns = hasPawns;
        be.key = key;
        be.symmetric = key == key2;
        be.num = 0;
        for (int i = 0; i < 16; i++) {
            be.num += pcs[i];
        }


        if (be.num > TB_MaxCardinality) {
            TB_MaxCardinality = be.num;
        }
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

    long calc_key_from_pcs(int[] pcs, boolean mirror) {
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

    private char pchr(int i) {
        return piece_to_char[PieceType.QUEEN.value - (i)];
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

    class BaseEntry {
        long key;
        boolean hasPawns;
        boolean symmetric;
        int num;
    }

    class TbHashEntry {
        long key;
        BaseEntry ptr;
    }

    class PieceEntry {
        BaseEntry be;
    }

    class PawnEntry {
        BaseEntry be;
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
