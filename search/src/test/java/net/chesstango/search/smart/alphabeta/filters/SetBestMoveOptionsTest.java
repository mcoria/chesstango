package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.alphabeta.MinMaxPruning;
import net.chesstango.search.smart.alphabeta.listeners.SetBestMoveOptions;
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

    private MinMaxPruning minMaxPruning;

    @BeforeEach
    public void setup() {
        MoveSorter moveSorter = new DefaultMoveSorter();

        GameEvaluator gameEvaluator = new GameEvaluatorByMaterial();

        QuiescenceNull quiescence = new QuiescenceNull();
        quiescence.setGameEvaluator(gameEvaluator);

        TranspositionTable transpositionTable = new TranspositionTable();

        AlphaBeta alphaBeta = new AlphaBeta();
        alphaBeta.setQuiescence(quiescence);
        alphaBeta.setMoveSorter(moveSorter);
        alphaBeta.setNext(transpositionTable);
        alphaBeta.setGameEvaluator(gameEvaluator);

        transpositionTable.setNext(alphaBeta);

        SetBestMoveOptions setBestMoveOptions =  new SetBestMoveOptions();

        minMaxPruning = new MinMaxPruning();
        minMaxPruning.setAlphaBetaSearch(transpositionTable);
        minMaxPruning.setSearchActions(Arrays.asList(alphaBeta, transpositionTable, quiescence, moveSorter, setBestMoveOptions));
    }


    @Test
    public void testEvaluationCollisions01() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = minMaxPruning.search(game, new SearchContext(2));

        assertEquals(19, searchResult.getEvaluationCollisions());
    }

    @Test
    public void testEvaluationCollisions02() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = minMaxPruning.search(game, new SearchContext(3));

        assertEquals(19, searchResult.getEvaluationCollisions());
    }
}
