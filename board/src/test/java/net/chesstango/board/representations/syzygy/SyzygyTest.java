package net.chesstango.board.representations.syzygy;

import net.chesstango.board.position.Position;
import net.chesstango.board.representations.fen.FEN;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mauricio Coria
 */
public class SyzygyTest {

    public static final String PATH = "C:\\java\\projects\\chess\\chess-utils\\books\\syzygy\\3-4-5";

    private Syzygy syzygy;

    @BeforeEach
    public void setUp() throws Exception {
        syzygy = new Syzygy();
    }

    @Test
    public void test_tb_init() {
        syzygy.tb_init(PATH);

        assertEquals(650, syzygy.pieceEntry.length);
        assertEquals(861, syzygy.pawnEntry.length);
        assertEquals(4096, syzygy.tbHash.length);

        assertEquals(3, syzygy.TB_LARGEST);
        assertEquals(3, syzygy.TB_MaxCardinality);
        assertEquals(0, syzygy.TB_MaxCardinalityDTM);
        assertEquals(4, syzygy.tbNumPiece);
        assertEquals(1, syzygy.tbNumPawn);
        assertEquals(5, syzygy.numWdl);
        assertEquals(0, syzygy.numDtm);
        assertEquals(5, syzygy.numDtz);
    }

    /**
     * Test for the "KQvK" tableType: tableType without PAWNs
     */
    @Test
    public void test_init_tb_KQvK() {
        syzygy.setPath(PATH);
        syzygy.init_tb("KQvK");

        assertEquals(1, syzygy.numWdl);
        assertEquals(0, syzygy.numDtm);
        assertEquals(1, syzygy.numDtz);

        /**
         * PieceEntry assertions
         */
        PieceEntry pieceEntry = syzygy.pieceEntry[0];

        assertEquals("KQvK", pieceEntry.tableName);
        assertEquals(0xa3ec1abc71e90863L, pieceEntry.key);
        assertEquals(3, pieceEntry.num);
        assertFalse(pieceEntry.symmetric);
        assertFalse(pieceEntry.kk_enc);

        assertFalse(pieceEntry.dtmLossOnly);

        assertEquals(662, pieceEntry.dtzMap.ptr);
        assertArrayEquals(new short[]{0, 0, 0, 0}, pieceEntry.dtzMapIdx);
        assertEquals(0, pieceEntry.dtzFlags);

        /**
         * HashEntry assertions
         */
        Syzygy.HashEntry tbHash = null;

        tbHash = syzygy.tbHash[2622];
        assertEquals(0xa3ec1abc71e90863L, tbHash.key);
        assertSame(tbHash.ptr, pieceEntry);

        tbHash = syzygy.tbHash[3438];
        assertEquals(0xd6e4e47d24962951L, tbHash.key);
        assertSame(tbHash.ptr, pieceEntry);
    }

    /**
     * Test for the "KQvKR" tableType: tableType without PAWNs
     */
    @Test
    public void test_init_tb_KQvKR() {
        syzygy.setPath(PATH);
        syzygy.init_tb("KQvKR");

        assertEquals(1, syzygy.numWdl);
        assertEquals(0, syzygy.numDtm);
        assertEquals(1, syzygy.numDtz);

        /**
         * PieceEntry assertions
         */
        PieceEntry pieceEntry = syzygy.pieceEntry[0];

        assertEquals("KQvKR", pieceEntry.tableName);
        assertEquals(0xA1648170ABA24CF8L, pieceEntry.key);
        assertEquals(4, pieceEntry.num);
        assertFalse(pieceEntry.symmetric);
        assertFalse(pieceEntry.kk_enc);

        assertFalse(pieceEntry.dtmLossOnly);

        assertEquals(12330, pieceEntry.dtzMap.ptr);
        assertArrayEquals(new short[]{(short) 1, (short) 33, (short) 36, (short) 37}, pieceEntry.dtzMapIdx);
        assertEquals(2, pieceEntry.dtzFlags);

        /**
         * HashEntry assertions
         */
        Syzygy.HashEntry tbHash = null;

        tbHash = syzygy.tbHash[2582];
        assertEquals(0xA1648170ABA24CF8L, tbHash.key);
        assertSame(tbHash.ptr, pieceEntry);

        tbHash = syzygy.tbHash[1780];
        assertEquals(0x6f42d01ce9295d4aL, tbHash.key);
        assertSame(tbHash.ptr, pieceEntry);
    }

    @Test
    public void test_tb_probe_root_KQvK() {
        syzygy.setPath(PATH);
        syzygy.init_tb("KQvK");

        FEN fen = FEN.of("7k/8/7K/7Q/8/8/8/8 w - - 0 1");
        Position chessPosition = fen.toChessPosition();
        BitPosition bitPosition = BitPosition.from(chessPosition);

        int[] results = new int[TB_MAX_MOVES];

        int res = syzygy.tb_probe_root(bitPosition, results);

        assertNotEquals(TB_RESULT_FAILED, res);

        assertEquals(TB_WIN, TB_GET_WDL(res));
        assertEquals(1, TB_GET_DTZ(res));

        assertEquals(15, count(results, TB_WIN));
        assertEquals(0, count(results, TB_CURSED_WIN));
        assertEquals(5, count(results, TB_DRAW));
        assertEquals(0, count(results, TB_BLESSED_LOSS));
        assertEquals(0, count(results, TB_LOSS));
    }

    /**
     * Test for the "KPvK" tableType: tableType with PAWNs
     */
    @Test
    public void test_init_tb_KPvK() {
        syzygy.setPath(PATH);
        syzygy.init_tb("KPvK");

        assertEquals(1, syzygy.numWdl);
        assertEquals(0, syzygy.numDtm);
        assertEquals(1, syzygy.numDtz);

        PawnEntry baseEntry = syzygy.pawnEntry[0];
        assertEquals(0xec0ade190c0f6003L, baseEntry.key);
        assertEquals(3, baseEntry.num);
        assertFalse(baseEntry.symmetric);
        assertEquals(1, baseEntry.pawns[0]);
        assertEquals(0, baseEntry.pawns[1]);

        Syzygy.HashEntry tbHash = null;

        tbHash = syzygy.tbHash[3776];
        assertEquals(0xec0ade190c0f6003L, tbHash.key);
        assertSame(tbHash.ptr, baseEntry);

        tbHash = syzygy.tbHash[2596];
        assertEquals(0xa24f0f571bb202e7L, tbHash.key);
        assertSame(tbHash.ptr, baseEntry);
    }


    static int count(int[] results, int wdl) {
        int count = 0;
        for (int i = 0; i < results.length && results[i] != TB_RESULT_FAILED; i++) {
            if (TB_GET_WDL(results[i]) == wdl) {
                count++;
            }
        }
        return count;
    }
}

