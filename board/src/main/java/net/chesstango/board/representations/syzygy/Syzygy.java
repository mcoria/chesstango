package net.chesstango.board.representations.syzygy;

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
            String tableName = String.format("K%cvK", SyzygyConstants.pchr(i));
            init_tb(tableName);
        }

        TB_LARGEST = TB_MaxCardinality;
        if (TB_MaxCardinalityDTM > TB_LARGEST) {
            TB_LARGEST = TB_MaxCardinalityDTM;
        }
    }

    public void probeTable(BitPosition bitPosition) {
        long key = calcKey(bitPosition);
        int idx = (int) (key >>> (64 - TB_HASHBITS));
    }

    void init_tb(String tbName) {
        int[] pcs = tableName_to_pcs(tbName);

        long key = calc_key_from_pcs(pcs, false);

        long key2 = calc_key_from_pcs(pcs, true);

        boolean hasPawns = (pcs[Piece.W_PAWN.getValue()] | pcs[Piece.B_PAWN.getValue()]) != 0;

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
            be.pawns[0] = (char) pcs[Piece.W_PAWN.getValue()];
            be.pawns[1] = (char) pcs[Piece.B_PAWN.getValue()];
            if (pcs[Piece.B_PAWN.getValue()] != 0 && (pcs[Piece.W_PAWN.getValue()] != 0 || (pcs[Piece.W_PAWN.getValue()] > pcs[Piece.B_PAWN.getValue()]))) {
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

    boolean test_tb(String fileName, String suffix) {
        Path path = Paths.get(this.path, String.format("%s%s", fileName, suffix));
        if (!path.toFile().exists()) {
            //System.out.println("File not found: " + path);
            return false;
        }
        return true;
    }
}
