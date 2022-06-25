package chess.ai.imp.smart;

import chess.board.Game;
import chess.board.moves.Move;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

/**
 * @author Mauricio Coria
 *
 */
public class IterativeDeepingTest {

    @Test
    public void testFindBestMove(){
        //IterativeDeeping loop = spy(new IterativeDeeping());
        AbstractSmart smart = mock(AbstractSmart.class);

        IterativeDeeping loop = new IterativeDeeping(smart);

        Game game = mock (Game.class);

        Move move1 = mock(Move.class);
        when(smart.searchBestMove(game, 2)).thenReturn(move1);
        when(smart.getEvaluation()).thenReturn(1);

        Move move2 = mock(Move.class);
        when(smart.searchBestMove(game, 4)).thenReturn(move2);
        when(smart.getEvaluation()).thenReturn(4);

        Move bestMove = loop.searchBestMove(game, 2);
        int evaluation = loop.getEvaluation();

        Assert.assertEquals(move2, bestMove);
        Assert.assertEquals(4, evaluation);
    }
}
