package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class PawnSymmetric extends TableBase {
    final PawnEntry pawnEntry;

    PawnSymmetric(PawnEntry pawnEntry, TableType table) {
        super(table);
        this.pawnEntry = pawnEntry;
    }

    @Override
    PawnEntry getBaseEntry() {
        return pawnEntry;
    }

    @Override
    boolean init_table_imp() {
        return false;
    }

    @Override
    int probe_table_imp(BitPosition bitPosition, long key) {
        return 0;
    }
}
