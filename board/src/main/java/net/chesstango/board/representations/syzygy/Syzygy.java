package net.chesstango.board.representations.syzygy;

import lombok.Setter;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.Optional;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.*;
import static net.chesstango.board.representations.syzygy.SyzygyConstants.Encoding.*;
import static net.chesstango.board.representations.syzygy.SyzygyConstants.Table.DTM;
import static net.chesstango.board.representations.syzygy.SyzygyConstants.Table.DTZ;

/**
 * Syzygy Bases consist of two sets of files,
 * - WDL files (extension .rtbw) storing win/draw/loss information considering the fifty-move rule for access during search
 * - DTZ files (extension .rtbz) with distance-to-zero information for access at the root.
 * <p>
 * <p>
 * DTZ measures the shortest distance to zero the position (zeroing = pawn move, capture or checkmate),
 * without compromising the Win/Draw/Loss status. If White only has just sufficient material to mate,
 * then any zeroing is likely to be useful, but if White has way more material than is necessary,
 * White may try to sacrifice its own unit, and Black may try to refuse the sacrifice
 * <p>
 * WDL has full data for two sides but DTZ50 omitted data of one side to save space. Each endgame has a pair of those types.
 * <p>
 * Syzygy WDL is double-sided, DTZ is single-sided.
 * So to know whether a 7-piece position is winning, losing or drawn (or cursed), the engine needs to do only a single probe of a 7-piece WDL table. (It may in addition have to do some probes of 6-piece WDL tables if any direct captures are available.)
 * If the engine needs to know the DTZ value (which is only necessary when a TB root position has been reached), the probing code may have to do a 1-ply search to get to the "right" side of the DTZ table.
 *
 * @author Mauricio Coria
 */
public class Syzygy {

    TbHashEntry[] tbHash = new TbHashEntry[1 << TB_HASHBITS];
    PieceEntry[] pieceEntry = new PieceEntry[TB_MAX_PIECE];
    PawnEntry[] pawnEntry = new PawnEntry[TB_MAX_PAWN];

    int tbNumPiece;
    int tbNumPawn;
    int numWdl;
    int numDtm;
    int numDtz;
    int TB_MaxCardinality;
    int TB_MaxCardinalityDTM;
    int TB_LARGEST;

    @Setter
    String path;

    /**
     * Initializes the tablebase system with the specified path.
     * This method resets all counters and properties related to the tablebase,
     * sets the path for the tablebase files, and initializes the first five
     * tablebases using predefined table names. It also updates the largest
     * cardinality values based on the initialized tablebases.
     *
     * @param path the file path to the tablebase directory
     */
    public void tb_init(String path) {
        // Reset counters and properties
        tbNumPiece = 0;
        tbNumPawn = 0;
        numWdl = 0;
        numDtm = 0;
        numDtz = 0;
        TB_MaxCardinality = 0;
        TB_MaxCardinalityDTM = 0;
        TB_LARGEST = 0;

        // Set the path for the tablebase files
        setPath(path);

        // Initialize the first five tablebases with predefined table names starting with QUEEN (IMPORTANT TO KEEP THE ORDER for testing)
        for (int i = 0; i < 5; i++) {
            String tableName = String.format("K%cvK", SyzygyConstants.pchr(i));
            init_tb(tableName);
        }

        // Update the largest cardinality values
        TB_LARGEST = TB_MaxCardinality;
        if (TB_MaxCardinalityDTM > TB_LARGEST) {
            TB_LARGEST = TB_MaxCardinalityDTM;
        }
    }


    int probe_table(BitPosition bitPosition, Table type) {
        long key = calcKey(bitPosition);

        int hashIdx = (int) (key >>> (64 - TB_HASHBITS));
        final int hashIdxStart = hashIdx;

        while (tbHash[hashIdx] == null || tbHash[hashIdx].key != key) {
            hashIdx = (hashIdx + 1) & ((1 << TB_HASHBITS) - 1);
            if(hashIdx == hashIdxStart) {
                return 0;
            }
        }

        BaseEntry be = tbHash[hashIdx].ptr;
        if (DTM.equals(type) && !be.hasDtm || DTZ.equals(type) && !be.hasDtz) {
            return 0;
        }

        if (!be.ready[type.ordinal()]) {
            String table = prt_str(bitPosition, be.key != key);
            if (!init_table(be, table, type)) {
                return 0;
            }
            be.ready[type.ordinal()] = true;
        }

        return 0;
    }

