package net.chesstango.board.representations.syzygy;

import static net.chesstango.board.representations.syzygy.Chess.poplsb;
import static net.chesstango.board.representations.syzygy.SyzygyConstants.*;
import static net.chesstango.board.representations.syzygy.TableType.WDL;

/**
 * @author Mauricio Coria
 */
class PieceAlgorithm {
    final PieceEntry pieceEntry;
    final MappedFile mappedFile;

    PieceAlgorithm(PieceEntry pieceEntry, MappedFile mappedFile) {
        this.pieceEntry = pieceEntry;
        this.mappedFile = mappedFile;
    }

    int init_enc_info(EncInfo ei, U_INT8_PTR UINT8PTR, int shift) {
        for (int i = 0; i < pieceEntry.num; i++) {
            ei.pieces[i] = (byte) ((UINT8PTR.read_uint8_t(i + 1) >>> shift) & 0x0f);
            ei.norm[i] = 0;
        }

        int order = (UINT8PTR.read_uint8_t(0) >>> shift) & 0x0f;
        int order2 = 0x0f;

        int k = pieceEntry.kk_enc ? 2 : 3;
        ei.norm[0] = (byte) k;

        for (int i = k; i < pieceEntry.num; i += ei.norm[i]) {
            for (int j = i; j < pieceEntry.num && ei.pieces[j] == ei.pieces[i]; j++) {
                ei.norm[i]++;
            }
        }

        int n = 64 - k;
        int f = 1;
        for (int i = 0; k < pieceEntry.num || i == order || i == order2; i++) {
            if (i == order) {
                ei.factor[0] = f;
                f *= pieceEntry.kk_enc ? 462 : 31332;
            } else if (i == order2) {
                ei.factor[ei.norm[0]] = f;
                f *= subfactor(ei.norm[ei.norm[0]], 48 - ei.norm[0]);
            } else {
                ei.factor[k] = f;
                f *= subfactor(ei.norm[k], n);
                n -= ei.norm[k];
                k += ei.norm[k];
            }
        }
        return f;
    }

    // Count number of placements of k like pieces on n squares
    static int subfactor(int k, int n) {
        int f = n;
        int l = 1;
        for (int i = 1; i < k; i++) {
            f *= n - i;
            l *= i + 1;
        }
        return f / l;
    }

    PairsData setup_pairs(TableType tableType, U_INT8_PTR ptr, int tb_size, int[] size) {
        PairsData d;
        U_INT8_PTR data = ptr.clone();

        if ((data.read_uint8_t(0) & 0x80) != 0) {
            d = new PairsData();
            d.idxBits = 0;
            d.constValue[0] = WDL == tableType ? data.read_uint8_t(1) : 0;
            d.constValue[1] = 0;
            ptr.incPtr(2);
            size[0] = 0;
            size[1] = 0;
            size[2] = 0;
            return d;
        }

        byte blockSize = data.read_uint8_t(1);
        byte idxBits = data.read_uint8_t(2);
        int realNumBlocks = mappedFile.read_le_u32(data.ptr + 4);
        int numBlocks = realNumBlocks + data.read_uint8_t(3);
        byte maxLen = data.read_uint8_t(8);
        byte minLen = data.read_uint8_t(9);
        int h = maxLen - minLen + 1;
        int numSyms = mappedFile.read_le_u16(data.ptr + 10 + 2 * h);


        d = new PairsData();
        d.blockSize = blockSize;
        d.idxBits = idxBits;
        d.offset = data.createU_INT16_PTR(10);
        d.symLen = new byte[numSyms];
        d.symPat = data.clone().incPtr(12 + 2 * h);
        d.minLen = minLen;
        d.base = new long[h];

        ptr.ptr = data.ptr + (12 + 2 * h + 3 * numSyms + (numSyms & 1));

        int num_indices = (tb_size + (1 << idxBits) - 1) >>> idxBits;
        size[0] = 6 * num_indices;
        size[1] = 2 * numBlocks;
        size[2] = realNumBlocks << blockSize;

        assert (numSyms < TB_MAX_SYMS);
        byte[] tmp = new byte[TB_MAX_SYMS];

        for (int s = 0; s < numSyms; s++) {
            if (tmp[s] == 0) {
                calc_symLen(d, s, tmp);
            }
        }

        d.base[h - 1] = 0;
        for (int i = h - 2; i >= 0; i--) {
            d.base[i] = (d.base[i + 1] + mappedFile.read_le_u16(d.offset.ptr + 2 * i) - mappedFile.read_le_u16(d.offset.ptr + 2 * i + 2)) / 2;
        }
        for (int i = 0; i < h; i++) {
            d.base[i] <<= 64 - (minLen + i);
        }

        // offset is a two byte pointer
        d.offset.incPtr(-2 * d.minLen);

        return d;
    }

