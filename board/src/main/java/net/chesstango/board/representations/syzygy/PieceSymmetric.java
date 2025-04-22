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
        return false;
    }

    @Override
    int probe_table_imp(BitPosition pos, long key) {
        return 0;
    }
}
