package net.chesstango.reports.search;

import net.chesstango.reports.Report;
import net.chesstango.reports.search.board.BoardModel;
import net.chesstango.reports.search.evaluation.EvaluationModel;
import net.chesstango.reports.search.evaluation.cache.EvaluationCacheModel;
import net.chesstango.reports.search.nodes.depth.NodesDepthModel;
import net.chesstango.reports.search.nodes.types.NodesTypesModel;
import net.chesstango.reports.search.pv.PrincipalVariationModel;
import net.chesstango.reports.search.transposition.TranspositionModel;
import net.chesstango.search.SearchResult;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

/**
 * Este reporte resume las sessiones de engine Tango
 *
 * @author Mauricio Coria
 */
public class SummaryReport implements Report {
    /**
     * Observar que es una lista a diferencia de detail
     */
    private final List<SummaryModel> summaryModels = new LinkedList<>();

    private boolean printBoardStatistics;
    private boolean printNodesVisitedStatistics;
    private boolean printNodesTypesStatistics;
    private boolean printCutoffStatistics;
    private boolean printTranspositionStatistics;
    private boolean printEvaluationStatistics;
    private boolean principalVariationStatistics;
    private boolean principalEvaluationCacheReport;

    private PrintStream out;

    @Override
    public SummaryReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }

    public SummaryReport addSearchesByTreeSummaryModel(String searchGroupName, List<SearchResult> searches) {
        summaryModels.add(new SummaryModel().collectStatistics(searchGroupName, searches));
        return this;
    }

    private void print() {
        out.print("--------------------------------------------------------------------------------------------------------------------------------------------------------");

        if (printBoardStatistics) {
            List<BoardModel> reportRows = summaryModels
                    .stream()
                    .map(SummaryModel::getBoardModel)
                    .toList();
            new SummaryBoardPrinter()
                    .setReportRows(reportRows)
                    .setOut(out)
                    .print();
        }

        if (printNodesVisitedStatistics) {
            List<NodesDepthModel> reportRows = summaryModels
                    .stream()
                    .map(SummaryModel::getNodesVisitedModel)
                    .toList();
            new SummaryNodesDepthPrinter()
                    .setReportRows(reportRows)
                    .setOut(out)
                    .print();
        }

        if (printNodesTypesStatistics) {
            List<NodesTypesModel> reportRows = summaryModels
                    .stream()
                    .map(SummaryModel::getNodesTypesModel)
                    .toList();
            new SummaryNodesTypesPrinter()
                    .setReportRows(reportRows)
                    .setOut(out)
                    .print();
        }

        if (printCutoffStatistics) {
            List<NodesDepthModel> reportRows = summaryModels
                    .stream()
                    .map(SummaryModel::getNodesVisitedModel)
                    .toList();

            new SummaryCutoffPrinter()
                    .setReportRows(reportRows)
                    .setOut(out)
                    .print();
        }

        if (printEvaluationStatistics) {
            List<EvaluationModel> reportRows = summaryModels
                    .stream()
                    .map(SummaryModel::getEvaluationModel)
                    .toList();

            new SummaryEvaluationPrinter()
                    .setReportRows(reportRows)
                    .setOut(out)
                    .print();
        }

        if (principalEvaluationCacheReport) {
            List<EvaluationCacheModel> reportRows = summaryModels
                    .stream()
                    .map(SummaryModel::getEvaluationCacheModel)
                    .toList();

            new SummaryEvaluationCachePrinter()
                    .setReportRows(reportRows)
                    .setOut(out)
                    .print();
        }

        if (printTranspositionStatistics) {
            List<TranspositionModel> reportRows = summaryModels
                    .stream()
                    .map(SummaryModel::getTranspositionModel)
                    .toList();

            new SummaryTranspositionPrinter()
                    .setReportRows(reportRows)
                    .setOut(out)
                    .print();
        }

        if (principalVariationStatistics) {
            List<PrincipalVariationModel> reportRows = summaryModels
                    .stream()
                    .map(SummaryModel::getPrincipalVariationModel)
                    .toList();

            new SummaryPrincipalVariationPrinter()
                    .setReportRows(reportRows)
                    .setOut(out)
                    .print();
        }
    }

    public SummaryReport withBoardStatistics() {
        this.printBoardStatistics = true;
        return this;
    }

    public SummaryReport withNodesVisitedStatistics() {
        this.printNodesVisitedStatistics = true;
        return this;
    }

    public SummaryReport withNodesTypesStatistics() {
        this.printNodesTypesStatistics = true;
        return this;
    }

    public SummaryReport withCutoffStatistics() {
        this.printCutoffStatistics = true;
        return this;
    }

    public SummaryReport withTranspositionStatistics() {
        this.printTranspositionStatistics = true;
        return this;
    }

    public SummaryReport withEvaluationStatistics() {
        this.printEvaluationStatistics = true;
        return this;
    }

    public SummaryReport withEvaluationCacheStatistics() {
        this.principalEvaluationCacheReport = true;
        return this;
    }

    public SummaryReport withPrincipalVariationStatistics() {
        this.principalVariationStatistics = true;
        return this;
    }
}
