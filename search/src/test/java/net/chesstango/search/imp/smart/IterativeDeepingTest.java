package net.chesstango.search.imp.smart;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;


/**
 * @author Mauricio Coria
 */
public class IterativeDeepingTest {

    @Test
    public void testFindBestMove() {
        //IterativeDeeping loop = spy(new IterativeDeeping());
        AbstractSmart smart = mock(AbstractSmart.class);

        IterativeDeeping loop = Mockito.spy(new IterativeDeeping(smart));

        Game game = mock(Game.class);

        Move move1 = mock(Move.class);
        when(smart.searchBestMove(game, 1)).thenReturn(move1);
        when(smart.getEvaluation()).thenReturn(30);

        Move move2 = mock(Move.class);
        when(smart.searchBestMove(game, 2)).thenReturn(move2);
        when(smart.getEvaluation()).thenReturn(20);

        Move move3 = mock(Move.class);
        when(smart.searchBestMove(game, 3)).thenReturn(move3);
        when(smart.getEvaluation()).thenReturn(10);

        Move bestMove = loop.searchBestMove(game, 3);
        int evaluation = loop.getEvaluation();

        Assert.assertEquals(move3, bestMove);
        Assert.assertEquals(10, evaluation); // Notar que a medida que profundiza empeora el score
    }
}
