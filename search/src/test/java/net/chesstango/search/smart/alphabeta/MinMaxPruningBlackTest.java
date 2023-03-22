package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.*;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveFactory;
import net.chesstango.board.moves.containers.MoveContainer;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.MoveSorter;
import net.chesstango.search.smart.SearchContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@RunWith(MockitoJUnitRunner.class)
public class MinMaxPruningBlackTest {

    private MoveFactory moveFactoryBlack = SingletonMoveFactories.getDefaultMoveFactoryBlack();

    @Mock
    private MoveSorter moveSorter;

    @Mock
    private Quiescence quiescence;

    @Before
    public void setUp() {
    }


    @Test
    public void test_findBestMove_BlackPlays_SingleMove() {
        AlphaBetaImp alphaBetaImp = new AlphaBetaImp();
        alphaBetaImp.setQuiescence(quiescence);
        alphaBetaImp.setMoveSorter(moveSorter);

        MinMaxPruning minMaxPruning = new MinMaxPruning();
        minMaxPruning.setAlphaBetaSearch(alphaBetaImp);
        minMaxPruning.setMoveSorter(moveSorter);
        minMaxPruning.setFilters(Arrays.asList(alphaBetaImp, quiescence));

        Game rootGame = setupGame(Color.BLACK, GameStatus.NO_CHECK);

        Game childGame = setupGame(Color.WHITE, GameStatus.NO_CHECK);
        when(quiescence.maximize(eq(childGame), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE))).thenReturn(1);

        Move move = mock(Move.class);

        linkMovesToGames(rootGame, new Move[]{move}, new Game[]{childGame});

        SearchMoveResult searchResult = minMaxPruning.searchBestMove(rootGame, new SearchContext(1));

        Move bestMove = searchResult.getBestMove();

        Assert.assertEquals(move, bestMove);
        Assert.assertEquals(1, searchResult.getEvaluation());
        verify(quiescence, times(1)).maximize(eq(childGame), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
    }

