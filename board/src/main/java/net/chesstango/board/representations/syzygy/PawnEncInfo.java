package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class PawnEncInfo extends EncInfo {
    final PawnEntry pawnEntry;

    final byte[] FileToFile = {0, 1, 2, 3, 3, 2, 1, 0};

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

    int leading_pawn(int[] p) {
        for (int i = 1; i < pawnEntry.pawns[0]; i++)
            if (Flap[0][p[0]] > Flap[0][p[i]]) {
                int tmp = p[0];
                p[0] = p[i];
                p[i] = tmp;
            }
        return FileToFile[p[0] & 7];
    }

    int encode_pawn(int[] p) {
        int n = pawnEntry.num;
        int idx = 0;
        int k = 0;

        if ((p[0] & 0x04) != 0)
            for (int i = 0; i < n; i++)
                p[i] ^= 0x07;


        for (int i = 1; i < pawnEntry.pawns[0]; i++)
            for (int j = i + 1; j < pawnEntry.pawns[0]; j++)
                if (PawnTwist[0][p[i]] < PawnTwist[0][p[j]]) {
                    int tmp = p[i];
                    p[i] = p[j];
                    p[j] = tmp;
                }

        k = pawnEntry.pawns[0];
        idx = PawnIdx[0][k - 1][Flap[0][p[0]]];
        for (int i = 1; i < k; i++)
            idx += Binomial[k - i][PawnTwist[0][p[i]]];
        idx *= factor[0];

        // Pawns of other color
        if (pawnEntry.pawns[1] != 0) {
            int t = k + pawnEntry.pawns[1];
            for (int i = k; i < t; i++)
                for (int j = i + 1; j < t; j++)
                    if (p[i] > p[j]) {
                        int tmp = p[i];
                        p[i] = p[j];
                        p[j] = tmp;
                    }
            int s = 0;
            for (int i = k; i < t; i++) {
                int sq = p[i];
                int skips = 0;
                for (int j = 0; j < k; j++)
                    skips += (sq > p[j]) ? 1 : 0;
                s += Binomial[i - k + 1][sq - skips - 8];
            }
            idx += s * factor[k];
            k = t;
        }

        while (k < n) {
            int t = k + norm[k];
            for (int i = k; i < t; i++)
                for (int j = i + 1; j < t; j++)
                    if (p[i] > p[j]) {
                        int tmp = p[i];
                        p[i] = p[j];
                        p[j] = tmp;
                    }
            int s = 0;
            for (int i = k; i < t; i++) {
                int sq = p[i];
                int skips = 0;
                for (int j = 0; j < k; j++) {
                    skips = sq > p[j] ? skips + 1 : skips;
                }
                s += Binomial[i - k + 1][sq - skips];
            }
            idx += s * factor[k];
            k = t;
        }

        return idx;
    }
}
