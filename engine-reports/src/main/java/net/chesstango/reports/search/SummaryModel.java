package net.chesstango.reports.search;

import lombok.Getter;
import net.chesstango.reports.Model;
import net.chesstango.reports.search.nodes.NodesModel;
import net.chesstango.reports.search.transposition.TranspositionModel;
import net.chesstango.search.SearchResult;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class SummaryModel implements Model<List<SearchResult>> {
    public String searchGroupName;

    public int searches;

    @Getter
    private NodesModel nodesModel;

    @Getter
    private TranspositionModel transpositionModel;

    public SummaryModel collectStatistics(String searchGroupName, List<SearchResult> searchResults) {
        this.searchGroupName = searchGroupName;

        load(searchResults);

        return this;
    }

    private void load(List<SearchResult> searchResults) {
        this.searches = searchResults.size();

        nodesModel = new NodesModel().collectStatistics(searchGroupName, searchResults);

        transpositionModel = new TranspositionModel().collectStatistics(searchGroupName, searchResults);
    }

}
