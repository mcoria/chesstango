package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class EncInfo {
    PairsData precomp;
    long[] factor = new long[SyzygyConstants.TB_PIECES];
    byte[] pieces = new byte[SyzygyConstants.TB_PIECES];
    byte[] norm = new byte[SyzygyConstants.TB_PIECES];
}
