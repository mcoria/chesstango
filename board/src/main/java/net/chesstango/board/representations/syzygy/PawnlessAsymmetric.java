package net.chesstango.board.representations.syzygy;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.Encoding.PIECE_ENC;
import static net.chesstango.board.representations.syzygy.TableType.DTZ;

/**
 * @author Mauricio Coria
 */
class PawnlessAsymmetric extends TableData {

    public PawnlessAsymmetric(PieceEntry pieceEntry, TableType tableType) {
        super(pieceEntry, tableType);
    }

    @Override
    boolean init_table_imp() {
        boolean split = table != DTZ;

        int dataPtr = 5;

        int[][] tb_size = new int[6][2];

        int num = baseEntry.num_tables(table);

        BaseEntry.EncInfo[] ei = baseEntry.first_ei(table);

        SyzygyConstants.Encoding enc = PIECE_ENC;


        for (int t = 0; t < num; t++) {
            tb_size[t][0] = init_enc_info(ei[t], dataPtr, 0, t, enc);
            if (split)
                tb_size[t][1] = init_enc_info(ei[num + t], dataPtr, 4, t, enc);

            //dataPtr += be.num + 1 + (be.hasPawns() && be.pawns[1] != 0);
        }

        return true;
    }
}
