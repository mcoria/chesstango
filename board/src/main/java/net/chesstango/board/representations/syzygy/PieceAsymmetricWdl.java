package net.chesstango.board.representations.syzygy;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.TB_PIECES;
import static net.chesstango.board.representations.syzygy.TableType.WDL;

/**
 * @author Mauricio Coria
 */
class PieceAsymmetricWdl extends TableBase {
    final PieceEntry pieceEntry;

    final PieceEncInfo ei_wtm;
    final PieceEncInfo ei_btm;

    public PieceAsymmetricWdl(PieceEntry pieceEntry) {
        super(WDL, pieceEntry);
        this.pieceEntry = pieceEntry;
        this.ei_wtm = new PieceEncInfo(pieceEntry);
        this.ei_btm = new PieceEncInfo(pieceEntry);
    }

    @Override
    boolean init_table_imp() {
        U_INT8_PTR data = new U_INT8_PTR(mappedFile);
        data.incPtr(5);

        int tb_size_white = ei_wtm.init_enc_info(data, 0);
        int tb_size_black = ei_btm.init_enc_info(data, 4);

        data.incPtr(pieceEntry.num + 1);

        // Next, there may be a padding byte to align the position within the tablebase file to a multiple of 2 bytes.
        data.ptr += data.ptr & 1;

        int[] size_white = new int[3];
        int[] size_black = new int[3];

        ei_wtm.precomp = new PairsData(WDL, data, tb_size_white, size_white);
        ei_btm.precomp = new PairsData(WDL, data, tb_size_black, size_black);

        // indexTable ptr
        ei_wtm.precomp.indexTable = data.clone();
        data.incPtr(size_white[0]);

        ei_btm.precomp.indexTable = data.clone();
        data.incPtr(size_black[0]);

        // sizeTable ptr
        ei_wtm.precomp.sizeTable = data.createU_INT16_PTR(0);
        data.incPtr(size_white[1]);

        ei_btm.precomp.sizeTable = data.createU_INT16_PTR(0);
        data.incPtr(size_black[1]);

        // data ptr
        data.ptr = (data.ptr + 0x3f) & ~0x3f;
        ei_wtm.precomp.data = data.clone();
        data.incPtr(size_white[2]);

        data.ptr = (data.ptr + 0x3f) & ~0x3f;
        ei_btm.precomp.data = data.clone();
        data.incPtr(size_black[2]);

        return true;
    }

    @Override
    int probe_table_imp(BitPosition pos, long key, int score) {
        boolean flip = key != pieceEntry.key;
        boolean bside = pos.turn == flip;

        int[] p = new int[TB_PIECES];

        PieceEncInfo ei = bside ? ei_btm : ei_wtm;

        for (int i = 0; i < pieceEntry.num; ) {
            i = PieceEncInfo.fill_squares(pos, ei.pieces, flip, 0, p, i);
        }

        int idx = ei.encode_piece(p);

        byte[] w = ei.precomp.decompress_pairs(idx);

        return (int) w[0] - 2;
    }

}
