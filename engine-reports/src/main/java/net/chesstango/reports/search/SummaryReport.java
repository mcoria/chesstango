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

    private boolean withBoardStatistics;
    private boolean withNodesVisitedStatistics;
    private boolean withNodesTypesStatistics;
    private boolean withCutoffStatistics;
    private boolean withTranspositionStatistics;
    private boolean withEvaluationStatistics;
    private boolean withPrincipalVariationStatistics;
    private boolean withEvaluationCacheStatistics;

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

        if (withBoardStatistics) {
            List<BoardModel> reportRows = summaryModels
                    .stream()
                    .map(SummaryModel::getBoardModel)
                    .toList();
            new SummaryBoardPrinter()
                    .setReportRows(reportRows)
                    .setOut(out)
                    .print();
        }

        if (withNodesVisitedStatistics) {
            List<NodesDepthModel> reportRows = summaryModels
                    .stream()
                    .map(SummaryModel::getNodesVisitedModel)
                    .toList();
            new SummaryNodesDepthPrinter()
                    .setReportRows(reportRows)
                    .setOut(out)
                    .print();
        }

        if (withNodesTypesStatistics) {
            List<NodesTypesModel> reportRows = summaryModels
                    .stream()
                    .map(SummaryModel::getNodesTypesModel)
                    .toList();
            new SummaryNodesTypesPrinter()
                    .setReportRows(reportRows)
                    .setOut(out)
                    .print();
        }

        if (withCutoffStatistics) {
            List<NodesDepthModel> reportRows = summaryModels
                    .stream()
                    .map(SummaryModel::getNodesVisitedModel)
                    .toList();

            new SummaryCutoffPrinter()
                    .setReportRows(reportRows)
                    .setOut(out)
                    .print();
        }

        if (withPrincipalVariationStatistics) {
            List<PrincipalVariationModel> reportRows = summaryModels
                    .stream()
                    .map(SummaryModel::getPrincipalVariationModel)
                    .toList();

            new SummaryPrincipalVariationPrinter()
                    .setReportRows(reportRows)
                    .setOut(out)
                    .print();
        }

        if (withTranspositionStatistics) {
            List<TranspositionModel> reportRows = summaryModels
                    .stream()
                    .map(SummaryModel::getTranspositionModel)
                    .toList();

            new SummaryTranspositionPrinter()
                    .setReportRows(reportRows)
                    .setOut(out)
                    .print();
        }

        if (withEvaluationStatistics) {
            List<EvaluationModel> reportRows = summaryModels
                    .stream()
                    .map(SummaryModel::getEvaluationModel)
                    .toList();

            new SummaryEvaluationPrinter()
                    .setReportRows(reportRows)
                    .setOut(out)
                    .print();
        }

        if (withEvaluationCacheStatistics) {
            List<EvaluationCacheModel> reportRows = summaryModels
                    .stream()
                    .map(SummaryModel::getEvaluationCacheModel)
                    .toList();

            new SummaryEvaluationCachePrinter()
                    .setReportRows(reportRows)
                    .setOut(out)
                    .print();
        }
    }

    public SummaryReport withBoardStatistics() {
        this.withBoardStatistics = true;
        return this;
    }

    public SummaryReport withNodesVisitedStatistics() {
        this.withNodesVisitedStatistics = true;
        return this;
    }

    public SummaryReport withNodesTypesStatistics() {
        this.withNodesTypesStatistics = true;
        return this;
    }

    public SummaryReport withCutoffStatistics() {
        this.withCutoffStatistics = true;
        return this;
    }

    public SummaryReport withTranspositionStatistics() {
        this.withTranspositionStatistics = true;
        return this;
    }

    public SummaryReport withEvaluationStatistics() {
        this.withEvaluationStatistics = true;
        return this;
    }

    public SummaryReport withEvaluationCacheStatistics() {
        this.withEvaluationCacheStatistics = true;
        return this;
    }

    public SummaryReport withPrincipalVariationStatistics() {
        this.withPrincipalVariationStatistics = true;
        return this;
    }
}
