package net.chesstango.board.representations.syzygy;

import static net.chesstango.board.representations.syzygy.Syzygy.PAFlags;
import static net.chesstango.board.representations.syzygy.Syzygy.WdlToMap;
import static net.chesstango.board.representations.syzygy.SyzygyConstants.TB_PIECES;
import static net.chesstango.board.representations.syzygy.TableBase.TableType.DTZ;

/**
 * @author Mauricio Coria
 */
class PieceDtz extends TableBase {
    final PieceEntry pieceEntry;

    final PieceEncInfo ei_dtz;

    byte dtzFlags;
    U_INT8_PTR dtzMap;
    short[] dtzMapIdx = new short[4];

    public PieceDtz(PieceEntry pieceEntry) {
        super(DTZ, pieceEntry);
        this.pieceEntry = pieceEntry;
        this.ei_dtz = new PieceEncInfo(pieceEntry);
    }

    @Override
    boolean init_table_imp() {
        U_INT8_PTR data = new U_INT8_PTR(mappedFile);
        data.incPtr(5);

        int tb_size = ei_dtz.init_enc_info(data, 0);

        data.incPtr(pieceEntry.num + 1);

        // Next, there may be a padding byte to align the position within the tablebase file to a multiple of 2 bytes.
        data.ptr += data.ptr & 1;

        int[] size = new int[3];
        dtzFlags = data.read_uint8_t(0);
        ei_dtz.precomp = new PairsData(DTZ, data, tb_size, size);

        // DTZ specific attributes
        dtzMap = data.clone();
        if ((dtzFlags & 2) != 0) {
            if ((dtzFlags & 16) == 0) {
                for (int i = 0; i < 4; i++) {
                    dtzMapIdx[i] = (short) (data.ptr + 1 - dtzMap.ptr);
                    data.incPtr(1 + data.read_uint8_t(0) & 0xFF);
                }
            } else {
                throw new RuntimeException("not implemented: pieceEntry.dtzFlags & 16 == 0");
            }
        }
        data.ptr += data.ptr & 1;

        // indexTable ptr
        ei_dtz.precomp.indexTable = data.clone();
        data.incPtr(size[0]);

        // sizeTable ptr
        ei_dtz.precomp.sizeTable = data.createU_INT16_PTR(0);
        data.incPtr(size[1]);

        // data ptr
        data.ptr = (data.ptr + 0x3f) & ~0x3f;
        ei_dtz.precomp.data = data.clone();
        data.incPtr(size[2]);

        return true;
    }

    @Override
    int probe_table_imp(BitPosition pos, long key, int score) {
        boolean flip;
        boolean bside;
        if (!pieceEntry.symmetric) {
            flip = key != pieceEntry.key;
            bside = pos.turn == flip;
        } else {
            flip = !pos.turn;
            bside = false;
        }


        byte flags = dtzFlags;
        boolean flagFlag = (flags & 1) != 0;
        if (flagFlag != bside && !pieceEntry.symmetric) {
            pieceEntry.syzygy.success = -1;
            return 0;
        }

        int[] p = new int[TB_PIECES];

        for (int i = 0; i < pieceEntry.num; ) {
            i = PieceEncInfo.fill_squares(pos, ei_dtz.pieces, flip, 0, p, i);
        }

        int idx = ei_dtz.encode_piece(p);

        byte[] w = ei_dtz.precomp.decompress_pairs(idx);

        int v = w[0] + ((w[1] & 0x0f) << 8);

        if ((flags & 2) != 0) {
            int m = WdlToMap[score + 2];
            if ((flags & 16) == 0) {
                v = dtzMap.read_uint8_t(dtzMapIdx[m] + v);
            } else {
                v = dtzMap.read_le_u16(dtzMapIdx[m] + v);
            }
        }
        if ((flags & PAFlags[score + 2]) == 0 || (score & 1) != 0) {
            v *= 2;
        }

        return v;
    }

}
