package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class PieceSymmetric extends TableBase {
    final PieceEntry pieceEntry;

    public PieceSymmetric(PieceEntry pieceEntry, TableType tableType) {
        super(tableType);
        this.pieceEntry = pieceEntry;
    }

    @Override
    PieceEntry getBaseEntry() {
        return pieceEntry;
    }

    @Override
    boolean init_table_imp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    int probe_table_imp(BitPosition pos, long key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
