package net.chesstango.tools;

import net.chesstango.board.representations.epd.EPD;
import net.chesstango.board.representations.epd.EPDDecoder;
import net.chesstango.engine.Tango;
import net.chesstango.evaluation.DefaultEvaluator;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.tools.search.EpdSearch;
import net.chesstango.tools.search.EpdSearchResult;
import net.chesstango.tools.search.EpdSearchResultBuildWithBestMove;
import net.chesstango.tools.search.reports.epd.EpdSearchReport;
import net.chesstango.tools.search.reports.epd.EpdSearchReportModel;
import net.chesstango.tools.search.reports.evaluation.EvaluationReport;
import net.chesstango.tools.search.reports.evaluation.EvaluationReportModel;
import net.chesstango.tools.search.reports.nodes.NodesReport;
import net.chesstango.tools.search.reports.nodes.NodesReportModel;
import net.chesstango.tools.search.reports.pv.PrincipalVariationReport;
import net.chesstango.tools.search.reports.pv.PrincipalVariationReportModel;
import net.chesstango.tools.search.reports.summary.SummaryModel;
import net.chesstango.tools.search.reports.summary.SummarySaver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
public class EpdSearchMain {

    private static final String SEARCH_SESSION_ID = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm"));

    /**
     * Parametros
     * 1. Depth
     * 2. TimeOut in milliseconds
     * 3. Directorio donde se encuentran los archivos de posicion
     * 4. Filtro de archivos
     * <p>
     * Ejemplo:
     * 4 500 C:\java\projects\chess\chess-utils\testing\positions\database "(mate-[wb][123].epd|Bratko-Kopec.epd|wac-2018.epd|STS*.epd)"
     *
     * @param args
     */
    public static void main(String[] args) {

        int depth = Integer.parseInt(args[0]);

        int timeOut = Integer.parseInt(args[1]);

        String directory = args[2];

        String filePattern = args[3];

        System.out.printf("depth={%d}; timeOut={%d}; directory={%s}; filePattern={%s}\n", depth, timeOut, directory, filePattern);

        EpdSearchMain suite = new EpdSearchMain(depth, timeOut);

        getEpdFiles(directory, filePattern).forEach(suite::execute);
    }

    private final EpdSearch epdSearch;
    private final int depth;

    public EpdSearchMain(int depth, int timeOut) {
        this.depth = depth;
        this.epdSearch = new EpdSearch()
                .setSearchSupplier(() -> AlphaBetaBuilder
                        // Hasta v0.0.27 se utilizó EvaluatorSEandImp02 (ahora EvaluatorImp04) como evaluador
                        .createDefaultBuilderInstance(new DefaultEvaluator())
                        .withStatistics()
                        .build())
                .setDepth(depth)
                .setEpdSearchResultBuilder(new EpdSearchResultBuildWithBestMove());

        if (timeOut > 0) {
            this.epdSearch.setTimeOut(timeOut);
        }

    }

    public void execute(Path suitePath) {
        EPDDecoder reader = new EPDDecoder();

        Stream<EPD> epdEntryStream = reader.readEdpFile(suitePath);

        List<EpdSearchResult> epdSearchResults = epdSearch.run(epdEntryStream);

        report(suitePath, epdSearchResults);

        //System.gc();
    }

    private void report(Path suitePath, List<EpdSearchResult> epdSearchResults) {
        String suiteName = suitePath.getFileName().toString();

        EpdSearchReportModel epdSearchReportModel = EpdSearchReportModel.collectStatistics(suiteName, epdSearchResults);
        NodesReportModel nodesReportModel = NodesReportModel.collectStatistics(suiteName, epdSearchResults.stream().map(EpdSearchResult::getSearchResult).toList());
        EvaluationReportModel evaluationReportModel = EvaluationReportModel.collectStatistics(suiteName, epdSearchResults.stream().map(EpdSearchResult::getSearchResult).toList());
        PrincipalVariationReportModel principalVariationReportModel = PrincipalVariationReportModel.collectStatics(suiteName, epdSearchResults.stream().map(EpdSearchResult::getSearchResult).toList());
        SummaryModel summaryModel = SummaryModel.collectStatics(SEARCH_SESSION_ID, epdSearchResults, epdSearchReportModel, nodesReportModel, evaluationReportModel, principalVariationReportModel);

        //printReports(System.out, epdSearchReportModel, nodesReportModel, evaluationReportModel);


        Path sessionDirectory = createSessionDirectory(suitePath);
        //saveReports(sessionDirectory, suiteName, epdSearchReportModel, nodesReportModel, evaluationReportModel, principalVariationReportModel);
        saveSearchSummary(sessionDirectory, suiteName, summaryModel);
    }

    private void saveSearchSummary(Path sessionDirectory, String suiteName, SummaryModel summaryModel) {
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

    private void saveReports(Path sessionDirectory, String suiteName, EpdSearchReportModel epdSearchReportModel, NodesReportModel nodesReportModel, EvaluationReportModel evaluationReportModel, PrincipalVariationReportModel principalVariationReportModel) {
        Path suitePathReport = sessionDirectory.resolve(String.format("%s-report.txt", suiteName));

        try (PrintStream out = new PrintStream(new FileOutputStream(suitePathReport.toFile()), true)) {

            printReports(out, epdSearchReportModel, nodesReportModel, evaluationReportModel, principalVariationReportModel);

            out.flush();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private void printReports(PrintStream output, EpdSearchReportModel epdSearchReportModel, NodesReportModel nodesReportModel, EvaluationReportModel evaluationReportModel, PrincipalVariationReportModel principalVariationReportModel) {
        output.printf("Version: %s\n", Tango.ENGINE_VERSION);

        new EpdSearchReport()
                .setReportModel(epdSearchReportModel)
                .printReport(output);

        new NodesReport()
                .setReportModel(nodesReportModel)
                .withCutoffStatistics()
                .withNodesVisitedStatistics()
                .printReport(output);

        new EvaluationReport()
                .setReportModel(evaluationReportModel)
                //.withExportEvaluations()
                .withEvaluationsStatistics()
                .printReport(output);


        new PrincipalVariationReport()
                .setReportModel(principalVariationReportModel)
                .printReport(output);
    }

    private Path createSessionDirectory(Path suitePath) {
        Path parentDirectory = suitePath.getParent();

        Path sessionDirectory = parentDirectory.resolve(String.format("depth-%d-%s-%s", depth, SEARCH_SESSION_ID, Tango.ENGINE_VERSION));

        try {
            Files.createDirectory(sessionDirectory);
        } catch (FileAlreadyExistsException e) {
            System.err.printf("Session directory already exists %s\n", sessionDirectory.getFileName().toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return sessionDirectory;
    }

    private static List<Path> getEpdFiles(String directory, String filePattern) {
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

}
