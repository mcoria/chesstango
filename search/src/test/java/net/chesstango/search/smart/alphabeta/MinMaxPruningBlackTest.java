package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.*;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveFactory;
import net.chesstango.board.moves.containers.MoveContainer;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.alphabeta.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.filters.Quiescence;
import net.chesstango.search.smart.sorters.MoveSorter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
public class MinMaxPruningBlackTest {

    private MoveFactory moveFactoryBlack = SingletonMoveFactories.getDefaultMoveFactoryBlack();

    @Mock
    private MoveSorter moveSorter;

    @Mock
    private Quiescence quiescence;

    @BeforeEach
    public void setUp() {
    }


    @Test
    @Disabled
    public void test_findBestMove_BlackPlays_SingleMove() {
        AlphaBeta alphaBeta = new AlphaBeta();
        alphaBeta.setQuiescence(quiescence);
        alphaBeta.setMoveSorter(moveSorter);

        MinMaxPruning minMaxPruning = new MinMaxPruning();
        minMaxPruning.setAlphaBetaSearch(alphaBeta);
        minMaxPruning.setSearchActions(Arrays.asList(alphaBeta, quiescence));

        Game rootGame = setupGame(Color.BLACK, GameStatus.NO_CHECK);

        Game childGame = setupGame(Color.WHITE, GameStatus.NO_CHECK);
        //when(quiescence.maximize(eq(childGame), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE))).thenReturn(1L);

        Move move = mock(Move.class);

        linkMovesToGames(rootGame, new Move[]{move}, new Game[]{childGame});

        SearchMoveResult searchResult = minMaxPruning.searchBestMove(rootGame, new SearchContext(1));

        Move bestMove = searchResult.getBestMove();

        assertEquals(move, bestMove);
        assertEquals(1, searchResult.getEvaluation());
        //verify(quiescence, times(1)).maximize(eq(childGame), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
    }

    @Test
    @Disabled
    public void test_findBestMove_BlackPlays_TwoMoves() {
        AlphaBeta alphaBeta = Mockito.spy(new AlphaBeta());
        alphaBeta.setQuiescence(quiescence);
        alphaBeta.setMoveSorter(moveSorter);

        MinMaxPruning minMaxPruning = Mockito.spy(new MinMaxPruning());
        minMaxPruning.setAlphaBetaSearch(alphaBeta);
        minMaxPruning.setSearchActions(Arrays.asList(alphaBeta, quiescence));

        Game rootGame = setupGame(Color.BLACK, GameStatus.NO_CHECK);

        Game childGame1 = setupGame(Color.WHITE, GameStatus.NO_CHECK);
        //when(quiescence.maximize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE))).thenReturn(1L);

        Game childGame2 = setupGame(Color.WHITE, GameStatus.NO_CHECK);
        //when(quiescence.maximize(eq(childGame2), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(1))).thenReturn(2L);

        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        linkMovesToGames(rootGame, new Move[]{move1, move2}, new Game[]{childGame1, childGame2});

        SearchMoveResult searchResult = minMaxPruning.searchBestMove(rootGame, new SearchContext(1));

        Move bestMove = searchResult.getBestMove();

        assertEquals(move1, bestMove);
        assertEquals(1, searchResult.getEvaluation());

        //verify(quiescence, times(1)).maximize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        //verify(quiescence, times(1)).maximize(eq(childGame2), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(1));

        //verify(alphaBeta).maximize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        //verify(alphaBeta).maximize(eq(childGame2), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(1));
    }

    @Test
    @Disabled
    public void test_findBestMove_BlackPlays_MateCutOff() {
        AlphaBeta alphaBeta = Mockito.spy(new AlphaBeta());
        alphaBeta.setQuiescence(quiescence);
        alphaBeta.setMoveSorter(moveSorter);

        MinMaxPruning minMaxPruning = Mockito.spy(new MinMaxPruning());
        minMaxPruning.setAlphaBetaSearch(alphaBeta);
        minMaxPruning.setSearchActions(Arrays.asList(alphaBeta, quiescence));

        Game rootGame = setupGame(Color.BLACK, GameStatus.NO_CHECK);

        Game childGame1 = setupGame(Color.WHITE, GameStatus.NO_CHECK);
        //when(quiescence.maximize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE))).thenReturn(1L);

        Game childGame2 = setupGame(Color.WHITE, GameStatus.NO_CHECK);
        //when(quiescence.maximize(eq(childGame2), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(1))).thenReturn((long)GameEvaluator.BLACK_WON);

        // childGame3 no llega a evaluarse, dado que existe CuteOff por el mate que se encuentra en childGame2
        Game childGame3 = setupGame(Color.WHITE, GameStatus.NO_CHECK);

        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        Move move3 = mock(Move.class);
        linkMovesToGames(rootGame, new Move[]{move1, move2, move3}, new Game[]{childGame1, childGame2, childGame3});

        SearchMoveResult searchResult = minMaxPruning.searchBestMove(rootGame, new SearchContext(1));

        Move bestMove = searchResult.getBestMove();

        assertEquals(move2, bestMove);
        assertEquals(GameEvaluator.BLACK_WON, searchResult.getEvaluation());

        verify(rootGame, times(1)).executeMove(move1);
        verify(rootGame, times(1)).executeMove(move2);
        verify(rootGame, never()).executeMove(move3);

        //verify(quiescence, times(1)).maximize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        //verify(quiescence, times(1)).maximize(eq(childGame2), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(1));

        //verify(alphaBeta).maximize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        //verify(alphaBeta).maximize(eq(childGame2), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(1));

        verifyNoInteractions(move3);
        verifyNoInteractions(childGame3);
    }

