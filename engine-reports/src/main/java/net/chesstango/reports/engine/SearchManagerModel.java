package net.chesstango.reports.engine;

import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.engine.SearchByOpenBookResult;
import net.chesstango.engine.SearchByTablebaseResult;
import net.chesstango.engine.SearchByTreeResult;
import net.chesstango.engine.SearchResponse;

import java.util.List;

/**
 *
 * @author Mauricio Coria
 */
public class SearchManagerModel {
    String searchesName;
    int searchByOpenBookCounter;
    int SearchByTablebaseCounter;
    int searchByTreeCounter;

    List<SearchManagerModelDetail> moveList;

    static class SearchManagerModelDetail {
        String move;
        String type;
    }


    public static SearchManagerModel collectStatics(String searchesName, List<SearchResponse> searchResponses) {
        SearchManagerModel model = new SearchManagerModel();
        model.searchesName = searchesName;

        SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

        searchResponses.forEach(searchResponse -> {

            SearchManagerModelDetail searchManagerModelDetail = new SearchManagerModelDetail();

            if (searchResponse instanceof SearchByOpenBookResult) {
                searchManagerModelDetail.type = "Open Book";
                model.searchByOpenBookCounter++;
            } else if (searchResponse instanceof SearchByTablebaseResult) {
                searchManagerModelDetail.type = "Tablebase";
                model.SearchByTablebaseCounter++;
            } else if (searchResponse instanceof SearchByTreeResult) {
                searchManagerModelDetail.type = "Tree";
                model.searchByTreeCounter++;
            }

            searchManagerModelDetail.move = simpleMoveEncoder.encode(searchResponse.getMove());

            model.moveList.add(searchManagerModelDetail);
        });

        return model;
    }
}