    @Test
    public void test_findBestMove_BlackPlays_TwoMoves() {
        AlphaBetaImp alphaBetaImp = Mockito.spy(new AlphaBetaImp());
        alphaBetaImp.setQuiescence(quiescence);
        alphaBetaImp.setMoveSorter(moveSorter);

        MinMaxPruning minMaxPruning = Mockito.spy(new MinMaxPruning());
        minMaxPruning.setAlphaBetaSearch(alphaBetaImp);
        minMaxPruning.setMoveSorter(moveSorter);
        minMaxPruning.setFilters(Arrays.asList(alphaBetaImp, quiescence));

        Game rootGame = setupGame(Color.BLACK, GameStatus.NO_CHECK);

        Game childGame1 = setupGame(Color.WHITE, GameStatus.NO_CHECK);
        when(quiescence.maximize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE))).thenReturn(1);

        Game childGame2 = setupGame(Color.WHITE, GameStatus.NO_CHECK);
        when(quiescence.maximize(eq(childGame2), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(1))).thenReturn(2);

        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        linkMovesToGames(rootGame, new Move[]{move1, move2}, new Game[]{childGame1, childGame2});

        SearchMoveResult searchResult = minMaxPruning.searchBestMove(rootGame, new SearchContext(1));

        Move bestMove = searchResult.getBestMove();

        Assert.assertEquals(move1, bestMove);
        Assert.assertEquals(1, searchResult.getEvaluation());

        verify(quiescence, times(1)).maximize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        verify(quiescence, times(1)).maximize(eq(childGame2), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(1));

        verify(alphaBetaImp).maximize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        verify(alphaBetaImp).maximize(eq(childGame2), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(1));
    }

    @Test
    public void test_findBestMove_BlackPlays_MateCutOff() {
        AlphaBetaImp alphaBetaImp = Mockito.spy(new AlphaBetaImp());
        alphaBetaImp.setQuiescence(quiescence);
        alphaBetaImp.setMoveSorter(moveSorter);

        MinMaxPruning minMaxPruning = Mockito.spy(new MinMaxPruning());
        minMaxPruning.setAlphaBetaSearch(alphaBetaImp);
        minMaxPruning.setMoveSorter(moveSorter);
        minMaxPruning.setFilters(Arrays.asList(alphaBetaImp, quiescence));

        Game rootGame = setupGame(Color.BLACK, GameStatus.NO_CHECK);

        Game childGame1 = setupGame(Color.WHITE, GameStatus.NO_CHECK);
        when(quiescence.maximize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE))).thenReturn(1);

        Game childGame2 = setupGame(Color.WHITE, GameStatus.NO_CHECK);
        when(quiescence.maximize(eq(childGame2), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(1))).thenReturn(GameEvaluator.BLACK_WON);

        // childGame3 no llega a evaluarse, dado que existe CuteOff por el mate que se encuentra en childGame2
        Game childGame3 = setupGame(Color.WHITE, GameStatus.NO_CHECK);

        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        Move move3 = mock(Move.class);
        linkMovesToGames(rootGame, new Move[]{move1, move2, move3}, new Game[]{childGame1, childGame2, childGame3});

        SearchMoveResult searchResult = minMaxPruning.searchBestMove(rootGame, new SearchContext(1));

        Move bestMove = searchResult.getBestMove();

        Assert.assertEquals(move2, bestMove);
        Assert.assertEquals(GameEvaluator.BLACK_WON, searchResult.getEvaluation());

        verify(rootGame, times(1)).executeMove(move1);
        verify(rootGame, times(1)).executeMove(move2);
        verify(rootGame, never()).executeMove(move3);

        verify(quiescence, times(1)).maximize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        verify(quiescence, times(1)).maximize(eq(childGame2), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(1));

        verify(alphaBetaImp).maximize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        verify(alphaBetaImp).maximize(eq(childGame2), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(1));

        verifyNoInteractions(move3);
        verifyNoInteractions(childGame3);
    }

    @Test
    public void test_findBestMove_BlackPlays_ImminentMate() {
        AlphaBetaImp alphaBetaImp = Mockito.spy(new AlphaBetaImp());
        alphaBetaImp.setQuiescence(quiescence);
        alphaBetaImp.setMoveSorter(moveSorter);

        MinMaxPruning minMaxPruning = Mockito.spy(new MinMaxPruning());
        minMaxPruning.setAlphaBetaSearch(alphaBetaImp);
        minMaxPruning.setMoveSorter(moveSorter);
        minMaxPruning.setFilters(Arrays.asList(alphaBetaImp, quiescence));

        Game rootGame = setupGame(Color.BLACK, GameStatus.NO_CHECK);

        Game childGame1 = setupGame(Color.WHITE, GameStatus.NO_CHECK);
        when(quiescence.maximize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE))).thenReturn(GameEvaluator.BLACK_LOST);

        Game childGame2 = setupGame(Color.WHITE, GameStatus.NO_CHECK);
        when(quiescence.maximize(eq(childGame2), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE))).thenReturn(GameEvaluator.BLACK_LOST);

        Game childGame3 = setupGame(Color.WHITE, GameStatus.NO_CHECK);
        when(quiescence.maximize(eq(childGame3), eq(1),eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE))).thenReturn(GameEvaluator.BLACK_LOST);

        Move move1 = moveFactoryBlack.createCaptureMove(PiecePositioned.getPiecePositioned(Square.c3, Piece.KNIGHT_BLACK), PiecePositioned.getPosition(Square.d4));
        Move move2 = moveFactoryBlack.createCaptureMove(PiecePositioned.getPiecePositioned(Square.c3, Piece.KNIGHT_BLACK), PiecePositioned.getPosition(Square.b4));
        Move move3 = moveFactoryBlack.createCaptureMove(PiecePositioned.getPiecePositioned(Square.c3, Piece.KNIGHT_BLACK), PiecePositioned.getPosition(Square.e5));
        linkMovesToGames(rootGame, new Move[]{move1, move2, move3}, new Game[]{childGame1, childGame2, childGame3});

        SearchMoveResult searchResult = minMaxPruning.searchBestMove(rootGame, new SearchContext(1));

        Move bestMove = searchResult.getBestMove();

        Assert.assertNotNull(bestMove);
        Assert.assertEquals(GameEvaluator.BLACK_LOST, searchResult.getEvaluation());

        verify(rootGame, times(1)).executeMove(move1);
        verify(rootGame, times(1)).executeMove(move2);
        verify(rootGame, times(1)).executeMove(move3);

        verify(quiescence, times(1)).maximize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        verify(quiescence, times(1)).maximize(eq(childGame2), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        verify(quiescence, times(1)).maximize(eq(childGame3), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));

        verify(alphaBetaImp).maximize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        verify(alphaBetaImp).maximize(eq(childGame2), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        verify(alphaBetaImp).maximize(eq(childGame3), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
    }

    @Test
    public void test_minimize_BlackPlays_MateCutOff() {
        AlphaBetaImp alphaBetaImp = Mockito.spy(new AlphaBetaImp());
        //AlphaBetaImp alphaBetaImp = new AlphaBetaImp();
        alphaBetaImp.setQuiescence(quiescence);
        alphaBetaImp.setMoveSorter(moveSorter);
        alphaBetaImp.setNext(alphaBetaImp);

        Game rootGame = setupGame(Color.BLACK, GameStatus.NO_CHECK);

        Game childGame1 = setupGame(Color.WHITE, GameStatus.NO_CHECK);
        when(quiescence.maximize(eq(childGame1), eq(2), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE))).thenReturn(1);

        Game childGame2 = setupGame(Color.WHITE, GameStatus.NO_CHECK);
        when(quiescence.maximize(eq(childGame2), eq(2), eq(GameEvaluator.INFINITE_NEGATIVE), eq(1))).thenReturn(GameEvaluator.BLACK_WON);

        Game childGame3 = setupGame(Color.WHITE, GameStatus.NO_CHECK);

        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        Move move3 = mock(Move.class);
        linkMovesToGames(rootGame, new Move[]{move1, move2, move3}, new Game[]{childGame1, childGame2, childGame3});

        alphaBetaImp.init(rootGame, new SearchContext(2));
        int minValue = alphaBetaImp.minimize(rootGame, 1, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE);

        Assert.assertEquals(GameEvaluator.BLACK_WON, minValue);

        verify(rootGame, times(1)).executeMove(move1);
        verify(rootGame, times(1)).executeMove(move2);
        verify(rootGame, never()).executeMove(move3);

        verify(quiescence, times(1)).maximize(eq(childGame1), eq(2), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        verify(quiescence, times(1)).maximize(eq(childGame2), eq(2), eq(GameEvaluator.INFINITE_NEGATIVE), eq(1));

        verify(alphaBetaImp).maximize(eq(childGame1), eq(2), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        verify(alphaBetaImp).maximize(eq(childGame2), eq(2), eq(GameEvaluator.INFINITE_NEGATIVE), eq(1));

        verifyNoInteractions(move3);
        verifyNoInteractions(childGame3);
    }

    private Game setupGame(Color turn, GameStatus gameStatus) {
        Game game = mock(Game.class);

        ChessPositionReader mockPositionReader = mock(ChessPositionReader.class);
        when(game.getChessPosition()).thenReturn(mockPositionReader);
        when(game.getStatus()).thenReturn(gameStatus);
        when(mockPositionReader.getCurrentTurn()).thenReturn(turn);

        return game;
    }

    private void linkMovesToGames(Game parentGame, Move moves[], Game childGames[]) {
        List<Move> moveList = new LinkedList<Move>();
        if (moves != null) {
            for (int i = 0; i < moves.length; i++) {
                Move move = moves[i];
                when(parentGame.executeMove(move)).thenReturn(childGames[i]);
                when(childGames[i].undoMove()).thenReturn(parentGame);
                moveList.add(move);
            }
        }

        MoveContainer moveContainer = new MoveContainer();
        moveList.forEach(moveContainer::add);
        when(parentGame.getPossibleMoves()).thenReturn(moveContainer);

        when(moveSorter.sortMoves(moveContainer)).thenReturn(new ArrayDeque<>(moveList));
    }

}
