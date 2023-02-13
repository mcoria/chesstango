package net.chesstango.search.smart;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.GameState;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.position.ChessPositionReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@RunWith(MockitoJUnitRunner.class)
public class MinMaxPruningBlackTest {

    @Mock
    private MoveSorter moveSorter;

    @Mock
    private Quiescence quiescence;

    @Before
    public void setUp() {
    }


    @Test
    public void test_findBestMove_BlackPlays_SingleMove(){
        MinMaxPruning minMax = new MinMaxPruning(quiescence, moveSorter);

        Game rootGame = setupGame(Color.BLACK, GameState.Status.NO_CHECK);

        Game childGame = setupGame(Color.WHITE, GameState.Status.NO_CHECK);
        when(quiescence.quiescenceMax(childGame, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE)).thenReturn(1);

        Move move = mock(Move.class);

        linkMovesToGames(rootGame, new Move[]{move}, new Game[]{childGame});

        Move bestMove = minMax.searchBestMove(rootGame, 1);

        Assert.assertEquals(move, bestMove);
        Assert.assertEquals(1, minMax.getEvaluation());
        verify(quiescence, times(1)).quiescenceMax(childGame, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE);
    }

    @Test
    public void test_findBestMove_BlackPlays_TwoMoves(){
        MinMaxPruning minMax = Mockito.spy(new MinMaxPruning(quiescence, moveSorter));
        //MinMaxPruning minMax = Mockito.spy(new MinMaxPruning(quiescence, moveSorter));

        Game rootGame = setupGame(Color.BLACK, GameState.Status.NO_CHECK);

        Game childGame1 = setupGame(Color.WHITE, GameState.Status.NO_CHECK);
        when(quiescence.quiescenceMax(childGame1, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE)).thenReturn(1);

        Game childGame2 = setupGame(Color.WHITE, GameState.Status.NO_CHECK);
        when(quiescence.quiescenceMax(childGame2, GameEvaluator.INFINITE_NEGATIVE, 1)).thenReturn(2);

        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        linkMovesToGames(rootGame, new Move[]{move1, move2}, new Game[]{childGame1, childGame2});

        Move bestMove = minMax.searchBestMove(rootGame, 1);

        Assert.assertEquals(move1, bestMove);
        Assert.assertEquals(1, minMax.getEvaluation());

        verify(quiescence, times(1)).quiescenceMax(childGame1, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE);
        verify(quiescence, times(1)).quiescenceMax(childGame2,GameEvaluator.INFINITE_NEGATIVE, 1);

        verify(minMax).maximize(childGame1,0, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE);
        verify(minMax).maximize(childGame2,0, GameEvaluator.INFINITE_NEGATIVE, 1);
    }


    @Test
    public void test_findBestMove_BlackPlays_MateCutOff(){
        MinMaxPruning minMax = Mockito.spy(new MinMaxPruning(quiescence, moveSorter));

        Game rootGame = setupGame(Color.BLACK, GameState.Status.NO_CHECK);

        Game childGame1 = setupGame(Color.WHITE, GameState.Status.NO_CHECK);
        when(quiescence.quiescenceMax(childGame1, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE)).thenReturn(1);

        Game childGame2 = setupGame(Color.WHITE, GameState.Status.NO_CHECK);
        when(quiescence.quiescenceMax(childGame2, GameEvaluator.INFINITE_NEGATIVE, 1)).thenReturn(GameEvaluator.BLACK_WON);

        // childGame3 no llega a evaluarse, dado que existe CuteOff por el mate que se encuentra en childGame2
        Game childGame3 = setupGame(Color.WHITE, GameState.Status.NO_CHECK);

        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        Move move3 = mock(Move.class);
        linkMovesToGames(rootGame, new Move[]{move1, move2, move3}, new Game[]{childGame1, childGame2, childGame3});

        Move bestMove = minMax.searchBestMove(rootGame, 1);

        Assert.assertEquals(move2, bestMove);
        Assert.assertEquals(GameEvaluator.BLACK_WON, minMax.getEvaluation());

        verify(rootGame, times(1)).executeMove(move1);
        verify(rootGame, times(1)).executeMove(move2);
        verify(rootGame, never()).executeMove(move3);

        verify(quiescence, times(1)).quiescenceMax(childGame1, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE);
        verify(quiescence, times(1)).quiescenceMax(childGame2,GameEvaluator.INFINITE_NEGATIVE, 1);

        verify(minMax).maximize(childGame1,0, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE);
        verify(minMax).maximize(childGame2,0, GameEvaluator.INFINITE_NEGATIVE, 1);

        verifyNoInteractions(move3);
        verifyNoInteractions(childGame3);
    }

