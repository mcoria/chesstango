package net.chesstango.search.alphabeta.transposition.sorters;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.transposition.TTable;
import net.chesstango.search.alphabeta.transposition.TranspositionEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class TranspositionHeadPredicateTest {

    private TranspositionHeadPredicate transpositionHeadPredicate;

    @Mock
    private TTable tTable;

    @Mock
    private Visitor visitor;

    @Mock
    private Move move1;

    @Mock
    private Move move2;

    @Mock
    private Move move3;

    @Mock
    private Move move4;

    @Mock
    private Move move5;

    @BeforeEach
    public void setUp() {
        transpositionHeadPredicate = new TranspositionHeadPredicate();
        transpositionHeadPredicate.setTTable(tTable);
    }

    @Test
    public void testAccept() {
        transpositionHeadPredicate.accept(visitor);

        verify(visitor).visit(transpositionHeadPredicate);
        verifyNoMoreInteractions(visitor);
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(tTable, transpositionHeadPredicate.getTTable());

        transpositionHeadPredicate.setBestMoveEncoded((short) 1234);
        assertEquals((short) 1234, transpositionHeadPredicate.getBestMoveEncoded());
    }

    @Test
    public void testBeforeSortHitWithMatchingHash() {
        Game game = Game.from(FEN.START_POSITION);
        long hash = game.getPosition().getZobristHash();
        short encodedMove = (short) 0x1234;

        transpositionHeadPredicate.setGame(game);

        doAnswer(invocation -> {
            TranspositionEntry entry = invocation.getArgument(1);
            entry.setHash(hash);
            entry.setMove(encodedMove);
            return true;
        }).when(tTable).load(eq(hash), any(TranspositionEntry.class));

        transpositionHeadPredicate.beforeSort(0);

        assertEquals(encodedMove, transpositionHeadPredicate.getBestMoveEncoded());
    }

    @Test
    public void testBeforeSortMiss() {
        Game game = Game.from(FEN.START_POSITION);
        long hash = game.getPosition().getZobristHash();

        transpositionHeadPredicate.setGame(game);
        transpositionHeadPredicate.setBestMoveEncoded((short) 0x1234);

        when(tTable.load(eq(hash), any(TranspositionEntry.class))).thenReturn(false);

        transpositionHeadPredicate.beforeSort(0);

        assertEquals((short) 0, transpositionHeadPredicate.getBestMoveEncoded());
    }

    @Test
    public void testBeforeSortHitWithHashMismatch() {
        Game game = Game.from(FEN.START_POSITION);
        long hash = game.getPosition().getZobristHash();

        transpositionHeadPredicate.setGame(game);
        transpositionHeadPredicate.setBestMoveEncoded((short) 0x1234);

        doAnswer(invocation -> {
            TranspositionEntry entry = invocation.getArgument(1);
            entry.setHash(hash + 1L); // Hash mismatch (collision / different entry)
            entry.setMove((short) 0x5678);
            return true;
        }).when(tTable).load(eq(hash), any(TranspositionEntry.class));

        transpositionHeadPredicate.beforeSort(0);

        assertEquals((short) 0, transpositionHeadPredicate.getBestMoveEncoded());
    }
}
