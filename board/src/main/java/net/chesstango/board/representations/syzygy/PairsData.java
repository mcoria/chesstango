package net.chesstango.board.representations.syzygy;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.TB_MAX_SYMS;
import static net.chesstango.board.representations.syzygy.TableBase.TableType.WDL;

/**
 * @author Mauricio Coria
 */
class PairsData {
    U_INT8_PTR indexTable;
    U_INT16_PTR sizeTable;
    U_INT8_PTR data;
    U_INT16_PTR offset;
    U_INT8_PTR symPat;
    byte[] symLen;
    byte blockSize;
    byte idxBits;
    byte minLen;
    byte[] constValue = new byte[2];
    long[] base;

    PairsData(TableBase.TableType tableType, U_INT8_PTR ptr, int tb_size, int[] size) {
        U_INT8_PTR data = ptr.clone();

        if ((data.read_uint8_t(0) & 0x80) != 0) {
            this.idxBits = 0;
            this.constValue[0] = WDL == tableType ? data.read_uint8_t(1) : 0;
            this.constValue[1] = 0;
            ptr.incPtr(2);
            size[0] = 0;
            size[1] = 0;
            size[2] = 0;
            return;
        }

        final byte blockSize = data.read_uint8_t(1);
        final byte idxBits = data.read_uint8_t(2);
        final int realNumBlocks = data.read_le_u32(4);
        final int numBlocks = realNumBlocks + data.read_uint8_t(3);
        final byte maxLen = data.read_uint8_t(8);
        final byte minLen = data.read_uint8_t(9);
        final int h = maxLen - minLen + 1;
        final int numSyms = data.read_le_u16(10 + 2 * h);


        this.blockSize = blockSize;
        this.idxBits = idxBits;
        this.offset = data.createU_INT16_PTR(10);
        this.symLen = new byte[numSyms];
        this.symPat = data.clone().incPtr(12 + 2 * h);
        this.minLen = minLen;
        this.base = new long[h];

        ptr.ptr = data.ptr + (12 + 2 * h + 3 * numSyms + (numSyms & 1));

        int num_indices = (tb_size + (1 << idxBits) - 1) >>> idxBits;
        size[0] = 6 * num_indices;
        size[1] = 2 * numBlocks;
        size[2] = realNumBlocks << blockSize;

        assert (numSyms < TB_MAX_SYMS);
        byte[] tmp = new byte[TB_MAX_SYMS];

        for (int s = 0; s < numSyms; s++) {
            if (tmp[s] == 0) {
                calc_symLen(s, tmp);
            }
        }

        this.base[h - 1] = 0;
        for (int i = h - 2; i >= 0; i--) {
            this.base[i] = (this.base[i + 1] + this.offset.read_le_u16(i) - this.offset.read_le_u16(i + 1)) / 2;
        }
        for (int i = 0; i < h; i++) {
            this.base[i] <<= 64 - (minLen + i);
        }

        // offset is a two byte pointer
        this.offset.incPtr(-this.minLen);
    }

    void calc_symLen(int s, byte[] tmp) {
        U_INT8_PTR w = this.symPat.clone();
        w.incPtr(3 * s);
        int w2 = (w.read_uint8_t(2) & 0xFF) << 4;
        int w1 = (w.read_uint8_t(1) & 0xFF) >>> 4;

        int s2 = w2 | w1;
        if (s2 == 0x0fff) {
            this.symLen[s] = 0;
        } else {
            int s1 = ((w.read_uint8_t(1) & 0xF) << 8) | (w.read_uint8_t(0) & 0xFF);
            if (tmp[s1] == 0) calc_symLen(s1, tmp);
            if (tmp[s2] == 0) calc_symLen(s2, tmp);
            this.symLen[s] = (byte) (this.symLen[s1] + this.symLen[s2] + 0x01);
        }
        tmp[s] = 1;
    }


    byte[] decompress_pairs(int idx) {
        if (idxBits == 0) {
            return constValue;
        }

        int mainIdx = idx >>> idxBits;
        int litIdx = (idx & ((1 << idxBits) - 1)) - (1 << (idxBits - 1));
        int block = indexTable.read_le_u32(6 * mainIdx);

        short idxOffset = indexTable.read_le_u16(6 * mainIdx + 4);
        litIdx += (idxOffset & 0xFFFF);

        if (litIdx < 0) {
            while (litIdx < 0) {
                litIdx += (sizeTable.read_le_u16(--block) & 0xFFFF) + 1;
            }
        } else {
            while (litIdx > (sizeTable.read_le_u16(block) & 0xFFFF)) {
                litIdx -= (sizeTable.read_le_u16(block++) & 0xFFFF) + 1;
            }
        }

        U_INT32_PTR ptr = data.createU_INT32_PTR(block << blockSize);

        int m = minLen;
        int sym, bitCnt;

        long code = Long.reverseBytes(ptr.read_le_u64(0));

        ptr.incPtr(2);
        bitCnt = 0;     // number of "empty bits" in code
        for (; ; ) {
            int l = m;
            while (Long.compareUnsigned(code, base[l - m]) < 0) l++;
            sym = offset.read_le_u16(l);
            sym += (int) ((code - base[l - m]) >>> (64 - l));
            if (litIdx < (symLen[sym] & 0xFF) + 1) break;
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
            U_INT8_PTR w = symPat.clone().incPtr(3 * sym);
            int s1 = (((w.read_uint8_t(1) & 0x0F) << 8) | (w.read_uint8_t(0) & 0xFF));
            if (litIdx < (symLen[s1] & 0xFF) + 1) {
                sym = s1;
            } else {
                litIdx -= (symLen[s1] & 0xFF) + 1;
                sym = ((w.read_uint8_t(2) & 0xFF) << 4) | ((w.read_uint8_t(1) & 0xFF) >>> 4);
            }
        }

        return new byte[]{symPat.read_uint8_t(3 * sym), symPat.read_uint8_t(3 * sym + 1)};
    }
}
