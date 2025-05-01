package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
public class PieceEncInfo extends EncInfo {
    final PieceEntry pieceEntry;

    PieceEncInfo(PieceEntry pieceEntry) {
        this.pieceEntry = pieceEntry;
    }

    int init_enc_info(U_INT8_PTR UINT8PTR, int shift) {
        for (int i = 0; i < pieceEntry.num; i++) {
            pieces[i] = (byte) ((UINT8PTR.read_uint8_t(i + 1) >>> shift) & 0x0f);
            norm[i] = 0;
        }

        int order = (UINT8PTR.read_uint8_t(0) >>> shift) & 0x0f;
        int order2 = 0x0f;

        int k = pieceEntry.kk_enc ? 2 : 3;
        norm[0] = (byte) k;

        for (int i = k; i < pieceEntry.num; i += norm[i]) {
            for (int j = i; j < pieceEntry.num && pieces[j] == pieces[i]; j++) {
                norm[i]++;
            }
        }

        int n = 64 - k;
        int f = 1;
        for (int i = 0; k < pieceEntry.num || i == order || i == order2; i++) {
            if (i == order) {
                factor[0] = f;
                f *= pieceEntry.kk_enc ? 462 : 31332;
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
        int n = pieceEntry.num;
        int idx;
        int k;

        if ((p[0] & 0x04) != 0)
            for (int i = 0; i < n; i++)
                p[i] ^= 0x07;


        if ((p[0] & 0x20) != 0)
            for (int i = 0; i < n; i++)
                p[i] ^= 0x38;

        for (int i = 0; i < n; i++)
            if (OffDiag[p[i]] != 0) {
                if (OffDiag[p[i]] > 0 && i < (pieceEntry.kk_enc ? 2 : 3))
                    for (int j = 0; j < n; j++)
                        p[j] = FlipDiag[p[j]];
                break;
            }

        if (pieceEntry.kk_enc) {
            idx = KKIdx[Triangle[p[0]]][p[1]];
            k = 2;
        } else {
            int s1 = p[1] > p[0] ? 1 : 0;
            int s2 = (p[2] > p[0] ? 1 : 0) + (p[2] > p[1] ? 1 : 0);

            if (OffDiag[p[0]] != 0)
                idx = Triangle[p[0]] * 63 * 62 + (p[1] - s1) * 62 + (p[2] - s2);
            else if (OffDiag[p[1]] != 0)
                idx = 6 * 63 * 62 + Diag[p[0]] * 28 * 62 + Lower[p[1]] * 62 + p[2] - s2;
            else if (OffDiag[p[2]] != 0)
                idx = 6 * 63 * 62 + 4 * 28 * 62 + Diag[p[0]] * 7 * 28 + (Diag[p[1]] - s1) * 28 + Lower[p[2]];
            else
                idx = 6 * 63 * 62 + 4 * 28 * 62 + 4 * 7 * 28 + Diag[p[0]] * 7 * 6 + (Diag[p[1]] - s1) * 6 + (Diag[p[2]] - s2);
            k = 3;
        }
        idx *= factor[0];


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
