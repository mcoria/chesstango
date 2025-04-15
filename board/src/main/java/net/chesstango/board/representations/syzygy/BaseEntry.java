package net.chesstango.board.representations.syzygy;

/**
 * @author Mauricio Coria
 */
class BaseEntry {
    long key;
    char num;
    boolean[] ready = new boolean[3];
    boolean symmetric;
    boolean hasPawns;
    boolean hasDtm;
    boolean hasDtz;

    boolean kk_enc;
    char[] pawns = new char[2];

    boolean dtmLossOnly;
}
