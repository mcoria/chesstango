package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class PieceEntry extends BaseEntry {
    boolean kk_enc;

    BytePTR dtzMap;
    byte dtzFlags;

    short[] dtzMapIdx = new short[4];

    PieceEntry(Syzygy syzygy) {
        super(syzygy);
    }

    @Override
    protected TableBase createTable(TableType tableType) {
        if (!symmetric && tableType == TableType.WDL)
            return new PieceAsymmetricWdl(this);

        if (!symmetric && tableType == TableType.DTZ)
            return new PieceAsymmetricDtz(this);

        return new PieceSymmetric(this, tableType);
    }

    @Override
    protected void init_tb(int[] pcs) {
        syzygy.pieceEntry[syzygy.tbNumPiece++] = this;
        int j = 0;
        for (int i = 0; i < 16; i++) {
            if (pcs[i] == 1) j++;
        }
        this.kk_enc = j == 2;
    }

}
