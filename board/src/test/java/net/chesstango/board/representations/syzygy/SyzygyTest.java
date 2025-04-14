package net.chesstango.board.representations.syzygy;

import net.chesstango.board.position.Position;
import net.chesstango.board.representations.fen.FEN;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.chesstango.board.representations.syzygy.Syzygy.TB_HASHBITS;
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

        syzygy.probeTable(chessPosition);
    }

    @Test
    public void test_tb_init() {
        syzygy.tb_init("C:\\java\\projects\\chess\\chess-utils\\books\\syzygy\\3-4-5");

        assertEquals(650, syzygy.pieceEntry.length);
        assertEquals(861, syzygy.pawnEntry.length);
        assertEquals(4096, syzygy.tbHash.length);
    }

    @Test
    public void test_init_tb() {
        syzygy.init_tb("KQvK");
    }

    @Test
    public void test_toPcsArray() {
        int[] pcs = syzygy.toPcsArray("KQvK");
        assertArrayEquals(new int[]{0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0}, pcs);
    }

    @Test
    public void test_toPosition() {
        FEN fen = FEN.of("7k/8/7K/7Q/8/8/8/8 w - - 0 1");
        Position chessPosition = fen.toChessPosition();

        Syzygy.BitPosition bitPosition = syzygy.toPosition(chessPosition);

        assertEquals(0x0000808000000000L, bitPosition.white());
        assertEquals(0x8000000000000000L, bitPosition.black());
        assertEquals(0x8000800000000000L, bitPosition.kings());
        assertEquals(0x0000008000000000L, bitPosition.queens());
        assertEquals(0x0000000000000000L, bitPosition.rooks());
        assertEquals(0x0000000000000000L, bitPosition.bishops());
        assertEquals(0x0000000000000000L, bitPosition.knights());
        assertEquals(0x0000000000000000L, bitPosition.pawns());
        assertEquals(0x00L, bitPosition.rule50());
        assertEquals(0x00L, bitPosition.ep());
        assertTrue(bitPosition.turn());
    }

    @Test
    public void test_calcKey() {
        FEN fen = FEN.of("7k/8/7K/7Q/8/8/8/8 w - - 0 1");

        Position chessPosition = fen.toChessPosition();

        Syzygy.BitPosition bitPosition = syzygy.toPosition(chessPosition);

        assertEquals(0xa3ec1abc71e90863L, syzygy.calcKey(bitPosition));

        assertEquals(2622, 0xa3ec1abc71e90863L >>> (64 - TB_HASHBITS));
    }
}

