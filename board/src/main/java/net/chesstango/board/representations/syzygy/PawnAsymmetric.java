package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class PawnAsymmetric extends TableBase {
    final PawnEntry pawnEntry;

    PawnAsymmetric(PawnEntry pawnEntry, TableType table) {
        super(table);
        this.pawnEntry = pawnEntry;
    }

    @Override
    PawnEntry getBaseEntry() {
        return pawnEntry;
    }

    @Override
    boolean init_table_imp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    int probe_table_imp(BitPosition pos, long key) {
        return 0;
    }
}
