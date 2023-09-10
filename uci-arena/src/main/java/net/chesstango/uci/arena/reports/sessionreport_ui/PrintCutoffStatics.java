package net.chesstango.uci.arena.reports.sessionreport_ui;

import net.chesstango.uci.arena.reports.SessionReportModel;

import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @author Mauricio Coria
 */
public class PrintCutoffStatics {
    private final List<SessionReportModel> reportRows;
    private final PrintStream out;

    public PrintCutoffStatics(PrintStream out, List<SessionReportModel> reportRows) {
        this.reportRows = reportRows;
        this.out = out;
    }

    public void printCutoffStatics(AtomicInteger maxLevelVisited) {
        out.println("\n Cutoff per search level (higher is better)");

        // Marco superior de la tabla
        out.printf(" ______________________________________________");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> out.printf("___________"));
        out.printf("\n");


        // Nombre de las columnas
        out.printf("|ENGINE NAME                        | SEARCHES ");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> out.printf("| Level %2d ", depth + 1));
        out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            out.printf("|%35s|%9d ", row.engineName, row.searches);
            IntStream.range(0, maxLevelVisited.get()).forEach(depth -> out.printf("| %6d %% ", row.cutoffPercentages[depth]));
            out.printf("|\n");
        });

        // Marco inferior de la tabla
        out.printf(" ----------------------------------------------");
        IntStream.range(0, maxLevelVisited.get()).forEach(depth -> out.printf("-----------"));
        out.printf("\n");
    }
}