    void calc_symLen(PairsData d, int s, byte[] tmp) {
        U_INT8_PTR w = d.symPat.clone();
        w.incPtr(3 * s);
        int w2 = (w.read_uint8_t(2) & 0xFF) << 4;
        int w1 = (w.read_uint8_t(1) & 0xFF) >>> 4;

        int s2 = w2 | w1;
        if (s2 == 0x0fff) {
            d.symLen[s] = 0;
        } else {
            int s1 = ((w.read_uint8_t(1) & 0xF) << 8) | (w.read_uint8_t(0) & 0xFF);
            if (tmp[s1] == 0) calc_symLen(d, s1, tmp);
            if (tmp[s2] == 0) calc_symLen(d, s2, tmp);
            d.symLen[s] = (byte) (d.symLen[s1] + d.symLen[s2] + 0x01);
        }
        tmp[s] = 1;
    }

    byte[] decompress_pairs(PairsData d, int idx) {
        if (d.idxBits == 0) {
            return d.constValue;
        }


        int mainIdx = idx >>> d.idxBits;
        int litIdx = (idx & ((1 << d.idxBits) - 1)) - (1 << (d.idxBits - 1));

        int block = d.indexTable.read_le_u32(6 * mainIdx);

        short idxOffset = d.indexTable.read_short(6 * mainIdx + 4);
        litIdx += idxOffset;


        if (litIdx < 0) {
            while (litIdx < 0) {
                litIdx += (d.sizeTable.read_short(--block) & 0xFFFF) + 1;
            }
        } else {
            while (litIdx > d.sizeTable.read_short(block)) {
                litIdx -= (d.sizeTable.read_short(block++) & 0xFFFF) + 1;
            }
        }


        U_INT32_PTR ptr = d.data.createU_INT32_PTR(block << d.blockSize);

        int m = d.minLen;
        U_INT16_PTR offset = d.offset.clone();
        byte[] symLen = d.symLen;
        int sym, bitCnt;

        long code = Long.reverseBytes(ptr.read_le_u64(0));

        ptr.incPtr(2);
        bitCnt = 0;     // number of "empty bits" in code
        for (; ; ) {
            int l = m;
            while (Long.compareUnsigned(code, d.base[l - m]) < 0) l++;
            sym = offset.read_short(l);
            sym += (int) ((code - d.base[l - m]) >>> (64 - l));
            if (litIdx < (int) symLen[sym] + 1) break;
            litIdx -= (symLen[sym] & 0xFF) + 1;
            code <<= (l & 0xFFFFFFFFL);
            bitCnt += l;
            if (bitCnt >= 32) {
                bitCnt -= 32;
                int tmp = Integer.reverseBytes(ptr.read_le_u32(0));
                ptr.incPtr(1);
                code |= (tmp & 0xFFFFFFFFL) << bitCnt;
            }
        }


        while (symLen[sym] != 0) {
            U_INT8_PTR w = d.symPat.clone().incPtr(3 * sym);
            int s1 = (((w.read_uint8_t(1) & 0x0F) << 8) | (w.read_uint8_t(0) & 0xFF));
            if (litIdx < (int) symLen[s1] + 1) {
                sym = s1;
            } else {
                litIdx -= (int) symLen[s1] + 1;
                sym = (w.read_uint8_t(2) << 4) | ((w.read_uint8_t(1) & 0xFF) >>> 4);
            }
        }

        return new byte[]{d.symPat.read_uint8_t(3 * sym), d.symPat.read_uint8_t(3 * sym + 1)};
    }

