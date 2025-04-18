package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class PawnAsymmetric extends TableBase {
    PawnAsymmetric(BaseEntry baseEntry, TableType table) {
        super(baseEntry, table);
    }

    @Override
    boolean init_table_imp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
