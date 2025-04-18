package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class PieceSymmetric extends TableBase {
    public PieceSymmetric(PieceEntry pieceEntry, TableType tableType) {
        super(pieceEntry, tableType);
    }

    @Override
    boolean init_table_imp() {
        return false;
    }
}
