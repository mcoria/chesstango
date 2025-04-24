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
}