    boolean init_table(BaseEntry be, String tableName, Table type) {
        Optional<BaseEntry.TableData> opData = map_tb(tableName, type.getSuffix());

        if (opData.isPresent()) {
            BaseEntry.TableData data = opData.get();

            /**
             * The main header of the tablebases file:
             * bytes 0-3: magic number
             */
            int magicNumber = data.read_le_u32();

            if (magicNumber != tbMagic[type.ordinal()]) {
                throw new RuntimeException("Corrupted file");
                // handle close
            }

            long dataPtr = 0;
            be.data[type.ordinal()] = data;


            /**
             * byte 4:
             *      bit 0 is set for a non-symmetric table, i.e. separate wtm and btm.
             *      bit 1 is set for a pawnful table.
             *      bits 4-7: number of pieces N (N=5 for KRPvKR)
             */
            byte byte4 = data.read_uint8_t(4);

            boolean nonSymmetric = (byte4 & 0b00000001) != 0; //bit 0 is set for a non-symmetric table, i.e. separate wtm and btm.
            boolean pawnfulTable = (byte4 & 0b00000010) != 0; //bit 1 is set for a pawnful table.
            int numPieces = byte4 >>> 4;

            boolean split = type != DTZ && nonSymmetric;

            dataPtr += 5;
            int[][] tb_size = new int[6][2];
            int num = be.num_tables(type);

            BaseEntry.EncInfo[] ei = be.first_ei(type);

            Encoding enc = !be.hasPawns() ? PIECE_ENC : type != DTM ? FILE_ENC : RANK_ENC;


            for (int t = 0; t < num; t++) {
                tb_size[t][0] = init_enc_info(ei[t], be, dataPtr, 0, t, enc);
                if (split)
                    tb_size[t][1] = init_enc_info(ei[num + t], be, dataPtr, 4, t, enc);

                //dataPtr += be.num + 1 + (be.hasPawns() && be.pawns[1] != 0);
            }


            return true;
        }

        return false;
    }

    private int init_enc_info(BaseEntry.EncInfo encInfo, BaseEntry be, long dataPtr, int i, int t, Encoding enc) {
        return 0;
    }

    Optional<BaseEntry.TableData> map_tb(String tableName, String suffix) {
        Path pathToRead = Path.of(path, String.format("%s%s", tableName, suffix));
        try {

            FileChannel channel = (FileChannel) Files.newByteChannel(pathToRead, EnumSet.of(StandardOpenOption.READ));

            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());

            buffer.order(ByteOrder.LITTLE_ENDIAN);

            return Optional.of(new BaseEntry.TableData(channel, buffer));
        } catch (IOException e) {
            return Optional.empty();
        }
    }


    /**
     * Initializes a tablebase entry for the given table name.
     * This method processes the table name to determine the pieces involved,
     * calculates unique keys for the tablebase, and sets up the corresponding
     * `BaseEntry` object with relevant attributes. It also updates global counters
     * and properties related to the tablebase.
     *
     * @param tbName the name of the tablebase to initialize
     */
    void init_tb(String tbName) {
        BaseEntry baseEntry = null;
        if (tbName.toUpperCase().contains("P")) {
            baseEntry = new PawnEntry(this);
        } else {
            baseEntry = new PieceEntry(this);
        }
        baseEntry.init_tb(tbName);
    }

    /**
     * Adds a `BaseEntry` object to the hash table using the provided key.
     * This method calculates the index in the hash table based on the key
     * and resolves collisions using linear probing.
     *
     * @param ptr the `BaseEntry` object to be added to the hash table
     * @param key the unique key used to identify the entry in the hash table
     */
    void add_to_hash(BaseEntry ptr, long key) {

        int idx = (int) (key >>> (64 - TB_HASHBITS));

        while (tbHash[idx] != null) {
            idx = (idx + 1) & ((1 << TB_HASHBITS) - 1);
        }

        tbHash[idx] = new TbHashEntry();
        tbHash[idx].key = key;
        tbHash[idx].ptr = ptr;
        tbHash[idx].error = false;
    }
}
