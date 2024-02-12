package net.chesstango.search;

import net.chesstango.board.representations.EPDEntry;
import net.chesstango.board.representations.EPDReader;
import net.chesstango.search.reports.*;
import net.chesstango.search.reports.evaluation.EvaluationReport;
import net.chesstango.search.reports.evaluation.EvaluationReportModel;
import net.chesstango.search.reports.nodes.NodesReport;
import net.chesstango.search.reports.nodes.NodesReportModel;
import net.chesstango.search.reports.pv.PrincipalVariationReportModel;
import net.chesstango.search.reports.summary.SummaryModel;
import net.chesstango.search.reports.summary.SummarySaver;

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

        EpdSearchMain suite = new EpdSearchMain(depth);

        getEpdFiles(directory, filePattern).forEach(suite::execute);
    }


    protected final int depth;

    public EpdSearchMain(int depth) {
        this.depth = depth;
    }

    public void execute(Path suitePath) {
        EPDReader reader = new EPDReader();

        List<EPDEntry> edpEntries = reader.readEdpFile(suitePath);

        List<EpdSearchResult> epdSearchResults = new EpdSearch()
                .setDepth(depth)
                .run(edpEntries);

        report(suitePath, epdSearchResults);

        //System.gc();
    }

    private void report(Path suitePath, List<EpdSearchResult> epdSearchResults) {
        String suiteName = suitePath.getFileName().toString();

        EpdSearchReportModel epdSearchReportModel = EpdSearchReportModel.collectStatistics(suiteName, epdSearchResults);
        NodesReportModel nodesReportModel = NodesReportModel.collectStatistics(suiteName, epdSearchResults.stream().map(EpdSearchResult::searchResult).toList());
        EvaluationReportModel evaluationReportModel = EvaluationReportModel.collectStatistics(suiteName, epdSearchResults.stream().map(EpdSearchResult::searchResult).toList());
        PrincipalVariationReportModel principalVariationReportModel = PrincipalVariationReportModel.collectStatics(suiteName, epdSearchResults.stream().map(EpdSearchResult::searchResult).toList());
        SummaryModel summaryModel = SummaryModel.collectStatics(SEARCH_SESSION_ID, epdSearchResults, epdSearchReportModel, nodesReportModel, evaluationReportModel, principalVariationReportModel);

        //printReports(System.out, epdSearchReportModel, nodesReportModel, evaluationReportModel);


        Path sessionDirectory = createSessionDirectory(suitePath);
        saveReports(sessionDirectory, suiteName, epdSearchReportModel, nodesReportModel, evaluationReportModel, principalVariationReportModel);
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
