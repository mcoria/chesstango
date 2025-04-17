package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class PawnlessSymmetric extends TableBase {
    public PawnlessSymmetric(PieceEntry pieceEntry, TableType tableType) {
        super(pieceEntry, tableType);
    }

    @Override
    boolean init_table_imp() {
        return false;
    }
}
