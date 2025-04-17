package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class PieceEntry extends BaseEntry {
    EncInfo[] eiWDL = new EncInfo[2];
    EncInfo[] eiDTM = new EncInfo[2];
    EncInfo[] eiDTZ = new EncInfo[1];

    @Override
    boolean hasPawns() {
        return false;
    }

    @Override
    int num_tables(SyzygyConstants.Table type) {
        return 1;
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
