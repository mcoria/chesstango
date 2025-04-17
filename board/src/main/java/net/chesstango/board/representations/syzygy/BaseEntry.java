package net.chesstango.board.representations.syzygy;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.*;
import static net.chesstango.board.representations.syzygy.TableType.*;

/**
 * @author Mauricio Coria
 */
abstract class BaseEntry {
    final Syzygy syzygy;

    String tableName;

    long key;

    char num;

    TableData wdl;
    TableData dtm;
    TableData dtz;


    boolean symmetric;
    boolean hasDtm;
    boolean hasDtz;

    boolean dtmLossOnly;

    BaseEntry(Syzygy syzygy) {
        this.syzygy = syzygy;
    }

    protected abstract TableData createTable(TableType tableType);

    protected abstract void init_tb(int[] pcs);


    abstract int num_tables(TableType type);

    abstract EncInfo[] first_ei(TableType type);


    void init_tb(String tbName) {
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
        this.syzygy.numWdl++;
        this.wdl = createTable(WDL);
        if (test_tb(this.syzygy.path, tbName, DTM.getSuffix())) {
            this.syzygy.numDtm++;
            this.hasDtm = true;
        }

        if (test_tb(syzygy.path, tbName, DTZ.getSuffix())) {
            this.syzygy.numDtz++;
            this.hasDtz = true;
        }

        // Update maximum cardinality values
        if (this.num > syzygy.TB_MaxCardinality) {
            this.syzygy.TB_MaxCardinality = this.num;
        }
        if (this.hasDtm && this.num > syzygy.TB_MaxCardinalityDTM) {
            this.syzygy.TB_MaxCardinalityDTM = this.num;
        }

        init_tb(pcs);

        // Add the entry to the hash table using the calculated keys
        this.syzygy.add_to_hash(this, key);
        if (key != key2) {
            this.syzygy.add_to_hash(this, key2);
        }
    }

    public int probe_table(long key, TableType type) {
        if (DTM == type && !hasDtm || DTZ == type && !hasDtz) {
            return 0;
        }
        return switch (type) {
            case WDL -> wdl.probe_table(key);
            case DTM -> 0;
            case DTZ -> 0;
            default -> throw new IllegalArgumentException("Unexpected value: " + type);
        };
    }



    static class EncInfo {
    }
}
