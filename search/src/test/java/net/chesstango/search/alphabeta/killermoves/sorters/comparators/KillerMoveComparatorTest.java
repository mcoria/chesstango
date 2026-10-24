package net.chesstango.search.alphabeta.killermoves.sorters.comparators;

import net.chesstango.board.moves.Move;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.killermoves.KillerMoves;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class KillerMoveComparatorTest {

    private KillerMoveComparator killerMoveComparator;

    @Mock
    private KillerMoves killerMoves;

    @Mock
    private Visitor visitor;

    @Mock
    private Move move1;

    @Mock
    private Move move2;

    @BeforeEach
    public void setUp() {
        killerMoveComparator = new KillerMoveComparator();
        killerMoveComparator.setKillerMoves(killerMoves);
    }

    @Test
    public void testAccept() {
        killerMoveComparator.accept(visitor);

        verify(visitor).visit(killerMoveComparator);
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void testCompareO1KillerO2NotKiller() {
        killerMoveComparator.beforeSort(1);

        when(killerMoves.isKiller(move1, 1)).thenReturn(true);
        when(killerMoves.isKiller(move2, 1)).thenReturn(false);

        int result = killerMoveComparator.compare(move1, move2);

        assertEquals(1, result);
        verify(killerMoves).isKiller(move1, 1);
        verify(killerMoves).isKiller(move2, 1);
    }

    @Test
    public void testCompareO1NotKillerO2Killer() {
        killerMoveComparator.beforeSort(1);

        when(killerMoves.isKiller(move1, 1)).thenReturn(false);
        when(killerMoves.isKiller(move2, 1)).thenReturn(true);

        int result = killerMoveComparator.compare(move1, move2);

        assertEquals(-1, result);
        verify(killerMoves).isKiller(move1, 1);
        verify(killerMoves).isKiller(move2, 1);
    }

    @Test
    public void testCompareBothKillers() {
        killerMoveComparator.beforeSort(1);

        when(killerMoves.isKiller(move1, 1)).thenReturn(true);
        when(killerMoves.isKiller(move2, 1)).thenReturn(true);

        int result = killerMoveComparator.compare(move1, move2);

        assertEquals(0, result);
        verify(killerMoves).isKiller(move1, 1);
        verify(killerMoves).isKiller(move2, 1);
    }

    @Test
    public void testCompareNeitherKiller() {
        killerMoveComparator.beforeSort(1);

        when(killerMoves.isKiller(move1, 1)).thenReturn(false);
        when(killerMoves.isKiller(move2, 1)).thenReturn(false);

        int result = killerMoveComparator.compare(move1, move2);

        assertEquals(0, result);
        verify(killerMoves).isKiller(move1, 1);
        verify(killerMoves).isKiller(move2, 1);
    }

    @Test
    public void testBeforeSortUpdatesPly() {
        killerMoveComparator.beforeSort(3);

        when(killerMoves.isKiller(move1, 3)).thenReturn(true);
        when(killerMoves.isKiller(move2, 3)).thenReturn(false);

        int result = killerMoveComparator.compare(move1, move2);

        assertEquals(1, result);
        verify(killerMoves).isKiller(move1, 3);
        verify(killerMoves).isKiller(move2, 3);
    }
}
