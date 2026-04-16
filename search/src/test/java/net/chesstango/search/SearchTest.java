package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.visitors.SetMaxDepthVisitor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mauricio Coria
 */
public class SearchTest {

    /**
     * Con TT:
     * - DEPTH 9 = 23 segs
     *
     * Sin TT:
     * - DEPTH 9 = 103 segs
     */
    @Test
    public void test_START_POSITION() {
        Game game = Game.from(FEN.START_POSITION);

        Search search = AlphaBetaBuilder
                .createDefaultBuilderInstance()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withDebugSearchTree(true, true, true)
                .build();

        search.accept(new SetMaxDepthVisitor(9));
        SearchResult searchResult = search.startSearch(game);

        Move bm = searchResult.getBestMove();

        assertNotNull(bm);

        assertEquals(Piece.KNIGHT_WHITE, bm.getFrom().piece());
        assertEquals(Square.g1, bm.getFrom().square());
        assertEquals(Square.h3, bm.getTo().square());
    }

    @Test
    public void testSearch_40H_069() {
        Game game = Game.from(FEN.of("1B1Q1R2/8/qNrn3p/2p1rp2/Rn3k1K/8/5P2/bbN4B w - - 0 1"));

        Search search = AlphaBetaBuilder
                .createDefaultBuilderInstance()
                //.withGameEvaluator(new EvaluatorByMaterial())
                .withGameEvaluator(Evaluator.createInstance())
                //.withDebugSearchTree(true, false, false)
                .build();

        search.accept(new SetMaxDepthVisitor(3));
        SearchResult searchResult = search.startSearch(game);

        Move bm = searchResult.getBestMove();

        assertNotNull(bm);

        assertEquals(Piece.QUEEN_WHITE, bm.getFrom().piece());
        assertEquals(Square.d8, bm.getFrom().square());
        assertEquals(Square.f6, bm.getTo().square());

        assertTrue(searchResult.isPvComplete());

        //List<String> pv = searchResult.getPrincipalVariation().stream().map(PrincipalVariation::move).map(SimpleMoveEncoder.INSTANCE::encode).toList();
        //assertArrayEquals(new String[]{"d8f6", "h6h5", "f6g5"}, pv.toArray());
    }

    @Test
    public void testSearch_40H_10021() {
        Game game = Game.from(FEN.of("3k4/p2r4/1pR4p/4Q3/8/5P2/q5P1/6K1 w - - 0 1"));

        Search search = AlphaBetaBuilder
                .createDefaultBuilderInstance()
                .withGameEvaluator(new EvaluatorByMaterial())
                //.withDebugSearchTree(true, false, true)
                .build();

        search.accept(new SetMaxDepthVisitor(5));
        SearchResult searchResult = search.startSearch(game);

        Move bm = searchResult.getBestMove();

        assertNotNull(bm);

        assertEquals(Piece.QUEEN_WHITE, bm.getFrom().piece());
        assertEquals(Square.e5, bm.getFrom().square());
        assertEquals(Square.f6, bm.getTo().square());

        assertTrue(searchResult.isPvComplete());

        List<String> pv = searchResult.getPrincipalVariation().stream().map(PrincipalVariation::move).map(SimpleMoveEncoder.INSTANCE::encode).toList();
        assertArrayEquals(new String[]{"e5f6", "d7e7", "f6f8", "e7e8", "f8d6"}, pv.toArray());
    }

    @Test
    public void testHashMismatch() {
        Game game = Game.from(FEN.of("1Q1NR3/6pk/1r5p/3n1p1P/P2p4/1P1B4/1KP2q2/8 w - - 0 1"));

        Search search = AlphaBetaBuilder
                .createDefaultBuilderInstance()
                .withGameEvaluator(Evaluator.createInstance())
                //.withDebugSearchTree(true, false, true)
                .build();

        search.accept(new SetMaxDepthVisitor(5));
        SearchResult searchResult = search.startSearch(game);

        Move bm = searchResult.getBestMove();

        assertNotNull(bm);
    }

    @Test
    public void testOutOfBound() {
        Game game = Game.from(FEN.of("1k1r4/pp1b1R2/3q2pp/4p3/2B5/4Q3/PPP2B2/2K5 b - - 1 1"));

        Search search = AlphaBetaBuilder
                .createDefaultBuilderInstance()
                .withGameEvaluator(Evaluator.createInstance())
                //.withDebugSearchTree(true, false, true)
                .build();

        search.accept(new SetMaxDepthVisitor(4));
        SearchResult searchResult = search.startSearch(game);

        Move bm = searchResult.getBestMove();

        assertNotNull(bm);
    }
}
