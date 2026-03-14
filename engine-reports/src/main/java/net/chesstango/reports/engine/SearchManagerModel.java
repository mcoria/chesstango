package net.chesstango.reports.engine;

import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.engine.SearchByOpenBookResult;
import net.chesstango.engine.SearchByTablebaseResult;
import net.chesstango.engine.SearchByTreeResult;
import net.chesstango.engine.SearchResponse;
import net.chesstango.reports.Model;
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

    int searches;

    int searchByOpenBookCounter;
    int searchByTreeCounter;
    int searchByTablebaseCounter;
    long searchTimeTotal;

    List<SearchManagerModelDetail> moveDetail = new LinkedList<>();

    static class SearchManagerModelDetail {
        String move;
        MoveType type;
        String value;
        long searchTime;
    }


    @Override
    public SearchManagerModel collectStatistics(String reportTitle, List<SearchResponse> searchResponses) {
        this.searchesName = reportTitle;

        SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

        searchResponses.forEach(searchResponse -> {

            SearchManagerModelDetail searchManagerModelDetail = new SearchManagerModelDetail();

            if (searchResponse instanceof SearchByOpenBookResult searchByOpenBookResult) {
                searchManagerModelDetail.type = OpenBook;
                searchManagerModelDetail.value = Integer.toString(searchByOpenBookResult.polyglotEntry().weight());
                this.searchByOpenBookCounter++;
            } else if (searchResponse instanceof SearchByTablebaseResult searchByTablebaseResult) {
                searchManagerModelDetail.type = Tablebase;
                searchManagerModelDetail.value = searchByTablebaseResult.toString();
                this.searchByTablebaseCounter++;
            } else if (searchResponse instanceof SearchByTreeResult searchByTreeResult) {
                searchManagerModelDetail.type = Tree;
                SearchResult searchResult = searchByTreeResult.searchResult();
                searchManagerModelDetail.value = searchResult.getBestEvaluation().toString();
                this.searchByTreeCounter++;
            }


            searchManagerModelDetail.move = simpleMoveEncoder.encode(searchResponse.move());
            searchManagerModelDetail.searchTime = searchResponse.getTimeSearching();

            this.searchTimeTotal += searchManagerModelDetail.searchTime;
            this.searches++;

            this.moveDetail.add(searchManagerModelDetail);
        });

        return this;
    }
}
