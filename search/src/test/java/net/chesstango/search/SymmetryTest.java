package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.smart.alphabeta.statistics.node.NodeStatistics;
import net.chesstango.search.visitors.SetMaxDepthVisitor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 */
public class SymmetryTest {

    /**
     * Con TT:
     * - DEPTH 9 = 90 segs
     *
     * Sin TT:
     * - DEPTH 9 = 180 segs
     */
    @Test
    public void testSearch_Fried_Liver_Attack_Mirror() {
        final int depthAnalysis = 6;

        Search search1 = AlphaBetaBuilder
                .createDefaultBuilderInstance()
                .withGameEvaluator(new EvaluatorByMaterial())
                //.withDebugSearchTree(true, false, true)
                .build();
        Game game1 = Game.from(FEN.of("r1bqkb1r/ppp2Npp/2n5/3np3/B1Q1P3/8/PPPP1PPP/RNB1K2R b KQkq - 0 1"));
        search1.accept(new SetMaxDepthVisitor(depthAnalysis));
        SearchResult searchResultBlack = search1.startSearch(game1);

        int movesCount = game1.getPossibleMoves().size();

        Search search2 = AlphaBetaBuilder
                .createDefaultBuilderInstance()
                .withGameEvaluator(new EvaluatorByMaterial())
                //.withDebugSearchTree(true, false, true)
                .build();
        Game game2 = Game.from(FEN.of("r1bqkb1r/ppp2Npp/2n5/3np3/B1Q1P3/8/PPPP1PPP/RNB1K2R b KQkq - 0 1")).mirror();
        search2.accept(new SetMaxDepthVisitor(depthAnalysis));
        SearchResult searchResultWhite = search2.startSearch(game2);


        assertEquals(searchResultBlack.getBestEvaluation(), -searchResultWhite.getBestEvaluation());
        assertEquals(movesCount, game2.getPossibleMoves().size());

        for (int i = 0; i < depthAnalysis; i++) {
            SearchResultByDepth blackSearchResultByDepth = searchResultBlack.getSearchResultByDepths().get(i);
            SearchResultByDepth whiteSearchResultByDepth = searchResultWhite.getSearchResultByDepths().get(i);

            List<RootMoveEvaluation> blackRootMoveEvaluations = blackSearchResultByDepth.getRootMoveEvaluations();
            List<RootMoveEvaluation> whiteRootMoveEvaluations = whiteSearchResultByDepth.getRootMoveEvaluations();

            for (int j = 0; j < movesCount; j++) {
                RootMoveEvaluation blackRootMoveEvaluation = blackRootMoveEvaluations.get(j);
                RootMoveEvaluation whiteRootMoveEvaluation = whiteRootMoveEvaluations.get(j);

                assertEquals(blackRootMoveEvaluation.evaluation(), -whiteRootMoveEvaluation.evaluation());

                if (blackRootMoveEvaluation.bound() == Bound.EXACT) {
                    assertEquals(Bound.EXACT, whiteRootMoveEvaluation.bound());
                } else if (blackRootMoveEvaluation.bound() == Bound.UPPER_BOUND) {
                    assertEquals(Bound.LOWER_BOUND, whiteRootMoveEvaluation.bound());
                } else if (blackRootMoveEvaluation.bound() == Bound.LOWER_BOUND) {
                    assertEquals(Bound.UPPER_BOUND, whiteRootMoveEvaluation.bound());
                }

                Move blackMove = blackRootMoveEvaluation.move();
                Move whiteMove = whiteRootMoveEvaluation.move();

                assertEquals(blackMove.getFrom().piece(), whiteMove.getFrom().piece().getOpposite());
                assertEquals(blackMove.getFrom(), whiteMove.getFrom().getMirrorPosition());
                assertEquals(blackMove.getTo(), whiteMove.getTo().getMirrorPosition());
            }
        }
    }

    @Test
    public void symmetryMate02() {
        Game game = Game.from(FEN.of("3q1rk1/2n1p3/2r2bpB/p2n2N1/Pp1p3Q/6N1/1P4PP/R4R1K w - - 0 1"));

        Search search = buildSearch();

        SearchResult searchResult = search.startSearch(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.BISHOP_WHITE, smartMove.getFrom().piece());
        assertEquals(Square.h6, smartMove.getFrom().square());
        assertEquals(Square.g7, smartMove.getTo().square());

        assertEquals(Evaluator.WHITE_WON, searchResult.getBestEvaluation());


        /**
         * Testing mirror
         */
        Game mirrorGame = game.mirror();

        Search searchMirror = buildSearch();

        SearchResult searchResultMirror = searchMirror.startSearch(mirrorGame);

        Move smartMoveMirror = searchResultMirror.getBestMove();

        assertEquals(Piece.BISHOP_WHITE.getOpposite(), smartMoveMirror.getFrom().piece());
        assertEquals(Square.h6.mirror(), smartMoveMirror.getFrom().square());
        assertEquals(Square.g7.mirror(), smartMoveMirror.getTo().square());

        assertEquals(Evaluator.BLACK_WON, searchResultMirror.getBestEvaluation());

        /**
         * Testing mirror
         */
        NodeStatistics regularNodeStatistics = searchResult.getNodeStatistics();
        long[] expectedNodes = regularNodeStatistics.expectedNodesCounters();
        long[] visitedNodes = regularNodeStatistics.visitedNodesCounters();

        NodeStatistics regularNodeStatisticsMirror = searchResultMirror.getNodeStatistics();
        long[] expectedNodesMirror = regularNodeStatisticsMirror.expectedNodesCounters();
        long[] visitedNodesMirror = regularNodeStatisticsMirror.visitedNodesCounters();


        for (int i = 0; i < 30; i++) {
            assertEquals(expectedNodes[i], expectedNodesMirror[i]);
            assertEquals(visitedNodes[i], visitedNodesMirror[i]);
        }

    }

    private Search buildSearch() {
        return AlphaBetaBuilder
                .createDefaultBuilderInstance()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withStatistics()
                .build();
    }
}
