package net.chesstango.tools.search.reports.arena.sessionreport_ui;

import net.chesstango.tools.search.reports.arena.SessionReportModel;

import java.io.PrintStream;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class PrintCollisionStatistics {
    private final List<SessionReportModel> reportRows;
    private final PrintStream out;

    public PrintCollisionStatistics(PrintStream out, List<SessionReportModel> reportRows) {
        this.reportRows = reportRows;
        this.out = out;
    }

    public void printCollisionStatistics() {
        // Marco superior de la tabla
        out.printf(" ______________________________________________________________________________________________________________");
        out.printf("\n");


        // Nombre de las columnas
        out.printf("|ENGINE NAME                        | SEARCHES | wo/COLLISIONS | w/COLLISIONS | wo/COLLISIONS%% | w/COLLISIONS%% ");
        out.printf("|\n");

        // Cuerpo
        reportRows.forEach(row -> {
            out.printf("|%35s|%9d |%14d |%13d |%12d %%  |%12d %% ", row.engineName, row.searches, row.searchesWithoutCollisions, row.searchesWithCollisions, row.searchesWithoutCollisionsPercentage, row.searchesWithCollisionsPercentage);
            out.printf("|\n");
        });

        // Marco inferior de la tabla
        out.printf(" --------------------------------------------------------------------------------------------------------------");
        out.printf("\n");
    }
}
