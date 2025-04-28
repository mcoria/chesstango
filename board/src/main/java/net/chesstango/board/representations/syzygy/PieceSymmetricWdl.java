package net.chesstango.board.representations.syzygy;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.TB_PIECES;
import static net.chesstango.board.representations.syzygy.TableBase.TableType.WDL;

/**
 * @author Mauricio Coria
 */
class PieceSymmetricWdl extends TableBase {
    final PieceEntry pieceEntry;

    final PieceEncInfo ei_wtm;

    public PieceSymmetricWdl(PieceEntry pieceEntry) {
        super(WDL, pieceEntry);
        this.pieceEntry = pieceEntry;
        this.ei_wtm = new PieceEncInfo(pieceEntry);
    }

    @Override
    boolean init_table_imp() {
        U_INT8_PTR data = new U_INT8_PTR(mappedFile);
        data.incPtr(5);

        int tb_size_white = ei_wtm.init_enc_info(data, 0);

        data.incPtr(pieceEntry.num + 1);

        // Next, there may be a padding byte to align the position within the tablebase file to a multiple of 2 bytes.
        data.ptr += data.ptr & 1;

        int[] size_white = new int[3];

        ei_wtm.precomp = new PairsData(WDL, data, tb_size_white, size_white);

        // indexTable ptr
        ei_wtm.precomp.indexTable = data.clone();
        data.incPtr(size_white[0]);

        // sizeTable ptr
        ei_wtm.precomp.sizeTable = data.createU_INT16_PTR(0);
        data.incPtr(size_white[1]);

        // data ptr
        data.ptr = (data.ptr + 0x3f) & ~0x3f;
        ei_wtm.precomp.data = data.clone();
        data.incPtr(size_white[2]);

        return true;
    }

    @Override
    int probe_table_imp(BitPosition pos, long key, int score) {
        boolean flip = !pos.turn;

        int[] p = new int[TB_PIECES];

        PieceEncInfo ei = ei_wtm;

        for (int i = 0; i < pieceEntry.num; ) {
            i = PieceEncInfo.fill_squares(pos, ei.pieces, flip, 0, p, i);
        }

        int idx = ei.encode_piece(p);

        byte[] w = ei.precomp.decompress_pairs(idx);

        return (int) w[0] - 2;
    }
}
