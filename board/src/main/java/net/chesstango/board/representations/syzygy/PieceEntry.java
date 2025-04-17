package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class PieceEntry extends BaseEntry {
    EncInfo[] eiWDL = new EncInfo[2];
    EncInfo[] eiDTM = new EncInfo[2];
    EncInfo[] eiDTZ = new EncInfo[1];
    boolean kk_enc;

    PieceEntry(Syzygy syzygy) {
        super(syzygy);
    }

    @Override
    protected TableData createTable(TableType tableType) {
        return this.symmetric ? new PawnlessSymmetric(this, tableType) : new PawnlessAsymmetric(this, tableType);
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
    int num_tables(TableType type) {
        return 1;
    }

    @Override
    EncInfo[] first_ei(TableType type) {
        return switch (type) {
            case WDL -> eiWDL;
            case DTM -> eiDTM;
            case DTZ -> eiDTZ;
            default -> throw new IllegalArgumentException("Unexpected value: " + type);
        };
    }
}
