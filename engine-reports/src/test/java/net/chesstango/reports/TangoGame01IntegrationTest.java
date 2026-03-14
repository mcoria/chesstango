package net.chesstango.reports;

import net.chesstango.engine.*;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.gardel.fen.FEN;
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

import java.util.Arrays;
import java.util.List;

/**
 * @author Mauricio Coria
 */
@Disabled
public class TangoGame01IntegrationTest {
    private static final String POLYGLOT_FILE = "C:\\java\\projects\\chess\\chess-utils\\books\\openings\\polyglot-collection\\komodo.bin";
    private static final String SYZYGY_PATH = "D:\\k8s_shared\\syzygy\\3-4-5;D:\\k8s_shared\\syzygy\\6-DTZ;D:\\k8s_shared\\syzygy\\6-WDL";


    private List<SearchResponse> searchResponseList;

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
                .addSearchResponses("TangoGame01", searchResponseList)
                .printReport(System.out);

        searchManagerReport
                .withMoveResults(searchResponseList)
                .printReport(System.out);

        List<SearchResult> searchResults = searchResponseList.stream()
                .filter(SearchByTreeResult.class::isInstance)
                .map(SearchByTreeResult.class::cast)
                .map(SearchByTreeResult::searchResult)
                .toList();

        summaryReport
                .addSearchesByTreeSummaryModel("TangoGame01", searchResults)
                .withBoardStatistics()
                //.withNodesVisitedStatistics()
                //.withCutoffStatistics()
                //.withEvaluationStatistics()
                //.withTranspositionStatistics()
                .printReport(System.out);

        detailsReport
                .setReportTitle("TangoGame01")
                .withBoardReport()
                //.withPrincipalVariationReport()
                //.withNodesVisitedStatistics()
                //.withEvaluationReport()
                //.withTranspositionReport()
                //.withCutoffStatistics()
                .withMoveResults(searchResults)
                .printReport(System.out);
    }

    @Test
    public void testPlay() {
        Config config = new Config()
                .setSearch(AlphaBetaBuilder
                        .createDefaultBuilderInstance()
                        .withGameEvaluator(Evaluator.createInstance())
                        .withStatistics()
                        .build()
                )
                .setSyncSearch(true)
                .setPolyglotFile(POLYGLOT_FILE)
                .setSyzygyPath(SYZYGY_PATH);

        String moves = "d2d4 g8f6 g1f3 d7d5 c2c4 e7e6 b1c3 c7c6 c1g5 f8e7 e2e3 b8d7 d1c2 e8g8 a1c1 f8e8 f1d3 h7h6 g5h4 d5c4 d3c4 b7b5 c4e2 c8b7 e1g1 a7a6 f1d1 d8c7 h4g3 c7b6 a2a4 b5b4 c3e4 f6e4 c2e4 c6c5 a4a5 b7e4 a5b6 d7b6 d4c5 b6a4 c5c6 a4b2 d1d4 e4f3 e2f3 e7f6 d4d7 a8d8 d7d8 f6d8 f3e2 b4b3 c1c3 b2c4 e2c4 b3b2 c3b3 a6a5 b3b2 d8f6 b2b1 f6d8 b1b8 g8h7 c6c7 d8c7 b8e8 c7g3 h2g3 g7g5 e8a8 h7g6 a8a5 g6f6 a5a7 h6h5 c4b5 h5h4 g3g4 f6g6 b5e8 g6f6 e8f7 e6e5 f7d5 f6g6 a7f7 h4h3 g2h3 g6h6 f7f5 h6g6 f5e5 g6f6 f2f4 g5f4 e3f4 f6g6 e5e7 g6f6 e7g7 f6g7 h3h4 g7f8 f4f5 f8e7 g4g5 e7d6 h4h5 d6e5 f5f6 e5d4 g5g6 d4e5 h5h6 e5f4 f6f7 f4f5 g6g7 f5e5 h6h7 e5d4 f7f8q d4c3 g7g8q c3d3 h7h8q d3e3 f8f1 e3d2 h8c3 d2c3 g8g3 c3b2";
        String[] movesArray = moves.split(" ");

        //Game theGame = Game.from(FEN.START_POSITION, List.of(movesArray));
        //System.out.println(theGame.encode().toString());

        try (Tango tango = Tango.open(config)) {
            Session session = tango.newSession();
            session.setFen(FEN.START_POSITION);

            session.setMoves(List.of());
            //session.goTime(1000);
            session.goDepth(3);

            for (int i = 0; i < movesArray.length; i += 2) {
                List<String> currentMoves = Arrays.stream(movesArray).limit(i + 2).toList();
                //System.out.println(currentMoves);
                session.setMoves(currentMoves);
                //session.goTime(1000);
                session.goDepth(3);
            }
            searchResponseList = session.getSearchResults();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
