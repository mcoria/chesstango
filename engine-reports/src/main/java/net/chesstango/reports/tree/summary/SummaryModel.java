package net.chesstango.reports.tree.summary;

import lombok.Getter;
import net.chesstango.reports.tree.nodes.NodesModel;
import net.chesstango.reports.tree.transposition.TranspositionModel;
import net.chesstango.search.SearchResult;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SummaryModel {
    public String searchName;

    public int searches;

    @Getter
    private NodesModel nodesModel;

    @Getter
    private TranspositionModel transpositionModel;

    public static SummaryModel collectStatics(String searchesName, List<SearchResult> searchResults) {
        SummaryModel summaryModel = new SummaryModel();

        summaryModel.searchName = searchesName;

        summaryModel.load(searchResults);

        return summaryModel;
    }

    private void load(List<SearchResult> searchResults) {
        this.searches = searchResults.size();

        nodesModel = NodesModel.collectStatistics(searchName, searchResults);

        transpositionModel = TranspositionModel.collectStatistics(searchName, searchResults);
    }

}
