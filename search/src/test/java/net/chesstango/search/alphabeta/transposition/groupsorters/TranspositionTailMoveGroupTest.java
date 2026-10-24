package net.chesstango.search.alphabeta.transposition.groupsorters;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.transposition.TTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class TranspositionTailMoveGroupTest {

    private TranspositionTailMoveGroup transpositionTailMoveGroup;

    @Mock
    private TTable tTable;

    @Mock
    private MoveToHashMap moveToZobrist;

    @Mock
    private Visitor visitor;

    @Mock
    private Move move1;


    @BeforeEach
    public void setUp() {
        transpositionTailMoveGroup = new TranspositionTailMoveGroup();
        transpositionTailMoveGroup.setTTable(tTable);
        transpositionTailMoveGroup.setMoveToZobrist(moveToZobrist);
    }

    @Test
    public void testAccept() {
        transpositionTailMoveGroup.accept(visitor);

        verify(visitor).visit(transpositionTailMoveGroup);
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(tTable, transpositionTailMoveGroup.getTTable());

        TTable newTTable = mock(TTable.class);
        transpositionTailMoveGroup.setTTable(newTTable);
        assertEquals(newTTable, transpositionTailMoveGroup.getTTable());
    }

    @Test
    public void testGetZobristHashMoveFromCache() {
        when(moveToZobrist.read(move1)).thenReturn(12345L);

        long hash = transpositionTailMoveGroup.getZobristHashMove(move1);

        assertEquals(12345L, hash);
        verify(moveToZobrist).read(move1);
        verify(move1, never()).getZobristHash();
        verify(moveToZobrist, never()).write(any(), anyLong());
    }

    @Test
    public void testGetZobristHashMoveComputesAndCaches() {
        when(moveToZobrist.read(move1)).thenReturn(0L);
        when(move1.getZobristHash()).thenReturn(67890L);

        long hash = transpositionTailMoveGroup.getZobristHashMove(move1);

        assertEquals(67890L, hash);
        verify(moveToZobrist).read(move1);
        verify(move1).getZobristHash();
        verify(moveToZobrist).write(move1, 67890L);
    }
}
