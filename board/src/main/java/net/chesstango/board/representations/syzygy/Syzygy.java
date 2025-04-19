package net.chesstango.board.representations.syzygy;

import lombok.Setter;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.*;

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
 * So to know whether a 7-piece position is winning, losing or drawn (or cursed), the engine needs to do only a single probe of a 7-piece WDL tableType. (It may in addition have to do some probes of 6-piece WDL tables if any direct captures are available.)
 * If the engine needs to know the DTZ value (which is only necessary when a TB root position has been reached), the probing code may have to do a 1-ply search to get to the "right" side of the DTZ tableType.
 *
 * @author Mauricio Coria
 */
public class Syzygy {

    HashEntry[] tbHash = new HashEntry[1 << TB_HASHBITS];
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
     * tablebases using predefined tableType names. It also updates the largest
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

        // Initialize the first five tablebases with predefined tableType names starting with QUEEN (IMPORTANT TO KEEP THE ORDER for testing)
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


    public int probe_table(BitPosition bitPosition, TableType type) {
        long key = calcKey(bitPosition);

        int hashIdx = (int) (key >>> (64 - TB_HASHBITS));
        final int hashIdxStart = hashIdx;

        while (tbHash[hashIdx] == null || tbHash[hashIdx].key != key) {
            hashIdx = (hashIdx + 1) & ((1 << TB_HASHBITS) - 1);
            if (hashIdx == hashIdxStart) {
                return 0;
            }
        }

        BaseEntry be = tbHash[hashIdx].ptr;

        return be.probe_table(bitPosition, key, type);
    }


    /**
     * Initializes a tablebase entry for the given tableType name.
     * This method processes the tableType name to determine the pieces involved,
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
     * Adds a `BaseEntry` object to the hash tableType using the provided key.
     * This method calculates the index in the hash tableType based on the key
     * and resolves collisions using linear probing.
     *
     * @param ptr the `BaseEntry` object to be added to the hash tableType
     * @param key the unique key used to identify the entry in the hash tableType
     */
    void add_to_hash(BaseEntry ptr, long key) {

        int idx = (int) (key >>> (64 - TB_HASHBITS));

        while (tbHash[idx] != null) {
            idx = (idx + 1) & ((1 << TB_HASHBITS) - 1);
        }

        tbHash[idx] = new HashEntry();
        tbHash[idx].key = key;
        tbHash[idx].ptr = ptr;
        tbHash[idx].error = false;
    }

    /**
     * @author Mauricio Coria
     */
    static class HashEntry {
        long key;
        BaseEntry ptr;
        boolean error;
    }
}
