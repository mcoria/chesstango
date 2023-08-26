package net.chesstango.search.reports;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class EpdSearchReportModel {
    long searches;
    long success;
    int successRate;
    List<String> failedEntries;
    long duration;

    public static EpdSearchReportModel collectStatics(List<EPDSearchResult> edpEntries) {
        EpdSearchReportModel reportModel = new EpdSearchReportModel();

        reportModel.searches = edpEntries.size();

        reportModel.success = edpEntries.stream().filter(EPDSearchResult::bestMoveFound).count();

        reportModel.successRate = (int) ((100 * reportModel.success) / reportModel.searches);

        reportModel.duration = edpEntries.stream().mapToLong(EPDSearchResult::searchDuration).sum();

        reportModel.failedEntries = new ArrayList<>();

        edpEntries.stream()
                .filter(edpEntry -> !edpEntry.bestMoveFound())
                .forEach(edpEntry ->
                        reportModel.failedEntries.add(
                                String.format("Fail [%s] - best move found %s",
                                        edpEntry.getText(),
                                        edpEntry.bestMoveFoundStr()
                                )
                        ));

        return reportModel;
    }
}
