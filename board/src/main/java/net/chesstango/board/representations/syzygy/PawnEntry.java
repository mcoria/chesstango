package net.chesstango.board.representations.syzygy;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.Table.DTM;
import static net.chesstango.board.representations.syzygy.SyzygyConstants.Table.WDL;

/**
 * @author Mauricio Coria
 */
class PawnEntry extends BaseEntry {

    EncInfo[] ei = new EncInfo[24]; // 4 * 2 + 6 * 2 + 4

    @Override
    boolean hasPawns() {
        return true;
    }

    @Override
    int num_tables(SyzygyConstants.Table type) {
        return DTM.equals(type) ? 6 : 4;
    }

    @Override
    EncInfo first_ei(SyzygyConstants.Table type) {
        return ei[WDL.equals(type) ? 0 : DTM.equals(type) ? 8 : 20];
    }
}
