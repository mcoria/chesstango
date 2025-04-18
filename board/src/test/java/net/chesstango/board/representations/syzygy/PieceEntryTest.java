package net.chesstango.board.representations.syzygy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * @author Mauricio Coria
 */
public class PieceEntryTest {

    private Syzygy syzygy;
    private PieceEntry pieceEntry;

    @BeforeEach
    public void setUp() throws Exception {
        syzygy = new Syzygy();
        pieceEntry = new PieceEntry(syzygy);
    }

    /**
     * Test for the "KQvK" table: table without PAWNs
     */
    @Test
    public void test_init_tb_KQvK() {
        syzygy.setPath("C:\\java\\projects\\chess\\chess-utils\\books\\syzygy\\3-4-5");
        syzygy.init_tb("KQvK");

        assertEquals(1, syzygy.numWdl);
        assertEquals(0, syzygy.numDtm);
        assertEquals(1, syzygy.numDtz);

        PieceEntry baseEntry = syzygy.pieceEntry[0];
        assertEquals(0xa3ec1abc71e90863L, baseEntry.key);
        assertEquals(3, baseEntry.num);
        assertFalse(baseEntry.symmetric);
        assertFalse(baseEntry.kk_enc);

        Syzygy.HashEntry tbHash = null;

        tbHash = syzygy.tbHash[2622];
        assertEquals(0xa3ec1abc71e90863L, tbHash.key);
        assertSame(tbHash.ptr, baseEntry);

        tbHash = syzygy.tbHash[3438];
        assertEquals(0xd6e4e47d24962951L, tbHash.key);
        assertSame(tbHash.ptr, baseEntry);
    }

    @Test
    public void test_init_table_KQvK() {
        syzygy.setPath("C:\\java\\projects\\chess\\chess-utils\\books\\syzygy\\3-4-5");
        pieceEntry.init_tb("KQvK");

        assertEquals("KQvK", pieceEntry.tableName);
        assertEquals(0xa3ec1abc71e90863L, pieceEntry.key);
        assertEquals(3, pieceEntry.num);
        assertFalse(pieceEntry.symmetric);
        assertFalse(pieceEntry.kk_enc);
    }

    @Test
    public void test_init_table_KQvKR() {
        syzygy.setPath("C:\\java\\projects\\chess\\chess-utils\\books\\syzygy\\3-4-5");
        pieceEntry.init_tb("KQvKR");


        assertEquals("KQvKR", pieceEntry.tableName);
        assertEquals(4, pieceEntry.num);
        assertFalse(pieceEntry.symmetric);
        assertFalse(pieceEntry.kk_enc);
    }
}
