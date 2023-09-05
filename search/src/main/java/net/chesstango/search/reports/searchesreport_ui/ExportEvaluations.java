package net.chesstango.search.reports.searchesreport_ui;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.reports.SearchesReportModel;
import net.chesstango.search.smart.statistics.EvaluationEntry;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HexFormat;

/**
 * @author Mauricio Coria
 */
public class ExportEvaluations {
    private final SearchesReportModel reportModel;

    public ExportEvaluations(SearchesReportModel reportModel) {
        this.reportModel = reportModel;
    }

    public void exportEvaluations() {
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
}
