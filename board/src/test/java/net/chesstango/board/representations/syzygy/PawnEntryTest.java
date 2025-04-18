package net.chesstango.board.representations.syzygy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mauricio Coria
 */
public class PawnEntryTest {

    private Syzygy syzygy;
    private PieceEntry pieceEntry;

    @BeforeEach
    public void setUp() throws Exception {
        syzygy = new Syzygy();
        pieceEntry = new PieceEntry(syzygy);
    }

    /**
     * Test for the "KPvK" table: table with PAWNs
     */
    @Test
    public void test_init_tb_KPvK() {
        syzygy.setPath("C:\\java\\projects\\chess\\chess-utils\\books\\syzygy\\3-4-5");
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
}
