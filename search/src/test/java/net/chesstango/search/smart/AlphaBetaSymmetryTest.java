package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.DefaultEvaluator;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.smart.statics.RNodeStatistics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaSymmetryTest {


    @Test
    public void symmetryMate02() {
        Game game = FENDecoder.loadGame("3q1rk1/2n1p3/2r2bpB/p2n2N1/Pp1p3Q/6N1/1P4PP/R4R1K w - -");

        SearchMove search = buildSearch();

        SearchMoveResult searchResult = search.search(game, 4);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.BISHOP_WHITE, smartMove.getFrom().getPiece());
        assertEquals(Square.h6, smartMove.getFrom().getSquare());
        assertEquals(Square.g7, smartMove.getTo().getSquare());

        assertEquals(GameEvaluator.WHITE_WON, searchResult.getEvaluation());


        /**
         * Testing mirror
         */
        Game mirrorGame = game.mirror();

        SearchMove searchMirror = buildSearch();

        SearchMoveResult searchResultMirror = searchMirror.search(mirrorGame, 4);

        Move smartMoveMirror = searchResultMirror.getBestMove();

        assertEquals(Piece.BISHOP_WHITE.getOpposite(), smartMoveMirror.getFrom().getPiece());
        assertEquals(Square.h6.getMirrorSquare(), smartMoveMirror.getFrom().getSquare());
        assertEquals(Square.g7.getMirrorSquare(), smartMoveMirror.getTo().getSquare());

        assertEquals(GameEvaluator.BLACK_WON, searchResultMirror.getEvaluation());

        /**
         * Testing mirror
         */
        RNodeStatistics regularNodeStatistics = searchResult.getRegularNodeStatistics();
        int[] expectedNodes = regularNodeStatistics.expectedNodesCounters();
        int[] visitedNodes = regularNodeStatistics.visitedNodesCounters();

        RNodeStatistics regularNodeStatisticsMirror = searchResultMirror.getRegularNodeStatistics();
        int[] expectedNodesMirror = regularNodeStatisticsMirror.expectedNodesCounters();
        int[] visitedNodesMirror = regularNodeStatisticsMirror.visitedNodesCounters();

        int[] visitedNodesQ = searchResult.getVisitedNodesQuiescenceCounter();
        int[] visitedNodesQMirror = searchResultMirror.getVisitedNodesQuiescenceCounter();
        for (int i = 0; i < 30; i++) {
            assertTrue(expectedNodes[i] == expectedNodesMirror[i]);
            assertTrue(visitedNodes[i] == visitedNodesMirror[i]);
            assertTrue(visitedNodesQ[i] == visitedNodesQMirror[i]);
        }


    }

    private SearchMove buildSearch() {
        return new AlphaBetaBuilder()
                //.withGameEvaluator(new EvaluatorByMaterial())
                .withGameEvaluator(new DefaultEvaluator())

                .withQuiescence()

                .withTranspositionTable()
                .withQTranspositionTable()

                .withTranspositionMoveSorter()
                .withQTranspositionMoveSorter()

                .withIterativeDeepening()

                .withtStatistics()

                .build();
    }
}
