package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
abstract class TableBase {
    final TableType table;
    final MappedFile mappedFile;

    boolean ready;
    boolean error;

    abstract BaseEntry getBaseEntry();
    abstract boolean init_table_imp();

    public TableBase(TableType table) {
        this.table = table;
        this.mappedFile = new MappedFile();
    }

    boolean init_table() {
        boolean result = false;

        BaseEntry baseEntry = getBaseEntry();

        if (!ready && mappedFile.map_tb(baseEntry.syzygy.path, baseEntry.tableName, table.getSuffix())) {

            /**
             * The main header of the tablebases file:
             * bytes 0-3: magic number
             */
            int magicNumber = mappedFile.read_le_u32(0);

            if (magicNumber == table.getMagicNumber()) {
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
                assert baseEntry instanceof PawnEntry && pawnfulTable || baseEntry instanceof PieceEntry && !pawnfulTable : "File name doesn't match header description";
            }
            result = init_table_imp();
        }
        error = !result;
        ready = true;
        return result;
    }

    public int probe_table(long key) {
        if (!ready || error) {
            return 0;
        }

        return 0;
    }

    static class EncInfo {
        long[] factor = new long[SyzygyConstants.TB_PIECES];
        byte[] pieces = new byte[SyzygyConstants.TB_PIECES];
        byte[] norm = new byte[SyzygyConstants.TB_PIECES];
    }
}
