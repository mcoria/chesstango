package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
abstract class TableBase {
    final TableType tableType;
    final MappedFile mappedFile;

    boolean ready;
    boolean error;

    abstract BaseEntry getBaseEntry();

    abstract boolean init_table_imp();

    abstract int probe_table_imp(BitPosition pos, long key, int s);

    public TableBase(TableType tableType) {
        this.tableType = tableType;
        this.mappedFile = new MappedFile();
    }

    boolean init_table() {
        boolean init_success = false;

        BaseEntry baseEntry = getBaseEntry();

        if (!ready && mappedFile.map_tb(baseEntry.syzygy.path, baseEntry.tableName, tableType.getSuffix())) {

            /**
             * The main header of the tablebases file:
             * bytes 0-3: magic number
             */
            int magicNumber = mappedFile.read_le_u32(0);

            if (magicNumber == tableType.getMagicNumber()) {
                /**
                 * byte 4:
                 *      bit 0 is set for a non-symmetric tableType, i.e. separate wtm and btm.
                 *      bit 1 is set for a pawnful tableType.
                 *      bits 4-7: number of pieces N (N=5 for KRPvKR)
                 */
                byte byte4 = mappedFile.read_uint8_t(4);

                boolean nonSymmetric = (byte4 & 0b00000001) != 0; //bit 0 is set for a non-symmetric tableType, i.e. separate wtm and btm.
                boolean pawnfulTable = (byte4 & 0b00000010) != 0; //bit 1 is set for a pawnful tableType.
                int numPieces = byte4 >>> 4;

                assert baseEntry.symmetric == !nonSymmetric : "baseEntry.symmetric: " + baseEntry.symmetric + " != nonSymmetric: " + nonSymmetric;
                assert baseEntry.num == numPieces : "baseEntry.num: " + baseEntry.num + " != numPieces: " + numPieces;
                assert baseEntry instanceof PawnEntry && pawnfulTable || baseEntry instanceof PieceEntry && !pawnfulTable : "File name doesn't match header description";

                init_success = init_table_imp();
            }
        }
        error = !init_success;
        ready = true;
        return init_success;
    }

    int probe_table(BitPosition pos, long key, int s) {
        BaseEntry baseEntry = getBaseEntry();
        if (!ready || error) {
            //baseEntry.syzygy.success = 0;
            //return 0;
            throw new IllegalStateException("TableBase not ready or error");
        }

        return probe_table_imp(pos, key, s);
    }
}
