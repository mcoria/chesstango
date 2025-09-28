package net.chesstango.reports.pv;

import net.chesstango.evaluation.Evaluator;

import java.io.PrintStream;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class PrincipalVariationReportPrinter {
    private final PrintStream out;
    private final PrincipalVariationReportModel reportModel;

    public PrincipalVariationReportPrinter(PrintStream out, PrincipalVariationReportModel reportModel) {
        this.out = out;
        this.reportModel = reportModel;
    }

    public void printPrincipalVariation() {
        out.printf("AccuracyAvgPercentageTotal: %d%%\n\n", reportModel.pvAccuracyAvgPercentageTotal);

        out.print("Principal Variations\n");
        // Cuerpo
        for (PrincipalVariationReportModel.PrincipalVariationReportModelDetail moveDetail : reportModel.moveDetails) {
            out.printf("%6s: %s", moveDetail.move, moveDetail.principalVariation);

            out.printf("; eval=%d", moveDetail.evaluation);
            if (moveDetail.evaluation == Evaluator.WHITE_WON || moveDetail.evaluation == Evaluator.BLACK_WON) {
                out.print(" MATE");
            }

            out.printf("; pvAccuracy=%d%%", moveDetail.pvAccuracyPercentage);

            if (Objects.nonNull(moveDetail.id)) {
                out.printf("; ID=%s", moveDetail.id);
            }

            out.print("\n");
        }
    }
}
