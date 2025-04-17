package net.chesstango.board.representations.syzygy;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.Table.DTM;

/**
 * @author Mauricio Coria
 */
class PawnEntry extends BaseEntry {
    EncInfo[] eiWDL = new EncInfo[8];
    EncInfo[] eiDTM = new EncInfo[12];
    EncInfo[] eiDTZ = new EncInfo[4];

    @Override
    boolean hasPawns() {
        return true;
    }

    @Override
    int num_tables(SyzygyConstants.Table type) {
        return DTM.equals(type) ? 6 : 4;
    }

    @Override
    EncInfo[] first_ei(SyzygyConstants.Table type) {
        return switch (type) {
            case WDL -> eiWDL;
            case DTM -> eiDTM;
            case DTZ -> eiDTZ;
            default -> throw new IllegalArgumentException("Unexpected value: " + type);
        };
    }
}