    // p[i] is to contain the square 0-63 (A1-H8) for a piece of type
    // pc[i] ^ flip, where 1 = white pawn, ..., 14 = black king and pc ^ flip
    // flips between white and black if flip == true.
    // Pieces of the same type are guaranteed to be consecutive.
    int fill_squares(BitPosition pos, byte[] pc, boolean flip, int mirror, int[] p, int i) {
        Color color = Color.colorOfPiece(pc[i]);
        if (flip) {
            color = color.oposite();
        }
        long bb = pos.pieces_by_type(color, PieceType.typeOfPiece(pc[i]));
        int sq;
        do {
            sq = Long.numberOfTrailingZeros(bb);
            p[i++] = sq ^ mirror;
            bb = poplsb(bb);
        } while (bb != 0);
        return i;
    }

    int encode_piece(int[] p, EncInfo ei) {
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
        idx *= ei.factor[0];


        while (k < n) {
            int t = k + ei.norm[k];
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
            idx += s * ei.factor[k];
            k = t;
        }

        return idx;
    }


    /**
     * Coeficients
     */

    static final byte[] OffDiag = new byte[]{
            0, -1, -1, -1, -1, -1, -1, -1,
            1, 0, -1, -1, -1, -1, -1, -1,
            1, 1, 0, -1, -1, -1, -1, -1,
            1, 1, 1, 0, -1, -1, -1, -1,
            1, 1, 1, 1, 0, -1, -1, -1,
            1, 1, 1, 1, 1, 0, -1, -1,
            1, 1, 1, 1, 1, 1, 0, -1,
            1, 1, 1, 1, 1, 1, 1, 0
    };

    static final byte[] Triangle = new byte[]{
            6, 0, 1, 2, 2, 1, 0, 6,
            0, 7, 3, 4, 4, 3, 7, 0,
            1, 3, 8, 5, 5, 8, 3, 1,
            2, 4, 5, 9, 9, 5, 4, 2,
            2, 4, 5, 9, 9, 5, 4, 2,
            1, 3, 8, 5, 5, 8, 3, 1,
            0, 7, 3, 4, 4, 3, 7, 0,
            6, 0, 1, 2, 2, 1, 0, 6
    };

    static final byte[] FlipDiag = new byte[]{
            0, 8, 16, 24, 32, 40, 48, 56,
            1, 9, 17, 25, 33, 41, 49, 57,
            2, 10, 18, 26, 34, 42, 50, 58,
            3, 11, 19, 27, 35, 43, 51, 59,
            4, 12, 20, 28, 36, 44, 52, 60,
            5, 13, 21, 29, 37, 45, 53, 61,
            6, 14, 22, 30, 38, 46, 54, 62,
            7, 15, 23, 31, 39, 47, 55, 63
    };

    static final byte[] Lower = new byte[]{
            28, 0, 1, 2, 3, 4, 5, 6,
            0, 29, 7, 8, 9, 10, 11, 12,
            1, 7, 30, 13, 14, 15, 16, 17,
            2, 8, 13, 31, 18, 19, 20, 21,
            3, 9, 14, 18, 32, 22, 23, 24,
            4, 10, 15, 19, 22, 33, 25, 26,
            5, 11, 16, 20, 23, 25, 34, 27,
            6, 12, 17, 21, 24, 26, 27, 35
    };

    static final byte[] Diag = new byte[]{
            0, 0, 0, 0, 0, 0, 0, 8,
            0, 1, 0, 0, 0, 0, 9, 0,
            0, 0, 2, 0, 0, 10, 0, 0,
            0, 0, 0, 3, 11, 0, 0, 0,
            0, 0, 0, 12, 4, 0, 0, 0,
            0, 0, 13, 0, 0, 5, 0, 0,
            0, 14, 0, 0, 0, 0, 6, 0,
            15, 0, 0, 0, 0, 0, 0, 7
    };

    static final byte[][] Flap = new byte[][]
            {
                    {
                            0, 0, 0, 0, 0, 0, 0, 0,
                            0, 6, 12, 18, 18, 12, 6, 0,
                            1, 7, 13, 19, 19, 13, 7, 1,
                            2, 8, 14, 20, 20, 14, 8, 2,
                            3, 9, 15, 21, 21, 15, 9, 3,
                            4, 10, 16, 22, 22, 16, 10, 4,
                            5, 11, 17, 23, 23, 17, 11, 5,
                            0, 0, 0, 0, 0, 0, 0, 0
                    },
                    {
                            0, 0, 0, 0, 0, 0, 0, 0,
                            0, 1, 2, 3, 3, 2, 1, 0,
                            4, 5, 6, 7, 7, 6, 5, 4,
                            8, 9, 10, 11, 11, 10, 9, 8,
                            12, 13, 14, 15, 15, 14, 13, 12,
                            16, 17, 18, 19, 19, 18, 17, 16,
                            20, 21, 22, 23, 23, 22, 21, 20,
                            0, 0, 0, 0, 0, 0, 0, 0
                    }
            };

