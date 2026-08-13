package net.chesstango.search.smart.alphabeta.debug.filters;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.position.Position;
import net.chesstango.search.Bound;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.debug.SearchTracker;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.pv.model.TriangularPVTable;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Disabled
public class DebugFilterTest {

    @Mock
    private DebugNode.NodeTopology topology;

    @Mock
    private AlphaBetaFilter nextFilter;

    @Mock
    private Game game;

    @Mock
    private SearchTracker searchTracker;

    @Mock
    private TriangularPVTable pvTable;

    @Mock
    private DebugNode debugNode;

    @Mock
    private Position position;

    @InjectMocks
    private DebugFilter debugFilter;



    @Test
    public void testAlphaBetaExactBound() {
        when(game.getPosition()).thenReturn(position);
        when(position.getCurrentTurn()).thenReturn(Color.WHITE);

        when(searchTracker.newNode(topology, 0)).thenReturn(debugNode);
        when(pvTable.getPV(0)).thenReturn(new short[]{});
        when(nextFilter.alphaBeta(0, -100, 100)).thenReturn(50);

        int result = debugFilter.alphaBeta(0, -100, 100);

        assertEquals(50, result);
        verify(debugNode).setDebugSearch("WHITE", -100, 100);
        verify(debugNode).setValue(50);
        verify(debugNode).setPv(any());
        verify(debugNode).setBound(Bound.EXACT);
        verify(debugNode).setType(DebugNode.NodeType.PV);
        verify(searchTracker).save();
    }

    @Test
    public void testAlphaBetaUpperBound() {
        when(game.getPosition()).thenReturn(position);
        when(position.getCurrentTurn()).thenReturn(Color.BLACK);

        when(searchTracker.newNode(topology, 0)).thenReturn(debugNode);
        when(pvTable.getPV(0)).thenReturn(new short[]{});
        when(nextFilter.alphaBeta(0, -100, 100)).thenReturn(-150);

        int result = debugFilter.alphaBeta(0, -100, 100);

        assertEquals(-150, result);
        verify(debugNode).setDebugSearch("BLACK", -100, 100);
        verify(debugNode).setValue(-150);
        verify(debugNode).setPv(any());
        verify(debugNode).setBound(Bound.UPPER_BOUND);
        verify(debugNode).setType(DebugNode.NodeType.ALL);
        verify(searchTracker).save();
    }

    @Test
    public void testAlphaBetaLowerBound() {
        when(game.getPosition()).thenReturn(position);
        when(position.getCurrentTurn().toString()).thenReturn("WHITE");

        when(searchTracker.newNode(topology, 0)).thenReturn(debugNode);
        when(pvTable.getPV(0)).thenReturn(new short[]{});
        when(nextFilter.alphaBeta(0, -100, 100)).thenReturn(150);

        int result = debugFilter.alphaBeta(0, -100, 100);

        assertEquals(150, result);
        verify(debugNode).setDebugSearch("WHITE", -100, 100);
        verify(debugNode).setValue(150);
        verify(debugNode).setPv(any());
        verify(debugNode).setBound(Bound.LOWER_BOUND);
        verify(debugNode).setType(DebugNode.NodeType.CUT);
        verify(searchTracker).save();
    }
}