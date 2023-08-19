package net.chesstango.search;

import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.EPDReader;
import net.chesstango.board.representations.SANEncoder;
import net.chesstango.evaluation.DefaultEvaluator;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.reports.SearchesReport;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase esta destinada a resolver test-positions
 * <p>
 * https://www.chessprogramming.org/Test-Positions
 *
 * @author Mauricio Coria
 */
public class BestMoveSearchSuite {

    private static final int DEFAULT_MAX_DEPTH = 7;

    public static void main(String[] args) {
        //execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\40H-EPD-databases-2022-10-04\\mate-all.epd");
        //execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\40H-EPD-databases-2022-10-04\\failed-2023-04-30.epd");

        //execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\Bratko-Kopec\\Bratko-Kopec.epd");
        //execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\Bratko-Kopec\\Bratko-KopecCopy.epd");
        //execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\wac\\wac-2018.epd");

        execute("C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\SuiteForReportDevelop\\Sample.epd");

        /*
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS1.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS2.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS3.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS4.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS5.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS6.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS7.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS8.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS9.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS10.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS11.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS12.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS13.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS14.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\STS\\STS15.epd");
         */
    }

    protected static void execute(String suiteFile) {
        EPDReader reader = new EPDReader();
        List<EPDReader.EDPEntry> edpEntries = reader.readEdpFile(suiteFile);
        BestMoveSearchSuite suite = new BestMoveSearchSuite(DEFAULT_MAX_DEPTH);
        suite.run(suiteFile, edpEntries);
        System.gc();
    }

    protected final int depth;
    protected final List<SearchMoveResult> searchMoveResults;

    public BestMoveSearchSuite(int depth) {
        this.depth = depth;
        this.searchMoveResults = new ArrayList<>();
    }

    protected void run(String suiteFile, List<EPDReader.EDPEntry> edpEntries) {
        List<String> failedSuites = new ArrayList<>();


        Instant start = Instant.now();
        for (EPDReader.EDPEntry edpEntry : edpEntries) {
            if (run(edpEntry)) {
                System.out.printf("Success %s\n", edpEntry.fen);
            } else {
                String failedTest = String.format("Fail   [%s] - best move found %s",
                        edpEntry.text,
                        edpEntry.bestMoveFoundStr
                );
                System.out.println(failedTest);
                failedSuites.add(failedTest);
            }
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
        String nowStr = now.format(formatter);

        try (PrintStream out = new PrintStream(new FileOutputStream(String.format("%s-%s.txt", suiteFile, nowStr)), true)) {

            out.println("Suite summary " + suiteFile);
            if (failedSuites.isEmpty()) {
                out.println("\tall tests executed successfully !!!!");
            } else {
                for (String failedTest : failedSuites) {
                    out.printf("\t%s\n", failedTest);
                }
            }
            out.printf("Success rate: %d%% \n", (100 * (edpEntries.size() - failedSuites.size())) / edpEntries.size());
            out.printf("Time taken: " + Duration.between(start, Instant.now()).toMillis() + " ms \n");

            new SearchesReport()
                    //.withCutoffStatics()
                    .withNodesVisitedStatics()
                    //.withExportEvaluations()
                    .withMoveResults(searchMoveResults)
                    .printReport(out)
                    .printReport(System.out);

            out.flush();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    protected boolean run(EPDReader.EDPEntry edpEntry) {
        SearchMove searchMove = buildSearchMove();

        SearchMoveResult searchResult = searchMove.search(edpEntry.game, depth);

        Move bestMove = searchResult.getBestMove();

        SANEncoder sanEncoder = new SANEncoder();

        edpEntry.bestMoveFoundStr = sanEncoder.encode(bestMove, edpEntry.game.getPossibleMoves());

        boolean result = edpEntry.bestMoves.contains(bestMove);

        searchMoveResults.add(searchResult);

        return result;
    }

    private SearchMove buildSearchMove() {
        return new AlphaBetaBuilder()
                .withGameEvaluator(new DefaultEvaluator())

                .withQuiescence()

                .withTranspositionTable()
                .withQTranspositionTable()
                //.withTranspositionTableReuse()

                .withTranspositionMoveSorter()
                .withQTranspositionMoveSorter()

                .withStopProcessingCatch()

                .withIterativeDeepening()

                .withStatics()

                .build();
    }

}
