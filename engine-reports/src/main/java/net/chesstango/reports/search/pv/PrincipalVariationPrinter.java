package net.chesstango.reports.search.pv;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.reports.Printer;

import java.io.PrintStream;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
class PrincipalVariationPrinter implements Printer {

    @Setter
    @Accessors(chain = true)
    private PrincipalVariationModel reportModel;

    @Setter
    @Accessors(chain = true)
    private PrintStream out;

    @Override
    public PrincipalVariationPrinter print() {
        out.printf("--------------------------------------------------------------------------------------------------------------------------------------------------------\n");

        out.printf("PrincipalVariationReport: %s\n\n", reportModel.reportTitle);

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

        return this;
    }
}
