package net.chesstango.search.alphabeta.transposition.sorters;

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
public class TranspositionTailPredicateTest {

    private TranspositionTailPredicate transpositionTailPredicate;

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
        transpositionTailPredicate = new TranspositionTailPredicate();
        transpositionTailPredicate.setTTable(tTable);
        transpositionTailPredicate.setMoveToZobrist(moveToZobrist);
    }

    @Test
    public void testAccept() {
        transpositionTailPredicate.accept(visitor);

        verify(visitor).visit(transpositionTailPredicate);
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(tTable, transpositionTailPredicate.getTTable());

        TTable newTTable = mock(TTable.class);
        transpositionTailPredicate.setTTable(newTTable);
        assertEquals(newTTable, transpositionTailPredicate.getTTable());
    }

    @Test
    public void testGetZobristHashMoveFromCache() {
        when(moveToZobrist.read(move1)).thenReturn(12345L);

        long hash = transpositionTailPredicate.getZobristHashMove(move1);

        assertEquals(12345L, hash);
        verify(moveToZobrist).read(move1);
        verify(move1, never()).getZobristHash();
        verify(moveToZobrist, never()).write(any(), anyLong());
    }

    @Test
    public void testGetZobristHashMoveComputesAndCaches() {
        when(moveToZobrist.read(move1)).thenReturn(0L);
        when(move1.getZobristHash()).thenReturn(67890L);

        long hash = transpositionTailPredicate.getZobristHashMove(move1);

        assertEquals(67890L, hash);
        verify(moveToZobrist).read(move1);
        verify(move1).getZobristHash();
        verify(moveToZobrist).write(move1, 67890L);
    }
}
