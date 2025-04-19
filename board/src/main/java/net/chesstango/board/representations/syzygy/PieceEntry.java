package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class PieceEntry extends BaseEntry {
    boolean kk_enc;
    byte dtzFlags;

    PieceEntry(Syzygy syzygy) {
        super(syzygy);
    }

    @Override
    protected TableBase createTable(TableType tableType) {
        return this.symmetric ? new PieceSymmetric(this, tableType) : new PieceAsymmetric(this, tableType);
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
