package chess.ai.imp.smart;

import chess.ai.BestMoveFinder;
import chess.board.Game;
import chess.board.moves.Move;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class IterativeDeepingTest {

    @Test
    public void testFindBestMove(){
        IterativeDeeping loop = spy(new IterativeDeeping());

        Game game = mock (Game.class);

        AbstractSmart bmf1 = mock(AbstractSmart.class);
        AbstractSmart bmf2 = mock(AbstractSmart.class);

        doReturn(bmf1).when(loop).getBestMoveFinder(2);
        doReturn(bmf2).when(loop).getBestMoveFinder(4);

        Move move1 = mock(Move.class);
        when(bmf1.searchBestMove(game)).thenReturn(move1);
        when(bmf1.getEvaluation()).thenReturn(1);

        Move move2 = mock(Move.class);
        when(bmf2.searchBestMove(game)).thenReturn(move2);
        when(bmf2.getEvaluation()).thenReturn(2);

        Move bestMove = loop.searchBestMove(game);
        int evaluation = loop.getEvaluation();

        Assert.assertEquals(move2, bestMove);
        Assert.assertEquals(2, evaluation);
    }
}
