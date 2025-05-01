package net.chesstango.board.representations.syzygy;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.TB_PIECES;
import static net.chesstango.board.representations.syzygy.TableBase.TableType.WDL;

/**
 * @author Mauricio Coria
 */
class PawnAsymmetricWdl extends TableBase {
    final PawnEntry pawnEntry;
    final PawnEncInfo[] ei_wtm;
    final PawnEncInfo[] ei_btm;

    PawnAsymmetricWdl(PawnEntry pawnEntry) {
        super(TableType.WDL, pawnEntry);
        this.pawnEntry = pawnEntry;
        this.ei_wtm = new PawnEncInfo[4];
        this.ei_btm = new PawnEncInfo[4];
    }

    @Override
    boolean init_table_imp() {
        U_INT8_PTR data = new U_INT8_PTR(mappedFile);
        data.incPtr(5);

        final int NUM = 4;

        int[] tb_size_white = new int[NUM];
        int[] tb_size_black = new int[NUM];

        for (int t = 0; t < NUM; t++) {
            ei_wtm[t] = new PawnEncInfo(pawnEntry);
            ei_btm[t] = new PawnEncInfo(pawnEntry);
            tb_size_white[t] = ei_wtm[t].init_enc_info(data, 0, t);
            tb_size_black[t] = ei_btm[t].init_enc_info(data, 4, t);
            data.incPtr(pawnEntry.num + 1 + pawnEntry.pawns[1]);
        }

        // Next, there may be a padding byte to align the position within the tablebase file to a multiple of 2 bytes.
        data.ptr += data.ptr & 1;

        int[][] size_white = new int[NUM][3];
        int[][] size_black = new int[NUM][3];
        for (int t = 0; t < NUM; t++) {
            ei_wtm[t].precomp = new PairsData(WDL, data, tb_size_white[t], size_white[t]);
            ei_btm[t].precomp = new PairsData(WDL, data, tb_size_black[t], size_black[t]);
        }

        // indexTable ptr
        for (int t = 0; t < NUM; t++) {
            ei_wtm[t].precomp.indexTable = data.clone();
            data.incPtr(size_white[t][0]);

            ei_btm[t].precomp.indexTable = data.clone();
            data.incPtr(size_black[t][0]);
        }

        // sizeTable ptr
        for (int t = 0; t < NUM; t++) {
            ei_wtm[t].precomp.sizeTable = data.createU_INT16_PTR(0);
            data.incPtr(size_white[t][1]);

            ei_btm[t].precomp.sizeTable = data.createU_INT16_PTR(0);
            data.incPtr(size_black[t][1]);
        }

        // data ptr
        for (int t = 0; t < NUM; t++) {
            data.ptr = (data.ptr + 0x3f) & ~0x3f;
            ei_wtm[t].precomp.data = data.clone();
            data.incPtr(size_white[t][2]);

            data.ptr = (data.ptr + 0x3f) & ~0x3f;
            ei_btm[t].precomp.data = data.clone();
            data.incPtr(size_black[t][2]);
        }

        return true;
    }

    @Override
    int probe_table_imp(BitPosition pos, long key, int score) {
        boolean flip = key != pawnEntry.key;
        boolean bside = pos.turn == flip;

        int[] p = new int[TB_PIECES];

        PawnEncInfo ei = ei_wtm[0];

        int i = ei.fill_squares(pos, flip, flip ? 0x38 : 0, p, 0);
        int t = ei.leading_pawn(p);

        ei = bside ? ei_btm[t] : ei_wtm[t];

        while (i < pawnEntry.num) {
            i = ei.fill_squares(pos, flip, flip ? 0x38 : 0, p, i);
        }

        int idx = ei.encode_pawn_f(p);

        byte[] w = ei.precomp.decompress_pairs(idx);

        return (int) w[0] - 2;
    }

}
