package net.chesstango.board.representations.syzygy;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.Encoding.*;
import static net.chesstango.board.representations.syzygy.TableType.DTM;
import static net.chesstango.board.representations.syzygy.TableType.DTZ;

class TableData {
    final BaseEntry baseEntry;
    final TableType table;

    FileChannel channel;
    MappedByteBuffer buffer;
    boolean ready;
    boolean error;

    public TableData(BaseEntry baseEntry, TableType table) {
        this.baseEntry = baseEntry;
        this.table = table;
    }

    public int probe_table(long key) {
        if (!ready) {
            if (!init_table()) {
                return 0;
            }
            ready = true;
        }
        return 0;
    }

    boolean init_table() {

        if (map_tb(table.getSuffix())) {

            /**
             * The main header of the tablebases file:
             * bytes 0-3: magic number
             */
            int magicNumber = read_le_u32(0);

            if (magicNumber != table.getMagicNumber()) {
                throw new RuntimeException("Corrupted file");
            }

            long dataPtr = 0;


            /**
             * byte 4:
             *      bit 0 is set for a non-symmetric table, i.e. separate wtm and btm.
             *      bit 1 is set for a pawnful table.
             *      bits 4-7: number of pieces N (N=5 for KRPvKR)
             */
            byte byte4 = read_uint8_t(4);

            boolean nonSymmetric = (byte4 & 0b00000001) != 0; //bit 0 is set for a non-symmetric table, i.e. separate wtm and btm.
            boolean pawnfulTable = (byte4 & 0b00000010) != 0; //bit 1 is set for a pawnful table.
            int numPieces = byte4 >>> 4;

            boolean split = table != DTZ && nonSymmetric;

            dataPtr += 5;
            int[][] tb_size = new int[6][2];
            int num = baseEntry.num_tables(table);

            BaseEntry.EncInfo[] ei = baseEntry.first_ei(table);

            SyzygyConstants.Encoding enc = !baseEntry.hasPawns() ? PIECE_ENC : table != DTM ? FILE_ENC : RANK_ENC;


            for (int t = 0; t < num; t++) {
                tb_size[t][0] = init_enc_info(ei[t], dataPtr, 0, t, enc);
                if (split)
                    tb_size[t][1] = init_enc_info(ei[num + t], dataPtr, 4, t, enc);

                //dataPtr += be.num + 1 + (be.hasPawns() && be.pawns[1] != 0);
            }


            return true;
        }

        return false;
    }

    boolean map_tb(String suffix) {
        Path pathToRead = Path.of(baseEntry.syzygy.path, String.format("%s%s", baseEntry.tableName, suffix));
        try {

            channel = (FileChannel) Files.newByteChannel(pathToRead, EnumSet.of(StandardOpenOption.READ));

            buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());

            buffer.order(ByteOrder.LITTLE_ENDIAN);

            return true;

        } catch (IOException e) {
            error = true;
        }
        return false;
    }

    int read_le_u32(int idx) {
        return buffer.getInt();
    }

    public byte read_uint8_t(int idx) {
        return buffer.get(idx);
    }

    private int init_enc_info(BaseEntry.EncInfo encInfo, long dataPtr, int i, int t, SyzygyConstants.Encoding enc) {
        return 0;
    }
}
