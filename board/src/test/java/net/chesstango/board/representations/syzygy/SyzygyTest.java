package net.chesstango.board.representations.syzygy;

import net.chesstango.board.position.Position;
import net.chesstango.board.representations.fen.FEN;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.chesstango.board.representations.syzygy.SyzygyConstants.Table;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mauricio Coria
 */
public class SyzygyTest {

    private Syzygy syzygy;

    @BeforeEach
    public void setUp() throws Exception {
        syzygy = new Syzygy();
    }

    @Test
    public void test_probeTable() {
        FEN fen = FEN.of("7k/8/7K/7Q/8/8/8/8 w - - 0 1");

        Position chessPosition = fen.toChessPosition();
        BitPosition bitPosition = BitPosition.from(chessPosition);

        syzygy.probe_table(bitPosition, Table.WDL);
    }

    @Test
    public void test_tb_init() {
        syzygy.tb_init("C:\\java\\projects\\chess\\chess-utils\\books\\syzygy\\3-4-5");

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
     * Test for the "KQvK" table: table without PAWNs
     */
    @Test
    public void test_init_tb_KQvK() {
        syzygy.setPath("C:\\java\\projects\\chess\\chess-utils\\books\\syzygy\\3-4-5");
        syzygy.init_tb("KQvK");

        assertEquals(1, syzygy.numWdl);
        assertEquals(0, syzygy.numDtm);
        assertEquals(1, syzygy.numDtz);

        BaseEntry baseEntry = syzygy.pieceEntry[0];
        assertEquals(0xa3ec1abc71e90863L, baseEntry.key);
        assertEquals(3, baseEntry.num);
        assertFalse(baseEntry.symmetric);
        assertFalse(baseEntry.hasPawns());
        assertInstanceOf(PieceEntry.class, baseEntry);
        assertFalse(baseEntry.hasDtm);
        assertTrue(baseEntry.hasDtz);
        assertFalse(baseEntry.kk_enc);

        TbHashEntry tbHash = null;

        tbHash = syzygy.tbHash[2622];
        assertEquals(0xa3ec1abc71e90863L, tbHash.key);
        assertSame(tbHash.ptr, baseEntry);

        tbHash = syzygy.tbHash[3438];
        assertEquals(0xd6e4e47d24962951L, tbHash.key);
        assertSame(tbHash.ptr, baseEntry);
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

        BaseEntry baseEntry = syzygy.pawnEntry[0];
        assertEquals(0xec0ade190c0f6003L, baseEntry.key);
        assertEquals(3, baseEntry.num);
        assertFalse(baseEntry.symmetric);
        assertTrue(baseEntry.hasPawns());
        assertInstanceOf(PawnEntry.class, baseEntry);
        assertFalse(baseEntry.hasDtm);
        assertTrue(baseEntry.hasDtz);
        assertFalse(baseEntry.kk_enc);
        assertEquals(1, baseEntry.pawns[0]);
        assertEquals(0, baseEntry.pawns[1]);

        TbHashEntry tbHash = null;

        tbHash = syzygy.tbHash[3776];
        assertEquals(0xec0ade190c0f6003L, tbHash.key);
        assertSame(tbHash.ptr, baseEntry);

        tbHash = syzygy.tbHash[2596];
        assertEquals(0xa24f0f571bb202e7L, tbHash.key);
        assertSame(tbHash.ptr, baseEntry);
    }

    @Test
    public void test_init_table() {
        syzygy.setPath("C:\\java\\projects\\chess\\chess-utils\\books\\syzygy\\3-4-5");

        PieceEntry pieceEntry = new PieceEntry();
        pieceEntry.num = 3;
        pieceEntry.symmetric = false;
        pieceEntry.hasDtm = false;
        pieceEntry.hasDtz = true;
        pieceEntry.kk_enc = false;

        syzygy.init_table(pieceEntry, "KQvK", Table.WDL);

    }
}

