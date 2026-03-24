package net.chesstango.reports.search;

import lombok.Getter;
import net.chesstango.reports.Model;
import net.chesstango.reports.search.board.BoardModel;
import net.chesstango.reports.search.evaluation.EvaluationModel;
import net.chesstango.reports.search.evaluation.cache.EvaluationCacheModel;
import net.chesstango.reports.search.nodes.types.NodesTypesModel;
import net.chesstango.reports.search.nodes.depth.NodesDepthModel;
import net.chesstango.reports.search.pv.PrincipalVariationModel;
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
    private NodesDepthModel nodesVisitedModel;

    @Getter
    private NodesTypesModel nodesTypesModel;

    @Getter
    private TranspositionModel transpositionModel;

    @Getter
    private EvaluationModel evaluationModel;

    @Getter
    private EvaluationCacheModel evaluationCacheModel;

    @Getter
    private BoardModel boardModel;

    @Getter
    private PrincipalVariationModel principalVariationModel;

    @Override
    public SummaryModel collectStatistics(String searchGroupName, List<SearchResult> searchResults) {
        this.searchGroupName = searchGroupName;

        load(searchResults);

        return this;
    }

    private void load(List<SearchResult> searchResults) {
        this.searches = searchResults.size();

        nodesVisitedModel = new NodesDepthModel().collectStatistics(searchGroupName, searchResults);

        nodesTypesModel = new NodesTypesModel().collectStatistics(searchGroupName, searchResults);

        transpositionModel = new TranspositionModel().collectStatistics(searchGroupName, searchResults);

        evaluationModel = new EvaluationModel().collectStatistics(searchGroupName, searchResults);

        evaluationCacheModel = new EvaluationCacheModel().collectStatistics(searchGroupName, searchResults);

        boardModel = new BoardModel().collectStatistics(searchGroupName, searchResults);

        principalVariationModel = new PrincipalVariationModel().collectStatistics(searchGroupName, searchResults);
    }

}
