package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class PawnAsymmetricWdl extends TableBase {
    final PawnEntry pawnEntry;

    PawnAsymmetricWdl(PawnEntry pawnEntry) {
        super(TableType.WDL, pawnEntry);
        this.pawnEntry = pawnEntry;
    }

    @Override
    boolean init_table_imp() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    int probe_table_imp(BitPosition pos, long key, int score) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
