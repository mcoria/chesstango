package net.chesstango.board.representations.syzygy;

import static net.chesstango.board.representations.syzygy.TableBase.TableType.DTZ;

/**
 * @author Mauricio Coria
 */
class PawnDtz extends TableBase {
    final PawnEntry pieceEntry;

    final PawnEncInfo ei_dtz;

    byte dtzFlags;
    U_INT8_PTR dtzMap;
    short[] dtzMapIdx = new short[4];

    public PawnDtz(PawnEntry pieceEntry) {
        super(DTZ, pieceEntry);
        this.pieceEntry = pieceEntry;
        this.ei_dtz = new PawnEncInfo(pieceEntry);
    }

    @Override
    boolean init_table_imp() {
        return true;
    }

    @Override
    int probe_table_imp(BitPosition pos, long key, int score) {
        return 0;
    }

}
