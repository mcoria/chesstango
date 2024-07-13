package net.chesstango.uci.arena;

import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.engine.Tango;
import net.chesstango.search.dummy.Dummy;
import net.chesstango.uci.arena.gui.EngineController;
import net.chesstango.uci.arena.gui.EngineControllerImp;
import net.chesstango.uci.arena.gui.EngineControllerPoolFactory;
import net.chesstango.uci.arena.matchtypes.MatchByDepth;
import net.chesstango.uci.engine.UciTango;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */

public class MatchMultipleTest {

    private ObjectPool<EngineController> smartEnginePool;

    private ObjectPool<EngineController> dummyEnginePool;


    @BeforeEach
    public void setup() {
        smartEnginePool = new GenericObjectPool<>(new EngineControllerPoolFactory(() ->
                new EngineControllerImp(new UciTango())
                        .overrideEngineName("Smart")
        ));

        dummyEnginePool = new GenericObjectPool<>(new EngineControllerPoolFactory(() ->
                new EngineControllerImp(new UciTango(new Tango(new Dummy())))
                        .overrideEngineName("Dummy")
        ));
    }

    @Test
    public void testPlay() {
        MatchMultiple matchMultiple = new MatchMultiple(smartEnginePool, dummyEnginePool, new MatchByDepth(3));
        //matchMultiple.setDebugEnabled(true);

        List<MatchResult> matchResult = matchMultiple.play(Stream.of(FEN.of(FENDecoder.INITIAL_FEN)));

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
