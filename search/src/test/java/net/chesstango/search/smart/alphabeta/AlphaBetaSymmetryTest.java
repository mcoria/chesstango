package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.DefaultEvaluator;
import net.chesstango.evaluation.Evaluator;
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
        Game game = FENDecoder.loadGame("3q1rk1/2n1p3/2r2bpB/p2n2N1/Pp1p3Q/6N1/1P4PP/R4R1K w - -");

        Search search = buildSearch();

        SearchResult searchResult = search.search(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.BISHOP_WHITE, smartMove.getFrom().getPiece());
        assertEquals(Square.h6, smartMove.getFrom().getSquare());
        assertEquals(Square.g7, smartMove.getTo().getSquare());

        assertEquals(Evaluator.WHITE_WON, searchResult.getBestEvaluation());


        /**
         * Testing mirror
         */
        Game mirrorGame = game.mirror();

        Search searchMirror = buildSearch();

        SearchResult searchResultMirror = searchMirror.search(mirrorGame);

        Move smartMoveMirror = searchResultMirror.getBestMove();

        assertEquals(Piece.BISHOP_WHITE.getOpposite(), smartMoveMirror.getFrom().getPiece());
        assertEquals(Square.h6.getMirrorSquare(), smartMoveMirror.getFrom().getSquare());
        assertEquals(Square.g7.getMirrorSquare(), smartMoveMirror.getTo().getSquare());

        assertEquals(Evaluator.BLACK_WON, searchResultMirror.getBestEvaluation());

        /**
         * Testing mirror
         */
        NodeStatistics regularNodeStatistics = searchResult.getRegularNodeStatistics();
        int[] expectedNodes = regularNodeStatistics.expectedNodesCounters();
        int[] visitedNodes = regularNodeStatistics.visitedNodesCounters();

        NodeStatistics regularNodeStatisticsMirror = searchResultMirror.getRegularNodeStatistics();
        int[] expectedNodesMirror = regularNodeStatisticsMirror.expectedNodesCounters();
        int[] visitedNodesMirror = regularNodeStatisticsMirror.visitedNodesCounters();

        NodeStatistics quiescenceNodeStatistics = searchResult.getQuiescenceNodeStatistics();
        int[] visitedNodesQ = quiescenceNodeStatistics.visitedNodesCounters();

        NodeStatistics quiescenceNodeStatisticsMirror = searchResultMirror.getQuiescenceNodeStatistics();
        int[] visitedNodesQMirror = quiescenceNodeStatisticsMirror.visitedNodesCounters();
        for (int i = 0; i < 30; i++) {
            assertTrue(expectedNodes[i] == expectedNodesMirror[i]);
            assertTrue(visitedNodes[i] == visitedNodesMirror[i]);
            assertTrue(visitedNodesQ[i] == visitedNodesQMirror[i]);
        }


    }

    private Search buildSearch() {
        return new AlphaBetaBuilder()
                //.withGameEvaluator(new EvaluatorByMaterial())
                .withGameEvaluator(new DefaultEvaluator())

                .withQuiescence()

                .withTranspositionTable()

                .withTranspositionMoveSorter()

                .withIterativeDeepening()

                .withStatistics()

                .build();
    }
}
