package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.alphabeta.AlphaBeta;
import net.chesstango.search.smart.alphabeta.listeners.SetBestMoveOptions;
import net.chesstango.search.smart.alphabeta.listeners.SetTranspositionTables;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Mauricio Coria
 */
public class SetBestMoveOptionsTest {

    private AlphaBeta alphaBeta;

    @BeforeEach
    public void setup() {
        MoveSorter moveSorter = new DefaultMoveSorter();

        GameEvaluator gameEvaluator = new EvaluatorByMaterial();

        QuiescenceNull quiescence = new QuiescenceNull();
        quiescence.setGameEvaluator(gameEvaluator);

        TranspositionTable transpositionTable = new TranspositionTable();

        AlphaBetaImp alphaBetaImp = new AlphaBetaImp();
        alphaBetaImp.setQuiescence(quiescence);
        alphaBetaImp.setMoveSorter(moveSorter);
        alphaBetaImp.setNext(transpositionTable);
        alphaBetaImp.setGameEvaluator(gameEvaluator);

        transpositionTable.setNext(alphaBetaImp);

        SetBestMoveOptions setBestMoveOptions =  new SetBestMoveOptions();

        this.alphaBeta = new AlphaBeta();
        this.alphaBeta.setAlphaBetaSearch(transpositionTable);
        this.alphaBeta.setSearchActions(Arrays.asList(new SetTranspositionTables(), alphaBetaImp, transpositionTable, quiescence, moveSorter, setBestMoveOptions));
    }


    @Test
    public void testEvaluationCollisions01() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = new NoIterativeDeepening(alphaBeta).search(game,2);

        assertEquals(19, searchResult.getEvaluationCollisions());
    }

    @Test
    public void testEvaluationCollisions02() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = new NoIterativeDeepening(alphaBeta).search(game, 3);

        assertEquals(19, searchResult.getEvaluationCollisions());
    }
}
