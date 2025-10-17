package net.chesstango.reports.detail.pv;

import net.chesstango.evaluation.Evaluator;

import java.io.PrintStream;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class PrincipalVariationPrinter {
    private final PrintStream out;
    private final PrincipalVariationModel reportModel;

    public PrincipalVariationPrinter(PrintStream out, PrincipalVariationModel reportModel) {
        this.out = out;
        this.reportModel = reportModel;
    }

    public void printPrincipalVariation() {
        out.printf("AccuracyAvgPercentageTotal: %d%%\n\n", reportModel.pvAccuracyAvgPercentageTotal);

        out.print("Principal Variations\n");
        // Cuerpo
        for (PrincipalVariationModel.PrincipalVariationReportModelDetail moveDetail : reportModel.moveDetails) {
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
