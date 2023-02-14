package net.chesstango.search.smart;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.board.position.ChessPositionReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MinMaxTest {

    @Mock
    private GameEvaluator evaluator;

    @Before
    public void setUp() {
    }

    @Test
    public void testSingleMoveWhitePlays(){
        MinMax minMax = new MinMax(evaluator);

        Game rootGame = setupGame(Color.WHITE);

        Game childGame = setupGame(Color.BLACK);
        when(evaluator.evaluate(childGame)).thenReturn(1);

        Move move = mock(Move.class);

        linkMovesToGames(rootGame, new Move[]{move}, new Game[]{childGame});

        Move bestMove = minMax.searchBestMove(rootGame, 1);

        Assert.assertEquals(move, bestMove);
        Assert.assertEquals(1, minMax.getEvaluation());
        verify(evaluator, times(1)).evaluate(childGame);
    }

    @Test
    public void testSingleMoveBlackPlays(){
        MinMax minMax = new MinMax(evaluator);

        Game rootGame = setupGame(Color.BLACK);

        Game childGame = setupGame(Color.WHITE);
        when(evaluator.evaluate(childGame)).thenReturn(1);

        Move move = mock(Move.class);

        linkMovesToGames(rootGame, new Move[]{move}, new Game[]{childGame});

        Move bestMove = minMax.searchBestMove(rootGame, 1);

        Assert.assertEquals(move, bestMove);
        Assert.assertEquals(1, minMax.getEvaluation());
        verify(evaluator, times(1)).evaluate(childGame);
    }

    @Test
    public void testTwoMovesWhitePlays(){
        MinMax minMax = Mockito.spy(new MinMax(evaluator));

        Game rootGame = setupGame(Color.WHITE);

        Game childGame1 = setupGame(Color.BLACK);
        when(evaluator.evaluate(childGame1)).thenReturn(1);

        Game childGame2 = setupGame(Color.BLACK);
        when(evaluator.evaluate(childGame2)).thenReturn(2);

        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        linkMovesToGames(rootGame, new Move[]{move1, move2}, new Game[]{childGame1, childGame2});

        Move bestMove = minMax.searchBestMove(rootGame, 1);

        Assert.assertEquals(move2, bestMove);
        Assert.assertEquals(2, minMax.getEvaluation());

        verify(minMax).minMax(childGame1, true, 0);
        verify(minMax).minMax(childGame2, true, 0);

        verify(evaluator, times(1)).evaluate(childGame1);
        verify(evaluator, times(1)).evaluate(childGame2);
    }

    @Test
    public void testTwoMovesBlackPlays(){
        MinMax minMax = Mockito.spy(new MinMax(evaluator));

        Game rootGame = setupGame(Color.BLACK);

        Game childGame1 = setupGame(Color.WHITE);
        when(evaluator.evaluate(childGame1)).thenReturn(1);

        Game childGame2 = setupGame(Color.WHITE);
        when(evaluator.evaluate(childGame2)).thenReturn(2);

        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        linkMovesToGames(rootGame, new Move[]{move1, move2}, new Game[]{childGame1, childGame2});

        Move bestMove = minMax.searchBestMove(rootGame, 1);

        Assert.assertEquals(move1, bestMove);
        Assert.assertEquals(1, minMax.getEvaluation());

        verify(minMax).minMax(childGame1, false, 0);
        verify(minMax).minMax(childGame2, false, 0);

        verify(evaluator, times(1)).evaluate(childGame1);
        verify(evaluator, times(1)).evaluate(childGame2);
    }


    private Game setupGame(Color turn) {
        Game game = mock(Game.class);

        ChessPositionReader mockPositionReader = mock(ChessPositionReader.class);
        when(game.getChessPosition()).thenReturn(mockPositionReader);
        when(mockPositionReader.getCurrentTurn()).thenReturn(turn);

        return game;
    }

    private void linkMovesToGames(Game parentGame, Move moves[], Game childGames[]){
        List<Move> moveList = new ArrayList<Move>();
        if(moves !=null) {
            for (int i = 0; i < moves.length; i++) {
                Move move = moves[i];
                when(parentGame.executeMove(move)).thenReturn(childGames[i]);
                when(childGames[i].undoMove()).thenReturn(parentGame);
                moveList.add(move);
            }
        }

        MoveContainerReader mockMoveCollection = mock(MoveContainerReader.class);
        when(parentGame.getPossibleMoves()).thenReturn(mockMoveCollection);
        when(mockMoveCollection.iterator()).thenReturn(moveList.iterator());
    }
}
