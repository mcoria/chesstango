package net.chesstango.board.representations.syzygy;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.TB_PIECES;
import static net.chesstango.board.representations.syzygy.TableBase.TableType.WDL;

/**
 * @author Mauricio Coria
 */
class PawnSymmetricWdl extends TableBase {
    final PawnEntry pawnEntry;
    final PawnEncInfo[] ei_wtm;

    PawnSymmetricWdl(PawnEntry pawnEntry) {
        super(WDL, pawnEntry);
        this.pawnEntry = pawnEntry;
        this.ei_wtm = new PawnEncInfo[4];
    }

    @Override
    boolean init_table_imp() {
        U_INT8_PTR data = new U_INT8_PTR(mappedFile);
        data.incPtr(5);

        final int NUM = 4;

        int[] tb_size = new int[NUM];
        for (int i = 0; i < NUM; i++) {
            ei_wtm[i] = new PawnEncInfo(pawnEntry);
            tb_size[i] = ei_wtm[i].init_enc_info(data, 0, i);
            data.incPtr(pawnEntry.num + 1 + (pawnEntry.pawns[1] > 0 ? 1 : 0));
        }

        // Next, there may be a padding byte to align the position within the tablebase file to a multiple of 2 bytes.
        data.ptr += data.ptr & 1;

        int[][] size = new int[NUM][3];
        for (int t = 0; t < NUM; t++) {
            ei_wtm[t].precomp = new PairsData(WDL, data, tb_size[t], size[t]);
        }

        // indexTable ptr
        for (int t = 0; t < NUM; t++) {
            ei_wtm[t].precomp.indexTable = data.clone();
            data.incPtr(size[t][0]);
        }

        // sizeTable ptr
        for (int t = 0; t < NUM; t++) {
            ei_wtm[t].precomp.sizeTable = data.createU_INT16_PTR(0);
            data.incPtr(size[t][1]);
        }

        // data ptr
        for (int t = 0; t < NUM; t++) {
            data.ptr = (data.ptr + 0x3f) & ~0x3f;
            ei_wtm[t].precomp.data = data.clone();
            data.incPtr(size[t][2]);
        }

        return true;
    }

    @Override
    int probe_table_imp(BitPosition pos, long key, int score) {
        boolean flip = !pos.turn;

        int[] p = new int[TB_PIECES];

        PawnEncInfo ei = ei_wtm[0];

        int i = ei.fill_squares(pos, flip, flip ? 0x38 : 0, p, 0);
        int t = ei.leading_pawn(p);

        ei = ei_wtm[t];

        while (i < pawnEntry.num) {
            i = ei.fill_squares(pos, flip, flip ? 0x38 : 0, p, i);
        }

        int idx = ei.encode_pawn(p);

        byte[] w = ei.precomp.decompress_pairs(idx);

        return (int) w[0] - 2;
    }
}
