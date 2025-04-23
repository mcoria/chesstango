package net.chesstango.board.representations.syzygy;

import static net.chesstango.board.representations.syzygy.TableType.DTZ;
import static net.chesstango.board.representations.syzygy.TableType.WDL;

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
        BytePTR bytePTR = new BytePTR(mappedFile);
        bytePTR.ptr = 5;

        int[][] tb_size = new int[1][1];

        tb_size[0][0] = pieceAlgorithm.init_enc_info(ei_dtz, bytePTR, 0);

        bytePTR.incPtr(pieceEntry.num + 1);

        // Next, there may be a padding byte to align the position within the tablebase file to a multiple of 2 bytes.
        bytePTR.ptr += bytePTR.ptr & 1;

        int[][][] size = new int[6][2][3];

        pieceEntry.dtzFlags = bytePTR.read_uint8_t(0);
        ei_dtz.precomp = pieceAlgorithm.setup_pairs(DTZ, bytePTR, tb_size[0][0], size[0][0]);

        // indexTable ptr
        ei_dtz.precomp.indexTable = bytePTR.clone();
        bytePTR.incPtr(size[0][0][0]);

        // sizeTable ptr
        ei_dtz.precomp.sizeTable = bytePTR.createCharPTR(0);
        bytePTR.incPtr(size[0][0][1]);

        // data ptr
        bytePTR.ptr = (bytePTR.ptr + 0x3f) & ~0x3f;
        ei_dtz.precomp.data = bytePTR.clone();
        bytePTR.incPtr(size[0][0][2]);

        /*
        void *map = data;

        *(be->hasPawns ? &PAWN(be)->dtzMap : &PIECE(be)->dtzMap) = map;

        uint16_t (*mapIdx)[4] = be->hasPawns ? &PAWN(be)->dtzMapIdx[0]
                                      : &PIECE(be)->dtzMapIdx;
        uint8_t *flags = be->hasPawns ? &PAWN(be)->dtzFlags[0]
                              : &PIECE(be)->dtzFlags;
        for (int t = 0; t < num; t++) {
            if (flags[t] & 2) {
                if (!(flags[t] & 16)) {
                    for (int i = 0; i < 4; i++) {
                        mapIdx[t][i] = (uint16_t)(data + 1 - (uint8_t *)map);
                        data += 1 + data[0];
                    }
                } else {
                    data += (uintptr_t)data & 0x01;
                    for (int i = 0; i < 4; i++) {
                        mapIdx[t][i] = (uint16_t)((uint16_t*)data + 1 - (uint16_t *)map);
                        data += 2 + 2 * read_le_u16(data);
                    }
                }
            }
        }
        data += (uintptr_t)data & 0x01;
        */

        return true;
    }

    @Override
    int probe_table_imp(BitPosition pos, long key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