    @Test
    public void test_findBestMove_BlackPlays_ImminentMate(){
        MinMaxPruning minMax = Mockito.spy(new MinMaxPruning(quiescence, moveSorter));
        //MinMaxPruning minMax = new MinMaxPruning(quiescence, moveSorter);

        Game rootGame = setupGame(Color.BLACK, GameState.Status.NO_CHECK);

        Game childGame1 = setupGame(Color.WHITE, GameState.Status.NO_CHECK);
        when(quiescence.quiescenceMax(childGame1, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE)).thenReturn(GameEvaluator.BLACK_LOST);

        Game childGame2 = setupGame(Color.WHITE, GameState.Status.NO_CHECK);
        when(quiescence.quiescenceMax(childGame2, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE)).thenReturn(GameEvaluator.BLACK_LOST);

        Game childGame3 = setupGame(Color.WHITE, GameState.Status.NO_CHECK);
        when(quiescence.quiescenceMax(childGame3, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE)).thenReturn(GameEvaluator.BLACK_LOST);

        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        Move move3 = mock(Move.class);
        linkMovesToGames(rootGame, new Move[]{move1, move2, move3}, new Game[]{childGame1, childGame2, childGame3});

        Move bestMove = minMax.searchBestMove(rootGame, 1);

        Assert.assertNotNull(bestMove);
        Assert.assertEquals(GameEvaluator.BLACK_LOST, minMax.getEvaluation());

        verify(rootGame, times(1)).executeMove(move1);
        verify(rootGame, times(1)).executeMove(move2);
        verify(rootGame, times(1)).executeMove(move3);

        verify(quiescence, times(1)).quiescenceMax(childGame1, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE);
        verify(quiescence, times(1)).quiescenceMax(childGame2, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE);
        verify(quiescence, times(1)).quiescenceMax(childGame3, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE);

        verify(minMax).maximize(childGame1,0, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE);
        verify(minMax).maximize(childGame2,0, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE);
        verify(minMax).maximize(childGame3,0, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE);
    }

    @Test
    public void test_minimize_BlackPlays_MateCutOff(){
        MinMaxPruning minMax = Mockito.spy(new MinMaxPruning(quiescence, moveSorter));
        //MinMaxPruning minMax = new MinMaxPruning(quiescence, moveSorter);

        Game rootGame = setupGame(Color.BLACK, GameState.Status.NO_CHECK);

        Game childGame1 = setupGame(Color.WHITE, GameState.Status.NO_CHECK);
        when(quiescence.quiescenceMax(childGame1, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE)).thenReturn(1);

        Game childGame2 = setupGame(Color.WHITE, GameState.Status.NO_CHECK);
        when(quiescence.quiescenceMax(childGame2, GameEvaluator.INFINITE_NEGATIVE,1)).thenReturn(GameEvaluator.BLACK_WON);

        Game childGame3 = setupGame(Color.WHITE, GameState.Status.NO_CHECK);

        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        Move move3 = mock(Move.class);
        linkMovesToGames(rootGame, new Move[]{move1, move2, move3}, new Game[]{childGame1, childGame2, childGame3});

        int minValue = minMax.minimize(rootGame, 1, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE);

        Assert.assertEquals(GameEvaluator.BLACK_WON, minValue);

        verify(rootGame, times(1)).executeMove(move1);
        verify(rootGame, times(1)).executeMove(move2);
        verify(rootGame, never()).executeMove(move3);

        verify(quiescence, times(1)).quiescenceMax(childGame1, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE);
        verify(quiescence, times(1)).quiescenceMax(childGame2,GameEvaluator.INFINITE_NEGATIVE, 1);

        verify(minMax).maximize(childGame1,0, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE);
        verify(minMax).maximize(childGame2,0, GameEvaluator.INFINITE_NEGATIVE, 1);

        verifyNoInteractions(move3);
        verifyNoInteractions(childGame3);
    }

    private Game setupGame(Color turn, GameState.Status status) {
        Game game = mock(Game.class);

        ChessPositionReader mockPositionReader = mock(ChessPositionReader.class);
        when(game.getChessPosition()).thenReturn(mockPositionReader);
        when(game.getStatus()).thenReturn(status);
        when(mockPositionReader.getCurrentTurn()).thenReturn(turn);

        return game;
    }

    private void linkMovesToGames(Game parentGame, Move moves[], Game childGames[]){
        List<Move> moveList = new LinkedList<Move>();
        if(moves !=null) {
            for (int i = 0; i < moves.length; i++) {
                Move move = moves[i];
                when(parentGame.executeMove(move)).thenReturn(childGames[i]);
                when(childGames[i].undoMove()).thenReturn(parentGame);
                moveList.add(move);
            }
        }

        MoveContainerReader mockMoveCollection = mock(MoveContainerReader.class);
        when(mockMoveCollection.iterator()).thenAnswer( ans -> moveList.iterator());
        //when(mockMoveCollection.size()).thenReturn( moveList.size() );
        when(parentGame.getPossibleMoves()).thenReturn(mockMoveCollection);

        when(moveSorter.sortMoves(mockMoveCollection)).thenReturn( new ArrayDeque<>(moveList) );
    }
}
