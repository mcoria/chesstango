package chess.ai.imp.smart;

import chess.board.Color;
import chess.board.Game;
import chess.board.moves.Move;
import chess.board.moves.containers.MoveContainerReader;
import chess.board.position.ChessPositionReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MinMaxPrunningTest {

    @Mock
    private GameEvaluator evaluator;

    @Before
    public void setUp() {

    }

    @Test
    public void test_findBestMove_WhitePlays_SingleMove(){
        MinMaxPrunning minMax = new MinMaxPrunning(1, evaluator);

        Game rootGame = setupGame(Color.WHITE);

        Game childGame = setupGame(Color.BLACK);
        when(evaluator.evaluate(childGame)).thenReturn(1);

        Move move = mock(Move.class);

        linkMovesToGames(rootGame, new Move[]{move}, new Game[]{childGame});

        Move bestMove = minMax.findBestMove(rootGame);

        Assert.assertEquals(move, bestMove);
        Assert.assertEquals(1, minMax.getEvaluation());
        verify(evaluator, times(1)).evaluate(childGame);
    }

    @Test
    public void test_findBestMove_BlackPlays_SingleMove(){
        MinMaxPrunning minMax = new MinMaxPrunning(1, evaluator);

        Game rootGame = setupGame(Color.BLACK);

        Game childGame = setupGame(Color.WHITE);
        when(evaluator.evaluate(childGame)).thenReturn(1);

        Move move = mock(Move.class);

        linkMovesToGames(rootGame, new Move[]{move}, new Game[]{childGame});

        Move bestMove = minMax.findBestMove(rootGame);

        Assert.assertEquals(move, bestMove);
        Assert.assertEquals(1, minMax.getEvaluation());
        verify(evaluator, times(1)).evaluate(childGame);
    }

    @Test
    public void test_findBestMove_WhitePlays_TwoMoves(){
        MinMaxPrunning minMax = spy(new MinMaxPrunning(1, evaluator));

        Game rootGame = setupGame(Color.WHITE);

        Game childGame1 = setupGame(Color.BLACK);
        when(evaluator.evaluate(childGame1)).thenReturn(1);

        Game childGame2 = setupGame(Color.BLACK);
        when(evaluator.evaluate(childGame2)).thenReturn(2);

        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        linkMovesToGames(rootGame, new Move[]{move1, move2}, new Game[]{childGame1, childGame2});

        Move bestMove = minMax.findBestMove(rootGame);

        Assert.assertEquals(move2, bestMove);
        Assert.assertEquals(2, minMax.getEvaluation());

        verify(minMax).minimize(childGame1,0, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE);
        verify(minMax).minimize(childGame2,0, 1, GameEvaluator.INFINITE_POSITIVE);

        verify(evaluator, times(1)).evaluate(childGame1);
        verify(evaluator, times(1)).evaluate(childGame2);
    }

    @Test
    public void test_findBestMove_WhitePlays_MateCutOff(){
        MinMaxPrunning minMax = spy(new MinMaxPrunning(1, evaluator));

        Game rootGame = setupGame(Color.WHITE);

        Game childGame1 = setupGame(Color.BLACK);
        when(evaluator.evaluate(childGame1)).thenReturn(1);

        Game childGame2 = setupGame(Color.BLACK);
        when(evaluator.evaluate(childGame2)).thenReturn(GameEvaluator.WHITE_WON);

        Game childGame3 = setupGame(Color.BLACK);
        //when(evaluator.evaluate(childGame3)).thenReturn(3);

        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        Move move3 = mock(Move.class);
        linkMovesToGames(rootGame, new Move[]{move1, move2, move3}, new Game[]{childGame1, childGame2, childGame3});

        Move bestMove = minMax.findBestMove(rootGame);

        Assert.assertEquals(move2, bestMove);
        Assert.assertEquals(GameEvaluator.WHITE_WON, minMax.getEvaluation());

        verify(rootGame, times(1)).executeMove(move1);
        verify(rootGame, times(1)).executeMove(move2);
        verify(rootGame, never()).executeMove(move3);

        verify(minMax).minimize(childGame1,0, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE);
        verify(minMax).minimize(childGame2,0, 1, GameEvaluator.INFINITE_POSITIVE);

        verify(evaluator, times(1)).evaluate(childGame1);
        verify(evaluator, times(1)).evaluate(childGame2);
        verify(evaluator, never()).evaluate(childGame3);
    }

    @Test
    public void test_maximize_WhitePlays_MateCutOff(){
        MinMaxPrunning minMax = spy(new MinMaxPrunning(1, evaluator));
        //MinMaxPrunning minMax = new MinMaxPrunning(1, evaluator);

        Game rootGame = setupGame(Color.WHITE);

        Game childGame1 = setupGame(Color.BLACK);
        when(evaluator.evaluate(childGame1)).thenReturn(1);

        Game childGame2 = setupGame(Color.BLACK);
        when(evaluator.evaluate(childGame2)).thenReturn(GameEvaluator.WHITE_WON);

        Game childGame3 = setupGame(Color.BLACK);
        //when(evaluator.evaluate(childGame3)).thenReturn(3);

        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        Move move3 = mock(Move.class);
        linkMovesToGames(rootGame, new Move[]{move1, move2, move3}, new Game[]{childGame1, childGame2, childGame3});

        int maxValue = minMax.maximize(rootGame, 1, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE);

        Assert.assertEquals(GameEvaluator.WHITE_WON, maxValue);

        verify(rootGame, times(1)).executeMove(move1);
        verify(rootGame, times(1)).executeMove(move2);
        verify(rootGame, never()).executeMove(move3);

        verify(minMax).minimize(childGame1,0, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE);
        verify(minMax).minimize(childGame2,0, 1, GameEvaluator.INFINITE_POSITIVE);

        verify(evaluator, times(1)).evaluate(childGame1);
        verify(evaluator, times(1)).evaluate(childGame2);
        verify(evaluator, never()).evaluate(childGame3);

        verifyNoInteractions(move3);
        verifyNoInteractions(childGame3);
    }

    @Test
    public void test_findBestMove_BlackPlays_TwoMoves(){
        MinMaxPrunning minMax = spy(new MinMaxPrunning(1, evaluator));

        Game rootGame = setupGame(Color.BLACK);

        Game childGame1 = setupGame(Color.WHITE);
        when(evaluator.evaluate(childGame1)).thenReturn(1);

        Game childGame2 = setupGame(Color.WHITE);
        when(evaluator.evaluate(childGame2)).thenReturn(2);

        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        linkMovesToGames(rootGame, new Move[]{move1, move2}, new Game[]{childGame1, childGame2});

        Move bestMove = minMax.findBestMove(rootGame);

        Assert.assertEquals(move1, bestMove);
        Assert.assertEquals(1, minMax.getEvaluation());

        verify(minMax).maximize(childGame1,0, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE);
        verify(minMax).maximize(childGame2,0, GameEvaluator.INFINITE_NEGATIVE, 1);

        verify(evaluator, times(1)).evaluate(childGame1);
        verify(evaluator, times(1)).evaluate(childGame2);
    }

    @Test
    public void test_findBestMove_BlackPlays_MateCutOff(){
        MinMaxPrunning minMax = spy(new MinMaxPrunning(1, evaluator));

        Game rootGame = setupGame(Color.BLACK);

        Game childGame1 = setupGame(Color.WHITE);
        when(evaluator.evaluate(childGame1)).thenReturn(1);

        Game childGame2 = setupGame(Color.WHITE);
        when(evaluator.evaluate(childGame2)).thenReturn(GameEvaluator.BLACK_WON);

        Game childGame3 = setupGame(Color.WHITE);
        //when(evaluator.evaluate(childGame3)).thenReturn(3);

        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        Move move3 = mock(Move.class);
        linkMovesToGames(rootGame, new Move[]{move1, move2, move3}, new Game[]{childGame1, childGame2, childGame3});

        Move bestMove = minMax.findBestMove(rootGame);

        Assert.assertEquals(move2, bestMove);
        Assert.assertEquals(GameEvaluator.BLACK_WON, minMax.getEvaluation());

        verify(rootGame, times(1)).executeMove(move1);
        verify(rootGame, times(1)).executeMove(move2);
        verify(rootGame, never()).executeMove(move3);

        verify(minMax).maximize(childGame1,0, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE);
        verify(minMax).maximize(childGame2,0, GameEvaluator.INFINITE_NEGATIVE, 1);

        verify(rootGame, times(1)).executeMove(move1);
        verify(rootGame, times(1)).executeMove(move2);
        verify(rootGame, never()).executeMove(move3);

        verify(evaluator, times(1)).evaluate(childGame1);
        verify(evaluator, times(1)).evaluate(childGame2);
        verify(evaluator, never()).evaluate(childGame3);
    }

    @Test
    public void test_minimize_BlackPlays_MateCutOff(){
        MinMaxPrunning minMax = spy(new MinMaxPrunning(1, evaluator));

        Game rootGame = setupGame(Color.BLACK);

        Game childGame1 = setupGame(Color.WHITE);
        when(evaluator.evaluate(childGame1)).thenReturn(1);

        Game childGame2 = setupGame(Color.WHITE);
        when(evaluator.evaluate(childGame2)).thenReturn(GameEvaluator.BLACK_WON);

        Game childGame3 = setupGame(Color.WHITE);
        //when(evaluator.evaluate(childGame3)).thenReturn(3);

        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        Move move3 = mock(Move.class);
        linkMovesToGames(rootGame, new Move[]{move1, move2, move3}, new Game[]{childGame1, childGame2, childGame3});

        int minValue = minMax.minimize(rootGame, 1, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE);

        Assert.assertEquals(GameEvaluator.BLACK_WON, minValue);

        verify(rootGame, times(1)).executeMove(move1);
        verify(rootGame, times(1)).executeMove(move2);
        verify(rootGame, never()).executeMove(move3);

        verify(minMax).maximize(childGame1,0, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE);
        verify(minMax).maximize(childGame2,0, GameEvaluator.INFINITE_NEGATIVE, 1);

        verify(rootGame, times(1)).executeMove(move1);
        verify(rootGame, times(1)).executeMove(move2);
        verify(rootGame, never()).executeMove(move3);

        verify(evaluator, times(1)).evaluate(childGame1);
        verify(evaluator, times(1)).evaluate(childGame2);
        verify(evaluator, never()).evaluate(childGame3);

        verifyNoInteractions(move3);
        verifyNoInteractions(childGame3);
    }

    private Game setupGame(Color turn) {
        Game game = mock(Game.class);

        ChessPositionReader mockPositionReader = mock(ChessPositionReader.class);
        when(game.getChessPositionReader()).thenReturn(mockPositionReader);
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
        when(mockMoveCollection.size()).thenReturn(moveList.size());
    }
}