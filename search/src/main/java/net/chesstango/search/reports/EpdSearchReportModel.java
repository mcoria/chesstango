package net.chesstango.search.reports;

import net.chesstango.search.EpdSearchResult;

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

    public static EpdSearchReportModel collectStatics(List<EpdSearchResult> edpEntries) {
        EpdSearchReportModel reportModel = new EpdSearchReportModel();

        reportModel.searches = edpEntries.size();

        reportModel.success = (int) edpEntries.stream().filter(EpdSearchResult::bestMoveFound).count();

        reportModel.successRate = ((100 * reportModel.success) / reportModel.searches);

        reportModel.duration = edpEntries.stream().mapToLong(EpdSearchResult::searchDuration).sum();

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
