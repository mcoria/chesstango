package net.chesstango.board.representations.syzygy;

import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.Table.DTM;

/**
 * @author Mauricio Coria
 */
class BaseEntry {
    long key;
    char num;
    TableData[] data = new TableData[3];
    boolean[] ready = new boolean[3];
    boolean symmetric;
    boolean hasPawns;
    boolean hasDtm;
    boolean hasDtz;

    boolean kk_enc;
    char[] pawns = new char[2];

    boolean dtmLossOnly;

    record TableData(FileChannel channel, MappedByteBuffer buffer) {
        int read_le_u32() {
            return buffer.getInt();
        }
    }

    int num_tables(SyzygyConstants.Table type) {
        return hasPawns ? type == DTM ? 6 : 4 : 1;
    }
}
