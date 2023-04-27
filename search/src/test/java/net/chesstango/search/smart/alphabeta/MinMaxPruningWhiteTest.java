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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class MinMaxPruningWhiteTest {

    private MoveFactory moveFactoryWhite = SingletonMoveFactories.getDefaultMoveFactoryWhite();

    @Mock
    private MoveSorter moveSorter;

    @Mock
    private Quiescence quiescence;

    @BeforeEach
    public void setUp() {
    }


    @Test
    @Disabled
    public void test_findBestMove_WhitePlays_SingleMove() {
        AlphaBeta alphaBeta = new AlphaBeta();
        alphaBeta.setQuiescence(quiescence);
        alphaBeta.setMoveSorter(moveSorter);

        MinMaxPruning minMaxPruning = new MinMaxPruning();
        minMaxPruning.setAlphaBetaSearch(alphaBeta);
        minMaxPruning.setMoveSorter(moveSorter);
        minMaxPruning.setFilters(Arrays.asList(alphaBeta, quiescence));

        Game rootGame = setupGame(Color.WHITE, GameStatus.NO_CHECK);

        Game childGame = setupGame(Color.BLACK, GameStatus.NO_CHECK);
        when(quiescence.minimize(eq(childGame), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE))).thenReturn(1);

        Move move = mock(Move.class);

        linkMovesToGames(rootGame, new Move[]{move}, new Game[]{childGame});

        SearchMoveResult searchResult = minMaxPruning.searchBestMove(rootGame, new SearchContext(1));

        Move bestMove = searchResult.getBestMove();

        assertEquals(move, bestMove);
        assertEquals(1, searchResult.getEvaluation());
        verify(quiescence, times(1)).minimize(eq(childGame), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
    }

    @Test
    @Disabled
    public void test_findBestMove_WhitePlays_TwoMoves() {
        AlphaBeta alphaBeta = Mockito.spy(new AlphaBeta());
        alphaBeta.setQuiescence(quiescence);
        alphaBeta.setMoveSorter(moveSorter);

        MinMaxPruning minMaxPruning = Mockito.spy(new MinMaxPruning());
        minMaxPruning.setAlphaBetaSearch(alphaBeta);
        minMaxPruning.setMoveSorter(moveSorter);
        minMaxPruning.setFilters(Arrays.asList(alphaBeta, quiescence));

        Game rootGame = setupGame(Color.WHITE, GameStatus.NO_CHECK);

        Game childGame1 = setupGame(Color.BLACK, GameStatus.NO_CHECK);
        when(quiescence.minimize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE))).thenReturn(1);

        Game childGame2 = setupGame(Color.BLACK, GameStatus.NO_CHECK);
        when(quiescence.minimize(eq(childGame2), eq(1), eq(1), eq(GameEvaluator.INFINITE_POSITIVE))).thenReturn(2);

        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        linkMovesToGames(rootGame, new Move[]{move1, move2}, new Game[]{childGame1, childGame2});

        SearchMoveResult searchResult = minMaxPruning.searchBestMove(rootGame, new SearchContext(1));

        Move bestMove = searchResult.getBestMove();

        assertEquals(move2, bestMove);
        assertEquals(2, searchResult.getEvaluation());

        verify(quiescence, times(1)).minimize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        verify(quiescence, times(1)).minimize(eq(childGame2), eq(1), eq(1), eq(GameEvaluator.INFINITE_POSITIVE));

        verify(alphaBeta).minimize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        verify(alphaBeta).minimize(eq(childGame2), eq(1), eq(1), eq(GameEvaluator.INFINITE_POSITIVE));
    }

    @Test
    @Disabled
    public void test_findBestMove_WhitePlays_MateCutOff() {
        AlphaBeta alphaBeta = Mockito.spy(new AlphaBeta());
        alphaBeta.setQuiescence(quiescence);
        alphaBeta.setMoveSorter(moveSorter);

        MinMaxPruning minMaxPruning = Mockito.spy(new MinMaxPruning());
        minMaxPruning.setAlphaBetaSearch(alphaBeta);
        minMaxPruning.setMoveSorter(moveSorter);
        minMaxPruning.setFilters(Arrays.asList(alphaBeta, quiescence));

        Game rootGame = setupGame(Color.WHITE, GameStatus.NO_CHECK);

        Game childGame1 = setupGame(Color.BLACK, GameStatus.NO_CHECK);
        when(quiescence.minimize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE))).thenReturn(1);

        Game childGame2 = setupGame(Color.BLACK, GameStatus.NO_CHECK);
        when(quiescence.minimize(eq(childGame2), eq(1), eq(1), eq(GameEvaluator.INFINITE_POSITIVE))).thenReturn(GameEvaluator.WHITE_WON);

        // childGame3 no llega a evaluarse, dado que existe CuteOff por el mate que se encuentra en childGame2
        Game childGame3 = setupGame(Color.BLACK, GameStatus.NO_CHECK);

        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        Move move3 = mock(Move.class);
        linkMovesToGames(rootGame, new Move[]{move1, move2, move3}, new Game[]{childGame1, childGame2, childGame3});

        SearchMoveResult searchResult = minMaxPruning.searchBestMove(rootGame, new SearchContext(1));

        Move bestMove = searchResult.getBestMove();

        assertEquals(move2, bestMove);
        assertEquals(GameEvaluator.WHITE_WON, searchResult.getEvaluation());

        verify(rootGame, times(1)).executeMove(move1);
        verify(rootGame, times(1)).executeMove(move2);
        verify(rootGame, never()).executeMove(move3);

        verify(quiescence, times(1)).minimize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        verify(quiescence, times(1)).minimize(eq(childGame2), eq(1), eq(1), eq(GameEvaluator.INFINITE_POSITIVE));

        verify(alphaBeta).minimize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        verify(alphaBeta).minimize(eq(childGame2), eq(1), eq(1), eq(GameEvaluator.INFINITE_POSITIVE));

        verifyNoInteractions(move3);
        verifyNoInteractions(childGame3);
    }

    @Test
    @Disabled
    public void test_findBestMove_WhitePlays_ImminentMate() {
        AlphaBeta alphaBeta = Mockito.spy(new AlphaBeta());
        alphaBeta.setQuiescence(quiescence);
        alphaBeta.setMoveSorter(moveSorter);

        MinMaxPruning minMaxPruning = Mockito.spy(new MinMaxPruning());
        minMaxPruning.setAlphaBetaSearch(alphaBeta);
        minMaxPruning.setMoveSorter(moveSorter);
        minMaxPruning.setFilters(Arrays.asList(alphaBeta, quiescence));

        Game rootGame = setupGame(Color.WHITE, GameStatus.NO_CHECK);

        Game childGame1 = setupGame(Color.BLACK, GameStatus.NO_CHECK);
        when(quiescence.minimize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE))).thenReturn(GameEvaluator.WHITE_LOST);

        Game childGame2 = setupGame(Color.BLACK, GameStatus.NO_CHECK);
        when(quiescence.minimize(eq(childGame2), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE))).thenReturn(GameEvaluator.WHITE_LOST);

        Game childGame3 = setupGame(Color.BLACK, GameStatus.NO_CHECK);
        when(quiescence.minimize(eq(childGame3), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE))).thenReturn(GameEvaluator.WHITE_LOST);

        Move move1 = moveFactoryWhite.createCaptureMove(PiecePositioned.getPiecePositioned(Square.c3, Piece.KNIGHT_WHITE), PiecePositioned.getPosition(Square.d5));
        Move move2 = moveFactoryWhite.createCaptureMove(PiecePositioned.getPiecePositioned(Square.c3, Piece.KNIGHT_WHITE), PiecePositioned.getPosition(Square.b5));
        Move move3 = moveFactoryWhite.createCaptureMove(PiecePositioned.getPiecePositioned(Square.c3, Piece.KNIGHT_WHITE), PiecePositioned.getPosition(Square.e4));
        linkMovesToGames(rootGame, new Move[]{move1, move2, move3}, new Game[]{childGame1, childGame2, childGame3});

        SearchMoveResult searchResult = minMaxPruning.searchBestMove(rootGame, new SearchContext(1));

        Move bestMove = searchResult.getBestMove();

        assertNotNull(bestMove);
        assertEquals(GameEvaluator.WHITE_LOST, searchResult.getEvaluation());

        verify(rootGame, times(1)).executeMove(move1);
        verify(rootGame, times(1)).executeMove(move2);
        verify(rootGame, times(1)).executeMove(move3);

        verify(quiescence, times(1)).minimize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        verify(quiescence, times(1)).minimize(eq(childGame2), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        verify(quiescence, times(1)).minimize(eq(childGame3), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));

        verify(alphaBeta).minimize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        verify(alphaBeta).minimize(eq(childGame2), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        verify(alphaBeta).minimize(eq(childGame3), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
    }

    @Test
    @Disabled
    public void test_maximize_WhitePlays_MateCutOff() {
        AlphaBeta alphaBeta = Mockito.spy(new AlphaBeta());
        //AlphaBetaImp alphaBetaImp = new AlphaBetaImp();
        alphaBeta.setQuiescence(quiescence);
        alphaBeta.setMoveSorter(moveSorter);
        alphaBeta.setNext(alphaBeta);

        Game rootGame = setupGame(Color.WHITE, GameStatus.NO_CHECK);

        Game childGame1 = setupGame(Color.BLACK, GameStatus.NO_CHECK);
        when(quiescence.minimize(eq(childGame1), eq(2), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE))).thenReturn(1);

        Game childGame2 = setupGame(Color.BLACK, GameStatus.NO_CHECK);
        when(quiescence.minimize(eq(childGame2), eq(2), eq(1), eq(GameEvaluator.INFINITE_POSITIVE))).thenReturn(GameEvaluator.WHITE_WON);

        Game childGame3 = setupGame(Color.BLACK, GameStatus.NO_CHECK);

        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        Move move3 = mock(Move.class);
        linkMovesToGames(rootGame, new Move[]{move1, move2, move3}, new Game[]{childGame1, childGame2, childGame3});

        alphaBeta.init(rootGame, new SearchContext(2));
        int maxValue = alphaBeta.maximize(rootGame, 1, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE);

        assertEquals(GameEvaluator.WHITE_WON, maxValue);

        verify(rootGame, times(1)).executeMove(move1);
        verify(rootGame, times(1)).executeMove(move2);
        verify(rootGame, never()).executeMove(move3);

        verify(quiescence, times(1)).minimize(eq(childGame1), eq(2), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        verify(quiescence, times(1)).minimize(eq(childGame2), eq(2), eq(1), eq(GameEvaluator.INFINITE_POSITIVE));

        verify(alphaBeta).minimize(eq(childGame1), eq(2), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        verify(alphaBeta).minimize(eq(childGame2), eq(2), eq(1), eq(GameEvaluator.INFINITE_POSITIVE));

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
