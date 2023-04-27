package net.chesstango.search.smart.negamax;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainer;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class NegaMaxTest {

    @Mock
    private GameEvaluator evaluator;

    private NegaMax negaMax;

    @BeforeEach
    public void setup() {
        negaMax = new NegaMax();
        negaMax.setGameEvaluator(evaluator);
    }

    @Test
    public void testSingleMoveWhitePlays() {
        Game rootGame = setupGame(Color.WHITE);

        Game childGame = setupGame(Color.BLACK);
        when(evaluator.evaluate(childGame)).thenReturn(1);

        Move move = mock(Move.class);

        linkMovesToGames(rootGame, new Move[]{move}, new Game[]{childGame});

        SearchMoveResult searchResult = negaMax.searchBestMove(rootGame, new SearchContext(1));

        Move bestMove = searchResult.getBestMove();

        assertEquals(move, bestMove);
        assertEquals(1, searchResult.getEvaluation());
        verify(evaluator, times(1)).evaluate(childGame);
    }

    @Test
    public void testSingleMoveBlackPlays() {
        Game rootGame = setupGame(Color.BLACK);

        Game childGame = setupGame(Color.WHITE);
        when(evaluator.evaluate(childGame)).thenReturn(1);

        Move move = mock(Move.class);

        linkMovesToGames(rootGame, new Move[]{move}, new Game[]{childGame});

        SearchMoveResult searchResult = negaMax.searchBestMove(rootGame, new SearchContext(1));

        Move bestMove = searchResult.getBestMove();

        assertEquals(move, bestMove);
        assertEquals(1, searchResult.getEvaluation());
        verify(evaluator, times(1)).evaluate(childGame);
    }

    @Test
    public void testTwoMovesWhitePlays() {
        NegaMax minMax = Mockito.spy(this.negaMax);

        Game rootGame = setupGame(Color.WHITE);

        Game childGame1 = setupGame(Color.BLACK);
        when(evaluator.evaluate(childGame1)).thenReturn(1);

        Game childGame2 = setupGame(Color.BLACK);
        when(evaluator.evaluate(childGame2)).thenReturn(2);

        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        linkMovesToGames(rootGame, new Move[]{move1, move2}, new Game[]{childGame1, childGame2});

        SearchMoveResult searchResult = minMax.searchBestMove(rootGame, new SearchContext(1));

        Move bestMove = searchResult.getBestMove();

        assertEquals(move2, bestMove);
        assertEquals(2, searchResult.getEvaluation());

        verify(minMax).negaMax(childGame1, 0);
        verify(minMax).negaMax(childGame2, 0);

        verify(evaluator, times(1)).evaluate(childGame1);
        verify(evaluator, times(1)).evaluate(childGame2);
    }

    @Test
    public void testTwoMovesBlackPlays() {
        NegaMax minMax = Mockito.spy(this.negaMax);

        Game rootGame = setupGame(Color.BLACK);

        Game childGame1 = setupGame(Color.WHITE);
        when(evaluator.evaluate(childGame1)).thenReturn(1);

        Game childGame2 = setupGame(Color.WHITE);
        when(evaluator.evaluate(childGame2)).thenReturn(2);

        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        linkMovesToGames(rootGame, new Move[]{move1, move2}, new Game[]{childGame1, childGame2});

        SearchMoveResult searchResult = minMax.searchBestMove(rootGame, new SearchContext(1));

        Move bestMove = searchResult.getBestMove();

        assertEquals(move1, bestMove);
        assertEquals(1, searchResult.getEvaluation());

        verify(minMax).negaMax(childGame1, 0);
        verify(minMax).negaMax(childGame2, 0);

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
    }
}
