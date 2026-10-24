package net.chesstango.search.alphabeta.killermoves;

import net.chesstango.board.moves.Move;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.debug.DebugNodeTracker;
import net.chesstango.search.alphabeta.debug.model.DebugNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class KillerMovesDebugTest {

    private KillerMovesDebug killerMovesDebug;

    @Mock
    private KillerMoves imp;

    @Mock
    private DebugNodeTracker debugNodeTracker;

    @Mock
    private DebugNode debugNode;

    @Mock
    private Visitor visitor;

    @Mock
    private Move move1;

    @Mock
    private Move move2;

    @Mock
    private Move move3;

    @BeforeEach
    public void setUp() {
        killerMovesDebug = new KillerMovesDebug();
        killerMovesDebug.setImp(imp);
        killerMovesDebug.setDebugNodeTracker(debugNodeTracker);
    }

    @Test
    public void testAccept() {
        killerMovesDebug.accept(visitor);

        verify(visitor).visit(killerMovesDebug);
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(imp, killerMovesDebug.getImp());
        assertEquals(debugNodeTracker, killerMovesDebug.getDebugNodeTracker());

        KillerMoves newImp = mock(KillerMoves.class);
        DebugNodeTracker newTracker = mock(DebugNodeTracker.class);

        killerMovesDebug.setImp(newImp);
        killerMovesDebug.setDebugNodeTracker(newTracker);

        assertEquals(newImp, killerMovesDebug.getImp());
        assertEquals(newTracker, killerMovesDebug.getDebugNodeTracker());
    }

    @Test
    public void testTrackKillerMoveSuccess() {
        when(imp.trackKillerMove(move1, 1)).thenReturn(true);
        when(debugNodeTracker.getCurrentNode()).thenReturn(debugNode);

        boolean result = killerMovesDebug.trackKillerMove(move1, 1);

        assertTrue(result);
        verify(imp).trackKillerMove(move1, 1);
        verify(debugNodeTracker).getCurrentNode();
        verify(debugNode).setKillerMove(move1);
    }

    @Test
    public void testTrackKillerMoveFailure() {
        when(imp.trackKillerMove(move1, 1)).thenReturn(false);

        boolean result = killerMovesDebug.trackKillerMove(move1, 1);

        assertFalse(result);
        verify(imp).trackKillerMove(move1, 1);
        verifyNoInteractions(debugNodeTracker);
    }

    @Test
    public void testIsKillerWhenNotKiller() {
        when(imp.isKiller(move1, 1)).thenReturn(false);

        boolean result = killerMovesDebug.isKiller(move1, 1);

        assertFalse(result);
        verify(imp).isKiller(move1, 1);
        verifyNoInteractions(debugNodeTracker);
    }

    @Test
    public void testIsKillerSetsTableAWhenNull() {
        when(imp.isKiller(move1, 1)).thenReturn(true);
        when(debugNodeTracker.getCurrentNode()).thenReturn(debugNode);
        when(debugNode.getKillerMovesTableA()).thenReturn(null);

        boolean result = killerMovesDebug.isKiller(move1, 1);

        assertTrue(result);
        verify(imp).isKiller(move1, 1);
        verify(debugNode).setKillerMovesTableA(move1);
        verify(debugNode, never()).setKillerMovesTableB(any());
    }

    @Test
    public void testIsKillerSetsTableAWhenEqualsTableA() {
        when(imp.isKiller(move1, 1)).thenReturn(true);
        when(debugNodeTracker.getCurrentNode()).thenReturn(debugNode);
        when(debugNode.getKillerMovesTableA()).thenReturn(move1);

        boolean result = killerMovesDebug.isKiller(move1, 1);

        assertTrue(result);
        verify(imp).isKiller(move1, 1);
        verify(debugNode).setKillerMovesTableA(move1);
        verify(debugNode, never()).setKillerMovesTableB(any());
    }

    @Test
    public void testIsKillerSetsTableBWhenTableAOccupiedAndTableBNull() {
        when(imp.isKiller(move2, 1)).thenReturn(true);
        when(debugNodeTracker.getCurrentNode()).thenReturn(debugNode);
        when(debugNode.getKillerMovesTableA()).thenReturn(move1);
        when(debugNode.getKillerMovesTableB()).thenReturn(null);

        boolean result = killerMovesDebug.isKiller(move2, 1);

        assertTrue(result);
        verify(imp).isKiller(move2, 1);
        verify(debugNode).setKillerMovesTableB(move2);
    }

    @Test
    public void testIsKillerSetsTableBWhenEqualsTableB() {
        when(imp.isKiller(move2, 1)).thenReturn(true);
        when(debugNodeTracker.getCurrentNode()).thenReturn(debugNode);
        when(debugNode.getKillerMovesTableA()).thenReturn(move1);
        when(debugNode.getKillerMovesTableB()).thenReturn(move2);

        boolean result = killerMovesDebug.isKiller(move2, 1);

        assertTrue(result);
        verify(imp).isKiller(move2, 1);
        verify(debugNode).setKillerMovesTableB(move2);
    }

    @Test
    public void testIsKillerThrowsExceptionWhenBothTablesOccupiedWithDifferentMoves() {
        when(imp.isKiller(move3, 1)).thenReturn(true);
        when(debugNodeTracker.getCurrentNode()).thenReturn(debugNode);
        when(debugNode.getKillerMovesTableA()).thenReturn(move1);
        when(debugNode.getKillerMovesTableB()).thenReturn(move2);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> killerMovesDebug.isKiller(move3, 1));
        assertEquals("Ya se encuentran 2 movimientos presentes en la tabla killermoves", exception.getMessage());
    }

    @Test
    public void testReset() {
        killerMovesDebug.reset();

        verify(imp).reset();
        verifyNoMoreInteractions(imp);
    }
}
