package net.chesstango.board.representations.syzygy;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

/**
 * @author Mauricio Coria
 */
class MappedFile {
    FileChannel channel;
    MappedByteBuffer buffer;

    boolean map_tb(String basePath, String fileName, String suffix) {
        Path pathToRead = Path.of(basePath, String.format("%s%s", fileName, suffix));
        try {

            channel = (FileChannel) Files.newByteChannel(pathToRead, EnumSet.of(StandardOpenOption.READ));

            buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());

            buffer.order(ByteOrder.LITTLE_ENDIAN);

            return true;

        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
        return false;
    }

    byte read_uint8_t(int idx) {
        return buffer.get(idx);
    }

    int read_le_u32(int idx) {
        return buffer.getInt(idx);
    }

    char read_le_u16(int idx) {
        return buffer.getChar(idx);
    }

    short read_short(int idx) {
        return buffer.getShort(idx);
    }
}
