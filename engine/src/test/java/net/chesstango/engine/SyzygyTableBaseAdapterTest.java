package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.piazzolla.syzygy.Syzygy;
import net.chesstango.piazzolla.syzygy.SyzygyPosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class SyzygyTableBaseAdapterTest {
    private SyzygyTableBaseAdapter syzygyTableBaseAdapter;

    @Mock
    private Syzygy syzygy;

    @BeforeEach
    public void setup() {
        when(syzygy.tb_largest()).thenReturn(5);
        syzygyTableBaseAdapter = new SyzygyTableBaseAdapter(syzygy);
    }

    @Test
    public void whiteTurn_win() {
        when(syzygy.tb_probe_wdl(any(SyzygyPosition.class))).thenReturn(Syzygy.TB_WIN);

        Game game = Game.from(FEN.of("8/8/8/8/8/3k4/2R5/1K6 w - - 0 1"));
        syzygyTableBaseAdapter.setGame(game);
        assertTrue(syzygyTableBaseAdapter.isProbeAvailable());
        assertEquals(Evaluator.WHITE_WON, syzygyTableBaseAdapter.evaluate());
    }

    @Test
    public void whiteTurn_loss() {
        when(syzygy.tb_probe_wdl(any(SyzygyPosition.class))).thenReturn(Syzygy.TB_LOSS);

        Game game = Game.from(FEN.of("8/8/8/8/8/3k4/2r5/1K6 w - - 0 1"));
        syzygyTableBaseAdapter.setGame(game);
        assertTrue(syzygyTableBaseAdapter.isProbeAvailable());
        assertEquals(Evaluator.WHITE_LOST, syzygyTableBaseAdapter.evaluate());
    }

    @Test
    public void blackTurn_win() {
        when(syzygy.tb_probe_wdl(any(SyzygyPosition.class))).thenReturn(Syzygy.TB_WIN);

        Game game = Game.from(FEN.of("8/8/8/8/8/3k4/2r5/1K6 b - - 0 1"));
        syzygyTableBaseAdapter.setGame(game);
        assertTrue(syzygyTableBaseAdapter.isProbeAvailable());
        assertEquals(Evaluator.BLACK_WON, syzygyTableBaseAdapter.evaluate());
    }

    @Test
    public void blackTurn_loss() {
        when(syzygy.tb_probe_wdl(any(SyzygyPosition.class))).thenReturn(Syzygy.TB_LOSS);

        Game game = Game.from(FEN.of("8/8/8/8/8/8/2Rk4/1K6 b - - 0 1"));
        syzygyTableBaseAdapter.setGame(game);
        assertTrue(syzygyTableBaseAdapter.isProbeAvailable());
        assertEquals(Evaluator.BLACK_LOST, syzygyTableBaseAdapter.evaluate());
    }
}
