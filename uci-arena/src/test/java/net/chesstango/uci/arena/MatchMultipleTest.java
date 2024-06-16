package net.chesstango.uci.arena;

import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.engine.Tango;
import net.chesstango.search.dummy.Dummy;
import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.arena.gui.EngineControllerImp;
import net.chesstango.uci.arena.gui.EngineControllerPoolFactory;
import net.chesstango.uci.arena.matchtypes.MatchByDepth;
import net.chesstango.uci.engine.UciTango;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */

public class MatchMultipleTest {

    private EngineControllerPoolFactory smartEngineControllerFactory1;

    private EngineControllerPoolFactory dummyEngineControllerFactory1;


    @BeforeEach
    public void setup() {
        smartEngineControllerFactory1 = new EngineControllerPoolFactory(() ->
                new EngineControllerImp(new UciTango())
                        .overrideEngineName("Smart")
        );

        dummyEngineControllerFactory1 = new EngineControllerPoolFactory(() ->
                new EngineControllerImp(new UciTango(new Tango(new Dummy())))
                        .overrideEngineName("Dummy")
        );
    }

    @Test
    public void testPlay() {
        MatchMultiple matchMultiple = new MatchMultiple(smartEngineControllerFactory1, dummyEngineControllerFactory1, new MatchByDepth(3));
        //matchMultiple.setDebugEnabled(true);

        List<MatchResult> matchResult = matchMultiple.play(List.of(FENDecoder.INITIAL_FEN));

        assertEquals(2, matchResult.size());

        // Deberia ganar el engine smartEngine
        assertEquals(2, matchResult.stream()
                .map(MatchResult::getWinner)
                .filter(Objects::nonNull)
                .map(EngineController::getEngineName)
                .filter("Smart"::equals)
                .count()
        );

    }


}
