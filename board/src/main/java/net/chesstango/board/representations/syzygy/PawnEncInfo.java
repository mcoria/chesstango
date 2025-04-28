package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
public class PawnEncInfo extends EncInfo {
    final PawnEntry pawnEntry;

    PawnEncInfo(PawnEntry pawnEntry) {
        this.pawnEntry = pawnEntry;
    }

    int init_enc_info(U_INT8_PTR UINT8PTR, int shift, int t) {
        boolean morePawns = (pawnEntry.pawns[1] & 0xFF) > 0;

        for (int i = 0; i < pawnEntry.num; i++) {
            pieces[i] = (byte) ((UINT8PTR.read_uint8_t(i + 1 + (morePawns ? 1 : 0)) >>> shift) & 0x0f);
            norm[i] = 0;
        }

        int order = (UINT8PTR.read_uint8_t(0) >>> shift) & 0x0f;
        int order2 = morePawns ? (UINT8PTR.read_uint8_t(1) >>> shift) & 0x0f : 0x0f;

        int k = pawnEntry.pawns[0];
        norm[0] = (byte) k;

        if (morePawns) {
            norm[k] = pawnEntry.pawns[1];
            k += norm[k];
        }

        for (int i = k; i < pawnEntry.num; i += norm[i]) {
            for (int j = i; j < pawnEntry.num && pieces[j] == pieces[i]; j++) {
                norm[i]++;
            }
        }

        int n = 64 - k;
        int f = 1;

        for (int i = 0; k < pawnEntry.num || i == order || i == order2; i++) {
            if (i == order) {
                factor[0] = f;
                f *= PawnFactorFile[norm[0] - 1][t];
            } else if (i == order2) {
                factor[norm[0]] = f;
                f *= subfactor(norm[norm[0]], 48 - norm[0]);
            } else {
                factor[k] = f;
                f *= subfactor(norm[k], n);
                n -= norm[k];
                k += norm[k];
            }
        }

        return f;
    }

    int encode_piece(int[] p) {
        throw new RuntimeException("Not implemented yet");
    }
}
