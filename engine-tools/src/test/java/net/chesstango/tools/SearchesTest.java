package net.chesstango.tools;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.board.representations.pgn.PGNStringDecoder;
import net.chesstango.board.representations.pgn.PGN;
import net.chesstango.evaluation.DefaultEvaluator;
import net.chesstango.search.Search;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.builders.AlphaBetaBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * @author Mauricio Coria
 */
public class SearchesTest {
    private static final boolean PRINT_REPORT = false;
    private Search search;
    private SearchResult searchResult;

    private SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

    @BeforeEach
    public void setup() {
        searchResult = null;

        search = new AlphaBetaBuilder()
                .withGameEvaluator(new DefaultEvaluator())
                .withGameEvaluatorCache()

                .withQuiescence()

                .withTranspositionTable()

                .withTranspositionMoveSorter()
                .withKillerMoveSorter()
                .withRecaptureSorter()
                .withMvvLvaSorter()

                .withAspirationWindows()
                .withIterativeDeepening()

                //.withStatistics()
                //.withZobristTracker()
                //.withTrackEvaluations() // Consume demasiada memoria
                //.withMoveEvaluation()

                //.withPrintChain()
                //.withDebugSearchTree(null)

                .withStopProcessingCatch()

                .build();
    }

    @AfterEach
    public void printReport() {
        /*
        if (PRINT_REPORT) {
            new NodesReport()
                    .withMoveResults(List.of(searchResult))
                    .withCutoffStatistics()
                    .withNodesVisitedStatistics()
                    .printReport(System.out);

            new EvaluationReport()
                    .withMoveResults(List.of(searchResult))
                    //.withExportEvaluations()
                    .withEvaluationsStatistics()
                    .printReport(System.out);
            new PrincipalVariationReport()
                    .withMoveResults(List.of(searchResult))
                    .printReport(System.out);
        }
         */
    }

