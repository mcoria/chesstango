package net.chesstango.board.representations.syzygy;

import net.chesstango.board.position.Position;
import net.chesstango.board.representations.fen.FEN;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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


    @Test
    public void test_probeTable() {
        FEN fen = FEN.of("7k/8/7K/7Q/8/8/8/8 w - - 0 1");

        Position chessPosition = fen.toChessPosition();
        BitPosition bitPosition = BitPosition.from(chessPosition);

        syzygy.probe_table(bitPosition, TableType.WDL);
    }
}

