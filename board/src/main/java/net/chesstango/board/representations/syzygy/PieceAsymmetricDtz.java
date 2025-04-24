package net.chesstango.board.representations.syzygy;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.TB_PIECES;
import static net.chesstango.board.representations.syzygy.TableType.DTZ;

/**
 * @author Mauricio Coria
 */
class PieceAsymmetricDtz extends TableBase {

    final EncInfo ei_dtz;

    final PieceEntry pieceEntry;
    final PieceAlgorithm pieceAlgorithm;

    public PieceAsymmetricDtz(PieceEntry pieceEntry) {
        super(DTZ);
        this.pieceAlgorithm = new PieceAlgorithm(pieceEntry, mappedFile);
        this.pieceEntry = pieceEntry;
        this.ei_dtz = new EncInfo();
    }

    @Override
    PieceEntry getBaseEntry() {
        return pieceEntry;
    }

    @Override
    boolean init_table_imp() {
        BytePTR data = new BytePTR(mappedFile);
        data.ptr = 5;

        int tb_size = pieceAlgorithm.init_enc_info(ei_dtz, data, 0);

        data.incPtr(pieceEntry.num + 1);

        // Next, there may be a padding byte to align the position within the tablebase file to a multiple of 2 bytes.
        data.ptr += data.ptr & 1;

        int[] size = new int[3];
        pieceEntry.dtzFlags = data.read_uint8_t(0);
        ei_dtz.precomp = pieceAlgorithm.setup_pairs(DTZ, data, tb_size, size);

        // DTZ specific attributes
        pieceEntry.dtzMap = data.clone();
        short[] mapIdx = pieceEntry.dtzMapIdx;
        if ((pieceEntry.dtzFlags & 2) != 0) {
            if ((pieceEntry.dtzFlags & 16) == 0) {
                throw new RuntimeException("not implemented: pieceEntry.dtzFlags & 16 == 0");
            } else {
                throw new RuntimeException("not implemented: pieceEntry.dtzFlags & 16 == 0");
            }
        }
        data.ptr += data.ptr & 1;

        // indexTable ptr
        ei_dtz.precomp.indexTable = data.clone();
        data.incPtr(size[0]);

        // sizeTable ptr
        ei_dtz.precomp.sizeTable = data.createCharPTR(0);
        data.incPtr(size[1]);

        // data ptr
        data.ptr = (data.ptr + 0x3f) & ~0x3f;
        ei_dtz.precomp.data = data.clone();
        data.incPtr(size[2]);

        return true;
    }

    @Override
    int probe_table_imp(BitPosition pos, long key) {
        boolean flip = key != pieceEntry.key;
        boolean bside = pos.turn == flip;

        byte flags = pieceEntry.dtzFlags;
        boolean flagFlag = (flags & 1) != 0;
        if (flagFlag != bside && bside) {
            pieceEntry.syzygy.success = -1;
            return 0;
        }

        int[] p = new int[TB_PIECES];

        for (int i = 0; i < pieceEntry.num; ) {
            i = pieceAlgorithm.fill_squares(pos, ei_dtz.pieces, flip, 0, p, i);
        }

        int idx = pieceAlgorithm.encode_piece(p, ei_dtz);

        byte[] w = pieceAlgorithm.decompress_pairs(ei_dtz.precomp, idx);

        return (int) w[0] - 2;
    }

}
