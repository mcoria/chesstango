package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class PieceEntry extends BaseEntry {
    boolean kk_enc;

    PieceEntry(Syzygy syzygy) {
        super(syzygy);
    }

    @Override
    protected TableBase createTable(TableBase.TableType tableType) {
        if (tableType == TableBase.TableType.WDL) {
            if (symmetric)
                return new PieceSymmetricWdl(this);
            else
                return new PieceAsymmetricWdl(this);
        }

        if (tableType == TableBase.TableType.DTZ) {
            return new PieceDtz(this);
        }

        throw new UnsupportedOperationException("Not supported yet.");
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
