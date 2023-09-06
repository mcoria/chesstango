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
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
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
     * 5 C:\java\projects\chess\chess-utils\testing\positions\database "(mate-[wb][123].epd|Bratko-Kopec.epd|wac-2018.epd|STS*.epd)"
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

        return run(searchMove, epdEntry);
    }

    public EPDSearchResult run(SearchMove searchMove, EPDEntry epdEntry) {

        Instant start = Instant.now();

        SearchMoveResult searchResult = searchMove.search(epdEntry.game, depth);

        long duration = Duration.between(start, Instant.now()).toMillis();

        searchResult.setEpdID(epdEntry.id);

        Move bestMove = searchResult.getBestMove();

        String bestMoveFoundStr = sanEncoder.encode(bestMove, epdEntry.game.getPossibleMoves());

        boolean bestMoveFound = epdEntry.bestMoves.contains(bestMove);

        return new EPDSearchResult(epdEntry, bestMoveFoundStr, bestMoveFound, duration, searchResult);
    }

    private void execute(Path suitePath) {
        List<EPDEntry> edpEntries = reader.readEdpFile(suitePath);

        run(suitePath, edpEntries);

        System.gc();
    }

    private void run(Path suitePath, List<EPDEntry> edpEntries) {

        List<EPDSearchResult> epdSearchResults = run(edpEntries);

        EpdSearchReportModel epdSearchReportModel = EpdSearchReportModel.collectStatics(epdSearchResults);

        NodesReportModel nodesReportModel = NodesReportModel.collectStatics("", epdSearchResults.stream().map(EPDSearchResult::searchResult).toList());

        EvaluationReportModel evaluationReportModel = EvaluationReportModel.collectStatics("", epdSearchResults.stream().map(EPDSearchResult::searchResult).toList());

        String suiteName = suitePath.getFileName().toString();

        Path sessionDirectory = createSessionDirectory(suitePath);

        saveReport(sessionDirectory, suiteName, epdSearchReportModel, nodesReportModel);

        saveSearchSummary(sessionDirectory, suiteName, epdSearchReportModel, nodesReportModel, evaluationReportModel);

        //printReport(System.out, epdSearchReportModel, searchesReportModel);
    }

    private void saveSearchSummary(Path sessionDirectory, String suiteName, EpdSearchReportModel epdSearchReportModel, NodesReportModel nodesReportModel, EvaluationReportModel evaluationReportModel) {
        SummaryModel summaryModel = SummaryModel.collectStatics(SEARCH_SESSION_ID, epdSearchReportModel, nodesReportModel, evaluationReportModel);

        Path searchSummaryPath = sessionDirectory.resolve(String.format("%s.json", suiteName));

        try (PrintStream out = new PrintStream(new FileOutputStream(searchSummaryPath.toFile()), true)) {

            new SummarySaver()
                    .withSearchSummaryModel(summaryModel)
                    .print(out);

            out.flush();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private void saveReport(Path sessionDirectory, String suiteName, EpdSearchReportModel epdSearchReportModel, NodesReportModel nodesReportModel) {
        Path suitePathReport = sessionDirectory.resolve(String.format("%s-report.txt", suiteName));

        try (PrintStream out = new PrintStream(new FileOutputStream(suitePathReport.toFile()), true)) {

            printReport(out, epdSearchReportModel, nodesReportModel);

            out.flush();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private void printReport(PrintStream output, EpdSearchReportModel epdSearchReportModel, NodesReportModel nodesReportModel) {
        new EpdSearchReport()
                .setReportModel(epdSearchReportModel)
                .printReport(output);

        new NodesReport()
                .setReportModel(nodesReportModel)
                .withCutoffStatics()
                .withNodesVisitedStatics()
                //.withEvaluationsStatics()
                //.withPrincipalVariation()
                //.withExportEvaluations()
                .printReport(output);
    }

    private List<EPDSearchResult> run(List<EPDEntry> edpEntries) {
        ExecutorService executorService = Executors.newFixedThreadPool(SEARCH_THREADS);

        BlockingQueue<SearchMove> blockingQueue = new LinkedBlockingDeque<>(SEARCH_THREADS);

        for (int i = 0; i < SEARCH_THREADS; i++) {
            blockingQueue.add(buildSearchMove());
        }

        List<Future<EPDSearchResult>> futures = new LinkedList<>();
        for (EPDEntry epdEntry : edpEntries) {
            Future<EPDSearchResult> future = executorService.submit(new Callable<>() {
                @Override
                public EPDSearchResult call() throws Exception {
                    SearchMove searchMove = null;
                    try {
                        searchMove = blockingQueue.take();
                        EPDSearchResult epdSearchResult = run(searchMove, epdEntry);
                        if (epdSearchResult.bestMoveFound()) {
                            System.out.printf("Success %s\n", epdEntry.fen);
                        } else {
                            String failedTest = String.format("Fail [%s] - best move found %s",
                                    epdEntry.text,
                                    epdSearchResult.bestMoveFoundStr()
                            );
                            System.out.println(failedTest);
                        }
                        return epdSearchResult;
                    } catch (RuntimeException e) {
                        e.printStackTrace(System.err);
                        throw e;
                    } finally {
                        assert searchMove != null;
                        blockingQueue.put(searchMove);
                    }
                }
            });

            futures.add(future);
        }

        executorService.shutdown();
        try {
            while (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
            }
        } catch (InterruptedException e) {
            System.out.println("Stopping executorService....");
            executorService.shutdownNow();
        }

        List<EPDSearchResult> epdSearchResults = new LinkedList<>();
        futures.forEach(future -> {
            try {
                EPDSearchResult epdSearchResult = future.get();
                epdSearchResults.add(epdSearchResult);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });


        if (epdSearchResults.isEmpty()) {
            throw new RuntimeException("No edp entry was processed");
        }

        epdSearchResults.sort(Comparator.comparing(o -> o.epdEntry().id));


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

                .withTranspositionTable()
                .withQTranspositionTable()
                .withTranspositionTableReuse()

                .withTranspositionMoveSorter()
                .withQTranspositionMoveSorter()

                //.withStopProcessingCatch()

                .withIterativeDeepening()

                .withStatistics()
                //.withStatisticsTrackEvaluations() //Consume demasiada memoria

                .build();
    }

}
