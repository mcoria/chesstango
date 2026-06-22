package net.chesstango.reports;

import net.chesstango.engine.*;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.pgn.PGN;
import net.chesstango.reports.engine.SearchManagerReport;
import net.chesstango.reports.engine.SearchManagerSummaryReport;
import net.chesstango.reports.search.DetailsReport;
import net.chesstango.reports.search.SummaryReport;
import net.chesstango.search.SearchResult;
import net.chesstango.search.builders.AlphaBetaBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author Mauricio Coria
 */
@Disabled
public class ReportGamesIntegrationTest {
    private static final String POLYGLOT_FILE = "C:\\java\\projects\\chess\\chess-utils\\books\\openings\\polyglot-collection\\komodo.bin";
    private static final String SYZYGY_PATH = "D:\\k8s_shared\\syzygy\\3-4-5;D:\\k8s_shared\\syzygy\\6-DTZ;D:\\k8s_shared\\syzygy\\6-WDL";

    private List<SearchResponse> searchResponses;

    private SearchManagerSummaryReport searchManagerSummaryReport;
    private SearchManagerReport searchManagerReport;

    private SummaryReport summaryReport;
    private DetailsReport detailsReport;

    @BeforeEach
    public void setup() {
        searchManagerSummaryReport = new SearchManagerSummaryReport();
        searchManagerReport = new SearchManagerReport();

        summaryReport = new SummaryReport();
        detailsReport = new DetailsReport();
    }

    @AfterEach
    public void tearDown() {
        searchManagerSummaryReport
                .addSearchResponses("TangoGame01", searchResponses)
                .printReport(System.out);

        searchManagerReport
                .withMoveResults(searchResponses)
                .printReport(System.out);

        List<SearchResult> searchResults = searchResponses.stream()
                .filter(SearchByTreeResult.class::isInstance)
                .map(SearchByTreeResult.class::cast)
                .map(SearchByTreeResult::searchResult)
                .toList();

        summaryReport
                .addSearchesByTreeSummaryModel("TangoGame01", searchResults)
                .withBoardStatistics()
                .withNodesVisitedStatistics()
                .withNodesTypesStatistics()
                .withCutoffStatistics()
                .withEvaluationStatistics()
                .withEvaluationCacheStatistics()
                .withTranspositionStatistics()
                .withPrincipalVariationStatistics()
                .printReport(System.out);

        detailsReport
                .setReportTitle("TangoGame01")
                .withMoveResults(searchResults)
                .withBoardReport()
                .withNodesDepthStatistics()
                .withNodesTypesStatistics()
                .withCutoffStatistics()
                .withEvaluationReport()
                .withEvaluationCacheReport()
                .withTranspositionReport()
                .withEvaluationIterationReport()
                .withPrincipalVariationReport()
                .withPrincipalVariationIterationReport()
                //.withEbf()
                .printReport(System.out);
    }

    @Test
    public void testPlay() {
        Config config = Config.create()
                /*
               .setSearch(AlphaBetaBuilder
                       .createDefaultBuilderInstance()
                       .withGameEvaluator(Evaluator.createInstance())
                       .withStatistics()
                       .build()
               )
               */
              .setSearch(new AlphaBetaBuilder()
                      .withGameEvaluator(Evaluator.createInstance())
                      .withGameEvaluatorCache()

                      .withQuiescence()

                      //.withTranspositionTable()
                      //.withTranspositionMoveSorter()

                      .withKillerMoveSorter()
                      .withRecaptureSorter()
                      .withMvvLvaSorter()

                      //.withAspirationWindows()

                      //.withIterativeDeepening()

                      //.withStopProcessingCatch()

                      .withStatistics()

                      .build()
              )
                .setAsyncSearch(false)
                //.setPolyglotFile(POLYGLOT_FILE)
                //.setSyzygyPath(SYZYGY_PATH)
                //.setHashSizeMB(1)
                ;

        PGN pgn = PGN.from("""
                [Event "188c6255-5216-463a-bfc6-300b98a14ff9"]
                [Site "LAPTOP-PTVVKHNB"]
                [Date "2026.05.03"]
                [Round "?"]
                [White "Tango"]
                [Black "Oponent"]
                [Result "0-1"]
                [Termination "normal"]
                [SearchRange "10:66"]
                
                1. Nf3 Nf6 2. d4 d5 3. e3 Bg4 4. Be2 Bxf3 5. Bxf3 e6
                6. Nc3 Nc6 7. O-O Rc8 8. h3 Bd6 9. e4 dxe4 10. Bxe4 Nxe4
                11. Nxe4 Nxd4 12. Bg5 Be7 13. Bxe7 Kxe7 14. c3 Nf5 15. Qe2 c5
                16. Rad1 Qc7 17. Qh5 h6 18. Ng3 Nxg3 19. fxg3 Rhf8 20. Qg4 g6
                21. Qh4+ g5 22. Qg4 c4 23. Rf2 f5 24. Qf3 f4 25. g4 Rcd8
                26. Rd4 b5 27. Qe4 Kf6 28. Re2 Rde8 29. Red2 Qc5 30. Qh7 Rh8
                31. Qe4 h5 32. Rd1 hxg4 33. hxg4 Rh4 34. Qf3 Rc8 35. Kf1 Rh1+
                36. Ke2 Rch8 37. Qe4 R8h2 38. Rxh1 Rxh1 39. Rd8 Qg1 40. Qd4+ Qxd4
                41. Rxd4 Rb1 42. Rd2 a5 43. Kf3 a4 44. Ke2 Ra1 45. a3 Rg1
                46. Kf2 Rb1 47. Kf3 Ke7 48. Ke4 Rh1 49. Kf3 Rc1 50. Ke2 Rb1
                51. Kf2 Kf7 52. Re2 Kf6 53. Rd2 Kg6 54. Re2 Kf7 55. Rd2 e5
                56. Re2 Ke6 57. Kf3 Rf1+ 58. Ke4 Rd1 59. Kf3 Rd3+ 60. Kf2 Kd5
                61. Re1 e4 62. Ke2 e3 63. Kf3 Rd2 64. Re2 Rxe2 65. Kxe2 Ke4
                66. Kd1 f3 67. gxf3+ Kxf3 68. Ke1 e2 69. Kd2 Kf2 70. b4 cxb3
                71. Kc1 e1=Q+ 72. Kb2 Qd2+ 73. Kb1 Qc2+ 74. Ka1 Qc1# 0-1
                """);


        List<String> coordinateMoves = pgn.getCoordinateMoves();

        boolean whiteSearch = false;

        try (Tango tango = Tango.open(config)) {
            FEN fen = pgn.getFen() == null ? FEN.START_POSITION : pgn.getFen();
            Session session = tango.newSession(fen);

            for (int i = 0; i < coordinateMoves.size(); i++) {
                if (!whiteSearch && i % 2 == 1) {
                    List<String> currentMoves = coordinateMoves.stream().limit(i).toList();
                    session.setMoves(currentMoves);
                    session.goDepth(2);
                }
            }

            searchResponses = session.getSearchResults();
        }
    }
}
