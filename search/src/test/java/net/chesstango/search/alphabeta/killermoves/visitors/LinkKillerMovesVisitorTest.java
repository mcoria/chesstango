package net.chesstango.search.alphabeta.killermoves.visitors;

import net.chesstango.search.alphabeta.killermoves.KillerMoves;
import net.chesstango.search.alphabeta.killermoves.filters.KillerMoveTracker;
import net.chesstango.search.alphabeta.killermoves.sorters.KillerMovePredicate;
import net.chesstango.search.alphabeta.killermoves.sorters.comparators.KillerMoveComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class LinkKillerMovesVisitorTest {

    private LinkKillerMovesVisitor linkKillerMovesVisitor;

    @Mock
    private KillerMoves killerMoves;

    @Mock
    private KillerMoveTracker killerMoveTracker;

    @Mock
    private KillerMoveComparator killerMoveComparator;

    @Mock
    private KillerMovePredicate killerMovePredicate;

    @BeforeEach
    public void setUp() {
        linkKillerMovesVisitor = new LinkKillerMovesVisitor(killerMoves);
    }

    @Test
    public void testVisitKillerMoveTracker() {
        linkKillerMovesVisitor.visit(killerMoveTracker);

        verify(killerMoveTracker).setKillerMoves(killerMoves);
        verifyNoMoreInteractions(killerMoveTracker);
    }

    @Test
    public void testVisitKillerMoveComparator() {
        linkKillerMovesVisitor.visit(killerMoveComparator);

        verify(killerMoveComparator).setKillerMoves(killerMoves);
        verifyNoMoreInteractions(killerMoveComparator);
    }

    @Test
    public void testVisitKillerMovePredicate() {
        linkKillerMovesVisitor.visit(killerMovePredicate);

        verify(killerMovePredicate).setKillerMoves(killerMoves);
        verifyNoMoreInteractions(killerMovePredicate);
    }
}