    static final byte[][] PawnTwist = new byte[][]
            {
                    {
                            0, 0, 0, 0, 0, 0, 0, 0,
                            47, 35, 23, 11, 10, 22, 34, 46,
                            45, 33, 21, 9, 8, 20, 32, 44,
                            43, 31, 19, 7, 6, 18, 30, 42,
                            41, 29, 17, 5, 4, 16, 28, 40,
                            39, 27, 15, 3, 2, 14, 26, 38,
                            37, 25, 13, 1, 0, 12, 24, 36,
                            0, 0, 0, 0, 0, 0, 0, 0
                    },
                    {
                            0, 0, 0, 0, 0, 0, 0, 0,
                            47, 45, 43, 41, 40, 42, 44, 46,
                            39, 37, 35, 33, 32, 34, 36, 38,
                            31, 29, 27, 25, 24, 26, 28, 30,
                            23, 21, 19, 17, 16, 18, 20, 22,
                            15, 13, 11, 9, 8, 10, 12, 14,
                            7, 5, 3, 1, 0, 2, 4, 6,
                            0, 0, 0, 0, 0, 0, 0, 0
                    }
            };

    static final short[][] KKIdx = new short[][]
            {
                    {
                            -1, -1, -1, 0, 1, 2, 3, 4,
                            -1, -1, -1, 5, 6, 7, 8, 9,
                            10, 11, 12, 13, 14, 15, 16, 17,
                            18, 19, 20, 21, 22, 23, 24, 25,
                            26, 27, 28, 29, 30, 31, 32, 33,
                            34, 35, 36, 37, 38, 39, 40, 41,
                            42, 43, 44, 45, 46, 47, 48, 49,
                            50, 51, 52, 53, 54, 55, 56, 57
                    },
                    {
                            58, -1, -1, -1, 59, 60, 61, 62,
                            63, -1, -1, -1, 64, 65, 66, 67,
                            68, 69, 70, 71, 72, 73, 74, 75,
                            76, 77, 78, 79, 80, 81, 82, 83,
                            84, 85, 86, 87, 88, 89, 90, 91,
                            92, 93, 94, 95, 96, 97, 98, 99,
                            100, 101, 102, 103, 104, 105, 106, 107,
                            108, 109, 110, 111, 112, 113, 114, 115
                    },
                    {
                            116, 117, -1, -1, -1, 118, 119, 120,
                            121, 122, -1, -1, -1, 123, 124, 125,
                            126, 127, 128, 129, 130, 131, 132, 133,
                            134, 135, 136, 137, 138, 139, 140, 141,
                            142, 143, 144, 145, 146, 147, 148, 149,
                            150, 151, 152, 153, 154, 155, 156, 157,
                            158, 159, 160, 161, 162, 163, 164, 165,
                            166, 167, 168, 169, 170, 171, 172, 173
                    },
                    {
                            174, -1, -1, -1, 175, 176, 177, 178,
                            179, -1, -1, -1, 180, 181, 182, 183,
                            184, -1, -1, -1, 185, 186, 187, 188,
                            189, 190, 191, 192, 193, 194, 195, 196,
                            197, 198, 199, 200, 201, 202, 203, 204,
                            205, 206, 207, 208, 209, 210, 211, 212,
                            213, 214, 215, 216, 217, 218, 219, 220,
                            221, 222, 223, 224, 225, 226, 227, 228
                    },
                    {
                            229, 230, -1, -1, -1, 231, 232, 233,
                            234, 235, -1, -1, -1, 236, 237, 238,
                            239, 240, -1, -1, -1, 241, 242, 243,
                            244, 245, 246, 247, 248, 249, 250, 251,
                            252, 253, 254, 255, 256, 257, 258, 259,
                            260, 261, 262, 263, 264, 265, 266, 267,
                            268, 269, 270, 271, 272, 273, 274, 275,
                            276, 277, 278, 279, 280, 281, 282, 283
                    },
                    {
                            284, 285, 286, 287, 288, 289, 290, 291,
                            292, 293, -1, -1, -1, 294, 295, 296,
                            297, 298, -1, -1, -1, 299, 300, 301,
                            302, 303, -1, -1, -1, 304, 305, 306,
                            307, 308, 309, 310, 311, 312, 313, 314,
                            315, 316, 317, 318, 319, 320, 321, 322,
                            323, 324, 325, 326, 327, 328, 329, 330,
                            331, 332, 333, 334, 335, 336, 337, 338
                    },
                    {
                            -1, -1, 339, 340, 341, 342, 343, 344,
                            -1, -1, 345, 346, 347, 348, 349, 350,
                            -1, -1, 441, 351, 352, 353, 354, 355,
                            -1, -1, -1, 442, 356, 357, 358, 359,
                            -1, -1, -1, -1, 443, 360, 361, 362,
                            -1, -1, -1, -1, -1, 444, 363, 364,
                            -1, -1, -1, -1, -1, -1, 445, 365,
                            -1, -1, -1, -1, -1, -1, -1, 446
                    },
                    {
                            -1, -1, -1, 366, 367, 368, 369, 370,
                            -1, -1, -1, 371, 372, 373, 374, 375,
                            -1, -1, -1, 376, 377, 378, 379, 380,
                            -1, -1, -1, 447, 381, 382, 383, 384,
                            -1, -1, -1, -1, 448, 385, 386, 387,
                            -1, -1, -1, -1, -1, 449, 388, 389,
                            -1, -1, -1, -1, -1, -1, 450, 390,
                            -1, -1, -1, -1, -1, -1, -1, 451
                    },
                    {
                            452, 391, 392, 393, 394, 395, 396, 397,
                            -1, -1, -1, -1, 398, 399, 400, 401,
                            -1, -1, -1, -1, 402, 403, 404, 405,
                            -1, -1, -1, -1, 406, 407, 408, 409,
                            -1, -1, -1, -1, 453, 410, 411, 412,
                            -1, -1, -1, -1, -1, 454, 413, 414,
                            -1, -1, -1, -1, -1, -1, 455, 415,
                            -1, -1, -1, -1, -1, -1, -1, 456
                    },
                    {
                            457, 416, 417, 418, 419, 420, 421, 422,
                            -1, 458, 423, 424, 425, 426, 427, 428,
                            -1, -1, -1, -1, -1, 429, 430, 431,
                            -1, -1, -1, -1, -1, 432, 433, 434,
                            -1, -1, -1, -1, -1, 435, 436, 437,
                            -1, -1, -1, -1, -1, 459, 438, 439,
                            -1, -1, -1, -1, -1, -1, 460, 440,
                            -1, -1, -1, -1, -1, -1, -1, 461
                    }
            };

