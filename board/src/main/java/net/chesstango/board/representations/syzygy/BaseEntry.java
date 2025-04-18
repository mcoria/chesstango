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

    TableBase wdl;
    TableBase dtm;
    TableBase dtz;

    boolean symmetric;

    boolean dtmLossOnly;

    BaseEntry(Syzygy syzygy) {
        this.syzygy = syzygy;
    }

    abstract TableBase createTable(TableType tableType);

    abstract void init_tb(int[] pcs);

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

        init_tb(pcs);

        // Update maximum cardinality values
        if (this.num > syzygy.TB_MaxCardinality) {
            this.syzygy.TB_MaxCardinality = this.num;
        }

        if (test_tb(this.syzygy.path, tbName, WDL.getSuffix())) {
            this.wdl = createTable(WDL);
            if(this.wdl.init_table()){
                this.syzygy.numWdl++;
            }
        }

        if (test_tb(this.syzygy.path, tbName, DTM.getSuffix())) {
            this.dtm = createTable(DTM);
            if(this.dtm.init_table()){
                this.syzygy.numDtm++;
                if (this.num > syzygy.TB_MaxCardinalityDTM) {
                    this.syzygy.TB_MaxCardinalityDTM = this.num;
                }
            }
        }

        if (test_tb(syzygy.path, tbName, DTZ.getSuffix())) {
            this.dtz = createTable(DTZ);
            if(this.dtz.init_table()){
                this.syzygy.numDtz++;
            }
        }

        // Add the entry to the hash table using the calculated keys
        this.syzygy.add_to_hash(this, key);
        if (key != key2) {
            this.syzygy.add_to_hash(this, key2);
        }
    }

    int probe_table(long key, TableType type) {
        return switch (type) {
            case WDL -> wdl.probe_table(key);
            case DTM, DTZ -> 0;
            default -> throw new IllegalArgumentException("Unexpected value: " + type);
        };
    }

}