    @Test
    @Disabled
    public void testSearch_00() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        search.setSearchParameter(SearchParameter.MAX_DEPTH, 6);
        searchResult = search.search(game);
    }


    @Test
    @Disabled
    public void testSearch_01() {
        Game game = FENDecoder.loadGame("r4rk1/p1qbp1b1/2p3pp/2Pn1p2/1pQ5/5B2/PPP1NPPP/R1B2RK1 w - - 1 22");

        search.setSearchParameter(SearchParameter.MAX_DEPTH, 6);
        searchResult = search.search(game);
    }

    @Test
    @Disabled
    public void testSearch_02() {
        Game game = FENDecoder.loadGame("1k2r3/1pp5/4B3/1P3Q2/3q1Pp1/3n2Pp/3p3P/5R1K b - - 0 1");

        search.setSearchParameter(SearchParameter.MAX_DEPTH, 5);
        searchResult = search.search(game);
    }


    @Test
    @Disabled
    public void testSearch_03() {
        Game game = FENDecoder.loadGame("8/p7/2R5/4k3/8/Pp1b3P/1r3PP1/6K1 w - - 2 43");

        search.setSearchParameter(SearchParameter.MAX_DEPTH, 7);
        searchResult = search.search(game);
    }


    @Test
    @Disabled
    public void testSearch_04() {
        Game game = FENDecoder.loadGame("4R3/6pk/1p4Bp/5p2/p5P1/2BP3P/5P2/6K1 b - - 0 39");

        search.setSearchParameter(SearchParameter.MAX_DEPTH, 7);
        searchResult = search.search(game);
    }


    @Test
    @Disabled
    public void testSearch_06() {
        Game game = FENDecoder.loadGame("R7/P4k2/8/8/8/8/r7/6K1 w - - 0 1");

        search.setSearchParameter(SearchParameter.MAX_DEPTH, 7);
        searchResult = search.search(game);
    }

    @Test
    @Disabled
    public void testSearch_07() {
        Game game = FENDecoder.loadGame("2rr2k1/2p2ppp/1p3bn1/p2P1q2/2P5/1Q4B1/PP3PPP/R2R2K1 w - - 6 22");

        search.setSearchParameter(SearchParameter.MAX_DEPTH, 3);
        searchResult = search.search(game);
    }

    @Test
    @Disabled
    public void testSearch_08() {
        Game game = FENDecoder.loadGame("7k/6p1/8/8/8/N7/8/K7 w - - 0 1");

        search.setSearchParameter(SearchParameter.MAX_DEPTH, 9);
        searchResult = search.search(game);
    }


    @Test
    @Disabled
    public void testSearch_09() {
        Game game = FENDecoder.loadGame("rnb2rk1/pp3ppp/4pn2/2q5/1Q2P3/P1P2P2/3B2PP/R3KBNR b KQ - 4 12");

        search.setSearchParameter(SearchParameter.MAX_DEPTH, 7);
        searchResult = search.search(game);

        System.out.println(searchResult.getBestEvaluation());
    }

    @Test
    @Disabled
    public void testSearch_10() throws IOException {
        String lines = "[Event \"Rated Rapid game\"]\n" +
                "[Site \"https://lichess.org/cjatYH5c\"]\n" +
                "[Date \"2024.02.20\"]\n" +
                "[White \"ChessChildren\"]\n" +
                "[Black \"chesstango_bot\"]\n" +
                "[Result \"1-0\"]\n" +
                "[UTCDate \"2024.02.20\"]\n" +
                "[UTCTime \"21:23:34\"]\n" +
                "[WhiteElo \"1765\"]\n" +
                "[BlackElo \"1863\"]\n" +
                "[WhiteRatingDiff \"+7\"]\n" +
                "[BlackRatingDiff \"-7\"]\n" +
                "[WhiteTitle \"BOT\"]\n" +
                "[BlackTitle \"BOT\"]\n" +
                "[Variant \"Standard\"]\n" +
                "[TimeControl \"600+0\"]\n" +
                "[ECO \"E25\"]\n" +
                "[Opening \"Nimzo-Indian Defense: Sämisch Variation, Keres Variation\"]\n" +
                "[Termination \"Time forfeit\"]\n" +
                "[Annotator \"lichess.org\"]\n" +
                "\n" +
                "1. d4 Nf6 2. c4 e6 3. Nc3 Bb4 4. a3 Bxc3+ 5. bxc3 c5 6. f3 d5 7. cxd5 Nxd5 8. dxc5 { E25 Nimzo-Indian Defense: Sämisch Variation, Keres Variation } Qa5 9. Bd2 Qxc5 10. e4 Nf6 11. Qb3 O-O 12. Qb4 *";
        Reader reader = new StringReader(lines);

        BufferedReader bufferReader = new BufferedReader(reader);

        PGNStringDecoder decoder = new PGNStringDecoder();

        PGN pgn = decoder.decodePGN(bufferReader);

        Game game = pgn.toGame();

        search.setSearchParameter(SearchParameter.MAX_DEPTH, 7);
        searchResult = search.search(game);

        System.out.println(searchResult.getBestEvaluation());
    }

    @Test
    @Disabled
    public void testSearch_11() {
        Game game = FENDecoder.loadGame("1r2r1k1/pp3p1p/3pb1pB/4b3/P2pQ3/1PqP2P1/2P2RBP/3R2K1 b - - 2 23");

        search.setSearchParameter(SearchParameter.MAX_DEPTH, 4);
        searchResult = search.search(game);

        System.out.println(searchResult.getBestEvaluation());
    }



    @Test
    @Disabled
    public void testSearch_12() {
        Game game = FENDecoder.loadGame("1RRbr3/3pkp2/2b1p1p1/2P1P3/5PP1/P6P/1KP5/5B2 w - - 17 49");

        search.setSearchParameter(SearchParameter.MAX_DEPTH, 7);
        searchResult = search.search(game);

        System.out.println(searchResult.getBestEvaluation());
    }
    
}
