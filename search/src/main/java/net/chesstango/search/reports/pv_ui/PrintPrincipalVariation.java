package net.chesstango.search.reports.pv_ui;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.reports.PrincipalVariationReportModel;

import java.io.PrintStream;

/**
 * @author Mauricio Coria
 */
public class PrintPrincipalVariation {
    private final PrintStream out;
    private final PrincipalVariationReportModel reportModel;

    public PrintPrincipalVariation(PrintStream out, PrincipalVariationReportModel reportModel) {
        this.out = out;
        this.reportModel = reportModel;
    }

    public void printPrincipalVariation() {
        out.printf("Principal Variations\n");


        // Nombre de las columnas
        out.printf("Move\n");

        // Cuerpo
        for (PrincipalVariationReportModel.PrincipalVariationReportModelDetail moveDetail : reportModel.moveDetails) {
            out.printf("%6s:", moveDetail.move);
            out.printf(" %s; Points = %d ", moveDetail.principalVariation, moveDetail.evaluation);
            if (moveDetail.evaluation == GameEvaluator.WHITE_WON || moveDetail.evaluation == GameEvaluator.BLACK_WON) {
                out.printf(" MATE");
            }
            out.printf("\n");
        }
    }
}
