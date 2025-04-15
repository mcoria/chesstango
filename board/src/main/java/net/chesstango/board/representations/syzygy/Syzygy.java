package net.chesstango.board.representations.syzygy;

import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.nio.file.Paths;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.*;

/**
 * Syzygy Bases consist of two sets of files,
 * - WDL files (extension .rtbw) storing win/draw/loss information considering the fifty-move rule for access during search
 * - DTZ files (extension .rtbz) with distance-to-zero information for access at the root.
 * <p>
 * <p>
 * DTZ measures the shortest distance to zero the position (zeroing = pawn move, capture or checkmate),
 * without compromising the Win/Draw/Loss status. If White only has just sufficient material to mate,
 * then any zeroing is likely to be useful, but if White has way more material than is necessary,
 * White may try to sacrifice its own unit, and Black may try to refuse the sacrifice
 * <p>
 * WDL has full data for two sides but DTZ50 omitted data of one side to save space. Each endgame has a pair of those types.
 * <p>
 * Syzygy WDL is double-sided, DTZ is single-sided.
 * So to know whether a 7-piece position is winning, losing or drawn (or cursed), the engine needs to do only a single probe of a 7-piece WDL table. (It may in addition have to do some probes of 6-piece WDL tables if any direct captures are available.)
 * If the engine needs to know the DTZ value (which is only necessary when a TB root position has been reached), the probing code may have to do a 1-ply search to get to the "right" side of the DTZ table.
 *
 * @author Mauricio Coria
 */
public class Syzygy {

    TbHashEntry[] tbHash = new TbHashEntry[1 << TB_HASHBITS];
    PieceEntry[] pieceEntry = new PieceEntry[TB_MAX_PIECE];
    PawnEntry[] pawnEntry = new PawnEntry[TB_MAX_PAWN];

    int tbNumPiece;
    int tbNumPawn;
    int numWdl;
    int numDtm;
    int numDtz;
    int TB_MaxCardinality;
    int TB_MaxCardinalityDTM;
    int TB_LARGEST;

    @Setter
    String path;

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


    public void probeTable(BitPosition bitPosition) {
        long key = calcKey(bitPosition);
        int idx = (int) (key >>> (64 - TB_HASHBITS));
    }

    public void tb_init(String path) {
        tbNumPiece = 0;
        tbNumPawn = 0;
        numWdl = 0;
        numDtm = 0;
        numDtz = 0;
        TB_MaxCardinality = 0;
        TB_MaxCardinalityDTM = 0;
        TB_LARGEST = 0;

        setPath(path);

        for (int i = 0; i < 5; i++) {
            String tableName = String.format("K%cvK", pchr(i));
            init_tb(tableName);
        }

        TB_LARGEST = TB_MaxCardinality;
        if (TB_MaxCardinalityDTM > TB_LARGEST) {
            TB_LARGEST = TB_MaxCardinalityDTM;
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
            be.num += (char) pcs[i];
        }

        numWdl++;
        if (test_tb(tbName, Suffix.DTM.getSuffix())) {
            numDtm++;
            be.hasDtm = true;
        }
        if (test_tb(tbName, Suffix.DTZ.getSuffix())) {
            numDtz++;
            be.hasDtz = true;
        }
        if (be.num > TB_MaxCardinality) {
            TB_MaxCardinality = be.num;
        }
        if (be.hasDtm && be.num > TB_MaxCardinalityDTM) {
            TB_MaxCardinalityDTM = be.num;
        }

        if (!be.hasPawns) {
            int j = 0;
            for (int i = 0; i < 16; i++)
                if (pcs[i] == 1) j++;
            be.kk_enc = j == 2;
        } else {
            be.pawns[0] = (char) pcs[Piece.W_PAWN.value];
            be.pawns[1] = (char) pcs[Piece.B_PAWN.value];
            if (pcs[Piece.B_PAWN.value] != 0 && (pcs[Piece.W_PAWN.value] != 0 || (pcs[Piece.W_PAWN.value] > pcs[Piece.B_PAWN.value]))) {
                char tmp = be.pawns[0];
                be.pawns[0] = be.pawns[1];
                be.pawns[1] = tmp;
            }
        }

        add_to_hash(be, key);
        if (key != key2) {
            add_to_hash(be, key2);
        }
    }

    void add_to_hash(BaseEntry ptr, long key) {
        int idx = (int) (key >>> (64 - TB_HASHBITS));
        while (tbHash[idx].ptr != null) {
            idx = (idx + 1) & ((1 << TB_HASHBITS) - 1);
        }

        tbHash[idx].key = key;
        tbHash[idx].ptr = ptr;
        tbHash[idx].error = false;
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

    boolean test_tb(String fileName, String suffix) {
        Path path = Paths.get(this.path, String.format("%s%s", fileName, suffix));
        if (!path.toFile().exists()) {
            //System.out.println("File not found: " + path);
            return false;
        }
        return true;
    }

    char pchr(int i) {
        return piece_to_char[PieceType.QUEEN.value - (i)];
    }


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
    enum Suffix {
        WDL(".rtbw"), DTM(".rtbm"), DTZ(".rtbz");

        private final String suffix;

        Suffix(String suffix) {
            this.suffix = suffix;
        }
    }
}
