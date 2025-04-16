package net.chesstango.board.representations.syzygy;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.Table.DTM;
import static net.chesstango.board.representations.syzygy.SyzygyConstants.Table.WDL;

/**
 * @author Mauricio Coria
 */
class PieceEntry extends BaseEntry{
    EncInfo[] ei = new EncInfo[5]; // 2 + 2 + 1

    @Override
    int num_tables(SyzygyConstants.Table type) {
        return 1;
    }

    @Override
    EncInfo first_ei(SyzygyConstants.Table type) {
        return ei[WDL.equals(type) ? 0 : DTM.equals(type) ? 2 : 4];
    }
}
