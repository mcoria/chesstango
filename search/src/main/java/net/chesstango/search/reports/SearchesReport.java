package net.chesstango.search.reports;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.statistics.EvaluationEntry;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HexFormat;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * @author Mauricio Coria
 */
public class SearchesReport {
    private boolean printCutoffStatistics;
    private boolean printNodesVisitedStatistics;
    private boolean printEvaluationsStatistics;
    private boolean printPrincipalVariation;
    private boolean exportEvaluations;

    @Setter
    @Accessors(chain = true)
    private String engineName = "Tango";

    @Setter
    @Accessors(chain = true)
    private SearchesReportModel reportModel;


    private PrintStream out;

    public SearchesReport printReport(PrintStream output) {
        out = output;
        print();
        return this;
    }


    protected void print() {
        printSummary();

        if (printNodesVisitedStatistics) {
            printVisitedNodes();
        }

        if (printEvaluationsStatistics) {
            printEvaluationsStatistics();
        }

        if (printCutoffStatistics) {
            printCutoff();
        }

        if (printPrincipalVariation) {
            printPrincipalVariation();
        }

        if (exportEvaluations) {
            exportEvaluations();
        }

    }

    private void printSummary() {
        out.printf("----------------------------------------------------------------------------\n\n");

        out.printf("Moves played by engine: %s\n", reportModel.engineName);
        out.printf("Visited         RNodes: %8d\n", reportModel.visitedRNodesTotal);
        out.printf("Visited         QNodes: %8d\n", reportModel.visitedQNodesTotal);
        out.printf("Visited  Total   Nodes: %8d\n", reportModel.visitedNodesTotal);
        out.printf("Cutoff                : %3d%%\n", reportModel.cutoffPercentageTotal);
        out.printf("Evaluated        Nodes: %8d\n", reportModel.evaluatedGamesCounterTotal);
        out.printf("Executed         Moves: %8d\n", reportModel.executedMovesTotal);
        out.printf("Max             RDepth: %8d\n", reportModel.maxSearchRLevel);
        out.printf("Max             QDepth: %8d\n", reportModel.maxSearchQLevel);
        out.printf("\n");
    }

