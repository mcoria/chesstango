package net.chesstango.board.representations.syzygy;

import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Mauricio Coria
 */
abstract class BaseEntry {
    long key;
    char num;

    TableData[] data = new TableData[3];
    boolean[] ready = new boolean[3];

    boolean symmetric;
    boolean hasDtm;
    boolean hasDtz;

    boolean kk_enc;
    char[] pawns = new char[2];

    boolean dtmLossOnly;

    record TableData(FileChannel channel, MappedByteBuffer buffer) {
        int read_le_u32() {
            return buffer.getInt();
        }

        public byte read_uint8_t(int idx) {
            return buffer.get(idx);
        }
    }

    abstract boolean hasPawns();

    abstract int num_tables(SyzygyConstants.Table type);

    abstract EncInfo[] first_ei(SyzygyConstants.Table type);

    static class EncInfo{}
}
