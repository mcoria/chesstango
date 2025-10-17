package net.chesstango.engine;

import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.piazzolla.syzygy.Syzygy;
import net.chesstango.piazzolla.syzygy.SyzygyPosition;
import net.chesstango.piazzolla.syzygy.SyzygyPositionBuilder;
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
public class SyzygyAdapterTest {
    private SyzygyAdapter syzygyAdapter;

    @Mock
    private Syzygy syzygy;

    @BeforeEach
    public void setup() {
        when(syzygy.tb_largest()).thenReturn(5);
        syzygyAdapter = new SyzygyAdapter(syzygy);
    }

    @Test
    public void whiteTurn_win() {
        when(syzygy.tb_probe_wdl(any(SyzygyPosition.class))).thenReturn(Syzygy.TB_WIN);

        Game game = Game.from(FEN.of("8/8/8/8/8/3k4/2R5/1K6 w - - 0 1"));
        syzygyAdapter.setGame(game);
        assertTrue(syzygyAdapter.isProbeAvailable());
        assertEquals(Evaluator.WHITE_WON, syzygyAdapter.evaluate());
    }

    @Test
    public void whiteTurn_loss() {
        when(syzygy.tb_probe_wdl(any(SyzygyPosition.class))).thenReturn(Syzygy.TB_LOSS);

        Game game = Game.from(FEN.of("8/8/8/8/8/3k4/2r5/1K6 w - - 0 1"));
        syzygyAdapter.setGame(game);
        assertTrue(syzygyAdapter.isProbeAvailable());
        assertEquals(Evaluator.WHITE_LOST, syzygyAdapter.evaluate());
    }

    @Test
    public void blackTurn_win() {
        when(syzygy.tb_probe_wdl(any(SyzygyPosition.class))).thenReturn(Syzygy.TB_WIN);

        Game game = Game.from(FEN.of("8/8/8/8/8/3k4/2r5/1K6 b - - 0 1"));
        syzygyAdapter.setGame(game);
        assertTrue(syzygyAdapter.isProbeAvailable());
        assertEquals(Evaluator.BLACK_WON, syzygyAdapter.evaluate());
    }

    @Test
    public void blackTurn_loss() {
        when(syzygy.tb_probe_wdl(any(SyzygyPosition.class))).thenReturn(Syzygy.TB_LOSS);

        Game game = Game.from(FEN.of("8/8/8/8/8/8/2Rk4/1K6 b - - 0 1"));
        syzygyAdapter.setGame(game);
        assertTrue(syzygyAdapter.isProbeAvailable());
        assertEquals(Evaluator.BLACK_LOST, syzygyAdapter.evaluate());
    }

    @Test
    public void testBindSyzygyPosition01() {
        Game game = Game.from(FEN.of("8/8/8/8/8/3k4/2R5/1K6 w - - 0 1"));

        SyzygyPositionBuilder positionBuilder = new SyzygyPositionBuilder();
        game.getPosition().export(positionBuilder);
        SyzygyPosition syzygyPositionExpected = positionBuilder.getPositionRepresentation();

        syzygyAdapter.setGame(game);
        SyzygyPosition syzygyPositionActual = syzygyAdapter.bindSyzygyPosition();

        assertEquals(syzygyPositionExpected, syzygyPositionActual);
    }

    @Test
    public void testBindSyzygyPosition02() {
        Game game = Game.from(FEN.of("8/8/8/8/8/3k4/2R5/1K6 w - - 3 10"));

        SyzygyPositionBuilder positionBuilder = new SyzygyPositionBuilder();
        game.getPosition().export(positionBuilder);
        SyzygyPosition syzygyPositionExpected = positionBuilder.getPositionRepresentation();

        syzygyAdapter.setGame(game);
        SyzygyPosition syzygyPositionActual = syzygyAdapter.bindSyzygyPosition();

        assertEquals(syzygyPositionExpected, syzygyPositionActual);
    }
}
