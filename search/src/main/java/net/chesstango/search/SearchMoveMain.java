package net.chesstango.search;

import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.EPDEntry;
import net.chesstango.board.representations.EPDReader;
import net.chesstango.board.representations.SANEncoder;
import net.chesstango.evaluation.DefaultEvaluator;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.reports.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Esta clase esta destinada a resolver test-positions
 * <p>
 * https://www.chessprogramming.org/Test-Positions
 *
 * @author Mauricio Coria
 */
public class SearchMoveMain {

    //private static final String SEARCH_SESSION_ID = "2023-08-20-18-37";
    //private static final String SEARCH_SESSION_ID = "test";

    private static final String SEARCH_SESSION_ID = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm"));
    private static final int SEARCH_THREADS = 3;

    /**
     * Parametros
     * 1. Depth
     * 2. Directorio donde se encuentran los archivos de posicion
     * 3. Filtro de archivos
     * <p>
     * Ejemplo:
     * 5 C:\java\projects\chess\chess-utils\testing\positions\database "(mate-w[12].epd|mate-b[12].epd|Bratko-Kopec.epd|wac-2018.epd|STS*.epd)"
     *
     * @param args
     */
    public static void main(String[] args) {
        int depth = Integer.parseInt(args[0]);

        String directory = args[1];

        String filePattern = args[2];

        System.out.printf("depth={%d}, directory={%s}; filePattern={%s}\n", depth, directory, filePattern);

        SearchMoveMain suite = new SearchMoveMain(depth);

        getFiles(directory, filePattern).forEach(suite::execute);
    }

    private static List<Path> getFiles(String directory, String filePattern) {
        String finalPattern = filePattern.replace(".", "\\.").replace("*", ".*");
        Predicate<String> matchPredicate = Pattern.compile(finalPattern).asMatchPredicate();
        try (Stream<Path> stream = Files.list(Paths.get(directory))) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .filter(file -> matchPredicate.test(file.getFileName().toString()))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static final SANEncoder sanEncoder = new SANEncoder();
    protected static final EPDReader reader = new EPDReader();

    protected final int depth;

    public SearchMoveMain(int depth) {
        this.depth = depth;
    }

    public boolean test(EPDEntry EPDEntry) {
        return run(EPDEntry).bestMoveFound();
    }

    public EPDSearchResult run(EPDEntry epdEntry) {
        SearchMove searchMove = buildSearchMove();

        Instant start = Instant.now();

        SearchMoveResult searchResult = searchMove.search(epdEntry.game, depth);

        long duration = Duration.between(start, Instant.now()).toMillis();

        searchResult.setEpdID(epdEntry.id);

        Move bestMove = searchResult.getBestMove();

        String bestMoveFoundStr = sanEncoder.encode(bestMove, epdEntry.game.getPossibleMoves());

        boolean bestMoveFound = epdEntry.bestMoves.contains(bestMove);

        searchMove.reset();

        return new EPDSearchResult(epdEntry, bestMoveFoundStr, bestMoveFound, duration, searchResult);
    }

    private void execute(Path suitePath) {
        List<EPDEntry> edpEntries = reader.readEdpFile(suitePath);

        run(suitePath, edpEntries);

        System.gc();
    }

    private void run(Path suitePath, List<EPDEntry> edpEntries) {

        List<EPDSearchResult> epdSearchResults = run(edpEntries);

        SearchesReportModel searchesReportModel = SearchesReportModel.collectStatics("", epdSearchResults.stream().map(EPDSearchResult::searchResult).toList());

        EpdSearchReportModel epdSearchReportModel = EpdSearchReportModel.collectStatics(epdSearchResults);

        //String suiteName = suitePath.getFileName().toString();

        //Path sessionDirectory = createSessionDirectory(suitePath);

        //saveReport(sessionDirectory, suiteName, epdSearchReportModel, searchesReportModel);

        //saveSearchSummary(sessionDirectory, suiteName, epdSearchReportModel, searchesReportModel);

        printReport(System.out, epdSearchReportModel, searchesReportModel);
    }

    private void saveSearchSummary(Path sessionDirectory, String suiteName, EpdSearchReportModel epdSearchReportModel, SearchesReportModel searchesReportModel) {
        SearchSummaryModel searchSummaryModel = SearchSummaryModel.collectStatics(SEARCH_SESSION_ID, epdSearchReportModel, searchesReportModel);

        Path searchSummaryPath = sessionDirectory.resolve(String.format("%s.json", suiteName));

        try (PrintStream out = new PrintStream(new FileOutputStream(searchSummaryPath.toFile()), true)) {

            new SearchSummarySaver()
                    .withSearchSummaryModel(searchSummaryModel)
                    .print(out);

            out.flush();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private void saveReport(Path sessionDirectory, String suiteName, EpdSearchReportModel epdSearchReportModel, SearchesReportModel searchesReportModel) {
        Path suitePathReport = sessionDirectory.resolve(String.format("%s-report.txt", suiteName));

        try (PrintStream out = new PrintStream(new FileOutputStream(suitePathReport.toFile()), true)) {

            printReport(out, epdSearchReportModel, searchesReportModel);

            out.flush();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private void printReport(PrintStream output, EpdSearchReportModel epdSearchReportModel, SearchesReportModel searchesReportModel) {
        new EpdSearchReport()
                .setReportModel(epdSearchReportModel)
                .printReport(output);

        new SearchesReport()
                .setReportModel(searchesReportModel)
                //.withCutoffStatics()
                .withNodesVisitedStatics()
                //.withEvaluationsStatics()
                //.withPrincipalVariation()
                //.withExportEvaluations()
                .printReport(output);
    }

    private List<EPDSearchResult> run(List<EPDEntry> edpEntries) {
        ExecutorService executorService = Executors.newFixedThreadPool(SEARCH_THREADS);

        List<EPDSearchResult> epdSearchResults = Collections.synchronizedList(new LinkedList<>());
        for (EPDEntry epdEntry : edpEntries) {
            executorService.submit(() -> {
                try {
                    EPDSearchResult epdSearchResult = run(epdEntry);
                    if (epdSearchResult.bestMoveFound()) {
                        System.out.printf("Success %s\n", epdEntry.fen);
                    } else {
                        String failedTest = String.format("Fail [%s] - best move found %s",
                                epdEntry.text,
                                epdSearchResult.bestMoveFoundStr()
                        );
                        System.out.println(failedTest);
                    }
                    epdSearchResults.add(epdSearchResult);
                } catch (RuntimeException e) {
                    e.printStackTrace(System.err);
                }
            });
        }

        executorService.shutdown();
        try {
            while (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
            }
        } catch (InterruptedException e) {
            System.out.println("Stopping executorService....");
            executorService.shutdownNow();
        }

        if (epdSearchResults.isEmpty()) {
            throw new RuntimeException("No edp entry was processed");
        }

        return epdSearchResults;
    }

    private Path createSessionDirectory(Path suitePath) {
        Path parentDirectory = suitePath.getParent();

        Path sessionDirectory = parentDirectory.resolve(String.format("depth-%d-%s", depth, SEARCH_SESSION_ID));

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

                //.withTranspositionTable()
                //.withQTranspositionTable()
                //.withTranspositionTableReuse()

                //.withTranspositionMoveSorter()
                //.withQTranspositionMoveSorter()

                //.withStopProcessingCatch()

                .withIterativeDeepening()

                .withtStatistics()
                //.withStaticsTrackEvaluations() //Consume demasiada memoria

                .build();
    }

}
