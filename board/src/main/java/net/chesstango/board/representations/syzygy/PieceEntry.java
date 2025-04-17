package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class PieceEntry extends BaseEntry {
    EncInfo[] eiWDL = new EncInfo[2];
    EncInfo[] eiDTM = new EncInfo[2];
    EncInfo[] eiDTZ = new EncInfo[1];

    PieceEntry(Syzygy syzygy) {
        super(syzygy);
    }

    @Override
    protected void init_tb(int[] pcs) {
        syzygy.pieceEntry[syzygy.tbNumPiece++] = this;
        int j = 0;
        for (int i = 0; i < 16; i++)
            if (pcs[i] == 1) j++;
        this.kk_enc = j == 2;
    }

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
