package net.chesstango.search.reports;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class EpdSearchReportModel {
    int searches;
    int success;
    int successRate;
    List<String> failedEntries;
    long duration;

    public static EpdSearchReportModel collectStatics(List<EPDSearchResult> edpEntries) {
        EpdSearchReportModel reportModel = new EpdSearchReportModel();

        reportModel.searches = edpEntries.size();

        reportModel.success = (int) edpEntries.stream().filter(EPDSearchResult::bestMoveFound).count();

        reportModel.successRate = ((100 * reportModel.success) / reportModel.searches);

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
