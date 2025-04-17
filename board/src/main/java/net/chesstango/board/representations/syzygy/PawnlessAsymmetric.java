package net.chesstango.board.representations.syzygy;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.Encoding.PIECE_ENC;

/**
 * @author Mauricio Coria
 */
class PawnlessAsymmetric extends TableBase {
    final EncInfo[] ei;

    public PawnlessAsymmetric(PieceEntry pieceEntry, TableType tableType) {
        super(pieceEntry, tableType);
        this.ei = new EncInfo[tableType.getEcInfoSizePawnless()];
    }

    @Override
    boolean init_table_imp() {

        int dataPtr = 5;

        int[][] tb_size = new int[6][2];

        int num = 1;

        SyzygyConstants.Encoding enc = PIECE_ENC;

        for (int t = 0; t < num; t++) {
            tb_size[t][0] = init_enc_info(ei[t], dataPtr, 0, t, enc);
        }

        return true;
    }
}
