package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.visitors.SetMaxDepthVisitor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class SymmetryTest {

    @Test
    public void symmetry_START_POSITION() {
        testSymmetry(FEN.START_POSITION, 7);
    }

    /**
     * Con TT:
     * - DEPTH 9 = 90 segs
     * <p>
     * Sin TT:
     * - DEPTH 9 = 180 segs
     */
    @Test
    public void symmetry_Fried_Liver_Attack_Mirror() {
        testSymmetry(FEN.from("r1bqkb1r/ppp2Npp/2n5/3np3/B1Q1P3/8/PPPP1PPP/RNB1K2R b KQkq - 0 1"), 6);
    }

    @Test
    public void symmetry_Mate02() {
        testSymmetry(FEN.from("3q1rk1/2n1p3/2r2bpB/p2n2N1/Pp1p3Q/6N1/1P4PP/R4R1K w - - 0 1"), 2);
    }

    private Search buildSearch() {
        return AlphaBetaBuilder
                .createDefaultBuilderInstance()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withStatistics()
                .build();
    }

    private void testSymmetry(FEN fen, int depthAnalysis) {
        Search search1 = buildSearch();
        Game game1 = Game.from(fen);
        search1.accept(new SetMaxDepthVisitor(depthAnalysis));
        SearchResult searchResultBlack = search1.startSearch(game1);

        Search search2 = buildSearch();
        Game game2 = Game.from(fen).mirror();
        search2.accept(new SetMaxDepthVisitor(depthAnalysis));
        SearchResult searchResultWhite = search2.startSearch(game2);

        assertEquals(searchResultBlack.getBestEvaluation(), searchResultWhite.getBestEvaluation());

        int movesCount = game1.getPossibleMoves().size();
        assertEquals(movesCount, game2.getPossibleMoves().size());

        for (int i = 0; i < depthAnalysis; i++) {
            SearchResultByDepth blackSearchResultByDepth = searchResultBlack.getSearchResultByDepths().get(i);
            SearchResultByDepth whiteSearchResultByDepth = searchResultWhite.getSearchResultByDepths().get(i);

            List<RootMoveEvaluation> blackRootMoveEvaluations = blackSearchResultByDepth.getRootMoveEvaluations();
            List<RootMoveEvaluation> whiteRootMoveEvaluations = whiteSearchResultByDepth.getRootMoveEvaluations();

            for (int j = 0; j < movesCount; j++) {
                RootMoveEvaluation blackRootMoveEvaluation = blackRootMoveEvaluations.get(j);
                RootMoveEvaluation whiteRootMoveEvaluation = whiteRootMoveEvaluations.get(j);

                assertEquals(blackRootMoveEvaluation.evaluation(), whiteRootMoveEvaluation.evaluation(), String.format("Evaluation mismatch %d %d", i, j));

                assertEquals(blackRootMoveEvaluation.bound(), whiteRootMoveEvaluation.bound());

                Move blackMove = blackRootMoveEvaluation.move();
                Move whiteMove = whiteRootMoveEvaluation.move();

                assertEquals(blackMove.getFrom().piece(), whiteMove.getFrom().piece().getOpposite());
                assertEquals(blackMove.getFrom(), whiteMove.getFrom().getMirrorPosition());
                assertEquals(blackMove.getTo(), whiteMove.getTo().getMirrorPosition());
            }
        }
    }
}
