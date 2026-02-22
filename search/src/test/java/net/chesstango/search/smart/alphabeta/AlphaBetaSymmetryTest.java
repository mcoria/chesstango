package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.Search;
import net.chesstango.search.SearchResult;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.smart.features.statistics.node.NodeStatistics;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaSymmetryTest {


    @Test
    @Disabled
    public void symmetryMate02() {
        Game game = Game.from(FEN.of("3q1rk1/2n1p3/2r2bpB/p2n2N1/Pp1p3Q/6N1/1P4PP/R4R1K w - -"));

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
        NodeStatistics regularNodeStatistics = searchResult.getRegularNodeStatistics();
        long[] expectedNodes = regularNodeStatistics.expectedNodesCounters();
        long[] visitedNodes = regularNodeStatistics.visitedNodesCounters();

        NodeStatistics regularNodeStatisticsMirror = searchResultMirror.getRegularNodeStatistics();
        long[] expectedNodesMirror = regularNodeStatisticsMirror.expectedNodesCounters();
        long[] visitedNodesMirror = regularNodeStatisticsMirror.visitedNodesCounters();

        NodeStatistics quiescenceNodeStatistics = searchResult.getQuiescenceNodeStatistics();
        long[] visitedNodesQ = quiescenceNodeStatistics.visitedNodesCounters();

        NodeStatistics quiescenceNodeStatisticsMirror = searchResultMirror.getQuiescenceNodeStatistics();
        long[] visitedNodesQMirror = quiescenceNodeStatisticsMirror.visitedNodesCounters();
        for (int i = 0; i < 30; i++) {
            assertEquals(expectedNodes[i], expectedNodesMirror[i]);
            assertEquals(visitedNodes[i], visitedNodesMirror[i]);
            assertEquals(visitedNodesQ[i], visitedNodesQMirror[i]);
        }


    }

    private Search buildSearch() {
        return new AlphaBetaBuilder()
                //.withGameEvaluator(new EvaluatorByMaterial())
                .withGameEvaluator(Evaluator.createInstance())

                .withQuiescence()

                .withTranspositionTable()

                .withTranspositionMoveSorter()

                .withIterativeDeepening()

                .withStatistics()

                .build();
    }
}
