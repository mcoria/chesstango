package net.chesstango.reports.evaluation;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.smart.features.statistics.evaluation.EvaluationEntry;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HexFormat;

/**
 * @author Mauricio Coria
 */
public class ExportEvaluations {
    private final EvaluationReportModel evaluationReportModel;

    public ExportEvaluations(EvaluationReportModel evaluationReportModel) {
        this.evaluationReportModel = evaluationReportModel;
    }

    public void exportEvaluations() {
        int fileCounter = 0;
        // Cuerpo
        for (EvaluationReportModel.EvaluationReportModelDetail moveDetail : evaluationReportModel.moveDetails) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(String.format("evaluations-%d.txt", fileCounter)))) {

                writer.append(String.format("%6s ; Points = %d ", moveDetail.move, moveDetail.evaluation));
                if (moveDetail.evaluation == Evaluator.WHITE_WON || moveDetail.evaluation == Evaluator.BLACK_WON) {
                    writer.append(String.format(" MATE"));
                }
                writer.append(String.format("\n"));

                if (moveDetail.evaluations != null) {
                    HexFormat hexFormat = HexFormat.of().withUpperCase();
                    for (EvaluationEntry evaluation : moveDetail.evaluations) {
                        writer.append(String.format("0x%sL\t%d\n", hexFormat.formatHex(longToByte(evaluation.key())), evaluation.value()));
                    }
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
}
