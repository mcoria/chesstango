package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
abstract class TableBase {
    final BaseEntry baseEntry;
    final TableType table;
    final MappedFile mappedFile;

    boolean ready;
    boolean error;

    abstract boolean init_table_imp();

    public TableBase(BaseEntry baseEntry, TableType table) {
        this.baseEntry = baseEntry;
        this.table = table;
        this.mappedFile = new MappedFile();
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
        if (mappedFile.map_tb(baseEntry.syzygy.path, baseEntry.tableName, table.getSuffix())) {

            /**
             * The main header of the tablebases file:
             * bytes 0-3: magic number
             */
            int magicNumber = mappedFile.read_le_u32(0);

            if (magicNumber != table.getMagicNumber()) {
                return false;
            }

            /**
             * byte 4:
             *      bit 0 is set for a non-symmetric table, i.e. separate wtm and btm.
             *      bit 1 is set for a pawnful table.
             *      bits 4-7: number of pieces N (N=5 for KRPvKR)
             */
            byte byte4 = mappedFile.read_uint8_t(4);

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

    int init_enc_info(EncInfo encInfo, long dataPtr, int i, int t, SyzygyConstants.Encoding enc) {
        return 0;
    }

    static class EncInfo {
    }
}