    @Test
    @Disabled
    public void test_findBestMove_BlackPlays_ImminentMate() {
        AlphaBeta alphaBeta = Mockito.spy(new AlphaBeta());
        alphaBeta.setQuiescence(quiescence);
        alphaBeta.setMoveSorter(moveSorter);

        MinMaxPruning minMaxPruning = Mockito.spy(new MinMaxPruning());
        minMaxPruning.setAlphaBetaSearch(alphaBeta);
        minMaxPruning.setSearchActions(Arrays.asList(alphaBeta, quiescence));

        Game rootGame = setupGame(Color.BLACK, GameStatus.NO_CHECK);

        Game childGame1 = setupGame(Color.WHITE, GameStatus.NO_CHECK);
        //when(quiescence.maximize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE))).thenReturn((long)GameEvaluator.BLACK_LOST);

        Game childGame2 = setupGame(Color.WHITE, GameStatus.NO_CHECK);
        //when(quiescence.maximize(eq(childGame2), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE))).thenReturn((long)GameEvaluator.BLACK_LOST);

        Game childGame3 = setupGame(Color.WHITE, GameStatus.NO_CHECK);
        //when(quiescence.maximize(eq(childGame3), eq(1),eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE))).thenReturn((long)GameEvaluator.BLACK_LOST);

        Move move1 = moveFactoryBlack.createCaptureMove(PiecePositioned.getPiecePositioned(Square.c3, Piece.KNIGHT_BLACK), PiecePositioned.getPosition(Square.d4));
        Move move2 = moveFactoryBlack.createCaptureMove(PiecePositioned.getPiecePositioned(Square.c3, Piece.KNIGHT_BLACK), PiecePositioned.getPosition(Square.b4));
        Move move3 = moveFactoryBlack.createCaptureMove(PiecePositioned.getPiecePositioned(Square.c3, Piece.KNIGHT_BLACK), PiecePositioned.getPosition(Square.e5));
        linkMovesToGames(rootGame, new Move[]{move1, move2, move3}, new Game[]{childGame1, childGame2, childGame3});

        SearchMoveResult searchResult = minMaxPruning.searchBestMove(rootGame, new SearchContext(1));

        Move bestMove = searchResult.getBestMove();

        assertNotNull(bestMove);
        assertEquals(GameEvaluator.BLACK_LOST, searchResult.getEvaluation());

        verify(rootGame, times(1)).executeMove(move1);
        verify(rootGame, times(1)).executeMove(move2);
        verify(rootGame, times(1)).executeMove(move3);

        //verify(quiescence, times(1)).maximize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        //verify(quiescence, times(1)).maximize(eq(childGame2), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        //verify(quiescence, times(1)).maximize(eq(childGame3), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));

        //verify(alphaBeta).maximize(eq(childGame1), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        //verify(alphaBeta).maximize(eq(childGame2), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        //verify(alphaBeta).maximize(eq(childGame3), eq(1), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
    }

    @Test
    @Disabled
    public void test_minimize_BlackPlays_MateCutOff() {
        AlphaBeta alphaBeta = Mockito.spy(new AlphaBeta());
        //AlphaBetaImp alphaBetaImp = new AlphaBetaImp();
        alphaBeta.setQuiescence(quiescence);
        alphaBeta.setMoveSorter(moveSorter);
        alphaBeta.setNext(alphaBeta);

        Game rootGame = setupGame(Color.BLACK, GameStatus.NO_CHECK);

        Game childGame1 = setupGame(Color.WHITE, GameStatus.NO_CHECK);
        //when(quiescence.maximize(eq(childGame1), eq(2), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE))).thenReturn(1L);

        Game childGame2 = setupGame(Color.WHITE, GameStatus.NO_CHECK);
        //when(quiescence.maximize(eq(childGame2), eq(2), eq(GameEvaluator.INFINITE_NEGATIVE), eq(1))).thenReturn((long)GameEvaluator.BLACK_WON);

        Game childGame3 = setupGame(Color.WHITE, GameStatus.NO_CHECK);

        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        Move move3 = mock(Move.class);
        linkMovesToGames(rootGame, new Move[]{move1, move2, move3}, new Game[]{childGame1, childGame2, childGame3});

        alphaBeta.init(rootGame, new SearchContext(2));
        //long minValue = alphaBeta.minimize(rootGame, 1, GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.INFINITE_POSITIVE);

        //assertEquals(GameEvaluator.BLACK_WON, minValue);

        verify(rootGame, times(1)).executeMove(move1);
        verify(rootGame, times(1)).executeMove(move2);
        verify(rootGame, never()).executeMove(move3);

        //verify(quiescence, times(1)).maximize(eq(childGame1), eq(2), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        //verify(quiescence, times(1)).maximize(eq(childGame2), eq(2), eq(GameEvaluator.INFINITE_NEGATIVE), eq(1));

        //verify(alphaBeta).maximize(eq(childGame1), eq(2), eq(GameEvaluator.INFINITE_NEGATIVE), eq(GameEvaluator.INFINITE_POSITIVE));
        //verify(alphaBeta).maximize(eq(childGame2), eq(2), eq(GameEvaluator.INFINITE_NEGATIVE), eq(1));

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

        when(moveSorter.getSortedMoves()).thenReturn(moveList);
    }

}
