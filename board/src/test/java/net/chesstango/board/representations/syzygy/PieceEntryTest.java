package net.chesstango.board.representations.syzygy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void test_init_table_KQvK() {
        syzygy.setPath("C:\\java\\projects\\chess\\chess-utils\\books\\syzygy\\3-4-5");
        pieceEntry.init_tb("KQvK");

        assertEquals("KQvK", pieceEntry.tableName);
        assertEquals(0xa3ec1abc71e90863L, pieceEntry.key);
        assertEquals(3, pieceEntry.num);
        assertFalse(pieceEntry.symmetric);
        assertFalse(pieceEntry.kk_enc);


        PieceAsymmetric wdl = (PieceAsymmetric) pieceEntry.wdl;
        TableBase.EncInfo ei0 = wdl.ei[0];

        assertArrayEquals(new long[]{1, 0, 0, 0, 0, 0, 0}, ei0.factor);
        assertArrayEquals(new byte[]{14, 6, 5, 0, 0, 0, 0}, ei0.pieces);
        assertArrayEquals(new byte[]{3, 0, 0, 0, 0, 0, 0}, ei0.norm);
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
