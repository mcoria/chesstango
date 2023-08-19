package net.chesstango.search.reports;

import net.chesstango.board.representations.EPDReader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class EdpSearchReportModel {
    long searches;
    long success;
    int successRate;
    List<String> failedEntries;
    long duration;

    public static EdpSearchReportModel collectStatics(List<EPDReader.EDPEntry> edpEntries) {
        EdpSearchReportModel reportModel = new EdpSearchReportModel();

        reportModel.searches = edpEntries.size();

        reportModel.success = edpEntries.stream().filter(edpEntry -> edpEntry.bestMoveFound).count();

        reportModel.successRate = (int) ((100 * reportModel.success) / reportModel.searches);

        reportModel.duration = edpEntries.stream().mapToLong(edpEntry -> edpEntry.searchDuration).sum();

        reportModel.failedEntries = new ArrayList<>();

        edpEntries.stream()
                .filter(edpEntry -> !edpEntry.bestMoveFound)
                .forEach(edpEntry ->
                        reportModel.failedEntries.add(
                                String.format("Fail [%s] - best move found %s",
                                        edpEntry.text,
                                        edpEntry.bestMoveFoundStr
                                )
                        ));

        return reportModel;
    }
}
