package net.chesstango.tools.tuning.fitnessfunctions;

import net.chesstango.uci.arena.MatchResult;
import net.chesstango.uci.arena.gui.EngineController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class FitnessByMatchTest {

    private FitnessByMatch fitnessFn;

    @Mock
    private EngineController engineTango;

    @Mock
    private EngineController opponent;


    @BeforeEach
    public void setup() {
        fitnessFn = new FitnessByMatch();

        when(engineTango.getEngineName()).thenReturn("TANGO");

        when(opponent.getEngineName()).thenReturn("OPPONENT");
    }


    @Test
    public void testCalculatePoints01() {
        List<MatchResult> results = List.of(new MatchResult("1", null, engineTango, opponent, engineTango));
        assertEquals(1L, fitnessFn.calculatePoints(results));
    }

    @Test
    public void testCalculatePoints02() {
        List<MatchResult> results = List.of(new MatchResult("1", null, engineTango, opponent, null));
        assertEquals(0L, fitnessFn.calculatePoints(results));
    }

    @Test
    public void testCalculatePoints03() {
        List<MatchResult> results = List.of(new MatchResult("1", null, opponent, engineTango, engineTango));
        assertEquals(1L, fitnessFn.calculatePoints(results));
    }

    @Test
    public void testCalculatePoints04() {
        List<MatchResult> results = List.of(new MatchResult("1", null, opponent, engineTango, null));
        assertEquals(0L, fitnessFn.calculatePoints(results));
    }

    @Test
    public void testCalculatePoints05() {
        List<MatchResult> results = List.of(new MatchResult("1", null, engineTango, opponent, engineTango),
                new MatchResult("2", null, engineTango, opponent, null),
                new MatchResult("3", null, opponent, engineTango, engineTango),
                new MatchResult("4", null, opponent, engineTango, null));
        assertEquals(2L, fitnessFn.calculatePoints(results));
    }


    @Test
    public void testCalculatePoints06() {
        List<MatchResult> results = List.of(new MatchResult("1", null, engineTango, opponent, opponent));
        assertEquals(-1L, fitnessFn.calculatePoints(results));
    }

    @Test
    public void testCalculatePoints07() {
        List<MatchResult> results = List.of(new MatchResult("1", null, opponent, engineTango, opponent));
        assertEquals(-1L, fitnessFn.calculatePoints(results));
    }


}