    /**
     * Array initialization
     */
    static final int[][] Binomial = new int[7][64];
    static final int[][][] PawnIdx = new int[2][6][24];
    static final int[][] PawnFactorFile = new int[6][4];
    static final int[][] PawnFactorRank = new int[6][6];

    static {
        int i, j, k;

        // Binomial[k][n] = Bin(n, k)
        for (i = 0; i < 7; i++)
            for (j = 0; j < 64; j++) {
                int f = 1;
                int l = 1;
                for (k = 0; k < i; k++) {
                    f *= (j - k);
                    l *= (k + 1);
                }
                Binomial[i][j] = f / l;
            }

        for (i = 0; i < 6; i++) {
            int s = 0;
            for (j = 0; j < 24; j++) {
                PawnIdx[0][i][j] = s;
                s += Binomial[i][PawnTwist[0][(1 + (j % 6)) * 8 + (j / 6)]];
                if ((j + 1) % 6 == 0) {
                    PawnFactorFile[i][j / 6] = s;
                    s = 0;
                }
            }
        }

        for (i = 0; i < 6; i++) {
            int s = 0;
            for (j = 0; j < 24; j++) {
                PawnIdx[1][i][j] = s;
                s += Binomial[i][PawnTwist[1][(1 + (j / 4)) * 8 + (j % 4)]];
                if ((j + 1) % 4 == 0) {
                    PawnFactorRank[i][j / 4] = s;
                    s = 0;
                }
            }
        }
    }
}
