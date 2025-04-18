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
        bytePTR.ptr = 5;

        boolean split = DTZ != tableType;

        long[][] tb_size = new long[1][2];

        SyzygyConstants.Encoding enc = PIECE_ENC;

        tb_size[0][0] = init_enc_info(ei[0], bytePTR, 0);
        if (split) {
            tb_size[0][1] = init_enc_info(ei[1], bytePTR, 4);
        }

        bytePTR.incPtr(pieceEntry.num + 1);

        return true;
    }

    long init_enc_info(EncInfo ei, BytePTR bytePTR, int shift) {
        for (int i = 0; i < pieceEntry.num; i++) {
            ei.pieces[i] = (byte) ((bytePTR.read_uint8_t(i + 1) >>> shift) & 0x0f);
            ei.norm[i] = 0;
        }

        int order = (bytePTR.read_uint8_t(0) >>> shift) & 0x0f;
        int order2 = 0x0f;

        int k = pieceEntry.kk_enc ? 2 : 3;
        ei.norm[0] = (byte) k;

        for (int i = k; i < pieceEntry.num; i += ei.norm[i])
            for (int j = i; j < pieceEntry.num && ei.pieces[j] == ei.pieces[i]; j++)
                ei.norm[i]++;

        int n = 64 - k;
        long f = 1;

        for (int i = 0; k < pieceEntry.num || i == order || i == order2; i++) {
            if (i == order) {
                ei.factor[0] = f;
                f *= pieceEntry.kk_enc ? 462 : 31332;
            } else if (i == order2) {
                ei.factor[ei.norm[0]] = f;
                f *= subfactor(ei.norm[ei.norm[0]], 48 - ei.norm[0]);
            } else {
                ei.factor[k] = f;
                f *= subfactor(ei.norm[k], n);
                n -= ei.norm[k];
                k += ei.norm[k];
            }
        }
        return f;
    }

    // Count number of placements of k like pieces on n squares
    static long subfactor(long k, long n) {
        long f = n;
        long l = 1;
        for (long i = 1; i < k; i++) {
            f *= n - i;
            l *= i + 1;
        }
        return f / l;
    }

}
