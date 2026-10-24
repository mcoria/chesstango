package net.chesstango.search.alphabeta.killermoves.sorters;

import net.chesstango.board.moves.Move;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.killermoves.KillerMoves;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class KillerMovePredicateTest {

    private KillerMovePredicate killerMovePredicate;

    @Mock
    private KillerMoves killerMoves;

    @Mock
    private Visitor visitor;

    @Mock
    private Move move;

    @BeforeEach
    public void setUp() {
        killerMovePredicate = new KillerMovePredicate();
        killerMovePredicate.setKillerMoves(killerMoves);
    }

    @Test
    public void testAccept() {
        killerMovePredicate.accept(visitor);

        verify(visitor).visit(killerMovePredicate);
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void testTestWhenKiller() {
        killerMovePredicate.beforeSort(2);

        when(killerMoves.isKiller(move, 2)).thenReturn(true);

        assertTrue(killerMovePredicate.test(move));
        verify(killerMoves).isKiller(move, 2);
    }

    @Test
    public void testTestWhenNotKiller() {
        killerMovePredicate.beforeSort(2);

        when(killerMoves.isKiller(move, 2)).thenReturn(false);

        assertFalse(killerMovePredicate.test(move));
        verify(killerMoves).isKiller(move, 2);
    }

    @Test
    public void testBeforeSortUpdatesPly() {
        killerMovePredicate.beforeSort(1);
        when(killerMoves.isKiller(move, 1)).thenReturn(false);
        assertFalse(killerMovePredicate.test(move));

        killerMovePredicate.beforeSort(3);
        when(killerMoves.isKiller(move, 3)).thenReturn(true);
        assertTrue(killerMovePredicate.test(move));

        verify(killerMoves).isKiller(move, 1);
        verify(killerMoves).isKiller(move, 3);
    }
}
