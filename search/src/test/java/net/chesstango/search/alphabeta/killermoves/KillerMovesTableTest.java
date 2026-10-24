package net.chesstango.search.alphabeta.killermoves;

import net.chesstango.board.moves.Move;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static net.chesstango.search.alphabeta.Constants.MAX_DEPTH;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class KillerMovesTableTest {

    private KillerMovesTable killerMovesTable;

    @Mock
    private Move move1;

    @Mock
    private Move move2;

    @Mock
    private Move move3;

    @BeforeEach
    public void setUp() {
        killerMovesTable = new KillerMovesTable();
    }

    @Test
    public void testTrackNonQuietMove() {
        when(move1.isQuiet()).thenReturn(false);

        boolean tracked = killerMovesTable.trackKillerMove(move1, 1);

        assertFalse(tracked);
        assertFalse(killerMovesTable.isKiller(move1, 1));
    }

    @Test
    public void testTrackQuietMove() {
        when(move1.isQuiet()).thenReturn(true);

        boolean tracked = killerMovesTable.trackKillerMove(move1, 1);

        assertTrue(tracked);
        assertTrue(killerMovesTable.isKiller(move1, 1));
        assertFalse(killerMovesTable.isKiller(move2, 1));
    }

    @Test
    public void testTrackDuplicateMoveA() {
        when(move1.isQuiet()).thenReturn(true);

        assertTrue(killerMovesTable.trackKillerMove(move1, 1));
        assertFalse(killerMovesTable.trackKillerMove(move1, 1));
        assertTrue(killerMovesTable.isKiller(move1, 1));
    }

    @Test
    public void testTrackSecondMoveShiftsAtoB() {
        when(move1.isQuiet()).thenReturn(true);
        when(move2.isQuiet()).thenReturn(true);

        assertTrue(killerMovesTable.trackKillerMove(move1, 1));
        assertTrue(killerMovesTable.trackKillerMove(move2, 1));

        assertTrue(killerMovesTable.isKiller(move1, 1));
        assertTrue(killerMovesTable.isKiller(move2, 1));
        assertFalse(killerMovesTable.isKiller(move3, 1));
    }

    @Test
    public void testTrackDuplicateMoveB() {
        when(move1.isQuiet()).thenReturn(true);
        when(move2.isQuiet()).thenReturn(true);

        assertTrue(killerMovesTable.trackKillerMove(move1, 1));
        assertTrue(killerMovesTable.trackKillerMove(move2, 1));

        // move1 is now in Table B, attempting to track move1 again should return false
        assertFalse(killerMovesTable.trackKillerMove(move1, 1));
        assertTrue(killerMovesTable.isKiller(move1, 1));
        assertTrue(killerMovesTable.isKiller(move2, 1));
    }

    @Test
    public void testTrackThirdMoveReplacesB() {
        when(move1.isQuiet()).thenReturn(true);
        when(move2.isQuiet()).thenReturn(true);
        when(move3.isQuiet()).thenReturn(true);

        assertTrue(killerMovesTable.trackKillerMove(move1, 1));
        assertTrue(killerMovesTable.trackKillerMove(move2, 1));
        assertTrue(killerMovesTable.trackKillerMove(move3, 1));

        // move3 is in Table A, move2 is in Table B, move1 was replaced
        assertTrue(killerMovesTable.isKiller(move3, 1));
        assertTrue(killerMovesTable.isKiller(move2, 1));
        assertFalse(killerMovesTable.isKiller(move1, 1));
    }

    @Test
    public void testPliesAreIndependent() {
        when(move1.isQuiet()).thenReturn(true);
        when(move2.isQuiet()).thenReturn(true);

        assertTrue(killerMovesTable.trackKillerMove(move1, 1));
        assertTrue(killerMovesTable.trackKillerMove(move2, 2));

        assertTrue(killerMovesTable.isKiller(move1, 1));
        assertFalse(killerMovesTable.isKiller(move1, 2));

        assertTrue(killerMovesTable.isKiller(move2, 2));
        assertFalse(killerMovesTable.isKiller(move2, 1));
    }

    @Test
    public void testMaxDepthBoundary() {
        when(move1.isQuiet()).thenReturn(true);

        assertTrue(killerMovesTable.trackKillerMove(move1, MAX_DEPTH));
        assertTrue(killerMovesTable.isKiller(move1, MAX_DEPTH));
        assertFalse(killerMovesTable.isKiller(move1, MAX_DEPTH - 1));
    }

    @Test
    public void testReset() {
        when(move1.isQuiet()).thenReturn(true);
        when(move2.isQuiet()).thenReturn(true);

        killerMovesTable.trackKillerMove(move1, 1);
        killerMovesTable.trackKillerMove(move2, MAX_DEPTH);

        assertTrue(killerMovesTable.isKiller(move1, 1));
        assertTrue(killerMovesTable.isKiller(move2, MAX_DEPTH));

        killerMovesTable.reset();

        assertFalse(killerMovesTable.isKiller(move1, 1));
        assertFalse(killerMovesTable.isKiller(move2, MAX_DEPTH));
    }
}
