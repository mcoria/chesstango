package net.chesstango.board.representations.syzygy;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.Encoding.PIECE_ENC;
import static net.chesstango.board.representations.syzygy.TableType.DTZ;

/**
 * @author Mauricio Coria
 */
class PieceAsymmetric extends TableBase {
    final EncInfo[] ei;
    final PieceEntry pieceEntry;

    public PieceAsymmetric(PieceEntry pieceEntry, TableType tableType) {
        super(tableType);
        this.pieceEntry = pieceEntry;
        this.ei = new EncInfo[tableType.getEcInfoSizePiece()];
        for (int i = 0; i < ei.length; i++) {
            ei[i] = new EncInfo();
        }
    }

    @Override
    PieceEntry getBaseEntry() {
        return pieceEntry;
    }

    @Override
    boolean init_table_imp() {
        BytePTR bytePTR = new BytePTR(mappedFile);
        bytePTR.ptr = 0;

        boolean split = DTZ != table;

        long[][] tb_size = new long[1][2];

        SyzygyConstants.Encoding enc = PIECE_ENC;

        tb_size[0][0] = init_enc_info(ei[0], bytePTR, 0);
        if(split) {
            tb_size[0][1] = init_enc_info(ei[1], bytePTR, 4);
        }

        bytePTR.incPtr(pieceEntry.num + 1);

        return true;
    }

    long init_enc_info(EncInfo ei, BytePTR bytePTR, int shift) {
        for (int i = 0; i < pieceEntry.num; i++) {
            ei.pieces[i] = (byte) ((bytePTR.read_uint8_t(i + 1) >> shift) & 0x0f);
            ei.norm[i] = 0;
        }

        int order = (bytePTR.read_uint8_t(0) >> shift) & 0x0f;
        int order2 = 0x0f;

        return 0;
    }

}
