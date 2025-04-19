package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class PairsData {
    BytePTR indexTable;
    CharPTR sizeTable;
    BytePTR data;
    CharPTR offset;
    byte[] symLen;
    BytePTR symPat;
    byte blockSize;
    byte idxBits;
    byte minLen;
    byte[] constValue = new byte[2];
    long[] base = new long[1];
}
