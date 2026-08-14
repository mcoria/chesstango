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
import org.junit.jupiter.api.Disabled;
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
     * <p>
     * Sin TT:
     * - DEPTH 9 = 103 segs
     */
    @Test
    public void test_START_POSITION() {
        Game game = Game.from(FEN.START_POSITION);

        Search search = defaultSearch()
                .withGameEvaluator(new EvaluatorByMaterial())
                //.withDebugSearchTree(true, false, false)
                .build();

        search.accept(new SetMaxDepthVisitor(9));
        SearchResult searchResult = search.startSearch(game);

        // Al final del dia la evaluacion es lo importante, tanto con TT como sin TT se mantiene en 0
        assertEquals(0, searchResult.getBestEvaluation());

        Move bm = searchResult.getBestMove();
        assertNotNull(bm);

        assertEquals(Piece.KNIGHT_WHITE, bm.getFrom().piece());
        assertEquals(Square.g1, bm.getFrom().square());
        assertEquals(Square.h3, bm.getTo().square());

        List<String> pv = searchResult.getPrincipalVariation().stream().map(PrincipalVariation::move).map(SimpleMoveEncoder.INSTANCE::encode).toList();
        assertArrayEquals(new String[]{"g1h3", "g8h6", "h3g5", "h6g4", "g5e4", "g4e5", "e4g5", "e5g4"}, pv.toArray());
        assertEquals(8, pv.size());     // Observar que PV size es menor que MaxDepth dado que entra en Loop

        assertTrue(searchResult.isPvComplete());

        /*
        List<String> pv = searchResult.getPrincipalVariation().stream().map(PrincipalVariation::move).map(SimpleMoveEncoder.INSTANCE::toPGN).toList();
        System.out.printf("Evaluation: %d%n", searchResult.getBestEvaluation());
        System.out.printf("PV moves %d: %s%n", pv.size(), Arrays.toString(pv.toArray()));
        System.out.printf("PV complete: %s", searchResult.isPvComplete());
         */
    }

    @Test
    public void test_40H_069() {
        Game game = Game.from(FEN.from("1B1Q1R2/8/qNrn3p/2p1rp2/Rn3k1K/8/5P2/bbN4B w - - 0 1"));

        Search search = defaultSearch()
                .withGameEvaluator(new EvaluatorByMaterial())
                //.withDebugSearchTree(false, true, true)
                .build();

        search.accept(new SetMaxDepthVisitor(3));
        SearchResult searchResult = search.startSearch(game);

        // Al final del dia la evaluacion es lo importante, tanto con TT como sin TT se mantiene
        assertEquals(Evaluator.WON, searchResult.getBestEvaluation());

        Move bm = searchResult.getBestMove();
        assertNotNull(bm);

        assertEquals(Piece.QUEEN_WHITE, bm.getFrom().piece());
        assertEquals(Square.d8, bm.getFrom().square());
        assertEquals(Square.f6, bm.getTo().square());

        List<String> pv = searchResult.getPrincipalVariation().stream().map(PrincipalVariation::move).map(SimpleMoveEncoder.INSTANCE::encode).toList();
        assertArrayEquals(new String[]{"d8f6", "d6c4", "c1e2"}, pv.toArray());

        assertTrue(searchResult.isPvComplete());
    }

    @Test
    public void test_40H_10021() {
        Game game = Game.from(FEN.from("3k4/p2r4/1pR4p/4Q3/8/5P2/q5P1/6K1 w - - 0 1"));

        Search search = defaultSearch()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withDebugSearchTree()
                .build();

        search.accept(new SetMaxDepthVisitor(5));

        SearchResult searchResult = search.startSearch(game);

        // Al final del dia la evaluacion es lo importante, tanto con TT como sin TT se mantiene
        assertEquals(Evaluator.WON, searchResult.getBestEvaluation());

        Move bm = searchResult.getBestMove();
        assertNotNull(bm);

        assertEquals(Piece.QUEEN_WHITE, bm.getFrom().piece());
        assertEquals(Square.e5, bm.getFrom().square());
        assertEquals(Square.f6, bm.getTo().square());

        List<String> pv = searchResult.getPrincipalVariation().stream().map(PrincipalVariation::move).map(SimpleMoveEncoder.INSTANCE::encode).toList();
        assertArrayEquals(new String[]{"e5f6", "d7e7", "f6f8", "e7e8", "f8d6"}, pv.toArray());

        assertTrue(searchResult.isPvComplete());
    }

    @Test
    public void test_HashMismatch() {
        Game game = Game.from(FEN.from("1Q1NR3/6pk/1r5p/3n1p1P/P2p4/1P1B4/1KP2q2/8 w - - 0 1"));

        Search search = defaultSearch()
                .withGameEvaluator(new EvaluatorByMaterial())
                //.withDebugSearchTree(true, false, true)
                .build();

        search.accept(new SetMaxDepthVisitor(5));
        SearchResult searchResult = search.startSearch(game);

        // Al final del dia la evaluacion es lo importante, tanto con TT como sin TT se mantiene
        assertEquals(Evaluator.WON, searchResult.getBestEvaluation());

        Move bm = searchResult.getBestMove();
        assertNotNull(bm);

        assertEquals(Piece.ROOK_WHITE, bm.getFrom().piece());
        assertEquals(Square.e8, bm.getFrom().square());
        assertEquals(Square.h8, bm.getTo().square());

        List<String> pv = searchResult.getPrincipalVariation().stream().map(PrincipalVariation::move).map(SimpleMoveEncoder.INSTANCE::encode).toList();
        assertArrayEquals(new String[]{"e8h8", "h7h8", "d8f7", "h8h7", "b8h8"}, pv.toArray());

        assertTrue(searchResult.isPvComplete());
    }

    @Test
    public void test_OutOfBound() {
        Game game = Game.from(FEN.from("1k1r4/pp1b1R2/3q2pp/4p3/2B5/4Q3/PPP2B2/2K5 b - - 1 1"));

        Search search = defaultSearch()
                .withGameEvaluator(new EvaluatorByMaterial())
                //.withDebugSearchTree(true, false, true)
                .build();

        search.accept(new SetMaxDepthVisitor(5));
        SearchResult searchResult = search.startSearch(game);

        // Al final del dia la evaluacion es lo importante, tanto con TT como sin TT se mantiene
        assertEquals(Evaluator.WON, searchResult.getBestEvaluation());

        Move bm = searchResult.getBestMove();
        assertNotNull(bm);

        assertEquals(Piece.QUEEN_BLACK, bm.getFrom().piece());
        assertEquals(Square.d6, bm.getFrom().square());
        assertEquals(Square.d1, bm.getTo().square());

        List<String> pv = searchResult.getPrincipalVariation().stream().map(PrincipalVariation::move).map(SimpleMoveEncoder.INSTANCE::encode).toList();
        assertArrayEquals(new String[]{"d6d1", "c1d1", "d7g4", "d1e1", "d8d1"}, pv.toArray());

        assertTrue(searchResult.isPvComplete());
    }

    @Test
    public void test_40H_001() {
        Game game = Game.from(FEN.from("1R5r/1R2bpp1/2k1p2r/q3P3/b1P2P1p/PN1P2n1/5QPP/6K1 w - - 0 1"));

        Search search = defaultSearch()
                .withGameEvaluator(new EvaluatorByMaterial())
                //.withDebugSearchTree(false, false, false)
                .build();

        search.accept(new SetMaxDepthVisitor(1));
        SearchResult searchResult = search.startSearch(game);


        // Al final del dia la evaluacion es lo importante, tanto con TT como sin TT se mantiene
        assertEquals(Evaluator.WON, searchResult.getBestEvaluation());

        Move bm = searchResult.getBestMove();
        assertNotNull(bm);

        assertEquals(Piece.KNIGHT_WHITE, bm.getFrom().piece());
        assertEquals(Square.b3, bm.getFrom().square());
        assertEquals(Square.a5, bm.getTo().square());

        List<String> pv = searchResult.getPrincipalVariation().stream().map(PrincipalVariation::move).map(SimpleMoveEncoder.INSTANCE::encode).toList();
        assertArrayEquals(new String[]{"b3a5"}, pv.toArray());

        assertTrue(searchResult.isPvComplete());

        /*
        List<String> thePV = searchResult.getPrincipalVariation()
                .stream()
                .map(PrincipalVariation::move)
                .map(SimpleMoveEncoder.INSTANCE::encode)
                .toList();
        System.out.printf("Evaluation: %d%n", searchResult.getBestEvaluation());
        System.out.printf("PV moves %d: %s%n", thePV.size(), Arrays.toString(thePV.toArray()));
        System.out.printf("PV complete: %s", searchResult.isPvComplete());
         */
    }

    @Test
    @Disabled
    public void test_1_7_0() {
        Game game = Game.from(FEN.from("rnbqkb1r/p4p2/2p1p2p/1p1nP1p1/2pP4/2N2NB1/PP3PPP/R2QKB1R w KQkq - 1 10"));

        Search search = defaultSearch()
                //.withGameEvaluator(new EvaluatorByMaterial())
                .withGameEvaluator(Evaluator.createInstance())
                //.withDebugSearchTree(true, true, true)
                .build();

        search.accept(new SetMaxDepthVisitor(5));
        SearchResult searchResult = search.startSearch(game);

        // Al final del dia la evaluacion es lo importante, tanto con TT como sin TT se mantiene
        assertEquals(45793, searchResult.getBestEvaluation());

        Move bm = searchResult.getBestMove();
        assertNotNull(bm);

        assertEquals(Piece.BISHOP_WHITE, bm.getFrom().piece());
        assertEquals(Square.f1, bm.getFrom().square());
        assertEquals(Square.e2, bm.getTo().square());

        List<String> pv = searchResult.getPrincipalVariation().stream().map(PrincipalVariation::move).map(SimpleMoveEncoder.INSTANCE::encode).toList();
        assertArrayEquals(new String[]{"f1e2", "g5g4", "f3d2", "d5c3", "b2c3"}, pv.toArray());

        assertTrue(searchResult.isPvComplete());
    }

    @Test
    @Disabled
    public void test_1_7_1() {
        Game game = Game.from(FEN.from("R7/6p1/P1Bp4/3Pb3/1K3k2/8/8/1r6 w - - 1 59"));

        Search search = defaultSearch()
                //.withGameEvaluator(new EvaluatorByMaterial())
                .withGameEvaluator(Evaluator.createInstance())
                //.withDebugSearchTree(false, false, false)
                .build();

        search.accept(new SetMaxDepthVisitor(5));
        SearchResult searchResult = search.startSearch(game);

        // Al final del dia la evaluacion es lo importante, tanto con TT como sin TT se mantiene
        // Observar que ahora esta fallando y entregando un valor menor: 63030
        assertEquals(69920, searchResult.getBestEvaluation());

        Move bm = searchResult.getBestMove();
        assertNotNull(bm);

        assertEquals(Piece.KING_WHITE, bm.getFrom().piece());
        assertEquals(Square.b4, bm.getFrom().square());
        assertEquals(Square.a5, bm.getTo().square());

        List<String> pv = searchResult.getPrincipalVariation().stream().map(PrincipalVariation::move).map(SimpleMoveEncoder.INSTANCE::encode).toList();
        assertArrayEquals(new String[]{"b4a5", "b1a1", "a5b5", "a1b1", "b5c4"}, pv.toArray());

        assertTrue(searchResult.isPvComplete());
    }


    private AlphaBetaBuilder defaultSearch() {
        return AlphaBetaBuilder.createDefaultBuilderInstance();
    }

    private AlphaBetaBuilder noTransposition() {
        return new AlphaBetaBuilder()
                .withGameEvaluatorCache()

                .withQuiescence()

                .withKillerMoveSorter()
                .withRecaptureSorter()
                .withMvvLvaSorter()

                .withAspirationWindows()

                .withIterativeDeepening()

                .withStopProcessingCatch();
    }

    private AlphaBetaBuilder noTranspositionNoQuiescence() {
        return new AlphaBetaBuilder()
                .withGameEvaluatorCache()

                .withKillerMoveSorter()
                .withRecaptureSorter()
                .withMvvLvaSorter()

                .withAspirationWindows()

                .withIterativeDeepening()

                .withStopProcessingCatch();
    }

    private AlphaBetaBuilder noTranspositionNoAspirationWindowsNoIterativeDeepening() {
        return new AlphaBetaBuilder()
                .withGameEvaluatorCache()

                .withQuiescence()

                .withKillerMoveSorter()
                .withRecaptureSorter()
                .withMvvLvaSorter()

                .withStopProcessingCatch();
    }
}
