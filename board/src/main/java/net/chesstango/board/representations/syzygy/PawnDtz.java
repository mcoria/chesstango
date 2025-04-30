package net.chesstango.board.representations.syzygy;

import static net.chesstango.board.representations.syzygy.TableBase.TableType.DTZ;

/**
 * @author Mauricio Coria
 */
class PawnDtz extends TableBase {
    final PawnEntry pawnEntry;

    final PawnEncInfo[] ei;

    U_INT8_PTR dtzMap;
    byte[] dtzFlags = new byte[4];
    short[][] dtzMapIdx = new short[4][4];

    public PawnDtz(PawnEntry pawnEntry) {
        super(DTZ, pawnEntry);
        this.pawnEntry = pawnEntry;
        this.ei = new PawnEncInfo[4];
    }

    @Override
    boolean init_table_imp() {
        U_INT8_PTR data = new U_INT8_PTR(mappedFile);
        data.incPtr(5);

        final int num = 4;
        int[] tb_size = new int[num];
        for (int i = 0; i < num; i++) {
            ei[i] = new PawnEncInfo(pawnEntry);
            tb_size[i] = ei[i].init_enc_info(data, 0, i);
            data.incPtr(pawnEntry.num + 1 + pawnEntry.pawns[1]);
        }

        // Next, there may be a padding byte to align the position within the tablebase file to a multiple of 2 bytes.
        data.ptr += data.ptr & 1;

        int[][] size = new int[4][3];
        for (int t = 0; t < num; t++) {
            dtzFlags[t] = data.read_uint8_t(0);
            ei[t].precomp = new PairsData(DTZ, data, tb_size[t], size[t]);
        }


        // DTZ specific attributes
        dtzMap = data.clone();
        for (int t = 0; t < num; t++) {
            if ((dtzFlags[t] & 2) != 0) {
                if ((dtzFlags[t] & 16) == 0) {
                    for (int i = 0; i < 4; i++) {
                        dtzMapIdx[t][i] = (short) (data.ptr + 1 - dtzMap.ptr);
                        data.incPtr(1 + data.read_uint8_t(0) & 0xFF);
                    }
                } else {
                    throw new RuntimeException("not implemented: pieceEntry.dtzFlags & 16 == 0");
                }
            }
        }
        data.ptr += data.ptr & 1;

        // indexTable ptr
        for (int t = 0; t < num; t++) {
            ei[t].precomp.indexTable = data.clone();
            data.incPtr(size[t][0]);
        }

        // sizeTable ptr
        for (int t = 0; t < num; t++) {
            ei[t].precomp.sizeTable = data.createU_INT16_PTR(0);
            data.incPtr(size[t][1]);
        }

        // data ptr
        for (int t = 0; t < num; t++) {
            data.ptr = (data.ptr + 0x3f) & ~0x3f;
            ei[t].precomp.data = data.clone();
            data.incPtr(size[t][2]);
        }

        return true;
    }

    @Override
    int probe_table_imp(BitPosition pos, long key, int score) {
        return 0;
    }

}
