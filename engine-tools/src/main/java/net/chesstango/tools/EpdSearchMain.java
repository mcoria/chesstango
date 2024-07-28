package net.chesstango.tools;

import net.chesstango.board.representations.epd.EPD;
import net.chesstango.board.representations.epd.EPDDecoder;
import net.chesstango.engine.Tango;
import net.chesstango.evaluation.DefaultEvaluator;
import net.chesstango.evaluation.evaluators.EvaluatorImp06;
import net.chesstango.tools.search.EpdSearch;
import net.chesstango.tools.search.EpdSearchResult;
import net.chesstango.search.builders.AlphaBetaBuilder;
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
                .setSearchMoveSupplier(() -> AlphaBetaBuilder
                        .createDefaultBuilderInstance(new EvaluatorImp06(EvaluatorImp06.readValues("{\"id\":\"a87ac393\",\"weighs\":[902,45,43],\"mgPawnTbl\":[0,0,0,0,0,0,0,0,-35,-1,-20,-23,-15,24,38,-22,-26,-4,-4,-10,3,3,33,-12,-27,-2,-5,12,17,6,10,-25,-14,13,6,21,23,12,17,-23,-6,7,26,31,65,56,25,-20,98,134,61,95,68,126,34,-82,334,0,0,645,961,289,-201,342],\"mgKnightTbl\":[-105,-21,-58,-33,-17,-28,-19,-23,-29,-53,-12,-3,-1,18,-14,-19,-23,-9,12,10,19,17,25,-16,-13,4,16,13,28,19,21,-8,-9,17,19,53,37,69,18,22,-47,60,37,65,84,129,73,44,-73,-41,72,36,23,62,7,-17,-167,-89,-34,-49,61,-97,-15,-107],\"mgBishopTbl\":[-33,-3,-14,-21,-13,-12,-39,-21,4,15,16,0,7,21,33,1,0,15,15,15,14,27,18,10,-6,13,13,26,34,12,10,4,-4,5,19,50,37,37,7,-2,-16,37,43,40,35,50,37,-2,-26,16,-18,-13,30,59,18,-47,-29,4,-82,-37,-25,-42,7,-8],\"mgRookTbl\":[-19,-13,1,17,16,7,-37,-26,-44,-16,-20,-9,-1,11,-6,-71,-45,-25,-16,-17,3,0,-5,-33,-36,-26,-12,-1,9,-7,6,-23,-24,-11,7,26,24,35,-8,-20,-5,19,26,36,17,45,61,16,27,32,58,62,80,67,26,44,32,42,32,51,63,9,31,43],\"mgQueenTbl\":[-1,-18,-9,10,-15,-25,-31,-50,-35,-8,11,2,8,15,-3,1,-14,2,-11,-2,-5,2,14,5,-9,-26,-9,-10,-2,-4,3,-3,-27,-27,-16,-16,-1,17,-2,1,-13,-17,7,8,29,56,47,57,-24,-39,-5,1,-16,57,28,54,-28,0,29,12,59,44,43,45],\"mgKingTbl\":[-15,36,12,-54,8,-28,24,14,1,7,-8,-64,-43,-16,9,8,-14,-14,-22,-46,-44,-30,-15,-27,-49,-1,-27,-39,-46,-44,-33,-51,-17,-20,-12,-27,-30,-25,-14,-36,-9,24,2,-16,-20,6,-704,-495,29,-1,-20,-7,-357,-4,-697,-29,462,23,-560,-335,3,259,-982,13],\"egPawnTbl\":[0,0,0,0,0,0,0,0,13,8,8,10,13,0,2,-7,4,7,-6,1,0,-5,-1,-8,13,9,-3,-7,-7,-8,3,-1,32,24,13,5,-2,4,17,17,94,100,85,67,56,53,82,84,178,173,158,134,147,132,165,187,0,0,0,0,0,0,0,0],\"egKnightTbl\":[-29,-51,-23,-15,-22,-18,-50,-64,-42,-20,-10,-5,-2,-20,-23,-44,-23,-3,-1,15,10,-3,-20,-22,-18,-6,16,25,16,17,4,-18,-17,3,22,22,22,11,8,-18,-24,-20,10,9,-1,-9,-19,-41,-25,300,-25,-70,680,968,-527,948,58,-771,947,-356,379,-27,542,502],\"egBishopTbl\":[-23,-9,-23,-5,-9,-16,-5,-17,-14,-18,-7,-1,4,-9,-15,-27,-12,-3,8,10,13,3,-7,-15,-6,3,13,19,7,10,-3,-9,-3,9,12,9,14,10,3,2,2,-8,0,-1,-2,6,0,4,-8,-4,7,-12,-3,-13,-4,-14,-14,-21,-11,-8,-7,-9,-17,-24],\"egRookTbl\":[-9,2,3,-1,-5,-13,4,-20,-6,-6,0,2,-9,-9,-11,-3,-4,0,-5,-1,-7,-12,-8,-16,3,5,8,4,-5,-6,-8,-11,4,3,13,1,2,1,-1,2,7,7,7,5,4,-3,-5,-3,11,13,13,11,-3,3,8,3,13,10,18,15,12,12,8,5],\"egQueenTbl\":[-33,-28,-22,-43,-5,-32,-20,-41,-22,-23,-30,-16,-16,-23,-36,-32,-16,-27,15,6,9,17,10,5,-18,28,19,47,31,34,39,23,3,22,24,45,57,40,57,36,-20,6,9,49,47,35,19,9,-17,20,32,41,58,25,30,0,-9,22,22,27,27,19,10,20],\"egKingTbl\":[-53,-34,-21,-11,-28,-14,-24,-43,-27,-11,4,13,14,4,-5,-17,-19,-3,11,21,23,16,7,-9,-18,-4,21,24,27,23,9,-11,-8,22,24,27,26,33,26,3,10,17,23,15,20,45,44,13,-12,17,14,17,17,38,23,11,-74,-35,-18,-18,-11,15,4,-17]}")))
                        .withStatistics()
                        .build())
                .setDepth(depth);

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
        NodesReportModel nodesReportModel = NodesReportModel.collectStatistics(suiteName, epdSearchResults.stream().map(EpdSearchResult::searchResult).toList());
        EvaluationReportModel evaluationReportModel = EvaluationReportModel.collectStatistics(suiteName, epdSearchResults.stream().map(EpdSearchResult::searchResult).toList());
        PrincipalVariationReportModel principalVariationReportModel = PrincipalVariationReportModel.collectStatics(suiteName, epdSearchResults.stream().map(EpdSearchResult::searchResult).toList());
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
