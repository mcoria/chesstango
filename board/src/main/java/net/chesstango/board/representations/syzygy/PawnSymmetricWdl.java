package net.chesstango.board.representations.syzygy;

import static net.chesstango.board.representations.syzygy.TableBase.TableType.WDL;

/**
 * @author Mauricio Coria
 */
class PawnSymmetricWdl extends TableBase {
    final PawnEntry pawnEntry;
    final PawnEncInfo[] ei;

    PawnSymmetricWdl(PawnEntry pawnEntry) {
        super(WDL, pawnEntry);
        this.pawnEntry = pawnEntry;
        this.ei = new PawnEncInfo[4];
    }

    @Override
    boolean init_table_imp() {
        U_INT8_PTR data = new U_INT8_PTR(mappedFile);
        data.incPtr(5);

        final int NUM = 4;

        int[] tb_size = new int[NUM];
        for (int i = 0; i < NUM; i++) {
            ei[i] = new PawnEncInfo(pawnEntry);
            tb_size[i] = ei[i].init_enc_info(data, 0, i);
            data.incPtr(pawnEntry.num + 1 + pawnEntry.pawns[1]);
        }

        // Next, there may be a padding byte to align the position within the tablebase file to a multiple of 2 bytes.
        data.ptr += data.ptr & 1;

        int[][] size = new int[NUM][3];
        for (int t = 0; t < NUM; t++) {
            ei[t].precomp = new PairsData(WDL, data, tb_size[t], size[t]);
        }

        // indexTable ptr
        for (int t = 0; t < NUM; t++) {
            ei[t].precomp.indexTable = data.clone();
            data.incPtr(size[t][0]);
        }

        // sizeTable ptr
        for (int t = 0; t < NUM; t++) {
            ei[t].precomp.sizeTable = data.createU_INT16_PTR(0);
            data.incPtr(size[t][1]);
        }

        // data ptr
        for (int t = 0; t < NUM; t++) {
            data.ptr = (data.ptr + 0x3f) & ~0x3f;
            ei[t].precomp.data = data.clone();
            data.incPtr(size[t][2]);
        }

        return true;
    }

    @Override
    int probe_table_imp(BitPosition pos, long key, int score) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
