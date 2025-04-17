package net.chesstango.board.representations.syzygy;

abstract class TableData {
    final BaseEntry baseEntry;
    final TableType table;
    final TableFile tableFile;

    boolean ready;
    boolean error;

    abstract boolean init_table_imp();

    public TableData(BaseEntry baseEntry, TableType table) {
        this.baseEntry = baseEntry;
        this.table = table;
        this.tableFile = new TableFile();
    }

    public int probe_table(long key) {
        if (!ready) {
            if (!error && !init_table()) {
                error = true;
                return 0;
            }
            ready = true;
        }
        return 0;
    }

    boolean init_table() {
        if (tableFile.map_tb(baseEntry.syzygy.path, baseEntry.tableName, table.getSuffix())) {

            /**
             * The main header of the tablebases file:
             * bytes 0-3: magic number
             */
            int magicNumber = tableFile.read_le_u32(0);

            if (magicNumber != table.getMagicNumber()) {
                return false;
            }

            /**
             * byte 4:
             *      bit 0 is set for a non-symmetric table, i.e. separate wtm and btm.
             *      bit 1 is set for a pawnful table.
             *      bits 4-7: number of pieces N (N=5 for KRPvKR)
             */
            byte byte4 = tableFile.read_uint8_t(4);

            boolean nonSymmetric = (byte4 & 0b00000001) != 0; //bit 0 is set for a non-symmetric table, i.e. separate wtm and btm.
            boolean pawnfulTable = (byte4 & 0b00000010) != 0; //bit 1 is set for a pawnful table.
            int numPieces = byte4 >>> 4;

            assert baseEntry.symmetric == !nonSymmetric : "baseEntry.symmetric: " + baseEntry.symmetric + " != nonSymmetric: " + nonSymmetric;
            assert baseEntry.num == numPieces : "baseEntry.num: " + baseEntry.num + " != numPieces: " + numPieces;
            assert baseEntry instanceof PawnEntry && pawnfulTable : "baseEntry: " + baseEntry + " != pawnfulTable: " + pawnfulTable;

            return init_table_imp();
        }

        return false;
    }

    int init_enc_info(BaseEntry.EncInfo encInfo, long dataPtr, int i, int t, SyzygyConstants.Encoding enc) {
        return 0;
    }

}