    private void printVisitedNodes() {
        out.printf("Visited Nodes Statistics\n");

        int longestId = 0;
        for (int i = 0; i < reportModel.moveDetails.size(); i++) {
            String epdId = reportModel.moveDetails.get(i).id;
            if (Objects.nonNull(epdId) && epdId.length() > longestId) {
                longestId = epdId.length();
            }
        }

        // Marco superior de la tabla
        out.printf(" ________");
        IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("_____________________"));
        IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("_____________________"));
        out.printf("_____________________");
        out.printf("_____________________");
        out.printf("_____________________");
        out.printf("____________");
        if (longestId > 0) {
            out.printf(" %s", "_".repeat(longestId + 2));
        }
        out.printf("\n");

        // Nombre de las columnas
        out.printf("| Move   ");
        IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("|    RLevel %2d       ", depth + 1));
        IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("|    QLevel %2d       ", depth + 1));
        out.printf("|      RTotal        ");
        out.printf("|      QTotal        ");
        out.printf("|       Total        ");
        out.printf("| MovesExe  ");
        if (longestId > 0) {
            out.printf("| ID");
            out.printf(" ".repeat(longestId - 1));
        }
        out.printf("|\n");

        // Cuerpo
        for (SearchesReportModel.SearchReportModelDetail moveDetail : reportModel.moveDetails) {
            out.printf("| %6s ", moveDetail.move);

            IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("| %7d / %8d ", moveDetail.visitedRNodesCounters[depth], moveDetail.expectedRNodesCounters[depth]));
            IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("| %7d / %8d ", moveDetail.visitedQNodesCounters[depth], moveDetail.expectedQNodesCounters[depth]));

            out.printf("| %7d / %8d ", moveDetail.visitedRNodesCounter, moveDetail.expectedRNodesCounter);
            out.printf("| %7d / %8d ", moveDetail.visitedQNodesCounter, moveDetail.expectedQNodesCounter);
            out.printf("| %7d / %8d ", moveDetail.visitedNodesTotal, moveDetail.expectedNodesTotal);

            out.printf("|   %7d ", moveDetail.executedMoves);

            if (longestId > 0) {
                out.printf("| %" + longestId + "s ", moveDetail.id);
            }
            out.printf("|\n");
        }

        // Totales
        out.printf("|--------");
        IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("|--------------------"));
        IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("|--------------------"));
        out.printf("|--------------------");
        out.printf("|--------------------");
        out.printf("|--------------------");
        out.printf("|-----------");
        if (longestId > 0) {
            out.printf("|");
            out.printf("-".repeat(longestId + 2));
        }
        out.printf("|\n");

        out.printf("| SUM    ");
        IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("| %7d / %8d ", reportModel.visitedRNodesCounters[depth], reportModel.expectedRNodesCounters[depth]));
        IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("| %7d / %8d ", reportModel.visitedQNodesCounters[depth], reportModel.expectedQNodesCounters[depth]));
        out.printf("| %7d / %8d ", reportModel.visitedRNodesTotal, reportModel.expectedRNodesTotal);
        out.printf("| %7d / %8d ", reportModel.visitedQNodesTotal, reportModel.expectedQNodesTotal);
        out.printf("| %7d / %8d ", reportModel.visitedNodesTotal, reportModel.expectedNodesTotal);
        out.printf("|   %7d ", reportModel.executedMovesTotal);
        if (longestId > 0) {
            out.printf("|");
            out.printf(" ".repeat(longestId + 2));
        }
        out.printf("|\n");


        // Marco inferior de la tabla
        out.printf("----------");
        IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("---------------------"));
        IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("---------------------"));
        out.printf("---------------------");
        out.printf("---------------------");
        out.printf("---------------------");
        out.printf("----------- ");
        if (longestId > 0) {
            out.printf("%s", "-".repeat(longestId + 2));
        }
        out.printf("\n\n");
    }

    private void printEvaluationsStatistics() {
        out.printf("Evaluation Statistics\n");

        int longestId = 0;
        for (int i = 0; i < reportModel.moveDetails.size(); i++) {
            String epdId = reportModel.moveDetails.get(i).id;
            if (Objects.nonNull(epdId) && epdId.length() > longestId) {
                longestId = epdId.length();
            }
        }

        // Marco superior de la tabla
        out.printf(" ________");
        out.printf("______________");
        out.printf("_______________");
        out.printf("_______________");
        out.printf("_______________");
        out.printf("______________");
        if (longestId > 0) {
            out.printf(" %s", "_".repeat(longestId + 2));
        }
        out.printf("\n");

        // Nombre de las columnas
        out.printf("| Move   ");
        out.printf("| Evaluations ");
        out.printf("| GEvaluations ");
        out.printf("| UEvaluations ");
        out.printf("| CEvaluations ");
        out.printf("| PCollisions ");
        if (longestId > 0) {
            out.printf("| ID");
            out.printf(" ".repeat(longestId - 1));
        }
        out.printf("|\n");

        // Cuerpo
        for (SearchesReportModel.SearchReportModelDetail moveDetail : reportModel.moveDetails) {
            out.printf("| %6s ", moveDetail.move);
            out.printf("| %11d ", moveDetail.evaluatedGamesCounter);
            out.printf("| %12d ", moveDetail.distinctEvaluatedGamesCounter);
            out.printf("| %12d ", moveDetail.distinctEvaluatedGamesCounterUnique);
            out.printf("| %12d ", moveDetail.distinctEvaluatedGamesCounterCollisions);
            out.printf("| %9d %% ", moveDetail.evaluationCollisionPercentage);
            if (longestId > 0) {
                out.printf("| %" + longestId + "s ", moveDetail.id);
            }
            out.printf("|\n");
        }

        // Totales
        out.printf("|--------");
        out.printf("|-------------");
        out.printf("|--------------");
        out.printf("|--------------");
        out.printf("|--------------");
        out.printf("|-------------");
        if (longestId > 0) {
            out.printf("|");
            out.printf("-".repeat(longestId + 2));
        }
        out.printf("|\n");


        out.printf("| SUM    ");
        out.printf("| %11d ", reportModel.evaluatedGamesCounterTotal);
        out.printf("| %12d ", reportModel.distinctEvaluatedGamesCounterTotal);
        out.printf("| %12d ", reportModel.distinctEvaluatedGamesCounterUniqueTotal);
        out.printf("| %12d ", reportModel.distinctEvaluatedGamesCounterCollisionsTotal);
        out.printf("| %9d %% ", reportModel.evaluationCollisionPercentageTotal);
        if (longestId > 0) {
            out.printf("|");
            out.printf(" ".repeat(longestId + 2));
        }
        out.printf("|\n");


        // Marco inferior de la tabla
        out.printf(" ---------");
        out.printf("--------------");
        out.printf("---------------");
        out.printf("---------------");
        out.printf("---------------");
        out.printf("------------- ");
        if (longestId > 0) {
            out.printf("%s", "-".repeat(longestId + 2));
        }
        out.printf("\n\n");
    }

    private void printCutoff() {
        out.printf("Cutoff per search level (higher is better)\n");

        // Marco superior de la tabla
        out.printf(" ________");
        IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("____________"));
        IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("____________"));
        out.printf("\n");

        // Nombre de las columnas
        out.printf("| Move   ");
        IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("| RLevel %2d ", depth + 1));
        IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("| QLevel %2d ", depth + 1));
        out.printf("|\n");

        // Cuerpo
        for (SearchesReportModel.SearchReportModelDetail moveDetail : reportModel.moveDetails) {
            out.printf("| %6s ", moveDetail.move);
            IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("|  %6d %% ", moveDetail.cutoffRPercentages[depth]));
            IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("|  %6d %% ", moveDetail.cutoffQPercentages[depth]));
            out.printf("|\n");
        }

        // Marco inferior de la tabla
        out.printf(" --------");
        IntStream.range(0, reportModel.maxSearchRLevel).forEach(depth -> out.printf("------------"));
        IntStream.range(0, reportModel.maxSearchQLevel).forEach(depth -> out.printf("------------"));
        out.printf("\n\n");
    }

    private void printPrincipalVariation() {
        out.printf("Principal Variations\n");


        // Nombre de las columnas
        out.printf("Move\n");

        // Cuerpo
        for (SearchesReportModel.SearchReportModelDetail moveDetail : reportModel.moveDetails) {
            out.printf("%6s:", moveDetail.move);
            out.printf(" %s; Points = %d ", moveDetail.principalVariation, moveDetail.evaluation);
            if (moveDetail.evaluation == GameEvaluator.WHITE_WON || moveDetail.evaluation == GameEvaluator.BLACK_WON) {
                out.printf(" MATE");
            }
            out.printf("\n");
        }
    }

    private void exportEvaluations() {
        int fileCounter = 0;
        // Cuerpo
        for (SearchesReportModel.SearchReportModelDetail moveDetail : reportModel.moveDetails) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("evaluations-%d.txt", fileCounter)))) {

                writer.append(String.format("%6s:", moveDetail.move));
                writer.append(String.format(" %s; Points = %d ", moveDetail.principalVariation, moveDetail.evaluation));
                if (moveDetail.evaluation == GameEvaluator.WHITE_WON || moveDetail.evaluation == GameEvaluator.BLACK_WON) {
                    writer.append(String.format(" MATE"));
                }
                writer.append(String.format("\n"));

                HexFormat hexFormat = HexFormat.of().withUpperCase();
                for (EvaluationEntry evaluation : moveDetail.evaluations) {
                    writer.append(String.format("0x%sL\t%d\n", hexFormat.formatHex(longToByte(evaluation.key())), evaluation.value()));
                }
                writer.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            fileCounter++;
        }
    }

    private byte[] longToByte(long lng) {
        byte[] b = new byte[]{
                (byte) (lng >> 56),
                (byte) (lng >> 48),
                (byte) (lng >> 40),
                (byte) (lng >> 32),
                (byte) (lng >> 24),
                (byte) (lng >> 16),
                (byte) (lng >> 8),
                (byte) lng
        };
        return b;
    }

    public SearchesReport withCutoffStatics() {
        this.printCutoffStatistics = true;
        return this;
    }

    public SearchesReport withNodesVisitedStatics() {
        this.printNodesVisitedStatistics = true;
        return this;
    }

    public SearchesReport withEvaluationsStatics() {
        this.printEvaluationsStatistics = true;
        return this;
    }

    public SearchesReport withPrincipalVariation() {
        this.printPrincipalVariation = true;
        return this;
    }

    public SearchesReport withExportEvaluations() {
        this.exportEvaluations = true;
        return this;
    }

    public SearchesReport withMoveResults(List<SearchMoveResult> searchMoveResults) {
        this.reportModel = SearchesReportModel.collectStatics(this.engineName, searchMoveResults);
        return this;
    }

    public SearchesReport withReportModel(SearchesReportModel searchesReportModel) {
        this.reportModel = searchesReportModel;
        return this;
    }

}
