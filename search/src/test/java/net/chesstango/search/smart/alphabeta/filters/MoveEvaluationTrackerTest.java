package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterialPieces;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.builders.AlphaBetaBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Mauricio Coria
 */
public class MoveEvaluationTrackerTest {

    private SearchMove searchMove;

    @BeforeEach
    public void setup() {
        this.searchMove = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterialPieces())
                .build();
    }


    @Test
    public void testEvaluationCollisions01() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        searchMove.setSearchParameter(SearchParameter.MAX_DEPTH, 1);
        SearchMoveResult searchResult = searchMove.search(game);

        assertEquals(19, searchResult.getPossibleCollisions().size());
    }

    @Test
    public void testEvaluationCollisions02() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        searchMove.setSearchParameter(SearchParameter.MAX_DEPTH, 2);
        SearchMoveResult searchResult = searchMove.search(game);

        assertEquals(19, searchResult.getPossibleCollisions().size());
    }
}
