package net.chesstango.board.representations.syzygy;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.TB_MAX_SYMS;
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

    boolean init_table_wdl(PieceAsymmetric pieceAsymmetric) {
        BytePTR bytePTR = new BytePTR(pieceAsymmetric.mappedFile);
        bytePTR.ptr = 5;

        EncInfo[] ei = pieceAsymmetric.ei;

        long[][] tb_size = new long[1][2];

        tb_size[0][0] = init_enc_info(ei[0], bytePTR, 0);
        tb_size[0][1] = init_enc_info(ei[1], bytePTR, 4);

        bytePTR.incPtr(pieceAsymmetric.pieceEntry.num + 1);

        // Next, there may be a padding byte to align the position within the tablebase file to a multiple of 2 bytes.
        bytePTR.ptr += bytePTR.ptr & 1;

        long[][][] size = new long[6][2][3];

        byte flags = bytePTR.read_uint8_t(0);
        ei[0].precomp = setup_pairs(WDL, bytePTR, tb_size[0][0], size[0][0]);
        ei[1].precomp = setup_pairs(WDL, bytePTR, tb_size[0][1], size[0][1]);

        // indexTable ptr
        ei[0].precomp.indexTable = bytePTR.clone();
        bytePTR.incPtr((int) size[0][0][0]);

        ei[1].precomp.indexTable = bytePTR.clone();
        bytePTR.incPtr((int) size[0][1][0]);

        // sizeTable ptr
        ei[0].precomp.sizeTable = bytePTR.createCharPTR(0);
        bytePTR.incPtr((int) size[0][0][1]);

        ei[1].precomp.sizeTable = bytePTR.createCharPTR(0);
        bytePTR.incPtr((int) size[0][1][1]);

        bytePTR.ptr = (bytePTR.ptr + 0x3f) & ~0x3f;
        ei[0].precomp.data = bytePTR.clone();
        bytePTR.incPtr((int) size[0][0][2]);

        bytePTR.ptr = (bytePTR.ptr + 0x3f) & ~0x3f;
        ei[1].precomp.data = bytePTR.clone();
        bytePTR.incPtr((int) size[0][1][2]);

        return true;
    }

    boolean init_table_dtz(PieceAsymmetric pieceAsymmetric) {
        return false;
    }


    long init_enc_info(EncInfo ei, BytePTR bytePTR, int shift) {
        for (int i = 0; i < pieceEntry.num; i++) {
            ei.pieces[i] = (byte) ((bytePTR.read_uint8_t(i + 1) >>> shift) & 0x0f);
            ei.norm[i] = 0;
        }

        int order = (bytePTR.read_uint8_t(0) >>> shift) & 0x0f;
        int order2 = 0x0f;

        int k = pieceEntry.kk_enc ? 2 : 3;
        ei.norm[0] = (byte) k;

        for (int i = k; i < pieceEntry.num; i += ei.norm[i]) {
            for (int j = i; j < pieceEntry.num && ei.pieces[j] == ei.pieces[i]; j++) {
                ei.norm[i]++;
            }
        }

        int n = 64 - k;
        long f = 1;
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
    static long subfactor(long k, long n) {
        long f = n;
        long l = 1;
        for (long i = 1; i < k; i++) {
            f *= n - i;
            l *= i + 1;
        }
        return f / l;
    }

    PairsData setup_pairs(TableType tableType, BytePTR ptr, long tb_size, long[] size) {
        PairsData d;
        BytePTR data = ptr.clone();

        if ((data.read_uint8_t(0) & 0x80) != 0) {
            d = new PairsData();
            d.idxBits = 0;
            d.constValue[0] = WDL == tableType ? data.read_uint8_t(1) : 0;
            d.constValue[1] = 0;
            ptr.ptr = data.ptr + 2;
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
        d.offset = data.createCharPTR(10);
        d.symLen = new byte[numSyms];
        d.symPat = data.clone().incPtr(12 + 2 * h);
        d.minLen = minLen;
        d.base = new long[h];

        ptr.ptr = data.ptr + (12 + 2 * h + 3 * numSyms + (numSyms & 1));

        long num_indices = (tb_size + (1L << idxBits) - 1) >>> idxBits;
        size[0] = 6L * num_indices;
        size[1] = 2L * numBlocks;
        size[2] = (long) realNumBlocks << blockSize;

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
        BytePTR w = d.symPat.clone();
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
}
