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
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;

/**
 * Esta clase esta destinada a resolver test-positions
 * <p>
 * https://www.chessprogramming.org/Test-Positions
 *
 * @author Mauricio Coria
 */
public class BestMoveSearchSuite {

    private static final int DEFAULT_MAX_DEPTH = 7;

    private static final String SEARCH_SESSION_ID = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm"));

    public static void main(String[] args) {

        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\40H-EPD-databases-2022-10-04\\mate-w1.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\40H-EPD-databases-2022-10-04\\mate-b1.epd");

        /*
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\40H-EPD-databases-2022-10-04\\mate-w2.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\40H-EPD-databases-2022-10-04\\mate-b2.epd");

        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\40H-EPD-databases-2022-10-04\\mate-w3.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\40H-EPD-databases-2022-10-04\\mate-b3.epd");

        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\40H-EPD-databases-2022-10-04\\mate-w4.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\40H-EPD-databases-2022-10-04\\mate-b4.epd");

        //execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\40H-EPD-databases-2022-10-04\\mate-w5.epd");
        //execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\40H-EPD-databases-2022-10-04\\mate-b5.epd");

        //execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\40H-EPD-databases-2022-10-04\\mate-w6.epd");
        //execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\40H-EPD-databases-2022-10-04\\mate-b6.epd");

        //execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\40H-EPD-databases-2022-10-04\\mate-w7.epd");
        //execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\40H-EPD-databases-2022-10-04\\mate-b7.epd");

        //execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\40H-EPD-databases-2022-10-04\\mate-w8.epd");
        //execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\40H-EPD-databases-2022-10-04\\mate-b8.epd");


        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\Bratko-Kopec\\Bratko-Kopec.epd");
        execute("C:\\Java\\projects\\chess\\chess-utils\\testing\\positions\\wac\\wac-2018.epd");

        //execute("C:\\java\\projects\\chess\\chess-utils\\testing\\positions\\SuiteForReportDevelop\\Sample.epd");


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
        Path suitePath = Paths.get(suiteFile);

        if(!Files.exists(suitePath)){
            System.err.printf("file not found: %s\n", suiteFile);
            return;
        }

        EPDReader reader = new EPDReader();

        List<EPDReader.EDPEntry> edpEntries = reader.readEdpFile(suiteFile);

        BestMoveSearchSuite suite = new BestMoveSearchSuite(DEFAULT_MAX_DEPTH);

        suite.run(suitePath, edpEntries);

        System.gc();
    }

    protected final int depth;
    protected final List<SearchMoveResult> searchMoveResults;

    public BestMoveSearchSuite(int depth) {
        this.depth = depth;
        this.searchMoveResults = new ArrayList<>();
    }

    protected void run(Path suitePath, List<EPDReader.EDPEntry> edpEntries) {
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

        String suiteName = suitePath.getFileName().toString();

        Path sessionDirectory = createSessionDirectory(suitePath);

        Path suitePathReport = sessionDirectory.resolve( String.format("%s-report.txt", suiteName));

        try (PrintStream out = new PrintStream(new FileOutputStream(suitePathReport.toFile()), true)) {

            out.println("Suite summary " + suiteName);
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

    private static Path createSessionDirectory(Path suitePath) {
        Path parentDirectory = suitePath.getParent();

        Path sessionDirectory = parentDirectory.resolve(SEARCH_SESSION_ID);

        try {
            Files.createDirectory(sessionDirectory);
        } catch (FileAlreadyExistsException e) {
            System.err.printf("Session directory already exists %s\n", sessionDirectory.getFileName().toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return sessionDirectory;
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
