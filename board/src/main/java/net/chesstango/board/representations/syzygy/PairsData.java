package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class PairsData {
    U_INT8_PTR indexTable;
    U_INT16_PTR sizeTable;
    U_INT8_PTR data;
    U_INT16_PTR offset;
    byte[] symLen;
    U_INT8_PTR symPat;
    byte blockSize;
    byte idxBits;
    byte minLen;
    byte[] constValue = new byte[2];
    long[] base;

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
