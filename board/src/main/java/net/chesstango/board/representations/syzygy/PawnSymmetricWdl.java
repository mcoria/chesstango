package net.chesstango.board.representations.syzygy;

import static net.chesstango.board.representations.syzygy.TableBase.TableType.WDL;

/**
 * @author Mauricio Coria
 */
class PawnSymmetricWdl extends TableBase {
    final PawnEntry pawnEntry;

    PawnSymmetricWdl(PawnEntry pawnEntry) {
        super(WDL, pawnEntry);
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
