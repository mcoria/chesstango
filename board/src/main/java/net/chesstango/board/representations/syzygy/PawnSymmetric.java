package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class PawnSymmetric extends TableBase {
    PawnSymmetric(BaseEntry baseEntry, TableType table) {
        super(baseEntry, table);
    }

    @Override
    boolean init_table_imp() {
        return false;
    }
}
