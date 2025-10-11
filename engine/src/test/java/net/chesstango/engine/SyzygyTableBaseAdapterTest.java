package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.piazzolla.syzygy.Syzygy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 */
@Disabled
public class SyzygyTableBaseAdapterTest {
    private SyzygyTableBaseAdapter syzygyTableBaseAdapter;
    private Path zygosityTablePath = Path.of("C:/java/projects/chess/chess-utils/books/syzygy/3-4-5");


    @BeforeEach
    public void setup() {
        Syzygy syzygy = Syzygy.open(zygosityTablePath);
        syzygyTableBaseAdapter = new SyzygyTableBaseAdapter(syzygy);
    }

    @Test
    public void test01() {
        Game game = Game.from(FEN.of("8/8/8/8/8/8/2Rk4/1K6 b - - 0 1"));
        syzygyTableBaseAdapter.setGame(game);
        assertTrue(syzygyTableBaseAdapter.isProbeAvailable());
        assertEquals(Evaluator.BLACK_LOST, syzygyTableBaseAdapter.evaluate());
    }

    @Test
    public void test02() {
        Game game = Game.from(FEN.of("8/8/8/8/8/3k4/2R5/1K6 w - - 0 1"));
        syzygyTableBaseAdapter.setGame(game);
        assertTrue(syzygyTableBaseAdapter.isProbeAvailable());
        assertEquals(Evaluator.WHITE_WON, syzygyTableBaseAdapter.evaluate());
    }
}
