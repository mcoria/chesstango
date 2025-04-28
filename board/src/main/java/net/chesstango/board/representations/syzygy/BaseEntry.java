package net.chesstango.board.representations.syzygy;

import static net.chesstango.board.representations.syzygy.MappedFile.test_tb;
import static net.chesstango.board.representations.syzygy.SyzygyConstants.*;
import static net.chesstango.board.representations.syzygy.SyzygyConstants.Piece.*;
import static net.chesstango.board.representations.syzygy.TableType.*;

/**
 * @author Mauricio Coria
 */
abstract class BaseEntry {
    final Syzygy syzygy;

    String tableName;

    long key;
    int num;

    boolean symmetric;
    boolean dtmLossOnly;

    TableBase wdl;
    TableBase dtm;
    TableBase dtz;

    abstract TableBase createTable(TableType tableType);

    abstract void init_tb(int[] pcs);

    BaseEntry(Syzygy syzygy) {
        this.syzygy = syzygy;
    }

    void init_tb(String tbName) {
        this.tableName = tbName;

        // Convert the tableType name into an array of piece counts
        int[] pcs = tableName_to_pcs(tbName);

        // Calculate unique keys for the tablebase
        long key = calc_key_from_pcs(pcs, false);
        long keyMirror = calc_key_from_pcs(pcs, true);

        // Set attributes for the BaseEntry
        this.key = key;
        this.symmetric = key == keyMirror;
        this.num = 0;
        for (int i = 0; i < 16; i++) {
            this.num += pcs[i];
        }

        init_tb(pcs);

        // Update maximum cardinality values
        if (this.num > syzygy.TB_MaxCardinality) {
            this.syzygy.TB_MaxCardinality = this.num;
        }

        if (test_tb(this.syzygy.path, tbName, WDL.getSuffix())) {
            this.wdl = createTable(WDL);
            if (this.wdl.init_table()) {
                this.syzygy.numWdl++;
            }
        }

        if (test_tb(this.syzygy.path, tbName, DTM.getSuffix())) {
            this.dtm = createTable(DTM);
            if (this.dtm.init_table()) {
                this.syzygy.numDtm++;
                if (this.num > syzygy.TB_MaxCardinalityDTM) {
                    this.syzygy.TB_MaxCardinalityDTM = this.num;
                }
            }
        }

        if (test_tb(syzygy.path, tbName, DTZ.getSuffix())) {
            this.dtz = createTable(DTZ);
            if (this.dtz.init_table()) {
                this.syzygy.numDtz++;
            }
        }

        // Add the entry to the hash tableType using the calculated keys
        this.syzygy.add_to_hash(this, key);
        if (key != keyMirror) {
            this.syzygy.add_to_hash(this, keyMirror);
        }
    }


    int probe_wdl(BitPosition bitPosition, long key, int s) {
        return wdl.probe_table(bitPosition, key, s);
    }

    int probe_dtz(BitPosition bitPosition, long key, int s) {
        return dtz.probe_table(bitPosition, key, s);
    }

    static long calc_key_from_pcs(int[] pcs, boolean mirror) {
        int theMirror = (mirror ? 8 : 0);
        return pcs[WHITE_QUEEN ^ theMirror] * W_QUEEN.getPrime() +
                pcs[WHITE_ROOK ^ theMirror] * W_ROOK.getPrime() +
                pcs[WHITE_BISHOP ^ theMirror] * W_BISHOP.getPrime() +
                pcs[WHITE_KNIGHT ^ theMirror] * W_KNIGHT.getPrime() +
                pcs[WHITE_PAWN ^ theMirror] * W_PAWN.getPrime() +
                pcs[BLACK_QUEEN ^ theMirror] * B_QUEEN.getPrime() +
                pcs[BLACK_ROOK ^ theMirror] * B_ROOK.getPrime() +
                pcs[BLACK_BISHOP ^ theMirror] * B_BISHOP.getPrime() +
                pcs[BLACK_KNIGHT ^ theMirror] * B_KNIGHT.getPrime() +
                pcs[BLACK_PAWN ^ theMirror] * B_PAWN.getPrime();
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
}
