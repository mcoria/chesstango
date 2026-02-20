package net.chesstango.reports.engine;

import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.engine.SearchByOpenBookResult;
import net.chesstango.engine.SearchByTablebaseResult;
import net.chesstango.engine.SearchByTreeResult;
import net.chesstango.engine.SearchResponse;
import net.chesstango.reports.Model;
import net.chesstango.reports.search.evaluation.EvaluationModel;
import net.chesstango.search.SearchResult;

import java.util.LinkedList;
import java.util.List;

import static net.chesstango.reports.engine.SearchManagerModel.MoveType.*;

/**
 *
 * @author Mauricio Coria
 */
public class SearchManagerModel implements Model<List<SearchResponse>> {
    public enum MoveType {
        OpenBook,
        Tablebase,
        Tree
    }

    String searchesName;

    long searches;

    int searchByOpenBookCounter;
    int searchByTreeCounter;
    int searchByTablebaseCounter;

    List<SearchManagerModelDetail> moveDetail = new LinkedList<>();

    static class SearchManagerModelDetail {
        String move;
        MoveType type;
    }


    @Override
    public SearchManagerModel collectStatistics(String reportTitle, List<SearchResponse> searchResponses) {
        this.searchesName = reportTitle;

        SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

        searchResponses.forEach(searchResponse -> {

            SearchManagerModelDetail searchManagerModelDetail = new SearchManagerModelDetail();

            if (searchResponse instanceof SearchByOpenBookResult) {
                searchManagerModelDetail.type = OpenBook;
                this.searchByOpenBookCounter++;
            } else if (searchResponse instanceof SearchByTablebaseResult) {
                searchManagerModelDetail.type = Tablebase;
                this.searchByTablebaseCounter++;
            } else if (searchResponse instanceof SearchByTreeResult) {
                searchManagerModelDetail.type = Tree;
                this.searchByTreeCounter++;
            }
            this.searches++;

            searchManagerModelDetail.move = simpleMoveEncoder.encode(searchResponse.move());

            this.moveDetail.add(searchManagerModelDetail);
        });

        return this;
    }
}
