package net.chesstango.board.representations.syzygy;

import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.*;
import static net.chesstango.board.representations.syzygy.SyzygyConstants.Table.DTM;
import static net.chesstango.board.representations.syzygy.SyzygyConstants.Table.DTZ;
import static net.chesstango.board.representations.syzygy.SyzygyConstants.test_tb;

/**
 * @author Mauricio Coria
 */
abstract class BaseEntry {
    final Syzygy syzygy;

    String tableName;

    long key;

    char num;

    TableData[] data = new TableData[3];
    boolean[] ready = new boolean[3];

    boolean symmetric;
    boolean hasDtm;
    boolean hasDtz;

    boolean kk_enc;
    char[] pawns = new char[2];

    boolean dtmLossOnly;

    BaseEntry(Syzygy syzygy) {
        this.syzygy = syzygy;
    }

    protected abstract void init_tb(int[] pcs);

    abstract boolean hasPawns();

    abstract int num_tables(Table type);

    abstract EncInfo[] first_ei(Table type);


    public void init_tb(String tbName) {
        this.tableName = tbName;

        // Convert the table name into an array of piece counts
        int[] pcs = tableName_to_pcs(tbName);

        // Calculate unique keys for the tablebase
        long key = calc_key_from_pcs(pcs, false);
        long key2 = calc_key_from_pcs(pcs, true);

        // Set attributes for the BaseEntry
        this.key = key;
        this.symmetric = key == key2;
        this.num = 0;
        for (int i = 0; i < 16; i++) {
            this.num += (char) pcs[i];
        }

        // Update global counters for WDL, DTM, and DTZ tablebases
        syzygy.numWdl++;
        if (test_tb(syzygy.path, tbName, DTM.getSuffix())) {
            syzygy.numDtm++;
            this.hasDtm = true;
        }
        if (test_tb(syzygy.path, tbName, DTZ.getSuffix())) {
            syzygy.numDtz++;
            this.hasDtz = true;
        }

        // Update maximum cardinality values
        if (this.num > syzygy.TB_MaxCardinality) {
            syzygy.TB_MaxCardinality = this.num;
        }
        if (this.hasDtm && this.num > syzygy.TB_MaxCardinalityDTM) {
            syzygy.TB_MaxCardinalityDTM = this.num;
        }

        init_tb(pcs);

        // Add the entry to the hash table using the calculated keys
        syzygy.add_to_hash(this, key);
        if (key != key2) {
            syzygy.add_to_hash(this, key2);
        }
    }


    record TableData(FileChannel channel, MappedByteBuffer buffer) {
        int read_le_u32() {
            return buffer.getInt();
        }

        public byte read_uint8_t(int idx) {
            return buffer.get(idx);
        }
    }

    static class EncInfo{}
}
